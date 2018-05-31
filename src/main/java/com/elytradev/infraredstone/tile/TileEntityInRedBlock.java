package com.elytradev.infraredstone.tile;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.BlockInfraRedstone;
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

public class TileEntityInRedBlock extends TileEntityIRComponent {
    private InfraRedstoneHandler signal = new InfraRedstoneHandler();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    	if (capability==InfraRedstone.CAPABILITY_IR) return true;
    	
    	return super.hasCapability(capability, facing);
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
    	if (capability==InfraRedstone.CAPABILITY_IR) {
    	    signal.setSignalValue(63);
    	    return (T) signal;
    	}
    	
    	return super.getCapability(capability, facing);
    }
}
