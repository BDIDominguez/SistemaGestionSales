package Utilidades;

import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JTextField;

public class MiCampoHora extends JTextField {

    public MiCampoHora() {
        super();
        setFont(new Font("Arial", Font.BOLD, 14));  // Fuente bold, tamaño 14
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
        String cadena = getText().replace(":", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
        if (cadena.length() == 1){
            cadena = "0" + cadena;
        }
        int cantidad = 6 - cadena.length();
        if (cantidad > 0){
            cadena = String.format("%s%0" + cantidad + "d", cadena, 0);
        }else if(cantidad < 0){
            cadena = cadena.substring(0,6);
        }
        String hora = cadena.substring(0,2);
        String minutos = cadena.substring(2,4);
        String segundos = cadena.substring(4,6);
        if (Integer.parseInt(hora) > 24){
            hora = "00";
        }
        if (Integer.parseInt(minutos) > 59 || hora.equalsIgnoreCase("24")){
            minutos = "00";
        }
        if (Integer.parseInt(segundos)> 59 || hora.equalsIgnoreCase("24")){
            segundos = "00";
        }
        cadena = hora + minutos + segundos;
        LocalTime horaf = LocalTime.parse(cadena, formatter);
        setText(horaf.format(DateTimeFormatter.ofPattern("HH:mm:ss")));

    }
}
