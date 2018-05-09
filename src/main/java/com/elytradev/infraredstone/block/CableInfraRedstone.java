package com.elytradev.infraredstone.block;

import com.elytradev.infraredstone.tile.TileEntityFineLever;
import com.elytradev.infraredstone.util.EnumCableConnection;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class CableInfraRedstone extends BlockBase implements IBlockBase {

    public CableInfraRedstone() {
        super(Material.CIRCUITS, "infra_redstone");

        this.setDefaultState(blockState.getBaseState()
                .withProperty(NORTH, EnumCableConnection.DISCONNECTED)
                .withProperty(SOUTH, EnumCableConnection.DISCONNECTED)
                .withProperty(EAST, EnumCableConnection.DISCONNECTED)
                .withProperty(WEST, EnumCableConnection.DISCONNECTED));

    }

    public static final PropertyEnum<EnumCableConnection> NORTH = PropertyEnum.create("north", EnumCableConnection.class);
    public static final PropertyEnum<EnumCableConnection> SOUTH = PropertyEnum.create("south", EnumCableConnection.class);
    public static final PropertyEnum<EnumCableConnection> EAST = PropertyEnum.create("east", EnumCableConnection.class);
    public static final PropertyEnum<EnumCableConnection> WEST = PropertyEnum.create("west", EnumCableConnection.class);

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    private EnumCableConnection getCableConnections(IBlockAccess world, BlockPos pos, EnumFacing dir) {
        if (world.getBlockState(pos.offset(dir)).getBlock() instanceof IBlockBase) {
            return EnumCableConnection.CONNECTED;
        }
        if (world.isAirBlock(pos.offset(EnumFacing.UP))) {
            if (world.getBlockState(pos.offset(dir).offset(EnumFacing.UP)).getBlock() == ModBlocks.INFRA_REDSTONE) {
                return EnumCableConnection.CONNECTED_UP;
            }
        }
        return EnumCableConnection.DISCONNECTED;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    public IBlockState getActualState(IBlockState state, World world, BlockPos pos) {
        return state
                .withProperty(NORTH, getCableConnections(world, pos, EnumFacing.NORTH))
                .withProperty(SOUTH, getCableConnections(world, pos, EnumFacing.SOUTH))
                .withProperty(EAST, getCableConnections(world, pos, EnumFacing.EAST))
                .withProperty(WEST, getCableConnections(world, pos, EnumFacing.WEST));
    }

    @Override
    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, NORTH, SOUTH, EAST, WEST);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState()
                .withProperty(NORTH, getCableConnections(world, pos, EnumFacing.NORTH))
                .withProperty(SOUTH, getCableConnections(world, pos, EnumFacing.SOUTH))
                .withProperty(EAST, getCableConnections(world, pos, EnumFacing.EAST))
                .withProperty(WEST, getCableConnections(world, pos, EnumFacing.WEST));
    }



}