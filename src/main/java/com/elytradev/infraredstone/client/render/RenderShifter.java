package com.elytradev.infraredstone.client.render;

import com.elytradev.infraredstone.block.BlockShifter;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.tile.TileEntityShifter;

import com.elytradev.infraredstone.util.enums.EnumShifterSelection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class RenderShifter extends RenderInRedBase<TileEntityShifter> {
    public static final String LIT = "infraredstone:blocks/shifter_glow";
    public static final String CENTER = "_center";
    public static final String LEFT = "_left";
    public static final String RIGHT = "_right";

    @Override
    public EnumFacing getFacing(TileEntityShifter tile) {
        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
        if (state.getBlock()==ModBlocks.SHIFTER) return state.getValue(BlockShifter.FACING);

        return EnumFacing.NORTH;
    }

    @Override
    public TextureAtlasSprite getLightupTexture(TileEntityShifter tile) {
        String phrase = LIT;
        if (tile.isActive()) phrase += CENTER;
        if (tile.isEject()) {
            if (tile.selection == EnumShifterSelection.LEFT) {
                phrase += LEFT;
            } else {
                phrase += RIGHT;
            }
        }
        return (!phrase.equals(LIT)) ? Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(phrase) : null;
    }
}
