package at.leisner.super_items.item;

import at.leisner.super_items.CustomItem;
import at.leisner.super_items.ItemManager;
import at.leisner.super_items.Super_Items;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class FireBall implements CustomItem {
    private ItemStack item;
    private ShapedRecipe recipe;
    private NamespacedKey namespacedKey = new NamespacedKey(Super_Items.getInstance(), "fire_charge");

    public FireBall() {
        item = new ItemStack(Material.FIRE_CHARGE);
        item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(Component.text("FIRE CHARGE").color(TextColor.color(255, 0, 0)));
        itemMeta.getPersistentDataContainer().set(Super_Items.customItemNamespacedKey, PersistentDataType.STRING, getName());
        item.setItemMeta(itemMeta);

        recipe = new ShapedRecipe(namespacedKey, item);
        recipe.shape("FFF","FDF","FFF");
        recipe.setIngredient('F',Material.FIRE_CHARGE);
        recipe.setIngredient('D', Material.DIAMOND);
    }

    @Override
    public Recipe getRecipe() {
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
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        if (event.getItem() == null || event.getItem().isEmpty()) return;
        if (!ItemManager.isSame(event.getItem(),this)) return;
        event.setCancelled(true);
        Player p = event.getPlayer();
        if (p.getCooldown(Material.FIRE_CHARGE) != 0) return;
        if (p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE) {
            ItemStack itemEvent = event.getItem();
            itemEvent.setAmount(event.getItem().getAmount()-1);
            p.getInventory().setItem(event.getHand(), itemEvent);
            p.setCooldown(Material.FIRE_CHARGE,35);
        }
        Vector direction = p.getLocation().getDirection().multiply(3);
        Fireball fireball = p.getWorld().spawn(p.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), Fireball.class);
        fireball.setShooter(p);
        fireball.setDirection(direction);
    }
}
