package com.elytradev.infraredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static final CableInfraRedstone INFRA_REDSTONE = new CableInfraRedstone();
    public static final BlockFineLever FINE_LEVER = new BlockFineLever();
    public static final BlockDiode DIODE = new BlockDiode();

    public static Block[] allBlocks = {
            INFRA_REDSTONE, FINE_LEVER, DIODE
    };

    public static void register(IForgeRegistry<Block> registry) {
        for (Block block: allBlocks) {
            registry.register(block);
        }

        GameRegistry.registerTileEntity(FINE_LEVER.getTileEntityClass(), FINE_LEVER.getRegistryName().toString());
        GameRegistry.registerTileEntity(DIODE.getTileEntityClass(), DIODE.getRegistryName().toString());

    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        for (Block block: allBlocks) {
            if (block instanceof IBlockBase) registry.register(((IBlockBase)block).createItemBlock());
            else registry.register(new ItemBlock(block));
        }

    }

    public static void registerModels() {
        for (Block block: allBlocks) {
            if (block instanceof IBlockBase) ((IBlockBase)block).registerItemModel(Item.getItemFromBlock(block));
        }
    }
}
