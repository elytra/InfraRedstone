package com.elytradev.infraredstone.container.widget;

import com.elytradev.concrete.inventory.gui.widget.WSwappableImage;
import net.minecraft.util.ResourceLocation;

import java.util.List;

// a button widget that will work on servers, because it doesn't have an inherent sound
public class WSilentButton extends WSwappableImage {
    protected boolean enabled = true;
    protected Runnable onClick;
    protected ResourceLocation disabledImage;
    public String tooltipLabel;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setOnClick(Runnable r) {
        this.onClick = r;
    }

    public WSilentButton(ResourceLocation enabledImage, ResourceLocation disabledImage, Runnable onClick) {
        this.image = enabledImage;
        this.disabledImage = disabledImage;
        this.onClick = onClick;
    }

    @Override
    public void onClick(int x, int y, int button) {
        if (enabled) {
            if (onClick!=null) onClick.run();
        }
    }

    @Override
    public void paintBackground(int x, int y) {
        if (enabled) {
            super.paintBackground(x, y);
        } else {
            if (disabledImage!=null) {
                rect(disabledImage, x, y, getWidth(), getHeight(), 0,0,1,1);
            } else {
                //No disabled image, so draw nothing
            }
        }
    }

    public WSilentButton withTooltip(String label) {
        this.setRenderTooltip(true);
        this.tooltipLabel = label;
        return this;
    }

    @Override
    public void addInformation(List<String> information) {
        if (tooltipLabel != null) information.add(tooltipLabel);
    }
}
