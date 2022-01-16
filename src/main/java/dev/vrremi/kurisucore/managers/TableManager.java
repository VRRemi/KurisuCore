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

}
