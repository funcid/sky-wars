package ru.func.skywars.kit;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.yamycraft.api.gui.builder.item.ItemStackBuilderImpl;

import java.util.function.Supplier;

/**
 * @author func 07.09.2019
 */
@Getter
public class WarriorKit implements Settable {

    private String name = "§eВоин";
    private int cost = 0;
    private Supplier<ItemStack> icon = () -> new ItemStackBuilderImpl()
            .setMaterial(Material.IRON_SWORD)
            .withItemMeta()
            .setDisplayName(name)
            .setLore(
                    "",
                    "§6* §fЦена: §6" + cost,
                    "§6* §fОписание:",
                    "    §7Отличный, мощный класс,",
                    "    §7враги не совладают с ним."
            ).then()
            .build();


    public void dress(Player player) {
        player.sendMessage("Одел");
    }
}
