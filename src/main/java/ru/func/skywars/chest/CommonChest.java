package ru.func.skywars.chest;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import ru.yamycraft.api.gui.builder.item.ItemStackBuilderImpl;

import java.util.Arrays;
import java.util.List;

/**
 * @author func 08.09.2019
 */
public class CommonChest implements Openable {

    private ItemStack[] heap = {
            new ItemStackBuilderImpl()
                    .setMaterial(Material.SNOW_BALL)
                    .setAmount(12)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.EGG)
                    .setAmount(8)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.ARROW)
                    .setAmount(10)
                    .build(),
            new ItemStackBuilderImpl().setMaterial(Material.LAVA_BUCKET).build(),
            new ItemStackBuilderImpl().setMaterial(Material.WATER_BUCKET).build(),
            new ItemStackBuilderImpl().setMaterial(Material.WATER_BUCKET).build(),
            new ItemStackBuilderImpl().setMaterial(Material.FLINT_AND_STEEL).build(),
    };

    private ItemStack[] helmet = {
            new ItemStackBuilderImpl()
                    .setMaterial(Material.CHAINMAIL_HELMET)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.IRON_HELMET)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.IRON_HELMET)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
    };

    private ItemStack[] chestPlate = {
            new ItemStackBuilderImpl()
                    .setMaterial(Material.CHAINMAIL_CHESTPLATE)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.IRON_CHESTPLATE)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.IRON_CHESTPLATE)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
    };

    private ItemStack[] leggins = {
            new ItemStackBuilderImpl()
                    .setMaterial(Material.CHAINMAIL_LEGGINGS)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.IRON_LEGGINGS)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.IRON_LEGGINGS)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
    };

    private ItemStack[] boots = {
            new ItemStackBuilderImpl()
                    .setMaterial(Material.CHAINMAIL_BOOTS)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.IRON_BOOTS)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.IRON_BOOTS)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
    };

    @Override
    public List<ItemStack> getDrop() {
        return Arrays.asList(
                helmet[RANDOM.nextInt(helmet.length)],
                chestPlate[RANDOM.nextInt(chestPlate.length)],
                leggins[RANDOM.nextInt(leggins.length)],
                boots[RANDOM.nextInt(boots.length)],

                BLOCKS[RANDOM.nextInt(BLOCKS.length)],
                BLOCKS[RANDOM.nextInt(BLOCKS.length)],
                BLOCKS[RANDOM.nextInt(BLOCKS.length)],

                heap[RANDOM.nextInt(heap.length)],
                heap[RANDOM.nextInt(heap.length)],
                heap[RANDOM.nextInt(heap.length)]
        );
    }
}
