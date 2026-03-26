package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AutoDAO {

    private Connection connection;

    public AutoDAO(Connection connection) {
        this.connection = connection;
    }

    // ======= CREATE =======
    public void addAuto(Auto auto) throws SQLException {
        String sql = "INSERT INTO auto (model, gos_number) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, auto.getModel());
            ps.setString(2, auto.getGosNumber());
            ps.executeUpdate();
        }
    }

    // ======= READ =======
    public List<Auto> getAllAutos() throws SQLException {
        List<Auto> autos = new ArrayList<>();
        String sql = "SELECT * FROM auto ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Auto auto = new Auto(
                        rs.getInt("id"),
                        rs.getString("model"),
                        rs.getString("gos_number")
                );
                autos.add(auto);
            }
        }
        return autos;
    }

    public Auto getAutoById(int id) throws SQLException {
        String sql = "SELECT * FROM auto WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Auto(
                            rs.getInt("id"),
                            rs.getString("model"),
                            rs.getString("gos_number")
                    );
                }
            }
        }
        return null;
    }

    // ======= UPDATE =======
    public void updateAuto(Auto auto) throws SQLException {
        String sql = "UPDATE auto SET model = ?, gos_number = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, auto.getModel());
            ps.setString(2, auto.getGosNumber());
            ps.setInt(3, auto.getId());
            ps.executeUpdate();
        }
    }

    // ======= DELETE =======
    public void deleteAuto(int id) throws SQLException {
        String sql = "DELETE FROM auto WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
