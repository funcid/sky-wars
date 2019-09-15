package ru.func.skywars.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import ru.func.skywars.SkyWars;
import ru.func.skywars.kit.Set;
import ru.func.skywars.player.PlayerStatistic;
import ru.func.skywars.player.Shuffler;
import ru.func.skywars.status.GameStatus;
import ru.func.skywars.team.Team;
import ru.yamycraft.api.gui.builder.item.ItemStackBuilderImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author func 08.09.2019
 */
public class ConnectionListener implements Listener {

    private final SkyWars skyWars;

    private ItemStack voteKit = new ItemStackBuilderImpl()
            .setMaterial(Material.SLIME_BALL)
            .withItemMeta()
            .setDisplayName("§f§l<< §6Выбор набора §f§l>>")
            .then()
            .build();
    private ItemStack voteTeam = new ItemStackBuilderImpl()
            .setMaterial(Material.QUARTZ)
            .withItemMeta()
            .setDisplayName("§f§l<< §6Выбор команды §f§l>>")
            .then()
            .build();
    private ItemStack playerStatistic = new ItemStackBuilderImpl()
            .setMaterial(Material.PAPER)
            .withItemMeta()
            .setDisplayName("§f§l<< §6Ваша статистика §f§l>>")
            .then()
            .build();

    public ConnectionListener(SkyWars skyWars) {
        this.skyWars = skyWars;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        /* Очищает инвентарь игрока */
        player.getInventory().clear();

        /* Написание о том, что игрок присоединился */
        e.setJoinMessage("[§bi§f] §l" + player.getName() + " §7присоединился к игре!");

        /* Загружает статистику игрока, если игра начинается иначе выдает режим наблюдателя */
        player.setGameMode(GameMode.ADVENTURE);
        if (skyWars.getGameCycle().getGameStatus().equals(GameStatus.STARTING)) {
            if (!loadStats(player))
                player.kickPlayer("Шалит база данных, я тут не виноват ps func.");
            skyWars.getPlayers().add(player.getUniqueId());
            Team.getSmallestTeam(skyWars.getTeams()).getPlayers().add(player);
            player.getInventory().addItem(voteTeam);
            player.getInventory().addItem(voteKit);
            player.getInventory().addItem(playerStatistic);
        } else
            player.setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        skyWars.getPlayers().remove(player.getUniqueId());

        /* Написание о том, что игрок отключился */
        e.setQuitMessage("[§bi§f] §l" + player.getName() + " §7вышел из игры.");

        /* Удаляет игрока из команды */
        for (Team team : Team.teams(skyWars.getTeams()))
            team.getPlayers().remove(player);

        /* Проверяет существует ли экземпляр статистики игрока */
        if (skyWars.getPlayerStatistic().containsKey(player.getUniqueId())) {

            /* Записывает игроку одну игру и количество сыгранных миллисекунд */
            PlayerStatistic playerStatistic = skyWars.getPlayerStatistic().get(player.getUniqueId());
            playerStatistic.setGames(playerStatistic.getGames() + 1);
            playerStatistic.setGameTime(playerStatistic.getGameTime() + skyWars.getGameCycle().getTime() * 1000);

            /* Сохраняет статистику игрока */
            saveStats(player, 1);
        }
        if (skyWars.getGameCycle().getGameStatus().equals(GameStatus.STARTING))
            return;

        /* Попытка завершить игру */
        if (!skyWars.getGameCycle().getGameStatus().equals(GameStatus.STARTING))
            skyWars.getGameCloser().tryCloseGame();
    }

