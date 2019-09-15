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
public class SnowmanKit implements Settable {

    @Getter
    private String name = "§eСнеговик";
    @Getter
    private int cost = 0;
    @Getter
    private Supplier<ItemStack> icon = () -> new ItemStackBuilderImpl()
            .setMaterial(Material.SNOW_BALL)
            .withItemMeta()
            .setDisplayName(name)
            .setLore(
                    "",
                    "§6* §fЦена: §6" + cost,
                    "§6* §fСодержимое:",
                    "    §7Два блока снега",
                    "    §7Тыква",
                    "    §7Железная лопата",
                    "    §724 cнежка"
            ).then()
            .build();

    private ItemStack helmet = new ItemStack(Material.PUMPKIN);
    private ItemStack snowballs = new ItemStack(Material.SNOW_BALL, 24);
    private ItemStack blocks = new ItemStack(Material.SNOW_BLOCK, 2);
    private ItemStack spade = new ItemStack(Material.IRON_SPADE);

    public void dress(Player player) {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setHelmet(helmet);
        playerInventory.addItem(spade);
        playerInventory.addItem(snowballs);
        playerInventory.addItem(blocks);
    }
}
