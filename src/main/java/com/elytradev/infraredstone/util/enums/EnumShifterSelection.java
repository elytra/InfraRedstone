package com.elytradev.infraredstone.util.enums;

import net.minecraft.util.IStringSerializable;

public enum EnumShifterSelection implements IStringSerializable {
    LEFT("left"), RIGHT("right");

    private final String name;

    EnumShifterSelection(String name) {
        this.name=name;
    }

    @Override
    public String getName() {
        return name;
    }

    public static EnumShifterSelection forName(String s) {
        for (EnumShifterSelection value : EnumShifterSelection.values()) {
            if (s.equals(value.getName())) {
                return value;
            }
        }
        return EnumShifterSelection.LEFT;
    }
}
