package com.elytradev.infraredstone.tile;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.BlockDiode;
import com.elytradev.infraredstone.logic.IInfraRedstone;
import com.elytradev.infraredstone.logic.Search;
import com.elytradev.infraredstone.logic.impl.InfraRedstoneHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityDiode extends TileEntityIRComponent implements ITickable {
    public boolean active;
    private InfraRedstoneHandler signal = new InfraRedstoneHandler();

    public void update() {
        if (world.isRemote || !hasWorld()) return;
        IBlockState state = world.getBlockState(this.getPos());
        if (state.getBlock() instanceof BlockDiode) {
            EnumFacing back = EnumFacing.getHorizontal((state.getBlock()).getMetaFromState(state)).getOpposite();
            int sig = Search.forIRValue(world, pos, back);
            signal.setNextSignalValue(sig);
            if (sig != 0) {
                active = true;
            }
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
}
