package controladores;

import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.EnumUsuario;
import entidades.Objeto;
import entidades.Permiso;
import entidades.Usuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import vistas.VistaPantallaPrincipal;
import vistas.VistaPermisos;

public class ControladorVistaPermisos implements ActionListener, ListSelectionListener {

    VistaPantallaPrincipal menu;
    VistaPermisos vista;
    ComandosComunes comandos = new ComandosComunes();
    Usuario usuario;
    Controladora ctrl = new Controladora();
    MyModelo modelo = new MyModelo();
    Objeto obj;
    
    public ControladorVistaPermisos(VistaPantallaPrincipal menu, VistaPermisos vista, Usuario usuario, Objeto obj) {
        this.menu = menu;
        this.vista = vista;
        this.usuario = usuario;
        this.obj = obj;

        vista.btEliminar.addActionListener(this);
        vista.btGuardar.addActionListener(this);
        vista.btSalir.addActionListener(this);

        vista.tabla.getSelectionModel().addListSelectionListener(this);

    }

    public void iniciar() {
        menu.fondo.add(vista);
        vista.setVisible(true);
        menu.fondo.moveToFront(vista);
        vista.requestFocus();
        cargarCbUsuarios();
        modelarTabla();
        cargaTabla();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btSalir) {
            vista.dispose();
        }
        if (e.getSource() == vista.btGuardar) {
            List<Permiso> permisos = ctrl.traerListaPermisos();
            int codigo = 0;
            int fila = vista.tabla.getSelectedRow();
            if (fila != -1) {
                codigo = (int) vista.tabla.getValueAt(fila, 0);
            }
            Objeto obj = ctrl.traerObjeto(codigo);
            Permiso perNuevo = new Permiso();
            Boolean existe = false;
            for (Permiso permiso : permisos) {
                if (permiso.getObjeto().getCodigo() == obj.getCodigo()) {
                    Permiso perOriginal = permiso;
                    permiso.setActualizar(vista.chActualizar.isSelected());
                    permiso.setBorrar(vista.chBorrar.isSelected());
                    permiso.setCrear(vista.chCrear.isSelected());
                    permiso.setImprimir(vista.chImprimir.isSelected());
                    permiso.setIngresar(vista.chIngresar.isSelected());
                    permiso.setLeer(vista.chLeer.isSelected());
                    permiso.setEstado(true);
                    perNuevo = permiso;
                    existe = true;
                    break;
                }
            }
            Objeto obj1 = ctrl.traerObjeto(2);
            if (existe) {
                if (comandos.tienePermiso(usuario, obj1, "Actualizar")) {
                    ctrl.editarPermiso(perNuevo);
                    JOptionPane.showMessageDialog(vista, "Se han Guardado los Cambios");
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes Permisos para Realizar Cambios");
                }
            } else {
                if (comandos.tienePermiso(usuario, obj1, "Crear")) {
                    perNuevo.setCodigo(1);
                    perNuevo.setObjeto(obj);
                    perNuevo.setUsuario(ctrl.traerUsuario(extraerIdUsuario()));
                    perNuevo.setActualizar(vista.chActualizar.isSelected());
                    perNuevo.setBorrar(vista.chBorrar.isSelected());
                    perNuevo.setCrear(vista.chCrear.isSelected());
                    perNuevo.setImprimir(vista.chImprimir.isSelected());
                    perNuevo.setIngresar(vista.chIngresar.isSelected());
                    perNuevo.setLeer(vista.chLeer.isSelected());
                    perNuevo.setEstado(true);
                    ctrl.crearPermiso(perNuevo);
                    JOptionPane.showMessageDialog(vista, "Se han Creado los Nuevos Permisos");
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes Permisos para Dar de Alta Nuevos");
                }
            }
            cambiarEstado(false);
        }
        if (e.getSource() == vista.btEliminar) {
            if (comandos.tienePermiso(usuario, obj, "Eliminar")) {
                int codigo = 0;
                int fila = vista.tabla.getSelectedRow();
                if (fila != -1) {
                   codigo = (int) vista.tabla.getValueAt(fila, 0);
                }
                Objeto obj = ctrl.traerObjeto(codigo);
                List<Permiso> permisos = usuario.getPermiso();
                int codigoPermiso = 0;
                for (Permiso permiso : permisos) {
                    if(permiso.getObjeto().getCodigo() == obj.getCodigo()){
                        codigoPermiso = permiso.getCodigo();
                        break;
                    }
                }
                if (usuario.getTipo() == EnumUsuario.SUPERADMIN){
                    ctrl.eliminarPermiso(codigoPermiso);
                }else{
                    Permiso per = ctrl.traerPermiso(codigoPermiso);
                    per.setEstado(false);
                    ctrl.editarPermiso(per);
                }
                JOptionPane.showMessageDialog(vista, "Se Elimino el Permiso");
            } else {
                JOptionPane.showMessageDialog(vista, "No tienes Permisos Elimiar Datos");
            }
        }

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int fila = vista.tabla.getSelectedRow();
            if (fila != -1) {
                int codigo = (int) vista.tabla.getValueAt(fila, 0);
                motrarPermisos(codigo);
            }
        }
    }

    private void motrarPermisos(int codigo) {
        Usuario us = ctrl.traerUsuario(extraerIdUsuario());
        List<Permiso> lista = us.getPermiso();
        cambiarEstado(Boolean.FALSE);
        for (Permiso permiso : lista) {
            if (permiso.getObjeto().getCodigo() == codigo) {
                vista.chActualizar.setSelected(permiso.getActualizar());
                vista.chBorrar.setSelected(permiso.getBorrar());
                vista.chCrear.setSelected(permiso.getCrear());
                vista.chImprimir.setSelected(permiso.getImprimir());
                vista.chIngresar.setSelected(permiso.getIngresar());
                vista.chLeer.setSelected(permiso.getLeer());
                break;
            }
        }
    }    // Metodos Genericos para esta Vista

    private void cambiarEstado(Boolean cambio) {
        vista.chActualizar.setSelected(cambio);
        vista.chBorrar.setSelected(cambio);
        vista.chCrear.setSelected(cambio);
        vista.chImprimir.setSelected(cambio);
        vista.chIngresar.setSelected(cambio);
        vista.chLeer.setSelected(cambio);
    }

    private class MyModelo extends DefaultTableModel {

        //para evitar las edicion de los campos de la tabla pero que se puedan seleccionar
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    private void cargarCbUsuarios() {
        List<Usuario> lista = ctrl.traerListaUsuarios();
        lista.stream().sorted(Comparator.comparing(Usuario::getNombre)).collect(Collectors.toList());
        vista.cbUsuarios.removeAllItems();
        for (Usuario us : lista) {
            if (us.getEstado()) {
                String cadena = us.getNombre() + " - " + us.getCodigo();
                vista.cbUsuarios.addItem(cadena);
            }
        }
    }

    private void modelarTabla() {
        modelo.addColumn("Codigo");
        modelo.addColumn("Nombre");
        vista.tabla.setModel(modelo);
        vista.tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        vista.tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
        vista.tabla.getColumnModel().getColumn(1).setPreferredWidth(500);

    }

    public void cargaTabla() {
        List<Objeto> lista = ctrl.traerListaObjetos();
        modelo.setRowCount(0);
        for (Objeto objeto : lista) {
            if (objeto.getEstado()) {
                modelo.addRow(new Object[]{objeto.getCodigo(), objeto.getNombre()});
            }
        }
        vista.tabla.setModel(modelo);
    }

    private int extraerIdUsuario() {
        int id = -1;
        try {
            String combobox = vista.cbUsuarios.getSelectedItem().toString();
            String partes[] = combobox.split("-");
            id = Integer.parseInt(partes[1].trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "A ocurrido un error al cargar los indices en el combobox, revices la posicion del idMateria");
        }
        return id;
    }

}
