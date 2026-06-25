/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.monitoringenergirumah.Model;

/**
 *
 * @author ASUS
 */
public class User {
    private static int idCounter = 1;
    
    private final int id;
    private String namaLengkap;
    private String email;
    private String password;
    
    public User(int id, String namaLengkap, String email, String password) {
        this.id          = id;
        this.namaLengkap = namaLengkap;
        this.email       = email;
        this.password    = password;
    }
    
    public User(String namaLengkap, String email, String password) {
        this.id = idCounter++;
        this.namaLengkap = namaLengkap;
        this.email = email;
        this.password = password;
    }
    
    public int getId() {
        return id;
    }
    
    public String getNamaLengkap() {
        return namaLengkap;
    }
    
    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }
    
    public String getEmail(){
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
     @Override
    public String toString() {
        return "User{id=" + id + ", nama='" + namaLengkap + "', email='" + email + "'}";
    }
}
