package com.elytradev.infraredstone.block;

import com.elytradev.infraredstone.tile.TileEntityShifter;
import com.elytradev.infraredstone.util.enums.EnumShifterSelection;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
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


public class BlockShifter extends BlockModule<TileEntityShifter> implements IBlockBase {

    protected String name;
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final PropertyEnum<EnumShifterSelection> SELECTION = PropertyEnum.create("selection", EnumShifterSelection.class);
    public static int FACE = 3;

    public BlockShifter() {
        super(Material.CIRCUITS, "shifter");
        this.setDefaultState(blockState.getBaseState()
                .withProperty(FACING, EnumFacing.NORTH)
                .withProperty(ACTIVE, false)
                .withProperty(SELECTION, EnumShifterSelection.LEFT));

        this.setHardness(0.5f);
    }

    private static final AxisAlignedBB LEFT_AABB   = new AxisAlignedBB( 0/16d, 0d,  6/16d,  3/16d, 0.5d, 10/16d);
    private static final AxisAlignedBB BACK_AABB   = new AxisAlignedBB( 6/16d, 0d, 13/16d, 10/16d, 0.5d, 16/16d);
    private static final AxisAlignedBB RIGHT_AABB  = new AxisAlignedBB(13/16d, 0d,  6/16d, 16/16d, 0.5d, 10/16d);
    private static final AxisAlignedBB INVERT_AABB = new AxisAlignedBB( 6/16d, 0d,  3/16d, 10/16d, 0.5d,  7/16d);

    @Override
    public Class<TileEntityShifter> getTileEntityClass() {
        return TileEntityShifter.class;
    }

    @Override
    public TileEntityShifter createTileEntity(World world, IBlockState state) {
        return new TileEntityShifter();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote && !player.isSneaking() && world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEntityShifter) {
            TileEntityShifter te = (TileEntityShifter) world.getTileEntity(pos);
            te.toggleSelection();
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
        return new BlockStateContainer(this, FACING, ACTIVE, SELECTION);
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
        if (!(te instanceof TileEntityShifter)) return state;
        TileEntityShifter shifter = (TileEntityShifter)te;
        return state
                .withProperty(ACTIVE, shifter.isActive())
                .withProperty(SELECTION, shifter.selection);
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState()
                .withProperty(FACING, placer.getHorizontalFacing())
                .withProperty(ACTIVE, false)
                .withProperty(SELECTION, EnumShifterSelection.LEFT);
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
