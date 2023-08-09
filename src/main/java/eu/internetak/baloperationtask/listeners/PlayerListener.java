package eu.internetak.baloperationtask.listeners;

import eu.internetak.baloperationtask.BalOperationTask;
import eu.internetak.baloperationtask.objects.BalPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final BalOperationTask plugin;

    public PlayerListener(BalOperationTask plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        plugin.players.put(
                e.getPlayer().getUniqueId().toString(),
                plugin.database.fetchPlayer(e.getPlayer().getUniqueId().toString()
                ));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        BalPlayer player = plugin.players.get(e.getPlayer().getUniqueId().toString());
        System.out.println("Player left: " + player.getUuid() + ", " + player.getBalance());
        boolean result = plugin.database.savePlayer(player);
        System.out.println("Result: " + result);
    }
}
