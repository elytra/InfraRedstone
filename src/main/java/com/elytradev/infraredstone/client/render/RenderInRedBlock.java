package com.elytradev.infraredstone.client.render;

import com.elytradev.infraredstone.tile.TileEntityInRedBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.model.animation.FastTESR;

public class RenderInRedBlock<T extends TileEntityInRedBlock> extends FastTESR<T> {

    private double minDist = -0.002;
    private double maxDist = 1.002;

    @Override
    public void renderTileEntityFast(T te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        buffer.setTranslation(x, y, z);
        TextureAtlasSprite tex = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("infraredstone:blocks/infra_redstone_block");
        renderUp(buffer, tex);
        renderDown(buffer, tex);
        renderNorth(buffer, tex);
        renderSouth(buffer, tex);
        renderEast(buffer, tex);
        renderWest(buffer, tex);
    }

    public void renderUp(BufferBuilder buffer, TextureAtlasSprite tex) {
        buffer.pos(0, maxDist, 0).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMinV()).lightmap(240, 240).endVertex();
        buffer.pos(1, maxDist, 0).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMinV()).lightmap(240, 240).endVertex();
        buffer.pos(1, maxDist, 1).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMaxV()).lightmap(240, 240).endVertex();
        buffer.pos(0, maxDist, 1).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMaxV()).lightmap(240, 240).endVertex();
    }

    public void renderDown(BufferBuilder buffer, TextureAtlasSprite tex) {
        buffer.pos(0, minDist, 0).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMinV()).lightmap(240, 240).endVertex();
        buffer.pos(1, minDist, 0).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMinV()).lightmap(240, 240).endVertex();
        buffer.pos(1, minDist, 1).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMaxV()).lightmap(240, 240).endVertex();
        buffer.pos(0, minDist, 1).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMaxV()).lightmap(240, 240).endVertex();
    }

    public void renderNorth(BufferBuilder buffer, TextureAtlasSprite tex) {
        buffer.pos(minDist, 0, 0).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMinV()).lightmap(240, 240).endVertex();
        buffer.pos(minDist, 0, 1).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMinV()).lightmap(240, 240).endVertex();
        buffer.pos(minDist, 1, 1).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMaxV()).lightmap(240, 240).endVertex();
        buffer.pos(minDist, 1, 0).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMaxV()).lightmap(240, 240).endVertex();
    }

    public void renderSouth(BufferBuilder buffer, TextureAtlasSprite tex) {
        buffer.pos(maxDist, 0, 0).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMinV()).lightmap(240, 240).endVertex();
        buffer.pos(maxDist, 0, 1).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMinV()).lightmap(240, 240).endVertex();
        buffer.pos(maxDist, 1, 1).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMaxV()).lightmap(240, 240).endVertex();
        buffer.pos(maxDist, 1, 0).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMaxV()).lightmap(240, 240).endVertex();
    }

    public void renderEast(BufferBuilder buffer, TextureAtlasSprite tex) {
        buffer.pos(0, 0, maxDist).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMinV()).lightmap(240, 240).endVertex();
        buffer.pos(1, 0, maxDist).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMinV()).lightmap(240, 240).endVertex();
        buffer.pos(1, 1, maxDist).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMaxV()).lightmap(240, 240).endVertex();
        buffer.pos(0, 1, maxDist).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMaxV()).lightmap(240, 240).endVertex();
    }

    public void renderWest(BufferBuilder buffer, TextureAtlasSprite tex) {
        buffer.pos(0, 0, minDist).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMinV()).lightmap(240, 240).endVertex();
        buffer.pos(1, 0, minDist).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMinV()).lightmap(240, 240).endVertex();
        buffer.pos(1, 1, minDist).color(1f, 1f, 1f, 1f).tex(tex.getMaxU(), tex.getMaxV()).lightmap(240, 240).endVertex();
        buffer.pos(0, 1, minDist).color(1f, 1f, 1f, 1f).tex(tex.getMinU(), tex.getMaxV()).lightmap(240, 240).endVertex();
    }

}
