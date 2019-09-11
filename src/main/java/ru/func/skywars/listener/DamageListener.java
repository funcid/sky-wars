package ru.func.skywars.listener;

import lombok.AllArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import ru.func.skywars.SkyWars;
import ru.func.skywars.player.PlayerStatistic;
import ru.func.skywars.team.Team;

import java.util.Objects;

/**
 * @author func 08.09.2019
 */
@AllArgsConstructor
public class DamageListener implements Listener {

    private SkyWars skyWars;

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {

        Player player = e.getEntity();
        Player killer = player.getKiller();

        /* Удаление игрока из списка живых и изменение режима игры на наблюдателя */
        skyWars.getPlayers().remove(player.getUniqueId());
        player.setGameMode(GameMode.SPECTATOR);

        /* Удаляет игрока из команды */
        for (Team team : Team.teams(skyWars.getTeams()))
            team.getPlayers().remove(player);

        /* Попытка завершить игру */
        skyWars.getGameCloser().tryCloseGame();

        /* Если игрока убили, убийце обновить статистику и написать посмертное сообщение */
        if (killer != null) {
            PlayerStatistic killerStatistic = skyWars.getPlayerStatistic().get(killer.getUniqueId());
            if (player.getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE))
                killerStatistic.setBowKills(killerStatistic.getBowKills() + 1);
            else
                killerStatistic.setKills(killerStatistic.getKills() + 1);

            e.setDeathMessage("[§bi§f] §7" + player.getName() + " убит игроком " + killer.getName());
        } else
            e.setDeathMessage("[§bi§f] §7" + player.getName() + " как-то умер.");
    }

    @EventHandler
    public void playerDamageEvent(EntityDamageByEntityEvent e) {

        if (!(e.getEntity() instanceof Player))
            return;

        Entity damager = e.getDamager();

        PlayerStatistic playerStatistic;

        /* Если нападающий использовал метательное оружие то обновить статистику попаданий,
         * если игрок не использовал метательное оружие, то просто обновить нанесенный урон
         */
        if (damager instanceof Projectile) {
            playerStatistic = skyWars.getPlayerStatistic().get(((Player) ((Projectile) damager).getShooter()).getUniqueId());
            if (Objects.equals(
                    Team.getPlayerTeam(
                            (Player) e.getEntity(),
                            skyWars.getTeams()
                    ), Team.getPlayerTeam((Player) (((Projectile) damager).getShooter()
                    ), skyWars.getTeams()))
            ) {
                e.setCancelled(true);
                return;
            }
            if (damager instanceof Arrow)
                playerStatistic.setDamage(playerStatistic.getDamage() + e.getDamage());
            playerStatistic.setHits(playerStatistic.getHits() + 1);
        } else if (damager instanceof Player) {
            playerStatistic = skyWars.getPlayerStatistic().get(damager.getUniqueId());
            if (Objects.equals(Team.getPlayerTeam((Player) e.getEntity(), skyWars.getTeams()), Team.getPlayerTeam((Player) damager, skyWars.getTeams()))) {
                e.setCancelled(true);
                return;
            }
            playerStatistic.setDamage(playerStatistic.getDamage() + e.getDamage());
        }
    }

    @EventHandler
    public void onArrowShootEvent(ProjectileLaunchEvent e) {

        Entity shooter = (Entity) e.getEntity().getShooter();

        /* Если игрок стреляет из лука, то обновить статистику выстрелов */
        if (shooter instanceof Player && e.getEntity() instanceof Arrow) {
            PlayerStatistic playerStatistic = skyWars.getPlayerStatistic().get(shooter.getUniqueId());
            playerStatistic.setStrikes(playerStatistic.getStrikes() + 1);
        }
    }
}
