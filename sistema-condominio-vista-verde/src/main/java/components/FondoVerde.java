package components;

import javax.swing.JPanel;
import java.awt.*;

public class FondoVerde extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo verde oscuro
        g2.setColor(new Color(26, 58, 10));
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Círculo grande arriba a la derecha
        g2.setColor(new Color(45, 90, 20));
        g2.fillOval(getWidth() - 120, -60, 180, 180);

        // Círculo mediano abajo a la izquierda
        g2.setColor(new Color(45, 90, 20));
        g2.fillOval(-60, getHeight() - 120, 180, 180);

        // Círculo pequeño abajo a la derecha
        g2.setColor(new Color(35, 75, 15));
        g2.fillOval(getWidth() - 80, getHeight() - 80, 120, 120);
    }
}