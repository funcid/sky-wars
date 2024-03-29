package ru.func.skywars.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author func 07.09.2019
 */
@Getter
@AllArgsConstructor
public enum GameStatus {

    STARTING(20, "- Начало"),
    STARTED(200, "- Игра"),
    REOPEN(220, "- Перезаполнение"),
    DEATH_MATCH(300, "- Последний бой"),
    ENDING(400, "- Игра закончена"),
    ;

    private int time;
    private String name;
}
