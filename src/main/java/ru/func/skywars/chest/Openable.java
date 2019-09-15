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

    ItemStack[] FOOD = {
            new ItemStackBuilderImpl()
                    .setMaterial(Material.COOKED_BEEF)
                    .setAmount(32)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.APPLE)
                    .setAmount(16)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.COOKED_CHICKEN)
                    .setAmount(24)
                    .build(),
    };

    ItemStack[] WEAPON = {

    };

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
