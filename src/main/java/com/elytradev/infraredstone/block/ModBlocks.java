package com.elytradev.infraredstone.block;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {

    public static final CableInfraRedstone INFRA_REDSTONE = new CableInfraRedstone();
    public static final BlockFineLever FINE_LEVER = new BlockFineLever();
    public static final BlockDiode DIODE = new BlockDiode();
    public static final BlockGateNot GATE_NOT = new BlockGateNot();
    public static final BlockGateAnd GATE_AND = new BlockGateAnd();
    public static final BlockGateXor GATE_XOR = new BlockGateXor();
    public static final BlockOscillator OSCILLATOR = new BlockOscillator();
    public static final BlockTransistor TRANSISTOR = new BlockTransistor();

    public static Block[] allBlocks = {
            INFRA_REDSTONE, FINE_LEVER, DIODE, GATE_NOT, GATE_AND, GATE_XOR, OSCILLATOR, TRANSISTOR
    };

    public static void register(IForgeRegistry<Block> registry) {
        for (Block block: allBlocks) {
            registry.register(block);
        }

        GameRegistry.registerTileEntity(FINE_LEVER.getTileEntityClass(), FINE_LEVER.getRegistryName().toString());
        GameRegistry.registerTileEntity(DIODE.getTileEntityClass(), DIODE.getRegistryName().toString());
        GameRegistry.registerTileEntity(GATE_NOT.getTileEntityClass(), GATE_NOT.getRegistryName().toString());
        GameRegistry.registerTileEntity(GATE_AND.getTileEntityClass(), GATE_AND.getRegistryName().toString());
        GameRegistry.registerTileEntity(GATE_XOR.getTileEntityClass(), GATE_XOR.getRegistryName().toString());
        GameRegistry.registerTileEntity(OSCILLATOR.getTileEntityClass(), OSCILLATOR.getRegistryName().toString());
        GameRegistry.registerTileEntity(TRANSISTOR.getTileEntityClass(), TRANSISTOR.getRegistryName().toString());

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
