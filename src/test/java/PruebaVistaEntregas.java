
import controladores.ControladorVistaEntregas;
import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.Objeto;
import entidades.Usuario;
import javax.swing.JFrame;
import vistas.VistaEntregas;
import vistas.VistaPantallaPrincipal;

public class PruebaVistaEntregas {
    static VistaPantallaPrincipal menu = new VistaPantallaPrincipal();
    static ComandosComunes comandos = new ComandosComunes();
    static Controladora ctrl = new Controladora();
    static Usuario usuario = ctrl.traerUsuario(2);
    
    
    public static void main(String[] args) {
       
        menu.setExtendedState(JFrame.MAXIMIZED_BOTH);
        menu.setTitle("Sistema de Gestion -- Bienvenido " + usuario.getNombre());
        menu.setVisible(true);
        Objeto obj = ctrl.traerObjeto(7);
        VistaEntregas vista = new VistaEntregas();
        ControladorVistaEntregas control= new ControladorVistaEntregas(menu, vista, usuario, obj);
        control.iniciar();
        
    }

    
}
