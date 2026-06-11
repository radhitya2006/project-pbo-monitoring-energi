/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.monitoringenergirumah.Service;

/**
 *
 * @author ASUS
 */
public class PerhitunganBiaya {
    
    private double tarifPerKwh;
    private double persentasePajak;
    
    public PerhitunganBiaya(double tarifPerKwh, double persentasePajak) {
        this.tarifPerKwh = tarifPerKwh;
        this.persentasePajak = persentasePajak;
    }
    
    /** Constructor dengan tarif PLN default dan pajak 5% */
    public PerhitunganBiaya() {
        this(1444.70, 0.05);
    }
    
    public double hitungBiaya(double energiKwh){
        return energiKwh * tarifPerKwh;
    }
    
    
    public double hitungBiayaDenganPajak(double energiKwh) {
        double biayaPokok = hitungBiaya(energiKwh);
        return biayaPokok + (biayaPokok * persentasePajak);
    }
    
    public static String formatRupiah(double biaya) {
        return String.format("Rp %,.2f", biaya);
    }
    
    // =========================================================
    // Getter & Setter
    // =========================================================
 
    public double getTarifPerKwh() {
        return tarifPerKwh;
    }
 
    public void setTarifPerKwh(double tarifPerKwh) {
        if (tarifPerKwh <= 0) {
            System.out.println("Tarif harus bernilai positif.");
            return;
        }
        this.tarifPerKwh = tarifPerKwh;
    }
 
    public double getPersentasePajak() {
        return persentasePajak;
    }
 
    public void setPersentasePajak(double persentasePajak) {
        this.persentasePajak = persentasePajak;
    }

 
    
}
