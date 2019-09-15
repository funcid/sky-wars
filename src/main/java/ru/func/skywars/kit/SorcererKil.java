package ru.func.skywars.kit;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ru.yamycraft.api.gui.builder.item.ItemStackBuilderImpl;

import java.util.function.Supplier;

/**
 * @author func 15.09.2019
 */
public class SorcererKil implements Settable {

    @Getter
    private String name = "§eЧаровальщик";
    @Getter
    private int cost = 0;
    @Getter
    private Supplier<ItemStack> icon = () -> new ItemStackBuilderImpl()
            .setMaterial(Material.ENCHANTED_BOOK)
            .withItemMeta()
            .setDisplayName(name)
            .setLore(
                    "",
                    "§6* §fЦена: §6" + cost,
                    "§6* §fСодержимое:",
                    "    §7Стак лазурита",
                    "    §7Чаровальный стол",
                    "    §7Стак пузырьков опыта"
            ).then()
            .build();

    private ItemStack table = new ItemStack(Material.ENCHANTMENT_TABLE);
    private ItemStack lapis = new ItemStack(Material.LAPIS_ORE, 64);
    private ItemStack potions = new ItemStack(Material.EXP_BOTTLE, 64);

    public void dress(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.addItem(table);
        playerInventory.addItem(lapis);
        playerInventory.addItem(potions);
    }
}
