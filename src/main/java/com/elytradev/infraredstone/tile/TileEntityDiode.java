package com.elytradev.infraredstone.tile;

import com.elytradev.infraredstone.logic.IInfraRedstone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityDiode extends TileEntityIRComponent implements IInfraRedstone, ITickable {
    public boolean active;
    private int signal;

    public void update() {
        if
    }

    @Override
    public int getSignalValue(World world, BlockPos pos, IBlockState state, EnumFacing inspectingFrom) {
        return signal;
    }
}
