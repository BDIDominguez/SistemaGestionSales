package Utilidades;

import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class MiCampoSerie extends JTextField {
   int cantidadDigitos;

    public MiCampoSerie(int cantidadDigitos) {
        super();
        setFont(new Font("Arial", Font.BOLD, 14));  // Fuente bold, tamaño 14
        this.cantidadDigitos = cantidadDigitos;

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
       if (cadena.length()<cantidadDigitos){
           int cantidadDeCeros = cantidadDigitos - cadena.length();
           cadena = String.format("%0" + cantidadDeCeros + "d%s", 0, cadena);
       }else if (cadena.length() > cantidadDigitos){
           cadena = cadena.substring(0,cantidadDigitos);
       }
       setText(cadena);
    }
}