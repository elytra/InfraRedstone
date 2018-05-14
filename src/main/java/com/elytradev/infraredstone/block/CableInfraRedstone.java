package com.elytradev.infraredstone.block;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.logic.ISimpleInfraRedstone;
import com.elytradev.infraredstone.util.EnumCableConnection;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class CableInfraRedstone extends BlockBase implements IBlockBase {

    public static final PropertyEnum<EnumCableConnection> NORTH = PropertyEnum.create("north", EnumCableConnection.class);
    public static final PropertyEnum<EnumCableConnection> SOUTH = PropertyEnum.create("south", EnumCableConnection.class);
    public static final PropertyEnum<EnumCableConnection> EAST = PropertyEnum.create("east", EnumCableConnection.class);
    public static final PropertyEnum<EnumCableConnection> WEST = PropertyEnum.create("west", EnumCableConnection.class);

    public CableInfraRedstone() {
        super(Material.CIRCUITS, "infra_redstone");

        this.setDefaultState(blockState.getBaseState()
                .withProperty(NORTH, EnumCableConnection.DISCONNECTED)
                .withProperty(SOUTH, EnumCableConnection.DISCONNECTED)
                .withProperty(EAST, EnumCableConnection.DISCONNECTED)
                .withProperty(WEST, EnumCableConnection.DISCONNECTED));
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
    	if (state.getBlock()!=this) return NULL_AABB;
    	double minX =  6/16d;
    	double minZ =  6/16d;
    	double maxX = 10/16d;
    	double maxZ = 10/16d;
    	state = getActualState(state, world, pos);
    	
    	if (state.getValue(WEST)!=EnumCableConnection.DISCONNECTED) minX = 0d;
    	if (state.getValue(NORTH)!=EnumCableConnection.DISCONNECTED) minZ = 0d;
    	if (state.getValue(EAST)!=EnumCableConnection.DISCONNECTED) maxX = 1d;
    	if (state.getValue(SOUTH)!=EnumCableConnection.DISCONNECTED) maxZ = 1d;
    	
        return new AxisAlignedBB(minX, 0.0D, minZ, maxX, 3/16.0, maxZ);
    }

    private EnumCableConnection getCableConnections(IBlockAccess world, BlockPos pos, EnumFacing dir) {
    	if (canConnect(world, pos.offset(dir), dir.getOpposite())) return EnumCableConnection.CONNECTED;
    	
        if (world.isAirBlock(pos.offset(EnumFacing.UP))) {
        	if (canConnect(world, pos.offset(dir).up(), dir.getOpposite())) return EnumCableConnection.CONNECTED_UP;
        }
        
        if (world.isAirBlock(pos.offset(dir))) {
        	if (canConnect(world, pos.offset(dir).down(), dir.getOpposite())) return EnumCableConnection.CONNECTED;
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

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state
                .withProperty(NORTH, getCableConnections(world, pos, EnumFacing.NORTH))
                .withProperty(SOUTH, getCableConnections(world, pos, EnumFacing.SOUTH))
                .withProperty(EAST, getCableConnections(world, pos, EnumFacing.EAST))
                .withProperty(WEST, getCableConnections(world, pos, EnumFacing.WEST));
    }

    /** Ask the *destination block* if it can be connected to. {@code from} side has the same semantics as Capability sides */
    public static boolean canConnect(IBlockAccess world, BlockPos pos, EnumFacing from) {
    	if (world.isAirBlock(pos)) return false;
    	
    	IBlockState state = world.getBlockState(pos);
    	Block block = state.getBlock();
    	if (block==ModBlocks.INFRA_REDSTONE) return true;
    	if (block instanceof ISimpleInfraRedstone) {
    		return ((ISimpleInfraRedstone)block).canConnectIR(world, pos, state, from);
    	}
    	TileEntity te = world.getTileEntity(pos);
    	if (te==null) return false;
    	
    	return te.hasCapability(InfraRedstone.CAPABILITY_IR, from);
    }
    
    @Override
    public BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, NORTH, SOUTH, EAST, WEST);
    }
}