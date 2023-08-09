package eu.internetak.baloperationtask.commands;

import eu.internetak.baloperationtask.BalOperationTask;
import eu.internetak.baloperationtask.objects.BalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {

    private BalOperationTask plugin;

    public BalanceCommand(BalOperationTask plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;

        Player target = p;

        if (args.length > 0) {
            String targetName = args[0];
            target = Bukkit.getPlayer(targetName);
            if (target == null) {
                sender.sendMessage(
                        plugin.getConfig().getString("messages.commands.bal.target_not_online")
                                .replace("&", "§")
                                .replace("%player", targetName)
                );
                return false;
            }
        }

        BalPlayer balPlayer = plugin.players.get(target.getUniqueId().toString());

        sender.sendMessage(
                plugin.getConfig().getString(
                                target == p ?
                                        "messages.commands.bal.sender_balance" :
                                        "messages.commands.bal.target_balance"
                        )
                        .replace("&", "§")
                        .replace("%bal", String.valueOf(balPlayer.getBalance()))
                        .replace("%player", target.getName())
        );

        return false;
    }
}
