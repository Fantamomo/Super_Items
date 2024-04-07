package at.leisner.super_items;

import at.leisner.super_items.item.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    private List<CustomItem> customItems = new ArrayList<>();
    private Super_Items plugin;

    public ItemManager() {
        this.plugin = Super_Items.getInstance();
    }

    public void addItem(CustomItem item) {
        customItems.add(item);
        if (item.getRecipe() != null) {
            plugin.getServer().addRecipe(item.getRecipe());
        }
        plugin.getServer().getPluginManager().registerEvents(item,plugin);
    }
    public void removeItem(CustomItem item) {
        customItems.remove(item);
        if (item.getNameKey() != null) {
            plugin.getServer().removeRecipe(item.getNameKey());
        }
        plugin.getServer().getPluginManager().registerEvents(item, plugin);
    }
    public static boolean isSame(ItemStack item, CustomItem customItem) {
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().getOrDefault(Super_Items.customItemNamespacedKey, PersistentDataType.STRING,"").equals(customItem.getName());
    }
    public void register() {
        addItem(new FireBall());
        addItem(new InstaTNT());
        addItem(new NoTargetHelmet());
        addItem(new WindFeather());
        addItem(new ArrowRide());
        addItem(new FireSword());
        addItem(new InvisibilityHelmet());
        addItem(new SmeltingPickaxe());
    }

    public List<CustomItem> getCustomItems() {
        return customItems;
    }
}
