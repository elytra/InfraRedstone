package com.elytradev.infraredstone.client.render;

import com.elytradev.infraredstone.tile.TileEntityIRComponent;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.animation.FastTESR;

public abstract class RenderInRedBase<T extends TileEntityIRComponent> extends FastTESR<T>{

    @Override
    public void renderTileEntityFast(T te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        buffer.setTranslation(x, y, z);
        TextureAtlasSprite sprite = getLightupTexture(te);
        if (sprite==null) return;
        renderTopFace(buffer, sprite, getFacing(te));
    }
    
    
    public void renderTopFace(BufferBuilder buffer, TextureAtlasSprite tex, EnumFacing facing) {
        double faceHeight = 3/16.0;
        faceHeight += 0.002; //Far enough to not z-fight. Hopefully.
        
        switch(facing) {
        case NORTH:
        default:
            buffer.pos(0, faceHeight, 0).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMinV()).lightmap(240, 240).endVertex();
            buffer.pos(1, faceHeight, 0).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMinV()).lightmap(240, 240).endVertex();
            buffer.pos(1, faceHeight, 1).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMaxV()).lightmap(240, 240).endVertex();
            buffer.pos(0, faceHeight, 1).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMaxV()).lightmap(240, 240).endVertex();
            break;
        case EAST:
            buffer.pos(0, faceHeight, 0).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMaxV()).lightmap(240, 240).endVertex();
            buffer.pos(1, faceHeight, 0).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMinV()).lightmap(240, 240).endVertex();
            buffer.pos(1, faceHeight, 1).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMinV()).lightmap(240, 240).endVertex();
            buffer.pos(0, faceHeight, 1).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMaxV()).lightmap(240, 240).endVertex();
            break;
        case SOUTH:
            buffer.pos(0, faceHeight, 0).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMaxV()).lightmap(240, 240).endVertex();
            buffer.pos(1, faceHeight, 0).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMaxV()).lightmap(240, 240).endVertex();
            buffer.pos(1, faceHeight, 1).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMinV()).lightmap(240, 240).endVertex();
            buffer.pos(0, faceHeight, 1).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMinV()).lightmap(240, 240).endVertex();
            break;
        case WEST:
            buffer.pos(0, faceHeight, 0).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMinV()).lightmap(240, 240).endVertex();
            buffer.pos(1, faceHeight, 0).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMaxV()).lightmap(240, 240).endVertex();
            buffer.pos(1, faceHeight, 1).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMaxV()).lightmap(240, 240).endVertex();
            buffer.pos(0, faceHeight, 1).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMinV()).lightmap(240, 240).endVertex();
            break;
        }
    }
    
    public abstract EnumFacing getFacing(T tile);
    public abstract TextureAtlasSprite getLightupTexture(T tile);
}
