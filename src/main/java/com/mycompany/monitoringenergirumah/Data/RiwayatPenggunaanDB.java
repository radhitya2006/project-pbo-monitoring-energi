package com.mycompany.monitoringenergirumah.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RiwayatPenggunaanDB {

    private int idRiwayat;
    private int idPerangkat;
    private int idUser;
    private LocalDate tanggal;
    private double jamPenggunaan;
    private double konsumsiKwh;
    private double biaya;
    private String namaPerangkat; // hasil JOIN dari tabel perangkat
    private String jenisPerangkat;

    // Constructor untuk load dari DB
    public RiwayatPenggunaanDB(int idRiwayat, int idPerangkat, int idUser,
            LocalDate tanggal, double jamPenggunaan,
            double konsumsiKwh, double biaya,
            String namaPerangkat, String jenisPerangkat) {
        this.idRiwayat      = idRiwayat;
        this.idPerangkat    = idPerangkat;
        this.idUser         = idUser;
        this.tanggal        = tanggal;
        this.jamPenggunaan  = jamPenggunaan;
        this.konsumsiKwh    = konsumsiKwh;
        this.biaya          = biaya;
        this.namaPerangkat  = namaPerangkat;
        this.jenisPerangkat = jenisPerangkat;
    }

    // Getters
    public int getId()              { return idRiwayat; }
    public int getIdPerangkat()     { return idPerangkat; }
    public int getIdUser()          { return idUser; }
    public LocalDate getTanggal()   { return tanggal; }
    public double getJamPenggunaan(){ return jamPenggunaan; }
    public double getKonsumsiKwh()  { return konsumsiKwh; }
    public double getBiaya()        { return biaya; }
    public String getNamaPerangkat(){ return namaPerangkat; }
    public String getJenisPerangkat(){ return jenisPerangkat; }

    // Format untuk tabel UI
    public String getTanggalFormatted() {
        return tanggal.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public String getBiayaFormatted() {
        return String.format("Rp %,.0f", biaya);
    }

    public String getKonsumsiFormatted() {
        return String.format("%.4f kWh", konsumsiKwh);
    }
}