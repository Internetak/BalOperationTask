package eu.internetak.baloperationtask.commands;

import eu.internetak.baloperationtask.BalOperationTask;
import eu.internetak.baloperationtask.objects.BalPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

public class EarnCommand implements CommandExecutor {

    private BalOperationTask plugin;

    public EarnCommand(BalOperationTask plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        BalPlayer balPlayer = plugin.players.get(p.getUniqueId().toString());

        // if lastearn less than 1 minute, return false
        if (balPlayer.getLastEarn() != 0 && System.currentTimeMillis() - balPlayer.getLastEarn() < 60000) {
            sender.sendMessage(plugin.getConfig().getString("messages.commands.earn.used_already")
                    .replace("&", "§")
                    .replace("%seconds", String.valueOf(
                            (long) (balPlayer.getLastEarn() - System.currentTimeMillis() + 60000) / 1000
                    ))
            );
            return false;
        }

        long amount = new Random().nextInt(5) + 1;

        balPlayer.setLastEarn(System.currentTimeMillis());
        balPlayer.setBalance(balPlayer.getBalance() + amount);
        plugin.database.savePlayer(balPlayer);

        sender.sendMessage(plugin.getConfig().getString("messages.commands.earn.earn_success")
                .replace("&", "§")
                .replace("%amount", String.valueOf(amount))
                .replace("%bal", String.valueOf(balPlayer.getBalance())));

        return false;
    }
}
