package dev.vrremi.kurisucore.managers.data;

import dev.vrremi.kurisucore.data.Permission;
import dev.vrremi.kurisucore.data.Rank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RankDataManager {

    public void createRank(String name, Connection connection) throws SQLException {
        if (!rankExists(name, connection)) {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO `kurisu_ranks` (name, priority, prefix, suffix, color, " +
                            "permissions, default_rank) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, name);
            statement.setInt(2, -1);
            statement.setString(3, "");
            statement.setString(4, "");
            statement.setString(5, "");
            statement.setString(6, "");
            statement.setBoolean(7, false);
            statement.executeUpdate();
        }
    }

    public void deleteRank(String name, Connection connection) throws SQLException {
        if (rankExists(name, connection)) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM `kurisu_ranks` WHERE LOWER(name)=?");
            statement.setString(1, name.toLowerCase());
            statement.execute();
        }
    }

    public int getPriority(String name, Connection connection) throws SQLException {
        if (rankExists(name, connection)) {
            return getInteger(name, "priority", connection);
        }
        return 0;
    }

    public void setPriority(String name, int priority, Connection connection) throws SQLException {
        if (rankExists(name, connection)) {
            setInteger(name, "priority", priority, connection);
        }
    }

    public String getPrefix(String name, Connection connection) throws SQLException {
        if (rankExists(name, connection)) {
            return getString(name, "prefix", connection);
        }
        return null;
    }

    public void setPrefix(String name, String prefix, Connection connection) throws SQLException {
        if (rankExists(name, connection)) {
            setString(name, "prefix", prefix, connection);
        }
    }

    public String getSuffix(String name, Connection connection) throws SQLException {
        if (rankExists(name, connection)) {
            return getString(name, "suffix", connection);
        }
        return null;
    }


}
