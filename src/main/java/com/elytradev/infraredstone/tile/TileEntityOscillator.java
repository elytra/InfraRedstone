package com.elytradev.infraredstone.tile;

import com.elytradev.concrete.inventory.ConcreteItemStorage;
import com.elytradev.concrete.inventory.IContainerInventoryHolder;
import com.elytradev.concrete.inventory.ValidatedInventoryView;
import com.elytradev.infraredstone.InRedLog;
import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.BlockBase;
import com.elytradev.infraredstone.block.BlockOscillator;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.logic.IMultimeterProbe;
import com.elytradev.infraredstone.logic.InRedLogic;
import com.elytradev.infraredstone.logic.impl.InfraRedstoneHandler;

import com.google.common.base.Predicates;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
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

public class TileEntityOscillator extends TileEntityIRComponent implements ITickable, IContainerInventoryHolder, IMultimeterProbe {
    private InfraRedstoneHandler signal = new InfraRedstoneHandler();
    private int refreshTicks;
    public int maxRefreshTicks = 4;
    private int sigToWrite;
    public ConcreteItemStorage inv;

    //Transient data to throttle sync down here
    boolean lastActive = false;

    public TileEntityOscillator() {
        this.inv = new ConcreteItemStorage(0).withName(ModBlocks.OSCILLATOR.getTranslationKey() + ".name");
    }

    public void update() {
        if (world.isRemote || !hasWorld()) return;

        IBlockState state = world.getBlockState(this.getPos());

        if (InRedLogic.isIRTick()) {
            //IR tick means we're searching for a next value
            if (state.getBlock() instanceof BlockOscillator) {
                EnumFacing back = state.getValue(BlockOscillator.FACING).getOpposite();
                int sig = InRedLogic.findIRValue(world, pos, back);
                if (sig != signal.getSignalValue()) {
                    //in and out signals are different, check if it's in the middle of a refresh cycle
                    if (refreshTicks <= 0) {
                        //refresh cycle ended, set signal and grab next signal
                        signal.setNextSignalValue(sigToWrite);
                        sigToWrite = sig;
                        refreshTicks = maxRefreshTicks;
                    } else {
                        //in the middle of a cycle, keep at what it currently is
                        signal.setNextSignalValue(signal.getSignalValue());
                    }
                    refreshTicks--;
                    markDirty();
                }
            }
        } else {
            //Not an IR tick, so this is a "copy" tick. Adopt the previous tick's "next" value.
            signal.setSignalValue(signal.getNextSignalValue());
            markDirty();
        }
    }

    public void setDelay() {
        if (maxRefreshTicks >= 100) maxRefreshTicks = 100;
        if (maxRefreshTicks < 1) maxRefreshTicks = 1;
        refreshTicks = maxRefreshTicks;
        markDirty();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (facing == EnumFacing.UP || facing == EnumFacing.DOWN) return false;
        if (capability==InfraRedstone.CAPABILITY_IR) {
            if (world==null) return true;
            if (facing==null) return true;
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock()==ModBlocks.OSCILLATOR) {
                EnumFacing oscillatorFront = state.getValue(BlockOscillator.FACING);
                if (oscillatorFront==facing) {
                    return true;
                } else if (oscillatorFront==facing.getOpposite()) {
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
            if (state.getBlock()==ModBlocks.OSCILLATOR) {
                EnumFacing oscillatorFront = state.getValue(BlockOscillator.FACING);
                if (oscillatorFront==facing) {
                    return (T) signal;
                } else if (oscillatorFront==facing.getOpposite()) {
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
        //please forgive me, falk. We'll work on moving these out soon.
        tag.setInteger("NextSignal", sigToWrite);
        tag.setInteger("CurrentRefresh", refreshTicks);
        tag.setInteger("MaxRefresh", maxRefreshTicks);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("Signal")) InfraRedstone.CAPABILITY_IR.readNBT(signal, null, compound.getTag("Signal"));
        sigToWrite = compound.getInteger("NextSignal");
        refreshTicks = compound.getInteger("CurrentRefresh");
        maxRefreshTicks = compound.getInteger("MaxRefresh");
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
                if (state.getBlock()==ModBlocks.OSCILLATOR) {
                    EnumFacing facing = state.getValue(BlockOscillator.FACING);
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

    @Override
    public IInventory getContainerInventory() {
        ValidatedInventoryView view = new ValidatedInventoryView(inv);
        if(world.isRemote) {
            return view;
        }
        else {
            return view.withField(0, () -> maxRefreshTicks)
                    .withField(1, () -> (maxRefreshTicks != 10)? 1 : 0);
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
