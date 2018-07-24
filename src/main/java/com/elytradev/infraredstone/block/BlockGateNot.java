package com.elytradev.infraredstone.block;

import com.elytradev.infraredstone.tile.TileEntityGateNot;
import com.elytradev.infraredstone.util.enums.EnumInactiveSelection;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
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


public class BlockGateNot extends BlockModule<TileEntityGateNot> implements IBlockBase {

    protected String name;
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool BOOLEAN_MODE = PropertyBool.create("boolean_mode");
    public static int FACE = 3;

    public BlockGateNot() {
        super(Material.CIRCUITS, "gate_not");
        this.setDefaultState(blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(BOOLEAN_MODE, false));

        this.setHardness(0.5f);
    }

    private static final AxisAlignedBB BOOLEAN_AABB = new AxisAlignedBB( 6/16d, 0d,  3/16d, 10/16d, 0.5d,  7/16d);

    @Override
    public Class<TileEntityGateNot> getTileEntityClass() {
        return TileEntityGateNot.class;
    }

    @Override
    public TileEntityGateNot createTileEntity(World world, IBlockState state) {
        return new TileEntityGateNot();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote && !player.isSneaking() && world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityGateNot) {
            TileEntityGateNot te = (TileEntityGateNot) world.getTileEntity(pos);
            Vec3d blockCenteredHit = new Vec3d(hitX, hitY, hitZ);
            blockCenteredHit = blockCenteredHit.subtract(0.5, 0.5, 0.5);
            switch (state.getValue(BlockGateNot.FACING)) {
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
           if (BOOLEAN_AABB.contains(blockCenteredHit)) {
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
        return new BlockStateContainer(this, FACING, BOOLEAN_MODE);
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
        if (!(te instanceof TileEntityGateNot)) return state;
        TileEntityGateNot not = (TileEntityGateNot)te;
        return state
                .withProperty(BOOLEAN_MODE, not.booleanMode);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState()
                .withProperty(FACING, placer.getHorizontalFacing())
                .withProperty(BOOLEAN_MODE, false);
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
