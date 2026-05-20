package Al3x.antiPodSoss.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import Al3x.antiPodSoss.AntiPodSos;

public class PlayerPickupListener implements Listener {

    private final AntiPodSos plugin;

    public PlayerPickupListener(AntiPodSos plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        if (!plugin.getLootManager().canPickup(player, event.getItem())) {
            event.setCancelled(true);
        }
    }
}