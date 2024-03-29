package ru.func.skywars.kit;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ru.yamycraft.api.gui.builder.item.ItemStackBuilderImpl;

import java.util.function.Supplier;

/**
 * @author func 07.09.2019
 */
public class WarriorKit implements Settable {

    @Getter
    private String name = "§eВоин";
    @Getter
    private int cost = 0;
    @Getter
    private Supplier<ItemStack> icon = () -> new ItemStackBuilderImpl()
            .setMaterial(Material.IRON_SWORD)
            .withItemMeta()
            .setDisplayName(name)
            .setLore(
                    "",
                    "§6* §fЦена: §6" + cost,
                    "§6* §fСодержимое:",
                    "    §7Железный нагрудник с зачарованием",
                    "    §7Каменный меч"
            ).then()
            .build();

    private ItemStack chestplate = new ItemStackBuilderImpl()
            .setMaterial(Material.IRON_CHESTPLATE)
            .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
            .build();
    private ItemStack sword = new ItemStack(Material.STONE_SWORD);

    public void dress(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setChestplate(chestplate);
        playerInventory.addItem(sword);
    }
}
