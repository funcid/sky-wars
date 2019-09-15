package ru.func.skywars.kit;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import ru.yamycraft.api.gui.builder.item.ItemStackBuilderImpl;

import java.util.Random;
import java.util.function.Supplier;

/**
 * @author func 15.09.2019
 */
public class IroncladKit implements Settable {

    @Getter
    private String name = "§eБроневик";
    @Getter
    private int cost = 0;
    @Getter
    private Supplier<ItemStack> icon = () -> new ItemStackBuilderImpl()
            .setMaterial(Material.CHAINMAIL_HELMET)
            .withItemMeta()
            .setDisplayName(name)
            .setLore(
                    "",
                    "§6* §fЦена: §6" + cost,
                    "§6* §fСодержимое:",
                    "    §7Случайный нагрудник",
                    "    §7Случайный шлем"
            ).then()
            .build();

    private String[] materialType = { "IRON_", "GOLD_", "CHAINMAIL_", "DIAMOND_" };
    private Random random = new Random();

    public void dress(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setChestplate(new ItemStack(Material.valueOf(materialType[random.nextInt(materialType.length)] + "CHESTPLATE")));
        playerInventory.setHelmet(new ItemStack(Material.valueOf(materialType[random.nextInt(materialType.length)] + "HELMET")));
    }
}
