package com.elytradev.infraredstone.logic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * <p>This Capability describes an IR signal value to other InfraRedstone logic tiles.
 * <p>Either implement this interface on your Block, or provide it as a Capability on your TE.
 */
public interface IInfraRedstone {
	/**
	 * Get the signal value that this block is providing to an adjacent block looking at this block's {@code inspectingFrom} side.
	 */
	int getSignalValue(World world, BlockPos pos, IBlockState state, EnumFacing inspectingFrom);
}
