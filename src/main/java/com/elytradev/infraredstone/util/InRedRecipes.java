package com.elytradev.infraredstone.util;

import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.item.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

public class InRedRecipes {

    @SubscribeEvent
    public static void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {

        IForgeRegistry<IRecipe> r = event.getRegistry();

//         Crafting bench recipes

//        Gates
        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.GATE_AND, 1),
                " t ", "trt", "ptp",
                'r', "dustRedstone",
                't', new ItemStack(Blocks.REDSTONE_TORCH),
                'p', new ItemStack(ModItems.PCB)
        ));

        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.GATE_XOR, 1),
                "tr ", "prp", " rt",
                'r', "dustRedstone",
                't', new ItemStack(Blocks.REDSTONE_TORCH),
                'p', new ItemStack(ModItems.PCB)
        ));

        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.GATE_XOR, 1),
                " rt", "prp", "tr ",
                'r', "dustRedstone",
                't', new ItemStack(Blocks.REDSTONE_TORCH),
                'p', new ItemStack(ModItems.PCB)
        ));

        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.GATE_NOT, 1),
                "t", "p",
                't', new ItemStack(Blocks.REDSTONE_TORCH),
                'p', new ItemStack(ModItems.PCB)
        ));

//        Modules
        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.IN_RED_BLOCK, 1),
                "rrr", "rrr", "rrr",
                'r', new ItemStack(ModBlocks.INFRA_REDSTONE)
        ));
        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.INFRA_REDSTONE, 9),
                "b",
                'b', new ItemStack(ModBlocks.IN_RED_BLOCK)
        ));

        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.DIODE, 1),
                "nnn", "rpr",
                'n', "nuggetGold",
                'r', "dustRedstone",
                'p', new ItemStack(ModItems.PCB)
        ));

        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.TRANSISTOR, 1),
                " r ", "rpr", " r ",
                'r', "dustRedstone",
                'p', new ItemStack(ModItems.PCB)
        ));

        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.OSCILLATOR, 1),
                " q ", "rpr",
                'r', "dustRedstone",
                'q', "gemQuartz",
                'p', new ItemStack(ModItems.PCB)
        ));

        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.ENCODER, 1),
                "rpr", "qeq",
                'r', "dustRedstone",
                'q', "gemQuartz",
                'p', new ItemStack(ModItems.PCB),
                'e', new ItemStack(Items.SPIDER_EYE)
        ));

        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.SHIFTER, 1),
                " r ", "rpr",
                'r', "dustRedstone",
                'p', new ItemStack(ModItems.PCB)
        ));

//        Other
        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.INFRA_REDSTONE, 3),
                "rrr", "ggg",
                'r', "dustRedstone",
                'g', "blockGlass"
        ));

        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.IN_RED_SCAFFOLD, 1),
                "sss", "prp", "sss",
                's', "stickWood",
                'p', "plankWood",
                'r', new ItemStack(ModBlocks.INFRA_REDSTONE)
        ));
        
//        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:blocks"), new ItemStack(ModBlocks.DEVICE_LIQUID_CRYSTAL, 8),
//                "ggg", "gqg", "gpg",
//                'q', "gemQuartz",
//                'g', "blockGlass",
//                'p', new ItemStack(ModItems.PCB)
//        ));

//        Items
        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:items"), new ItemStack(ModItems.PCB, 1),
                "c", "r", "s",
                'c', new ItemStack(Items.CLAY_BALL),
                'r', new ItemStack(ModBlocks.INFRA_REDSTONE),
                's', new ItemStack(Blocks.STONE_SLAB)
        ));
        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:items"), new ItemStack(ModItems.PCB, 2),
                "cc", "rr", "ss",
                'c', new ItemStack(Items.CLAY_BALL),
                'r', new ItemStack(ModBlocks.INFRA_REDSTONE),
                's', new ItemStack(Blocks.STONE_SLAB)
        ));
        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:items"), new ItemStack(ModItems.PCB, 3),
                "ccc", "rrr", "sss",
                'c', new ItemStack(Items.CLAY_BALL),
                'r', new ItemStack(ModBlocks.INFRA_REDSTONE),
                's', new ItemStack(Blocks.STONE_SLAB)
        ));
        recipe(r, new ShapedOreRecipe(new ResourceLocation("infraredstone:items"), new ItemStack(ModItems.MULTIMETER, 1),
                "r r", "rpr", " r ",
                'r', new ItemStack(ModBlocks.INFRA_REDSTONE),
                'p', new ItemStack(ModItems.PCB)
        ));

    }

    public static <T extends IRecipe> T recipe(IForgeRegistry<IRecipe> registry, T t) {
        t.setRegistryName(new ResourceLocation(t.getRecipeOutput().getItem().getRegistryName()+"_"+t.getRecipeOutput().getItemDamage()));
        registry.register(t);
        return t;
    }

}
