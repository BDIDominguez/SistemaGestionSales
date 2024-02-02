package abl.sistemagestion;

import entidades.ComandosComunes;
import controladores.ControladorVistaInicioSesion;
import entidades.Controladora;
import entidades.Usuario;
import java.util.List;
import vistas.VistaInicioSesion;

public class SistemaGestion {

    public static void main(String[] args) {

        Controladora persistencia = new Controladora();
        List<Usuario> lista = persistencia.traerListaUsuarios();
        if (lista.size() <= 0){
            ComandosComunes comandos = new ComandosComunes();
            comandos.primerInicio();
            comandos.crearObjetos();
        }
        VistaInicioSesion vista = new VistaInicioSesion();
        ControladorVistaInicioSesion ctrl = new ControladorVistaInicioSesion(vista,null);
        ctrl.iniciar();
    }
}
