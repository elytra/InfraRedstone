package com.elytradev.infraredstone.util;

public interface IConfigSerializable {
    public String toConfigString();
    public boolean matches(String configName);
}