package com.elytradev.infraredstone.client;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.item.ModItems;
import com.elytradev.infraredstone.InfraRedstone;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.creativetab.CreativeTabs;

public class InRedTab extends CreativeTabs {
    public InRedTab() {
        super(InfraRedstone.modId);
        //setBackgroundImageName("betterboilers.png");
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Items.REDSTONE);
    }
}
