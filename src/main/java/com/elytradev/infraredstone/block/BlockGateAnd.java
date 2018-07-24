package com.elytradev.infraredstone.block;

import com.elytradev.infraredstone.tile.TileEntityGateAnd;
import com.elytradev.infraredstone.util.enums.EnumInactiveSelection;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockGateAnd extends BlockModule<TileEntityGateAnd> implements IBlockBase {

    protected String name;
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool BOOLEAN_MODE = PropertyBool.create("boolean_mode");
    public static final PropertyEnum<EnumInactiveSelection> INACTIVE = PropertyEnum.create("inactive", EnumInactiveSelection.class);
    public static int FACE = 3;

    public BlockGateAnd() {
        super(Material.CIRCUITS, "gate_and");
        this.setDefaultState(blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(BOOLEAN_MODE, false)
                .withProperty(INACTIVE, EnumInactiveSelection.NONE));

        this.setHardness(0.5f);
    }

    private static final AxisAlignedBB LEFT_AABB   = new AxisAlignedBB( 0/16d, 0d,  6/16d,  3/16d, 0.5d, 10/16d);
    private static final AxisAlignedBB BACK_AABB   = new AxisAlignedBB( 6/16d, 0d, 13/16d, 10/16d, 0.5d, 16/16d);
    private static final AxisAlignedBB RIGHT_AABB  = new AxisAlignedBB(13/16d, 0d,  6/16d, 16/16d, 0.5d, 10/16d);
    private static final AxisAlignedBB BOOLEAN_AABB = new AxisAlignedBB( 6/16d, 0d,  3/16d, 10/16d, 0.5d,  7/16d);

    @Override
    public Class<TileEntityGateAnd> getTileEntityClass() {
        return TileEntityGateAnd.class;
    }

    @Override
    public TileEntityGateAnd createTileEntity(World world, IBlockState state) {
        return new TileEntityGateAnd();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote && !player.isSneaking() && world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityGateAnd) {
            TileEntityGateAnd te = (TileEntityGateAnd) world.getTileEntity(pos);
            Vec3d blockCenteredHit = new Vec3d(hitX, hitY, hitZ);
            blockCenteredHit = blockCenteredHit.subtract(0.5, 0.5, 0.5);
            switch (state.getValue(BlockGateAnd.FACING)) {
                case SOUTH:
                    blockCenteredHit = blockCenteredHit.rotateYaw((float)Math.PI);
                    break;
                case EAST:
                    blockCenteredHit = blockCenteredHit.rotateYaw((float)Math.PI/2);
                    break;
                case WEST:
                    blockCenteredHit = blockCenteredHit.rotateYaw(3*(float)Math.PI/2);
                    break;
                default:
                    break;
            }
            blockCenteredHit = blockCenteredHit.add(0.5, 0.5, 0.5);
            if (LEFT_AABB.contains(blockCenteredHit)) {
                te.toggleInactive(EnumInactiveSelection.LEFT);
            } else if (BACK_AABB.contains(blockCenteredHit)) {
                te.toggleInactive(EnumInactiveSelection.BACK);
            } else if (RIGHT_AABB.contains(blockCenteredHit)) {
                te.toggleInactive(EnumInactiveSelection.RIGHT);
            } else if (BOOLEAN_AABB.contains(blockCenteredHit)) {
            	te.toggleBooleanMode();
            }
        }
        return true;
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
        return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 3/16.0, 1.0D);
    }

    @Override
    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FACING, BOOLEAN_MODE, INACTIVE);
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
        EnumFacing facing = EnumFacing.byHorizontalIndex(facebits);
        return blockState.getBaseState().withProperty(FACING, facing);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileEntityGateAnd)) return state;
        TileEntityGateAnd and = (TileEntityGateAnd)te;
        return state
                .withProperty(BOOLEAN_MODE, and.booleanMode)
                .withProperty(INACTIVE, and.inactive);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState()
                .withProperty(FACING, placer.getHorizontalFacing())
                .withProperty(BOOLEAN_MODE, false)
                .withProperty(INACTIVE, EnumInactiveSelection.NONE);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isTopSolid() && super.canPlaceBlockAt(worldIn, pos);
    }

    public boolean canBlockStay(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos.down()).isTopSolid();
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!this.canBlockStay(worldIn, pos)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
