package com.elytradev.infraredstone.tile;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.BlockBase;
import com.elytradev.infraredstone.block.BlockGateXor;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.logic.IMultimeterProbe;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityGateXor extends TileEntityIRComponent implements ITickable, IMultimeterProbe {
    private InfraRedstoneHandler signal = new InfraRedstoneHandler();
    private int valLeft;
    private int valRight;

    //Transient data to throttle sync down here
    boolean lastActive = false;
    int lastValLeft = 0;
    int lastValRight = 0;

    public void update() {
        if (world.isRemote || !hasWorld()) return;

        IBlockState state = world.getBlockState(this.getPos());

        if (InRedLogic.isIRTick()) {
            //IR tick means we're searching for a next value
            if (state.getBlock() instanceof BlockGateXor) {
                EnumFacing left = state.getValue(BlockGateXor.FACING).rotateYCCW();
                EnumFacing right = state.getValue(BlockGateXor.FACING).rotateY();
                int sigLeft = InRedLogic.findIRValue(world, pos, left);
                int sigRight = InRedLogic.findIRValue(world, pos, right);
                valLeft = sigLeft;
                valRight = sigRight;
                if (sigLeft > 0 && sigRight == 0) {
                    signal.setNextSignalValue(sigLeft);
                } else if (sigLeft == 0 && sigRight > 0) {
                    signal.setNextSignalValue(sigRight);
                } else {
                    signal.setNextSignalValue(0);
                }
                markDirty();
            }

        } else {
            //Not an IR tick, so this is a "copy" tick. Adopt the previous tick's "next" value.
            signal.setSignalValue(signal.getNextSignalValue());
            markDirty();
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) return false;
        if (capability==InfraRedstone.CAPABILITY_IR) {
            if (world==null) return true;
            if (facing==null) return true;
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock()==ModBlocks.GATE_XOR) {
                EnumFacing gateXorFront = state.getValue(BlockGateXor.FACING);
                if (gateXorFront==facing) {
                    return true;
                } else if (gateXorFront==facing.rotateYCCW()) {
                    return true;
                } else if (gateXorFront==facing.rotateY()) {
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
            if (state.getBlock()==ModBlocks.GATE_XOR) {
                EnumFacing gateXorFront = state.getValue(BlockGateXor.FACING);
                if (gateXorFront==facing) {
                    return (T) signal;
                } else if (gateXorFront==facing.rotateYCCW()) {
                    return (T) InfraRedstoneHandler.ALWAYS_OFF;
                } else if (gateXorFront==facing.rotateY()) {
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
        tag.setTag("Signal", InfraRedstone.CAPABILITY_IR.writeNBT(signal, null));
        //please forgive me, falk. We'll work on moving these out soon.
        tag.setInteger("Left", valLeft);
        tag.setInteger("Right", valRight);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Signal")) InfraRedstone.CAPABILITY_IR.readNBT(signal, null, compound.getTag("Signal"));
        valLeft = compound.getInteger("Left");
        valRight = compound.getInteger("Right");
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
        // please excuse the black magic
        if (!hasWorld() || getWorld().isRemote) return;

        if (isActive()!=lastActive
                ||valLeft!=lastValLeft
                || valRight!=lastValRight) { //Throttle updates - only send when something important changes

            WorldServer ws = (WorldServer)getWorld();
            Chunk c = getWorld().getChunkFromBlockCoords(getPos());
            SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
            for (EntityPlayerMP player : getWorld().getPlayers(EntityPlayerMP.class, Predicates.alwaysTrue())) {
                if (ws.getPlayerChunkMap().isPlayerWatchingChunk(player, c.x, c.z)) {
                    player.connection.sendPacket(packet);
                }
            }

            IBlockState state = world.getBlockState(pos);
            if (state.getBlock()==ModBlocks.GATE_XOR) {
                EnumFacing facing = state.getValue(BlockGateXor.FACING);
                BlockPos targetPos = pos.offset(facing);
                IBlockState targetState = world.getBlockState(targetPos);
                if (!(targetState.getBlock() instanceof BlockBase)) {
                    //Not one of ours. Update its redstone, and let observers see the fact that we updated too
                    world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1);
                    world.markAndNotifyBlock(targetPos, world.getChunkFromBlockCoords(targetPos), targetState, targetState, 3); // 1 : Just cuase a BUD and notify observers
                }
            }

            lastActive = isActive();
            lastValLeft = valLeft;
            lastValRight = valRight;

        }
    }

    public boolean isActive() {
        return signal.getSignalValue()!=0;
    }
    public boolean isLeftActive() {
        return valLeft!=0;
    }
    public boolean isRightActive() {
        return valRight!=0;
    }

    @Override
    public TextComponentString getProbeMessage() {
        TextComponentTranslation i18n = new TextComponentTranslation("msg.inred.multimeter.out");
        return new TextComponentString(i18n.getFormattedText()+getValue());
    }

    private String getValue() {
        int signal = this.signal.getSignalValue();
        int bit1 = ((signal & 0b00_0001) != 0) ? 1:0;
        int bit2 = ((signal & 0b00_0010) != 0) ? 1:0;
        int bit3 = ((signal & 0b00_0100) != 0) ? 1:0;
        int bit4 = ((signal & 0b00_1000) != 0) ? 1:0;
        int bit5 = ((signal & 0b01_0000) != 0) ? 1:0;
        int bit6 = ((signal & 0b10_0000) != 0) ? 1:0;
        return ": "+signal+" ("+bit6+bit5+"_"+bit4+bit3+bit2+bit1+")";
    }
}
