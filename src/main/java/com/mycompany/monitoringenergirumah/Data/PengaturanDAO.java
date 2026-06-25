package com.mycompany.monitoringenergirumah.Data;

import com.mycompany.monitoringenergirumah.Service.PerhitunganBiaya;
import java.sql.*;

public class PengaturanDAO {

    // Ambil PerhitunganBiaya sesuai setting user
    public PerhitunganBiaya getPerhitunganBiaya(int idUser) {
        String sql = "SELECT tarif_per_kwh, persentase_pajak "
                   + "FROM pengaturan WHERE id_user = ?";
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new PerhitunganBiaya(
                    rs.getDouble("tarif_per_kwh"),
                    rs.getDouble("persentase_pajak")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new PerhitunganBiaya(); // default jika belum ada setting
    }

    // Simpan/update tarif user
    public boolean simpanPengaturan(int idUser, double tarif, double pajak) {
        String sql = "INSERT INTO pengaturan (id_user, tarif_per_kwh, persentase_pajak) "
                   + "VALUES (?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE "
                   + "tarif_per_kwh = ?, persentase_pajak = ?";
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            ps.setDouble(2, tarif);
            ps.setDouble(3, pajak);
            ps.setDouble(4, tarif);
            ps.setDouble(5, pajak);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}