
import controladores.ControladorVistaCargaCombustible;
import controladores.ControladorVistaMorros;
import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.Objeto;
import entidades.Usuario;
import javax.swing.JFrame;
import vistas.VistaCargaCombustible;
import vistas.VistaCargaMorros;
import vistas.VistaMorros;
import vistas.VistaPantallaPrincipal;

public class PruebaVista {
    static VistaPantallaPrincipal menu = new VistaPantallaPrincipal();
    static ComandosComunes comandos = new ComandosComunes();
    static Controladora ctrl = new Controladora();
    static Usuario usuario = ctrl.traerUsuario(2);
    
    
    public static void main(String[] args) {
        menu.setExtendedState(JFrame.MAXIMIZED_BOTH);
        menu.setTitle("Sistema de Gestion -- Bienvenido " + usuario.getNombre());
        menu.setVisible(true);
        
        /*
        Objeto obj = ctrl.traerObjeto(6);
        VistaMorros vista = new VistaMorros();
        ControladorVistaMorros control= new ControladorVistaMorros(menu, vista, usuario, obj);
        */
        
        Objeto obj = ctrl.traerObjeto(8);
        VistaCargaCombustible vista = new VistaCargaCombustible();
        ControladorVistaCargaCombustible control= new ControladorVistaCargaCombustible(menu, vista, usuario, obj);
        control.iniciar();
        
        /*
        VistaCargaMorros vista = new VistaCargaMorros();
        menu.fondo.add(vista);
        vista.setVisible(true);
        menu.fondo.moveToFront(vista);
        vista.requestFocus();
        */
    }
    
}
