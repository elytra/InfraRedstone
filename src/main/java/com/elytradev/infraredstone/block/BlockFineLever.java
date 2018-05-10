package com.elytradev.infraredstone.block;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.tile.TileEntityFineLever;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockFineLever extends BlockModule<TileEntityFineLever> implements IBlockBase {

    protected String name;
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    public BlockFineLever() {
        super(Material.CIRCUITS, "fine_lever");
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ACTIVE, false));

        this.setHardness(0.5f);
    }

    //TODO: make this pop off when the block it's on is broken

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!world.isRemote && !player.isSneaking() && world.getTileEntity(pos) instanceof  TileEntityFineLever) {
            TileEntityFineLever te = (TileEntityFineLever)world.getTileEntity(pos);
            te.toggleState();
        }
        return true;
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
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FACING, ACTIVE);
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

    public IBlockState getActualState(IBlockState state, TileEntityFineLever te) {
        return state.withProperty(ACTIVE, te.active);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer){
        return this.getDefaultState().withProperty(FACING, facing).withProperty(ACTIVE, false);
    }

    public static EnumFacing getFacing(int meta)
    {
        return EnumFacing.getFront(meta & 7);
    }
}
