/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import com.mycompany.monitoringenergirumah.Data.PerangkatDAO;
import com.mycompany.monitoringenergirumah.Service.SistemMonitoring;
import com.mycompany.monitoringenergirumah.Model.*;
import com.mycompany.monitoringenergirumah.Service.AuthService;
import javax.swing.JOptionPane;

/**
 *
 * @author ASUS
 */
public class TambahPerangkat extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TambahPerangkat.class.getName());

    private Perangkat parent;
    private SistemMonitoring sistem;
    private PerangkatListrik perangkatEdit = null; 
    private AuthService authService;

   
    
    
    private void initLogic() {
        jPanel3.setVisible(false); 
        jPanel4.setVisible(false); 
        
        pack();

    
        pilihJenis.addActionListener(e -> updatePanelVisibility());
    }

    private void updatePanelVisibility() {
        String jenis = pilihJenis.getSelectedItem().toString();
        jPanel3.setVisible(jenis.equals("AC"));
        jPanel4.setVisible(jenis.equals("Televisi"));
        
         jPanel1.revalidate(); 
        jPanel1.repaint();    
        pack();
        
        hitungEstimasi();
    }
    
    public TambahPerangkat(Perangkat parent, SistemMonitoring sistem, AuthService authService) {
        initComponents();
        this.parent = parent;
        this.sistem = sistem;
        this.authService = authService;
        initLogic();
    }
    
    // ========================================================
    // CONSTRUCTOR 2: KHUSUS UNTUK MENGEDIT PERANGKAT
    // ========================================================
    public TambahPerangkat(Perangkat parent, SistemMonitoring sistem, PerangkatListrik p) {
        initComponents();
        this.parent = parent;
        this.sistem = sistem;
        this.perangkatEdit = p;
        initLogic();
        
        if (p.getTanggalPemasangan() != null) {
            java.time.LocalDate ld = p.getTanggalPemasangan();
            java.util.Date date = java.sql.Date.valueOf(ld);
            jDateChooser1.setDate(date);
        }

        // Ubah judul form
        jLabel1.setText("Edit Perangkat");
        
        // Kunci jenis perangkat
        pilihJenis.setSelectedItem(p.getClass().getSimpleName());
        pilihJenis.setEnabled(false);

        // Isi form dengan data lama (UBAH BAGIAN INI)
        comboboxLokasi.setSelectedItem(p.getNama()); // Memilih lokasi di ComboBox
        txtDaya.setText(String.valueOf((int) p.getDaya()));
        txtLamaPemakaian.setText(String.valueOf(p.getLamaPemakaian()));
        
        // Khusus untuk AC
        if (p instanceof AC) {
            txtCop.setText(String.valueOf(((AC) p).getCop()));
        } else {
            txtCop.setEnabled(false);
        }
        
        if (p instanceof Televisi) {
            txtDayaStandby.setText(String.valueOf(((Televisi) p).getDayaStandby()));
            txtPersentaseStandby.setText(String.valueOf(((Televisi) p).getPersentaseStandby()));
        }
    }
    
    private void hitungEstimasi() {
    try {
        String jenis = pilihJenis.getSelectedItem().toString();
        if (jenis.equals("Pilih Jenis Perangkat") 
                || txtDaya.getText().trim().isEmpty() 
                || txtLamaPemakaian.getText().trim().isEmpty()) {
            lblEnergi.setText("0.00 kWh");
            lblHarga.setText("Rp 0");
            return;
        }

        int daya     = Integer.parseInt(txtDaya.getText().trim());
        double waktu = Double.parseDouble(txtLamaPemakaian.getText().trim());
        PerangkatListrik sementara = null;

        switch (jenis) {
            case "AC": {
                double cop = 3.0;
                if (!txtCop.getText().isEmpty()) {
                    cop = Double.parseDouble(txtCop.getText());
                }
                sementara = new AC("Estimasi", daya, waktu, cop);
                break;
            }
            case "Lampu":
                sementara = new Lampu("Estimasi", daya, waktu);
                break;
            case "Televisi": {
                int ds   = txtDayaStandby.getText().isEmpty() ? 2 
                           : Integer.parseInt(txtDayaStandby.getText());
                double ps = txtPersentaseStandby.getText().isEmpty() ? 0.1 
                           : Double.parseDouble(txtPersentaseStandby.getText());
                sementara = new Televisi("Estimasi", daya, waktu, ds, ps);
                break;
            }
        }

        if (sementara != null) {
            double energi = sementara.hitungEnergi();
            double biaya  = sistem.getKalkulator().hitungBiayaDenganPajak(energi);
            lblEnergi.setText(String.format("%.2f kWh", energi));
            lblHarga.setText(String.format("Rp %,.0f", biaya));
        }

    } catch (NumberFormatException e) {
        lblEnergi.setText("0.00 kWh");
        lblHarga.setText("Rp 0");
    }
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pilihJenis = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtDaya = new javax.swing.JTextField();
        txtLamaPemakaian = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblEnergi = new javax.swing.JLabel();
        lblHarga = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtCop = new javax.swing.JTextField();
        comboboxLokasi = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtDayaStandby = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtPersentaseStandby = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Tambah Perangkat Baru");

        pilihJenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Pilih Jenis Perangkat", "Lampu", "AC", "Televisi" }));
        pilihJenis.setToolTipText("");

        jLabel2.setText("Jenis Perangkat");

        jLabel3.setText("Lokasi Pemasangan");

        jLabel4.setText("Daya (W)");

        jLabel5.setText("Lama Pemakaian (Jam)");

        txtDaya.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDayaKeyReleased(evt);
            }
        });

        txtLamaPemakaian.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLamaPemakaianKeyReleased(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setText("Estimasi Energi & Biaya");

        lblEnergi.setText("0.00 kWh");

        lblHarga.setText(" Rp 0");

        jLabel10.setText("per hari");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(lblEnergi))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(lblHarga))
                .addGap(26, 26, 26))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 137, Short.MAX_VALUE)
                .addComponent(lblHarga)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEnergi)
                    .addComponent(jLabel10))
                .addContainerGap())
        );

        jButton1.setBackground(new java.awt.Color(0, 204, 255));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Simpan");
        jButton1.setBorderPainted(false);
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setText("Batal");
        jButton2.setBorderPainted(false);
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setText("Nilai COP");

        txtCop.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCopKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCop, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        comboboxLokasi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Masukkan Lokasi", "Kamar Tidur", "Ruang Tamu", "Kamar Mandi", "Dapur" }));

        jLabel8.setText("Tanggal Pemasangan (DD/MM/YYYY)");

        jDateChooser1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel9.setText("Daya Standby");

        txtDayaStandby.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDayaStandbyKeyReleased(evt);
            }
        });

        jLabel11.setText("Persentase Standby (0.0 - 1.0)");

        txtPersentaseStandby.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPersentaseStandbyKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtDayaStandby, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11)
                    .addComponent(txtPersentaseStandby, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDayaStandby, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPersentaseStandby, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(120, 120, 120)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(143, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pilihJenis, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(comboboxLokasi, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(txtDaya, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtLamaPemakaian, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(38, 38, 38)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pilihJenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboboxLokasi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDaya, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLamaPemakaian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
        // ── Validasi input umum ──
        String jenis = pilihJenis.getSelectedItem().toString();
        if (jenis.equals("Pilih Jenis Perangkat")) {
            JOptionPane.showMessageDialog(this, "Silakan pilih jenis perangkat!",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String lokasi = comboboxLokasi.getSelectedItem().toString();
        if (lokasi.equals("Masukkan Lokasi")) {
            JOptionPane.showMessageDialog(this, "Silakan pilih lokasi pemasangan!",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.util.Date tanggalPilih = jDateChooser1.getDate();
        if (tanggalPilih == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih tanggal pemasangan!",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        java.time.LocalDate tanggalInput = tanggalPilih.toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        int daya     = Integer.parseInt(txtDaya.getText().trim());
        double waktu = Double.parseDouble(txtLamaPemakaian.getText().trim());

        PerangkatDAO dao = new PerangkatDAO();

        // ─────────────────────────────────────────
        // MODE EDIT
        // ─────────────────────────────────────────
        if (perangkatEdit != null) {
            // Update object di RAM
            perangkatEdit.setNama(lokasi);
            perangkatEdit.setDaya(daya);
            perangkatEdit.setLamaPemakaian(waktu);
            perangkatEdit.setTanggalPemasangan(tanggalInput);

            if (perangkatEdit instanceof AC && !txtCop.getText().trim().isEmpty()) {
                ((AC) perangkatEdit).setCop(
                    Double.parseDouble(txtCop.getText().trim()));
            }
            if (perangkatEdit instanceof Televisi) {
                if (!txtDayaStandby.getText().trim().isEmpty()) {
                    ((Televisi) perangkatEdit).setDayaStandby(
                        Integer.parseInt(txtDayaStandby.getText().trim()));
                }
                if (!txtPersentaseStandby.getText().trim().isEmpty()) {
                    ((Televisi) perangkatEdit).setPersentaseStandby(
                        Double.parseDouble(txtPersentaseStandby.getText().trim()));
                }
            }

            // ← Simpan ke DB
            boolean berhasil = dao.updatePerangkat(perangkatEdit.getId(), perangkatEdit);

            if (berhasil) {
                JOptionPane.showMessageDialog(this, "Perangkat berhasil diperbarui!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                parent.refreshData();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan perubahan ke database!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            return; // ← stop, jangan lanjut ke mode tambah
        }

        // ─────────────────────────────────────────
        // MODE TAMBAH
        // ─────────────────────────────────────────
        if (authService == null || authService.getCurrentUser() == null) {
            JOptionPane.showMessageDialog(this, "Sesi login tidak ditemukan!",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PerangkatListrik perangkatBaru = null;

        switch (jenis) {
            case "AC": {
                double cop = 3.0;
                if (!txtCop.getText().trim().isEmpty()) {
                    cop = Double.parseDouble(txtCop.getText().trim());
                }
                perangkatBaru = new AC(lokasi, daya, waktu, cop);
                break;
            }
            case "Lampu": {
                perangkatBaru = new Lampu(lokasi, daya, waktu);
                break;
            }
            case "Televisi": {
                int ds = txtDayaStandby.getText().trim().isEmpty() ? 2
                         : Integer.parseInt(txtDayaStandby.getText().trim());
                double ps = txtPersentaseStandby.getText().trim().isEmpty() ? 0.1
                         : Double.parseDouble(txtPersentaseStandby.getText().trim());
                perangkatBaru = new Televisi(lokasi, daya, waktu, ds, ps);
                break;
            }
        }

        if (perangkatBaru != null) {
            perangkatBaru.setTanggalPemasangan(tanggalInput);
            perangkatBaru.setStatus(PerangkatListrik.STATUS_AKTIF);

            // ← Simpan ke DB
            int idUser = authService.getCurrentUser().getId();
            boolean berhasil = dao.tambahPerangkat(perangkatBaru, idUser);

            if (berhasil) {
                JOptionPane.showMessageDialog(this, "Perangkat berhasil ditambahkan!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                parent.refreshData(); // ← load ulang dari DB otomatis
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan ke database!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
            "Daya, Waktu, COP, dan Daya Standby harus berupa angka!",
            "Error Input", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Terjadi kesalahan: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtDayaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDayaKeyReleased
        hitungEstimasi();
    }//GEN-LAST:event_txtDayaKeyReleased

    private void txtLamaPemakaianKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLamaPemakaianKeyReleased
        hitungEstimasi();
    }//GEN-LAST:event_txtLamaPemakaianKeyReleased

    private void txtCopKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCopKeyReleased
        hitungEstimasi();
    }//GEN-LAST:event_txtCopKeyReleased

    private void txtDayaStandbyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDayaStandbyKeyReleased
        hitungEstimasi();
    }//GEN-LAST:event_txtDayaStandbyKeyReleased

    private void txtPersentaseStandbyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPersentaseStandbyKeyReleased
        hitungEstimasi();
    }//GEN-LAST:event_txtPersentaseStandbyKeyReleased

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> comboboxLokasi;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lblEnergi;
    private javax.swing.JLabel lblHarga;
    private javax.swing.JComboBox<String> pilihJenis;
    private javax.swing.JTextField txtCop;
    private javax.swing.JTextField txtDaya;
    private javax.swing.JTextField txtDayaStandby;
    private javax.swing.JTextField txtLamaPemakaian;
    private javax.swing.JTextField txtPersentaseStandby;
    // End of variables declaration//GEN-END:variables
}


