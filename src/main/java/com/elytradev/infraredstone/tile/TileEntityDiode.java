package com.elytradev.infraredstone.tile;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.BlockDiode;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.logic.InRedLogic;
import com.elytradev.infraredstone.logic.impl.InfraRedstoneHandler;

import com.google.common.base.Predicates;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityDiode extends TileEntityIRComponent implements ITickable {
    private InfraRedstoneHandler signal = new InfraRedstoneHandler();
    private int resistance = 0;
    
    //Transient data to throttle sync down here
    boolean lastActive = false;
    int lastResistance = 0;

    public void update() {
        if (world.isRemote || !hasWorld()) return;
        
        IBlockState state = world.getBlockState(this.getPos());
        
        if (InRedLogic.isIRTick()) {
        	//IR tick means we're searching for a next value
            if (state.getBlock() instanceof BlockDiode) {
                EnumFacing back = state.getValue(BlockDiode.FACING).getOpposite();
                int sig = InRedLogic.findIRValue(world, pos, back);
                if (sig >= resistance) {
                	signal.setNextSignalValue(sig);
                }  else {
                	signal.setNextSignalValue(0);
                }
                markDirty();
            }
        } else {
        	//Not an IR tick, so this is a "copy" tick. Adopt the previous tick's "next" value.
        	signal.setSignalValue(signal.getNextSignalValue());
        	markDirty();
        	//setActive(state, signal.getSignalValue()!=0); //This is also when we light up
        }
    }

    public void cycleResistance() {
    	resistance += 16;
    	if (resistance>=64) resistance = 0;
    	markDirty();
    	//world.setBlockState(pos, world.getBlockState(pos), 3 | 16); //Don't change the blockstate, but *send an update* to the client and prevent observers from caring
	}
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    	if (capability==InfraRedstone.CAPABILITY_IR) {
    		if (world==null) return true;
    		if (facing==null) return true;
    		IBlockState state = world.getBlockState(pos);
    		if (state.getBlock()==ModBlocks.DIODE) {
    			EnumFacing diodeFront = state.getValue(BlockDiode.FACING);
    			if (diodeFront==facing) {
    				return true;
    			} else if (diodeFront==facing.getOpposite()) {
    				return true;
    			} else {
    				return false;
    			}
    		}
    		
    		return false;
    	}
    	
    	return super.hasCapability(capability, facing);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    	if (capability==InfraRedstone.CAPABILITY_IR) {
    		if (world==null) return (T)InfraRedstoneHandler.ALWAYS_OFF;
    		if (facing==null) return (T) signal;
    		
    		IBlockState state = world.getBlockState(pos);
    		if (state.getBlock()==ModBlocks.DIODE) {
    			EnumFacing diodeFront = state.getValue(BlockDiode.FACING);
    			if (diodeFront==facing) {
    				return (T) signal;
    			} else if (diodeFront==facing.getOpposite()) {
    				return (T)InfraRedstoneHandler.ALWAYS_OFF;
    			} else {
    				return null;
    			}
    		}
    		return (T)InfraRedstoneHandler.ALWAYS_OFF; //We can't tell what our front face is, so supply a dummy that's always-off.
    	}
    	
    	return super.getCapability(capability, facing);
    }

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagCompound tag = super.writeToNBT(compound);
		tag.setInteger("Resistance", resistance);
		tag.setTag("Signal", InfraRedstone.CAPABILITY_IR.writeNBT(signal, null));
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		resistance = compound.getInteger("Resistance");
		if (compound.hasKey("Signal")) InfraRedstone.CAPABILITY_IR.readNBT(signal, null, compound.getTag("Signal"));
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		readFromNBT(tag);
		IBlockState state = world.getBlockState(pos);
		getWorld().markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2 | 16);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		handleUpdateTag(pkt.getNbtCompound());
	}

	@Override
	public void markDirty() {
		super.markDirty();
		// again, I've copy-pasted this like 12 times, should probably go into Concrete
		if (!hasWorld() || getWorld().isRemote) return;
		
		if (resistance!=lastResistance || isActive()!=lastActive) { //Throttle updates - only send when something important changes
		
			WorldServer ws = (WorldServer)getWorld();
			Chunk c = getWorld().getChunkFromBlockCoords(getPos());
			SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
			for (EntityPlayerMP player : getWorld().getPlayers(EntityPlayerMP.class, Predicates.alwaysTrue())) {
				if (ws.getPlayerChunkMap().isPlayerWatchingChunk(player, c.x, c.z)) {
					player.connection.sendPacket(packet);
				}
			}
			
			lastResistance = resistance;
			lastActive = isActive();
			
			IBlockState state = world.getBlockState(pos);
			ws.markAndNotifyBlock(pos, c, state, state, 1 | 16);
		}
	}
    
	/*
    public void setActive(IBlockState existing, boolean active) {
    	//if (existing.getValue(BlockDiode.ACTIVE)==active) return;
    	world.setBlockState(pos, world.getBlockState(pos), 3 | 16); //Don't change the blockstate, but *send an update* to the client and prevent observers from caring
		//world.setBlockState(pos, existing.withProperty(BlockDiode.ACTIVE, active));
    	System.out.println("Set to "+active);
    }*/

    public int getResistance() {
    	return resistance;
	}

	public int getMark() {
    	return (int)(resistance * 4/64f);
	}
	
	public boolean isActive() {
		return signal.getSignalValue()!=0;
	}
}
