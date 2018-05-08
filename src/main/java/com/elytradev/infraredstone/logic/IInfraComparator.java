package com.elytradev.infraredstone.logic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * <p>This Capability describes an "inspection" signal value to InfraComparators.
 * <p>Either implement this interface on your Block, or provide it as a Capability on your TE.
 */
public interface IInfraComparator {
	/**
	 * Get the value that an InfraComparator will see if it's looking at the block's {@code inspectingFrom} side.
	 */
	int getComparatorValue(World world, BlockPos pos, IBlockState state, EnumFacing inspectingFrom);
}
