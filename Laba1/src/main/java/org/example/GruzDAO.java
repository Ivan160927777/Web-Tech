package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GruzDAO {
    private Connection connection;

    public GruzDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE
    public void addGruz(Gruz gruz) throws SQLException {
        String sql = "INSERT INTO gruz (name, sklad_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, gruz.getName());
            ps.setInt(2, gruz.getSkladId());
            ps.executeUpdate();
        }
    }

    // READ
    public List<Gruz> getAllGruz() throws SQLException {
        List<Gruz> list = new ArrayList<>();
        String sql = "SELECT * FROM gruz";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Gruz(rs.getInt("id"), rs.getString("name"), rs.getInt("sklad_id")));
            }
        }
        return list;
    }

    public Gruz getGruzById(int id) throws SQLException {
        String sql = "SELECT * FROM gruz WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Gruz(rs.getInt("id"), rs.getString("name"), rs.getInt("sklad_id"));
                }
            }
        }
        return null;
    }

    // UPDATE
    public void updateGruz(Gruz gruz) throws SQLException {
        String sql = "UPDATE gruz SET name = ?, sklad_id = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, gruz.getName());
            ps.setInt(2, gruz.getSkladId());
            ps.setInt(3, gruz.getId());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void deleteGruz(int id) throws SQLException {
        String sql = "DELETE FROM gruz WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
