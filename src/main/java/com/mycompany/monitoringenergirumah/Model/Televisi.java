/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.monitoringenergirumah.Model;

/**
 *
 * @author ASUS
 */
public class Televisi extends PerangkatListrik{
    
    // Daya standby dalam Watt (TV modern umumnya 0.5 – 3 W)
    private int dayaStandby;
 
    // Persentase waktu dalam mode standby (0.0 – 1.0)
    private double persentaseStandby;
 
    public Televisi(String nama, int daya, double lamaPemakaian, int dayaStandby, double persentaseStandby) {
        super(nama, daya, lamaPemakaian);
        this.dayaStandby = dayaStandby;
        this.persentaseStandby = persentaseStandby;
    }
    
    /** Constructor dengan nilai standby default */
    public Televisi(String nama, int daya, double lamaPemakaian) {
        this(nama, daya, lamaPemakaian, 2, 0.1);
    }
    
    @Override
    public double hitungEnergi() {
        double waktuAktif   = getLamaPemakaian() * (1 - persentaseStandby);
        double waktuStandby = getLamaPemakaian() * persentaseStandby;
        return (getDaya() * waktuAktif + dayaStandby * waktuStandby) / 1000.0;
    }
 
    public int getDayaStandby() {
        return dayaStandby;
    }
 
    public void setDayaStandby(int dayaStandby) {
        this.dayaStandby = dayaStandby;
    }
 
    public double getPersentaseStandby() {
        return persentaseStandby;
    }
 
    public void setPersentaseStandby(double persentaseStandby) {
        if (persentaseStandby < 0 || persentaseStandby > 1) {
            System.out.println("Persentase standby harus antara 0.0 dan 1.0.");
            return;
        }
        this.persentaseStandby = persentaseStandby;
    }
 
    @Override
    public String toString() {
        return "[Televisi ] " + super.toString();
    }
}
