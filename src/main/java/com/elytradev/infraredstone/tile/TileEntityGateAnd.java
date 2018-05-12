package com.elytradev.infraredstone.tile;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.BlockGateAnd;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.logic.InRedLogic;
import com.elytradev.infraredstone.logic.impl.InfraRedstoneHandler;

import com.google.common.base.Predicates;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityGateAnd extends TileEntityIRComponent implements ITickable {
    private InfraRedstoneHandler signal = new InfraRedstoneHandler();
    private int valLeft = 0;
    private int valRight = 0;
    private boolean inverted;

    //Transient data to throttle sync down here
    boolean lastActive = false;
    int lastValLeft = 0;
    int lastValRight = 0;
    boolean lastInvert = false;

    public void update() {
        if (world.isRemote || !hasWorld()) return;

        IBlockState state = world.getBlockState(this.getPos());

        if (InRedLogic.isIRTick()) {
            //IR tick means we're searching for a next value
            if (state.getBlock() instanceof BlockGateAnd) {
                EnumFacing left = state.getValue(BlockGateAnd.FACING).rotateYCCW();
                EnumFacing right = state.getValue(BlockGateAnd.FACING).rotateY();
                int sigLeft = InRedLogic.findIRValue(world, pos, left);
                int sigRight = InRedLogic.findIRValue(world, pos, right);
                valLeft = sigLeft;
                valRight = sigRight;
                if (!inverted) {
                    if (sigLeft > 0 && sigRight > 0) {
                        if (sigLeft == sigRight) {
                            signal.setNextSignalValue(sigLeft);
                        } else if (sigLeft > sigRight) {
                            signal.setNextSignalValue(sigLeft);
                        } else {
                            signal.setNextSignalValue(sigRight);
                        }
                    } else {
                        signal.setNextSignalValue(0);
                    }
                } else {
                    if (sigLeft > 0 && sigRight > 0) {
                        signal.setNextSignalValue(0);
                    } else {
                        signal.setNextSignalValue(63);
                    }
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

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability==InfraRedstone.CAPABILITY_IR) {
            if (world==null) return true;
            if (facing==null) return true;
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock()==ModBlocks.GATE_AND) {
                EnumFacing gateAndFront = state.getValue(BlockGateAnd.FACING);
                if (gateAndFront==facing) {
                    return true;
                } else if (gateAndFront==facing.rotateYCCW()) {
                    return true;
                } else if (gateAndFront==facing.rotateY()) {
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
            if (state.getBlock()==ModBlocks.GATE_AND) {
                EnumFacing gateAndFront = state.getValue(BlockGateAnd.FACING);
                if (gateAndFront==facing) {
                    return (T) signal;
                } else if (gateAndFront==facing.rotateYCCW()) {
                    return (T)InfraRedstoneHandler.ALWAYS_OFF;
                } else if (gateAndFront==facing.rotateY()) {
                    return (T)InfraRedstoneHandler.ALWAYS_OFF;
                } else {
                    return null;
                }
            }
            return (T)InfraRedstoneHandler.ALWAYS_OFF; //We can't tell what our front face is, so supply a dummy that's always-off.
        }

        return super.getCapability(capability, facing);
    }

    public void toggleInvert() {
        if (inverted) {
            inverted = false;
            signal.setSignalValue(0);
            world.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3f, 0.5f);
        } else {
            inverted = true;
            signal.setSignalValue(15);
            world.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3f, 0.55f);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound tag = super.writeToNBT(compound);
        tag.setTag("Signal", InfraRedstone.CAPABILITY_IR.writeNBT(signal, null));
        tag.setInteger("Left", valLeft);
        tag.setInteger("Right", valRight);
        tag.setBoolean("Inverted", inverted);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Signal")) InfraRedstone.CAPABILITY_IR.readNBT(signal, null, compound.getTag("Signal"));
        valLeft = compound.getInteger("Left");
        valRight = compound.getInteger("Right");
        inverted = compound.getBoolean("Inverted");
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

        if (valLeft!=lastValLeft || valRight != lastValRight || isActive()!=lastActive || inverted != lastInvert) { //Throttle updates - only send when something important changes

            WorldServer ws = (WorldServer)getWorld();
            Chunk c = getWorld().getChunkFromBlockCoords(getPos());
            SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
            for (EntityPlayerMP player : getWorld().getPlayers(EntityPlayerMP.class, Predicates.alwaysTrue())) {
                if (ws.getPlayerChunkMap().isPlayerWatchingChunk(player, c.x, c.z)) {
                    player.connection.sendPacket(packet);
                }
            }

            lastValLeft = valLeft;
            lastValRight = valRight;
            lastActive = isActive();
            lastInvert = inverted;

            IBlockState state = world.getBlockState(pos);
            ws.markAndNotifyBlock(pos, c, state, state, 1 | 16);
        }
    }
    
	/*
    public void setActive(IBlockState existing, boolean active) {
    	//if (existing.getValue(BlockGateAnd.ACTIVE)==active) return;
    	world.setBlockState(pos, world.getBlockState(pos), 3 | 16); //Don't change the blockstate, but *send an update* to the client and prevent observers from caring
		//world.setBlockState(pos, existing.withProperty(BlockGateAnd.ACTIVE, active));
    	System.out.println("Set to "+active);
    }*/

    public boolean isActive() {
        return signal.getSignalValue()!=0;
    }
    public boolean isLeftActive() {
        return valLeft!=0;
    }
    public boolean isRightActive() {
        return valRight!=0;
    }
}
