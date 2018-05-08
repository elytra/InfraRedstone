package com.elytradev.infraredstone.logic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Version of IInfraComparator that can be implemented by non-TE Blocks.
 */
public interface ISimpleInfraComparator {
	int getComparatorValue(World world, BlockPos pos, IBlockState state, EnumFacing inspectingFrom);
}
