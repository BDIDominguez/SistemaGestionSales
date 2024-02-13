
import entidades.Camion;
import entidades.Controladora;
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
        Controladora ctrl = new Controladora();
        Camion camion = ctrl.traerCamion(2);
        System.out.println("Odometro del Camion " + camion.getOdometro());
        System.out.println("Teorico del Camion " + camion.getTeorico());
        camion.setOdometro(250.00);
        camion.setTeorico(250.00);
        ctrl.editarCamion(camion);
        System.out.println("Se Cambiaron los Datos!!");
    }
}
