package com.elytradev.infraredstone.proxy;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.client.render.RenderDiode;
import com.elytradev.infraredstone.client.render.RenderLiquidCrystal;
import com.elytradev.infraredstone.tile.TileEntityDiode;
import com.elytradev.infraredstone.tile.TileEntityLiquidCrystal;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiquidCrystal.class, new RenderLiquidCrystal());
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDiode.class, new RenderDiode());
    }
    
    @SubscribeEvent
    public void onStitchTexture(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/diode_glow"));
    }
    
    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(InfraRedstone.modId, id), "inventory"));
    }
}
