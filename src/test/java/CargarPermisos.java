
import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.Objeto;
import entidades.Permiso;
import entidades.Usuario;
import java.util.List;

public class CargarPermisos {

    static ComandosComunes comandos = new ComandosComunes();
    static Controladora ctrl = new Controladora();

    public static void main(String[] args) {
        List<Objeto> a = ctrl.traerListaObjetos();
        List<Permiso> p = ctrl.traerListaPermisos();
        Usuario us = ctrl.traerUsuario(2);
        for (Objeto objeto : a) {
            System.out.println("Consultando permiso para " + objeto.getNombre());
            boolean cargar = true;
            for (Permiso permiso : p) {
                if (permiso.getObjeto().getCodigo() == objeto.getCodigo() && permiso.getUsuario().getCodigo() == 2) {
                    cargar = false;
                }
            }
            if (cargar){
                Permiso per = new Permiso(1, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, us, objeto);
                System.out.println("Creando permiso para objeto " + per.getObjeto().getNombre());
                ctrl.crearPermiso(per);
            }
        }
    }
}
