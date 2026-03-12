package Al3x.antiPodSoss;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import Al3x.antiPodSoss.listeners.PlayerDeathListener;
import Al3x.antiPodSoss.listeners.PlayerPickupListener;

public class AntiPodSos extends JavaPlugin {

    private static AntiPodSos instance;
    private LootManager lootManager;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        config = getConfig();

        lootManager = new LootManager(this);

        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerPickupListener(this), this);

        startParticleTask();

        getLogger().info("AntiPodSos успешно запущен!");
    }

    @Override
    public void onDisable() {
        if (lootManager != null) {
            lootManager.cleanup();
        }
        getLogger().info("AntiPodSos выключен!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("antipodsos")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("antipodsos.reload")) {
                    sendMessage(sender, "no-permission");
                    return true;
                }

                reloadConfig();
                config = getConfig();
                if (lootManager != null) {
                    lootManager.reloadConfig();
                }

                sendMessage(sender, "config-reloaded");
                return true;
            }

            // Показать помощь
            sender.sendMessage(ChatColor.YELLOW + "AntiPodSos v" + getDescription().getVersion());
            sender.sendMessage(ChatColor.YELLOW + "Используйте: /antipodsos reload");
            return true;
        }
        return false;
    }

    private void startParticleTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (lootManager != null) {
                    lootManager.updateParticles();
                }
            }
        }.runTaskTimer(this, 0L, 5L);
    }

    public void sendMessage(CommandSender sender, String messageKey) {
        String message = config.getString("settings.messages." + messageKey, "");
        if (!message.isEmpty()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    public static AntiPodSos getInstance() {
        return instance;
    }

    public LootManager getLootManager() {
        return lootManager;
    }

    public FileConfiguration getPluginConfig() {
        return config;
    }

}
