/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.monitoringenergirumah.Service;

import com.mycompany.monitoringenergirumah.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author ASUS
 */
public class AuthService {
    
    private final List<User> daftarUser = new ArrayList<>();
    private User currentUser = null;
    
     private static final int MIN_PANJANG_PASSWORD = 8;
    
    public AuthService() {
        // Akun demo bawaan untuk keperluan testing
        daftarUser.add(new User("Admin Demo", "admin@email.com", "admin123"));
    }
    
    public RegisterResult register(String namaLengkap, String email,String password, String konfirmasiPassword) {
        if (namaLengkap == null || namaLengkap.trim().isEmpty()) {
            return RegisterResult.NAMA_KOSONG;
        }
        if (email == null || email.trim().isEmpty()) {
            return RegisterResult.EMAIL_KOSONG;
        }
        if (!isEmailValid(email)) {
            return RegisterResult.EMAIL_TIDAK_VALID;
        }
        if (emailSudahTerdaftar(email)) {
            return RegisterResult.EMAIL_SUDAH_DIPAKAI;
        }
        if (password == null || password.length() < MIN_PANJANG_PASSWORD) {
            return RegisterResult.PASSWORD_TERLALU_PENDEK;
        }
        if (!password.equals(konfirmasiPassword)) {
            return RegisterResult.PASSWORD_TIDAK_COCOK;
        }
 
        daftarUser.add(new User(
                namaLengkap.trim(),
                email.trim().toLowerCase(),
                password
        ));
        return RegisterResult.SUKSES;
    }
 
    // =========================================================
    // Login
    // =========================================================
 
    /**
     * Memverifikasi email dan password pengguna.
     *
     * @return LoginResult berisi status dan pesan hasil
     */
    public LoginResult login(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return LoginResult.EMAIL_KOSONG;
        }
        if (password == null || password.isEmpty()) {
            return LoginResult.PASSWORD_KOSONG;
        }
        
        Optional<User> user = cariUserByEmail(email);
 
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            currentUser = user.get();
            return LoginResult.SUKSES;
        }
 
        return LoginResult.KREDENSIAL_SALAH;
    }
 
    // =========================================================
    // Logout
    // =========================================================
 
    public void logout() {
        currentUser = null;
    }
 
    // =========================================================
    // Getter
    // =========================================================
 
    /** Mengembalikan user yang sedang login, atau null jika belum login */
    public User getCurrentUser() {
        return currentUser;
    }
 
    public boolean isSudahLogin() {
        return currentUser != null;
    }
 
    public int getJumlahUser() {
        return daftarUser.size();
    }
 
    // =========================================================
    // Utilitas
    // =========================================================
 
    /**
     * Mencari user berdasarkan email.
     * Menggunakan Optional agar tidak perlu cek null manual.
     */
    public Optional<User> cariUserByEmail(String email) {
        return daftarUser.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst();
    }
 
    private boolean emailSudahTerdaftar(String email) {
        return cariUserByEmail(email).isPresent();
    }
 
    private boolean isEmailValid(String email) {
        String e = email.trim();
        return e.contains("@") && e.contains(".") && e.indexOf("@") < e.lastIndexOf(".");
    }
 
    public static int getMinPanjangPassword() {
        return MIN_PANJANG_PASSWORD;
    }
 
    // =========================================================
    // Enum Hasil Login
    // =========================================================
 
    public enum LoginResult {
        SUKSES,
        EMAIL_KOSONG,
        PASSWORD_KOSONG,
        KREDENSIAL_SALAH;
 
        public String pesan() {
            return switch (this) {
                case SUKSES           -> "Login berhasil.";
                case EMAIL_KOSONG     -> "Email tidak boleh kosong.";
                case PASSWORD_KOSONG  -> "Password tidak boleh kosong.";
                case KREDENSIAL_SALAH -> "Email atau password salah.";
            };
        }
    }
 
    // =========================================================
    // Enum Hasil Register
    // =========================================================
 
    public enum RegisterResult {
        SUKSES,
        NAMA_KOSONG,
        EMAIL_KOSONG,
        EMAIL_TIDAK_VALID,
        EMAIL_SUDAH_DIPAKAI,
        PASSWORD_TERLALU_PENDEK,
        PASSWORD_TIDAK_COCOK;
 
        public String pesan() {
            return switch (this) {
                case SUKSES                  -> "Registrasi berhasil. Silakan login.";
                case NAMA_KOSONG             -> "Nama lengkap tidak boleh kosong.";
                case EMAIL_KOSONG            -> "Email tidak boleh kosong.";
                case EMAIL_TIDAK_VALID       -> "Format email tidak valid.";
                case EMAIL_SUDAH_DIPAKAI     -> "Email sudah terdaftar.";
                case PASSWORD_TERLALU_PENDEK -> "Password minimal " + MIN_PANJANG_PASSWORD + " karakter.";
                case PASSWORD_TIDAK_COCOK    -> "Konfirmasi password tidak cocok.";
            };
        }
    }
     
    
}
