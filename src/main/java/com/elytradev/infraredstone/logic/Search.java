package com.elytradev.infraredstone.logic;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.ModBlocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Search {
	/**
	 * Searches for the highest IR signal which can be delivered to the indicated block face.
	 * @param world   The world the device resides in
	 * @param device  The location of the device
	 * @param dir     The direction *from* the device *towards* where the prospective signal is coming from.
	 * @return        The IR value, or the redstone level if no IR is present, or 0 if nothing is present.
	 */
	public static int forIRValue(World world, BlockPos device, EnumFacing dir) {
		BlockPos initialPos = device.offset(dir);
		if (world.isAirBlock(initialPos)) return 0;
		IBlockState initialState = world.getBlockState(initialPos);
		if (initialState.getBlock()==ModBlocks.INFRA_REDSTONE) {
			//Search!
			return wireSearch(world, device, initialPos);
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
		
		//Oh. Okay. No wires or machines. Well, return the vanilla redstone value here and call it a day.
		return world.isBlockIndirectlyGettingPowered(initialPos);
	}
	
	private static int wireSearch(World world, BlockPos device, BlockPos firstWire) {
		return 0;
		
		//copy-paste scanNetwork from BB while we write out a proper one
//	    public void scanNetwork(BiPredicate<World, BlockPos> membership, BiPredicate<World, List<BlockPos>> validator) {
//	        if (!hasWorld()) return;
//	        if (world.isRemote) return;
//	        Set<BlockPos> seen = new HashSet<>();
//	        List<BlockPos> members = new ArrayList<>();
//	        List<BlockPos> queue = new ArrayList<>();
//	        queue.add(getPos());
	//
//	        onDisassemble(world, members);
	//
//	        int itr = 0;
//	        while (!queue.isEmpty()) {
//	            if (members.size() > getMaxBlocksPerMultiblock()) {
//	                setControllerStatus(TileEntityMultiblockController.ControllerStatus.ERRORED, "msg.bb.tooBig");
//	                return;
//	            }
//	            BlockPos pos = queue.remove(0);
//	            seen.add(pos);
//	            if (membership.test(world, pos)) {
//	                //TODO: Replace with generalized neighbor function?
//	                for (EnumFacing ef : EnumFacing.VALUES) {
//	                    BlockPos p = pos.offset(ef);
//	                    if (seen.contains(p)) continue;
//	                    seen.add(p);
//	                    queue.add(p);
//	                }
	//
//	                if (!members.contains(pos)) {
//	                    //TODO: This is where we would do early checks, like "is this another controller?" or "is this malformed?"
//	                    //(world,pos)->world.getBlock(pos) instanceof IBoilerBlock
//	                    members.add(pos);
//	                }
//	            }
//	            itr++;
//	        }
	//
//	        if (!validator.test(world, members)) {
//	            setControllerStatus(TileEntityMultiblockController.ControllerStatus.ERRORED, status);
//	            return;
//	        }
	//
//	        onAssemble(world, members);
//	        setControllerStatus(TileEntityMultiblockController.ControllerStatus.ACTIVE, "msg.bb.noIssue");
//	    }
	}
}
