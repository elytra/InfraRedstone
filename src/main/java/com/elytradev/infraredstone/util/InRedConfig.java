package com.elytradev.infraredstone.util;

import com.elytradev.infraredstone.InRedLog;
import com.elytradev.infraredstone.InfraRedstone;
import net.minecraftforge.common.config.Configuration;
import com.elytradev.concrete.config.ConcreteConfig;
import com.elytradev.concrete.config.ConfigValue;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class InRedConfig extends ConcreteConfig {

    public File configFolder;

    @ConfigValue(type = Property.Type.INTEGER, category = "BoilerUsage", comment = "The amount of ticks needed for one boiler cycle, sans calculation. Actual value will be 200/(<number of firebox blocks> * <number of active fuel sources>.")
    public static int ticksToBoil = 200;
    @ConfigValue(type = Property.Type.INTEGER, category = "BoilerUsage", comment = "The amount of steam produced per boiler cycle. Water cost will always be 2x the resulting steam.")
    public static int steamPerBoil = 800;
    @ConfigValue(type = Property.Type.DOUBLE, category = "BoilerUsage", comment = "The multiplier for how much steam is produced per tick with a pump. Steam production calculated by <number of firebox blocks> * <number of active fuel sources> * <standard steam/tick> * <this multiplier>.")
    public static double pumpMultiplier = 1;
    @ConfigValue(type = Property.Type.INTEGER, category = "BoilerUsage", comment = "How much steam a pump will auto-output a tick. Can still be extracted from faster with a machine.")
    public static int pumpDrain = 500;

    @ConfigValue(type = Property.Type.INTEGER, category = "TurbineUsage", comment = "base amount of rotors used to calculate a turbine's diminishing returns. RF generation will always be 2x the steam cost.")
    public static int rotorBaseCount = 4;
    @ConfigValue(type = Property.Type.INTEGER, category = "TurbineUsage", comment = "The amount of steam consumed in a turbine with rotorBaseCount rotors. Used to calculate diminishing returns. RF generation will always be 2x the steam cost.")
    public static int steamBaseUse = 40;
    @ConfigValue(type = Property.Type.INTEGER, category = "TurbineUsage", comment = "How much RF/T the turbine power tap can transfer.")
    public static int turbineOut = 400;

    @ConfigValue(type = Property.Type.INTEGER, category = "Multiblock", comment = "The maximum amount of blocks that can be added to a standard multiblock. Some controllers may have different maxima. Includes all of the multiblock's components.")
    public static int defaultMaxMultiblock = 1000;
    @ConfigValue(type = Property.Type.INTEGER, category = "Multiblock", comment = "The minimum amount of blocks that can be added to a standard multiblock. Some controllers may have different minima. Includes all of the multiblock's components. Set to 0 for no minimum.")
    public static int defaultMinMultiblock = 36;

    private InRedConfig(File configFile) {
        super(configFile, InfraRedstone.modId);
        this.configFolder = configFile.getParentFile();
    }

    public static InRedConfig createConfig(FMLPreInitializationEvent event) {
        //Move config file if it exists.
        File bbFolder = new File(event.getModConfigurationDirectory(), "infra-redstone");
        bbFolder.mkdirs();
        if (event.getSuggestedConfigurationFile().exists()) {
            event.getSuggestedConfigurationFile().renameTo(new File(bbFolder, "infra-redstone.cfg"));
        }

        InRedConfig config = new InRedConfig(new File(bbFolder, "infra-redstone.cfg"));
        config.loadConfig();
        return config;
    }
}
