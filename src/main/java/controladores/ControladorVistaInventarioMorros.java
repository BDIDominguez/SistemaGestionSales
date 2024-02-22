package controladores;


import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.InventarioMorro;
import entidades.Morro;
import entidades.Objeto;
import entidades.Usuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import vistas.VistaInventarioMorros;
import vistas.VistaPantallaPrincipal;

public class ControladorVistaInventarioMorros implements ActionListener, ListSelectionListener, PropertyChangeListener, KeyListener{
    VistaPantallaPrincipal menu;
    VistaInventarioMorros vista;
    ComandosComunes comandos;
    Usuario usuario;
    Controladora ctrl;
    Objeto obj;
    MorroTableModel modelo = new MorroTableModel();
    Morro objActual;

    public ControladorVistaInventarioMorros(VistaPantallaPrincipal menu, VistaInventarioMorros vista, ComandosComunes comandos, Usuario usuario, Controladora ctrl, Objeto obj) {
        this.menu = menu;
        this.vista = vista;
        this.comandos = comandos;
        this.usuario = usuario;
        this.ctrl = ctrl;
        this.obj = obj;
        
         // Escuchando los Botones
        vista.btSalir.addActionListener(this);
        vista.btCambiar.addActionListener(this);
        vista.btSalir.addActionListener(this);

        // Agregando los Tabla a escuchar
        vista.tabla.getSelectionModel().addListSelectionListener(this);
        vista.tabla.addKeyListener(this);  // Agregar el KeyListener a la tabla
    
    }
    
    public void iniciar() {
        menu.fondo.add(vista);
        vista.setVisible(true);
        menu.fondo.moveToFront(vista);
        vista.requestFocus();
        configuraTabla();
        vista.txObs.setLineWrap(true);
        vista.txObs.setWrapStyleWord(true);
        vista.tabla.requestFocus();
        vista.btCambiar.setEnabled(false);
    }
    
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btSalir) {
            vista.dispose();
        }
        if (e.getSource() == vista.btCambiar){
            if (comandos.tienePermiso(usuario, obj, "Crear")) {
                if (vista.btCambiar.getText().equalsIgnoreCase("Cambiar")){
                    vista.btCambiar.setText("Guardar");
                    vista.txStock.setEnabled(true);
                    vista.tabla.setEnabled(false);
                    vista.txStock.requestFocus();
                }else{
                    InventarioMorro carga = new InventarioMorro();
                    carga.setCodigo(1);
                    carga.setBarrido(0);
                    carga.setCodigoMorro(objActual.getCodigo());
                    carga.setDetalles("Inventario realizado por " + usuario.getNombre());
                    carga.setEstado(Boolean.TRUE);
                    carga.setFecha(LocalDate.now());
                    carga.setPileta(0);
                    double stock = objActual.getStock();
                    double nuevoStock =vista.txStock.getValorDouble();
                    double valor = (stock - nuevoStock) * (-1);
                    carga.setTotal(valor);
                    ctrl.crearInventarioMorro(carga); // Se persiste el inventario en su propia tabla
                    objActual.setStock(vista.txStock.getValorDouble());
                    ctrl.editarMorro(objActual); // se persiste el nuevo stock en el Morro
                    JOptionPane.showMessageDialog(vista, "Se Actualizo el Stock de Forma Correcta");
                    cargarTabla();
                    vista.btCambiar.setText("Cambiar");
                    vista.btCambiar.setEnabled(false);
                    vista.txStock.setEnabled(false);
                    vista.tabla.setEnabled(true);
                }
            }else{
                JOptionPane.showMessageDialog(vista, "No tienes permiso para Cambiar el Stock");
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int fila = vista.tabla.getSelectedRow();
            if (fila != -1) {
                objActual = modelo.getDatoAt(fila);
                vista.btCambiar.setEnabled(true);
                mostrarObj();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    private void configuraTabla() {
        vista.tabla.setModel(modelo);
        vista.tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ?
        vista.tabla.getColumnModel().getColumn(0).setPreferredWidth(186);
        vista.tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
        cargarTabla();
    }

    private void cargarTabla() {
        List<Morro> lista = ctrl.traerListaMorros();
        for (Morro morro : lista) {
            if (!morro.getEstado()){
                lista.remove(morro);
            }
        }
        modelo.setData(lista);
    }

    private void mostrarObj() {
        vista.txCodigo.setText(objActual.getCodigo() + "");
        vista.txNombre.setText(objActual.getNombre());
        vista.txObs.setText(objActual.getDetalles());
        vista.txStock.setText(objActual.getStock() + "");
        vista.btCambiar.setEnabled(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == vista.tabla) {
            if (e.getKeyCode() == KeyEvent.VK_TAB && !e.isShiftDown()) {
                // Si la tecla presionada es Tab y no se mantiene presionado Shift,
                // establece el foco en el siguiente objeto después de la tabla
                vista.btCambiar.requestFocusInWindow();
                e.consume(); // Evita que se inserte un carácter de tabulación en la tabla
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}

class MorroTableModel extends AbstractTableModel {

    private List<Morro> lista;
    private String[] columnNames = {"Nombre", "Stock"};
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public MorroTableModel() {
        lista = new ArrayList<>();

    }

    public void agregarDato(Morro obj) {
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
        Morro obj = lista.get(row);
        switch (col) {
            case 0:
                //return obj.getFecha().format(dateFormatter); // Formatear la fecha;
                return obj.getNombre();
            case 1:
                return formatearImporte(obj.getStock());
            // return formatearImporte(obj.getTotal()); // Formatear el importe
            default:
                return null;
        }
    }

    public Morro getDatoAt(int row) {
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

    public void setData(List<Morro> obj) {
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