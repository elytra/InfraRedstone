package com.elytradev.infraredstone.block;

import com.elytradev.infraredstone.tile.TileEntityGateXor;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockGateXor extends BlockModule<TileEntityGateXor> implements IBlockBase {

    protected String name;
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool FRONT_ACTIVE = PropertyBool.create("front_active");
    public static final PropertyBool LEFT_ACTIVE = PropertyBool.create("left_active");
    public static final PropertyBool RIGHT_ACTIVE = PropertyBool.create("right_active");
    public static int FACE = 3;

    public BlockGateXor() {
        super(Material.CIRCUITS, "gate_xor");
        this.setDefaultState(blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(FRONT_ACTIVE, false)
                .withProperty(LEFT_ACTIVE, false)
                .withProperty(RIGHT_ACTIVE, false));

        this.setHardness(0.5f);
    }

    //TODO: make this pop off when the block it's on is broken, also maybe by water but ¯\_(ツ)_/¯

    @Override
    public Class<TileEntityGateXor> getTileEntityClass() {
        return TileEntityGateXor.class;
    }

    @Override
    public TileEntityGateXor createTileEntity(World world, IBlockState state) {
        return new TileEntityGateXor();
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
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 2/16.0, 1.0D);
    }

    @Override
    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FACING, FRONT_ACTIVE, LEFT_ACTIVE, RIGHT_ACTIVE);
    }

    @Override
    public int getMetaFromState(IBlockState state){
        int meta = 0;
        meta |= state.getValue(FACING).getHorizontalIndex();
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        int facebits = meta & FACE;
        EnumFacing facing = EnumFacing.getHorizontal(facebits);
        return blockState.getBaseState().withProperty(FACING, facing);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te==null || !(te instanceof TileEntityGateXor)) return state;
        TileEntityGateXor xor = (TileEntityGateXor)te;
        return state
                .withProperty(FRONT_ACTIVE, xor.isActive())
                .withProperty(LEFT_ACTIVE, xor.isLeftActive())
                .withProperty(RIGHT_ACTIVE, xor.isRightActive());
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState()
                .withProperty(FACING, placer.getHorizontalFacing())
                .withProperty(FRONT_ACTIVE, false)
                .withProperty(LEFT_ACTIVE, false)
                .withProperty(RIGHT_ACTIVE, false);
    }
}
