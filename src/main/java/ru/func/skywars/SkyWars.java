package ru.func.skywars;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import ru.func.skywars.database.MySql;
import ru.func.skywars.listener.ChestOpenListener;
import ru.func.skywars.listener.ConnectionListener;
import ru.func.skywars.listener.DamageListener;
import ru.func.skywars.listener.InteractListener;
import ru.func.skywars.player.PlayerStatistic;
import ru.func.skywars.status.GameCloser;
import ru.func.skywars.status.GameCycle;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * @author func 07.09.2019
 */
@Getter
public class SkyWars extends JavaPlugin {

    private Map<UUID, PlayerStatistic> playerStatistic = new HashMap<>();
    private List<UUID> players = new ArrayList<>();
    private GameCycle gameCycle = new GameCycle(this);
    private GameCloser gameCloser = new GameCloser(this);
    private int needPlayersToStart = getConfig().getInt("needPlayers");
    private Connection connection;
    private int playersInTeam = getConfig().getInt("playersInTeam");

    @Override
    public void onEnable() {

        /* Подготовка мира */
        World world = Bukkit.getWorld(getConfig().getString("world"));
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setDifficulty(Difficulty.HARD);
        world.setMonsterSpawnLimit(0);
        world.setAnimalSpawnLimit(0);
        world.setAutoSave(false);
        world.setTime(7000);

        /* Подготовка конфигурации */
        getConfig().options().copyDefaults(true);
        saveConfig();

        /* Подключение к базе данных */
        long currentTime = System.currentTimeMillis();
        try {
            getLogger().info("[!] Попытка подключения...");
            ConfigurationSection sqlSettingsConfigurationSection = getConfig().getConfigurationSection("sqlSettings");
            connection = new MySql(
                    sqlSettingsConfigurationSection.getString("user"),
                    sqlSettingsConfigurationSection.getString("password"),
                    sqlSettingsConfigurationSection.getString("host"),
                    sqlSettingsConfigurationSection.getString("database"),
                    sqlSettingsConfigurationSection.getInt("port")
            ).openConnection();

            Statement statement = connection.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS `SkyWarsPlayers` (" +
                            "id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                            "uuid TEXT, " +
                            "kills INT, " +
                            "bowKills INT, " +
                            "wins INT," +
                            "hits INT," +
                            "strikes INT," +
                            "damage DOUBLE," +
                            "gameTime LONG," +
                            "chestOpened INT," +
                            "games INT," +
                            "currentKit TEXT," +
                            "kits TEXT" +
                            ");"
            );

            statement.close();
            getLogger().info(String.format(
                    "[!] Успешное подключение. Завершено (%sс).",
                    (System.currentTimeMillis() - currentTime) / 1000D)
            );
        } catch (SQLException e) {
            getLogger().info("[!] Ошибка подключения. " + e.getMessage());
        }

        /* Регистрация обработчиков событий */
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ChestOpenListener(this), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(this), this);

        gameCycle.begin();
    }
}
