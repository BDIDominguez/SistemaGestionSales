package controladores;

import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.EnumUsuario;
import entidades.Objeto;
import entidades.Permiso;
import entidades.Usuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import vistas.VistaPantallaPrincipal;
import vistas.VistaUsuarios;

/**
 * @author Dario
 */
public class ControladorVistaUsuarios implements ActionListener, ListSelectionListener, FocusListener, KeyListener {

    VistaPantallaPrincipal menu;
    VistaUsuarios vista;
    MyModelo modelo = new MyModelo();
    Controladora ctrl = new Controladora();
    ComandosComunes comandos = new ComandosComunes();
    Usuario usuario;
    Objeto obj;

    public ControladorVistaUsuarios(VistaPantallaPrincipal menu, VistaUsuarios vista, Usuario usuario,Objeto obj) {
        this.menu = menu;
        this.vista = vista;
        this.usuario = usuario;
        this.obj = obj;

        // Botones
        vista.btNuevo.addActionListener(this);
        vista.btGuardar.addActionListener(this);
        vista.btEliminar.addActionListener(this);
        vista.btSalir.addActionListener(this);

        // Text
        vista.txCodigo.addActionListener(this);
        vista.txNombre.addActionListener(this);
        vista.txClave.addActionListener(this);

        vista.txCodigo.addFocusListener(this);
        vista.txNombre.addFocusListener(this);
        vista.txClave.addFocusListener(this);

        vista.txCodigo.addKeyListener(this);
        vista.txNombre.addKeyListener(this);
        vista.txClave.addKeyListener(this);

        // Combo
        vista.cbNivel.addActionListener(this);

        // Tabla
        vista.tabla.getSelectionModel().addListSelectionListener(this);

        // No permitir copiar
        vista.txClave.setTransferHandler(null);

    }

    public void iniciar() {
        menu.fondo.add(vista);
        vista.setVisible(true);
        menu.fondo.moveToFront(vista);
        vista.requestFocus();
        modelarTabla();
        cargaTabla();
        cargarCbNivel();
    }

