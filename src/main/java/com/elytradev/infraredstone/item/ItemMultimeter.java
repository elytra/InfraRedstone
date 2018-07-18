package com.elytradev.infraredstone.item;

import com.elytradev.infraredstone.InRedLog;
import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.block.ModBlocks;
import com.elytradev.infraredstone.logic.IMultimeterProbe;
import com.elytradev.infraredstone.logic.InRedLogic;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemMultimeter extends ItemBase {

    public ItemMultimeter() {
        super("multimeter");
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Block block = world.getBlockState(pos).getBlock();
        TileEntity te = world.getTileEntity(pos);
        String value;
        TextComponentTranslation i18n;
        TextComponentString message;
        if (world.isRemote) return EnumActionResult.PASS;
        if (te instanceof IMultimeterProbe) {
            message = ((IMultimeterProbe) te).getProbeMessage();
        } else if (block == ModBlocks.INFRA_REDSTONE || block == ModBlocks.IN_RED_SCAFFOLD) {
            value = getValue(world, pos, facing);
            i18n = new TextComponentTranslation("msg.inred.multimeter.cable");
            message = new TextComponentString(i18n.getFormattedText()+value);
        } else if (block == ModBlocks.IN_RED_BLOCK) {
            i18n = new TextComponentTranslation("msg.inred.multimeter.block");
            message = new TextComponentString(i18n.getFormattedText());
//        } else if (block == ModBlocks.DEVICE_LIQUID_CRYSTAL) {
//            value = getValue(world, pos, facing);
//            i18n = new TextComponentTranslation("msg.inred.multimeter.direction");
//            message = new TextComponentString(i18n.getFormattedText()+" "+facing.getName()+value);
        } else if (InRedLogic.checkCandidacy(world, pos, player.getAdjustedHorizontalFacing())) {
            value = getValue(world, pos, player.getAdjustedHorizontalFacing());
            i18n = new TextComponentTranslation("msg.inred.multimeter.direction");
            message = new TextComponentString(i18n.getFormattedText() + " " + player.getAdjustedHorizontalFacing().getName() + value);
        } else {
            return EnumActionResult.PASS;
        }
        player.sendMessage(message);
        return EnumActionResult.SUCCESS;
    }

    private String getValue(World world, BlockPos pos, EnumFacing face) {
        int signal = InRedLogic.findIRValue(world, pos, face.getOpposite());
        int bit1 = ((signal & 0b00_0001) != 0) ? 1:0;
        int bit2 = ((signal & 0b00_0010) != 0) ? 1:0;
        int bit3 = ((signal & 0b00_0100) != 0) ? 1:0;
        int bit4 = ((signal & 0b00_1000) != 0) ? 1:0;
        int bit5 = ((signal & 0b01_0000) != 0) ? 1:0;
        int bit6 = ((signal & 0b10_0000) != 0) ? 1:0;
        return ": 0b"+bit6+bit5+"_"+bit4+bit3+bit2+bit1+" ("+signal+")";
    }

}
