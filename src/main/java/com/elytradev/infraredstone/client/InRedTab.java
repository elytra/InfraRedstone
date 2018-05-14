package com.elytradev.infraredstone.client;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.ModBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;

public class InRedTab extends CreativeTabs {
    public InRedTab() {
        super(InfraRedstone.modId);
        //setBackgroundImageName("betterboilers.png");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModBlocks.INFRA_REDSTONE);
    }
}
