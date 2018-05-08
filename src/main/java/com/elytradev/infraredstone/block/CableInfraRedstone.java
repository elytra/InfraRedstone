package com.elytradev.infraredstone.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.IStringSerializable;

public class CableInfraRedstone extends BlockBase implements IBlockBase{

    public CableInfraRedstone() {
        super(Material.CIRCUITS, "infra-redstone");
    }

}
