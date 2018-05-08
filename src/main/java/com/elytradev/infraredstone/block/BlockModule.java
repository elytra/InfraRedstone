package com.elytradev.infraredstone.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;

public abstract class BlockModule<TE extends TileEntity> extends BlockBase {
    public BlockModule(Material material, String name) {
        super(material, name);
    }

    public abstract Class<TE> getTileEntityClass();

    public TE getTileEntity(IBlockAccess world, BlockPos pos) {
        return (TE)world.getTileEntity(pos);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public abstract TE createTileEntity(World world, IBlockState state);


    //copy-paste scanNetwork from BB while we write out a proper one
//    public void scanNetwork(BiPredicate<World, BlockPos> membership, BiPredicate<World, List<BlockPos>> validator) {
//        if (!hasWorld()) return;
//        if (world.isRemote) return;
//        Set<BlockPos> seen = new HashSet<>();
//        List<BlockPos> members = new ArrayList<>();
//        List<BlockPos> queue = new ArrayList<>();
//        queue.add(getPos());
//
//        onDisassemble(world, members);
//
//        int itr = 0;
//        while (!queue.isEmpty()) {
//            if (members.size() > getMaxBlocksPerMultiblock()) {
//                setControllerStatus(TileEntityMultiblockController.ControllerStatus.ERRORED, "msg.bb.tooBig");
//                return;
//            }
//            BlockPos pos = queue.remove(0);
//            seen.add(pos);
//            if (membership.test(world, pos)) {
//                //TODO: Replace with generalized neighbor function?
//                for (EnumFacing ef : EnumFacing.VALUES) {
//                    BlockPos p = pos.offset(ef);
//                    if (seen.contains(p)) continue;
//                    seen.add(p);
//                    queue.add(p);
//                }
//
//                if (!members.contains(pos)) {
//                    //TODO: This is where we would do early checks, like "is this another controller?" or "is this malformed?"
//                    //(world,pos)->world.getBlock(pos) instanceof IBoilerBlock
//                    members.add(pos);
//                }
//            }
//            itr++;
//        }
//
//        if (!validator.test(world, members)) {
//            setControllerStatus(TileEntityMultiblockController.ControllerStatus.ERRORED, status);
//            return;
//        }
//
//        onAssemble(world, members);
//        setControllerStatus(TileEntityMultiblockController.ControllerStatus.ACTIVE, "msg.bb.noIssue");
//    }

}
