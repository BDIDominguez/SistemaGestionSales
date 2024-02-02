package Utilidades;

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
        if (cadena.equals("") || cadena.equals("000000")) {
            cadena = "00:00:00";
        } else if (cadena.length() == 1) {
            cadena = "0" + cadena;
            int cantidad = 6 - cadena.length();
            cadena = String.format("%s%0" + cantidad + "d", cadena, 0);
        } else if (cadena.length() > 1) {
            if (cadena.substring(0, 1).equals("1")) {
                int cantidad = 6 - cadena.length();
                if (cantidad > 0){
                    cadena = String.format("%s%0" + cantidad + "d", cadena, 0);
                }
            } else if (Integer.parseInt(cadena.substring(0, 2)) > 24) {
                int numero = Integer.parseInt(cadena.substring(0, 2));
                numero = numero - 24;
                if (numero > 24){
                    numero = 24;
                }
                cadena = numero + cadena.substring(2, cadena.length());
                int cantidad = 6 - cadena.length();
                if (cadena.length() < 6) {
                    cadena = String.format("%s%0" + cantidad + "d", cadena, 0);
                }
            } else {
                if (cadena.length() < 6) {
                    int cantidad = 6 - cadena.length();
                    cadena = String.format("%s%0" + cantidad + "d", cadena, 0);
                    //cadena = String.format("%s%0" + cantidad + "d", cadena);
                    //cadena = String.format("%0" + cantidad + "s", cadena);
                }
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
        // Controlar los minutos y los segundos
        cadena = cadena.replaceAll(":", "");
        String horas = cadena.substring(0,2);
        String minutos = cadena.substring(2,4);
        String segundos = cadena.substring(4,6);
        if (Integer.parseInt(minutos)>59 && Integer.parseInt(horas) < 24){
            minutos = "59";
        }else{
            minutos = "00";
        }
        if (Integer.parseInt(segundos)>59 && Integer.parseInt(horas) < 24){
            segundos = "59";
        }else{
            segundos = "00";
        }
        cadena = cadena.substring(0,2) + minutos + segundos;
        LocalTime horaf = LocalTime.parse(cadena, formatter);
        setText(horaf.format(DateTimeFormatter.ofPattern("HH:mm:ss")));

    }
}
