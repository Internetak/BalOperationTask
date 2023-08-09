package eu.internetak.baloperationtask.objects;

public class BalPlayer {

    private final String uuid;
    private long balance;
    private long lastEarn;
    private boolean newPlayer = false;

    public BalPlayer(String uuid) {
        this.uuid = uuid;
        this.balance = 0;
        this.lastEarn = 0;
        this.newPlayer = true;
    }

    public BalPlayer(String uuid, long balance, long lastEarn) {
        this.uuid = uuid;
        this.balance = balance;
        this.lastEarn = lastEarn;
    }

    public String getUuid() {
        return uuid;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public long getLastEarn() {
        return lastEarn;
    }

    public void setLastEarn(long lastEarn) {
        this.lastEarn = lastEarn;
    }

    public boolean isNewPlayer() {
        return newPlayer;
    }
}
