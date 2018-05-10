package com.elytradev.infraredstone.logic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Version of IInfraRedstone that can be implemented by non-TE Blocks.
 */
public interface ISimpleInfraRedstone {
	int getSignalValue(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing inspectingFrom);
	boolean canConnectIR(IBlockAccess world, BlockPos pos, IBlockState state, EnumFacing inspectingFrom);
}
