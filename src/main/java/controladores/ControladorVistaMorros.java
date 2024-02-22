package controladores;

import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.EnumUsuario;
import entidades.Morro;
import entidades.Objeto;
import entidades.Usuario;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import vistas.VistaMorros;
import vistas.VistaPantallaPrincipal;

/**
 * @author Dario
 */
public class ControladorVistaMorros implements ActionListener, ListSelectionListener, KeyListener {

    VistaPantallaPrincipal menu;
    VistaMorros vista;
    ComandosComunes comandos = new ComandosComunes();
    Usuario usuario;
    Controladora ctrl = new Controladora();
    Objeto obj;
    InventarioTableModel modeloMorro = new InventarioTableModel();

    public ControladorVistaMorros(VistaPantallaPrincipal menu, VistaMorros vista, Usuario usuario, Objeto obj) {
        this.menu = menu;
        this.vista = vista;
        this.usuario = usuario;
        this.obj = obj;

        // Agregando los Botones a escuchar
        vista.btSalir.addActionListener(this);
        vista.btGuardar.addActionListener(this);
        vista.btNuevo.addActionListener(this);
        vista.btEliminar.addActionListener(this);

        // Agregando los Tabla a escuchar
        vista.tabla.getSelectionModel().addListSelectionListener(this);

        // Ajustando la Tabla el modo de redimensionamiento automatico se desactiva y se le pasa los parametros para cada Columna
        vista.tabla.setModel(modeloMorro);
        vista.tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        vista.tabla.getColumnModel().getColumn(0).setPreferredWidth(60);
        vista.tabla.getColumnModel().getColumn(1).setPreferredWidth(307);
        vista.tabla.getColumnModel().getColumn(2).setPreferredWidth(100);

        // Agregando los jTextField a escuchar
        vista.txNombre.addActionListener(this);
        //vista.txObs.addAncestorListener(listener);

        // Agregando los jTextArea a escuchar
        vista.chEstado.addActionListener(this);

        // Agregando los jTextField para escuchar las teclas precionadas
        vista.txNombre.addKeyListener(this);

        // Agregando la Tabla a escuchar
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
        // Configuracion para que el jTextArea al llegar al final del area visible continue la escritura en el area inferior (nueva Linea).
        vista.txObs.setLineWrap(true);
        // Configuracion para que cuando se baje a la linea nueva se lleve las palabras completas y no los corte al azar.
        vista.txObs.setWrapStyleWord(true);
        cargarTabla();
        // Desactivando lo que no tiene que estar activo hasta que se puedan realizar acciones correspondiente!!!
        vista.txCodigo.setEnabled(false);
        vista.txBarrido.setEnabled(false);
        vista.txNombre.setEnabled(false);
        vista.txObs.setEnabled(false);
        vista.txPileta.setEnabled(false);
        vista.txStock.setEnabled(false);
        vista.btGuardar.setEnabled(false);
        vista.btEliminar.setEnabled(false);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btSalir) {
            vista.dispose();
        }
        if (e.getSource() == vista.btNuevo) {
            if (comandos.tienePermiso(usuario, obj, "Crear")) {
                vista.txCodigo.setText("0");
                vista.txNombre.setText("");
                vista.txPileta.setText("0,00");
                vista.txBarrido.setText("0,00");
                vista.txStock.setText("0,00");
                vista.txObs.setText("");
                vista.chEstado.setSelected(true);
                vista.chEstado.setText("Activo");
                vista.chEstado.setBackground(Color.green);
                vista.txNombre.setEnabled(true);
                vista.txObs.setEnabled(true);
                vista.chEstado.setEnabled(true);
                vista.btGuardar.setEnabled(true);
                vista.txNombre.requestFocus();
            } else {
                JOptionPane.showMessageDialog(vista, "No tienes permiso para crear Nuevos");
            }
        }
        if (e.getSource() == vista.btGuardar) {
            Morro morro = crearMorro();
            if (vista.txCodigo.getText().equals("0")) { // Si es Nuevo
                if (comandos.tienePermiso(usuario, obj, "Crear")) {
                    ctrl.crearMorro(morro);
                    vista.txCodigo.setText(morro.getCodigo() + "");
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para crear.");
                }
            } else {// Se Guardara la modificacion, Solo se permite modificar el Nombre, las Observaciones y el Estado. nada mas!!!
                if (comandos.tienePermiso(usuario, obj, "Actualizar")) {
                    Morro editado = new Morro();
                    editado.setCodigo(Integer.parseInt(vista.txCodigo.getText()));
                    editado.setNombre(vista.txNombre.getText());
                    editado.setDetalles(vista.txObs.getText());
                    editado.setEstado(vista.chEstado.isSelected());
                    ctrl.editarMorro(editado);
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para Modificar el Morro.");
                }
            }
            vista.txNombre.setEnabled(false);
            vista.txObs.setEnabled(false);
            vista.chEstado.setEnabled(false);
            vista.btGuardar.setEnabled(false);
        }
        if (e.getSource() == vista.btEliminar) {
            if (usuario.getTipo() == EnumUsuario.SUPERADMIN) {
                ctrl.eliminarMorro(Integer.parseInt(vista.txCodigo.getText()));
                cargarTabla();
            } else {
                if (comandos.tienePermiso(usuario, obj, "Borrar")) {
                    Morro editado = new Morro();
                    editado.setCodigo(Integer.parseInt(vista.txCodigo.getText()));
                    editado.setNombre(vista.txNombre.getText());
                    editado.setDetalles(vista.txObs.getText());
                    editado.setEstado(false);
                    ctrl.editarMorro(editado);
                    vista.chEstado.setText("Desactivado");
                    vista.chEstado.setBackground(Color.RED);
                    vista.chEstado.setSelected(false);
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para Eliminar el Morro.");
                }
            }
            vista.btEliminar.setEnabled(false);
        } // Boton Eliminar
        if (e.getSource() == vista.chEstado) {
            if (vista.chEstado.isSelected()) {
                vista.chEstado.setText("Activo");
                vista.chEstado.setBackground(Color.green);
            } else {
                vista.chEstado.setText("Desactivado");
                vista.chEstado.setBackground(Color.RED);
            }
        }

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int fila = vista.tabla.getSelectedRow();
            if (fila != -1) {
                Morro morro = modeloMorro.getMorroAt(fila);
                mostrarMorro(morro);
                vista.btEliminar.setEnabled(true);
                vista.btGuardar.setEnabled(true);
                vista.txNombre.setEnabled(true);
                vista.txObs.setEnabled(true);
            }
        }
    }

    private Morro crearMorro() {  // Cuando se crea un morro, este inicia con el Stock, pileta y barrido en Cero. nunca se podran editar por medio de esta pantalla
        Morro morro = new Morro();
        morro.setCodigo(Integer.parseInt(vista.txCodigo.getText()));
        morro.setBarrido(0);
        morro.setDetalles(vista.txObs.getText());
        morro.setEstado(vista.chEstado.isSelected());
        morro.setNombre(vista.txNombre.getText());
        morro.setStock(0);
        morro.setPileta(0);
        morro.setEntregas(new ArrayList());
        return morro;
    }

    private void cargarTabla() {
        List<Morro> morros = new ArrayList();
        modeloMorro.setData(morros);
        morros = ctrl.traerListaMorros();
        for (Morro morro : morros) {
            modeloMorro.agregarMorro(morro);
        }
    }

    private void mostrarMorro(Morro morro) {

        vista.txStock.setText(morro.getStock() + "");
        vista.txCodigo.setText(morro.getCodigo() + "");
        vista.txNombre.setText(morro.getNombre());
        vista.txObs.setText(morro.getDetalles());
        double total = morro.getBarrido() + morro.getPileta();
        if (total > 0) {
            vista.txPileta.setText(((morro.getPileta() * 100) / total) + "");
            vista.txBarrido.setText(((morro.getBarrido() * 100) / total) + "");
        } else {
            vista.txPileta.setText("0");
            vista.txBarrido.setText("0");
        }

        vista.chEstado.setSelected(morro.getEstado());
        if (morro.getEstado() == true) {
            vista.chEstado.setText("Activo");
            vista.chEstado.setBackground(Color.green);
        } else {
            vista.chEstado.setText("Desactivado");
            vista.chEstado.setBackground(Color.RED);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == vista.txNombre) {
            char c = e.getKeyChar();
            if (!(Character.isLetter(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || Character.isDigit(c) || c == KeyEvent.VK_SPACE)) {
                e.consume();
            }
        }
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
                vista.txNombre.requestFocusInWindow();
                e.consume(); // Evita que se inserte un carácter de tabulación en la tabla
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}  // Fin Clase Controlador

class InventarioTableModel extends AbstractTableModel {

    private List<Morro> morros;
    private String[] columnNames = {"Codigo", "Nombre", "Stock"};

    public InventarioTableModel() {
        morros = new ArrayList<>();

    }

    public void agregarMorro(Morro morro) {
        morros.add(morro);
        fireTableRowsInserted(morros.size() - 1, morros.size() - 1);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return morros.size();
    }

    public Object getValueAt(int row, int col) {
        Morro morro = morros.get(row);
        switch (col) {
            case 0:
                return morro.getCodigo();
            case 1:
                return morro.getNombre();
            case 2:
                return formatearImporte(morro.getStock());
            default:
                return null;
        }
    }

    public Morro getMorroAt(int row) {
        return morros.get(row);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setData(List<Morro> morros) {
        this.morros = morros;
        fireTableDataChanged();
    }

    private String formatearImporte(double importe) {
        DecimalFormat formato = new DecimalFormat("#,##0.00");
        return formato.format(importe);
    }
}
