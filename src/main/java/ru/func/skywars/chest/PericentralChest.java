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
public class PericentralChest implements Openable {

    private ItemStack[] heap = {
            new ItemStackBuilderImpl()
                    .setMaterial(Material.SNOW_BALL)
                    .setAmount(8)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.ARROW)
                    .setAmount(32)
                    .build(),
            new ItemStackBuilderImpl().setMaterial(Material.LAVA_BUCKET).build(),
            new ItemStackBuilderImpl().setMaterial(Material.WATER_BUCKET).build(),
            new ItemStackBuilderImpl().setMaterial(Material.WATER_BUCKET).build(),
            new ItemStackBuilderImpl().setMaterial(Material.FLINT_AND_STEEL).build(),
            new ItemStackBuilderImpl().setMaterial(Material.ENDER_PEARL).build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.SLIME_BALL)
                    .addEnchant(Enchantment.KNOCKBACK, 3)
                    .build(),
    };

    private ItemStack[] armor = {
            new ItemStackBuilderImpl()
                    .setMaterial(Material.IRON_HELMET)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.DIAMOND_HELMET)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.DIAMOND_HELMET)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.IRON_CHESTPLATE)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.DIAMOND_CHESTPLATE)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.IRON_LEGGINGS)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.DIAMOND_LEGGINGS)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.IRON_BOOTS)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.DIAMOND_BOOTS)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
            new ItemStackBuilderImpl()
                    .setMaterial(Material.DIAMOND_BOOTS)
                    .addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                    .addEnchant(Enchantment.PROTECTION_PROJECTILE, 2)
                    .build(),
    };

    @Override
    public List<ItemStack> getDrop() {
        return Arrays.asList(
                armor[RANDOM.nextInt(armor.length)],
                armor[RANDOM.nextInt(armor.length)],

                BLOCKS[RANDOM.nextInt(BLOCKS.length)],

                FOOD[RANDOM.nextInt(FOOD.length)],
                FOOD[RANDOM.nextInt(FOOD.length)],

                heap[RANDOM.nextInt(heap.length)]
        );
    }
}
