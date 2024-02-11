package Utilidades;

import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class MiCampNroFactura extends JTextField {

    public MiCampNroFactura() {
        super();
        setFont(new Font("Arial", Font.BOLD, 14));  // Fuente bold, tamaño 14
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                // Verificar si es un dígito, retroceso o eliminar y un solo -.
                if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || (c == '-' && !getText().contains("-")))) {
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
        String sucursal;
        String numero;
        String cadena = getText();
        if (cadena.contains("-")) {
            String[] comp = cadena.split("-");
            sucursal = comp[0];
            int cantidadDeCeros = 4 - sucursal.length();
            if (cantidadDeCeros > 0) {
                sucursal = String.format("%0" + cantidadDeCeros + "d%s", 0, sucursal);
            } else if (cantidadDeCeros < 0) {
                sucursal = sucursal.substring(0, 3);
            }
            numero = comp[1];
            cantidadDeCeros = 8 - numero.length();
            if (cantidadDeCeros > 0) {
                numero = String.format("%0" + cantidadDeCeros + "d%s", 0, numero);
            } else if (cantidadDeCeros < 0) {
                numero = numero.substring(0, 8);
            }
            cadena = sucursal + "-" + numero;
        } else {
            sucursal = "0001";
            int cantidadDeCeros = 8 - cadena.length();
            if (cantidadDeCeros > 0) {
                numero = String.format("%0" + cantidadDeCeros + "d%s", 0, cadena);
            } else if (cantidadDeCeros < 0) {
                numero = cadena.substring(0, 8);
            } else {
                numero = cadena;
            }
            cadena = sucursal + "-" + numero;
        }
        setText(cadena);
    }
}
