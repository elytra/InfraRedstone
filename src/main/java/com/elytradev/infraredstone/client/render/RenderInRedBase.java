package com.elytradev.infraredstone.client.render;

import com.elytradev.infraredstone.InRedLog;
import com.elytradev.infraredstone.tile.TileEntityIRComponent;

import com.elytradev.infraredstone.util.Torch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.animation.FastTESR;

public abstract class RenderInRedBase<T extends TileEntityIRComponent> extends FastTESR<T>{

    protected Torch[] torches = new Torch[]{};

    @Override
    public void renderTileEntityFast(T te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        buffer.setTranslation(x, y, z);
        TextureAtlasSprite sprite = getLightupTexture(te);
        if (sprite!=null) renderTopFace(buffer, sprite, getFacing(te));
        if (torches != null && torches.length != 0) {
            for (Torch torch : torches) {
                TextureAtlasSprite light = (torch.isLit) ? Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("infraredstone:blocks/lights") : null;
                if (light != null) renderLight(buffer, light, torch.cornerX, torch.cornerZ, torch.isFullHeight, getFacing(te));
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

    public void renderLight(BufferBuilder buffer, TextureAtlasSprite tex, Double cornerX, Double cornerZ, boolean isFullHeight, EnumFacing facing) {
        double adaptedX = cornerX;
        double adaptedZ = cornerZ;
        switch(facing) {
            case EAST:
                adaptedX = -1*cornerZ + 14/16d;
                adaptedZ = cornerX;
                break;
            case SOUTH:
                adaptedZ = -1*adaptedZ + 14/16d;
                adaptedX = -1*adaptedX + 14/16d;
                break;
            case WEST:
                adaptedX = cornerZ;
                adaptedZ = -1*cornerX + 14/16d;
                break;
            default:
                break;
        }
        double eastX = adaptedX-0.002;
        double westX = adaptedX+0.127;
        double northZ = adaptedZ-0.002;
        double southZ = adaptedZ+0.127;
        double topY = (isFullHeight) ? 0.3145 : 0.252;
        double maxY = (isFullHeight) ? 5/16d : 1/4d;
        double minUV = 7;
        double maxUV = 9;
        double maxV = (isFullHeight) ? 9 : 8;

        //top
        buffer.pos(adaptedX, topY, adaptedZ).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(minUV), tex.getInterpolatedV(minUV)).lightmap(240, 240).endVertex();
        buffer.pos(adaptedX+2/16d, topY, adaptedZ).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(maxUV), tex.getInterpolatedV(minUV)).lightmap(240, 240).endVertex();
        buffer.pos(adaptedX+2/16d, topY, adaptedZ+2/16d).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(maxUV), tex.getInterpolatedV(maxUV)).lightmap(240, 240).endVertex();
        buffer.pos(adaptedX, topY, adaptedZ+2/16d).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(minUV), tex.getInterpolatedV(maxUV)).lightmap(240, 240).endVertex();

        //north
        buffer.pos(adaptedX, 3/16d, northZ).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(minUV), tex.getInterpolatedV(minUV)).lightmap(240, 240).endVertex();
        buffer.pos(adaptedX+2/16d, 3/16d, northZ).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(maxUV), tex.getInterpolatedV(minUV)).lightmap(240, 240).endVertex();
        buffer.pos(adaptedX+2/16d, maxY, northZ).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(maxUV), tex.getInterpolatedV(maxV)).lightmap(240, 240).endVertex();
        buffer.pos(adaptedX, maxY, northZ).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(minUV), tex.getInterpolatedV(maxV)).lightmap(240, 240).endVertex();

        //south
        buffer.pos(adaptedX, 3/16d, southZ).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(minUV), tex.getInterpolatedV(minUV)).lightmap(240, 240).endVertex();
        buffer.pos(adaptedX+2/16d, 3/16d, southZ).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(maxUV), tex.getInterpolatedV(minUV)).lightmap(240, 240).endVertex();
        buffer.pos(adaptedX+2/16d, maxY, southZ).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(maxUV), tex.getInterpolatedV(maxV)).lightmap(240, 240).endVertex();
        buffer.pos(adaptedX, maxY, southZ).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(minUV), tex.getInterpolatedV(maxV)).lightmap(240, 240).endVertex();

        //east
        buffer.pos(eastX, 3/16d, adaptedZ).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(minUV), tex.getInterpolatedV(minUV)).lightmap(240, 240).endVertex();
        buffer.pos(eastX, 3/16d, adaptedZ+2/16d).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(maxUV), tex.getInterpolatedV(minUV)).lightmap(240, 240).endVertex();
        buffer.pos(eastX, maxY, adaptedZ+2/16d).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(maxUV), tex.getInterpolatedV(maxV)).lightmap(240, 240).endVertex();
        buffer.pos(eastX, maxY, adaptedZ).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(minUV), tex.getInterpolatedV(maxV)).lightmap(240, 240).endVertex();

        //west
        buffer.pos(westX, 3/16d, adaptedZ).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(minUV), tex.getInterpolatedV(minUV)).lightmap(240, 240).endVertex();
        buffer.pos(westX, 3/16d, adaptedZ+2/16d).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(maxUV), tex.getInterpolatedV(minUV)).lightmap(240, 240).endVertex();
        buffer.pos(westX, maxY, adaptedZ+2/16d).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(maxUV), tex.getInterpolatedV(maxV)).lightmap(240, 240).endVertex();
        buffer.pos(westX, maxY, adaptedZ).color(1f, 1f, 1f, 1f).tex(tex.getInterpolatedU(minUV), tex.getInterpolatedV(maxV)).lightmap(240, 240).endVertex();
    }
    
    public abstract EnumFacing getFacing(T tile);
    public abstract TextureAtlasSprite getLightupTexture(T tile);

}
