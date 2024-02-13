package Utilidades;

import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MiCampoCUIT extends JTextField {
    private static final DecimalFormat formatoEntero = new DecimalFormat("#,##0");

    public MiCampoCUIT() {
        super();
        super.setText("00-00000000-0");
        setFont(new Font("Arial", Font.BOLD, 14));  // Fuente bold, tamaño 14
        setHorizontalAlignment(SwingConstants.RIGHT);

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
        String cadena = getText();
        if (cadena.contains("-") && cadena.length() == 13){
            setText(cadena);
        }else if (cadena.length() == 11){
            cadena = cadena.substring(0,2) + "-" + cadena.substring(2,10) + "-" + cadena.substring(10,11);
            setText(cadena);
        }else{
            setText(cadena);
        }
            
    }
}