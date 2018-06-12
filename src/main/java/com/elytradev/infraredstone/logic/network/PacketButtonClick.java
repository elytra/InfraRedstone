package com.elytradev.infraredstone.logic.network;

import com.elytradev.concrete.network.Message;
import com.elytradev.concrete.network.annotation.type.ReceivedOn;
import com.elytradev.infraredstone.InRedLog;
import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.container.OscillatorContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;

@ReceivedOn(Side.SERVER)
public class PacketButtonClick extends Message {

    private String name;

    public PacketButtonClick() {
        super(InfraRedstone.CONTEXT);

    }

    public PacketButtonClick(String name) {
        super(InfraRedstone.CONTEXT);
        this.name=name;

    }

    @Override
    protected void handle(EntityPlayer entityPlayer) {
        Container container = entityPlayer.openContainer;
        if (entityPlayer.openContainer instanceof OscillatorContainer) {
            int amount = 0;
            OscillatorContainer oscillator = (OscillatorContainer)container;
            switch (name) {
                case "plus_one":
                    amount = 1;
                    break;
                case "plus_ten":
                    amount = 10;
                    break;
                case "minus_one":
                    amount = -1;
                    break;
                case "minus_ten":
                    amount = -10;
                    break;
                default:
                    break;
            }
            oscillator.increaseDelay(amount);
        }
    }
}
