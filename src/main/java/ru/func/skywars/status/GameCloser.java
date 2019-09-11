package ru.func.skywars.status;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.func.skywars.SkyWars;
import ru.func.skywars.player.PlayerStatistic;
import ru.func.skywars.team.Team;

/**
 * @author func 10.09.2019
 */
@AllArgsConstructor
public class GameCloser {

    private SkyWars skyWars;

    public void tryCloseGame() {
        if (!skyWars.getGameCycle().getGameStatus().equals(GameStatus.STARTING)) {

            int aliveTeams = 0;
            Team winner = null;
            for (Team team : Team.values()) {
                if (team.getPlayers().size() > 0) {
                    winner = team;
                    aliveTeams++;
                }
            }
            if (aliveTeams == 1) {
                Bukkit.broadcastMessage("§e>>>>> §f§lИгра завершена §e<<<<<");
                Bukkit.broadcastMessage("§e§l* §fПобедили: " + winner.getName());
                for (Player player : winner.getPlayers()) {
                    Bukkit.broadcastMessage("   - §7" + player.getName());
                    PlayerStatistic playerStatistic = skyWars.getPlayerStatistic().get(player.getUniqueId());
                    playerStatistic.setWins(playerStatistic.getWins() + 1);
                }
                Bukkit.broadcastMessage("§e§l* §fИгра длилась: §l" + skyWars.getGameCycle().getTime() / 60 + " мин.");
                Bukkit.broadcastMessage("§6§lGG!");

                skyWars.getGameCycle().setGameStatus(GameStatus.DEATH_MATCH);
                skyWars.getGameCycle().setTime(GameStatus.ENDING.getTime() - 1);
            }
        }
    }
}
