
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;





/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Dario
 */
public class Pruebas {
    public static void main(String[] args) {
       String sucursal;
        String numero;
        String cadena = "2-56789";
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
        }else{
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
        System.out.println("Formato de Hora: " + cadena);
    }
}
