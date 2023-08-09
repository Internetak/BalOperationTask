package eu.internetak.baloperationtask.database;

import eu.internetak.baloperationtask.objects.BalPlayer;

import java.sql.*;

public class Database {

    private Connection connection;

    public Database() {
        connect();
    }

    private boolean connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/BalOperationTask/database.db");

            try (PreparedStatement statement =
                         connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (" +
                                 "player_uuid TEXT PRIMARY KEY, " +
                                 "balance INTEGER, " +
                                 "last_earn INTEGER)");
            ) {
                statement.executeUpdate();
            }
            return true;
        } catch (Exception e) {
            System.out.println("[BalOperationTask] Problem connecting to database:");
            e.printStackTrace();
            return false;
        }
    }

    private boolean tryReconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return true;
            } else {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
                return connect();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public BalPlayer fetchPlayer(String uuid) {
        if (!tryReconnect()) {
            return null;
        }

        try {
            try (PreparedStatement statement =
                         connection.prepareStatement("SELECT * FROM users WHERE player_uuid = ?")) {
                statement.setString(1, uuid);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return new BalPlayer(uuid, resultSet.getLong("balance"), resultSet.getLong("last_earn"));
                    } else {
                        return new BalPlayer(uuid);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean savePlayer(BalPlayer player) {
        if (!tryReconnect()) {
            return false;
        }

        if (player.isNewPlayer()) {
            try {
                try (PreparedStatement statement =
                             connection.prepareStatement("INSERT INTO users (player_uuid, balance, last_earn) VALUES (?, ?, ?)")) {
                    statement.setString(1, player.getUuid());
                    statement.setLong(2, player.getBalance());
                    statement.setLong(3, player.getLastEarn());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                System.out.println("[BalOperationTask] Problem saving player to database:");
                e.printStackTrace();
                return false;
            }
        } else {
            try {
                try (PreparedStatement statement =
                             connection.prepareStatement("UPDATE users SET balance = ?, last_earn = ? WHERE player_uuid = ?")) {
                    statement.setLong(1, player.getBalance());
                    statement.setLong(2, player.getLastEarn());
                    statement.setString(3, player.getUuid());
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                System.out.println("[BalOperationTask] Problem saving player to database:");
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }
}
