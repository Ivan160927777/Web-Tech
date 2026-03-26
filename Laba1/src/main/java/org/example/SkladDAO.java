package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SkladDAO {
    private Connection connection;

    public SkladDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE
    public void addSklad(Sklad sklad) throws SQLException {
        String sql = "INSERT INTO sklad (name, address) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sklad.getName());
            ps.setString(2, sklad.getAddress());
            ps.executeUpdate();
        }
    }

    // READ
    public List<Sklad> getAllSklads() throws SQLException {
        List<Sklad> list = new ArrayList<>();
        String sql = "SELECT * FROM sklad";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Sklad(rs.getInt("id"), rs.getString("name"), rs.getString("address")));
            }
        }
        return list;
    }

    public Sklad getSkladById(int id) throws SQLException {
        String sql = "SELECT * FROM sklad WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Sklad(rs.getInt("id"), rs.getString("name"), rs.getString("address"));
                }
            }
        }
        return null;
    }

    // UPDATE
    public void updateSklad(Sklad sklad) throws SQLException {
        String sql = "UPDATE sklad SET name = ?, address = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sklad.getName());
            ps.setString(2, sklad.getAddress());
            ps.setInt(3, sklad.getId());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void deleteSklad(int id) throws SQLException {
        String sql = "DELETE FROM sklad WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
