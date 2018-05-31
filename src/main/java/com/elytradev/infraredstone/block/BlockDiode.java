package com.elytradev.infraredstone.block;

import com.elytradev.infraredstone.tile.TileEntityDiode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
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


public class BlockDiode extends BlockModule<TileEntityDiode> implements IBlockBase {

    protected String name;
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    //public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final PropertyBool BIT_0 = PropertyBool.create("bit_0");
    public static final PropertyBool BIT_1 = PropertyBool.create("bit_1");
    public static final PropertyBool BIT_2 = PropertyBool.create("bit_2");
    public static final PropertyBool BIT_3 = PropertyBool.create("bit_3");
    public static final PropertyBool BIT_4 = PropertyBool.create("bit_4");
    public static final PropertyBool BIT_5 = PropertyBool.create("bit_5");
    public static int FACE = 3;

    public BlockDiode() {
        super(Material.CIRCUITS, "diode");
        this.setDefaultState(blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                //.withProperty(ACTIVE, false)
                .withProperty(BIT_0, true)
                .withProperty(BIT_1, true)
                .withProperty(BIT_2, true)
                .withProperty(BIT_3, true)
                .withProperty(BIT_4, true)
                .withProperty(BIT_5, true));

        this.setHardness(0.5f);
    }

    private static final AxisAlignedBB AABB_BIT_0   = new AxisAlignedBB( 10/16d, 0d,  10/16d,  11/16d, 0.5d, 14/16d);
    private static final AxisAlignedBB AABB_BIT_1   = new AxisAlignedBB( 9/16d, 0d,  8/16d,  10/16d, 0.5d, 12/16d);
    private static final AxisAlignedBB AABB_BIT_2   = new AxisAlignedBB( 8/16d, 0d,  10/16d,  9/16d, 0.5d, 14/16d);
    private static final AxisAlignedBB AABB_BIT_3   = new AxisAlignedBB( 7/16d, 0d,  8/16d,  8/16d, 0.5d, 12/16d);
    private static final AxisAlignedBB AABB_BIT_4   = new AxisAlignedBB( 6/16d, 0d,  10/16d,  7/16d, 0.5d, 14/16d);
    private static final AxisAlignedBB AABB_BIT_5   = new AxisAlignedBB( 5/16d, 0d,  8/16d,  6/16d, 0.5d, 12/16d);

    @Override
    public Class<TileEntityDiode> getTileEntityClass() {
        return TileEntityDiode.class;
    }

    @Override
    public TileEntityDiode createTileEntity(World world, IBlockState state) {
        return new TileEntityDiode();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(pos);
        if(!world.isRemote && !player.isSneaking() && te instanceof TileEntityDiode) {
            Vec3d blockCenteredHit = new Vec3d(hitX, hitY, hitZ);
            blockCenteredHit = blockCenteredHit.subtract(0.5, 0.5, 0.5);
            switch (state.getValue(BlockDiode.FACING)) {
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
            blockCenteredHit = blockCenteredHit.addVector(0.5, 0.5, 0.5);
            TileEntityDiode teDiode = (TileEntityDiode)te;
            if (AABB_BIT_0.contains(blockCenteredHit)) {
                teDiode.setMask(0);
            }
            if (AABB_BIT_1.contains(blockCenteredHit)) {
                teDiode.setMask(1);
            }
            if (AABB_BIT_2.contains(blockCenteredHit)) {
                teDiode.setMask(2);
            }
            if (AABB_BIT_3.contains(blockCenteredHit)) {
                teDiode.setMask(3);
            }
            if (AABB_BIT_4.contains(blockCenteredHit)) {
                teDiode.setMask(4);
            }
            if (AABB_BIT_5.contains(blockCenteredHit)) {
                teDiode.setMask(5);
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
        return new BlockStateContainer(this, FACING,
                //ACTIVE,
                BIT_0, BIT_1, BIT_2, BIT_3, BIT_4, BIT_5);
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
    	if (!(te instanceof TileEntityDiode)) return state;
    	//TileEntityDiode diode = (TileEntityDiode)te;
    	return state
                //.withProperty(ACTIVE, diode.isActive())
                .withProperty(BIT_0, bitToBool(0, world, pos))
                .withProperty(BIT_1, bitToBool(1, world, pos))
                .withProperty(BIT_2, bitToBool(2, world, pos))
                .withProperty(BIT_3, bitToBool(3, world, pos))
                .withProperty(BIT_4, bitToBool(4, world, pos))
                .withProperty(BIT_5, bitToBool(5, world, pos));
    }

    public boolean bitToBool(int bit, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if(te instanceof TileEntityDiode) {
            TileEntityDiode teDiode = (TileEntityDiode)te;
            return (1<<bit & teDiode.getMask()) > 0;
        }
        return false;
    }
    
    @Override
    public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    	return getStrongPower(state, world, pos, side);
    }
    
    @Override
    public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    	if (side!=state.getValue(FACING).getOpposite()) return 0;
    	TileEntity te = world.getTileEntity(pos);
    	if (te!=null && te instanceof TileEntityDiode) {
    		return ((TileEntityDiode)te).isActive()?16:0;
    	}
    	return 0;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState()
                .withProperty(FACING, placer.getHorizontalFacing())
                //.withProperty(ACTIVE, false)
                .withProperty(BIT_0, true)
                .withProperty(BIT_1, true)
                .withProperty(BIT_2, true)
                .withProperty(BIT_3, true)
                .withProperty(BIT_4, true)
                .withProperty(BIT_5, true);
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
}
