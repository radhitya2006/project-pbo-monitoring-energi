package GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CardPerangkat extends JPanel {

    private JLabel lblHeader;
    private JLabel lblStatus;
    private JLabel lblDaya;
    private JLabel lblEnergi;
    private JLabel lblBiaya;
    private JLabel lblEstimasi;
  

    private JButton btnEdit;
    private JButton btnNonaktif;

    public CardPerangkat(
            String lokasi, 
            String jenis,
            int daya,
            double energi,
            double biaya, 
            String estimasiRusak, // <-- Data baru dari backend
            String status
    ) {

        // Sedikit tinggikan card agar muat 5 baris (dari 200 ke 220)
        setPreferredSize(new Dimension(260, 220)); 
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        setLayout(new BorderLayout());

        // =========================
        // Header
        // =========================
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        panelHeader.setBorder(new EmptyBorder(10,10,5,10));

        // Tampilkan format canggih: "Jenis - Lokasi" (Misal: AC - Kamar Utama)
        lblHeader = new JLabel(jenis + " - " + lokasi);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panelHeader.add(lblHeader, BorderLayout.CENTER);

        // =========================
        // Body
        // =========================
        JPanel panelBody = new JPanel();
        panelBody.setOpaque(false);
        
        // Ubah GridLayout menjadi 5 baris (sebelumnya 4)
        panelBody.setLayout(new GridLayout(5,1)); 
        panelBody.setBorder(new EmptyBorder(5,10,5,10));

        
        lblStatus = new JLabel("Status : " + status); 
        lblDaya = new JLabel("Daya : " + daya + " Watt");
        lblEnergi = new JLabel(String.format("Energi : %.2f kWh", energi));
        lblBiaya = new JLabel(String.format("Biaya : Rp %,.0f Per Hari", biaya));
        
        
        lblEstimasi = new JLabel("Estimasi Rusak : " + estimasiRusak);
        lblEstimasi.setForeground(new Color(204, 51, 0));
        lblEstimasi.setFont(new Font("Segoe UI", Font.BOLD, 12));

        panelBody.add(lblStatus);
        panelBody.add(lblDaya);
        panelBody.add(lblEnergi);
        panelBody.add(lblBiaya);
        panelBody.add(lblEstimasi); // <-- Masukkan ke dalam panel

        // =========================
        // Footer
        // =========================
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelFooter.setOpaque(false);

        btnEdit = new JButton("Edit");
        btnNonaktif = new JButton("Nonaktifkan"); // <-- Teks diubah

        Color warnaBiruKustom = new Color(0, 204, 255);

        // Desain tombol Edit
        btnEdit.setBackground(warnaBiruKustom);
        btnEdit.setForeground(Color.WHITE);                     
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 12));  
        btnEdit.setBorderPainted(false);                       
        btnEdit.setFocusPainted(false);                        

        // Desain tombol Nonaktif
        btnNonaktif.setBackground(new Color(255, 102, 102));
        btnNonaktif.setForeground(Color.WHITE);
        btnNonaktif.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNonaktif.setBorderPainted(false);
        btnNonaktif.setFocusPainted(false);

        panelFooter.add(btnEdit);
        panelFooter.add(btnNonaktif);

        // =========================
        add(panelHeader, BorderLayout.NORTH);
        add(panelBody, BorderLayout.CENTER);
        add(panelFooter, BorderLayout.SOUTH);
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

  
    public JButton getBtnNonaktif() {
        return btnNonaktif;
    }
}