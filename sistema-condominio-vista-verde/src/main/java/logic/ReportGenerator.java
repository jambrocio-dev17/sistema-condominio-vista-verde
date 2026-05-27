package logic;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
// Explicit imports to resolve wildcard ambiguity (Font/Cell/Row exist in both OpenPDF and POI)
import com.lowagie.text.Font;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Desktop;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportGenerator {

    private static final Color COLOR_VERDE_OSCURO = new Color(45, 90, 30);
    private static final Color COLOR_VERDE_CLARO  = new Color(200, 230, 196);
    private static final Color COLOR_ROJO_MOROSAS = new Color(180, 50, 50);
    private static final Color COLOR_GRIS_ALT     = new Color(245, 245, 245);
    private static final String CONDOMINIO = "Condominio Vista Verde";
    private static final String[] MESES = {
        "Enero","Febrero","Marzo","Abril","Mayo","Junio",
        "Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"
    };

    // ─────────────────────────────────────────────
    //  REPORTE GENERAL — PDF
    // ─────────────────────────────────────────────
    public static void exportarReporteGeneralPDF(
            javax.swing.table.DefaultTableModel modelo,
            double recaudado, double esperado, int mes, int anio,
            java.awt.Component parent) {

        File archivo = elegirArchivo(parent, "ReporteGeneral_" + MESES[mes-1] + anio, "pdf");
        if (archivo == null) return;

        try {
            Document doc = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
            PdfWriter.getInstance(doc, new FileOutputStream(archivo));
            doc.open();

            agregarEncabezado(doc, "Reporte General de Cuotas",
                    MESES[mes-1] + " " + anio, COLOR_VERDE_OSCURO);

            String[] cols = {"N.°","Propietario","DPI","Correo","Estado","Total"};
            float[]  widths = {0.5f, 2.5f, 1.5f, 2f, 1f, 1f};
            PdfPTable tabla = crearTablaPDF(cols, widths, COLOR_VERDE_OSCURO);

            for (int r = 0; r < modelo.getRowCount(); r++) {
                Color bg = (r % 2 == 0) ? Color.WHITE : COLOR_GRIS_ALT;
                for (int c = 0; c < modelo.getColumnCount(); c++) {
                    Object val = modelo.getValueAt(r, c);
                    PdfPCell celda = celdaDato(val != null ? val.toString() : "", bg);
                    if (c == 4) {
                        boolean pagado = "Pagado".equalsIgnoreCase(val != null ? val.toString() : "");
                        celda.getPhrase().getFont().setColor(
                                pagado ? new Color(0, 130, 0) : new Color(200, 50, 50));
                    }
                    tabla.addCell(celda);
                }
            }
            doc.add(tabla);

            doc.add(Chunk.NEWLINE);
            agregarResumenPDF(doc,
                    new String[]{"Recaudado del mes", "Total esperado", "Pendiente de cobro"},
                    new double[]{recaudado, esperado, esperado - recaudado},
                    COLOR_VERDE_OSCURO);

            doc.close();
            abrirArchivo(archivo, parent);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Error al generar PDF:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─────────────────────────────────────────────
    //  REPORTE GENERAL — EXCEL
    // ─────────────────────────────────────────────
    public static void exportarReporteGeneralExcel(
            javax.swing.table.DefaultTableModel modelo,
            double recaudado, double esperado, int mes, int anio,
            java.awt.Component parent) {

        File archivo = elegirArchivo(parent, "ReporteGeneral_" + MESES[mes-1] + anio, "xlsx");
        if (archivo == null) return;

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Reporte General");

            CellStyle estiloTitulo  = estiloTituloExcel(wb, (byte)45, (byte)90, (byte)30);
            CellStyle estiloHeader  = estiloHeaderExcel(wb, (byte)45, (byte)90, (byte)30);
            CellStyle estiloAlt     = estiloAltExcel(wb, (byte)210, (byte)235, (byte)200);
            CellStyle estiloNormal  = wb.createCellStyle();
            CellStyle estiloResumen = estiloResumenExcel(wb, (byte)230, (byte)245, (byte)220);

            agregarFilaTitulo(sheet, 0, CONDOMINIO + " — Reporte General " + MESES[mes-1] + " " + anio, estiloTitulo, 6);
            agregarFilaTitulo(sheet, 1, "Generado: " + fechaActual(), wb.createCellStyle(), 6);

            String[] cols = {"N.°","Propietario","DPI","Correo","Estado","Total"};
            agregarFilaHeader(sheet, 3, cols, estiloHeader);

            for (int r = 0; r < modelo.getRowCount(); r++) {
                Row fila = sheet.createRow(r + 4);
                CellStyle estilo = (r % 2 == 1) ? estiloAlt : estiloNormal;
                for (int c = 0; c < modelo.getColumnCount(); c++) {
                    Cell celda = fila.createCell(c);
                    Object val = modelo.getValueAt(r, c);
                    celda.setCellValue(val != null ? val.toString() : "");
                    celda.setCellStyle(estilo);
                }
            }

            int filaRes = modelo.getRowCount() + 5;
            agregarFilaResumenExcel(sheet, filaRes,
                    new String[]{"Recaudado","Total Esperado","Pendiente"},
                    new double[]{recaudado, esperado, esperado - recaudado},
                    estiloResumen);

            for (int c = 0; c < 6; c++) sheet.autoSizeColumn(c);

            try (FileOutputStream fos = new FileOutputStream(archivo)) { wb.write(fos); }
            abrirArchivo(archivo, parent);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Error al generar Excel:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─────────────────────────────────────────────
    //  CASAS MOROSAS — PDF
    // ─────────────────────────────────────────────
    public static void exportarCasasMorosasPDF(
            javax.swing.table.DefaultTableModel modelo,
            int conteo, double montoTotal, int mes, int anio,
            java.awt.Component parent) {

        File archivo = elegirArchivo(parent, "CasasMorosas_" + MESES[mes-1] + anio, "pdf");
        if (archivo == null) return;

        try {
            Document doc = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter.getInstance(doc, new FileOutputStream(archivo));
            doc.open();

            agregarEncabezado(doc, "Casas Morosas", MESES[mes-1] + " " + anio, COLOR_ROJO_MOROSAS);

            String[] cols = {"No. Casa","Propietario","Teléfono","Meses de Deuda"};
            float[]  widths = {1f, 2.5f, 1.5f, 1f};
            PdfPTable tabla = crearTablaPDF(cols, widths, COLOR_ROJO_MOROSAS);

            for (int r = 0; r < modelo.getRowCount(); r++) {
                Color bg = (r % 2 == 0) ? Color.WHITE : COLOR_GRIS_ALT;
                for (int c = 0; c < modelo.getColumnCount(); c++) {
                    Object val = modelo.getValueAt(r, c);
                    tabla.addCell(celdaDato(val != null ? val.toString() : "", bg));
                }
            }
            doc.add(tabla);

            doc.add(Chunk.NEWLINE);
            agregarResumenPDF(doc,
                    new String[]{"Casas morosas", "Monto pendiente total"},
                    new double[]{conteo, montoTotal},
                    COLOR_ROJO_MOROSAS);

            doc.close();
            abrirArchivo(archivo, parent);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Error al generar PDF:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─────────────────────────────────────────────
    //  CASAS MOROSAS — EXCEL
    // ─────────────────────────────────────────────
    public static void exportarCasasMorosasExcel(
            javax.swing.table.DefaultTableModel modelo,
            int conteo, double montoTotal, int mes, int anio,
            java.awt.Component parent) {

        File archivo = elegirArchivo(parent, "CasasMorosas_" + MESES[mes-1] + anio, "xlsx");
        if (archivo == null) return;

        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Casas Morosas");

            CellStyle estiloTitulo  = estiloTituloExcel(wb, (byte)-76, (byte)50, (byte)50);
            CellStyle estiloHeader  = estiloHeaderExcel(wb, (byte)-76, (byte)50, (byte)50);
            CellStyle estiloAlt     = estiloAltExcel(wb, (byte)-1, (byte)-36, (byte)-36);
            CellStyle estiloNormal  = wb.createCellStyle();
            CellStyle estiloResumen = estiloResumenExcel(wb, (byte)-1, (byte)-21, (byte)-21);

            agregarFilaTitulo(sheet, 0, CONDOMINIO + " — Casas Morosas " + MESES[mes-1] + " " + anio, estiloTitulo, 4);
            agregarFilaTitulo(sheet, 1, "Generado: " + fechaActual(), wb.createCellStyle(), 4);

            String[] cols = {"No. Casa","Propietario","Teléfono","Meses de Deuda"};
            agregarFilaHeader(sheet, 3, cols, estiloHeader);

            for (int r = 0; r < modelo.getRowCount(); r++) {
                Row fila = sheet.createRow(r + 4);
                CellStyle estilo = (r % 2 == 1) ? estiloAlt : estiloNormal;
                for (int c = 0; c < modelo.getColumnCount(); c++) {
                    Cell celda = fila.createCell(c);
                    Object val = modelo.getValueAt(r, c);
                    celda.setCellValue(val != null ? val.toString() : "");
                    celda.setCellStyle(estilo);
                }
            }

            int filaRes = modelo.getRowCount() + 5;
            agregarFilaResumenExcel(sheet, filaRes,
                    new String[]{"Casas morosas", "Monto pendiente"},
                    new double[]{conteo, montoTotal},
                    estiloResumen);

            for (int c = 0; c < 4; c++) sheet.autoSizeColumn(c);
            try (FileOutputStream fos = new FileOutputStream(archivo)) { wb.write(fos); }
            abrirArchivo(archivo, parent);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Error al generar Excel:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─────────────────────────────────────────────
    //  ESTADO DE CUENTA — PDF
    // ─────────────────────────────────────────────
    public static void exportarEstadoCuentaPDF(
            int numeroCasa, String propietario, int anio,
            java.util.List<Object[]> pagados,
            java.util.List<Object[]> pendientes,
            java.awt.Component parent) {

        File archivo = elegirArchivo(parent, "EstadoCuenta_Casa" + numeroCasa + "_" + anio, "pdf");
        if (archivo == null) return;

        try {
            Document doc = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter.getInstance(doc, new FileOutputStream(archivo));
            doc.open();

            agregarEncabezado(doc, "Estado de Cuenta — Casa " + numeroCasa,
                    propietario + " | " + anio, COLOR_VERDE_OSCURO);

            Font fBold   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COLOR_VERDE_OSCURO);
            Font fNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);
            PdfPTable info = new PdfPTable(2);
            info.setWidthPercentage(60);
            info.setHorizontalAlignment(Element.ALIGN_LEFT);
            agregarInfoRow(info, "Casa:", "Casa " + numeroCasa, fBold, fNormal);
            agregarInfoRow(info, "Propietario:", propietario, fBold, fNormal);
            agregarInfoRow(info, "Año consultado:", String.valueOf(anio), fBold, fNormal);
            doc.add(info);
            doc.add(Chunk.NEWLINE);

            PdfPTable doble = new PdfPTable(2);
            doble.setWidthPercentage(100);

            doble.addCell(celdaTituloLateral("MESES PAGADOS (" + pagados.size() + ")", COLOR_VERDE_OSCURO));
            doble.addCell(celdaTituloLateral("MESES PENDIENTES (" + pendientes.size() + ")", COLOR_ROJO_MOROSAS));

            doble.addCell(celdaSubtabla(pagados));
            doble.addCell(celdaSubtabla(pendientes));
            doc.add(doble);

            double total = calcularTotal(pagados);
            doc.add(Chunk.NEWLINE);
            Font fTotal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, COLOR_VERDE_OSCURO);
            Paragraph pTotal = new Paragraph(
                    String.format("Total pagado en %d:  Q %,.2f", anio, total), fTotal);
            pTotal.setAlignment(Element.ALIGN_RIGHT);
            doc.add(pTotal);

            doc.close();
            abrirArchivo(archivo, parent);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Error al generar PDF:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─────────────────────────────────────────────
    //  VOUCHER / FACTURA DE PAGO — PDF
    // ─────────────────────────────────────────────
    public static void generarVoucherPago(
            int numeroCasa, int mes, int anio,
            double monto, String propietario,
            java.awt.Component parent) {

        File archivo = elegirArchivo(parent,
                "Voucher_Casa" + numeroCasa + "_" + MESES[mes-1] + anio, "pdf");
        if (archivo == null) return;

        try {
            Document doc = new Document(PageSize.A5, 40, 40, 60, 40);
            PdfWriter.getInstance(doc, new FileOutputStream(archivo));
            doc.open();

            Font fTitulo   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, COLOR_VERDE_OSCURO);
            Font fSub      = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, COLOR_VERDE_OSCURO);
            Font fEtiqueta = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, new Color(100, 100, 100));
            Font fValor    = FontFactory.getFont(FontFactory.HELVETICA, 11);
            Font fMonto    = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28, COLOR_VERDE_OSCURO);
            Font fPagado   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new Color(0, 150, 0));
            Font fFooter   = FontFactory.getFont(FontFactory.HELVETICA, 8, new Color(150, 150, 150));

            Paragraph titulo = new Paragraph(CONDOMINIO, fTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(titulo);

            Paragraph subtitulo = new Paragraph("RECIBO DE PAGO DE CUOTA", fSub);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            doc.add(subtitulo);

            Paragraph sep = new Paragraph("----------------------------------------------");
            sep.setAlignment(Element.ALIGN_CENTER);
            doc.add(sep);
            doc.add(Chunk.NEWLINE);

            PdfPTable detalles = new PdfPTable(2);
            detalles.setWidthPercentage(100);
            detalles.setWidths(new float[]{1.2f, 2.5f});

            agregarVoucherRow(detalles, "No. de Casa:", "Casa " + numeroCasa, fEtiqueta, fValor);
            agregarVoucherRow(detalles, "Propietario:", propietario, fEtiqueta, fValor);
            agregarVoucherRow(detalles, "Período:", MESES[mes-1] + " " + anio, fEtiqueta, fValor);
            agregarVoucherRow(detalles, "Concepto:", "Cuota de mantenimiento", fEtiqueta, fValor);
            agregarVoucherRow(detalles, "Fecha de pago:",
                    new SimpleDateFormat("dd/MM/yyyy").format(new Date()), fEtiqueta, fValor);
            doc.add(detalles);

            doc.add(Chunk.NEWLINE);

            Paragraph montoP = new Paragraph(String.format("Q %,.2f", monto), fMonto);
            montoP.setAlignment(Element.ALIGN_CENTER);
            doc.add(montoP);

            Paragraph pagadoP = new Paragraph("PAGADO", fPagado);
            pagadoP.setAlignment(Element.ALIGN_CENTER);
            doc.add(pagadoP);

            doc.add(Chunk.NEWLINE);
            Paragraph sep2 = new Paragraph("----------------------------------------------");
            sep2.setAlignment(Element.ALIGN_CENTER);
            doc.add(sep2);

            Paragraph footer = new Paragraph(
                    "Este documento es un comprobante de pago oficial del Condominio Vista Verde.", fFooter);
            footer.setAlignment(Element.ALIGN_CENTER);
            doc.add(footer);

            doc.close();
            abrirArchivo(archivo, parent);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent,
                    "Error al generar voucher:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ═══════════════════════════════════════════
    //  MÉTODOS AUXILIARES PRIVADOS
    // ═══════════════════════════════════════════

    private static File elegirArchivo(java.awt.Component parent, String nombre, String ext) {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(nombre + "." + ext));
        String desc = ext.equalsIgnoreCase("pdf") ? "PDF (*.pdf)" : "Excel (*.xlsx)";
        fc.setFileFilter(new FileNameExtensionFilter(desc, ext));
        if (fc.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) return null;
        File f = fc.getSelectedFile();
        if (!f.getName().toLowerCase().endsWith("." + ext))
            f = new File(f.getPath() + "." + ext);
        return f;
    }

    private static void abrirArchivo(File f, java.awt.Component parent) {
        JOptionPane.showMessageDialog(parent,
                "Archivo guardado:\n" + f.getName(), "Exportado", JOptionPane.INFORMATION_MESSAGE);
        try {
            if (Desktop.isDesktopSupported()) Desktop.getDesktop().open(f);
        } catch (Exception ignored) {}
    }

    private static String fechaActual() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
    }

    // ─── PDF helpers ───

    private static void agregarEncabezado(Document doc, String titulo, String subtitulo,
                                           Color color) throws DocumentException {
        Font fCondominio = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, color);
        Font fTitulo     = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, color);
        Font fSub        = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(100, 100, 100));

        Paragraph p1 = new Paragraph(CONDOMINIO, fCondominio);
        p1.setAlignment(Element.ALIGN_CENTER);
        doc.add(p1);

        Paragraph p2 = new Paragraph(titulo, fTitulo);
        p2.setAlignment(Element.ALIGN_CENTER);
        doc.add(p2);

        Paragraph p3 = new Paragraph(subtitulo + "   |   Generado: " + fechaActual(), fSub);
        p3.setAlignment(Element.ALIGN_CENTER);
        doc.add(p3);
        doc.add(Chunk.NEWLINE);
    }

    private static PdfPTable crearTablaPDF(String[] cols, float[] widths,
                                            Color colorHeader) throws DocumentException {
        PdfPTable t = new PdfPTable(cols.length);
        t.setWidthPercentage(100);
        t.setWidths(widths);
        Font fH = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, Color.WHITE);
        for (String col : cols) {
            PdfPCell c = new PdfPCell(new Phrase(col, fH));
            c.setBackgroundColor(colorHeader);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            c.setPadding(5);
            t.addCell(c);
        }
        return t;
    }

    private static PdfPCell celdaDato(String texto, Color bg) {
        Font f = FontFactory.getFont(FontFactory.HELVETICA, 9);
        PdfPCell c = new PdfPCell(new Phrase(texto, f));
        c.setBackgroundColor(bg);
        c.setPadding(4);
        return c;
    }

    private static void agregarResumenPDF(Document doc, String[] etiquetas, double[] valores,
                                           Color color) throws DocumentException {
        Font fEt  = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, color);
        Font fVal = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        PdfPTable t = new PdfPTable(etiquetas.length);
        t.setWidthPercentage(80);
        t.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Color bgResumen = new Color(235, 248, 229);
        for (String et : etiquetas) {
            PdfPCell c = new PdfPCell(new Phrase(et, fEt));
            c.setBackgroundColor(bgResumen);
            c.setPadding(6);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            t.addCell(c);
        }
        for (double val : valores) {
            String texto = (val == Math.floor(val) && val < 1000)
                    ? String.valueOf((int) val)
                    : String.format("Q %,.2f", val);
            PdfPCell c = new PdfPCell(new Phrase(texto, fVal));
            c.setPadding(6);
            c.setHorizontalAlignment(Element.ALIGN_CENTER);
            t.addCell(c);
        }
        doc.add(t);
    }

    private static PdfPCell celdaTituloLateral(String texto, Color color) {
        Font f = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);
        PdfPCell c = new PdfPCell(new Phrase(texto, f));
        c.setBackgroundColor(color);
        c.setPadding(6);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        return c;
    }

    private static PdfPCell celdaSubtabla(java.util.List<Object[]> filas) {
        Font f = FontFactory.getFont(FontFactory.HELVETICA, 9);
        PdfPTable sub = new PdfPTable(3);
        if (filas.isEmpty()) {
            PdfPCell empty = new PdfPCell(new Phrase("(ninguno)", f));
            empty.setColspan(3);
            empty.setPadding(4);
            sub.addCell(empty);
        } else {
            for (Object[] fila : filas) {
                for (Object val : fila) {
                    PdfPCell c = new PdfPCell(new Phrase(val != null ? val.toString() : "", f));
                    c.setPadding(3);
                    sub.addCell(c);
                }
            }
        }
        PdfPCell contenedor = new PdfPCell(sub);
        contenedor.setPadding(2);
        return contenedor;
    }

    private static void agregarInfoRow(PdfPTable t, String etiqueta, String valor,
                                        Font fEt, Font fVal) {
        PdfPCell ce = new PdfPCell(new Phrase(etiqueta, fEt));
        ce.setBorderColor(new Color(220, 220, 220));
        ce.setPadding(4);
        t.addCell(ce);
        PdfPCell cv = new PdfPCell(new Phrase(valor, fVal));
        cv.setBorderColor(new Color(220, 220, 220));
        cv.setPadding(4);
        t.addCell(cv);
    }

    private static void agregarVoucherRow(PdfPTable t, String etiqueta, String valor,
                                           Font fEt, Font fVal) {
        PdfPCell ce = new PdfPCell(new Phrase(etiqueta, fEt));
        ce.setBorder(0);
        ce.setPadding(5);
        t.addCell(ce);
        PdfPCell cv = new PdfPCell(new Phrase(valor, fVal));
        cv.setBorder(0);
        cv.setPadding(5);
        t.addCell(cv);
    }

    private static double calcularTotal(java.util.List<Object[]> pagados) {
        double total = 0;
        for (Object[] fila : pagados) {
            if (fila.length > 2 && fila[2] != null) {
                try {
                    String s = fila[2].toString().replace("Q", "").replace(",", "").trim();
                    total += Double.parseDouble(s);
                } catch (NumberFormatException ignored) {}
            }
        }
        return total;
    }

    // ─── Excel helpers ───

    private static CellStyle estiloTituloExcel(XSSFWorkbook wb, byte r, byte g, byte b) {
        XSSFCellStyle s = wb.createCellStyle();
        XSSFFont f = wb.createFont();
        f.setBold(true);
        f.setFontHeightInPoints((short) 13);
        f.setColor(new XSSFColor(new byte[]{r, g, b}, new DefaultIndexedColorMap()));
        s.setFont(f);
        return s;
    }

    private static CellStyle estiloHeaderExcel(XSSFWorkbook wb, byte r, byte g, byte b) {
        XSSFCellStyle s = wb.createCellStyle();
        s.setFillForegroundColor(new XSSFColor(new byte[]{r, g, b}, new DefaultIndexedColorMap()));
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        s.setAlignment(HorizontalAlignment.CENTER);
        s.setBorderBottom(BorderStyle.THIN);
        XSSFFont f = wb.createFont();
        f.setBold(true);
        f.setColor(IndexedColors.WHITE.getIndex());
        s.setFont(f);
        return s;
    }

    private static CellStyle estiloAltExcel(XSSFWorkbook wb, byte r, byte g, byte b) {
        XSSFCellStyle s = wb.createCellStyle();
        s.setFillForegroundColor(new XSSFColor(new byte[]{r, g, b}, new DefaultIndexedColorMap()));
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return s;
    }

    private static CellStyle estiloResumenExcel(XSSFWorkbook wb, byte r, byte g, byte b) {
        XSSFCellStyle s = wb.createCellStyle();
        s.setFillForegroundColor(new XSSFColor(new byte[]{r, g, b}, new DefaultIndexedColorMap()));
        s.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont f = wb.createFont();
        f.setBold(true);
        s.setFont(f);
        s.setAlignment(HorizontalAlignment.CENTER);
        return s;
    }

    private static void agregarFilaTitulo(Sheet sheet, int rowIdx, String texto,
                                           CellStyle estilo, int span) {
        Row fila = sheet.createRow(rowIdx);
        Cell celda = fila.createCell(0);
        celda.setCellValue(texto);
        celda.setCellStyle(estilo);
        sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rowIdx, rowIdx, 0, span - 1));
    }

    private static void agregarFilaHeader(Sheet sheet, int rowIdx, String[] cols, CellStyle estilo) {
        Row fila = sheet.createRow(rowIdx);
        for (int i = 0; i < cols.length; i++) {
            Cell c = fila.createCell(i);
            c.setCellValue(cols[i]);
            c.setCellStyle(estilo);
        }
    }

    private static void agregarFilaResumenExcel(Sheet sheet, int rowIdx, String[] etiquetas,
                                                  double[] valores, CellStyle estilo) {
        Row filaEt = sheet.createRow(rowIdx);
        Row filaVal = sheet.createRow(rowIdx + 1);
        for (int i = 0; i < etiquetas.length; i++) {
            Cell ce = filaEt.createCell(i);
            ce.setCellValue(etiquetas[i]);
            ce.setCellStyle(estilo);

            Cell cv = filaVal.createCell(i);
            if (valores[i] == Math.floor(valores[i]) && valores[i] < 1000)
                cv.setCellValue((int) valores[i]);
            else
                cv.setCellValue(String.format("Q %,.2f", valores[i]));
            cv.setCellStyle(estilo);
        }
    }
}
