package Utilidades;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import javax.swing.JTextField;

public class MiCampoEntero extends JTextField {
    private static final DecimalFormat formatoEntero = new DecimalFormat("#,##0");

    public MiCampoEntero() {
        super();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                // Verificar si es un dígito, retroceso o eliminar
                if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                formatearTexto();
            }

            @Override
            public void focusGained(FocusEvent e) {
                selectAll();
            }
        });

        setTransferHandler(null);
    }

    private void formatearTexto() {
        try {
            int numero = Integer.parseInt(getText().replace(".", ""));
            setText(formatoEntero.format(numero));
        } catch (NumberFormatException ex) {
            // Manejar la excepción si el texto no es un número válido
            setText("0"); // O manejar de otra manera según tus necesidades
        }
    }
}