package com.elytradev.infraredstone.client.render;

import com.elytradev.infraredstone.block.BlockGateXor;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.tile.TileEntityGateXor;

import com.elytradev.infraredstone.util.Torch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class RenderGateXor extends RenderInRedBase<TileEntityGateXor> {
    public static final String LIT = "infraredstone:blocks/gate_xor_glow";

    public RenderGateXor() {
        torches = new Torch[3];
        torches[0] = new Torch(7/16d, 4/16d, true, true); // output torch
        torches[1] = new Torch(14/16d, 7/16d, true, true); // left torch
        torches[2] = new Torch(0/16d, 7/16d, true, true); // right torch
    }

    @Override
    public EnumFacing getFacing(TileEntityGateXor tile) {
        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
        if (state.getBlock()==ModBlocks.GATE_XOR) return state.getValue(BlockGateXor.FACING);

        return EnumFacing.NORTH;
    }

    @Override
    public TextureAtlasSprite getLightupTexture(TileEntityGateXor tile) {
        getTorches(tile);
        return (tile.isActive()) ? Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(LIT) : null;
    }

    public void getTorches(TileEntityGateXor tile) {
        torches[0].isLit = tile.isActive();
        torches[1].isLit = tile.isLeftActive();
        torches[2].isLit = tile.isRightActive();
    }
}
