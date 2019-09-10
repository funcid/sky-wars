package ru.func.skywars.player;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.func.skywars.kit.Set;

import java.util.List;

/**
 * @author func 07.09.2019
 */
@Getter
@Setter
@Builder
public class PlayerStatistic implements Shuffler {

    private int kills;
    private int bowKills;
    private int wins;
    private int hits;
    private int strikes;
    private double damage;
    private long gameTime;
    private int chestOpened;
    private int games;
    private Set currentKit;
    private List<Set> kits;
}
