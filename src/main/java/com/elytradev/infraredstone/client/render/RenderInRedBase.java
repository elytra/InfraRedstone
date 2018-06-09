package com.elytradev.infraredstone.client.render;

import com.elytradev.infraredstone.tile.TileEntityIRComponent;

import com.elytradev.infraredstone.util.Torch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.animation.FastTESR;

public abstract class RenderInRedBase<T extends TileEntityIRComponent> extends FastTESR<T>{

    protected Torch[] torches;

    @Override
    public void renderTileEntityFast(T te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        buffer.setTranslation(x, y, z);
        TextureAtlasSprite sprite = getLightupTexture(te);
        if (sprite==null) return;
        renderTopFace(buffer, sprite, getFacing(te));
        if (torches != null && torches.length != 0) {
            for (Torch torch : torches) {
                TextureAtlasSprite light = (torch.isLit) ? Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("infraredstone:blocks/lights") : null;
                if (light == null) return;
                renderLight(buffer, light, torch.cornerX, torch.cornerZ, torch.isFullHeight);
            }
        }
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

    public void renderLight(BufferBuilder buffer, TextureAtlasSprite tex, Double cornerX, Double cornerZ, boolean isFullHeight) {
        double eastX = cornerX-0.002;
        double westX = cornerX+0.127;
        double northZ = cornerZ-0.002;
        double southZ = cornerZ+0.127;
        double topY = (isFullHeight) ? 0.3145 : 0.252;
        double maxY = (isFullHeight) ? 5/16d : 1/4d;
        double minUV = 7/16;
        double maxUV = 9/16;
        double maxV = (isFullHeight) ? 9/16 : 1/2;

        //top
        buffer.pos(cornerX, topY, cornerZ).color(1f, 1f, 1f, 1f).tex(minUV, minUV).lightmap(240, 240).endVertex();
        buffer.pos(cornerX+2/16, topY, cornerZ).color(1f, 1f, 1f, 1f).tex(maxUV, minUV).lightmap(240, 240).endVertex();
        buffer.pos(cornerX+2/16, topY, cornerZ+2/16).color(1f, 1f, 1f, 1f).tex(maxUV, maxUV).lightmap(240, 240).endVertex();
        buffer.pos(cornerX, topY, cornerZ+2/16).color(1f, 1f, 1f, 1f).tex(minUV, maxUV).lightmap(240, 240).endVertex();

        //north
        buffer.pos(cornerX, 3/16, northZ).color(1f, 1f, 1f, 1f).tex(minUV, minUV).lightmap(240, 240).endVertex();
        buffer.pos(cornerX+2/16, 3/16, northZ).color(1f, 1f, 1f, 1f).tex(maxUV, minUV).lightmap(240, 240).endVertex();
        buffer.pos(cornerX+2/16, maxY, northZ).color(1f, 1f, 1f, 1f).tex(minUV, maxV).lightmap(240, 240).endVertex();
        buffer.pos(cornerX, maxY, northZ).color(1f, 1f, 1f, 1f).tex(minUV, maxV).lightmap(240, 240).endVertex();

        //south
        buffer.pos(cornerX, 3/16, southZ).color(1f, 1f, 1f, 1f).tex(minUV, minUV).lightmap(240, 240).endVertex();
        buffer.pos(cornerX+2/16, 3/16, southZ).color(1f, 1f, 1f, 1f).tex(maxUV, minUV).lightmap(240, 240).endVertex();
        buffer.pos(cornerX+2/16, maxY, southZ).color(1f, 1f, 1f, 1f).tex(maxUV, maxV).lightmap(240, 240).endVertex();
        buffer.pos(cornerX, maxY, southZ).color(1f, 1f, 1f, 1f).tex(minUV, maxV).lightmap(240, 240).endVertex();

        //east
        buffer.pos(eastX, 3/16, cornerZ).color(1f, 1f, 1f, 1f).tex(minUV, minUV).lightmap(240, 240).endVertex();
        buffer.pos(eastX, 3/16, cornerZ+2/16).color(1f, 1f, 1f, 1f).tex(maxUV, minUV).lightmap(240, 240).endVertex();
        buffer.pos(eastX, maxY, cornerZ+2/16).color(1f, 1f, 1f, 1f).tex(maxUV, maxV).lightmap(240, 240).endVertex();
        buffer.pos(eastX, maxY, cornerZ).color(1f, 1f, 1f, 1f).tex(minUV, maxV).lightmap(240, 240).endVertex();

        //west
        buffer.pos(westX, 3/16, cornerZ).color(1f, 1f, 1f, 1f).tex(minUV, minUV).lightmap(240, 240).endVertex();
        buffer.pos(westX, 3/16, cornerZ+2/16).color(1f, 1f, 1f, 1f).tex(maxUV, minUV).lightmap(240, 240).endVertex();
        buffer.pos(westX, maxY, cornerZ+2/16).color(1f, 1f, 1f, 1f).tex(maxUV, maxV).lightmap(240, 240).endVertex();
        buffer.pos(westX, maxY, cornerZ).color(1f, 1f, 1f, 1f).tex(minUV, maxV).lightmap(240, 240).endVertex();
    }
    
    public abstract EnumFacing getFacing(T tile);
    public abstract TextureAtlasSprite getLightupTexture(T tile);

}
