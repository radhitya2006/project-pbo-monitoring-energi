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
    private JLabel lblKondisi;
    private JLabel lblPersen;
    private JLabel lblSisaUmur;

    private JProgressBar progressKondisi;

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
        int persentaseKondisi,
        String sisaUmur,
        String status
    ) {

        setPreferredSize(new Dimension(250, 220));
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
        JPanel panelBody = new JPanel(new GridBagLayout());
        panelBody.setOpaque(false);
        panelBody.setBorder(new EmptyBorder(5, 10, 5, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        lblDaya = new JLabel("Daya : " + daya + " Watt");
        lblDaya.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblEnergi = new JLabel(String.format("Energi : %.2f kWh", energi));
        lblEnergi.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBiaya = new JLabel(String.format("Biaya : Rp %,.0f Per Hari", biaya));
        lblBiaya.setFont(new Font("Segoe UI", Font.BOLD, 12));

        lblKondisi = new JLabel("Kondisi Perangkat");
        lblKondisi.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        lblPersen = new JLabel(persentaseKondisi + "%");
        lblPersen.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSisaUmur = new JLabel("± " + sisaUmur + " lagi");
        lblSisaUmur.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        progressKondisi = new JProgressBar();

        progressKondisi.setMinimum(0);
        progressKondisi.setMaximum(100);
        progressKondisi.setValue(persentaseKondisi);

        progressKondisi.setBorderPainted(false);
        progressKondisi.setStringPainted(false);
        progressKondisi.setPreferredSize(new Dimension(220,8));
        progressKondisi.setMaximumSize(new Dimension(Integer.MAX_VALUE,8));
        
        if (persentaseKondisi >= 80) {

            progressKondisi.setForeground(new Color(46,204,113));

        } else if (persentaseKondisi >= 60) {

            progressKondisi.setForeground(new Color(132,204,22));

        } else if (persentaseKondisi >= 40) {

            progressKondisi.setForeground(new Color(241,196,15));

        } else if (persentaseKondisi >= 20) {

            progressKondisi.setForeground(new Color(230,126,34));

        } else {

            progressKondisi.setForeground(new Color(231,76,60));

        }
        
        JPanel panelKondisi = new JPanel(new BorderLayout());
        panelKondisi.setOpaque(false);

        panelKondisi.add(lblKondisi, BorderLayout.WEST);
        panelKondisi.add(lblPersen, BorderLayout.EAST);

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panelBody.add(lblDaya, gbc);

        gbc.gridy++;
        panelBody.add(lblEnergi, gbc);

        gbc.gridy++;
        panelBody.add(lblBiaya, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1;

        panelBody.add(lblKondisi, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0;

        panelBody.add(lblPersen, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        panelBody.add(progressKondisi, gbc);

        gbc.gridy++;
        panelBody.add(lblSisaUmur, gbc);

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