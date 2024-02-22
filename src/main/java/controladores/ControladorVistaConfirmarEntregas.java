package controladores;

import entidades.Cliente;
import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.Entrega;
import entidades.Morro;
import entidades.Objeto;
import entidades.Usuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import vistas.VistaConfirmarEntregas;
import vistas.VistaPantallaPrincipal;

public class ControladorVistaConfirmarEntregas implements ActionListener, PropertyChangeListener, ListSelectionListener {

    VistaPantallaPrincipal menu;
    VistaConfirmarEntregas vista;
    ComandosComunes comandos;
    Usuario usuario;
    Controladora ctrl;
    Objeto obj;
    EntregaTableModel modelo = new EntregaTableModel();
    Entrega objetoActual;

    public ControladorVistaConfirmarEntregas(VistaPantallaPrincipal menu, VistaConfirmarEntregas vista, ComandosComunes comandos, Usuario usuario, Controladora ctrl, Objeto obj) {
        this.menu = menu;
        this.vista = vista;
        this.comandos = comandos;
        this.usuario = usuario;
        this.ctrl = ctrl;
        this.obj = obj;

        // escuchando a los botones
        vista.btSalir.addActionListener(this);
        vista.btCargar.addActionListener(this);

        // jCalendars
        vista.dcFecha.addPropertyChangeListener("date", this);

        // Agregando los Tabla a escuchar
        vista.tabla.getSelectionModel().addListSelectionListener(this);

        // Agregando el Combo Para que actualize la tabla!!
        vista.cbCliente.addActionListener(this);
    }

    public void iniciar() {
        menu.fondo.add(vista);
        vista.setVisible(true);
        menu.fondo.moveToFront(vista);
        vista.requestFocus();
        vista.dcFecha.setDate(new Date());
        vista.btCargar.setEnabled(false);
        vista.txTeorioco.setEditable(false);
        cargaComboBox();
        configuraTabla();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btSalir) {
            vista.dispose();
        }
        if (e.getSource() == vista.btCargar) {
            if (comandos.tienePermiso(usuario, obj, "Crear")) {
                int vRespuesta = JOptionPane.showConfirmDialog(vista, "Seguro de Confirmar", "Confirmar Envio", JOptionPane.YES_NO_OPTION);
                if (vRespuesta == JOptionPane.YES_OPTION) {
                    objetoActual.setConfirmado(vista.txConfirmado.getValorDouble());
                    objetoActual.setFecentrega(vista.dcFecha.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    objetoActual.setEtapa(1);
                    ctrl.editarEntrega(objetoActual);
                    // Se modifica el clietne para sumar los Datos de ventas confirmadas.
                    Cliente cliente = objetoActual.getCliente();
                    cliente.sumarCantidad(1);
                    ctrl.editarCliente(cliente);
                    // Se debe descontar del morro las cantidades confirmadas para llevar el Stock
                    Morro morro = objetoActual.getMorro();
                    morro.quitarDelMorro(objetoActual.getConfirmado(), 0, 0); // Se decuenta del morro la cantidad confirmada!!
                    ctrl.editarMorro(morro);

                    cargarTabla();
                    vista.btCargar.setEnabled(false);
                }
            } else {
                JOptionPane.showMessageDialog(vista, "No tienes permiso para Confirmar Entregas");
            }
        }
        if (e.getSource() == vista.cbCliente) {
            cargarTabla();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int fila = vista.tabla.getSelectedRow();
            if (fila != -1) {
                objetoActual = modelo.getDatoAt(fila);
                mostrarObjeto();
            }
        }
    }

    private void cargaComboBox() {
        List<Cliente> lista = ctrl.traerListaClientes();
        for (Cliente cliente : lista) {
            if (cliente.getEstado()) {
                vista.cbCliente.addItem(cliente);
            }
        }
    }

    private void configuraTabla() {
        vista.tabla.setModel(modelo);
        vista.tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ?
        vista.tabla.getColumnModel().getColumn(0).setPreferredWidth(108);
        vista.tabla.getColumnModel().getColumn(1).setPreferredWidth(145);
        vista.tabla.getColumnModel().getColumn(2).setPreferredWidth(105);
        cargarTabla();
    }

    private void cargarTabla() {
        Cliente cliente = (Cliente) vista.cbCliente.getSelectedItem();
        List<Entrega> lista = ctrl.traerEntregaPorClienteAConfirmar(cliente);
        // Usar un iterador para evitar ConcurrentModificationException
        Iterator<Entrega> iterator = lista.iterator();
        while (iterator.hasNext()) {
            Entrega entrega = iterator.next();
            if (!entrega.getEstado()) {
                iterator.remove(); // Eliminar usando el iterador
            }
        }
        modelo.setData(lista);
    }

    private void mostrarObjeto() {
        vista.txConfirmado.setText(objetoActual.getTeorico() + "");
        vista.txTeorioco.setText(objetoActual.getTeorico() + "");
        vista.btCargar.setEnabled(true);
        vista.txConfirmado.requestFocus();
    }

}

class EntregaTableModel extends AbstractTableModel {

    private List<Entrega> lista;
    private String[] columnNames = {"Fecha Salida", "Nro Remito", "Cant. Teorica"};
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public EntregaTableModel() {
        lista = new ArrayList<>();

    }

    public void agregarDato(Entrega obj) {
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
        Entrega obj = lista.get(row);
        switch (col) {
            case 0:
                //return obj.getFecha().format(dateFormatter); // Formatear la fecha;
                return obj.getFecsalida().format(dateFormatter);
            case 1:
                return obj.getRemito();
            case 2:
                return formatearImporte(obj.getTeorico());

            default:
                return null;
        }
    }

    public Entrega getDatoAt(int row) {
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

    public void setData(List<Entrega> obj) {
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
