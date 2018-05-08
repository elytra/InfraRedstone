package com.elytradev.infraredstone.block;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.tile.TileEntityFineLever;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockFineLever extends BlockTileEntity<TileEntityFineLever> implements IBlockBase {

    protected String name;
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public BlockFineLever(Material material, String name) {
        super(material, name);
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));

        setCreativeTab(InfraRedstone.creativeTab);
    }

    @Override
    public Class<TileEntityFineLever> getTileEntityClass() {
        return TileEntityFineLever.class;
    }

    @Override
    public TileEntityFineLever createTileEntity(World world, IBlockState state) {
        return new TileEntityFineLever();
    }

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

    @Override
    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FACING);
    }


    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | (state.getValue(FACING)).getIndex();
        i |= 8;

        return i;
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, getFacing(meta));
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState().withProperty(FACING, facing);
    }

    public static EnumFacing getFacing(int meta)
    {
        return EnumFacing.getFront(meta & 7);
    }
}
