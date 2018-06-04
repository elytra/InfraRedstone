package com.elytradev.infraredstone.item;

import com.elytradev.infraredstone.InRedLog;
import com.elytradev.infraredstone.InfraRedstone;
import com.elytradev.infraredstone.logic.InRedLogic;
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
        EnumFacing face;
        TextComponentTranslation note;
        String dir;
        boolean needSneak;
        if (!(InRedLogic.checkCandidacy(world, pos, facing)
                || InRedLogic.checkCandidacy(world, pos, player.getAdjustedHorizontalFacing()))) return EnumActionResult.PASS;
        if (world.getTileEntity(pos) != null) {
            if (world.getTileEntity(pos).hasCapability(InfraRedstone.CAPABILITY_IR, facing)) face = facing;
            else face = player.getAdjustedHorizontalFacing();
            note = new TextComponentTranslation("msg.inred.multimeter.direction");
            dir=" "+face+": ";
            needSneak = true;
        } else {
            face = facing;
            note = new TextComponentTranslation("msg.inred.multimeter.cable");
            dir=": ";
            needSneak = false;
        }
        if (player.isSneaking() == needSneak) {
            int signal = InRedLogic.findIRValue(world, pos, face.getOpposite());
            TextComponentString string = new TextComponentString(note.getFormattedText()+dir+signal+getBits(world, pos, face));
            if (!world.isRemote) player.sendMessage(string);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    private String getBits(World world, BlockPos pos, EnumFacing face) {
        int signal = InRedLogic.findIRValue(world, pos, face.getOpposite());
        int bit1 = ((signal & 0b00_0001) != 0) ? 1:0;
        int bit2 = ((signal & 0b00_0010) != 0) ? 1:0;
        int bit3 = ((signal & 0b00_0100) != 0) ? 1:0;
        int bit4 = ((signal & 0b00_1000) != 0) ? 1:0;
        int bit5 = ((signal & 0b01_0000) != 0) ? 1:0;
        int bit6 = ((signal & 0b10_0000) != 0) ? 1:0;
        return " (0b"+bit6+bit5+"_"+bit4+bit3+bit2+bit1+")";
    }

}
