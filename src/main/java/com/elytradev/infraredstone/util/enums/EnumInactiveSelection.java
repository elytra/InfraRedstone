package com.elytradev.infraredstone.util.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumInactiveSelection implements IStringSerializable {
    NONE("none"), LEFT("left"), BACK("back"), RIGHT("right");

    private final String name;

    EnumInactiveSelection(String name) {
        this.name=name;
    }

    public static EnumInactiveSelection forName(String s) {
        for (EnumInactiveSelection value : EnumInactiveSelection.values()) {
            if (s.equals(value.getName())) {
                return value;
            }
        }
        return EnumInactiveSelection.NONE;
    }

    @Override
    public String getName() {
        return name;
    }
}
