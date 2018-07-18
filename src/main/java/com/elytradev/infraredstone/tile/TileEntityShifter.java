package com.elytradev.infraredstone.tile;

import com.elytradev.infraredstone.InRedLog;
import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.BlockBase;
import com.elytradev.infraredstone.block.BlockShifter;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.logic.IMultimeterProbe;
import com.elytradev.infraredstone.logic.InRedLogic;
import com.elytradev.infraredstone.logic.impl.InfraRedstoneHandler;

import com.elytradev.infraredstone.util.enums.EnumShifterSelection;
import com.google.common.base.Predicates;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityShifter extends TileEntityIRComponent implements ITickable, IMultimeterProbe {
    private InfraRedstoneHandler signal = new InfraRedstoneHandler();
    private InfraRedstoneHandler eject = new InfraRedstoneHandler();
    public EnumShifterSelection selection = EnumShifterSelection.LEFT;

    //Transient data to throttle sync down here
    boolean lastActive = false;
    boolean lastEject = false;
    EnumShifterSelection lastSelection = EnumShifterSelection.LEFT;

    public void update() {
        if (world.isRemote || !hasWorld()) return;

        IBlockState state = world.getBlockState(this.getPos());

        if (InRedLogic.isIRTick()) {
            //IR tick means we're searching for a next value
            if (state.getBlock() instanceof BlockShifter) {
                EnumFacing back = state.getValue(BlockShifter.FACING).getOpposite();
                int sig = InRedLogic.findIRValue(world, pos, back);
                int ej = 0;
                
                if (selection == EnumShifterSelection.LEFT) {
                    ej = (sig & 0b10_0000);
                    ej = (ej != 0) ? 1 : 0;
                    sig <<= 1;
                    sig &= 0b011_1111;
                } else {
                    ej = (sig & 0b00_0001);
                    ej = (ej != 0) ? 1 : 0;
                    sig >>>= 1;
                    sig &= 0b011_1111;
                }
                
                signal.setNextSignalValue(sig);
                eject.setNextSignalValue(ej);
                markDirty();
            }

        } else {
            //Not an IR tick, so this is a "copy" tick. Adopt the previous tick's "next" value.
            signal.setSignalValue(signal.getNextSignalValue());
            eject.setSignalValue(eject.getNextSignalValue());
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
            if (state.getBlock()==ModBlocks.SHIFTER) {
                EnumFacing shifterFront = state.getValue(BlockShifter.FACING);
                if (shifterFront==facing) {
                    return true;
                } else if (shifterFront.rotateYCCW()==facing) {
                    return selection == EnumShifterSelection.LEFT;
                } else if (shifterFront.rotateY()==facing) {
                    return selection == EnumShifterSelection.RIGHT;
                } else if (shifterFront.getOpposite()==facing) {
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
            if (state.getBlock()==ModBlocks.SHIFTER) {
                EnumFacing shifterFront = state.getValue(BlockShifter.FACING);
                if (shifterFront==facing) {
                    return (T) signal;
                } else if (shifterFront.rotateYCCW()==facing) {
                    if (selection == EnumShifterSelection.LEFT) return (T) eject;
                    else return (T) InfraRedstoneHandler.ALWAYS_OFF;
                } else if (shifterFront.rotateY()==facing) {
                    if (selection == EnumShifterSelection.RIGHT) return (T) eject;
                    else return (T) InfraRedstoneHandler.ALWAYS_OFF;
                } else if (shifterFront.getOpposite()==facing) {
                    return (T)InfraRedstoneHandler.ALWAYS_OFF;
                } else {
                    return null;
                }
            }
            return (T)InfraRedstoneHandler.ALWAYS_OFF; //We can't tell what our front face is, so supply a dummy that's always-off.
        }

        return super.getCapability(capability, facing);
    }

    public void toggleSelection() {
        if (selection == EnumShifterSelection.LEFT) {
            selection = EnumShifterSelection.RIGHT;
            world.playSound(null, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3f, 0.55f);
        } else {
            selection = EnumShifterSelection.LEFT;
            world.playSound(null, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3f, 0.55f);
        }
        eject.setSignalValue(0);
        markDirty();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound tag = super.writeToNBT(compound);
        tag.setTag("Signal", InfraRedstone.CAPABILITY_IR.writeNBT(signal, null));
        tag.setTag("Eject", InfraRedstone.CAPABILITY_IR.writeNBT(eject, null));
        tag.setString("Selection", selection.getName());
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Signal")) InfraRedstone.CAPABILITY_IR.readNBT(signal, null, compound.getTag("Signal"));
        if (compound.hasKey("Eject")) InfraRedstone.CAPABILITY_IR.readNBT(eject, null, compound.getTag("Eject"));
        selection = EnumShifterSelection.forName(compound.getString("Selection"));
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
        if (lastSelection!=selection) {
            world.markBlockRangeForRenderUpdate(pos, pos);
            lastSelection = selection;
        }
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
        boolean active = isActive();
        boolean eject = isEject();
        if (isActive()!=lastActive || isEject() != lastEject || selection!=lastSelection) { //Throttle updates - only send when something important changes

            WorldServer ws = (WorldServer)getWorld();
            Chunk c = getWorld().getChunk(getPos());
            SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
            for (EntityPlayerMP player : getWorld().getPlayers(EntityPlayerMP.class, Predicates.alwaysTrue())) {
                if (ws.getPlayerChunkMap().isPlayerWatchingChunk(player, c.x, c.z)) {
                    player.connection.sendPacket(packet);
                }
            }

            if (lastSelection!=selection) {
                //IBlockState state = world.getBlockState(pos);
                //ws.markAndNotifyBlock(pos, c, state, state, 1 | 2 | 16);
            } else if (lastActive!=active || lastEject!= eject) {
                //BlockState isn't changing, but we need to notify the block in front of us so that vanilla redstone updates
                IBlockState state = world.getBlockState(pos);
                if (state.getBlock()==ModBlocks.SHIFTER) {
                    EnumFacing facing = state.getValue(BlockShifter.FACING);
                    BlockPos targetPos = pos.offset(facing);
                    IBlockState targetState = world.getBlockState(targetPos);
                    if (!(targetState.getBlock() instanceof BlockBase)) {
                        //Not one of ours. Update its redstone, and let observers see the fact that we updated too
                        world.markAndNotifyBlock(pos, world.getChunk(pos), state, state, 1);
                        world.markAndNotifyBlock(targetPos, world.getChunk(targetPos), targetState, targetState, 3); // 1 : Just cuase a BUD and notify observers
                    }
                }
            }

            lastActive = isActive();
            lastEject = isEject();
            lastSelection = selection;

            IBlockState state = world.getBlockState(pos);
            ws.markAndNotifyBlock(pos, c, state, state, 1 | 16);
        }
    }

    public boolean isActive() {
        return signal.getSignalValue()!=0;
    }
    public boolean isEject() {
        return eject.getSignalValue()!=0;
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
        return ": 0b"+bit6+bit5+"_"+bit4+bit3+bit2+bit1+" ("+signal+")";
    }
}
