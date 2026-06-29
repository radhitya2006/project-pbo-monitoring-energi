package com.mycompany.monitoringenergirumah.Data;

import com.mycompany.monitoringenergirumah.Model.User;
import java.sql.*;
import java.util.Optional;

public class UserDAO {

    public Optional<User> findByEmail(String email) {
    String sql = "SELECT * FROM `user` WHERE email = ?";
    try (Connection conn = KoneksiDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, email.trim().toLowerCase());
        
        // Tambah ini sementara
        System.out.println("Mencari email: " + email.trim().toLowerCase());
        
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            System.out.println("User ditemukan: " + rs.getString("nama_lengkap"));
            return Optional.of(new User(
                rs.getInt("id_user"),
                rs.getString("nama_lengkap"),
                rs.getString("email"),
                rs.getString("password")
            ));
        } else {
            System.out.println("User TIDAK ditemukan di DB");
        }
    } catch (SQLException e) {
        System.out.println("findByEmail error: " + e.getMessage());
    }
    return Optional.empty();
}

    public boolean insert(User user) {
        String sql = "INSERT INTO user (nama_lengkap, email, password) VALUES (?, ?, ?)";
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getNamaLengkap());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("insert error: " + e.getMessage());
            return false;
        }
    }

    public boolean update(User user) {

    String sql = "UPDATE user SET nama_lengkap = ?, email = ?, password = ? WHERE id_user = ?";

    try (Connection conn = KoneksiDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, user.getNamaLengkap());
        stmt.setString(2, user.getEmail());
        stmt.setString(3, user.getPassword());
        stmt.setInt(4, user.getId());   // <-- gunakan getId()

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        System.out.println("update error: " + e.getMessage());
        return false;
    }
}
    
    public int count() {
        String sql = "SELECT COUNT(*) FROM user";
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            System.out.println("count error: " + e.getMessage());
        }
        return 0;
    }
}