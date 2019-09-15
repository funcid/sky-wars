package ru.func.skywars.kit;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import ru.yamycraft.api.gui.builder.item.ItemStackBuilderImpl;

import java.util.Random;
import java.util.function.Supplier;

/**
 * @author func 15.09.2019
 */
public class BlacksmithKit implements Settable {

    @Getter
    private String name = "§eКузнец";
    @Getter
    private int cost = 0;
    @Getter
    private Supplier<ItemStack> icon = () -> new ItemStackBuilderImpl()
            .setMaterial(Material.ANVIL)
            .withItemMeta()
            .setDisplayName(name)
            .setLore(
                    "",
                    "§6* §fЦена: §6" + cost,
                    "§6* §fСодержимое:",
                    "    §7Наковальня",
                    "    §7Три случайных книги зачарования"
            ).then()
            .build();

    private ItemStack anvil = new ItemStack(Material.ANVIL);
    private ItemStack prototypeBook = new ItemStack(Material.ENCHANTED_BOOK);
    private Random random = new Random();

    public void dress(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.addItem(anvil);
        for (int i = 0; i < 3; i++) {
            ItemMeta meta = prototypeBook.getItemMeta();
            meta.addEnchant(Enchantment.values()[random.nextInt(Enchantment.values().length)], 1, false);
            prototypeBook.setItemMeta(meta);
            playerInventory.addItem(prototypeBook);
        }
    }
}
