package GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CardPerangkat extends JPanel {

    private JLabel lblNama;
    private JLabel lblJenis;
    private JLabel lblDaya;
    private JLabel lblEnergi;
    private JLabel lblBiaya; // <-- 1. Tambahkan variabel baru untuk Biaya
  

    private JButton btnEdit;
    private JButton btnHapus;

    // 2. Tambahkan 'double biaya' pada parameter
    public CardPerangkat(
            String nama,
            String jenis,
            int daya,
            double energi,
            double biaya, 
            String status
    ) {

        setPreferredSize(new Dimension(260, 200)); // Sedikit tinggikan card agar muat (dari 180 ke 200)
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        setLayout(new BorderLayout());

        // =========================
        // Header
        // =========================
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        panelHeader.setBorder(new EmptyBorder(10,10,5,10));

        lblNama = new JLabel(nama);
        lblNama.setFont(new Font("Segoe UI", Font.BOLD, 16));

        

        // =========================
        // Body
        // =========================
        JPanel panelBody = new JPanel();
        panelBody.setOpaque(false);
        
        // 3. Ubah GridLayout menjadi 4 baris (sebelumnya 3)
        panelBody.setLayout(new GridLayout(4,1)); 
        panelBody.setBorder(new EmptyBorder(5,10,5,10));

        lblJenis = new JLabel("Jenis : " + jenis);
        lblDaya = new JLabel("Daya : " + daya + " Watt");
        lblEnergi = new JLabel(String.format("Energi : %.2f kWh", energi));
        
        // 4. Inisialisasi label biaya dengan format Rupiah
        lblBiaya = new JLabel(String.format("Biaya : Rp %,.0f Per Hari", biaya));

        panelBody.add(lblJenis);
        panelBody.add(lblDaya);
        panelBody.add(lblEnergi);
        panelBody.add(lblBiaya); // <-- 5. Masukkan ke dalam panel

        // =========================
        // Footer
        // =========================
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelFooter.setOpaque(false);

        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");
        
        
        Color warnaBiruKustom = new Color(0, 204, 255);
        
        btnEdit.setBackground(warnaBiruKustom);
        btnEdit.setForeground(Color.WHITE);                     // Warna teks putih
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 12));  // Gunakan font semi-bold agar tegas
        btnEdit.setBorderPainted(false);                       // Hilangkan border kaku
        btnEdit.setFocusPainted(false);                        // Hilangkan garis putus-putus saat diklik

        // 4. Atur desain tombol HAPUS agar senada dengan Dashboard
        btnHapus.setBackground(warnaBiruKustom);
        btnHapus.setForeground(Color.WHITE);
        btnHapus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnHapus.setBorderPainted(false);
        btnHapus.setFocusPainted(false);

        panelFooter.add(btnEdit);
        panelFooter.add(btnHapus);

        // =========================
        add(panelHeader, BorderLayout.NORTH);
        add(panelBody, BorderLayout.CENTER);
        add(panelFooter, BorderLayout.SOUTH);
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public JButton getBtnHapus() {
        return btnHapus;
    }
}