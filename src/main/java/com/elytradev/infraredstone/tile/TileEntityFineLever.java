package com.elytradev.infraredstone.tile;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.BlockFineLever;
import com.elytradev.infraredstone.logic.impl.InfraRedstoneHandler;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityFineLever extends TileEntityIRComponent {
    public boolean active = false;
    private InfraRedstoneHandler signal = new InfraRedstoneHandler();

    public void toggleState() {
        if (active) {
            active = false;
            signal.setSignalValue(0);
            world.setBlockState(this.getPos(), world.getBlockState(pos).withProperty(BlockFineLever.ACTIVE, false));
        } else {
            active = true;
            signal.setSignalValue(63);
            world.setBlockState(this.getPos(), world.getBlockState(pos).withProperty(BlockFineLever.ACTIVE, true));
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
