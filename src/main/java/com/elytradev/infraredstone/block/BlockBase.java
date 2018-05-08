package com.elytradev.infraredstone.block;

import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.util.C28n;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockBase extends Block implements IBlockBase {

    protected String name;

    public BlockBase(Material material, String name) {
        super(material);

        this.name = name;

        setUnlocalizedName(name);
        setRegistryName(name);

        this.setHarvestLevel("pickaxe", 1);
        this.setHardness(1.4f);
        this.setResistance(8f); //actually quite susceptible to explosions
    }

    public void registerItemModel(Item itemBlock) {
        InfraRedstone.proxy.registerItemRenderer(itemBlock, 0, name);
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(getRegistryName());
    }

    @Override
    public BlockBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (GuiScreen.isShiftKeyDown()) {
            C28n.formatList(tooltip, "tooltip.ir." + name);
        } else C28n.formatList(tooltip,"preview.ir." + name);
    }

    public Block toBlock() {
        return this;
    }
}