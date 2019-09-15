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
 * @author func 15.09.2019
 */
public class TerroristKit implements Settable {

    @Getter
    private String name = "§eТеррорист";
    @Getter
    private int cost = 0;
    @Getter
    private Supplier<ItemStack> icon = () -> new ItemStackBuilderImpl()
            .setMaterial(Material.TNT)
            .withItemMeta()
            .setDisplayName(name)
            .setLore(
                    "",
                    "§6* §fЦена: §6" + cost,
                    "§6* §fСодержимое:",
                    "    §7Железные поножи с зачарованием",
                    "    §7Четыре блока редстоуна",
                    "    §7Ведро с водой",
                    "    §724 блока взрывчатки"
            ).then()
            .build();

    private ItemStack leggins = new ItemStackBuilderImpl()
            .setMaterial(Material.IRON_LEGGINGS)
            .addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 2)
            .build();
    private ItemStack tnt = new ItemStack(Material.TNT, 24);
    private ItemStack blocks = new ItemStack(Material.REDSTONE_BLOCK, 4);
    private ItemStack water = new ItemStack(Material.WATER_BUCKET);

    public void dress(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setLeggings(leggins);
        playerInventory.addItem(tnt);
        playerInventory.addItem(blocks);
        playerInventory.addItem(water);
    }
}
