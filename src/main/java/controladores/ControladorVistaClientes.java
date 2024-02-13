package controladores;

import entidades.Camion;
import entidades.Cliente;
import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.Entrega;
import entidades.EnumUsuario;
import entidades.Objeto;
import entidades.Usuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import vistas.VistaClientes;
import vistas.VistaPantallaPrincipal;

public class ControladorVistaClientes implements ActionListener, ListSelectionListener, PropertyChangeListener {

    VistaPantallaPrincipal menu;
    VistaClientes vista;
    ComandosComunes comandos;
    Usuario usuario;
    Controladora ctrl;
    Objeto obj;
    ClienteTableModel modelo = new ClienteTableModel();
    Cliente objetoActual;

    public ControladorVistaClientes(VistaPantallaPrincipal menu, VistaClientes vista, ComandosComunes comandos, Usuario usuario, Controladora ctrl, Objeto obj) {
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

        // Agregando los Text
        vista.txCodigo.addActionListener(this);
        vista.txCantCompras.addActionListener(this);
        vista.txCuit.addActionListener(this);
        vista.txDistancia.addActionListener(this);
        vista.txEstado.addActionListener(this);
        vista.txImporte.addActionListener(this);
        vista.txNombre.addActionListener(this);

    }

    public void iniciar() {
        menu.fondo.add(vista);
        vista.setVisible(true);
        menu.fondo.moveToFront(vista);
        vista.requestFocus();
        vista.btGuardar.setEnabled(false);
        vista.btEliminar.setEnabled(false);
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
                vista.txCantCompras.setText("0,00");
                vista.txCuit.setText("");
                vista.txDistancia.setText("0,00");
                vista.txEstado.setSelected(true);
                vista.txImporte.setText("0,00");
                vista.txNombre.setText("");
                vista.txNombre.requestFocus();
                vista.btNuevo.setEnabled(false);
                vista.btGuardar.setEnabled(true);
                vista.btEliminar.setEnabled(false);

            } else {
                JOptionPane.showMessageDialog(vista, "No tienes permiso para Crear nuevos Camiones");
            }
        }
        if (e.getSource() == vista.btGuardar) {
            if (objetoActual == null) {
                if (comandos.tienePermiso(usuario, obj, "Crear")) {
                    Cliente cliente = crearCliente();
                    ctrl.crearCliente(cliente);
                    vista.btNuevo.setEnabled(true);
                    vista.btGuardar.setEnabled(false);
                    vista.btEliminar.setEnabled(false);
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para Crear nuevos Camiones");
                }
            } else {
                if (comandos.tienePermiso(usuario, obj, "Actualizar")) {
                    Cliente cliente = crearCliente();
                    ctrl.editarCliente(cliente);
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
                ctrl.eliminarCliente(objetoActual.getCodigo()); // Si se elimina correctamente se debe corregir el Stock en el morro
                cargarTabla();
                vista.btEliminar.setEnabled(false);
                vista.btGuardar.setEnabled(false);
                vista.btNuevo.setEnabled(true);
                cargarTabla();
            } else {
                if (comandos.tienePermiso(usuario, obj, "Borrar")) {
                    objetoActual.setEstado(Boolean.FALSE);
                    ctrl.editarCliente(objetoActual);
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
        /*
        vista.tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ?
        vista.tabla.getColumnModel().getColumn(0).setPreferredWidth(100);
        vista.tabla.getColumnModel().getColumn(1).setPreferredWidth(130);
         */
        cargarTabla();
    }

    private void cargarTabla() {
        List<Cliente> lista = ctrl.traerListaClientes();
        modelo.setData(lista);
    }

    private void mostrarObjeto() {
        vista.txCodigo.setText(objetoActual.getCodigo() + "");
        vista.txCantCompras.setText(objetoActual.getCantidad() + "");
        vista.txCuit.setText(objetoActual.getCuil());
        vista.txDistancia.setText(objetoActual.getDistancia() + "");
        vista.txEstado.setSelected(objetoActual.getEstado());
        vista.txImporte.setText(objetoActual.getCompras() + "");
        vista.txNombre.setText(objetoActual.getNombre());
    }

    private Cliente crearCliente() {
        Cliente cliente = new Cliente();
        if (objetoActual == null){
            List<Entrega> lista = new ArrayList();
            cliente.setCodigo(1);
            cliente.setEntrega(lista);
        }else{
            cliente.setEntrega(objetoActual.getEntrega());
            cliente.setCodigo(objetoActual.getCodigo());
        }
        cliente.setCantidad(vista.txCantCompras.getValorDouble());
        cliente.setCompras(vista.txImporte.getValorDouble());
        cliente.setCuil(vista.txCuit.getText());
        cliente.setDistancia(vista.txDistancia.getValorDouble());
        cliente.setEstado(vista.txEstado.isSelected());
        cliente.setNombre(vista.txNombre.getText());
        return cliente;
    }

}

class ClienteTableModel extends AbstractTableModel {

    private List<Cliente> lista;
    private String[] columnNames = {"Nombre", "CUIT"};
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public ClienteTableModel() {
        lista = new ArrayList<>();

    }

    public void agregarDato(Cliente obj) {
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
        Cliente obj = lista.get(row);
        switch (col) {
            case 0:
                //return obj.getFecha().format(dateFormatter); // Formatear la fecha;
                return obj.getNombre();
            case 1:
                return obj.getCuil();
            // return formatearImporte(obj.getTotal()); // Formatear el importe
            default:
                return null;
        }
    }

    public Cliente getDatoAt(int row) {
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

    public void setData(List<Cliente> obj) {
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
