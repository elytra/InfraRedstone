package com.elytradev.infraredstone.tile;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.BlockBase;
import com.elytradev.infraredstone.block.BlockEncoder;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.api.IEncoderScannable;
import com.elytradev.infraredstone.api.IMultimeterProbe;
import com.elytradev.infraredstone.api.ISimpleEncoderScannable;
import com.elytradev.infraredstone.logic.InRedLogic;
import com.elytradev.infraredstone.logic.impl.InfraRedstoneHandler;
import com.google.common.base.Predicates;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityEncoder extends TileEntityIRComponent implements ITickable, IMultimeterProbe {
    private InfraRedstoneHandler signal = new InfraRedstoneHandler();

    //Transient data to throttle sync down here
    boolean lastActive = false;

    public void update() {
        if (world.isRemote || !hasWorld()) return;

        IBlockState state = world.getBlockState(this.getPos());

        if (InRedLogic.isIRTick()) {
            // IR tick means we're searching for a next value
            if (state.getBlock() instanceof BlockEncoder) {
                EnumFacing back = state.getValue(BlockEncoder.FACING).getOpposite();
                BlockPos backPos = this.getPos().offset(back);
                IBlockState quantify = world.getBlockState(backPos);
                // check for the main encoder API
                if (quantify instanceof IEncoderScannable) {
                    signal.setNextSignalValue(((IEncoderScannable) quantify).getComparatorValue());
                    // make sure we don't hit the if later down
                    markDirty();
                    return;
                    // check for the non-TE encoder API
                } else if (quantify instanceof ISimpleEncoderScannable) {
                    signal.setNextSignalValue(((ISimpleEncoderScannable) quantify).getComparatorValue(world, backPos, quantify, back.getOpposite()));
                    markDirty();
                    return;
                    // no encoder API, so check for a tile entity
                } else if (world.getTileEntity(backPos) != null) {
                    TileEntity te = world.getTileEntity(backPos);
                    // check for capabilities on the tile entity, make sure we only move on if we don't find any
                    if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, back.getOpposite())) {
                        IItemHandler inv = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, back);
                        signal.setNextSignalValue(getInventoryCapacity(inv));
                        markDirty();
                        return;
                    }  if (te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, back.getOpposite())) {
                        IFluidHandler fluid = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, back);
                        IFluidTankProperties[] props = fluid.getTankProperties();
                        for (IFluidTankProperties prop : props) {
                            if (prop.getContents() == null) signal.setNextSignalValue(0b00_0000);
                            else signal.setNextSignalValue(((prop.getContents().amount/prop.getCapacity()) * 62) + 1);
                        }
                        markDirty();
                        return;
                    } if (te.hasCapability(CapabilityEnergy.ENERGY, back.getOpposite())) {
                        IEnergyStorage energy = te.getCapability(CapabilityEnergy.ENERGY, back);
                        if (energy.getEnergyStored() == 0) signal.setNextSignalValue(0b00_0000);
                        else signal.setNextSignalValue(((energy.getEnergyStored()/energy.getMaxEnergyStored()) * 62) + 1);
                        markDirty();
                        return;
                    }
                    // this if is the reason for all the markDirty(); return; garbage up above
                    // check for a vanilla comparator interface
                } if (quantify.hasComparatorInputOverride()) {
                    signal.setNextSignalValue(4*quantify.getComparatorInputOverride(world, backPos));
                    // can't find anything else, so check for redstone/inred signal
                } else {
                    // redstone first so inred's redstone-catching doesn't override it
                    int sigBack = world.getRedstonePower(backPos, back);
                    if (sigBack != 0) {
                        signal.setNextSignalValue(sigBack);
                    } else {
                        signal.setNextSignalValue(InRedLogic.findIRValue(world, pos, back));
                    }
                }

                markDirty();
            }
        } else {
        // Not an IR tick, so this is a "copy" tick. Adopt the previous tick's "next" value.
        signal.setSignalValue(signal.getNextSignalValue());
        markDirty();
    }
    }

    private int getInventoryCapacity(IItemHandler inv) {
        int stacks = 0;
        float percent = 0.0F;

        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);

            if (!stack.isEmpty()) {
                percent += (float)stack.getCount() / (float)Math.min(inv.getSlotLimit(i), stack.getMaxStackSize());
                stacks++;
            }
        }

        percent /= (float)inv.getSlots();
        return MathHelper.floor(percent * 62.0F) + (stacks > 0 ? 1 : 0);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) return false;
        if (capability==InfraRedstone.CAPABILITY_IR) {
            if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) return false;
            if (world==null) return true;
            if (facing==null) return true;
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock()==ModBlocks.ENCODER) {
                EnumFacing encoderFront = state.getValue(BlockEncoder.FACING);
                if (encoderFront==facing) {
                    return true;
                } else if (encoderFront==facing.getOpposite()) {
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
            if (state.getBlock()==ModBlocks.ENCODER) {
                EnumFacing encoderFront = state.getValue(BlockEncoder.FACING);
                if (encoderFront==facing) {
                    return (T) signal;
                } else if (encoderFront==facing.getOpposite()) {
                    return (T) InfraRedstoneHandler.ALWAYS_OFF;
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
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
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
        getWorld().markAndNotifyBlock(pos, world.getChunk(pos), state, state, 1 | 2 | 16);
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
        boolean active = isActive();
        if (active!=lastActive) { //Throttle updates - only send when something important changes

            WorldServer ws = (WorldServer)getWorld();
            Chunk c = getWorld().getChunk(getPos());
            SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
            for (EntityPlayerMP player : getWorld().getPlayers(EntityPlayerMP.class, Predicates.alwaysTrue())) {
                if (ws.getPlayerChunkMap().isPlayerWatchingChunk(player, c.x, c.z)) {
                    player.connection.sendPacket(packet);
                }
            }

            if (lastActive!=active) {
                //BlockState isn't changing, but we need to notify the block in front of us so that vanilla redstone updates
                IBlockState state = world.getBlockState(pos);
                if (state.getBlock()==ModBlocks.ENCODER) {
                    EnumFacing facing = state.getValue(BlockEncoder.FACING);
                    BlockPos targetPos = pos.offset(facing);
                    IBlockState targetState = world.getBlockState(targetPos);
                    if (!(targetState.getBlock() instanceof BlockBase)) {
                        //Not one of ours. Update its redstone, and let observers see the fact that we updated too
                        world.markAndNotifyBlock(pos, world.getChunk(pos), state, state, 1);
                        world.markAndNotifyBlock(targetPos, world.getChunk(targetPos), targetState, targetState, 3); // 1 : Just cuase a BUD and notify observers
                    }
                }
            }

            lastActive = active;
        }
    }

    public boolean isActive() {
        return signal.getSignalValue()!=0;
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