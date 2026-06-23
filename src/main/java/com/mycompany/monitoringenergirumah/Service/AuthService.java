package com.mycompany.monitoringenergirumah.Service;

import com.mycompany.monitoringenergirumah.Data.UserDAO;
import com.mycompany.monitoringenergirumah.Model.User;
import java.util.Optional;

public class AuthService {

    // ↓ ArrayList diganti dengan UserDAO
    private final UserDAO userDAO = new UserDAO();
    private User currentUser = null;

    private static final int MIN_PANJANG_PASSWORD = 8;

    public AuthService() {
        // Constructor kosong — akun demo bisa ditambah
        // langsung via SQL: INSERT INTO user VALUES (...)
    }

    // =========================================================
    // Register
    // =========================================================

    public RegisterResult register(String namaLengkap, String email,
                                   String password, String konfirmasiPassword) {
        if (namaLengkap == null || namaLengkap.trim().isEmpty())
            return RegisterResult.NAMA_KOSONG;
        if (email == null || email.trim().isEmpty())
            return RegisterResult.EMAIL_KOSONG;
        if (!isEmailValid(email))
            return RegisterResult.EMAIL_TIDAK_VALID;
        if (emailSudahTerdaftar(email))           // ← sekarang cek ke DB
            return RegisterResult.EMAIL_SUDAH_DIPAKAI;
        if (password == null || password.length() < MIN_PANJANG_PASSWORD)
            return RegisterResult.PASSWORD_TERLALU_PENDEK;
        if (!password.equals(konfirmasiPassword))
            return RegisterResult.PASSWORD_TIDAK_COCOK;

        // ↓ Simpan ke DB, bukan ArrayList
        User user = new User(
            namaLengkap.trim(),
            email.trim().toLowerCase(),
            password
        );
        boolean berhasil = userDAO.insert(user);
        return berhasil ? RegisterResult.SUKSES : RegisterResult.EMAIL_SUDAH_DIPAKAI;
    }

    // =========================================================
    // Login
    // =========================================================

    public LoginResult login(String email, String password) {
        if (email == null || email.trim().isEmpty())
            return LoginResult.EMAIL_KOSONG;
        if (password == null || password.isEmpty())
            return LoginResult.PASSWORD_KOSONG;

        Optional<User> user = cariUserByEmail(email); // ← sekarang query DB

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

    public User getCurrentUser()  { return currentUser; }
    public boolean isSudahLogin() { return currentUser != null; }

    public int getJumlahUser() {
        return userDAO.count(); // ← sekarang COUNT(*) dari DB
    }

    // =========================================================
    // Utilitas
    // =========================================================

    public Optional<User> cariUserByEmail(String email) {
        return userDAO.findByEmail(email); // ← sekarang query DB
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
    // Enum — tidak ada perubahan sama sekali
    // =========================================================

    public enum LoginResult {
        SUKSES, EMAIL_KOSONG, PASSWORD_KOSONG, KREDENSIAL_SALAH;

        public String pesan() {
            return switch (this) {
                case SUKSES           -> "Login berhasil.";
                case EMAIL_KOSONG     -> "Email tidak boleh kosong.";
                case PASSWORD_KOSONG  -> "Password tidak boleh kosong.";
                case KREDENSIAL_SALAH -> "Email atau password salah.";
            };
        }
    }

    public enum RegisterResult {
        SUKSES, NAMA_KOSONG, EMAIL_KOSONG, EMAIL_TIDAK_VALID,
        EMAIL_SUDAH_DIPAKAI, PASSWORD_TERLALU_PENDEK, PASSWORD_TIDAK_COCOK;

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