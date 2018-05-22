package com.elytradev.infraredstone.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.ModBlocks;
import com.google.common.base.Objects;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class InRedLogic {
	private static final int INTER_IR_TICKS = 1;
	public static int tickCount = 0;
	
	@SubscribeEvent
	public static void onServerTick(TickEvent.ServerTickEvent event) {
		if (event.phase!=TickEvent.Phase.END) return;
		
		tickCount++;
		if (tickCount>INTER_IR_TICKS) tickCount = 0;
	}
	
	public static boolean isIRTick() {
		return (tickCount==0);
	}
	
	/**
	 * Searches for the highest IR signal which can be delivered to the indicated block face.
	 * @param world   The world the device resides in
	 * @param device  The location of the device
	 * @param dir     The direction *from* the device *towards* where the prospective signal is coming from.
	 * @return        The IR value, or the redstone level if no IR is present, or 0 if nothing is present.
	 */
	public static int findIRValue(World world, BlockPos device, EnumFacing dir) {
		BlockPos initialPos = device.offset(dir);
		
		if (!checkCandidacy(world, initialPos, dir.getOpposite())) {
			BlockPos up = initialPos.up();
			if (checkCandidacy(world, up, dir.getOpposite())) {
				initialPos = up;
			} else {
				BlockPos down = initialPos.down();
				if (checkCandidacy(world, down, dir.getOpposite())) {
					initialPos = down;
				} else {
					return (world.getRedstonePower(initialPos, dir)!=0) ? 1 : 0;
				}
			}
		}
		
		if (world.isAirBlock(initialPos)) return 0;
		IBlockState initialState = world.getBlockState(initialPos);
		if (initialState.getBlock()==ModBlocks.INFRA_REDSTONE) {
			//Search!
			return wireSearch(world, device, dir);
		}
		
		if (initialState.getBlock() instanceof ISimpleInfraRedstone) {
			//We have a simple IR block behind us. Excellent! Don't search, just get its value.
			return ((ISimpleInfraRedstone)initialState.getBlock()).getSignalValue(world, initialPos, initialState, dir.getOpposite());
		}
		
		TileEntity te = world.getTileEntity(initialPos);
		if (te!=null && te.hasCapability(InfraRedstone.CAPABILITY_IR, dir.getOpposite())) {
			//We have a full IR tile behind us. Fantastic! Don't search, just get its value.
			IInfraRedstone cap = te.getCapability(InfraRedstone.CAPABILITY_IR, dir.getOpposite());
			return cap.getSignalValue();
		}
		
		//Oh. Okay. No wires or machines. Well, return the vanilla redstone value as the bottom bit here and call it a day.
		return (world.getRedstonePower(initialPos, dir)!=0) ? 1 : 0;
	}
	
	private static boolean checkCandidacy(World world, BlockPos pos, EnumFacing side) {
		if (world.isAirBlock(pos)) return false;
		
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock()==ModBlocks.INFRA_REDSTONE) return true;
		if (state.getBlock() instanceof ISimpleInfraRedstone) return true;
		TileEntity te = world.getTileEntity(pos);
		if (te==null) return false;
		if (te.hasCapability(InfraRedstone.CAPABILITY_IR, side)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	private static final EnumFacing[] PLANAR_FACINGS = { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST };
	private static int wireSearch(World world, BlockPos device, EnumFacing dir) {
		int depth = 0;
		Set<Endpoint> rejected = new HashSet<Endpoint>();
		Set<BlockPos> traversed = new HashSet<BlockPos>();
		List<Endpoint> members = new ArrayList<>();
		List<Endpoint> queue = new ArrayList<>();
		List<Endpoint> next = new ArrayList<>();
		
		queue.add(new Endpoint(device.offset(dir), dir.getOpposite()));
		
		if (device.getY()<255 && !world.isSideSolid(device.offset(EnumFacing.UP), EnumFacing.DOWN)) queue.add(new Endpoint(device.offset(dir).up(), dir.getOpposite()));
		if (device.getY()>0   && !world.isSideSolid(device.offset(dir), dir.getOpposite()))         queue.add(new Endpoint(device.offset(dir).down(), dir.getOpposite()));
		
		while(!queue.isEmpty() || !next.isEmpty()) {
			if (queue.isEmpty()) {
				depth++;
				if (depth>63) return 0; //We've searched too far, there's no signal in range.
				queue.addAll(next);
				next.clear();
			}
			
			Endpoint cur = queue.remove(0);
			
			if (world.isAirBlock(cur.pos)) continue;
			IBlockState state = world.getBlockState(cur.pos);
			
			Block block = state.getBlock();
			if (block==ModBlocks.INFRA_REDSTONE) {
				traversed.add(cur.pos);
				
				//Add neighbors
				for(EnumFacing facing : PLANAR_FACINGS) {
					BlockPos offset = cur.pos.offset(facing);
					
					if (offset.getY()<255 && !world.isSideSolid(cur.pos.up(), EnumFacing.DOWN)) checkAdd(new Endpoint(offset.up(), facing.getOpposite()), next, traversed, rejected);
					if (offset.getY()>0   && !world.isSideSolid(offset, facing.getOpposite()))  checkAdd(new Endpoint(offset.down(), facing.getOpposite()), next, traversed, rejected);
					if (facing==cur.facing) continue; //Don't try to bounce back to the block we came from
					checkAdd(new Endpoint(offset, facing.getOpposite()), next, traversed, rejected);
				}
				
				continue;
			}
			
			Integer rightHere = valueDirectlyAt(world, cur.pos, cur.facing);
			if (rightHere!=null) {
				members.add(cur);
				rejected.add(cur);
				continue;
			}
		}
		
		//Grab the bitwise OR of all signals
		int result = 0;
		for(Endpoint cur : members) {
			int val = valueDirectlyAt(world, cur.pos, cur.facing);
			result |= val;
		}
		return result;
	}
	
	private static void checkAdd(Endpoint endpoint, List<Endpoint> next, Set<BlockPos> traversed, Set<Endpoint> rejected) {
		if (traversed.contains(endpoint.pos)) return;
		if (rejected.contains(endpoint)) return;
		next.add(endpoint);
	}
	
	private static Integer valueDirectlyAt(World world, BlockPos pos, EnumFacing dir) {
		if (world.isAirBlock(pos)) return null;
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block==ModBlocks.INFRA_REDSTONE) return null; //wires don't carry power directly
		if (block instanceof ISimpleInfraRedstone) {
			return ((ISimpleInfraRedstone)block).getSignalValue(world, pos, state, dir);
		}
		TileEntity te = world.getTileEntity(pos);
		if (te!=null && te.hasCapability(InfraRedstone.CAPABILITY_IR, dir)) {
			return te.getCapability(InfraRedstone.CAPABILITY_IR, dir).getSignalValue();
		}
		return null;
	}
	
	private static class Endpoint {
		BlockPos pos;
		EnumFacing facing;
		

		public Endpoint(BlockPos pos, EnumFacing facing) {
			this.pos = pos;
			this.facing = facing;
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(pos, facing);
		}
		
		@Override
		public boolean equals(Object other) {
			if (other==null) return false;
			if (!(other instanceof Endpoint)) return false;
			Endpoint otherEnd = (Endpoint)other;
			return Objects.equal(pos, otherEnd.pos) && Objects.equal(facing, otherEnd.facing);
		}
		
		@Override
		public String toString() {
			return "{x:"+pos.getX()+", y:"+pos.getY()+", z:"+pos.getZ()+", dir:"+facing+"}";
		}
		
	}
}
