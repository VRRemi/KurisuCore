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

    public void setSuffix(String name, String suffix, Connection connection) throws SQLException {
        if (rankExists(name, connection)) {
            setString(name, "suffix", suffix, connection);
        }
    }

    public String getColor(String name, Connection connection) throws SQLException {
        if (rankExists(name, connection)) {
            return getString(name, "color", connection);
        }
        return null;
    }

    public void setColor(String name, String color, Connection connection) throws SQLException {
        if (rankExists(name, connection)) {
            setString(name, "color", color, connection);
        }
    }

    public List<Permission> getPermissions(String name, Connection connection) throws SQLException {
        if (rankExists(name, connection)) {
            String permissionData = getString(name, "permissions", connection);
            List<Permission> permissions = new ArrayList<>();
            for (String perm : permissionData.split(",")) {
                if (perm.isEmpty()) continue;
                permissions.add(new Permission(perm, Long.MAX_VALUE));
            }
            return permissions;
        }
        return null;
    }

    public void setPermissions(String name, List<Permission> permissions, Connection connection) throws SQLException {
        if (rankExists(name, connection)) {
            StringBuilder permissionString = new StringBuilder();
            for (Permission permission : permissions) {
                permissionString
                        .append(permission.getNode())
                        .append(",");
            }
            setString(name, "permissions", permissionString.substring(0, permissionString.length() - 1), connection);
        }
    }

    public boolean getDefault(String name, Connection connection) throws SQLException {
        if (rankExists(name, connection)) {
            return getBoolean(name, "default_rank", connection);
        }
        return false;
    }

    public void setDefault(String name, boolean defaultRank, Connection connection) throws SQLException {
        if (rankExists(name, connection)) {
            setBoolean(name, "default_rank", defaultRank, connection);
        }
    }

    


}
