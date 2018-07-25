package com.elytradev.infraredstone.logic.impl;

import com.elytradev.infraredstone.logic.IEncoderScannable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * Don't use. Comparator readers don't store any data.
 */
public class InfraComparatorSerializer implements Capability.IStorage<IEncoderScannable>{
	@Override
	public NBTBase writeNBT(Capability<IEncoderScannable> capability, IEncoderScannable instance, EnumFacing side) {
		return null;
	}

	@Override
	public void readNBT(Capability<IEncoderScannable> capability, IEncoderScannable instance, EnumFacing side, NBTBase nbt) {
	}
}
