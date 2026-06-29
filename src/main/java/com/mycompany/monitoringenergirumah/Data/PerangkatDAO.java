package com.mycompany.monitoringenergirumah.Data;

import com.mycompany.monitoringenergirumah.Model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerangkatDAO {

    // ── Tambah perangkat baru ──
    public boolean tambahPerangkat(PerangkatListrik p, int idUser) {
        System.out.println("Method tambahPerangkat dipanggil");
        String sql = "INSERT INTO perangkat "
           + "(id_user, lokasi_pemasangan, jenis, daya_watt, lama_pemakaian, "
           + "status, cop, daya_standby, persentase_standby, terakhir_update, tanggal_pemasangan) "
           + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?)";
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            ps.setString(2, p.getNama());           // lokasi
            ps.setString(3, getJenisString(p));
            ps.setInt(4, p.getDaya());
            ps.setDouble(5, p.getLamaPemakaian());
            ps.setString(6, p.getStatus());
            ps.setDate(10, java.sql.Date.valueOf(p.getTanggalPemasangan()));

            // Field khusus per jenis
            if (p instanceof AC) {
                ps.setDouble(7, ((AC) p).getCop());
                ps.setNull(8, Types.INTEGER);
                ps.setNull(9, Types.DECIMAL);
            } else if (p instanceof Televisi) {
                ps.setNull(7, Types.DECIMAL);
                ps.setInt(8, ((Televisi) p).getDayaStandby());
                ps.setDouble(9, ((Televisi) p).getPersentaseStandby());
            } else {
                // Lampu / Lainnya
                ps.setNull(7, Types.DECIMAL);
                ps.setNull(8, Types.INTEGER);
                ps.setNull(9, Types.DECIMAL);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── Ambil semua perangkat milik user ──
    public List<PerangkatListrik> getPerangkatByUser(int idUser) {
        List<PerangkatListrik> list = new ArrayList<>();
        String sql = "SELECT * FROM perangkat WHERE id_user = ? "
                   + "ORDER BY terakhir_update DESC";

        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PerangkatListrik p = buatObjekDariRS(rs);
                if (p != null) list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ── Update status saja ──
    public boolean updateStatus(int idPerangkat, String status) {
        String sql =
        "UPDATE perangkat SET status=?, terakhir_update=NOW() "
        + "WHERE id_perangkat=?";
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, idPerangkat);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── Update seluruh data perangkat ──
    public boolean updatePerangkat(int idPerangkat, PerangkatListrik p) {
        String sql =
        "UPDATE perangkat SET lokasi_pemasangan=?, jenis=?, daya_watt=?, "
        + "lama_pemakaian=?, status=?, cop=?, daya_standby=?, "
        + "persentase_standby=?, tanggal_pemasangan=?, terakhir_update=NOW() "
        + "WHERE id_perangkat=?";
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNama());
            ps.setString(2, getJenisString(p));
            ps.setInt(3, p.getDaya());
            ps.setDouble(4, p.getLamaPemakaian());
            ps.setString(5, p.getStatus());

            if (p instanceof AC) {
                ps.setDouble(6, ((AC) p).getCop());
                ps.setNull(7, Types.INTEGER);
                ps.setNull(8, Types.DECIMAL);
            } else if (p instanceof Televisi) {
                ps.setNull(6, Types.DECIMAL);
                ps.setInt(7, ((Televisi) p).getDayaStandby());
                ps.setDouble(8, ((Televisi) p).getPersentaseStandby());
            } else {
                ps.setNull(6, Types.DECIMAL);
                ps.setNull(7, Types.INTEGER);
                ps.setNull(8, Types.DECIMAL);
            }

            ps.setDate(9, Date.valueOf(p.getTanggalPemasangan()));
            ps.setInt(10, idPerangkat);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── Hapus perangkat ──
    public boolean hapusPerangkat(int idPerangkat) {
        String sql = "DELETE FROM perangkat WHERE id_perangkat = ?";
        try (Connection conn = KoneksiDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPerangkat);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    

    // ── Helper: ResultSet → Object yang tepat ──
    private PerangkatListrik buatObjekDariRS(ResultSet rs) throws SQLException {
        String jenis         = rs.getString("jenis");
        String lokasi        = rs.getString("lokasi_pemasangan");
        int daya             = rs.getInt("daya_watt");
        double lamaPemakaian = rs.getDouble("lama_pemakaian");
        String status        = rs.getString("status");
        int id               = rs.getInt("id_perangkat");

        PerangkatListrik p;
        switch (jenis) {
            case "AC":
                double cop = rs.getDouble("cop");
                p = new AC(lokasi, daya, lamaPemakaian, cop);
                break;
            case "Televisi":
                int dayaStandby         = rs.getInt("daya_standby");
                double persentaseStandby = rs.getDouble("persentase_standby");
                p = new Televisi(lokasi, daya, lamaPemakaian, dayaStandby, persentaseStandby);
                break;
            case "Lampu":
                p = new Lampu(lokasi, daya, lamaPemakaian);
                break;
            default:
                return null;
        }

        p.setId(id);
        p.setStatus(status);
        p.setJenis(jenis);
        p.setTerakhirUpdate(rs.getTimestamp("terakhir_update"));
        Date tanggal = rs.getDate("tanggal_pemasangan");

        if (tanggal != null) {
            p.setTanggalPemasangan(tanggal.toLocalDate());
        }
        return p;
    }

    // ── Helper: ambil nama jenis dari tipe object ──
    public static String getJenisString(PerangkatListrik p) {
        if (p instanceof Lampu)    return "Lampu";
        if (p instanceof AC)       return "AC";
        if (p instanceof Televisi) return "Televisi";
        return "Lainnya";
    }
    
    public List<PerangkatListrik> getPerangkatAktifByUser(int idUser) {
    List<PerangkatListrik> list = new ArrayList<>();
    String sql = "SELECT * FROM perangkat "
               + "WHERE id_user = ? AND status = 'Aktif'";
    try (Connection conn = KoneksiDB.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, idUser);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            PerangkatListrik p = buatObjekDariRS(rs);
            if (p != null) list.add(p);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
    
}