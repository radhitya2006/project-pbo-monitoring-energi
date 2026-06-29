package GUI;

import com.mycompany.monitoringenergirumah.Service.AuthService;
import com.mycompany.monitoringenergirumah.Service.SistemMonitoring;
import com.mycompany.monitoringenergirumah.Data.RiwayatDAO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.*;
import java.util.Map;

public class Dashboard extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(Dashboard.class.getName());

    // ── Fields — dideklarasikan SEKALI saja ──
    private AuthService authService;
    private SistemMonitoring sistem;
    private ChartPanel lineChartPanel;
    private ChartPanel donutChartPanel;
    private final RiwayatDAO riwayatDAO = new RiwayatDAO();
    // panelLineChartArea, panelDonutChartArea, lblNilai*
    // sudah ada di Variables declaration (GEN block bawah)

    // ── Constructor utama ──
    public Dashboard(AuthService authService, SistemMonitoring sistem) {
        this.authService = authService;
        this.sistem = sistem;
        initComponents();

        if (authService != null && authService.getCurrentUser() != null) {
            lblUser.setText(authService.getCurrentUser().getNamaLengkap());
            lblUser2.setText(authService.getCurrentUser().getNamaLengkap());
        }

        initLogic();
    }

    public Dashboard() {
        initComponents();
    }

    // ── initLogic ──
    private void initLogic() {
        // Panel sudah ada di designer, cukup set BorderLayout
        panelLineChartArea.setLayout(new BorderLayout());
        panelDonutChartArea.setLayout(new BorderLayout());

        loadStatistikKartu();
        initLineChart(7);
        initDonutChart();
    }

    // ── 4 Kartu Statistik ──
    private void loadStatistikKartu() {
        if (authService == null || authService.getCurrentUser() == null) return;
        int idUser = authService.getCurrentUser().getId();

        double hariIni  = riwayatDAO.getTotalKonsumsiHariIni(idUser);
        double bulanIni = riwayatDAO.getTotalKonsumsiUserBulanIni(idUser);
        double rataRata = riwayatDAO.getRataRataHarian(idUser);
        double biaya    = riwayatDAO.getTotalBiayaBulanIni(idUser);

        lblNilaiHariIni.setText(String.format("%.1f kWh", hariIni));
        lblNilaiTotalBulan.setText(String.format("%.1f kWh", bulanIni));
        lblNilaiRataRata.setText(String.format("%.1f kWh", rataRata));
        lblNilaiBiaya.setText(String.format("Rp %,.0f", biaya));
    }

    // ── Line Chart ──
    private void initLineChart(int jumlahHari) {
        if (authService == null || authService.getCurrentUser() == null) return;
        int idUser = authService.getCurrentUser().getId();

        Map<String, Double> data = riwayatDAO.getKonsumsiHarian(idUser, jumlahHari);

        XYSeries series = new XYSeries("kWh");
        String[] labels = new String[data.isEmpty() ? 1 : data.size()];

        if (data.isEmpty()) {
            series.add(0, 0);
            labels[0] = "-";
        } else {
            int i = 0;
            for (Map.Entry<String, Double> entry : data.entrySet()) {
                series.add(i, entry.getValue());
                try {
                    java.time.LocalDate ld = java.time.LocalDate.parse(entry.getKey());
                    labels[i] = ld.format(java.time.format.DateTimeFormatter.ofPattern(
                            "d MMM", new java.util.Locale("id", "ID")));
                } catch (Exception ex) {
                    labels[i] = entry.getKey();
                }
                i++;
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                null, null, null, dataset,
                PlotOrientation.VERTICAL, false, true, false);

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setDomainGridlinePaint(new Color(230, 230, 230));
        plot.setRangeGridlinePaint(new Color(230, 230, 230));

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(0, 188, 212));
        renderer.setSeriesStroke(0, new BasicStroke(2.5f,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new java.awt.geom.Ellipse2D.Double(-4, -4, 8, 8));
        renderer.setSeriesFillPaint(0, new Color(0, 188, 212));
        renderer.setUseFillPaint(true);
        plot.setRenderer(renderer);

        final String[] finalLabels = labels;
        org.jfree.chart.axis.NumberAxis xAxis =
                (org.jfree.chart.axis.NumberAxis) plot.getDomainAxis();
        xAxis.setAxisLineVisible(false);
        xAxis.setTickMarksVisible(false);
        xAxis.setNumberFormatOverride(new java.text.NumberFormat() {
            @Override
            public StringBuffer format(double n, StringBuffer b, java.text.FieldPosition p) {
                int idx = (int) Math.round(n);
                if (idx >= 0 && idx < finalLabels.length) b.append(finalLabels[idx]);
                return b;
            }
            @Override
            public StringBuffer format(long n, StringBuffer b, java.text.FieldPosition p) { return b; }
            @Override
            public Number parse(String s, java.text.ParsePosition p) { return null; }
        });

        org.jfree.chart.axis.NumberAxis yAxis =
                (org.jfree.chart.axis.NumberAxis) plot.getRangeAxis();
        yAxis.setAxisLineVisible(false);
        yAxis.setTickMarksVisible(false);

        chart.setBackgroundPaint(Color.WHITE);
        chart.setBorderVisible(false);

        panelLineChartArea.removeAll();
        lineChartPanel = new ChartPanel(chart);
        lineChartPanel.setBackground(Color.WHITE);
        lineChartPanel.setPopupMenu(null);
        lineChartPanel.setPreferredSize(new Dimension(520, 175)); // ← tambahkan
        lineChartPanel.setMaximumSize(new Dimension(520, 175));
        panelLineChartArea.add(lineChartPanel, BorderLayout.CENTER);
        panelLineChartArea.revalidate();
        panelLineChartArea.repaint();
    }

    // ── Donut Chart ──
    private void initDonutChart() {
        if (authService == null || authService.getCurrentUser() == null) return;
        int idUser = authService.getCurrentUser().getId();

        Map<String, Double> distribusi = riwayatDAO.getDistribusiByJenis(idUser);

        DefaultPieDataset dataset = new DefaultPieDataset();
        if (distribusi.isEmpty()) {
            dataset.setValue("Belum ada data", 1.0);
        } else {
            distribusi.forEach(dataset::setValue);
        }

        JFreeChart chart = ChartFactory.createRingChart(
                null, dataset, true, true, false);

        RingPlot plot = (RingPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setSectionDepth(0.38);
        plot.setSeparatorsVisible(false);
        plot.setLabelGenerator(null);
        plot.setSectionPaint("Lampu",    new Color(255, 193,   7));
        plot.setSectionPaint("AC",       new Color(33,  150, 243));
        plot.setSectionPaint("Televisi", new Color(76,  175,  80));
        plot.setShadowPaint(null);
        plot.setStartAngle(90);

        org.jfree.chart.title.LegendTitle legend = chart.getLegend();
        legend.setPosition(org.jfree.chart.ui.RectangleEdge.BOTTOM);
        legend.setBackgroundPaint(Color.WHITE);
        legend.setFrame(new org.jfree.chart.block.BlockBorder(Color.WHITE));
        legend.setItemFont(new Font("SansSerif", Font.PLAIN, 11));

        chart.setBackgroundPaint(Color.WHITE);
        chart.setBorderVisible(false);

        panelDonutChartArea.removeAll();
        donutChartPanel = new ChartPanel(chart);
        donutChartPanel.setBackground(Color.WHITE);
        donutChartPanel.setPopupMenu(null);
        donutChartPanel.setPreferredSize(new Dimension(250, 195)); // ← tambahkan
        donutChartPanel.setMaximumSize(new Dimension(250, 195));
        panelDonutChartArea.add(donutChartPanel, BorderLayout.CENTER);
        panelDonutChartArea.revalidate();
        panelDonutChartArea.repaint();
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
        jLabel2 = new javax.swing.JLabel();
        btnDashboard = new javax.swing.JButton();
        btnPerangkat = new javax.swing.JButton();
        btnPengaturan = new javax.swing.JButton();
        btnLogOut = new javax.swing.JButton();
        lblUser2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        lblNilaiHariIni = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblNilaiTotalBulan = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblNilaiRataRata = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblNilaiBiaya = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        panelLineChartArea = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        panelDonutChartArea = new javax.swing.JPanel();
        lblUser = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Energy");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Energi");

        btnDashboard.setBackground(new java.awt.Color(0, 204, 255));
        btnDashboard.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDashboard.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard.setText("Beranda");
        btnDashboard.addActionListener(this::btnDashboardActionPerformed);

        btnPerangkat.setBackground(new java.awt.Color(0, 204, 255));
        btnPerangkat.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPerangkat.setForeground(new java.awt.Color(255, 255, 255));
        btnPerangkat.setText("Perangkat");
        btnPerangkat.addActionListener(this::btnPerangkatActionPerformed);

        btnPengaturan.setBackground(new java.awt.Color(0, 204, 255));
        btnPengaturan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPengaturan.setForeground(new java.awt.Color(255, 255, 255));
        btnPengaturan.setText("Pengaturan");
        btnPengaturan.addActionListener(this::btnPengaturanActionPerformed);

        btnLogOut.setBackground(new java.awt.Color(0, 204, 255));
        btnLogOut.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLogOut.setForeground(new java.awt.Color(255, 255, 255));
        btnLogOut.setText("Logout");
        btnLogOut.addActionListener(this::btnLogOutActionPerformed);

        lblUser2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUser2.setForeground(new java.awt.Color(255, 255, 255));
        lblUser2.setText("User");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPerangkat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(btnPengaturan, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                    .addComponent(btnLogOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(lblUser2)
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
                .addComponent(btnDashboard)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPerangkat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPengaturan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLogOut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblUser2)
                .addGap(28, 28, 28))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Dashboard");

        jLabel5.setText("Selamat datang, ");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Total Konsumsi Hari Ini");

        lblNilaiHariIni.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNilaiHariIni.setText("nilai kWh");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(lblNilaiHariIni, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(92, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(lblNilaiHariIni)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Total Konsumsi Bulan Ini");

        lblNilaiTotalBulan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNilaiTotalBulan.setText("nilai kWh");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(lblNilaiTotalBulan))
                .addContainerGap(106, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(lblNilaiTotalBulan)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Rata-Rata Harian");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Dalam 30 Hari Terakhir");

        lblNilaiRataRata.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNilaiRataRata.setText("nilai kWh");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10)
                    .addComponent(lblNilaiRataRata))
                .addContainerGap(92, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(lblNilaiRataRata)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Biaya Bulan Ini");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Estimasi Biaya");

        lblNilaiBiaya.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblNilaiBiaya.setText("nilai Rp");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel11)
                    .addComponent(lblNilaiBiaya))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(lblNilaiBiaya)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addContainerGap())
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Grafik Penggunaan Energi");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "7 Hari Terakhir", "14 Hari Terakhir", "21 Hari Terakhir", "30 Hari Terakhir" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jComboBox1.addActionListener(this::jComboBox1ActionPerformed);

        panelLineChartArea.setBackground(new java.awt.Color(255, 255, 255));
        panelLineChartArea.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelLineChartArea.setMinimumSize(new java.awt.Dimension(500, 250));
        panelLineChartArea.setPreferredSize(new java.awt.Dimension(500, 250));

        javax.swing.GroupLayout panelLineChartAreaLayout = new javax.swing.GroupLayout(panelLineChartArea);
        panelLineChartArea.setLayout(panelLineChartAreaLayout);
        panelLineChartAreaLayout.setHorizontalGroup(
            panelLineChartAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelLineChartAreaLayout.setVerticalGroup(
            panelLineChartAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 246, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelLineChartArea, javax.swing.GroupLayout.DEFAULT_SIZE, 561, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 280, Short.MAX_VALUE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelLineChartArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Distribusi Penggunaan");

        panelDonutChartArea.setBackground(new java.awt.Color(255, 255, 255));
        panelDonutChartArea.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout panelDonutChartAreaLayout = new javax.swing.GroupLayout(panelDonutChartArea);
        panelDonutChartArea.setLayout(panelDonutChartAreaLayout);
        panelDonutChartAreaLayout.setHorizontalGroup(
            panelDonutChartAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelDonutChartAreaLayout.setVerticalGroup(
            panelDonutChartAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelDonutChartArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(panelDonutChartArea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

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
                        .addGap(414, 925, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addContainerGap())))
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
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(282, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1070, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDashboardActionPerformed

    private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutActionPerformed
        authService.logout();

        SignIn signin = new SignIn(authService);
        signin.setVisible(true);

        dispose();
    }//GEN-LAST:event_btnLogOutActionPerformed

    private void btnPerangkatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerangkatActionPerformed
    Perangkat formPerangkat = new Perangkat(authService, sistem);
    formPerangkat.setLocationRelativeTo(null);
    formPerangkat.setVisible(true);

    this.dispose();
    }//GEN-LAST:event_btnPerangkatActionPerformed
    
    private void btnPengaturanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPengaturanActionPerformed
    Pengaturan formPengaturan = new Pengaturan(authService, sistem);
    formPengaturan.setLocationRelativeTo(null);
    formPengaturan.setVisible(true);

    this.dispose();
    }//GEN-LAST:event_btnPengaturanActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if (panelLineChartArea == null) return; // guard: belum siap

        String pilihan = jComboBox1.getSelectedItem().toString();
        int hari;
        switch (pilihan) {
            case "14 Hari Terakhir": hari = 14; break;
            case "21 Hari Terakhir": hari = 21; break;
            case "30 Hari Terakhir": hari = 30; break;
            default: hari = 7;
        }
        initLineChart(hari);
    }//GEN-LAST:event_jComboBox1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Dashboard().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JButton btnPengaturan;
    private javax.swing.JButton btnPerangkat;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblNilaiBiaya;
    private javax.swing.JLabel lblNilaiHariIni;
    private javax.swing.JLabel lblNilaiRataRata;
    private javax.swing.JLabel lblNilaiTotalBulan;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblUser2;
    private javax.swing.JPanel panelDonutChartArea;
    private javax.swing.JPanel panelLineChartArea;
    // End of variables declaration//GEN-END:variables
}
