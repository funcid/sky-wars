package ru.func.skywars.status;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.func.skywars.SkyWars;
import ru.func.skywars.listener.ChestOpenListener;
import ru.func.skywars.team.Team;

import java.util.ArrayList;
import java.util.List;

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
    private List<Location> spawnLocations = new ArrayList<>();
    private Location deathmathLocation;

    public GameCycle(SkyWars skyWars) {
        this.skyWars = skyWars;

        World world = Bukkit.getWorld(skyWars.getConfig().getString("world"));
        ConfigurationSection configurationSection = skyWars.getConfig().getConfigurationSection("locations");
        for (String string : configurationSection.getKeys(false)) {
            String[] cords = configurationSection.getString(string).split("\\s+");
            spawnLocations.add(new Location(
                    world,
                    Double.parseDouble(cords[0]),
                    Double.parseDouble(cords[1]),
                    Double.parseDouble(cords[2])
            ));
        }
        String[] cords = skyWars.getConfig().getString("deathmath_location").split("\\s+");
        deathmathLocation = new Location(
                world,
                Double.parseDouble(cords[0]),
                Double.parseDouble(cords[1]),
                Double.parseDouble(cords[2])
        );
    }

    public void begin() {
        new BukkitRunnable() {
            public void run() {

                switch (gameStatus) {
                    case STARTING:
                        enoughPlayers = Bukkit.getOnlinePlayers().size() >= skyWars.getNeedPlayersToStart();

                        if (time == 0) {
                            Bukkit.getOnlinePlayers().forEach(player -> skyWars.getPlayers().add(player.getUniqueId()));
                        } else if (GameStatus.STARTING.getTime() == time) {
                            if (enoughPlayers) {
                                Bukkit.broadcastMessage("[§bi§f] §7Игра начинается!");
                                gameStatus = GameStatus.STARTED;
                                Bukkit.getOnlinePlayers().forEach(player -> {
                                            player.getInventory().clear();
                                            player.setGameMode(GameMode.SURVIVAL);
                                            skyWars.getPlayerStatistic().get(player.getUniqueId())
                                                    .getCurrentKit()
                                                    .getSettable()
                                                    .dress(player);
                                        }
                                );
                                int i = 0;
                                for (Team team : Team.teams(skyWars.getTeams())) {
                                    for (Player player : team.getPlayers())
                                        player.teleport(spawnLocations.get(i));
                                    i++;
                                }
                            } else
                                time = GameStatus.STARTING.getTime() / 2;
                        }
                        break;
                    case STARTED:
                        int dt = GameStatus.STARTED.getTime() - time;
                        if (time - GameStatus.STARTING.getTime() == 5)
                            skyWars.setActiveDamage(true);
                        else if (dt == 30) {
                            Bukkit.broadcastMessage("[§bi§f] §7Сундуки будут перезаполнены через 30 секунд!");
                        } else if (dt < 6 && dt > 0) {
                            Bukkit.broadcastMessage(String.format("[§bi§f] §7Сундуки будут перезаполнены через %s секунд!", dt));
                        } else if (GameStatus.STARTED.getTime() == time) {
                            ChestOpenListener.getOpenChests().clear();
                            Bukkit.broadcastMessage("[§bi§f] §7Сундуки перезаполнены!");
                            gameStatus = GameStatus.REOPEN;
                        }
                        break;
                    case REOPEN:
                        int dx = GameStatus.REOPEN.getTime() - time;
                        if (dx < 6 && dx > 0)
                            Bukkit.broadcastMessage(String.format("[§bi§f] §7Последний бой начнется через %d с.", dx));
                        else if (GameStatus.REOPEN.getTime() == time) {
                            Bukkit.getOnlinePlayers().forEach(player -> player.teleport(deathmathLocation));
                            Bukkit.broadcastMessage("[§bi§f] §7Начинается последний бой!");
                            skyWars.setActiveDamage(false);
                            gameStatus = GameStatus.DEATH_MATCH;
                        }
                        break;
                    case DEATH_MATCH:
                        if (time - GameStatus.REOPEN.getTime() == 10)
                            skyWars.setActiveDamage(true);
                        else if (GameStatus.DEATH_MATCH.getTime() == time) {
                            Bukkit.broadcastMessage("[§bi§f] §7Закрытие сервера. Ничья.");
                            skyWars.setActiveDamage(false);
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
