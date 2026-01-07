package com.example.bookshelf;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationLogic {
    private static final String URL = "jdbc:sqlite:app_db.sqlite";

    public ApplicationLogic() {
        try {
            initDatabase();
        } catch (SQLException e) {
            throw new RuntimeException("DB init failed", e);
        }
    }

    private void initDatabase() throws SQLException {
        String createSql = "CREATE TABLE IF NOT EXISTS items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL)";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createSql);
        }
    }

    public void addItem(String name) throws SQLException {
        String insertSql = "INSERT INTO items (name) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setString(1, name);
            ps.executeUpdate();
        }
    }

    public List<String> getItems() throws SQLException {
        String selectSql = "SELECT id, name FROM items ORDER BY id";
        List<String> items = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSql)) {
            while (rs.next()) {
                items.add(rs.getInt("id") + " - " + rs.getString("name"));
            }
        }
        return items;
    }

    // ✅ CORRECT DELETE METHOD (delete by id)
    public boolean deleteById(int id) throws SQLException {
        String deleteSql = "DELETE FROM items WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement ps = conn.prepareStatement(deleteSql)) {
            ps.setInt(1, id);
            int affected = ps.executeUpdate();
            return affected > 0;  // true if deletion happened
        }
    }
}
