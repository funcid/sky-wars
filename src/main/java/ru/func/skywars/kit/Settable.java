package ru.func.skywars.kit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

/**
 * @author func 07.09.2019
 */
public interface Settable {

    int getCost();
    String getName();
    Supplier<ItemStack> getIcon();
    void dress(Player player);
}
