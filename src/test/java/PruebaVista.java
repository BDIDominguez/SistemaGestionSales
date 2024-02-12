
import controladores.ControladorVistaCamiones;
import controladores.ControladorVistaCargaCombustible;
import controladores.ControladorVistaCargaMorros;
import controladores.ControladorVistaChoferes;
import controladores.ControladorVistaConfirmarEntregas;
import controladores.ControladorVistaMorros;
import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.Objeto;
import entidades.Combustible;
import entidades.Usuario;
import java.time.format.DateTimeFormatter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import vistas.VistaCamiones;
import vistas.VistaCargaCombustible;
import vistas.VistaCargaMorros;
import vistas.VistaChoferes;
import vistas.VistaConfirmarEntregas;
import vistas.VistaMorros;
import vistas.VistaPantallaPrincipal;

public class PruebaVista {
    static VistaPantallaPrincipal menu = new VistaPantallaPrincipal();
    static ComandosComunes comandos = new ComandosComunes();
    static Controladora ctrl = new Controladora();
    static Usuario usuario = ctrl.traerUsuario(1);
    
    
    public static void main(String[] args) {
        menu.setExtendedState(JFrame.MAXIMIZED_BOTH);
        menu.setTitle("Sistema de Gestion -- Bienvenido " + usuario.getNombre());
        menu.setVisible(true);
        
        /*
        Objeto obj = ctrl.traerObjeto(6);
        VistaMorros vista = new VistaMorros();
        ControladorVistaMorros control= new ControladorVistaMorros(menu, vista, usuario, obj);
        */
        
        /*
        Objeto obj = ctrl.traerObjeto(8);
        VistaCargaCombustible vista = new VistaCargaCombustible();
        ControladorVistaCargaCombustible control= new ControladorVistaCargaCombustible(menu, vista, usuario, obj);
        control.iniciar();
        */
        
        /*
        VistaCargaMorros vista = new VistaCargaMorros();
        menu.fondo.add(vista);
        vista.setVisible(true);
        menu.fondo.moveToFront(vista);
        vista.requestFocus();
        */
        /*
        Objeto obj = ctrl.traerObjeto(9);
        VistaCargaMorros vista = new VistaCargaMorros();
        ControladorVistaCargaMorros control = new ControladorVistaCargaMorros(menu, vista, comandos,usuario,ctrl,obj);
        control.iniciar();
        */
        /*
        Objeto obj = ctrl.traerObjeto(10);
        VistaConfirmarEntregas vista = new VistaConfirmarEntregas();
        ControladorVistaConfirmarEntregas control = new ControladorVistaConfirmarEntregas(menu, vista, comandos, usuario ,ctrl, obj);
        control.iniciar();
        */
        /*
        Objeto obj = ctrl.traerObjeto(11);
        VistaCamiones vista = new VistaCamiones();
        ControladorVistaCamiones control = new ControladorVistaCamiones(menu, vista, comandos, usuario ,ctrl, obj);
        control.iniciar();
        */
        
        Objeto obj = ctrl.traerObjeto(12);
        VistaChoferes vista = new VistaChoferes();
        ControladorVistaChoferes control = new ControladorVistaChoferes(menu, vista, comandos, usuario ,ctrl, obj);
        control.iniciar();
        
    }
    
}
