/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.monitoringenergirumah.Model;

/**
 *
 * @author ASUS
 */
public abstract class PerangkatListrik {
    private String nama;
    private int daya;
    private double lamaPemakaian;
    
    public PerangkatListrik(String nama, int daya, double lamaPemakaian) {
        this.nama = nama;
        this.daya = daya;
        this.lamaPemakaian = lamaPemakaian;
    }
    
    // =========================================================
    // Abstract Method — wajib di-override oleh setiap subclass
    // =========================================================
 
    /**
     * Menghitung konsumsi energi dalam satuan kWh.
     * Setiap perangkat dapat memiliki formula perhitungan yang berbeda (Polymorphism).
     *
     * @return energi dalam kWh
     */
    public abstract double hitungEnergi();
 
    // =========================================================
    // Getter & Setter
    // =========================================================
 
    public String getNama() {
        return nama;
    }
 
    public void setNama(String nama) {
        this.nama = nama;
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
 
    // =========================================================
    // toString
    // =========================================================
 
    @Override
    public String toString() {
        return String.format("%-15s | %5d W | %5.1f jam | %6.3f kWh",
                nama, daya, lamaPemakaian, hitungEnergi());
    }
    
}
