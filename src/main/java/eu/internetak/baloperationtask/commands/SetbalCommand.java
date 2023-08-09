package eu.internetak.baloperationtask.commands;

import eu.internetak.baloperationtask.BalOperationTask;
import eu.internetak.baloperationtask.objects.BalPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetbalCommand implements CommandExecutor {

    private BalOperationTask plugin;

    public SetbalCommand(BalOperationTask plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.isOp()) {
            sender.sendMessage(
                    plugin.getConfig().getString("messages.commands.setbal.no_permission")
                            .replace("&", "§")
            );
            return false;
        }

        if (args.length < 2) {
            sender.sendMessage(
                    plugin.getConfig().getString("messages.commands.setbal.wrong_usage")
                            .replace("&", "§")
            );
            return false;
        }

        String tagetName = args[0];

        Player target = Bukkit.getPlayer(tagetName);

        if (target == null) {
            sender.sendMessage(
                    plugin.getConfig().getString("messages.commands.setbal.target_not_online")
                            .replace("&", "§")
            );
            return false;
        }


        String strAmount = args[1];
        if (!strAmount.matches("\\d+")) {
            sender.sendMessage(
                    plugin.getConfig().getString("messages.commands.setbal.amount_not_integer")
                            .replace("&", "§")
            );
            return false;
        }

        long amount = Long.parseLong(strAmount);

        if (amount < 0) {
            sender.sendMessage(
                    plugin.getConfig().getString("messages.commands.setbal.amount_not_integer")
                            .replace("&", "§")
            );
        }

        BalPlayer targetPlayer = plugin.players.get(target.getUniqueId().toString());

        targetPlayer.setBalance(amount);

        plugin.database.savePlayer(targetPlayer);

        sender.sendMessage(
                plugin.getConfig().getString("messages.commands.setbal.setbal_success")
                        .replace("&", "§")
                        .replace("%player", target.getName())
                        .replace("%bal", String.valueOf(targetPlayer.getBalance()))
        );

        return true;
    }
}
