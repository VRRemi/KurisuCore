package dev.vrremi.kurisucore.managers.data;

import dev.vrremi.kurisucore.KurisuCore;
import dev.vrremi.kurisucore.data.*;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserDataManager {

    public void create(Player player, Connection connection) throws SQLException {
        if (!playerExists(player.getUniqueId(), connection)) {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO `kurisu_users` (uuid, name, rank, permissions, tag, punishments) " +
                            "VALUES (?, ?, ?, ?, ?, ?)");
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            statement.setString(3, "");
            statement.setString(4, "");
            statement.setString(5, "");
            statement.setString(6, "");
            statement.executeUpdate();
        }
        updateName(player, connection);
    }

}
