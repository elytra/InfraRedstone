package com.elytradev.infraredstone.client.render;

import com.elytradev.infraredstone.block.BlockEncoder;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.tile.TileEntityEncoder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class RenderEncoder extends RenderInRedBase<TileEntityEncoder> {
    public static final String LIT = "infraredstone:blocks/encoder_glow";

    @Override
    public EnumFacing getFacing(TileEntityEncoder tile) {
        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
        if (state.getBlock()==ModBlocks.ENCODER) return state.getValue(BlockEncoder.FACING);

        return EnumFacing.NORTH;
    }

    @Override
    public TextureAtlasSprite getLightupTexture(TileEntityEncoder tile) {
        return (tile.isActive()) ? Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(LIT) : null;
    }

}
