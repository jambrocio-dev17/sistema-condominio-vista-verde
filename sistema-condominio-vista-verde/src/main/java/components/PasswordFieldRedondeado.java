package components;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PasswordFieldRedondeado extends JPasswordField {
    private int radius;
    private String placeholder;
    private Color colorPlaceholder = new Color(153, 153, 153);
    private char echoCharOriginal; // Guarda el caracter de censura por defecto (suele ser • o *)

    public PasswordFieldRedondeado(int radius, String placeholder) {
        this.radius = radius;
        this.placeholder = placeholder;
        this.echoCharOriginal = getEchoChar();
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 40)); // Margen derecho más grande para dejar espacio al ícono del ojo
        
        // Estado inicial (mostrando el texto gris sin censura)
        setEchoChar((char) 0); 
        setForeground(colorPlaceholder);
        setText(placeholder);

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(getPassword()).equals(placeholder)) {
                    setText("");
                    setForeground(Color.BLACK);
                    setEchoChar(echoCharOriginal); // Activa la censura
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (String.valueOf(getPassword()).isEmpty()) {
                    setEchoChar((char) 0); // Quita la censura
                    setForeground(colorPlaceholder);
                    setText(placeholder);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.GRAY);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
        g2.dispose();
    }
}