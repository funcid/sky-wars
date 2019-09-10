package ru.func.skywars.listener;

import org.bukkit.Material;
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
import ru.yamycraft.api.gui.builder.LayoutBuilder;
import ru.yamycraft.api.gui.builder.PerPlayerGuiBuilder;
import ru.yamycraft.api.gui.builder.item.ItemStackBuilderImpl;
import ru.yamycraft.api.gui.icon.ConsumerableIcon;
import ru.yamycraft.api.gui.icon.NotClickableIcon;

/**
 * @author func 08.09.2019
 */
public class InteractListener implements Listener {

    private SkyWars skyWars;
    private PerPlayerGuiBuilder voteKitGui = PerPlayerGuiBuilder.generate(45)
            .setTitle("§e§l[ §f§lВыбор класса §e§l]")
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

        /* Если игра еще не началась и выбранный предмет слизь, а так же сошлись звезды */
        if (skyWars.getGameCycle().getGameStatus().equals(GameStatus.STARTING) &&
                player.getInventory().getItemInMainHand() != null &&
                player.getInventory().getItemInMainHand().getType().equals(Material.SLIME_BALL)
        ) {
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
                voteKitGui.setIcon(i, new ConsumerableIcon(itemStack, event -> {
                    if (playerStatistic.getKits().contains(kit)) {
                        playerStatistic.setCurrentKit(kit);
                        event.getWhoClicked().sendMessage("[§bi§f] §7Вы выбрали класс " + kit.getSettable().getName());
                    } else
                        return; /* todo: Реализация покупки класса */
                    event.getWhoClicked().closeInventory();
                }
                ));
            }

            /* Открытие самого инвентаря с наборами */
            voteKitGui.getGui().open(player);
        }
    }
}
