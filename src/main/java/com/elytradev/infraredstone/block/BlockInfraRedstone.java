package com.elytradev.infraredstone.block;

import com.elytradev.infraredstone.tile.TileEntityInRedBlock;
import net.minecraft.block.Block;
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

public class BlockInfraRedstone extends BlockModule<TileEntityInRedBlock> implements IBlockBase {

    protected String name;

    public BlockInfraRedstone() {
        super(Material.CIRCUITS, "infra_redstone_block");

        this.setHardness(1.0f);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    public Class<TileEntityInRedBlock> getTileEntityClass() {
        return TileEntityInRedBlock.class;
    }

    @Override
    public TileEntityInRedBlock createTileEntity(World world, IBlockState state) {
        return new TileEntityInRedBlock();
    }
}