    // Metodos del ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btSalir) {
            vista.dispose();
        }
        if (e.getSource() == vista.btNuevo) {
            if (comandos.tienePermiso(usuario, obj, "Crear")) {
                vista.txCodigo.setText("-1");
                vista.txNombre.setText("");
                vista.cbNivel.setSelectedItem(1);
                vista.chEstado.setSelected(true);
                vista.btGuardar.setEnabled(true);
                cambiarEstadoTx(true);
                vista.txCodigo.setEnabled(false);
                vista.txNombre.requestFocus();
            } else {
                JOptionPane.showMessageDialog(vista, "No tienes permiso para crear.");
            }
        }
        if (e.getSource() == vista.btGuardar) {
            Usuario us = crearUsuario();
            if (us.getCodigo() == -1) { // si es -1 es uno nuevo
                if (comandos.tienePermiso(usuario, obj, "Crear")) {
                    Boolean existe = comprobarNombre(us.getNombre());
                    if (existe) {
                        JOptionPane.showMessageDialog(vista, "Ese nombre de Usuario ya existe");
                    } else {
                        us.setCodigo(1);
                        ctrl.crearUsuario(us);
                        JOptionPane.showMessageDialog(vista, "Se a Cargado el nuevo usuario ");
                    }
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso de Crear");
                }
            } else { // si es distinto a -1 es una modificacion de uno existente
                if (comandos.tienePermiso(usuario, obj, "Actualizar")) {
                    Boolean existe = comprobarNombre(us.getNombre());
                    if (existe) {
                        JOptionPane.showMessageDialog(vista, "Ese nombre de Usuario ya existe");
                    } else {
                        ctrl.editarUsuario(us);
                        JOptionPane.showMessageDialog(vista, "Se Guardaron los Cambios");
                    }
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso de Editar");
                }
            }
            cambiarEstadoTx(false);
            limpiarFormulario();
            vista.chEstado.setSelected(true);
            vista.btGuardar.setEnabled(false);
            cambiarEstadoTx(false);
            cargaTabla();
        }
        if (e.getSource() == vista.btEliminar) {
            if (comandos.tienePermiso(usuario, obj, "eliminar")) {
                Usuario us = crearUsuario();
                int vOps = JOptionPane.showConfirmDialog(vista, "Seguro de Eliminar el Usuario " + vista.txNombre.getText() + "?", "Advertencia", JOptionPane.YES_NO_OPTION);
                if (vOps == 0) {
                    if (usuario.getTipo() == EnumUsuario.SUPERADMIN){
                        ctrl.eliminarUsuario(us.getCodigo());
                    }else{
                        us.setEstado(false);
                        ctrl.editarUsuario(us);
                    }
                    JOptionPane.showMessageDialog(vista, "Se Elimino de forma Correcta!");
                    cargaTabla();
                    limpiarFormulario();
                }
            }
        }
    }

    // Metodos del ListSelectionListener
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int fila = vista.tabla.getSelectedRow();
            if (fila != -1) {
                int codigo = (int) vista.tabla.getValueAt(fila, 0);
                mostrarUsuario(codigo);
            }
        }
    }

    // Metodos del FocusListener
    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() == vista.txCodigo) {
            vista.txCodigo.selectAll();
        }
        if (e.getSource() == vista.txNombre) {
            vista.txNombre.selectAll();
        }
        if (e.getSource() == vista.txClave) {
            vista.txClave.selectAll();
        }

    }

    @Override
    public void focusLost(FocusEvent e) {

    }

    // Metodos del KeyTiped
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == vista.txCodigo) {
            char c = e.getKeyChar();
            if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                e.consume();
            }
        }
        if (e.getSource() == vista.txNombre) {
            char c = e.getKeyChar();
            if (!(Character.isLetter(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                e.consume();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    // Metodos Genericos para esta Vista
    private void limpiarFormulario() {
        vista.txCodigo.setText("0");
        vista.txNombre.setText("");
        vista.txClave.setText("");
        vista.cbNivel.setSelectedItem(1);
        vista.chEstado.setSelected(true);
    }

    private void cargarCbNivel() {
        EnumUsuario[] valoresEnum = EnumUsuario.values();
        DefaultComboBoxModel<EnumUsuario> modeloComboBox = new DefaultComboBoxModel<>(valoresEnum);
        vista.cbNivel.setModel(modeloComboBox);
    }

    private void modelarTabla() {
        modelo.addColumn("Codigo");
        modelo.addColumn("Nombre");
        modelo.addColumn("Nivel");
        vista.tabla.setModel(modelo);
    }

    public void cargaTabla() {
        List<Usuario> usuarios = ctrl.traerListaUsuarios();
        modelo.setRowCount(0);
        for (Usuario us : usuarios) {
            modelo.addRow(new Object[]{us.getCodigo(), us.getNombre(), us.getTipo()});
        }
        vista.tabla.setModel(modelo);
    }

    private void mostrarUsuario(int codigo) {
        Usuario us = ctrl.traerUsuario(codigo);
        vista.txCodigo.setText(us.getCodigo() + "");
        vista.txNombre.setText(us.getNombre());
        vista.txClave.setText(us.getClave());
        vista.cbNivel.setSelectedItem(us.getTipo());
        vista.chEstado.setSelected(us.getEstado());
        vista.btEliminar.setEnabled(true);
        vista.btGuardar.setEnabled(true);
        cambiarEstadoTx(true);
    }

    private class MyModelo extends DefaultTableModel {

        //para evitar las edicion de los campos de la tabla pero que se puedan seleccionar
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    private void cambiarEstadoTx(boolean estado) {
        vista.txCodigo.setEnabled(estado);
        vista.txNombre.setEnabled(estado);
        vista.txClave.setEnabled(estado);
        vista.chEstado.setEnabled(estado);
        vista.cbNivel.setEnabled(estado);
    }

    private Usuario crearUsuario() {
        Usuario us = new Usuario();
        List<Permiso> lista = new ArrayList<>();
        if (us.getCodigo() > 1) { // si el usuario ya existe
            Usuario pre = ctrl.traerUsuario(Integer.parseInt(vista.txCodigo.getText()));
            lista = pre.getPermiso();
        }
        us.setCodigo(Integer.parseInt(vista.txCodigo.getText()));
        us.setNombre(vista.txNombre.getText());
        us.setClave(vista.txClave.getText());
        us.setEstado(vista.chEstado.isSelected());
        us.setTipo(EnumUsuario.valueOf(vista.cbNivel.getSelectedItem().toString()));
        us.setPermiso(lista);
        return us;
    }

    private Boolean comprobarNombre(String nombre) {
        List<Usuario> lista = ctrl.traerListaUsuarios();
        Boolean existe = false;
        for (Usuario usuario : lista) {
            if (usuario.getNombre().equalsIgnoreCase(nombre)) {
                existe = true;
                break;
            }
        }
        return existe;
    }
}
