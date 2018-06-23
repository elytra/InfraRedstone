package com.elytradev.infraredstone.container;

import com.elytradev.concrete.inventory.gui.ConcreteContainer;
import com.elytradev.concrete.inventory.gui.widget.*;
import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.container.widget.WDelayLabel;
import com.elytradev.infraredstone.logic.network.PacketButtonClick;
import com.elytradev.infraredstone.tile.TileEntityOscillator;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class OscillatorContainer extends ConcreteContainer {

    private ResourceLocation tick_up = new ResourceLocation(InfraRedstone.modId,"textures/gui/button_tick_up.png");
    private ResourceLocation tick_down = new ResourceLocation(InfraRedstone.modId,"textures/gui/button_tick_down.png");
    private ResourceLocation second_up = new ResourceLocation(InfraRedstone.modId,"textures/gui/button_second_up.png");
    private ResourceLocation second_down = new ResourceLocation(InfraRedstone.modId,"textures/gui/button_second_down.png");
    private ResourceLocation dummy_off = new ResourceLocation(InfraRedstone.modId,"textures/blocks/oscillator_glow.png");
    private TileEntityOscillator oscillator;

    public OscillatorContainer(IInventory player, IInventory container, TileEntityOscillator oscillator) {
        super(player, container);
        this.oscillator = oscillator;
        WPlainPanel panel = new WPlainPanel();
        setRootPanel(panel);
        WClientButton plusOneTick = new WClientButton(tick_up, dummy_off, this::sendOneTick).withTooltip("Increase by 0.1 seconds");
        WClientButton plusOneSecond = new WClientButton(second_up, dummy_off, this::sendOneSecond).withTooltip("Increase by 1 second");
        WClientButton minusOneTick = new WClientButton(tick_down, dummy_off, this::sendOneTickDown).withTooltip("Decrease by 0.1 seconds");
        WClientButton minusOneSecond = new WClientButton(second_down, dummy_off, this::sendOneSecondDown).withTooltip("Decrease by 1 second");
        WDelayLabel delay = new WDelayLabel("delay: %s second%s").withFields(container, 0, 1);
        panel.add(plusOneTick, 64, 32);
        panel.add(plusOneSecond, 96, 32);
        panel.add(minusOneSecond, 0, 32);
        panel.add(minusOneTick, 32, 32);
        panel.add(delay, 0, 15);
    }

    private void sendOneTick() {
        PacketButtonClick plusOne = new PacketButtonClick("plus_one");
        plusOne.sendToServer();
    }

    private void sendOneSecond() {
        PacketButtonClick plusTen = new PacketButtonClick("plus_ten");
        plusTen.sendToServer();
    }

    private void sendOneTickDown() {
        PacketButtonClick minusOne = new PacketButtonClick("minus_one");
        minusOne.sendToServer();
    }

    private void sendOneSecondDown() {
        PacketButtonClick minusTen = new PacketButtonClick("minus_ten");
        minusTen.sendToServer();
    }

    public void increaseDelay(int amt) {
        oscillator.maxRefreshTicks += amt;
        oscillator.setDelay();
    }
}