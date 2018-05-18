package com.elytradev.infraredstone.proxy;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.client.render.RenderLiquidCrystal;
import com.elytradev.infraredstone.tile.TileEntityLiquidCrystal;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiquidCrystal.class, new RenderLiquidCrystal());
    }

    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(InfraRedstone.modId, id), "inventory"));
    }
}
