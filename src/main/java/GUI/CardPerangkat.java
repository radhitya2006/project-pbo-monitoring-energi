package GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CardPerangkat extends JPanel {

    private JLabel lblNama;
    private JLabel lblJenis;
    private JLabel lblDaya;
    private JLabel lblEnergi;
    private JLabel lblStatus;

    private JButton btnEdit;
    private JButton btnHapus;

    public CardPerangkat(
            String nama,
            String jenis,
            int daya,
            double energi,
            String status
    ) {

        setPreferredSize(new Dimension(260, 180));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(
                new Color(220, 220, 220)
        ));

        setLayout(new BorderLayout());

        // =========================
        // Header
        // =========================
        JPanel panelHeader = new JPanel(
                new BorderLayout()
        );

        panelHeader.setOpaque(false);
        panelHeader.setBorder(
                new EmptyBorder(10,10,5,10)
        );

        lblNama = new JLabel(nama);
        lblNama.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );

        lblStatus = new JLabel(status);

        if(status.equalsIgnoreCase("Aktif")){
            lblStatus.setForeground(
                    new Color(0,153,51)
            );
        }else{
            lblStatus.setForeground(Color.RED);
        }

        panelHeader.add(lblNama, BorderLayout.WEST);
        panelHeader.add(lblStatus, BorderLayout.EAST);

        // =========================
        // Body
        // =========================
        JPanel panelBody = new JPanel();
        panelBody.setOpaque(false);
        panelBody.setLayout(
                new GridLayout(3,1)
        );

        panelBody.setBorder(
                new EmptyBorder(5,10,5,10)
        );

        lblJenis = new JLabel(
                "Jenis : " + jenis
        );

        lblDaya = new JLabel(
                "Daya : " + daya + " Watt"
        );

        lblEnergi = new JLabel(
                String.format(
                        "Energi : %.2f kWh",
                        energi
                )
        );

        panelBody.add(lblJenis);
        panelBody.add(lblDaya);
        panelBody.add(lblEnergi);

        // =========================
        // Footer
        // =========================
        JPanel panelFooter = new JPanel(
                new FlowLayout(
                        FlowLayout.RIGHT
                )
        );

        panelFooter.setOpaque(false);

        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");

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