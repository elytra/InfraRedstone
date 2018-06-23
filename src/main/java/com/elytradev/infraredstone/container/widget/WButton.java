package com.elytradev.infraredstone.container.widget;

import com.elytradev.concrete.inventory.gui.widget.WClientButton;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class WButton extends WClientButton {

    public String tooltipLabel;

    public WButton(ResourceLocation enabled, ResourceLocation disabled, Runnable onClick) {
        this.image = enabled;
        this.disabledImage = disabled;
        this.onClick = onClick;
        this.enabled = true;
    }

    public WButton withTooltip(String label) {
        this.setRenderTooltip(true);
        this.tooltipLabel = label;
        return this;
    }

    @Override
    public void addInformation(List<String> information) {
        if (tooltipLabel != null) information.add(tooltipLabel);
    }
}
