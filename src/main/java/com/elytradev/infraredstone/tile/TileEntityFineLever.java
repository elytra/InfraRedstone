package com.elytradev.infraredstone.tile;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.BlockFineLever;
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
import net.minecraft.util.SoundCategory;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityFineLever extends TileEntityIRComponent {
    public boolean active = false;
    private InfraRedstoneHandler signal = new InfraRedstoneHandler();

    //Transient data to throttle sync down here
    boolean lastActive = false;

    public void toggleState() {
        if (active) {
            active = false;
            signal.setSignalValue(0);
            world.setBlockState(this.getPos(), world.getBlockState(pos).withProperty(BlockFineLever.ACTIVE, false));
            world.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3f, 0.5f);
        } else {
            active = true;
            signal.setSignalValue(15);
            world.setBlockState(this.getPos(), world.getBlockState(pos).withProperty(BlockFineLever.ACTIVE, true));
            world.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3f, 0.6f);
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    	if (capability==InfraRedstone.CAPABILITY_IR) return true;
    	
    	return super.hasCapability(capability, facing);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    	if (capability==InfraRedstone.CAPABILITY_IR) return (T) signal;
    	
    	return super.getCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound tag = super.writeToNBT(compound);
        tag.setBoolean("Active", active);
        tag.setTag("Signal", InfraRedstone.CAPABILITY_IR.writeNBT(signal, null));
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        active = compound.getBoolean("active");
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

        if (active!=lastActive) { //Throttle updates - only send when something important changes

            WorldServer ws = (WorldServer)getWorld();
            Chunk c = getWorld().getChunkFromBlockCoords(getPos());
            SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
            for (EntityPlayerMP player : getWorld().getPlayers(EntityPlayerMP.class, Predicates.alwaysTrue())) {
                if (ws.getPlayerChunkMap().isPlayerWatchingChunk(player, c.x, c.z)) {
                    player.connection.sendPacket(packet);
                }
            }

            lastActive = active;

            IBlockState state = world.getBlockState(pos);
            ws.markAndNotifyBlock(pos, c, state, state, 1 | 16);
        }
    }
}
