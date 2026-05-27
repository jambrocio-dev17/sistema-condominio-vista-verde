package logic;

import java.util.prefs.Preferences;

public class ThemeManager {

    private static final String PREF_KEY = "flatlaf_theme";
    private static final Preferences PREFS =
            Preferences.userRoot().node("condominio_vista_verde");

    public static final String TEMA_CLARO     = "FlatLightLaf";
    public static final String TEMA_OSCURO    = "FlatDarkLaf";
    public static final String TEMA_INTELLIJ  = "FlatIntelliJLaf";
    public static final String TEMA_DARCULA   = "FlatDarculaLaf";

    private static final String[] NOMBRES = {
        "Claro  (FlatLight)",
        "Oscuro (FlatDark)",
        "IntelliJ",
        "Darcula"
    };
    private static final String[] CLASES = {
        TEMA_CLARO, TEMA_OSCURO, TEMA_INTELLIJ, TEMA_DARCULA
    };

    /** Aplica el tema guardado; si no hay ninguno aplica el tema claro. */
    public static void aplicarTema() {
        String tema = PREFS.get(PREF_KEY, TEMA_CLARO);
        aplicarTema(tema);
    }

    /** Aplica el tema indicado y lo persiste. */
    public static void aplicarTema(String clasesTema) {
        try {
            switch (clasesTema) {
                case TEMA_OSCURO   -> com.formdev.flatlaf.FlatDarkLaf.setup();
                case TEMA_INTELLIJ -> com.formdev.flatlaf.FlatIntelliJLaf.setup();
                case TEMA_DARCULA  -> com.formdev.flatlaf.FlatDarculaLaf.setup();
                default            -> com.formdev.flatlaf.FlatLightLaf.setup();
            }
            PREFS.put(PREF_KEY, clasesTema);
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el tema FlatLaf: " + e.getMessage());
        }
    }

    /**
     * Muestra un diálogo para seleccionar el tema.
     * Tras aceptar, actualiza todos los componentes abiertos.
     */
    public static void mostrarSelectorTema(java.awt.Component parent) {
        String temaActual = PREFS.get(PREF_KEY, TEMA_CLARO);
        int idxActual = 0;
        for (int i = 0; i < CLASES.length; i++) {
            if (CLASES[i].equals(temaActual)) { idxActual = i; break; }
        }

        javax.swing.JComboBox<String> combo = new javax.swing.JComboBox<>(NOMBRES);
        combo.setSelectedIndex(idxActual);

        Object[] mensaje = {"Seleccione el tema de la aplicación:", combo};
        int res = javax.swing.JOptionPane.showConfirmDialog(
                parent, mensaje, "Cambiar Tema",
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE);

        if (res == javax.swing.JOptionPane.OK_OPTION) {
            aplicarTema(CLASES[combo.getSelectedIndex()]);
            refrescarVentanas();
        }
    }

    /** Refresca todos los componentes Swing con el nuevo Look & Feel. */
    private static void refrescarVentanas() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            for (java.awt.Window w : java.awt.Window.getWindows()) {
                javax.swing.SwingUtilities.updateComponentTreeUI(w);
                w.repaint();
            }
        });
    }
}
