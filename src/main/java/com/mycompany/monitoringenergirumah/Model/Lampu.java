/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.monitoringenergirumah.Model;

/**
 *
 * @author ASUS
 */
public class Lampu extends PerangkatListrik {
    
    public Lampu(String nama, int daya, double lamaPemakaian) {
        super(nama, daya, lamaPemakaian);
    }
    
    @Override
    public double hitungEnergi() {
        return (getDaya() * getLamaPemakaian()) / 1000.0;
    }
    
    @Override
    public String toString(){
        return "[Lampu  ]" + super.toString();
    }

    
    
}
