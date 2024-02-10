
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

    private static final DecimalFormat formatoDecimal = new DecimalFormat("#,##0.00");

    public static void main(String[] args) {
        String texto = "150.0";
        if (texto.contains(",")) {
            texto = texto.replace(".", "").replace(",", ".");
        }
        double numero = Double.parseDouble(texto);
        String texto2 = String.format("%.2f",numero);
        try {
            System.out.println("Como se Formatea Final mente " + formatoDecimal.format(formatoDecimal.parse(texto2.replace(".","")).doubleValue()));
        } catch (ParseException ex) {
            Logger.getLogger(Pruebas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
