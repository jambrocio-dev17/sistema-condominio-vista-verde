/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mc296
 */
public class reporte_general extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(reporte_general.class.getName());

    // Datos del último reporte cargado (para exportar)
    private javax.swing.table.DefaultTableModel modeloActual;
    private double recaudadoActual;
    private double esperadoActual;
    private int    mesActual;
    private int    anioActual;

    private static final String DB_URL  = "jdbc:postgresql://localhost:5432/vista_verde";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "Elfogg2006.";

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    private void cargarReporte() {
        Calendar cal    = Calendar.getInstance();
        int mes         = cal.get(Calendar.MONTH) + 1;
        int anio        = cal.get(Calendar.YEAR);

        String sql =
            "SELECT c.numero_casa, " +
            "       COALESCE(p.nombre_completo, '(Sin propietario)') AS propietario, " +
            "       COALESCE(p.dpi, '-')    AS dpi, " +
            "       COALESCE(p.correo, '-') AS correo, " +
            "       COALESCE(pa.estado, 'Pendiente') AS estado, " +
            "       COALESCE(pa.monto, (SELECT cuota_actual FROM Configuracion LIMIT 1)) AS total " +
            "FROM Casa c " +
            "LEFT JOIN Propietario p  ON p.numero_casa  = c.numero_casa " +
            "LEFT JOIN Pago pa        ON pa.numero_casa = c.numero_casa " +
            "                        AND pa.mes = ? AND pa.anio = ? " +
            "ORDER BY c.numero_casa";

        DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"N.°","Propietario","DPI","Correo","Estado","Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        double recaudado = 0.0;
        double esperado  = 0.0;
        int    totalCasas = 0;

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, mes);
            ps.setInt(2, anio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String estado = rs.getString("estado");
                    double total  = rs.getDouble("total");

                    modelo.addRow(new Object[]{
                        rs.getInt("numero_casa"),
                        rs.getString("propietario"),
                        rs.getString("dpi"),
                        rs.getString("correo"),
                        estado,
                        String.format("Q%,.2f", total)
                    });

                    esperado += total;
                    if ("Pagado".equalsIgnoreCase(estado)) recaudado += total;
                    totalCasas++;
                }
            }

            // Actualizar la tabla y los labels con los datos reales
            jtbDatosReporte.setModel(modelo);

            double pendiente = esperado - recaudado;
            lblCantidadRecaudo.setText(String.format("Q%,.2f", recaudado));
            lblCantidadTotalEsperado.setText(String.format("Q%,.2f", esperado));
            lblCantidadPendiente.setText(String.format("Q%,.2f", pendiente));
            lblTextoCasasRegistradas.setText(totalCasas + " casas registradas");

            String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio",
                              "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
            lblReporteGeneral.setText(
                "<html><b style='color:white; font-size:11px;'>Reporte General — "
                + meses[mes - 1] + " " + anio
                + "</b><br><span style='color:#8fcc6f; font-size:9px;'>Condominio Vista Verde - "
                + totalCasas + " casas</span></html>");

            // Guardar datos para exportación posterior
            modeloActual    = modelo;
            recaudadoActual = recaudado;
            esperadoActual  = esperado;
            mesActual       = mes;
            anioActual      = anio;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar el reporte:\n" + ex.getMessage(),
                "Error de base de datos",
                JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, "Error cargando reporte", ex);
        }
    }
    
    public reporte_general() {
        initComponents();
        this.setLocationRelativeTo(null);
        btnExportarCSV.setText("Exportar Reporte");
        cargarReporte();
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
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbDatosReporte = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        lblRecaudomes = new javax.swing.JLabel();
        lblCantidadRecaudo = new javax.swing.JLabel();
        lblTotalEsperado = new javax.swing.JLabel();
        lblCantidadTotalEsperado = new javax.swing.JLabel();
        lblPendienteCobro = new javax.swing.JLabel();
        lblCantidadPendiente = new javax.swing.JLabel();
        btnExportarCSV = new javax.swing.JButton();
        lblTextoCasasRegistradas = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lblReporteGeneral = new javax.swing.JLabel();
        btnVolverInicio = new javax.swing.JButton();
        lblInicioReporte = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(26, 58, 10));

        jPanel5.setBackground(new java.awt.Color(240, 255, 234));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(240, 255, 234));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(58, 122, 26)));
        jScrollPane1.setViewportBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(58, 122, 26)));
        jScrollPane1.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N

        jtbDatosReporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "N.°", "Propietario", "DPI NUEVO", "Correo NUEVO", "Estado", " Total"
            }
        ));
        jScrollPane1.setViewportView(jtbDatosReporte);

        jPanel4.setBackground(new java.awt.Color(45, 90, 30));

        lblRecaudomes.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblRecaudomes.setForeground(new java.awt.Color(122, 170, 116));
        lblRecaudomes.setText("Recaudado del mes");

        lblCantidadRecaudo.setForeground(new java.awt.Color(255, 255, 255));
        lblCantidadRecaudo.setText("           Q27,000.00");

        lblTotalEsperado.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblTotalEsperado.setForeground(new java.awt.Color(122, 170, 116));
        lblTotalEsperado.setText("Total esperado");

        lblCantidadTotalEsperado.setForeground(new java.awt.Color(255, 255, 255));
        lblCantidadTotalEsperado.setText("Q45,000.00");

        lblPendienteCobro.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        lblPendienteCobro.setForeground(new java.awt.Color(122, 170, 116));
        lblPendienteCobro.setText("Pendiente de cobro");

        lblCantidadPendiente.setForeground(new java.awt.Color(244, 164, 53));
        lblCantidadPendiente.setText("Q18,000.00");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(lblRecaudomes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTotalEsperado, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(127, 127, 127)
                .addComponent(lblPendienteCobro)
                .addGap(60, 60, 60))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(lblCantidadRecaudo, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblCantidadTotalEsperado, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(138, 138, 138)
                .addComponent(lblCantidadPendiente, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRecaudomes)
                    .addComponent(lblTotalEsperado)
                    .addComponent(lblPendienteCobro))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCantidadTotalEsperado)
                    .addComponent(lblCantidadPendiente)
                    .addComponent(lblCantidadRecaudo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7))
        );

        btnExportarCSV.setBackground(new java.awt.Color(0, 0, 0));
        btnExportarCSV.setFont(new java.awt.Font("SansSerif", 1, 13)); // NOI18N
        btnExportarCSV.setForeground(new java.awt.Color(200, 230, 196));
        btnExportarCSV.setText("Exportar CSV");
        btnExportarCSV.addActionListener(this::btnExportarCSVActionPerformed);

        lblTextoCasasRegistradas.setFont(new java.awt.Font("SansSerif", 1, 11)); // NOI18N
        lblTextoCasasRegistradas.setForeground(new java.awt.Color(58, 122, 26));
        lblTextoCasasRegistradas.setText("30 casas registradas");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblTextoCasasRegistradas, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(472, 472, 472)
                        .addComponent(btnExportarCSV, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExportarCSV, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTextoCasasRegistradas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(45, 90, 30));

        jPanel6.setBackground(new java.awt.Color(26, 58, 10));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        lblReporteGeneral.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblReporteGeneral.setText("<html><b style='color:white; font-size:11px;'>Reporte General - Mayo 2026</b><br><span style='color:#8fcc6f; font-size:9px;'>Condominio Vista Verde - 30 casas</span></html>");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblReporteGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblReporteGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        btnVolverInicio.setBackground(new java.awt.Color(204, 204, 204));
        btnVolverInicio.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        btnVolverInicio.setText("←Volver al Inicio");
        btnVolverInicio.addActionListener(this::btnVolverInicioActionPerformed);

        lblInicioReporte.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        lblInicioReporte.setForeground(new java.awt.Color(58, 122, 26));
        lblInicioReporte.setText("<html>Inicio › <b>Reporte General</b></html>");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(btnVolverInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblInicioReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVolverInicio)
                    .addComponent(lblInicioReporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExportarCSVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarCSVActionPerformed
        if (modeloActual == null || modeloActual.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No hay datos para exportar.", "Sin datos", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] opciones = {"Exportar PDF", "Exportar Excel", "Cancelar"};
        int sel = JOptionPane.showOptionDialog(this,
            "Seleccione el formato de exportación:",
            "Exportar Reporte General",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
            null, opciones, opciones[0]);

        if (sel == 0) {
            logic.ReportGenerator.exportarReporteGeneralPDF(
                modeloActual, recaudadoActual, esperadoActual, mesActual, anioActual, this);
        } else if (sel == 1) {
            logic.ReportGenerator.exportarReporteGeneralExcel(
                modeloActual, recaudadoActual, esperadoActual, mesActual, anioActual, this);
        }
    }//GEN-LAST:event_btnExportarCSVActionPerformed

    private void btnVolverInicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverInicioActionPerformed
        // TODO add your handling code here:
        new menuPrincipal().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnVolverInicioActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new reporte_general().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExportarCSV;
    private javax.swing.JButton btnVolverInicio;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtbDatosReporte;
    private javax.swing.JLabel lblCantidadPendiente;
    private javax.swing.JLabel lblCantidadRecaudo;
    private javax.swing.JLabel lblCantidadTotalEsperado;
    private javax.swing.JLabel lblInicioReporte;
    private javax.swing.JLabel lblPendienteCobro;
    private javax.swing.JLabel lblRecaudomes;
    private javax.swing.JLabel lblReporteGeneral;
    private javax.swing.JLabel lblTextoCasasRegistradas;
    private javax.swing.JLabel lblTotalEsperado;
    // End of variables declaration//GEN-END:variables
}
