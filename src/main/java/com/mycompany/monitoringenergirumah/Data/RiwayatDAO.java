package com.mycompany.monitoringenergirumah.Data;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class RiwayatDAO {

    // ─────────────────────────────────────────────
    // CREATE — Simpan satu sesi penggunaan
    // ─────────────────────────────────────────────
    public boolean simpanRiwayat(int idPerangkat, int idUser,
            LocalDate tanggal, double jamPenggunaan,
            double konsumsiKwh, double biaya) {

        String sql = "INSERT INTO riwayat_penggunaan "
                   + "(id_perangkat, id_user, tanggal, jam_penggunaan, "
                   + "konsumsi_kwh, biaya) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPerangkat);
            ps.setInt(2, idUser);
            ps.setDate(3, java.sql.Date.valueOf(tanggal));
            ps.setDouble(4, jamPenggunaan);
            ps.setDouble(5, konsumsiKwh);
            ps.setDouble(6, biaya);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean sudahAdaHariIni(int idPerangkat, int idUser) {
        String sql = "SELECT COUNT(*) FROM riwayat_penggunaan "
               + "WHERE id_perangkat = ? AND id_user = ? "
               + "AND tanggal = CURDATE()";
        try (Connection conn = KoneksiDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPerangkat);
            ps.setInt(2, idUser);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
    }

    // ─────────────────────────────────────────────
    // READ — Riwayat N hari terakhir (untuk line chart)
    // Key: tanggal (String "yyyy-MM-dd"), Value: total kWh
    // ─────────────────────────────────────────────
    public Map<String, Double> getKonsumsiHarian(int idUser, int jumlahHari) {
        Map<String, Double> hasil = new LinkedHashMap<>();
        String sql = "SELECT tanggal, SUM(konsumsi_kwh) AS total "
                   + "FROM riwayat_penggunaan "
                   + "WHERE id_user = ? "
                   + "AND tanggal >= CURDATE() - INTERVAL ? DAY "
                   + "GROUP BY tanggal "
                   + "ORDER BY tanggal ASC";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            ps.setInt(2, jumlahHari - 1); // -1 agar hari ini termasuk
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                hasil.put(rs.getString("tanggal"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hasil;
    }

    // ─────────────────────────────────────────────
    // READ — Distribusi per jenis (untuk donut chart)
    // Key: "Lampu"/"AC"/"Televisi", Value: total kWh
    // ─────────────────────────────────────────────
    public Map<String, Double> getDistribusiByJenis(int idUser) {
        Map<String, Double> hasil = new LinkedHashMap<>();
        String sql = "SELECT p.jenis, SUM(r.konsumsi_kwh) AS total "
                   + "FROM riwayat_penggunaan r "
                   + "JOIN perangkat p ON r.id_perangkat = p.id_perangkat "
                   + "WHERE r.id_user = ? "
                   + "GROUP BY p.jenis "
                   + "ORDER BY total DESC";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                hasil.put(rs.getString("jenis"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hasil;
    }

    // ─────────────────────────────────────────────
    // READ — Statistik untuk 4 kartu Dashboard
    // ─────────────────────────────────────────────
    public double getTotalKonsumsiHariIni(int idUser) {
        String sql = "SELECT COALESCE(SUM(konsumsi_kwh), 0) "
                   + "FROM riwayat_penggunaan "
                   + "WHERE id_user = ? AND tanggal = CURDATE()";
        return queryAngka(sql, idUser);
    }

    public double getTotalKonsumsiUserBulanIni(int idUser) {
        String sql = "SELECT COALESCE(SUM(konsumsi_kwh), 0) "
                   + "FROM riwayat_penggunaan "
                   + "WHERE id_user = ? "
                   + "AND MONTH(tanggal) = MONTH(CURDATE()) "
                   + "AND YEAR(tanggal)  = YEAR(CURDATE())";
        return queryAngka(sql, idUser);
    }

    public double getRataRataHarian(int idUser) {
        String sql = "SELECT COALESCE(AVG(daily_total), 0) FROM ("
                   + "  SELECT SUM(konsumsi_kwh) AS daily_total "
                   + "  FROM riwayat_penggunaan "
                   + "  WHERE id_user = ? "
                   + "  AND tanggal >= CURDATE() - INTERVAL 29 DAY "
                   + "  GROUP BY tanggal"
                   + ") AS sub";
        return queryAngka(sql, idUser);
    }

    public double getTotalBiayaBulanIni(int idUser) {
        String sql = "SELECT COALESCE(SUM(biaya), 0) "
                   + "FROM riwayat_penggunaan "
                   + "WHERE id_user = ? "
                   + "AND MONTH(tanggal) = MONTH(CURDATE()) "
                   + "AND YEAR(tanggal)  = YEAR(CURDATE())";
        return queryAngka(sql, idUser);
    }

    // ─────────────────────────────────────────────
    // READ — Riwayat terbaru (untuk tabel di Dashboard)
    // ─────────────────────────────────────────────
    public List<RiwayatPenggunaanDB> getRiwayatTerbaru(int idUser, int limit) {
        List<RiwayatPenggunaanDB> list = new ArrayList<>();
        String sql = "SELECT r.id_riwayat, r.id_perangkat, r.id_user, "
                   + "r.tanggal, r.jam_penggunaan, r.konsumsi_kwh, r.biaya, "
                   + "p.lokasi_pemasangan, p.jenis "
                   + "FROM riwayat_penggunaan r "
                   + "JOIN perangkat p ON r.id_perangkat = p.id_perangkat "
                   + "WHERE r.id_user = ? "
                   + "ORDER BY r.created_at DESC "
                   + "LIMIT ?";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new RiwayatPenggunaanDB(
                    rs.getInt("id_riwayat"),
                    rs.getInt("id_perangkat"),
                    rs.getInt("id_user"),
                    rs.getDate("tanggal").toLocalDate(),
                    rs.getDouble("jam_penggunaan"),
                    rs.getDouble("konsumsi_kwh"),
                    rs.getDouble("biaya"),
                    rs.getString("lokasi_pemasangan"),
                    rs.getString("jenis")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ─────────────────────────────────────────────
    // READ — Semua riwayat (untuk menu Data Penggunaan)
    // ─────────────────────────────────────────────
    public List<RiwayatPenggunaanDB> getAllRiwayat(int idUser) {
        return getRiwayatTerbaru(idUser, Integer.MAX_VALUE);
    }

    // ─────────────────────────────────────────────
    // READ — Filter by rentang tanggal (untuk Laporan)
    // ─────────────────────────────────────────────
    public List<RiwayatPenggunaanDB> getRiwayatByTanggal(int idUser,
            LocalDate dari, LocalDate sampai) {

        List<RiwayatPenggunaanDB> list = new ArrayList<>();
        String sql = "SELECT r.id_riwayat, r.id_perangkat, r.id_user, "
                   + "r.tanggal, r.jam_penggunaan, r.konsumsi_kwh, r.biaya, "
                   + "p.lokasi_pemasangan, p.jenis "
                   + "FROM riwayat_penggunaan r "
                   + "JOIN perangkat p ON r.id_perangkat = p.id_perangkat "
                   + "WHERE r.id_user = ? "
                   + "AND r.tanggal BETWEEN ? AND ? "
                   + "ORDER BY r.tanggal ASC";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            ps.setDate(2, java.sql.Date.valueOf(dari));
            ps.setDate(3, java.sql.Date.valueOf(sampai));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new RiwayatPenggunaanDB(
                    rs.getInt("id_riwayat"),
                    rs.getInt("id_perangkat"),
                    rs.getInt("id_user"),
                    rs.getDate("tanggal").toLocalDate(),
                    rs.getDouble("jam_penggunaan"),
                    rs.getDouble("konsumsi_kwh"),
                    rs.getDouble("biaya"),
                    rs.getString("lokasi_pemasangan"),
                    rs.getString("jenis")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ─────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────
    public boolean hapusRiwayat(int idRiwayat) {
        String sql = "DELETE FROM riwayat_penggunaan WHERE id_riwayat = ?";
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRiwayat);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ─────────────────────────────────────────────
    // Helper query angka tunggal
    // ─────────────────────────────────────────────
    private double queryAngka(String sql, int idUser) {
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}