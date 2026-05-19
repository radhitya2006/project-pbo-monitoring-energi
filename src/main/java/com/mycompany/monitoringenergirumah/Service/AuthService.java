/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.monitoringenergirumah.Service;

import com.mycompany.monitoringenergirumah.Model.User;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class AuthService {
    
    private final List<User> daftarUser = new ArrayList<>();
    private User currentUser = null;
    
    public AuthService() {
        // Akun demo bawaan untuk keperluan testing
        daftarUser.add(new User("Admin Demo", "admin@email.com", "admin123"));
    }
    
     
    
}
