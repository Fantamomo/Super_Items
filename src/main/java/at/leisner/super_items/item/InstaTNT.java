package at.leisner.super_items.item;

import at.leisner.super_items.CustomItem;
import at.leisner.super_items.ItemManager;
import at.leisner.super_items.Super_Items;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InstaTNT implements CustomItem {
    private ItemStack item;
    private ShapelessRecipe recipe;
    private NamespacedKey namespacedKey = new NamespacedKey(Super_Items.getInstance(), "insta_tnt");

    public InstaTNT() {
        item = new ItemStack(Material.TNT);
        item.addUnsafeEnchantment(Enchantment.ARROW_FIRE,1);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(Component.text("INSTA TNT").color(TextColor.color(255, 0, 0)));
        itemMeta.getPersistentDataContainer().set(Super_Items.customItemNamespacedKey, PersistentDataType.STRING, getName());
        item.setItemMeta(itemMeta);



        recipe = new ShapelessRecipe(namespacedKey, item);
        recipe.addIngredient(Material.FLINT_AND_STEEL);
        recipe.addIngredient(Material.TNT);
    }

    @Override
    public @Nullable Recipe getRecipe() {
        return recipe;
    }

    @Override
    public @NotNull ItemStack getItem() {
        return item;
    }

    @Override
    public NamespacedKey getNameKey() {
        return namespacedKey;
    }

    @Override
    public @NotNull String getName() {
        return namespacedKey.getKey();
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!ItemManager.isSame(event.getItemInHand(), this)) return;
        Player p = event.getPlayer();
        Location location = event.getBlock().getLocation();
        location.set(location.x()+.5, location.y(), location.z()+.5);
        event.getBlock().setType(Material.AIR);
        p.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
    }
}
