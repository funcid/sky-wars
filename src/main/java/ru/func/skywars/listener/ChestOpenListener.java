package ru.func.skywars.listener;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ru.func.skywars.SkyWars;
import ru.func.skywars.chest.CommonChest;
import ru.func.skywars.chest.PericentralChest;
import ru.func.skywars.player.PlayerStatistic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author func 08.09.2019
 */
public class ChestOpenListener implements Listener {

    private SkyWars skyWars;
    private int radius;
    private List<Location> centralCircleMask;
    @Getter
    private static List<Location> openChests;

    public ChestOpenListener(SkyWars skyWars) {
        this.skyWars = skyWars;
        openChests = new ArrayList<>();
        radius = skyWars.getConfig().getInt("centralChestsRadius");
        centralCircleMask = new ArrayList<>();

        World world = Bukkit.getWorld(skyWars.getConfig().getString("world"));

        ConfigurationSection configurationSection = skyWars.getConfig().getConfigurationSection("centralPointsMask");
        for (String point : configurationSection.getKeys(false)) {
            String[] cords = configurationSection.getString(point).split("\\s+");
            centralCircleMask.add(new Location(
                    world,
                    Double.parseDouble(cords[0]),
                    Double.parseDouble(cords[1]),
                    Double.parseDouble(cords[2])
            ));
        }
    }

    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent e) {

        Inventory inventory = e.getInventory();

        /* Ничего не делает если это не сундук или же если этот сундук уже был открыт */
        if (!(inventory.getHolder() instanceof Chest) || openChests.contains(inventory.getLocation()))
            return;

        /* Определяет что это сундук возле центра или нет */
        boolean isAroundCenter = false;
        for (Location location : centralCircleMask) {
            if (location.distance(inventory.getLocation()) < radius) {
                isAroundCenter = true;
                break;
            }
        }

        /* Выбирает какой дроп засунуть в сундук */
        inventory.addItem((isAroundCenter ? new PericentralChest().getDrop() : new CommonChest().getDrop()).toArray(new ItemStack[0]));

        /* Добавляет игроку в статистику 1 открытый сундук */
        PlayerStatistic playerStatistic = skyWars.getPlayerStatistic().get(e.getPlayer().getUniqueId());
        playerStatistic.setChestOpened(playerStatistic.getChestOpened() + 1);

        /* Добавляет сундук в список открытых */
        openChests.add(inventory.getLocation());
    }
}
