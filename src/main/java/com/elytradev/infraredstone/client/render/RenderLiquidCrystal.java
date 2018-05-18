package com.elytradev.infraredstone.client.render;

import com.elytradev.infraredstone.tile.TileEntityLiquidCrystal;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.animation.FastTESR;

public class RenderLiquidCrystal extends FastTESR<TileEntityLiquidCrystal> {

    @Override
    public void renderTileEntityFast(TileEntityLiquidCrystal te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        buffer.setTranslation(0, 0, 0);
        TextureAtlasSprite dirt = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/dirt");
        GlStateManager.disableAlpha();
        buffer.setTranslation(x, y, z);
        for (EnumFacing face : EnumFacing.VALUES) {
            renderFace(buffer, dirt, face, te.animationProgress[face.ordinal()], te.getFaceCardinality(face), partialTicks);
        }
    }
    
    public void renderFace(BufferBuilder buffer, TextureAtlasSprite dummy, EnumFacing face, float prog, int cardinality, float partialTicks) {
        if (cardinality > 0) {
            prog += (0.025f*cardinality)*partialTicks;
        } else {
            prog -= 0.05f*partialTicks;
            face = face.getOpposite();
        }
        prog = Math.min(1, Math.max(prog, 0));
        if (prog > 0) {
            float progi = 1-prog;
            float prog25 = Math.min(prog, 0.25f)*4;
            float prog50 = Math.min(prog, 0.5f)*2;
            float prog25i = 1-prog25;
            float prog50i = 1-prog50;
            // I'm sorry.
            if (face == EnumFacing.NORTH) {
                buffer.pos(0, 0, 0.002).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0, 0.002).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 1, 0.002).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 1, 0.002).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0.002, 0, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.002, 1, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.002, 1, 1*prog25).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0.002, 0, 1*prog25).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0.998, 0, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.998, 1, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.998, 1, 1*prog25).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0.998, 0, 1*prog25).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, 0.002, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.002, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.002, 1*prog25).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 0.002, 1*prog25).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, 0.998, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.998, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.998, 1*prog25).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 0.998, 1*prog25).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, 0, 0.998*prog25).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0, 0.998*prog25).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 1, 0.998*prog25).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 1, 0.998*prog25).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
            } else if (face == EnumFacing.SOUTH) {
                buffer.pos(0, 0, 0.999).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0, 0.999).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 1, 0.999).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 1, 0.999).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0.001, 0, 1*prog25i).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.001, 1, 1*prog25i).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.001, 1, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0.001, 0, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0.999, 0, 1*prog25i).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.999, 1, 1*prog25i).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.999, 1, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0.999, 0, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, 0.001, 1*prog25i).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.001, 1*prog25i).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.001, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 0.001, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, 0.999, 1*prog25i).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.999, 1*prog25i).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.999, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 0.999, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, 0, (0.998*prog25i)+0.001).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0, (0.998*prog25i)+0.001).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 1, (0.998*prog25i)+0.001).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 1, (0.998*prog25i)+0.001).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
            } else if (face == EnumFacing.EAST) {
                buffer.pos(0.997, 0, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.997, 0, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.997, 1, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0.997, 1, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(1*prog25i, 0, 0.003).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1*prog25i, 1, 0.003).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 1, 0.003).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0, 0.003).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(1*prog25i, 0, 0.997).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1*prog25i, 1, 0.997).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 1, 0.997).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0, 0.997).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(1*prog25i, 0.003, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1*prog25i, 0.003, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.003, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.003, 0).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(1*prog25i, 0.997, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1*prog25i, 0.997, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.997, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.997, 0).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos((0.996*prog25i)+0.001, 0, 0).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos((0.996*prog25i)+0.001, 0, 1).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos((0.996*prog25i)+0.001, 1, 1).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos((0.996*prog25i)+0.001, 1, 0).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
            } else if (face == EnumFacing.WEST) {
                buffer.pos(0.004, 0, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.004, 0, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.004, 1, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0.004, 1, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, 0, 0.004).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 1, 0.004).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1*prog25, 1, 0.004).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(1*prog25, 0, 0.004).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, 0, 0.996).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 1, 0.996).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1*prog25, 1, 0.996).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(1*prog25, 0, 0.996).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, 0.004, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 0.004, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1*prog25, 0.004, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(1*prog25, 0.004, 0).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, 0.996, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 0.996, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1*prog25, 0.996, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(1*prog25, 0.996, 0).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0.996*prog25, 0, 0).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.996*prog25, 0, 1).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.996*prog25, 1, 1).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0.996*prog25, 1, 0).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
            } else if (face == EnumFacing.DOWN) {
                buffer.pos(0, 0.005, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 0.005, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.005, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.005, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, 0, 0.005).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0, 0.005).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 1*prog25, 0.005).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 1*prog25, 0.005).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, 0, 0.995).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0, 0.995).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 1*prog25, 0.995).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 1*prog25, 0.995).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0.005, 0, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.005, 0, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.005, 1*prog25, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0.005, 1*prog25, 0).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0.995, 0, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.995, 0, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.995, 1*prog25, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0.995, 1*prog25, 0).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, 0.995*prog25, 0).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 0.995*prog25, 1).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.995*prog25, 1).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.995*prog25, 0).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
            } else if (face == EnumFacing.UP) {
                buffer.pos(0, 0.994, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 0.994, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.994, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 0.994, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, 1*prog25i, 0.006).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 1*prog25i, 0.006).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 1, 0.006).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 1, 0.006).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, 1*prog25i, 0.994).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 1*prog25i, 0.994).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, 1, 0.994).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0, 1, 0.994).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0.006, 1*prog25i, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.006, 1*prog25i, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.006, 1, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0.006, 1, 0).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0.994, 1*prog25i, 0).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.994, 1*prog25i, 1).color(0f, 0f, 0f, 0.95f*(prog25)).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0.994, 1, 1).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(0.994, 1, 0).color(0f, 0f, 0f, 0.95f*prog50).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                
                buffer.pos(0, (0.993*prog25i)+0.001, 0).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMinU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(0, (0.993*prog25i)+0.001, 1).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMaxU(), dummy.getMinV()).lightmap(240, 240).endVertex();
                buffer.pos(1, (0.993*prog25i)+0.001, 1).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMaxU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
                buffer.pos(1, (0.993*prog25i)+0.001, 0).color(0f, 0f, 0f, 0.95f*prog).tex(dummy.getMinU(), dummy.getMaxV()).lightmap(240, 240).endVertex();
            }
        }
    }

}
