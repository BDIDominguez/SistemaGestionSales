package controladores;

import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.Direccion;
import entidades.Objeto;
import entidades.Proveedor;
import entidades.Telefono;
import entidades.Usuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import vistas.VistaPantallaPrincipal;
import vistas.VistaProveedores;

public class ControladorVistaProveedores implements ActionListener, ListSelectionListener, FocusListener, KeyListener {

    VistaPantallaPrincipal menu;
    VistaProveedores vista;
    ProveedorTableModel modeloProveedor = new ProveedorTableModel();
    TelefonosTableModel modeloTelefono = new TelefonosTableModel();
    TelefonosTableModel modeloDireccion = new TelefonosTableModel();
    Controladora ctrl = new Controladora();
    ComandosComunes comandos = new ComandosComunes();
    Usuario usuario;
    Objeto obj;
    List<Telefono> telefonos;
    List<Direccion> direcciones;

    public ControladorVistaProveedores(VistaPantallaPrincipal menu, VistaProveedores vista, Usuario usuario, Objeto obj) {
        this.menu = menu;
        this.vista = vista;
        this.usuario = usuario;
        this.obj = obj;

        vista.btSalir.addActionListener(this);

        // Tabla
        vista.tbProveedores.getSelectionModel().addListSelectionListener(this);
        vista.tbDirecciones.getSelectionModel().addListSelectionListener(this);
        vista.tbTelefono.getSelectionModel().addListSelectionListener(this);
        

    }

    public void iniciar() {
        menu.fondo.add(vista);
        vista.setVisible(true);
        menu.fondo.moveToFront(vista);
        vista.requestFocus();
        vista.setTitle("Proveedores");
        modelarTablaDirecciones();
        modelarTablaProveedores();
        modelarTablaTelefonos();
        cargarProveedores();
        /*
        modelarTabla();
        cargaTabla();
        cargarCbNivel();
         */
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btSalir) {
            vista.dispose();
        }
        if (e.getSource() == vista.btAgTelefono) {
            boolean existe = false;
            for (Telefono tel : telefonos) {
                if (tel.getNumero().equalsIgnoreCase(vista.txTelefono.getText())) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                Telefono tel = new Telefono();
                tel.setCodigo(0);
                tel.setNumero(vista.txTelefono.getText());
                telefonos.add(tel);

            }
        }

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        //e.getSource() == vista.tbProveedores && &&  
        if (!e.getValueIsAdjusting()) {
            if (e.getSource() instanceof JTable && (JTable)e.getSource() == vista.tbProveedores){
                System.out.println("aqui si funcionar!!!");
            }
            
            int fila = vista.tbProveedores.getSelectedRow();
            if (fila != -1) {
                Proveedor prov = modeloProveedor.getProveedorAt(fila);
                mostrarProveedor(prov);
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void cargarTelefonos() {
        int fila = vista.tbProveedores.getSelectedRow();
        if (fila != -1) {

        }

        //List<Telefono> lista = 
    }

    private void cargarProveedores() {
        List<Proveedor> lista = ctrl.traerListaProveedor();
        for (Proveedor pro : lista) {
            if (pro.getEstado()) {
                modeloProveedor.agregarProveedor(pro);
            }
        }
        vista.tbProveedores.setModel(modeloProveedor);
    }

    private void mostrarProveedor(Proveedor prov) {
        vista.txCodigo.setText(prov.getCodigo() + "");
        vista.txRazon.setText(prov.getRazon());
        vista.txNombre.setText(prov.getNombre());
        vista.txCUIT.setText(prov.getCuit());
        vista.chEstado.setSelected(prov.getEstado());
        mostrarCargarTelefonos(prov.getTelefono());

    }

    private void mostrarCargarTelefonos(List<Telefono> telefono) {
        // mostrar los telefonos del proveedor, la carga del modelo se hace en otra parte aqui solo se cambia el contenido del modelo!!!
    }

    private class MyModelo extends DefaultTableModel {

        //para evitar las edicion de los campos de la tabla pero que se puedan seleccionar
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    private void modelarTablaProveedores() {
        vista.tbProveedores.setModel(modeloProveedor);
    }

    private void modelarTablaDirecciones() {
       
        vista.tbDirecciones.setModel(modeloDireccion);
    }

    private void modelarTablaTelefonos() {
        vista.tbTelefono.setModel(modeloTelefono);
    }

} // Fin Clase Vista

class ProveedorTableModel extends AbstractTableModel {

    private List<Proveedor> proveedores;
    private String[] columnNames = {"Nombre"};

    public ProveedorTableModel() {
        proveedores = new ArrayList<>();
    }

    public void agregarProveedor(Proveedor prov) {
        proveedores.add(prov);
        fireTableRowsInserted(proveedores.size() - 1, proveedores.size() - 1);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return proveedores.size();
    }

    public Object getValueAt(int row, int col) {
        Proveedor prov = proveedores.get(row);
        switch (col) {
            case 0:
                return prov.getNombre();
            default:
                return null;
        }
    }

    public Proveedor getProveedorAt(int row) {
        return proveedores.get(row);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

class TelefonosTableModel extends AbstractTableModel {

    private List<Telefono> telefonos;
    private String[] columnNames = {"Numero"};

    public TelefonosTableModel() {
        telefonos = new ArrayList<>();
    }

    public void agregarProveedor(Telefono tel) {
        telefonos.add(tel);
        fireTableRowsInserted(telefonos.size() - 1, telefonos.size() - 1);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return telefonos.size();
    }

    public Object getValueAt(int row, int col) {
        Telefono tel = telefonos.get(row);
        switch (col) {
            case 0:
                return tel.getNumero();
            default:
                return null;
        }
    }

    public Telefono getTelefonoAt(int row) {
        return telefonos.get(row);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}