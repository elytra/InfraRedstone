package com.elytradev.infraredstone.util.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumCableConnection implements IStringSerializable {
    DISCONNECTED("disconnected"), CONNECTED("connected"), CONNECTED_UP("connected_up");

    private final String name;

    EnumCableConnection(String name) {
        this.name=name;
    }

    @Override
    public String getName() {
        return name;
    }
}
