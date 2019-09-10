package ru.func.skywars.chest;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ru.yamycraft.api.gui.builder.item.ItemStackBuilderImpl;

import java.util.List;
import java.util.Random;

/**
 * @author func 08.09.2019
 */
public interface Openable {

    Random RANDOM = new Random();

    ItemStack[] BLOCKS = {
            new ItemStackBuilderImpl()
                    .setMaterial(Material.DIRT)
                    .setAmount(64)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.STONE)
                    .setAmount(32)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.TNT)
                    .setAmount(10)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.BRICK)
                    .setAmount(32)
                    .build(),
    };

    List<ItemStack> getDrop();
}
