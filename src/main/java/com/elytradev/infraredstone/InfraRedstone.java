package com.elytradev.infraredstone;

import com.elytradev.infraredstone.util.InRedConfig;
import com.elytradev.concrete.inventory.IContainerInventoryHolder;
import com.elytradev.concrete.inventory.gui.client.ConcreteGui;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.client.InRedTab;
import com.elytradev.infraredstone.item.ModItems;
import com.elytradev.infraredstone.logic.IInfraComparator;
import com.elytradev.infraredstone.logic.IInfraRedstone;
import com.elytradev.infraredstone.logic.impl.InfraComparatorSerializer;
import com.elytradev.infraredstone.logic.impl.InfraRedstoneHandler;
import com.elytradev.infraredstone.logic.impl.InfraRedstoneSerializer;
import com.elytradev.infraredstone.proxy.CommonProxy;
import com.elytradev.infraredstone.util.InRedRecipes;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;


@Mod(modid = InfraRedstone.modId, name = InfraRedstone.name, version = InfraRedstone.version)
public class InfraRedstone {
    public static final String modId = "infraredstone";
    public static final String name  = "Infra-Redstone";
    public static final String version = "@VERSION@";
    public static InRedConfig config;

    @Mod.Instance(modId)
    public static InfraRedstone instance;

    public static final InRedTab creativeTab = new InRedTab();

    @CapabilityInject(IInfraRedstone.class)
    public static final Capability<IInfraRedstone> CAPABILITY_IR = null;
    @CapabilityInject(IInfraComparator.class)
    public static final Capability<IInfraComparator> CAPABILITY_IR_COMPARATOR = null;
    
    static {
        FluidRegistry.enableUniversalBucket();
    }

    @SidedProxy(serverSide = "com.elytradev.infraredstone.proxy.CommonProxy", clientSide = "com.elytradev.infraredstone.proxy.ClientProxy")
    public static CommonProxy proxy;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        InRedLog.info(name + " is loading!");
        
        CapabilityManager.INSTANCE.register(IInfraRedstone.class, new InfraRedstoneSerializer(), InfraRedstoneHandler::new);
        CapabilityManager.INSTANCE.register(IInfraComparator.class, new InfraComparatorSerializer(), InfraRedstoneHandler::new);
        
        MinecraftForge.EVENT_BUS.register(InRedRecipes.class);
        config = InRedConfig.createConfig(event);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new IGuiHandler() {
//            public static final int BOILER = 0;

            @Nullable
            @Override
            public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                switch (ID) {
//                    case BOILER:
//                        return new BoilerContainer(
//                                player.inventory, ((IContainerInventoryHolder)world.getTileEntity(new BlockPos(x,y,z))).getContainerInventory(),
//                                (TileEntityBoilerController)world.getTileEntity(new BlockPos(x,y,z)));
                    default:
                        return null;
                }

            }

            @Nullable
            @Override
            @SideOnly(Side.CLIENT)
            public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
                switch (ID) {
//                    case BOILER:
//                        BoilerContainer boilerContainer = new BoilerContainer(
//                                player.inventory, ((IContainerInventoryHolder)world.getTileEntity(new BlockPos(x,y,z))).getContainerInventory(),
//                                (TileEntityBoilerController)world.getTileEntity(new BlockPos(x,y,z)));
//                        return new ConcreteGui(boilerContainer);
                    default:
                        return null;
                }

            }
        });
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        //MinecraftForge.EVENT_BUS.register(new SoundRegisterListener());
        //MinecraftForge.EVENT_BUS.register(LightHandler.class);
        ModItems.registerOreDict(); // register oredict
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            ModItems.register(event.getRegistry());
            ModBlocks.registerItemBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            ModItems.registerModels();
            ModBlocks.registerModels();
        }

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            ModBlocks.register(event.getRegistry());
        }
    }
}
