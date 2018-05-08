package com.elytradev.infraredstone.util;

import com.google.common.base.Joiner;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;

public class ConfigKey<T> {

    protected final String category;
    protected final String key;
    protected final Type type;
    protected final String description;

    protected final T defaultValue;

    protected ConfigKey(String category, String key, Type type, T defaultValue, String... description) {
        this.category = category;
        this.key = key;
        this.type = type;
        this.defaultValue = defaultValue;
        this.description = Joiner.on('\n').join(description);
    }

    public void set(Configuration config, T value) {
        getProperty(config).set(value.toString());
    }

    @SuppressWarnings("unchecked")
    public T get(Configuration config) {
        switch (type) {
            case BOOLEAN:
                return (T)Boolean.valueOf(getProperty(config).getBoolean());
            case DOUBLE:
                return (T)Double.valueOf(getProperty(config).getDouble());
            case INTEGER:
                return (T)Integer.valueOf(getProperty(config).getInt());
            case STRING:
                return (T)getProperty(config).getString();
            default:
                return null;
        }
    }

    protected Property getProperty(Configuration config) {
        return config.get(category, key, defaultValue.toString(), description, type);
    }


    public static ConfigKey<Boolean> create(String category, String key, boolean defaultValue, String... description) {
        return new ConfigKey<>(category, key, Type.BOOLEAN, defaultValue, description);
    }

    public static ConfigKey<Integer> create(String category, String key, int defaultValue, String... description) {
        return new ConfigKey<>(category, key, Type.INTEGER, defaultValue, description);
    }

    public static ConfigKey<Double> create(String category, String key, double defaultValue, String... description) {
        return new ConfigKey<>(category, key, Type.DOUBLE, defaultValue, description);
    }

    public static <T extends Enum<T> & IConfigSerializable> ConfigKey<T> create(String category, String key, T defaultValue, String... description) {
        return new ConfigKey<T>(category, key, Type.STRING, defaultValue, description) {
            @SuppressWarnings("unchecked")
            @Override
            public T get(Configuration config) {
                String str = getProperty(config).getString();
                for (T t : (T[])defaultValue.getClass().getEnumConstants()) {
                    if (t.matches(str)) return t;
                }
                return null;
            }
            @Override
            public void set(Configuration config, T value) {
                getProperty(config).set(value.toConfigString());
            }
        };
    }

}