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

    

}
