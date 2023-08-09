package eu.internetak.baloperationtask.commands;

import eu.internetak.baloperationtask.BalOperationTask;
import eu.internetak.baloperationtask.objects.BalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand implements CommandExecutor {

    private BalOperationTask plugin;

    public GiveCommand(BalOperationTask plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length < 2) {
            sender.sendMessage(
                    plugin.getConfig().getString("messages.commands.give.wrong_usage")
                            .replace("&", "§")
            );
            return false;
        }

        String tagetName = args[0];

        Player target = Bukkit.getPlayer(tagetName);

        if (target == null) {
            sender.sendMessage(
                    plugin.getConfig().getString("messages.commands.give.target_not_online")
                            .replace("&", "§")
                            .replace("%player", tagetName)
            );
            return false;
        }


        String strAmount = args[1];
        if (!strAmount.matches("\\d+")) {
            sender.sendMessage(
                    plugin.getConfig().getString("messages.commands.give.amount_not_integer")
                            .replace("&", "§")
            );
            return false;
        }

        long amount = Long.parseLong(strAmount);

        if (amount <= 0) {
            sender.sendMessage(
                    plugin.getConfig().getString("messages.commands.give.amount_not_integer")
                            .replace("&", "§")
            );
        }

        BalPlayer senderPlayer = plugin.players.get(((Player) sender).getUniqueId().toString());
        BalPlayer targetPlayer = plugin.players.get(target.getUniqueId().toString());

        if (senderPlayer.getBalance() < amount) {
            sender.sendMessage(
                    plugin.getConfig().getString("messages.commands.give.sender_low_balance")
                            .replace("&", "§")
                            .replace("%bal", String.valueOf(senderPlayer.getBalance()))
            );
            return false;
        }

        senderPlayer.setBalance(senderPlayer.getBalance() - amount);
        targetPlayer.setBalance(targetPlayer.getBalance() + amount);

        plugin.database.savePlayer(senderPlayer);
        plugin.database.savePlayer(targetPlayer);

        sender.sendMessage(plugin.getConfig().getString("messages.commands.give.sender_give_success")
                .replace("&", "§")
                .replace("%bal", String.valueOf(amount))
                .replace("%player", target.getName()));

        target.sendMessage(plugin.getConfig().getString("messages.commands.give.target_give_success")
                .replace("&", "§")
                .replace("%bal", String.valueOf(amount))
                .replace("%player", sender.getName()));

        return true;
    }
}
