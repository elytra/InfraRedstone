package com.elytradev.infraredstone.client.render;

import com.elytradev.infraredstone.block.BlockTransistor;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.tile.TileEntityTransistor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class RenderTransistor extends RenderInredBase<TileEntityTransistor> {
    public static final String LIT = "infraredstone:blocks/transistor_glow";

    @Override
    public EnumFacing getFacing(TileEntityTransistor tile) {
        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
        if (state.getBlock()==ModBlocks.TRANSISTOR) return state.getValue(BlockTransistor.FACING);

        return EnumFacing.NORTH;
    }

    @Override
    public TextureAtlasSprite getLightupTexture(TileEntityTransistor tile) {
        return (tile.isActive()) ? Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(LIT) : null;
    }

}
