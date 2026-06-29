/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.monitoringenergirumah.Model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
/**
 *
 * @author ASUS
 */
public abstract class PerangkatListrik {
    
    public static final String STATUS_AKTIF    = "Aktif";
    public static final String STATUS_NONAKTIF = "Nonaktif";
    public static final String STATUS_RUSAK    = "Rusak";
    
    // 1. Variabel 'nama' kita ubah secara konseptual menjadi 'lokasi'
    private String lokasi; 
    private int daya;
    private double lamaPemakaian;
    
    // Variabel Riwayat & Prediksi
    private LocalDate tanggalPemasangan;
    private String status; 
    private int batasUmurJam; 
    private int id; 
    private Timestamp terakhirUpdate;
    private String jenis;

    public PerangkatListrik(String lokasi, int daya, double lamaPemakaian) {
        this.lokasi = lokasi; // Ini akan diisi "Kamar", "Dapur", dll
        this.daya = daya;
        this.lamaPemakaian = lamaPemakaian;
        
        this.tanggalPemasangan = LocalDate.now(); 
        this.status = "Aktif";                    
        this.batasUmurJam = 15000;                
    }
    
    public LocalDate getEstimasiRusak() {
    if (this.lamaPemakaian <= 0) {
        return null;
    }

    double multiplierLokasi = 1.0;
    String lok = this.lokasi.toLowerCase();

    if (lok.contains("dapur")) {
        multiplierLokasi = 0.8;
    } else if (lok.contains("kamar mandi") || lok.contains("toilet")) {
        multiplierLokasi = 0.7;
    } else if (lok.contains("luar") || lok.contains("teras") || lok.contains("taman")) {
        multiplierLokasi = 0.5;
    }

    long umurEfektifJam = (long) (this.batasUmurJam * multiplierLokasi);
    long sisaHari = (long) (umurEfektifJam / this.lamaPemakaian);

    return this.tanggalPemasangan.plusDays(sisaHari);
}
        
    /**
     *
     * @return
     */
    public int getPersentaseKondisi() {

    long hariDipakai = ChronoUnit.DAYS.between(tanggalPemasangan, LocalDate.now());

    double totalJamTerpakai = hariDipakai * lamaPemakaian;

    double multiplierLokasi = 1.0;
    String lok = lokasi.toLowerCase();

    if (lok.contains("dapur")) {
        multiplierLokasi = 0.8;
    } else if (lok.contains("kamar mandi") || lok.contains("toilet")) {
        multiplierLokasi = 0.7;
    } else if (lok.contains("luar") || lok.contains("teras") || lok.contains("taman")) {
        multiplierLokasi = 0.5;
    }

    double umurEfektif = batasUmurJam * multiplierLokasi;

    double persen = (1 - (totalJamTerpakai / umurEfektif)) * 100;

    if (persen < 0) persen = 0;
    if (persen > 100) persen = 100;

    return (int) Math.round(persen);
}
    
    public String getSisaUmur() {
        
    int persentaseKondisi = getPersentaseKondisi();
        
    if (persentaseKondisi <= 0 || (status != null && status.equalsIgnoreCase("Rusak"))) {
        return "Rusak";
    }

    LocalDate estimasi = getEstimasiRusak();

    if (estimasi == null) {
        return "-";
    }

    if (!estimasi.isAfter(LocalDate.now())) {
        return "Segera Ganti";
    }

    Period p = Period.between(LocalDate.now(), estimasi);

    StringBuilder hasil = new StringBuilder();

    if (p.getYears() > 0) {
        hasil.append(p.getYears()).append(" thn ");
    }

    if (p.getMonths() > 0) {
        hasil.append(p.getMonths()).append(" bln ");
    }

    if (p.getYears() == 0 && p.getMonths() == 0) {
        hasil.append(p.getDays()).append(" hari");
    }

    return hasil.toString().trim();
}
    
    public abstract double hitungEnergi();

    // =========================================================
    // Getter & Setter (Nama method sengaja tetap getNama agar tidak error di file lain)
    // =========================================================

    public String getNama() {
        return lokasi; // Mengembalikan lokasi, tapi tetap pakai getNama() agar sinkron dengan GUI lamamu
    }

    public void setNama(String lokasi) {
        this.lokasi = lokasi;
    }

    public int getDaya() {
        return daya;
    }

    public void setDaya(int daya) {
        if (daya <= 0) {
            System.out.println("Daya harus bernilai positif.");
            return;
        }
        this.daya = daya;
    }

    public double getLamaPemakaian() {
        return lamaPemakaian;
    }

    public void setLamaPemakaian(double lamaPemakaian) {
        if (lamaPemakaian < 0) {
            System.out.println("Lama pemakaian tidak boleh negatif.");
            return;
        }
        this.lamaPemakaian = lamaPemakaian;
    }

    public LocalDate getTanggalPemasangan() {
        return tanggalPemasangan;
    }

    public void setTanggalPemasangan(java.time.LocalDate tanggalPemasangan) {
        this.tanggalPemasangan = tanggalPemasangan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBatasUmurJam() {
        return batasUmurJam;
    }

    public void setBatasUmurJam(int batasUmurJam) {
        this.batasUmurJam = batasUmurJam;
    }
    
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public Timestamp getTerakhirUpdate() {
        return terakhirUpdate;
    }

    public void setTerakhirUpdate(Timestamp terakhirUpdate) {
        this.terakhirUpdate = terakhirUpdate;
    }
    
    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
    

    @Override
    public String toString() {
        return String.format("%-15s | %5d W | %5.1f jam | %6.3f kWh",
                lokasi, daya, lamaPemakaian, hitungEnergi());
    }
}    

