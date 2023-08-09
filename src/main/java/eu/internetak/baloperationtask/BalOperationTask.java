package eu.internetak.baloperationtask;

import eu.internetak.baloperationtask.commands.BalanceCommand;
import eu.internetak.baloperationtask.commands.EarnCommand;
import eu.internetak.baloperationtask.commands.GiveCommand;
import eu.internetak.baloperationtask.commands.SetbalCommand;
import eu.internetak.baloperationtask.database.Database;
import eu.internetak.baloperationtask.listeners.PlayerListener;
import eu.internetak.baloperationtask.objects.BalPlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class BalOperationTask extends JavaPlugin {

    public Map<String, BalPlayer> players = new HashMap<>();
    public Database database;

    public void onEnable() {
        saveDefaultConfig();
        database = new Database();
        new PlayerListener(this);
        getCommand("bal").setExecutor(new BalanceCommand(this));
        getCommand("give").setExecutor(new GiveCommand(this));
        getCommand("setbal").setExecutor(new SetbalCommand(this));
        getCommand("earn").setExecutor(new EarnCommand(this));
    }
}
