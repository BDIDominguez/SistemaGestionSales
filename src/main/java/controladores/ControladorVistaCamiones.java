package controladores;

import entidades.Camion;
import entidades.ComandosComunes;
import entidades.Controladora;
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
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import vistas.VistaCamiones;
import vistas.VistaPantallaPrincipal;



public class ControladorVistaCamiones implements ActionListener, ListSelectionListener, PropertyChangeListener{
    VistaPantallaPrincipal menu;
    VistaCamiones vista;
    ComandosComunes comandos;
    Usuario usuario;
    Controladora ctrl;
    Objeto obj;
    CamionesTableModel modelo = new CamionesTableModel();

    public ControladorVistaCamiones(VistaPantallaPrincipal menu, VistaCamiones vista, ComandosComunes comandos, Usuario usuario, Controladora ctrl, Objeto obj) {
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
        
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        
    }

    private void configuraTabla() {
        vista.tabla.setModel(modelo);
        vista.tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ?
        vista.tabla.getColumnModel().getColumn(0).setPreferredWidth(90);
        vista.tabla.getColumnModel().getColumn(1).setPreferredWidth(150);
        vista.tabla.getColumnModel().getColumn(2).setPreferredWidth(150);
        cargarTabla();
    }

    private void cargarTabla() {
        List<Camion> lista = ctrl.traerListaCamiones();
        modelo.setData(lista);
    }
    
}


class CamionesTableModel extends AbstractTableModel {

    private List<Camion> lista;
    private String[] columnNames = {"Codigo", "Patente", "Odometro"};
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public CamionesTableModel() {
        lista = new ArrayList<>();

    }

    public void agregarDato(Camion obj) {
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
        Camion obj = lista.get(row);
        switch (col) {
            case 0:
                //return obj.getFecha().format(dateFormatter); // Formatear la fecha;
                return obj.getCodigo();
            case 1:
                return obj.getPatente();
            case 2:
                return obj.getOdometro();
               // return formatearImporte(obj.getTotal()); // Formatear el importe
            default:
                return null;
        }
    }

    public Camion getDatoAt(int row) {
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

    public void setData(List<Camion> obj) {
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