    private final String GET_UUID = "SELECT * FROM SkyWarsPlayers WHERE uuid = ?";
    private final String INSERT_NEW = "INSERT INTO SkyWarsPlayers " +
            "(uuid, kills, bowKills, chestOpened, damage, games, gameTime, hits, strikes, wins, currentKit, kits) " +
            "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
    private final String UPDATE_STAT = "UPDATE SkyWarsPlayers " +
            "SET kills=?, bowKills=?, chestOpened=?, damage=?, games=?, gameTime=?, hits=?, strikes=?, wins=?, currentKit=?, kits=? " +
            "WHERE uuid = ?";
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private boolean loadStats(Player player) {
        try {
            preparedStatement = skyWars.getConnection().prepareStatement(GET_UUID);
            preparedStatement.setString(1, String.valueOf(player.getUniqueId()));


            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                skyWars.getPlayerStatistic().put(player.getUniqueId(), PlayerStatistic.builder()
                        .kills(resultSet.getInt("kills"))
                        .bowKills(resultSet.getInt("bowKills"))
                        .chestOpened(resultSet.getInt("chestOpened"))
                        .damage(resultSet.getDouble("damage"))
                        .games(resultSet.getInt("games"))
                        .gameTime(resultSet.getLong("gameTime"))
                        .hits(resultSet.getInt("hits"))
                        .strikes(resultSet.getInt("strikes"))
                        .wins(resultSet.getInt("wins"))
                        .currentKit(Set.valueOf(resultSet.getString("currentKit")))
                        .kits(Set.getSetListFromString(resultSet.getString("kits")))
                        .build()
                );
            } else {
                Bukkit.getScheduler().runTaskAsynchronously(skyWars, () -> {
                    try {
                        preparedStatement = skyWars.getConnection().prepareStatement(INSERT_NEW);

                        preparedStatement.setString(1, String.valueOf(player.getUniqueId()));
                        preparedStatement.setInt(2, 0);
                        preparedStatement.setInt(3, 0);
                        preparedStatement.setInt(4, 0);
                        preparedStatement.setDouble(5, 0);
                        preparedStatement.setInt(6, 0);
                        preparedStatement.setLong(7, 0);
                        preparedStatement.setInt(8, 0);
                        preparedStatement.setInt(9, 0);
                        preparedStatement.setInt(10, 0);
                        preparedStatement.setString(11, "NO_ACTIVE_KIT");
                        preparedStatement.setString(12, "NO_ACTIVE_KIT, ");

                        preparedStatement.execute();
                        preparedStatement.close();

                        skyWars.getPlayerStatistic().put(player.getUniqueId(), PlayerStatistic.builder()
                                .kills(0)
                                .bowKills(0)
                                .chestOpened(0)
                                .damage(0)
                                .games(0)
                                .gameTime(0)
                                .hits(0)
                                .strikes(0)
                                .wins(0)
                                .currentKit(Set.NO_ACTIVE_KIT)
                                .kits(Set.getSetListFromString("NO_ACTIVE_KIT, "))
                                .build()
                        );
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
            return true;
        } catch (SQLException ignored) {
            return false;
        } finally {
            try {
                if (preparedStatement != null && !preparedStatement.isClosed())
                    preparedStatement.close();
                if (resultSet != null && !resultSet.isClosed())
                    resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveStats(Player player, int i) {

        Shuffler shuffler = skyWars.getPlayerStatistic().get(player.getUniqueId());

        try {
            preparedStatement = skyWars.getConnection().prepareStatement(GET_UUID);
            preparedStatement.setString(1, String.valueOf(player.getUniqueId()));

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                preparedStatement = skyWars.getConnection().prepareStatement(UPDATE_STAT);

                preparedStatement.setInt(1, shuffler.getKills());
                preparedStatement.setInt(2, shuffler.getBowKills());
                preparedStatement.setInt(3, shuffler.getChestOpened());
                preparedStatement.setDouble(4, shuffler.getDamage());
                preparedStatement.setInt(5, shuffler.getGames());
                preparedStatement.setLong(6, shuffler.getGameTime());
                preparedStatement.setInt(7, shuffler.getHits());
                preparedStatement.setInt(8, shuffler.getStrikes());
                preparedStatement.setInt(9, shuffler.getWins());
                preparedStatement.setString(10, shuffler.getCurrentKit().name());
                preparedStatement.setString(11, Set.getStringFromSetList(shuffler.getKits()));
                preparedStatement.setString(12, String.valueOf(player.getUniqueId()));

                preparedStatement.executeUpdate();
                preparedStatement.close();
            }
            Bukkit.getLogger().info(player.getName() + " сохранен.");
        } catch (SQLException e) {
            if (i < 3)
                saveStats(player, ++i);
        } finally {
            try {
                if (resultSet != null && !resultSet.isClosed())
                    resultSet.close();
                if (!preparedStatement.isClosed())
                    preparedStatement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        skyWars.getPlayerStatistic().remove(player.getUniqueId());
    }
}
