package ru.func.skywars.kit;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

/**
 * @author func 08.09.2019
 */
public class NoActiveKit implements Settable {

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Supplier<ItemStack> getIcon() {
        return null;
    }

    @Override
    public void dress(Player player) {

    }
}
