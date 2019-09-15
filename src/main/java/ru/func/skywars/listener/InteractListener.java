package ru.func.skywars.listener;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.func.skywars.SkyWars;
import ru.func.skywars.kit.Set;
import ru.func.skywars.player.PlayerStatistic;
import ru.func.skywars.status.GameStatus;
import ru.func.skywars.team.Team;
import ru.yamycraft.api.gui.builder.LayoutBuilder;
import ru.yamycraft.api.gui.builder.PerPlayerGuiBuilder;
import ru.yamycraft.api.gui.builder.item.ItemStackBuilderImpl;
import ru.yamycraft.api.gui.icon.ConsumerableIcon;
import ru.yamycraft.api.gui.icon.NotClickableIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

/**
 * @author func 08.09.2019
 */
public class InteractListener implements Listener {

    private SkyWars skyWars;
    private Random random = new Random();
    private Supplier<PerPlayerGuiBuilder> guiPrototype = () -> PerPlayerGuiBuilder.generate(45)
            .setLayout(
                    new LayoutBuilder()
                            .addLayout(
                                    's',
                                    new NotClickableIcon(
                                            new ItemStackBuilderImpl()
                                                    .setMaterial(Material.STAINED_GLASS_PANE)
                                                    .setData(random.nextInt(16))
                                                    .withItemMeta()
                                                    .setDisplayName("§7<< пустота >>")
                                                    .then()
                                                    .build()
                                    )
                            ).setPatterns(
                            "sssssssss",
                            "         ",
                            "         ",
                            "         ",
                            "sssssssss"
                    ).build()
            );

