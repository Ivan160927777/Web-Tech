package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerevozDAO {
    private Connection connection;

    public PerevozDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE
    public void addPerevoz(Perevoz p) throws SQLException {
        String sql = "INSERT INTO perevoz (auto_id, gruz_id, sklad_id, driver_id, date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, p.getAutoId());
            ps.setInt(2, p.getGruzId());
            ps.setInt(3, p.getSkladId());
            ps.setInt(4, p.getDriverId());
            ps.setDate(5, p.getDate());
            ps.executeUpdate();
        }
    }

    // READ
    public List<Perevoz> getAllPerevoz() throws SQLException {
        List<Perevoz> list = new ArrayList<>();
        String sql = "SELECT * FROM perevoz";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Perevoz(
                        rs.getInt("id"),
                        rs.getInt("auto_id"),
                        rs.getInt("gruz_id"),
                        rs.getInt("sklad_id"),
                        rs.getInt("driver_id"),
                        rs.getDate("date")
                ));
            }
        }
        return list;
    }

    public Perevoz getPerevozById(int id) throws SQLException {
        String sql = "SELECT * FROM perevoz WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Perevoz(
                            rs.getInt("id"),
                            rs.getInt("auto_id"),
                            rs.getInt("gruz_id"),
                            rs.getInt("sklad_id"),
                            rs.getInt("driver_id"),
                            rs.getDate("date")
                    );
                }
            }
        }
        return null;
    }

    // UPDATE
    public void updatePerevoz(Perevoz p) throws SQLException {
        String sql = "UPDATE perevoz SET auto_id=?, gruz_id=?, sklad_id=?, driver_id=?, date=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, p.getAutoId());
            ps.setInt(2, p.getGruzId());
            ps.setInt(3, p.getSkladId());
            ps.setInt(4, p.getDriverId());
            ps.setDate(5, p.getDate());
            ps.setInt(6, p.getId());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void deletePerevoz(int id) throws SQLException {
        String sql = "DELETE FROM perevoz WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
