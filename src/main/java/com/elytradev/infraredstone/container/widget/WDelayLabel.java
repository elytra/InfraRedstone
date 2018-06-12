package com.elytradev.infraredstone.container.widget;

import com.elytradev.concrete.inventory.gui.client.GuiDrawing;
import com.elytradev.concrete.inventory.gui.widget.WLabel;
import net.minecraft.inventory.IInventory;

public class WDelayLabel extends WLabel {

    public WDelayLabel(String text) {
        super(text);
    }

    public WDelayLabel withFields(IInventory inv, int field1, int field2) {
        this.inventory = inv;
        this.field1 = field1;
        this.field2 = field2;
        return this;
    }

    @Override
    public void paintBackground(int x, int y) {
        double field1Contents = 0;
        String field2Contents = "";
        if (inventory!=null) {
            if (field1>=0) {
                field1Contents = inventory.getField(field1)/10d;
            }

            if (field2>=0) {
                field2Contents = (inventory.getField(field2)==1)? "s": "";
            }
        }

        @SuppressWarnings("deprecation")
        String formatted = net.minecraft.util.text.translation.I18n.translateToLocalFormatted(text, field1Contents, field2Contents);

        GuiDrawing.drawString(formatted, x, y, color);
    }
}
