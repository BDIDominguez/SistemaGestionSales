package controladores;

import entidades.Camion;
import entidades.Chofer;
import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.EnumUsuario;
import entidades.Objeto;
import entidades.Usuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import vistas.VistaChoferes;
import vistas.VistaPantallaPrincipal;

public class ControladorVistaChoferes implements ActionListener, ListSelectionListener, PropertyChangeListener, KeyListener {

    VistaPantallaPrincipal menu;
    VistaChoferes vista;
    ComandosComunes comandos;
    Usuario usuario;
    Controladora ctrl;
    Objeto obj;
    ChoferesTableModel modelo = new ChoferesTableModel();
    Chofer objetoActual;

    public ControladorVistaChoferes(VistaPantallaPrincipal menu, VistaChoferes vista, ComandosComunes comandos, Usuario usuario, Controladora ctrl, Objeto obj) {
        this.menu = menu;
        this.vista = vista;
        this.comandos = comandos;
        this.usuario = usuario;
        this.ctrl = ctrl;
        this.obj = obj;

        // Escuchando los Botones
        vista.btSalir.addActionListener(this);
        vista.btEliminar.addActionListener(this);
        vista.btGuardar.addActionListener(this);
        vista.btNuevo.addActionListener(this);

        // Agregando los Tabla a escuchar
        vista.tabla.getSelectionModel().addListSelectionListener(this);
        vista.tabla.addKeyListener(this);  // Agregar el KeyListener a la tabla

        //Capturando las teclas del jTextArea
        vista.txObs.addKeyListener(this);

    }

    public void iniciar() {
        menu.fondo.add(vista);
        vista.setVisible(true);
        menu.fondo.moveToFront(vista);
        vista.requestFocus();
        vista.btGuardar.setEnabled(false);
        vista.btEliminar.setEnabled(false);
        vista.txObs.setLineWrap(true);
        vista.txObs.setWrapStyleWord(true);
        configurarCombo();
        configuraTabla();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btSalir) {
            vista.dispose();
        }
        if (e.getSource() == vista.btNuevo) {
            if (comandos.tienePermiso(usuario, obj, "Crear")) {
                objetoActual = null;
                vista.txCodigo.setText("-1");
                vista.txNombre.setText("");
                vista.cbCamion.setSelectedIndex(0);
                vista.txObs.setText("");
                vista.chEstado.setSelected(true);
                vista.btNuevo.setEnabled(false);
                vista.btGuardar.setEnabled(true);
                vista.btEliminar.setEnabled(false);
                vista.txNombre.requestFocus();
            } else {
                JOptionPane.showMessageDialog(vista, "No tienes permiso para Crear nuevos Camiones");
            }
        }
        if (e.getSource() == vista.btGuardar) {
            if (objetoActual == null) {
                if (comandos.tienePermiso(usuario, obj, "Crear")) {
                    Chofer chofer = crearObjeto();
                    ctrl.crearChofer(chofer);
                    vista.btNuevo.setEnabled(true);
                    vista.btGuardar.setEnabled(false);
                    vista.btEliminar.setEnabled(false);
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para Crear nuevos Camiones");
                }
            }else{
                if (comandos.tienePermiso(usuario, obj, "Actualizar")) {
                    Chofer chofer = crearObjeto();
                    ctrl.editarChofer(chofer);
                    vista.btNuevo.setEnabled(true);
                    vista.btGuardar.setEnabled(false);
                    vista.btEliminar.setEnabled(false);
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para Actualizar los Camiones");
                }
            }
        }

