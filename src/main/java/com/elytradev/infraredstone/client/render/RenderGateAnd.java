package com.elytradev.infraredstone.client.render;

import com.elytradev.infraredstone.block.BlockGateAnd;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.tile.TileEntityGateAnd;

import com.elytradev.infraredstone.util.Torch;
import com.elytradev.infraredstone.util.enums.EnumInactiveSelection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class RenderGateAnd extends RenderInRedBase<TileEntityGateAnd> {
    public static final String LIT = "infraredstone:blocks/gate_and_glow";

    public RenderGateAnd() {
        torches = new Torch[4];
        torches[0] = new Torch(7/16d, 4/16d, true, true); // output torch
        torches[1] = new Torch(0d, 7/16d, true, true); // left torch
        torches[2] = new Torch(7/16d, 14/16d, true, true); // back torch
        torches[3] = new Torch(14/16d, 7/16d, true, true); // right torch
    }

    @Override
    public EnumFacing getFacing(TileEntityGateAnd tile) {
        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
        if (state.getBlock()==ModBlocks.GATE_AND) return state.getValue(BlockGateAnd.FACING);

        return EnumFacing.NORTH;
    }

    @Override
    public TextureAtlasSprite getLightupTexture(TileEntityGateAnd tile) {
        getTorches(tile);
        return (tile.isActive()) ? Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(LIT) : null;
    }

    public void getTorches(TileEntityGateAnd tile) {
        torches[0].isLit = tile.isActive();
        torches[0].isFullHeight = !tile.inverted;
        torches[1].isLit = tile.isLeftActive();
        torches[1].isFullHeight = (tile.inactive != EnumInactiveSelection.LEFT);
        torches[2].isLit = tile.isBackActive();
        torches[2].isFullHeight = (tile.inactive != EnumInactiveSelection.BACK);
        torches[3].isLit = tile.isRightActive();
        torches[3].isFullHeight = (tile.inactive != EnumInactiveSelection.RIGHT);
    }
}
