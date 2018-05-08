package com.elytradev.infraredstone.logic.impl;

import com.elytradev.infraredstone.logic.IInfraComparator;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Don't use. Comparator readers don't store any data.
 */
public class InfraComparatorSerializer implements Capability.IStorage<IInfraComparator>{
	@Override
	public NBTBase writeNBT(Capability<IInfraComparator> capability, IInfraComparator instance, EnumFacing side) {
		return null;
	}

	@Override
	public void readNBT(Capability<IInfraComparator> capability, IInfraComparator instance, EnumFacing side, NBTBase nbt) {
	}
}
