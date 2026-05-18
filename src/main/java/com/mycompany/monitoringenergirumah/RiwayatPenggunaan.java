/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.monitoringenergirumah;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author ASUS
 */
public class RiwayatPenggunaan {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
    
           
    private final String tanggal;
    private final String namaPerangkat;
    private final double energiTerpakai;
    private final double biaya;
    
    public RiwayatPenggunaan(String namaPerangkat, double energiTerpakai, double biaya) {
        this.tanggal = LocalDateTime.now().format(FORMATTER);
        this.namaPerangkat = namaPerangkat;
        this.energiTerpakai = energiTerpakai;
        this.biaya = biaya;
    }
    
    public String getTanggal() {
        return tanggal;
    }
 
    public String getNamaPerangkat() {
        return namaPerangkat;
    }
 
    public double getEnergiTerpakai() {
        return energiTerpakai;
    }
 
    public double getBiaya() {
        return biaya;
    }
    
    @Override
    public String toString() {
        return String.format("%-17s | %-15s | %6.3f kWh | %s",
                tanggal,
                namaPerangkat,
                energiTerpakai,
                String.format("Rp %,.2f", biaya));
    }

}