    public InteractListener(SkyWars skyWars) {
        this.skyWars = skyWars;
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        /* Если игра еще не началась, а так же сошлись звезды */
        if (skyWars.getGameCycle().getGameStatus().equals(GameStatus.STARTING) &&
                player.getInventory().getItemInMainHand() != null
        ) {
            /* Если в руках слизь */
            if (player.getInventory().getItemInMainHand().getType().equals(Material.SLIME_BALL)) {
                PerPlayerGuiBuilder guiBuilder = guiPrototype.get().setTitle("Выбор класса");

                PlayerStatistic playerStatistic = skyWars.getPlayerStatistic().get(player.getUniqueId());

                /* Пробигаемся по всем наборам */
                int i = 19;
                for (Set kit : Set.values()) {
                    if (kit.equals(Set.NO_ACTIVE_KIT))
                        continue;

                    /* Замена оригинального названия на название содержащее информацию о наличии данного набора */
                    ItemStack itemStack = kit.getSettable().getIcon().get();
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    if (playerStatistic.getCurrentKit().equals(kit)) {
                        itemMeta.setDisplayName(itemMeta.getDisplayName() + " | §a§lВЫБРАНО");
                        itemMeta.addEnchant(Enchantment.LUCK, 1, false);
                    } else if (playerStatistic.getKits().contains(kit)) {
                        itemMeta.setDisplayName(itemMeta.getDisplayName() + " | §7§lДОСТУПНО");
                    } else
                        itemMeta.setDisplayName(itemMeta.getDisplayName() + " | §6§lКУПИТЬ");
                    itemStack.setItemMeta(itemMeta);

                    /* Выставление иконки в свое место и написание обработчика */
                    guiBuilder.setIcon(i++, new ConsumerableIcon(itemStack, event -> {
                        if (playerStatistic.getKits().contains(kit)) {
                            playerStatistic.setCurrentKit(kit);
                            player.sendMessage("[§bi§f] §7Вы выбрали класс " + kit.getSettable().getName());
                        } else
                            return; /* todo: Реализация покупки класса */
                        player.closeInventory();
                    }
                    ));
                }

                /* Открытие самого инвентаря с наборами */
                guiBuilder.getGui().open(player);
            }
            /* Если в руках кварц */
            else if (player.getInventory().getItemInMainHand().getType().equals(Material.QUARTZ)) {

                PerPlayerGuiBuilder guiBuilder = guiPrototype.get().setTitle("Выбор команды");

                int i = skyWars.getTeams() > 7 ? 18 : 19;
                for (Team team : Team.teams(skyWars.getTeams())) {

                    ItemStack teamIcon = team.getIcon().get();
                    ItemMeta itemMeta = teamIcon.getItemMeta();

                    if (team.getPlayers().contains(player))
                        itemMeta.addEnchant(Enchantment.LUCK, 1, true);

                    List<String> lore = new ArrayList<>();
                    lore.add("");
                    for (Player currentPlayer : team.getPlayers())
                        lore.add("§6* §f" + currentPlayer.getName());
                    itemMeta.setLore(lore);
                    teamIcon.setItemMeta(itemMeta);

                    guiBuilder.setIcon(i++, new ConsumerableIcon(teamIcon, event -> {
                        if (team.getPlayers().size() < skyWars.getPlayersInTeam()) {
                            Team.getPlayerTeam(player, skyWars.getTeams()).getPlayers().remove(player);

                            team.getPlayers().add(player);
                            player.sendMessage("[§bi§f] §7Вы выбрали команду " + team.getName());
                        } else
                            player.sendMessage("[§bi§f] §7Команда переполнена!");
                        player.closeInventory();

                    }));
                }

                guiBuilder.getGui().open(player);
            } else if (player.getInventory().getItemInMainHand().getType().equals(Material.PAPER)) {

                PerPlayerGuiBuilder guiBuilder = guiPrototype.get().setTitle("Ваша статистика");

                PlayerStatistic playerStatistic = skyWars.getPlayerStatistic().get(player.getUniqueId());

                /* Вывод статистики дальнего боя */
                guiBuilder.setIcon(20, new NotClickableIcon(new ItemStackBuilderImpl()
                        .setMaterial(Material.BOW)
                        .withItemMeta()
                        .setDisplayName("§eДальний бой")
                        .setLore(
                                "",
                                String.format("§fМеткость: §6%s%s", playerStatistic.getHits()/playerStatistic.getStrikes() * 100, "%"),
                                "§fУбийств из лука: §6" + playerStatistic.getBowKills(),
                                "§fВыстрелов: §6" + playerStatistic.getStrikes(),
                                "§fПопаданий: §6" + playerStatistic.getHits()
                        ).then()
                        .build()
                ));

                /* Вывод общей статистики */
                guiBuilder.setIcon(22, new NotClickableIcon(new ItemStackBuilderImpl()
                        .setMaterial(Material.ENDER_PEARL)
                        .withItemMeta()
                        .setDisplayName("§eОбщая статистика")
                        .setLore(
                                "",
                                "§fВыйграшей: §6" + playerStatistic.getWins(),
                                "§fСыграно игр: §6" + playerStatistic.getGames(),
                                String.format("§fВремени сыграно: §6%s (мин)", playerStatistic.getGameTime()/60_000),
                                "§fСундуков открыто: §6" + playerStatistic.getChestOpened(),
                                String.format("§fСобрано наборов: §6%s/%d", playerStatistic.getKits().size() - 1, Set.values().length - 1)
                        ).then()
                        .build()
                ));

                /* Вывод статистики ближнего боя */
                guiBuilder.setIcon(24, new NotClickableIcon(new ItemStackBuilderImpl()
                        .setMaterial(Material.IRON_SWORD)
                        .withItemMeta()
                        .setDisplayName("§eБлижний бой")
                        .setLore(
                                "",
                                String.format("§fНанесено урона: §6%.2fK", playerStatistic.getDamage() / 1000D),
                                "§fУбийств в ближнем бою: §6" + playerStatistic.getKills()
                        ).then()
                        .build()
                ));

                guiBuilder.getGui().open(player);
            }
        }
    }
}