        if (e.getSource() == vista.btEliminar) {
            if (usuario.getTipo() == EnumUsuario.SUPERADMIN) {
                ctrl.eliminarChofer(objetoActual.getCodigo()); // Si se elimina correctamente se debe corregir el Stock en el morro
                cargarTabla();
                vista.btEliminar.setEnabled(false);
                vista.btGuardar.setEnabled(false);
                vista.btNuevo.setEnabled(true);
                cargarTabla();
            } else {
                if (comandos.tienePermiso(usuario, obj, "Borrar")) {
                    objetoActual.setEstado(Boolean.FALSE);
                    ctrl.editarChofer(objetoActual);
                    vista.btEliminar.setEnabled(false);
                    vista.btGuardar.setEnabled(false);
                    vista.btNuevo.setEnabled(true);
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para Borror registro");
                }
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int fila = vista.tabla.getSelectedRow();
            if (fila != -1) {
                objetoActual = modelo.getDatoAt(fila);
                vista.btNuevo.setEnabled(true);
                vista.btEliminar.setEnabled(true);
                vista.btGuardar.setEnabled(true);
                mostrarObjeto();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

   

    private void configuraTabla() {
        vista.tabla.setModel(modelo);
        vista.tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ?
        vista.tabla.getColumnModel().getColumn(0).setPreferredWidth(90);
        vista.tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
        vista.tabla.getColumnModel().getColumn(2).setPreferredWidth(120);
        cargarTabla();
    }

    private void cargarTabla() {
        List<Chofer> lista = ctrl.traerListaChoferes();
        modelo.setData(lista);
    }

    private void configurarCombo() {
        List<Camion> lista = ctrl.traerListaCamiones();
        for (Camion camion : lista) {
            if (camion.getEstado()){
                vista.cbCamion.addItem(camion);
            }
        }
    }

    private Chofer crearObjeto() {
        Chofer chofer = new Chofer();
        if (objetoActual == null){
            chofer.setCodigo(1);
        }else{
            chofer.setCodigo(objetoActual.getCodigo());
        }
        chofer.setCamion((Camion) vista.cbCamion.getSelectedItem());
        chofer.setEstado(vista.chEstado.isSelected());
        chofer.setNombre(vista.txNombre.getText());
        chofer.setObs(vista.txObs.getText());
        return chofer;
    }

    private void mostrarObjeto() {
        vista.txCodigo.setText(objetoActual.getCodigo()+"");
        vista.txNombre.setText(objetoActual.getNombre());
        vista.txObs.setText(objetoActual.getObs());
        vista.chEstado.setSelected(objetoActual.getEstado());
        if (objetoActual.getCamion() != null){
            vista.cbCamion.setSelectedItem(objetoActual.getCamion());
        }else{
            JOptionPane.showMessageDialog(vista, "Este Chofer no tiene Asignado ningun camion! \n Recuerde que solo se puede tener un Camion por chofer no se pueden compartir");
        }
        vista.btEliminar.setEnabled(true);
        vista.btGuardar.setEnabled(true);
        vista.btNuevo.setEnabled(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == vista.txObs) {
            if (e.getKeyCode() == KeyEvent.VK_TAB) {
                // Si la tecla presionada es Tab, establece el foco en jText2
                vista.chEstado.requestFocusInWindow();
                e.consume(); // Evita que se inserte un carácter de tabulación en txObs
            }
        }
        if (e.getSource() == vista.tabla) {
            if (e.getKeyCode() == KeyEvent.VK_TAB && !e.isShiftDown()) {
                // Si la tecla presionada es Tab y no se mantiene presionado Shift,
                // establece el foco en el siguiente objeto después de la tabla
                vista.cbCamion.requestFocusInWindow();
                e.consume(); // Evita que se inserte un carácter de tabulación en la tabla
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    

}

class ChoferesTableModel extends AbstractTableModel {

    private List<Chofer> lista;
    private String[] columnNames = {"Codigo", "Nombre", "Camion"};
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public ChoferesTableModel() {
        lista = new ArrayList<>();

    }

    public void agregarDato(Chofer obj) {
        lista.add(obj);
        fireTableRowsInserted(lista.size() - 1, lista.size() - 1);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return lista.size();
    }

    public Object getValueAt(int row, int col) {
        Chofer obj = lista.get(row);
        switch (col) {
            case 0:
                //return obj.getFecha().format(dateFormatter); // Formatear la fecha;
                return obj.getCodigo();
            case 1:
                return obj.getNombre();
            case 2:
                String cadena;
                if (obj.getCamion() != null){
                    cadena = obj.getCamion().getPatente();
                }else{
                    cadena = "Sin Asignar";
                }
                return cadena;
            // return formatearImporte(obj.getTotal()); // Formatear el importe
            default:
                return null;
        }
    }

    public Chofer getDatoAt(int row) {
        return lista.get(row);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setData(List<Chofer> obj) {
        this.lista = obj;
        fireTableDataChanged();
    }

    public void actualizarDatosEnFila(int fila) {
        // Actualiza los datos en la fila correspondiente (fila en base a tu lógica)
        // Notifica a la tabla que la fila ha sido actualizada
        fireTableRowsUpdated(fila, fila);
    }

    private String formatearImporte(double importe) {
        DecimalFormat formato = new DecimalFormat("#,##0.00");
        return formato.format(importe);
    }
}
