package ru.func.skywars.status;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ru.func.skywars.SkyWars;
import ru.func.skywars.listener.ChestOpenListener;
import ru.yamycraft.api.gui.builder.item.ItemStackBuilderImpl;

/**
 * @author func 07.09.2019
 */
public class GameCycle {

    private SkyWars skyWars;

    @Getter
    @Setter
    private GameStatus gameStatus = GameStatus.STARTING;
    @Getter
    @Setter
    private int time;
    private boolean enoughPlayers = false;

    public GameCycle(SkyWars skyWars) {
        this.skyWars = skyWars;
    }

    public void begin() {
        new BukkitRunnable() {

            ItemStack voteKit = new ItemStackBuilderImpl()
                    .setMaterial(Material.SLIME_BALL)
                    .withItemMeta()
                    .setDisplayName("§f§l<< §6Выбор набора §f§l>>")
                    .then()
                    .build();

            public void run() {

                Bukkit.broadcastMessage(time + " <<");
                switch (gameStatus) {
                    case STARTING:
                        enoughPlayers = Bukkit.getOnlinePlayers().size() >= skyWars.getNeedPlayersToStart();

                        if (time == 0) {
                            Bukkit.getOnlinePlayers()
                                    .forEach(player -> {
                                        skyWars.getPlayers().add(player.getUniqueId());
                                        player.getInventory().addItem(voteKit);
                                    });
                        } else if (GameStatus.STARTING.getTime() == time) {
                            if (enoughPlayers) {
                                Bukkit.broadcastMessage("[§bi§f] §7Игра начинается!");
                                gameStatus = GameStatus.STARTED;
                                Bukkit.getOnlinePlayers().forEach(player -> {
                                            skyWars.getPlayerStatistic().get(player.getUniqueId())
                                                    .getCurrentKit()
                                                    .getSettable()
                                                    .dress(player);
                                            player.getLocation().subtract(0, 1, 0).getBlock().setType(Material.AIR);
                                            player.getLocation().subtract(0, 2, 0).getBlock().setType(Material.AIR);
                                        }
                                );
                            } else
                                time = GameStatus.STARTING.getTime() / 2;
                        }
                        break;
                    case STARTED:
                        if (GameStatus.STARTED.getTime() == time) {
                            ChestOpenListener.getOpenChests().clear();
                            Bukkit.broadcastMessage("[§bi§f] §7Сундуки перезаполнены!");
                            gameStatus = GameStatus.REOPEN;
                        }
                        break;
                    case REOPEN:
                        int dt = GameStatus.REOPEN.getTime() - time;
                        if (dt < 5 && dt > -1)
                            Bukkit.broadcastMessage(String.format("[§bi§f] §7Последний бой начнется через %d с.", ++dt));
                        else if (GameStatus.REOPEN.getTime() == time) {
                            Bukkit.broadcastMessage("[§bi§f] §7Начинается последний бой!");
                            gameStatus = GameStatus.DEATH_MATCH;
                        }
                        break;
                    case DEATH_MATCH:
                        if (GameStatus.DEATH_MATCH.getTime() == time) {
                            Bukkit.broadcastMessage("[§bi§f] §7Закрытие сервера. Ничья.");
                            gameStatus = GameStatus.ENDING;
                        }
                        break;
                    case ENDING:
                        if (GameStatus.ENDING.getTime() == time)
                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
                        break;
                }
                time++;
            }
        }.runTaskTimer(skyWars, 20L, 20L);
    }
}
