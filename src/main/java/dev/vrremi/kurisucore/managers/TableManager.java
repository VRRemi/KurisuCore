package dev.vrremi.kurisucore.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableManager {

    public TableManager(Connection connection) {
        createTable(connection);
    }

    private void createTable(Connection connection) {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `kurisu_users` " +
                            "(" +
                            "uuid VARCHAR(40)," +
                            "name VARCHAR(16)," +
                            "rank LONGTEXT," +
                            "permissions LONGTEXT," +
                            "tag TEXT," +
                            "punishments LONGTEXT" +
                            ");"
            );
            statement.execute();
            statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS `kurisu_ranks` " +
                            "(" +
                            "name VARCHAR(60)," +
                            "priority INT," +
                            "prefix VARCHAR(64)," +
                            "suffix VARCHAR(64)," +
                            "color VARCHAR(16)," +
                            "permissions LONGTEXT," +
                            "default_rank BOOLEAN" +
                            ");"
            );
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating table schema");
        } finally {
            ConnectionPoolManager.close(connection, statement);
        }
    }

}
