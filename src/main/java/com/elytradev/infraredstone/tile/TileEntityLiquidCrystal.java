package com.elytradev.infraredstone.tile;

import java.util.Arrays;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.BlockLiquidCrystal;
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

public class TileEntityLiquidCrystal extends TileEntityIRComponent implements ITickable {
    private int[] lastFaceCardinality = new int[6];
    private int[] faceCardinality = new int[6];
    
    public float[] animationProgress = new float[6];

    @Override
    public void update() {
        if (!hasWorld()) return;
        boolean blockingLight = false;
        for (int i = 0; i < animationProgress.length; i++) {
            int cardinality = faceCardinality[i];
            if (cardinality > 0) {
                animationProgress[i] += 0.025f*cardinality;
            } else {
                animationProgress[i] -= 0.05f;
            }
            animationProgress[i] = Math.min(1, Math.max(animationProgress[i], 0));
            if (animationProgress[i] > 0.15f) blockingLight = true;
        }
        if (world.isRemote) return;
        IBlockState state = world.getBlockState(this.getPos());

        if (InRedLogic.isIRTick()) {
            if (state.getBlock() instanceof BlockLiquidCrystal) {
                for (EnumFacing face : EnumFacing.VALUES) {
                    if (face == EnumFacing.DOWN) continue;
                    int sig = InRedLogic.findIRValue(world, pos, face);
                    faceCardinality[face.ordinal()] = Integer.bitCount(sig);
                }
                getWorld().setBlockState(getPos(), state.withProperty(BlockLiquidCrystal.BLOCKING_LIGHT, blockingLight));
                markDirty();
            }

        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == null) return false;
        if (capability==InfraRedstone.CAPABILITY_IR) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == null) return null;
        if (capability==InfraRedstone.CAPABILITY_IR) {
            return (T)InfraRedstoneHandler.ALWAYS_OFF;
        }
        return super.getCapability(capability, facing);
    }

    public int getFaceCardinality(EnumFacing face) {
        return face == null ? 0 : faceCardinality[face.ordinal()];
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        writeToNBTInternal(nbt);
        return super.writeToNBT(nbt);
    }
    
    private NBTTagCompound writeToNBTInternal(NBTTagCompound nbt) {
        nbt.setIntArray("FaceCardinality", faceCardinality);
        return nbt;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        readFromNBTInternal(nbt);
        super.readFromNBT(nbt);
    }

    private void readFromNBTInternal(NBTTagCompound nbt) {
        if (nbt.getIntArray("FaceCardinality").length == 6) {
            faceCardinality = nbt.getIntArray("FaceCardinality");
        }
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBTInternal(new NBTTagCompound());
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
    }
    
    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
       readFromNBTInternal(tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public void markDirty() {
        super.markDirty();
        // please excuse the black magic
        if (!hasWorld() || getWorld().isRemote) return;

        if (!Arrays.equals(faceCardinality, lastFaceCardinality)) { //Throttle updates - only send when something important changes
            for (int i = 0; i < lastFaceCardinality.length; i++) {
                lastFaceCardinality[i] = faceCardinality[i];
            }
            
            WorldServer ws = (WorldServer)getWorld();
            Chunk c = getWorld().getChunkFromBlockCoords(getPos());
            SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
            for (EntityPlayerMP player : getWorld().getPlayers(EntityPlayerMP.class, Predicates.alwaysTrue())) {
                if (ws.getPlayerChunkMap().isPlayerWatchingChunk(player, c.x, c.z)) {
                    player.connection.sendPacket(packet);
                }
            }
        }
    }
    
    @Override
    public boolean hasFastRenderer() {
        return true;
    }
    
}
