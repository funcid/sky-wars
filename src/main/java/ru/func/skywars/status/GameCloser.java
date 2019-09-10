package ru.func.skywars.status;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.func.skywars.SkyWars;
import ru.func.skywars.player.PlayerStatistic;

import java.util.UUID;

/**
 * @author func 10.09.2019
 */
@AllArgsConstructor
public class GameCloser {

    private SkyWars skyWars;

    public void tryCloseGame() {
        if (!skyWars.getGameCycle().getGameStatus().equals(GameStatus.STARTING) && skyWars.getPlayers().size() == 1) {
            UUID winnerUuid = skyWars.getPlayers().get(0);
            Player winner = Bukkit.getPlayer(winnerUuid);
            Bukkit.broadcastMessage("§e>>>>> §f§lИгра завершена §e<<<<<");
            Bukkit.broadcastMessage("§e§l* §fПобедитель: §l" + winner.getName());
            Bukkit.broadcastMessage("§e§l* §fИгра длилась: §l" + skyWars.getGameCycle().getTime() / 60 + " мин.");
            Bukkit.broadcastMessage("§6§lGG!");

            PlayerStatistic playerStatistic = skyWars.getPlayerStatistic().get(winnerUuid);
            playerStatistic.setWins(playerStatistic.getWins() + 1);

            skyWars.getGameCycle().setGameStatus(GameStatus.DEATH_MATCH);
            skyWars.getGameCycle().setTime(GameStatus.ENDING.getTime() - 1);
        }
    }
}
