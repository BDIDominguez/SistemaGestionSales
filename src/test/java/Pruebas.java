
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
        String cadena = "1550";
        cadena = cadena.replace(":","");
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
        System.out.println("Hora sin Formato: " + cadena);
    }
}
