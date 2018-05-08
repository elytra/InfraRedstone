package com.elytradev.infraredstone.item;

import com.elytradev.infraredstone.InfraRedstone;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

    public static ItemBase INFRA_REDSTONE = new ItemBase("infra-redstone");
    public static ItemBase[] allItems = {
            INFRA_REDSTONE
    };

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(allItems);
    }

    public static void registerModels() {
        for (int i = 0; i < allItems.length ; i++) {
            allItems[i].registerItemModel();
        }
    }

    public static void registerOreDict() {
        for (int i = 0; i < allItems.length ; i++) {
            allItems[i].initOreDict();
        }
    }
}
