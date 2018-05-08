package com.elytradev.infraredstone.logic.impl;

import com.elytradev.infraredstone.logic.IInfraRedstone;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class InfraRedstoneSerializer implements Capability.IStorage<IInfraRedstone> {

	@Override
	public NBTBase writeNBT(Capability<IInfraRedstone> capability, IInfraRedstone instance, EnumFacing side) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("SignalValue", instance.getSignalValue());
		if (instance instanceof IInfraRedstone) {
			tag.setInteger("NextSignalValue", ((InfraRedstoneHandler)instance).getNextSignalValue());
		}
		
		return tag;
	}

	@Override
	public void readNBT(Capability<IInfraRedstone> capability, IInfraRedstone instance, EnumFacing side, NBTBase nbt) {
		if(instance instanceof InfraRedstoneHandler) {
			if (!(nbt instanceof NBTTagCompound)) return;
			NBTTagCompound tag = (NBTTagCompound)nbt;
			
			((InfraRedstoneHandler)instance).setSignalValue(tag.getInteger("SignalValue"));
			((InfraRedstoneHandler)instance).setNextSignalValue(tag.getInteger("NextSignalValue"));
		}
	}

}
