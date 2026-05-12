package ui;

import javax.swing.*;
import java.awt.*;

public class PanelSoloBorde extends JPanel {
    private int cornerRadius;
    private Color colorBorde;
    private int grosorBorde;

    // Constructor donde puedes definir el radio, el color y el grosor de una vez
    public PanelSoloBorde(int radius, Color color, int grosor) {
        super();
        this.cornerRadius = radius;
        this.colorBorde = color;
        this.grosorBorde = grosor;
        // Obligatorio para que el interior sea totalmente transparente
        setOpaque(false); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension arcs = new Dimension(cornerRadius, cornerRadius);
        int width = getWidth();
        int height = getHeight();
        Graphics2D graphics = (Graphics2D) g;
        
        // Activar el antialiasing para bordes suaves
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // SOLO DIBUJAMOS EL PERÍMETRO (No hay fillRoundRect)
        graphics.setColor(colorBorde); 
        graphics.setStroke(new BasicStroke(grosorBorde)); 
        
        // Un pequeño truco matemático: desplazamos el dibujo la mitad del grosor 
        // para que la línea no se corte en los extremos del panel
        int x = grosorBorde / 2;
        int y = grosorBorde / 2;
        int w = width - grosorBorde;
        int h = height - grosorBorde;
        
        graphics.drawRoundRect(x, y, w, h, arcs.width, arcs.height);
    }
}