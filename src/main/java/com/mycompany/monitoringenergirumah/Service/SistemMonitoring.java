/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.monitoringenergirumah.Service;

import com.mycompany.monitoringenergirumah.Data.RiwayatPenggunaan;
import com.mycompany.monitoringenergirumah.Model.PerangkatListrik;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class SistemMonitoring {
 
    private final List<PerangkatListrik> perangkatList;
    private final PerhitunganBiaya kalkulator;
    private final List<RiwayatPenggunaan> riwayatList;
 
    public SistemMonitoring(PerhitunganBiaya kalkulator) {
        this.perangkatList = new ArrayList<>();
        this.riwayatList   = new ArrayList<>();
        this.kalkulator    = kalkulator;
    }
 
    public SistemMonitoring() {
        this(new PerhitunganBiaya());
    }
    
    /**
     * Menambahkan perangkat dan langsung menyimpan riwayatnya.
     */
    public void tambahPerangkat(PerangkatListrik perangkat) {
        perangkatList.add(perangkat);
 
        // Catat riwayat secara otomatis saat perangkat ditambahkan
        double energi = perangkat.hitungEnergi();
        double biaya  = kalkulator.hitungBiayaDenganPajak(energi);
        riwayatList.add(new RiwayatPenggunaan(perangkat.getNama(), energi, biaya));
 
 
    }
 
    public void hapusPerangkat(String nama) {
        boolean dihapus = perangkatList.removeIf(p -> p.getNama().equalsIgnoreCase(nama));
        if (dihapus) {
            System.out.println("  Perangkat \"" + nama + "\" berhasil dihapus.");
        } else {
            System.out.println("  Perangkat \"" + nama + "\" tidak ditemukan.");
        }
    }
    
    /** Menghitung total konsumsi energi seluruh perangkat dalam kWh */
    public double totalEnergi() {
        return perangkatList.stream()
                .mapToDouble(PerangkatListrik::hitungEnergi)
                .sum();
    }
 
    /** Menghitung total estimasi biaya listrik termasuk pajak */
    public double hitungTotalBiaya() {
        return kalkulator.hitungBiayaDenganPajak(totalEnergi());
    }
 
    // =========================================================
    // Tampilan
    // =========================================================
 
    /** Menampilkan semua perangkat beserta konsumsi energinya */
    public void tampilkanDaftarPerangkat() {
        if (perangkatList.isEmpty()) {
            System.out.println("  Belum ada perangkat yang terdaftar.");
            return;
        }
        System.out.println();
        System.out.println("  " + "=".repeat(67));
        System.out.printf("  %-24s | %7s | %9s | %10s%n",
                "Perangkat", "Daya", "Waktu", "Energi");
        System.out.println("  " + "-".repeat(67));
        for (PerangkatListrik p : perangkatList) {
            System.out.println("  " + p);
        }
        System.out.println("  " + "-".repeat(67));
        System.out.printf("  %-24s   %7s   %9s   %6.3f kWh%n",
                "TOTAL", "", "", totalEnergi());
        System.out.printf("  %-24s   %7s   %9s   %s%n",
                "ESTIMASI BIAYA (+ PPJ)", "", "",
                PerhitunganBiaya.formatRupiah(hitungTotalBiaya()));
        System.out.println("  " + "=".repeat(67));
        System.out.println();
    }
 
    /** Menampilkan riwayat penggunaan seluruh perangkat */
    public void tampilkanRiwayat() {
        if (riwayatList.isEmpty()) {
            System.out.println("  Belum ada riwayat penggunaan.");
            return;
        }
        System.out.println();
        System.out.println("  " + "=".repeat(67));
        System.out.printf("  %-17s | %-15s | %10s | %s%n",
                "Tanggal & Waktu", "Perangkat", "Energi", "Biaya");
        System.out.println("  " + "-".repeat(67));
        for (RiwayatPenggunaan r : riwayatList) {
            System.out.println("  " + r);
        }
        System.out.println("  " + "=".repeat(67));
        System.out.println();
    }
 
    // =========================================================
    // Getter
    // =========================================================
 
    public List<PerangkatListrik> getPerangkatList() {
        return perangkatList;
    }
 
    public List<RiwayatPenggunaan> getRiwayatList() {
        return riwayatList;
    }
 
    public PerhitunganBiaya getKalkulator() {
        return kalkulator;
    }
}
