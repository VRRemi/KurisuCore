package dev.vrremi.kurisucore.managers;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.vrremi.kurisucore.KurisuCore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionPoolManager {

    private HikariDataSource dataSource;

    private String hostname, port, database, username, password;
    private int minimumIdle, maximumPoolSize;
    private long connectionTimeout;

    public ConnectionPoolManager() {
        init();
        setupPool();
    }

    private void init() {
        hostname = KurisuCore.getConfigManager().getConfig().getString("sql-configuration.hostname");
        username = KurisuCore.getConfigManager().getConfig().getString("sql-configuration.username");
        password = KurisuCore.getConfigManager().getConfig().getString("sql-configuration.password");
        database = KurisuCore.getConfigManager().getConfig().getString("sql-configuration.database");
        port = KurisuCore.getConfigManager().getConfig().getString("sql-configuration.port");

        minimumIdle = KurisuCore.getConfigManager().getConfig().getInt("sql-configuration.hikariCP-configuration.minimum-idle");
        maximumPoolSize = KurisuCore.getConfigManager().getConfig().getInt("sql-configuration.hikariCP-configuration.maximum-pool-size");
        connectionTimeout = KurisuCore.getConfigManager().getConfig().getLong("sql-configuration.hikariCP-configuration.connection-timeout");
    }

    private void setupPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database);
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setUsername(username);
        config.setPassword(password);
        config.setMinimumIdle(minimumIdle);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setConnectionTimeout(connectionTimeout);
        dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting connection");
        }
    }

}
