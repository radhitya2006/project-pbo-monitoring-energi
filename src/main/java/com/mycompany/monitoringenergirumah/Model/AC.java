/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.monitoringenergirumah.Model;

/**
 *
 * @author ASUS
 */
public class AC extends PerangkatListrik{
    
    private double cop;
    
    public AC(String nama, int daya, double lamaPemakaian, double cop) {
        super(nama, daya, lamaPemakaian);
        this.cop = cop;
    }
    
    public AC(String nama, inr daya, double lamaPemakaian){
        this(nama, daya, lamaPemakaian, 3.0);
    }
    
    @Override
    public double hitungEnergi() {
        return(getDaya() / cop* getLamaPemakaian()) / 1000.0;
    }
    
    public double getCop(){
        return cop;
    }
    
    public void setCop(double cop) {
        if (cop <= 0) {
            System.out.println("COP harus bernilai positif.");
            return;
        }
        this.cop = cop;
    }
 
    @Override
    public String toString() {
        return "[AC       ] " + super.toString();
    }
}
