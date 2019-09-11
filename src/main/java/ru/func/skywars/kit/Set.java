package ru.func.skywars.kit;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author func 08.09.2019
 */
@Getter
@AllArgsConstructor
public enum Set {

    WARRIOR(new WarriorKit()),
    NO_ACTIVE_KIT(null),
    ;

    private Settable settable;

    /**
     * Преобразует строку в список доступных наборов.
     *
     * @param string строка вида 'WARRIOR, ARCHER, ...'
     * @return список доступных наборов
     */
    public static List<Set> getSetListFromString(String string) {
        String[] strings = string.split(", ");
        List<Set> kits = new ArrayList<>(strings.length);

        for (String arg : strings)
            kits.add(Set.valueOf(arg));

        return kits;
    }

    /**
     * Преобразует список доступных наборов в строку.
     *
     * @param kits список доступных наборов
     * @return строку вида 'WARRIOR, ARCHER, ...'
     */
    public static String getStringFromSetList(List<Set> kits) {
        StringBuilder stringBuilder = new StringBuilder();
        kits.forEach(kit -> stringBuilder.append(kit.name()).append(", "));

        return stringBuilder.toString();
    }
}
