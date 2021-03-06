package com.elytradev.infraredstone.client.render;

import com.elytradev.infraredstone.block.BlockGateNot;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.tile.TileEntityGateNot;

import com.elytradev.infraredstone.util.Torch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;

public class RenderGateNot extends RenderInRedBase<TileEntityGateNot> {
    public static final String BOTH = "infraredstone:blocks/gate_not_glow";
    public static final String IN = "infraredstone:blocks/gate_not_glow_in";
    public static final String OUT = "infraredstone:blocks/gate_not_glow_out";

    public RenderGateNot() {
        torches = new Torch[1];
        torches[0] = new Torch(7/16d, 2/16d, true, true);
    }

    @Override
    public EnumFacing getFacing(TileEntityGateNot tile) {
        IBlockState state = tile.getWorld().getBlockState(tile.getPos());
        if (state.getBlock()==ModBlocks.GATE_NOT) return state.getValue(BlockGateNot.FACING);

        return EnumFacing.NORTH;
    }

    @Override
    public TextureAtlasSprite getLightupTexture(TileEntityGateNot tile) {
        boolean front;
        torches[0].isFullHeight = !tile.booleanMode;
        if (tile.isActive()) {
            torches[0].isLit=true;
            front = true;
        } else {
            torches[0].isLit = false;
            front = false;
        }
        if (front) {
            if (tile.backActive) return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(BOTH); else return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(OUT);
        } else return (tile.backActive)? Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(IN) : null;
    }
}
