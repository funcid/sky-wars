package ru.func.skywars.player;

import ru.func.skywars.kit.Set;

import java.util.List;

/**
 * @author func 07.09.2019
 */
public interface Shuffler {

    int getKills();

    int getBowKills();

    int getWins();

    int getHits();

    int getStrikes();

    double getDamage();

    long getGameTime();

    int getChestOpened();

    int getGames();

    Set getCurrentKit();

    List<Set> getKits();

    void setKills(int kills);

    void setBowKills(int bowKills);

    void setWins(int wins);

    void setHits(int hits);

    void setStrikes(int strikes);

    void setDamage(double damage);

    void setGameTime(long gameTime);

    void setChestOpened(int chestOpened);

    void setGames(int games);

    void setCurrentKit(Set currentKit);

    void setKits(List<Set> kits);
}
