package ru.func.skywars.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.yamycraft.api.gui.builder.item.ItemStackBuilderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author func 10.09.2019
 */
@Getter
@AllArgsConstructor
public enum Team {

    WHITE("§fБелые",
            () -> new ItemStackBuilderImpl()
                    .withItemMeta()
                    .setDisplayName("§f<< §lБелые §f>>")
                    .then()
                    .setMaterial(Material.WOOL)
                    .setData(0)
                    .build(),
            new ArrayList<>()
    ), ORANGE("§6Оранжевые",
            () -> new ItemStackBuilderImpl()
                    .withItemMeta()
                    .setDisplayName("§6<< §f§lОранжевые §6>>")
                    .then()
                    .setMaterial(Material.WOOL)
                    .setData(1)
                    .build(),
            new ArrayList<>()
    ), PURPLE("§5Пурпурные",
            () -> new ItemStackBuilderImpl()
                    .withItemMeta()
                    .setDisplayName("§5<< §f§lПурпурные §5>>")
                    .then()
                    .setMaterial(Material.WOOL)
                    .setData(2)
                    .build(),
            new ArrayList<>()
    ), BLUE("§bГолубые",
            () -> new ItemStackBuilderImpl()
                    .withItemMeta()
                    .setDisplayName("§b<< §f§lГолубые §b>>")
                    .then()
                    .setMaterial(Material.WOOL)
                    .setData(3)
                    .build(),
            new ArrayList<>()
    ), YELLOW("§eЖелтые",
            () -> new ItemStackBuilderImpl()
                    .withItemMeta()
                    .setDisplayName("§e<< §f§lЖелтые §e>>")
                    .then()
                    .setMaterial(Material.WOOL)
                    .setData(4)
                    .build(),
            new ArrayList<>()
    ), LIME("§aЗеленые",
            () -> new ItemStackBuilderImpl()
                    .withItemMeta()
                    .setDisplayName("§a<< §f§lЗеленые §a>>")
                    .then()
                    .setMaterial(Material.WOOL)
                    .setData(5)
                    .build(),
            new ArrayList<>()
    ), PINK("§dРозовые",
            () -> new ItemStackBuilderImpl()
                    .withItemMeta()
                    .setDisplayName("§d<< §f§lРозовые §d>>")
                    .then()
                    .setMaterial(Material.WOOL)
                    .setData(6)
                    .build(),
            new ArrayList<>()
    ), GREY("§7Серые",
            () -> new ItemStackBuilderImpl()
                    .withItemMeta()
                    .setDisplayName("§7<< §f§lСерые §7>>")
                    .then()
                    .setMaterial(Material.WOOL)
                    .setData(7)
                    .build(),
            new ArrayList<>()
    ), RED("§cКрасные",
            () -> new ItemStackBuilderImpl()
                    .withItemMeta()
                    .setDisplayName("§c<< §f§lКрасные §c>>")
                    .then()
                    .setMaterial(Material.WOOL)
                    .setData(14)
                    .build(),
            new ArrayList<>()
    ),
    ;

    private String name;
    private Supplier<ItemStack> icon;
    private List<Player> players;

    /**
     * @return команду в которой наименьшее количество игроков
     */
    public static Team getSmallestTeam() {
        Team smallest = Team.WHITE;

        for (Team team : Team.values())
            if (team.getPlayers().size() < smallest.getPlayers().size())
                smallest = team;

        return smallest;
    }

    /**
     * @param player игрок чью команду надо узнать
     * @return команду в которой находится игрок
     */
    public static Team getPlayerTeam(Player player) {
        for (Team team : Team.values())
            if (team.getPlayers().contains(player))
                return team;

        return null;
    }
}
