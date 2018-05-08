package com.elytradev.infraredstone.item;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.util.C28n;
import com.elytradev.infraredstone.util.InRedConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ItemBase extends Item {
    protected String name;
    private String oreName;

    public ItemBase(String name) {
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(InfraRedstone.creativeTab);
    }

    public ItemBase(String name, int maxStack) {
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        this.maxStackSize = maxStack;
    }

    public ItemBase(String name, int maxStack, String oreDict){
        this(name,maxStack);
        this.oreName = oreDict;
    }

    public ItemBase(String name, String oreDict){
        this(name);
        this.oreName = oreDict;
    }

    public void registerItemModel() {
        InfraRedstone.proxy.registerItemRenderer(this, 0, name);
    }

    @Override
    public ItemBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(InfraRedstone.creativeTab);
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (GuiScreen.isShiftKeyDown()) {
            C28n.formatList(tooltip, "tooltip.ir." + name);
        } else C28n.formatList(tooltip,"preview.ir." + name);
    }

    public void initOreDict() {
        if(oreName==null){return;}
        OreDictionary.registerOre(oreName, this);
    }

}