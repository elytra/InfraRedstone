package com.elytradev.infraredstone.client.render;

import com.elytradev.infraredstone.block.BlockOscillator;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.tile.TileEntityOscillator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class RenderOscillator extends RenderInredBase<TileEntityOscillator> {
    public static final String LIT = "infraredstone:blocks/oscillator_glow";

    @Override
    public EnumFacing getFacing(TileEntityOscillator tile) {
        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
        if (state.getBlock()==ModBlocks.OSCILLATOR) return state.getValue(BlockOscillator.FACING);

        return EnumFacing.NORTH;
    }

    @Override
    public TextureAtlasSprite getLightupTexture(TileEntityOscillator tile) {
        return (tile.isActive()) ? Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(LIT) : null;
    }

}
