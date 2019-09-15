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
public class FarmerKit implements Settable {

    @Getter
    private String name = "§eФермер";
    @Getter
    private int cost = 0;
    @Getter
    private Supplier<ItemStack> icon = () -> new ItemStackBuilderImpl()
            .setMaterial(Material.EGG)
            .withItemMeta()
            .setDisplayName(name)
            .setLore(
                    "",
                    "§6* §fЦена: §6" + cost,
                    "§6* §fСодержимое:",
                    "    §7Железный нагрудник",
                    "    §7Стак яиц"
            ).then()
            .build();

    private ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
    private ItemStack eggs = new ItemStack(Material.EGG, 64);

    public void dress(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setChestplate(chestplate);
        playerInventory.addItem(eggs);
    }
}
