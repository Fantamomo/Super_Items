package at.leisner.super_items;

import at.leisner.super_items.cmd.SuperItemCMD;
import at.leisner.super_items.item.FireBall;
import at.leisner.super_items.listener.JoinQuitListener;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import at.leisner.api.Api;

public final class Super_Items extends JavaPlugin {
    private Api apiPlugin;
    private static Super_Items plugin;
    private ItemManager itemManager;
    public static NamespacedKey customItemNamespacedKey;
    private CommandMap commandMap;

    @Override
    public void onEnable() {
        commandMap = getServer().getCommandMap();
        plugin = this;
        customItemNamespacedKey = new NamespacedKey(this,"custom_item_id");
        itemManager = new ItemManager();
        getServer().getPluginManager().registerEvents(new JoinQuitListener(), this);
        apiPlugin = (Api) getServer().getPluginManager().getPlugin("api");
        onStart();
    }
    private void onStart() {
        itemManager.register();
        commandMap.register("superitem", plugin.getName(), new SuperItemCMD());
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Api getApiPlugin() {
        return apiPlugin;
    }
    public static Super_Items getInstance() {
        return plugin;
    }
    public ItemManager getItemManager() {
        return itemManager;
    }
}
