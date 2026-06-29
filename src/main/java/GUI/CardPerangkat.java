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

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public JButton getBtnNonaktif() {
        return btnNonaktif;
    }

    public CardPerangkat(
            String lokasi,
            String jenis,
            int daya,
            double energi,
            double biaya,
            String estimasiRusak,
            String status
    ) {

        setPreferredSize(new Dimension(260, 220));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        setLayout(new BorderLayout());

        // =========================
        // Header
        // =========================
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);
        panelHeader.setBorder(new EmptyBorder(10, 10, 5, 10));

        lblHeader = new JLabel(jenis + " - " + lokasi);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel lblStatusBadge = new JLabel(status);
        lblStatusBadge.setOpaque(true);
        lblStatusBadge.setBorder(new EmptyBorder(4, 8, 4, 8));
        lblStatusBadge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblStatusBadge.setForeground(Color.WHITE);

        switch (status.toLowerCase()) {
            case "aktif":
                lblStatusBadge.setBackground(new Color(46, 204, 113)); // hijau
                break;

            case "nonaktif":
                lblStatusBadge.setBackground(new Color(241, 196, 15)); // kuning
                lblStatusBadge.setForeground(Color.BLACK);
                break;

            case "rusak":
                lblStatusBadge.setBackground(new Color(231, 76, 60)); // merah
                break;

            default:
                lblStatusBadge.setBackground(Color.GRAY);
        }

        panelHeader.add(lblHeader, BorderLayout.WEST);
        panelHeader.add(lblStatusBadge, BorderLayout.EAST);

        // =========================
        // Body
        // =========================
        JPanel panelBody = new JPanel();
        panelBody.setOpaque(false);
        panelBody.setLayout(new GridLayout(4, 1));
        panelBody.setBorder(new EmptyBorder(5, 10, 5, 10));

        
        lblDaya = new JLabel("Daya : " + daya + " Watt");
        lblDaya.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEnergi = new JLabel(String.format("Energi : %.2f kWh", energi));
        lblEnergi.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBiaya = new JLabel(String.format("Biaya : Rp %,.0f Per Hari", biaya));
        lblBiaya.setFont(new Font("Segoe UI", Font.BOLD, 12));

        lblEstimasi = new JLabel("Estimasi Rusak : " + estimasiRusak);
        lblEstimasi.setForeground(new Color(204, 51, 0));
        lblEstimasi.setFont(new Font("Segoe UI", Font.BOLD, 12));

        
        panelBody.add(lblDaya);
        panelBody.add(lblEnergi);
        panelBody.add(lblBiaya);
        panelBody.add(lblEstimasi);

        // =========================
        // Footer
        // =========================
        JPanel panelFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelFooter.setOpaque(false);

        btnEdit = new JButton("Edit");
        btnEdit.setBackground(new Color(0, 204, 255));
        btnEdit.setForeground(Color.WHITE);
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEdit.setBorderPainted(false);
        btnEdit.setFocusPainted(false);

        btnNonaktif = new JButton();
        btnNonaktif.setForeground(Color.WHITE);
        btnNonaktif.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnNonaktif.setBorderPainted(false);
        btnNonaktif.setFocusPainted(false);

        if (status.equalsIgnoreCase("Aktif")) {

            btnNonaktif.setText("Nonaktifkan");
            btnNonaktif.setBackground(new Color(255, 102, 102));

            panelFooter.add(btnEdit);
            panelFooter.add(btnNonaktif);

        } else if (status.equalsIgnoreCase("Rusak")) {

            JLabel lblRusak = new JLabel("⚠ RUSAK");
            lblRusak.setForeground(new Color(204, 0, 0));
            lblRusak.setFont(new Font("Segoe UI", Font.BOLD, 12));

            btnNonaktif.setText("Aktifkan Kembali");
            btnNonaktif.setBackground(new Color(0, 180, 0));

            panelFooter.add(lblRusak);
            panelFooter.add(btnNonaktif);

        } else {

            btnNonaktif.setText("Aktifkan Kembali");
            btnNonaktif.setBackground(new Color(0, 180, 0));

            btnEdit.setEnabled(false);

            panelFooter.add(btnNonaktif);
        }

        // =========================
        // Tambahkan ke Card
        // =========================
        add(panelHeader, BorderLayout.NORTH);
        add(panelBody, BorderLayout.CENTER);
        add(panelFooter, BorderLayout.SOUTH);
    }
}