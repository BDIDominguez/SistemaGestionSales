package entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import lombok.Data;

@Data
public class ComandosComunes {

    Controladora ctrl = new Controladora();

    public ComandosComunes() {

    }

    public void primerInicio() {
        Usuario us = ctrl.traerUsuario(1);
        if (us == null) {
            JOptionPane.showMessageDialog(null, "Se pediran el nombre de Usuario y Clave del Administrador no los pierdas!!");
            List<Permiso> lista = new ArrayList<>();
            Usuario user = new Usuario(1, JOptionPane.showInputDialog("Ingresa un nombre para el Usuario Principal"), JOptionPane.showInputDialog("Ingrese Clave"), EnumUsuario.SUPERADMIN, Boolean.TRUE, lista);
            ctrl.crearUsuario(user);
        }
    }

    public void crearObjetos() { // Crea los Objetos que necesita el Sistema
        List<Objeto> objetos = new ArrayList<>();
        List<Permiso> permisos = new ArrayList<>();
        objetos.add(new Objeto(1, "Pantalla de Usuarios", Boolean.TRUE, permisos));
        objetos.add(new Objeto(2, "Pantalla de Permisos", Boolean.TRUE, permisos));
        objetos.add(new Objeto(3, "Pantalla de Objetos", Boolean.TRUE, permisos));
        objetos.add(new Objeto(4, "Pantalla de Inicio de Sesion", Boolean.TRUE, permisos));
        objetos.add(new Objeto(5, "Pantalla de Proveedores", Boolean.TRUE, permisos));
        objetos.add(new Objeto(6, "Pantalla de Morros", Boolean.TRUE, permisos));
        objetos.add(new Objeto(7, "Pantalla de Entregas", Boolean.TRUE, permisos));
        objetos.add(new Objeto(8, "Pantalla de Cargas de Combustible", Boolean.TRUE, permisos));
        List<Objeto> existentes = ctrl.traerListaObjetos();
        for (Objeto objeto : objetos) {
            if (!existentes.contains(objeto)) {
                ctrl.crearObjeto(objeto);
            }
        }
    }

    public boolean tienePermiso(Usuario usuario, Objeto obj, String cual) { //llamado por el cual sabremos si el usuario puede o no hacer alguna opcion
        boolean tiene = false;
        // Se Agrega Funcion de Seguridad para que el computador siempre tenga la fecha del servidor no se permitira que hagan trampa!!!
        if (ctrl.consultaFechaServidor().isEqual(LocalDate.now())) { // si la fecha es la misma se pasa a comprobar los permisos!!
            // Se comprueban los permisos!!
            if (usuario.getTipo() == EnumUsuario.SUPERADMIN) { // obviamente si es nivel DIOS no se pregunta se obedece!!!
                tiene = true;
            } else {
                cual = cual.toUpperCase();
                List<Permiso> lista = usuario.getPermiso();
                Permiso per = new Permiso();
                per.setEstado(false);
                for (Permiso permiso : lista) {
                    if (permiso.getObjeto().getCodigo() == obj.getCodigo()) {
                        per = permiso;
                        break;
                    }
                }
                if (per.getEstado()) {
                    switch (cual) {
                        case "IMPRIMIR":
                            tiene = per.getImprimir();
                            break;
                        case "ACTUALIZAR":
                            tiene = per.getActualizar();
                            break;
                        case "BORRAR":
                            tiene = per.getBorrar();
                            break;
                        case "CREAR":
                            tiene = per.getCrear();
                            break;
                        case "INGRESAR":
                            tiene = per.getIngresar();
                            break;
                        case "LEER":
                            tiene = per.getLeer();
                            break;
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Actualice la fecha del sistema!!!");
            tiene = false;
        }
        return tiene;
    }
}
