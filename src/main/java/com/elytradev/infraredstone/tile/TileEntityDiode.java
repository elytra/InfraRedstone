package com.elytradev.infraredstone.tile;

import com.elytradev.infraredstone.InRedLog;
import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.BlockDiode;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.logic.InRedLogic;
import com.elytradev.infraredstone.logic.impl.InfraRedstoneHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityDiode extends TileEntityIRComponent implements ITickable {
    private InfraRedstoneHandler signal = new InfraRedstoneHandler();
    private int resistance = 0;
    private int cycle = 0;

    public void update() {
        if (world.isRemote || !hasWorld()) return;
        
        IBlockState state = world.getBlockState(this.getPos());
        
        if (InRedLogic.isIRTick()) {
        	//IR tick means we're searching for a next value
            if (state.getBlock() instanceof BlockDiode) {
                EnumFacing back = state.getValue(BlockDiode.FACING).getOpposite();
                int sig = InRedLogic.findIRValue(world, pos, back);
                if (sig >= resistance) signal.setNextSignalValue(sig);
                else signal.setNextSignalValue(0);
            }
        } else {
        	//Not an IR tick, so this is a "copy" tick. Adopt the previous tick's "next" value.
        	signal.setSignalValue(signal.getNextSignalValue());
        	setActive(state, signal.getSignalValue()!=0); //This is also when we light up
        }
    }

    public void cycleDiodeTemp() {
    	cycle++;
    	if (cycle >= 4) cycle = 0;
    	resistance = 16*cycle;
		world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockDiode.MARK, cycle));
		InRedLog.info(cycle);
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
    
    
    public void setActive(IBlockState existing, boolean active) {
    	if (existing.getValue(BlockDiode.ACTIVE)==active) return;
		world.setBlockState(pos, existing.withProperty(BlockDiode.ACTIVE, active));
    }

    public int getResistance() {
    	return resistance;
	}

	public int getMark() {
    	return cycle;
	}
	
	public boolean isActive() {
		return signal.getSignalValue()!=0;
	}
}
