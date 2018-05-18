package com.elytradev.infraredstone.block;

import com.elytradev.infraredstone.tile.TileEntityLiquidCrystal;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLiquidCrystal extends BlockModule<TileEntityLiquidCrystal> {
    public static final PropertyBool BLOCKING_LIGHT = PropertyBool.create("blocking_light");

    public BlockLiquidCrystal() {
        super(Material.GLASS, "device_liquid_crystal");
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BLOCKING_LIGHT);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BLOCKING_LIGHT) ? 1 : 0;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BLOCKING_LIGHT, meta != 0);
    }
    
    @Override
    public int getLightOpacity(IBlockState state) {
        return state.getValue(BLOCKING_LIGHT) ? 13 : 0;
    }
    
    @Override
    public TileEntityLiquidCrystal createTileEntity(World world, IBlockState state) {
        return new TileEntityLiquidCrystal();
    }
    
    @Override
    public Class<TileEntityLiquidCrystal> getTileEntityClass() {
        return TileEntityLiquidCrystal.class;
    }
    
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        IBlockState other = world.getBlockState(pos.offset(side));
        Block block = other.getBlock();

        if (state != other) {
            return true;
        }

        if (block == this) {
            return false;
        }

        return super.shouldSideBeRendered(state, world, pos, side);
    }

}
