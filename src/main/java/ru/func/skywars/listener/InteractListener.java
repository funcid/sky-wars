package ru.func.skywars.listener;

import org.bukkit.ChatColor;
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
import java.util.function.Supplier;

/**
 * @author func 08.09.2019
 */
public class InteractListener implements Listener {

    private SkyWars skyWars;
    private Supplier<PerPlayerGuiBuilder> guiPrototype = () -> PerPlayerGuiBuilder.generate(45)
            .setLayout(
                    new LayoutBuilder()
                            .addLayout(
                                    's',
                                    new NotClickableIcon(
                                            new ItemStackBuilderImpl()
                                                    .setMaterial(Material.STAINED_GLASS_PANE)
                                                    .setData(7)
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
                PerPlayerGuiBuilder guiBuilder = guiPrototype.get().setTitle("§e§l[ §f§lВыбор класса §e§l]");

                PlayerStatistic playerStatistic = skyWars.getPlayerStatistic().get(player.getUniqueId());

                /* Пробигаемся по всем наборам */
                int i = 19;
                for (Set kit : Set.values()) {
                    if (kit.equals(Set.NO_ACTIVE_KIT))
                        continue;

                    /* Замена оригинального названия на название содержащее информацию о наличии данного набора */
                    ItemStack itemStack = kit.getSettable().getIcon().get();
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setDisplayName(
                            itemMeta.getDisplayName() +
                                    (playerStatistic.getKits().contains(kit) ? " | §a§lДОСТУПНО" : " | §c§lНЕ ДОСТУПНО")
                    );
                    itemStack.setItemMeta(itemMeta);

                    /* Выставление иконки в свое место и написание обработчика */
                    guiBuilder.setIcon(i, new ConsumerableIcon(itemStack, event -> {
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

                PerPlayerGuiBuilder guiBuilder = guiPrototype.get().setTitle("§e§l[ §f§lВыбор команды §e§l]");

                int i = 18;
                for (Team team : Team.teams(skyWars.getTeams())) {

                    ItemStack teamIcon = team.getIcon().get();
                    ItemMeta itemMeta = teamIcon.getItemMeta();

                    if (team.getPlayers().contains(player))
                        itemMeta.addEnchant(Enchantment.LUCK, 1, true);

                    List<String> lore = new ArrayList<>();
                    lore.add("§8" + team.name());
                    for (Player currentPlayer : team.getPlayers())
                        lore.add("§6* §7" + currentPlayer.getName());
                    itemMeta.setLore(lore);
                    teamIcon.setItemMeta(itemMeta);

                    guiBuilder.setIcon(i++, new ConsumerableIcon(teamIcon, event -> {
                        Team currentTeam = Team.valueOf(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getLore().get(0)));
                        if (currentTeam.getPlayers().size() < skyWars.getPlayersInTeam()) {
                            Team.getPlayerTeam(player, skyWars.getTeams()).getPlayers().remove(player);

                            currentTeam.getPlayers().add(player);
                            player.sendMessage("[§bi§f] §7Вы выбрали команду " + currentTeam.getName());
                        } else
                            player.sendMessage("[§bi§f] §7Команда переполнена!");
                        player.closeInventory();

                    }));
                }

                guiBuilder.getGui().open(player);
            }
        }
    }
}
