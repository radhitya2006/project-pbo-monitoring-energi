/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import com.mycompany.monitoringenergirumah.Data.PerangkatDAO;
import com.mycompany.monitoringenergirumah.Service.SistemMonitoring;
import com.mycompany.monitoringenergirumah.Model.PerangkatListrik;
import com.mycompany.monitoringenergirumah.Service.AuthService;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JButton;

/**
 *
 * @author USER
 */
public class Perangkat extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Perangkat.class.getName());

    private SistemMonitoring sistem;
    private com.mycompany.monitoringenergirumah.Service.AuthService authService; 
    private boolean isPlaceholderActive = true;

    
public Perangkat(AuthService authService, SistemMonitoring sistem) {

    initComponents();

    // Simpan objek ke dalam class
    this.authService = authService;
    this.sistem = sistem;

    // Tampilkan nama user
    if (authService != null && authService.getCurrentUser() != null) {
        String namaLengkap = authService.getCurrentUser().getNamaLengkap();
        lblUser.setText(namaLengkap);
        jLabel3.setText(namaLengkap);
    }

    // Atur layout grid agar rapi (2 kolom)
    panelContainer.setLayout(new java.awt.GridLayout(0, 2, 15, 15));

    // Inisialisasi filter dan tampilkan data
    initFilter();
    filterData();
}

    /**
     * Constructor default bawaan NetBeans (Bisa dibiarkan untuk fallback)
     */
    public Perangkat() {
        initComponents();
        sistem = new SistemMonitoring(); // Fallback jika dijalankan langsung (Shift+F6)
        panelContainer.setLayout(new java.awt.GridLayout(0, 2, 15, 15));
        initFilter();
        filterData();
    }

    private void filterData() {

    panelContainer.removeAll();

    if (authService == null || authService.getCurrentUser() == null) {
        panelContainer.revalidate();
        panelContainer.repaint();
        return;
    }

    PerangkatDAO dao = new PerangkatDAO();

    List<PerangkatListrik> daftarPerangkat =
            dao.getPerangkatByUser(
                    authService.getCurrentUser().getId()
            );

    String keyword =
            isPlaceholderActive
                    ? ""
                    : txtSearch.getText().toLowerCase().trim();

    String filterJenis =
            comboJenis.getSelectedItem().toString();

    String filterStatus =
            comboStatus.getSelectedItem().toString();

    for (PerangkatListrik p : daftarPerangkat) {

        String jenis = p.getClass().getSimpleName();
        String status = p.getStatus();

        boolean matchKeyword =
                keyword.isEmpty()
                || p.getNama().toLowerCase().contains(keyword)
                || jenis.toLowerCase().contains(keyword);

        boolean matchJenis =
                filterJenis.equals("Semua Jenis")
                || jenis.equalsIgnoreCase(filterJenis);

        boolean matchStatus;

        switch (filterStatus) {

            case "Perangkat Aktif":
                matchStatus =
                        status.equals(
                                PerangkatListrik.STATUS_AKTIF
                        );
                break;

            case "Nonaktif & Rusak":
                matchStatus =
                        status.equals(
                                PerangkatListrik.STATUS_NONAKTIF
                        )
                        || status.equals(
                                PerangkatListrik.STATUS_RUSAK
                        );
                break;

            default:
                matchStatus = true;
                break;
        }

        if (matchKeyword && matchJenis && matchStatus) {

            String lokasi = p.getNama();

            int daya = p.getDaya();

            double energi =
                    p.hitungEnergi();

            double biaya =
                    sistem.hitungBiayaPerangkat(p);

            String estimasi = "Belum ada data";

            if (p.getEstimasiRusak() != null) {

                java.time.format.DateTimeFormatter fmt =
                        java.time.format.DateTimeFormatter.ofPattern(
                                "dd MMM yyyy"
                        );

                estimasi =
                        p.getEstimasiRusak().format(fmt);
            }

            CardPerangkat card =
                    new CardPerangkat(
                            lokasi,
                            jenis,
                            daya,
                            energi,
                            biaya,
                            estimasi,
                            status
                    );

            card.getBtnEdit().addActionListener(e -> {

                TambahPerangkat formEdit =
                        new TambahPerangkat(
                                this,
                                sistem,
                                p
                        );

                formEdit.setLocationRelativeTo(this);
                formEdit.setVisible(true);
            });

            card.getBtnNonaktif().addActionListener(e -> {

                int confirm =
                        javax.swing.JOptionPane.showConfirmDialog(
                                this,
                                "Ubah status perangkat?",
                                "Konfirmasi",
                                javax.swing.JOptionPane.YES_NO_OPTION
                        );

                if (confirm ==
                        javax.swing.JOptionPane.YES_OPTION) {

                    PerangkatDAO perangkatDAO =
                            new PerangkatDAO();

                    String statusBaru;

                    if (status.equals(
                            PerangkatListrik.STATUS_AKTIF)) {

                        statusBaru =
                                PerangkatListrik.STATUS_NONAKTIF;

                    } else {

                        statusBaru =
                                PerangkatListrik.STATUS_AKTIF;
                    }

                    perangkatDAO.updateStatus(
                            p.getId(),
                            statusBaru
                    );

                    filterData();
                }
            });

            panelContainer.add(card);
        }
    }

    panelContainer.revalidate();
    panelContainer.repaint();
}
    
    public void refreshData() {
        filterData();
}
    
    private void initFilter() {
    // Reset model comboJenis (hapus tab \t di "Televisi")
    comboJenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
        "Semua Jenis", "AC", "Lampu", "Televisi"
    }));

    comboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{
        "Semua Status", "Perangkat Aktif", "Nonaktif & Rusak"
    }));

    // Tangani placeholder txtSearch
    txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            if (isPlaceholderActive) {
                isPlaceholderActive = false; // ← tambahkan ini
                txtSearch.setText("");
                txtSearch.setForeground(java.awt.Color.BLACK);
        }   
    }
        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (txtSearch.getText().isBlank()) {
                isPlaceholderActive = true;
                txtSearch.setText("Cari Perangkat....");
                txtSearch.setForeground(new java.awt.Color(204, 204, 204));
            }
        }
    });

    // Listener txtSearch
    txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        @Override public void insertUpdate(javax.swing.event.DocumentEvent e)  { filterData(); }
        @Override public void removeUpdate(javax.swing.event.DocumentEvent e)  { filterData(); }
        @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { filterData(); }
    });

    // Listener kedua ComboBox
    comboJenis.addActionListener(e -> filterData());
    comboStatus.addActionListener(e -> filterData());
}
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnTambahPerangkat = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        comboJenis = new javax.swing.JComboBox<>();
        txtSearch = new javax.swing.JTextField();
        comboStatus = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        panelContainer = new javax.swing.JPanel();
        lblUser = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnBeranda = new javax.swing.JButton();
        btnPerangkat = new javax.swing.JButton();
        btnPengaturan = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("Perangkat");

        jLabel5.setText("Selamat datang, ");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Daftar Perangkat");

        btnTambahPerangkat.setBackground(new java.awt.Color(0, 204, 255));
        btnTambahPerangkat.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahPerangkat.setText("+ Tambah Perangkat");
        btnTambahPerangkat.setBorderPainted(false);
        btnTambahPerangkat.addActionListener(this::btnTambahPerangkatActionPerformed);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        comboJenis.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Jenis", "AC", "Lampu", "Televisi\t" }));
        comboJenis.setOpaque(true);

        txtSearch.setForeground(new java.awt.Color(204, 204, 204));
        txtSearch.addActionListener(this::txtSearchActionPerformed);

        comboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua Status", "Perangkat Aktif", "Arsip & Riwayar" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(comboJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboJenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setOpaque(false);

        panelContainer.setBackground(new java.awt.Color(255, 255, 255));
        panelContainer.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelContainer.setLayout(new java.awt.GridLayout(0, 2));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(994, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(478, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel4);

        lblUser.setText("user");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblUser))
                            .addComponent(jLabel4))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnTambahPerangkat)))
                        .addGap(14, 14, 14))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblUser))
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTambahPerangkat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Energy");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Tracker");

        btnBeranda.setBackground(new java.awt.Color(0, 204, 255));
        btnBeranda.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBeranda.setForeground(new java.awt.Color(255, 255, 255));
        btnBeranda.setText("Beranda");
        btnBeranda.addActionListener(this::btnBerandaActionPerformed);

        btnPerangkat.setBackground(new java.awt.Color(0, 204, 255));
        btnPerangkat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPerangkat.setForeground(new java.awt.Color(255, 255, 255));
        btnPerangkat.setText("Perangkat");

        btnPengaturan.setBackground(new java.awt.Color(0, 204, 255));
        btnPengaturan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPengaturan.setForeground(new java.awt.Color(255, 255, 255));
        btnPengaturan.setText("Pengaturan");
        btnPengaturan.addActionListener(this::btnPengaturanActionPerformed);

        jButton6.setBackground(new java.awt.Color(0, 204, 255));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Logout");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("User");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBeranda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPerangkat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnPengaturan, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(btnBeranda)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPerangkat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPengaturan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(77, 77, 77))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBerandaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBerandaActionPerformed
        Dashboard formDashboard = new Dashboard(this.authService, this.sistem);
        
        formDashboard.setLocationRelativeTo(null); // Posisi tengah layar
        formDashboard.setVisible(true);            // Tampilkan dashboard
        this.dispose();
    }//GEN-LAST:event_btnBerandaActionPerformed

    private void btnTambahPerangkatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahPerangkatActionPerformed
        TambahPerangkat form =
            new TambahPerangkat(this, sistem, authService);

        form.setLocationRelativeTo(this);
        form.setVisible(true);
        
    }//GEN-LAST:event_btnTambahPerangkatActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnPengaturanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPengaturanActionPerformed
            Pengaturan formPengaturan = new Pengaturan(authService, sistem);
    formPengaturan.setLocationRelativeTo(null);
    formPengaturan.setVisible(true);

    this.dispose();
    }//GEN-LAST:event_btnPengaturanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Perangkat().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBeranda;
    private javax.swing.JButton btnPengaturan;
    private javax.swing.JButton btnPerangkat;
    private javax.swing.JButton btnTambahPerangkat;
    private javax.swing.JComboBox<String> comboJenis;
    private javax.swing.JComboBox<String> comboStatus;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblUser;
    private javax.swing.JPanel panelContainer;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
