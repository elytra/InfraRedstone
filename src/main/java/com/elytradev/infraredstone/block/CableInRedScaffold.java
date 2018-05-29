package com.elytradev.infraredstone.block;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.logic.ISimpleInfraRedstone;
import com.elytradev.infraredstone.util.enums.EnumCableConnection;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class CableInRedScaffold extends BlockBase implements IBlockBase {

    public static final AxisAlignedBB WITHDRAWN_SIDES_AABB = new AxisAlignedBB(0.005f, 0.0f, 0.005f, 0.995f, 1.0f, 0.995f);

    public static final PropertyEnum<EnumCableConnection> NORTH = PropertyEnum.create("north", EnumCableConnection.class);
    public static final PropertyEnum<EnumCableConnection> SOUTH = PropertyEnum.create("south", EnumCableConnection.class);
    public static final PropertyEnum<EnumCableConnection> EAST = PropertyEnum.create("east", EnumCableConnection.class);
    public static final PropertyEnum<EnumCableConnection> WEST = PropertyEnum.create("west", EnumCableConnection.class);
    public static final PropertyEnum<EnumCableConnection> UP = PropertyEnum.create("up", EnumCableConnection.class);

    public CableInRedScaffold() {
        super(Material.CIRCUITS, "infra_redstone_scaffold");

        this.setDefaultState(blockState.getBaseState()
                .withProperty(NORTH, EnumCableConnection.DISCONNECTED)
                .withProperty(SOUTH, EnumCableConnection.DISCONNECTED)
                .withProperty(EAST, EnumCableConnection.DISCONNECTED)
                .withProperty(WEST, EnumCableConnection.DISCONNECTED)
                .withProperty(UP, EnumCableConnection.DISCONNECTED));
    }

    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entity, boolean b) {
        super.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entity, b);
    }

    /* Not used that much but here for consistency. Probably a raytrace thing. */
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.005f, 0.0f, 0.005f, 0.995f, 1.0f, 0.995f);
    }

    /* Used for addCollisionBoxToList, so this is what really determines our shape for physics */
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return WITHDRAWN_SIDES_AABB;
    }

    @Override
    public boolean isNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        return true;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (entity instanceof EntityItem) return;
        if (entity.isCollidedHorizontally) {
            entity.motionY = 0.35;
        } else if (entity.isSneaking()) {
            entity.motionY = 0.08; //Stop, but also counteract EntityLivingBase-applied microgravity
        } else if (entity.motionY<-0.20) {
            entity.motionY = -0.20;
        }
        entity.fallDistance = 0.0f;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    private EnumCableConnection getCableConnections(IBlockAccess world, BlockPos pos, EnumFacing dir) {
        if (canConnect(world, pos.offset(dir), dir.getOpposite())) return EnumCableConnection.CONNECTED;

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

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state
                .withProperty(NORTH, getCableConnections(world, pos, EnumFacing.NORTH))
                .withProperty(SOUTH, getCableConnections(world, pos, EnumFacing.SOUTH))
                .withProperty(EAST, getCableConnections(world, pos, EnumFacing.EAST))
                .withProperty(WEST, getCableConnections(world, pos, EnumFacing.WEST))
                .withProperty(UP, getCableConnections(world, pos, EnumFacing.UP));
    }

    /** Ask the *destination block* if it can be connected to. {@code from} side has the same semantics as Capability sides */
    public static boolean canConnect(IBlockAccess world, BlockPos pos, EnumFacing from) {
        if (world.isAirBlock(pos)) return false;

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block==ModBlocks.INFRA_REDSTONE || block==ModBlocks.IN_RED_SCAFFOLD) return true;
        if (block instanceof ISimpleInfraRedstone) {
            return ((ISimpleInfraRedstone)block).canConnectIR(world, pos, state, from);
        }
        TileEntity te = world.getTileEntity(pos);
        if (te==null) return false;

        return te.hasCapability(InfraRedstone.CAPABILITY_IR, from);
    }

    @Override
    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, NORTH, SOUTH, EAST, WEST, UP);
    }
}