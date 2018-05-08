package com.elytradev.infraredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static final BlockBase FINE_LEVER = new BlockBase(Material.ROCK, "fine_lever");

    public static IBlockBase[] allBlocks = {
            FINE_LEVER
    };

    public static void register(IForgeRegistry<Block> registry) {
        for (int i = 0; i < allBlocks.length; i++) {
            IBlockBase block = allBlocks[i];
            registry.register(block.toBlock());
        }

//        GameRegistry.registerTileEntity(BOILER.getTileEntityClass(), BOILER.getRegistryName().toString());

    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        for (int i = 0; i < allBlocks.length; i++) {
            IBlockBase block = allBlocks[i];
            registry.register(block.createItemBlock());
        }

    }

    public static void registerModels() {
        for (int i = 0; i < allBlocks.length; i++) {
            IBlockBase block = allBlocks[i];
            block.registerItemModel(Item.getItemFromBlock(block.toBlock()));
        }
    }
}
