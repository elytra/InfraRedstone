package com.elytradev.infraredstone.client.render;

import com.elytradev.infraredstone.block.BlockDiode;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.tile.TileEntityDiode;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class RenderDiode extends RenderInredBase<TileEntityDiode> {
    public static final String LIT = "infraredstone:blocks/diode_glow";
    
    @Override
    public EnumFacing getFacing(TileEntityDiode tile) {
        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
        if (state.getBlock()==ModBlocks.DIODE) return state.getValue(BlockDiode.FACING);
        
        return EnumFacing.NORTH;
    }

    @Override
    public TextureAtlasSprite getLightupTexture(TileEntityDiode tile) {
        return (tile.isActive()) ? Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(LIT) : null;
    }
    
}
