package com.mycompany.monitoringenergirumah;

// 1. Import library FlatLaf
import com.formdev.flatlaf.FlatLightLaf;
import GUI.SignIn; // Pastikan ini mengarah ke file Perangkat.java milikmu
import com.mycompany.monitoringenergirumah.Service.AuthService;

public class MonitoringEnergiRumah {
    public static void main(String[] args) {
        com.formdev.flatlaf.FlatLightLaf.setup();
        
        // Buat SATU AuthService saja untuk seluruh aplikasi
        AuthService authService = new AuthService();
        
        java.awt.EventQueue.invokeLater(() -> {
            // Oper authService yang SAMA ke SignIn
            new SignIn(authService).setVisible(true);
        });
    }
}