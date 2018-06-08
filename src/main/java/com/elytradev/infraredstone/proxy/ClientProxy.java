package com.elytradev.infraredstone.proxy;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.client.render.*;
import com.elytradev.infraredstone.tile.*;
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
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOscillator.class, new RenderOscillator());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTransistor.class, new RenderTransistor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShifter.class, new RenderShifter());
    }
    
    @SubscribeEvent
    public void onStitchTexture(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/diode_glow"));
        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/oscillator_glow"));
        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/transistor_glow"));
        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/shifter_glow_left"));
        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/shifter_glow_right"));
        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/shifter_glow_center"));
        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/shifter_glow_center_left"));
        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/shifter_glow_center_right"));
    }
    
    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(InfraRedstone.modId, id), "inventory"));
    }
}
