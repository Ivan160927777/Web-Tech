package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDAO {
    private Connection connection;

    public DriverDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE
    public void addDriver(Driver driver) throws SQLException {
        String sql = "INSERT INTO driver (name) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, driver.getName());
            ps.executeUpdate();
        }
    }

    // READ
    public List<Driver> getAllDrivers() throws SQLException {
        List<Driver> list = new ArrayList<>();
        String sql = "SELECT * FROM driver ORDER BY id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Driver(rs.getInt("id"), rs.getString("name")));
            }
        }
        return list;
    }

    public Driver getDriverById(int id) throws SQLException {
        String sql = "SELECT * FROM driver WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Driver(rs.getInt("id"), rs.getString("name"));
                }
            }
        }
        return null;
    }

    // UPDATE
    public void updateDriver(Driver driver) throws SQLException {
        String sql = "UPDATE driver SET name = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, driver.getName());
            ps.setInt(2, driver.getId());
            ps.executeUpdate();
        }
    }

    // DELETE
    public void deleteDriver(int id) throws SQLException {
        String sql = "DELETE FROM driver WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

