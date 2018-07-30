package com.elytradev.infraredstone.proxy;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.client.render.*;
import com.elytradev.infraredstone.tile.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiquidCrystal.class, new RenderLiquidCrystal());
        
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDiode.class, new RenderDiode());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOscillator.class, new RenderOscillator());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTransistor.class, new RenderTransistor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityShifter.class, new RenderShifter());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInRedBlock.class, new RenderInRedBlock<>());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGateAnd.class, new RenderGateAnd());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGateXor.class, new RenderGateXor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGateNot.class, new RenderGateNot());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityEncoder.class, new RenderEncoder());
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

        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/infra_redstone_block"));

        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/gate_and_glow"));

        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/gate_xor_glow"));

        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/gate_not_glow"));
        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/gate_not_glow_in"));
        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/gate_not_glow_out"));

        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/encoder_glow"));

        event.getMap().registerSprite(new ResourceLocation("infraredstone:blocks/lights"));
    }
    
    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(InfraRedstone.modId, id), "inventory"));
    }

    @Override
    public void playUISound(SoundEvent sound, float pitch) {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(sound, pitch));

    }
}
