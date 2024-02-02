package controladores;

import entidades.Camion;
import entidades.ComandosComunes;
import entidades.Combustible;
import entidades.Controladora;
import entidades.EnumUsuario;
import entidades.Objeto;
import entidades.Usuario;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import vistas.VistaCargaCombustible;
import vistas.VistaPantallaPrincipal;

public class ControladorVistaCargaCombustible implements ActionListener, ListSelectionListener, KeyListener, PropertyChangeListener, FocusListener {

    VistaPantallaPrincipal menu;
    VistaCargaCombustible vista;
    ComandosComunes comandos = new ComandosComunes();
    Usuario usuario;
    Controladora ctrl = new Controladora();
    Objeto obj;
    CombustiblesTableModel modelo = new CombustiblesTableModel();
    int codigoActual;
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm:ss"); // para darle formato a la hora que se va a usar en el txHora
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public ControladorVistaCargaCombustible(VistaPantallaPrincipal menu, VistaCargaCombustible vista, Usuario usuario, Objeto obj) {
        this.menu = menu;
        this.vista = vista;
        this.usuario = usuario;
        this.obj = obj;

        vista.btSalir.addActionListener(this);
        vista.btEliminar.addActionListener(this);
        vista.btGuardar.addActionListener(this);
        vista.btNuevo.addActionListener(this);

        vista.txHora.addFocusListener(this);
        vista.txFactura.addFocusListener(this);
        vista.txImporte.addFocusListener(this);
        vista.txEstacion.addFocusListener(this);
        vista.txLitros.addFocusListener(this);
        vista.txTipo.addFocusListener(this);

        // jCalendars
        vista.dcFecha.addPropertyChangeListener("date", this);

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
        vista.dcFecha.setDate(new Date());
        cargarCamiones();
        modelarTabla();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btNuevo) {
            if (comandos.tienePermiso(usuario, obj, "Crear")) {
                vista.dcFecha.setDate(new Date());
                vista.txHora.setText("00:00:00");
                vista.txTipo.setText("Diesel");
                vista.txImporte.setText("0.00");
                vista.txLitros.setText("0.00");
                vista.txEstacion.setText("");
                cargaCheckEstado(true);
                vista.btEliminar.setEnabled(false);
                vista.btGuardar.setEnabled(true);
                vista.btNuevo.setEnabled(false);
                codigoActual = -1;
                vista.cbCamion.requestFocus();
            } else {
                JOptionPane.showMessageDialog(vista, "No tienes permiso para crear Nuevos");
            }
        }
        if (e.getSource() == vista.btGuardar) {
            Combustible combu = new Combustible();
            Combustible existente = new Combustible();
            if (codigoActual == -1) {
                if (comandos.tienePermiso(usuario, obj, "Crear")) {
                    existente = ctrl.existeFacturaCargaCombustible(vista.txFactura.getText());
                    //if ((existente.getCodigo() == -1) || (existente.getCodigo() != -1 && !existente.getEstacion().equalsIgnoreCase(vista.txEstacion.getText()))) {
                    if (existente.getCodigo() == -1) {
                        combu = crearCombustible();
                        combu.setCodigo(1);
                        ctrl.crearCombustible(combu);
                        cargarTabla();
                        codigoActual = combu.getCodigo();
                        vista.btEliminar.setEnabled(true);
                        vista.btGuardar.setEnabled(false);
                        vista.btNuevo.setEnabled(true);
                    } else if (existente.getEstacion().equalsIgnoreCase(vista.txEstacion.getText())) {
                        JOptionPane.showMessageDialog(vista, "Este comprobante parece haber sido ya cargado el dia " + existente.getFecha().format(dateFormatter));
                        vista.txFactura.requestFocus();
                    } else {
                        combu = crearCombustible();
                        combu.setCodigo(1);
                        ctrl.crearCombustible(combu);
                        cargarTabla();
                        codigoActual = combu.getCodigo();
                        vista.btEliminar.setEnabled(true);
                        vista.btGuardar.setEnabled(false);
                        vista.btNuevo.setEnabled(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para crear Nuevos");
                }
            } else {
                if (comandos.tienePermiso(usuario, obj, "Actualizar")) {
                    combu = ctrl.traerCombustible(codigoActual);
                    existente = ctrl.existeFacturaCargaCombustible(vista.txFactura.getText());
                    if (existente.getCodigo() == -1){
                        combu = crearCombustible();
                        ctrl.editarCombustible(combu);
                        cargarTabla();
                        vista.btEliminar.setEnabled(true);
                        vista.btGuardar.setEnabled(false);
                        vista.btNuevo.setEnabled(true);
                    }else if (!existente.getEstacion().equalsIgnoreCase(vista.txEstacion.getText()) && existente.getCodigo() != codigoActual ){
                        combu = crearCombustible();
                        ctrl.editarCombustible(combu);
                        cargarTabla();
                        vista.btEliminar.setEnabled(true);
                        vista.btGuardar.setEnabled(false);
                        vista.btNuevo.setEnabled(true);
                    }else{
                        JOptionPane.showMessageDialog(vista, "Este comprobante parece haber sido ya cargado el dia " + existente.getFecha().format(dateFormatter));
                        vista.txFactura.requestFocus();
                    }
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para Modificar");
                }
            }
        }
        if (e.getSource() == vista.btEliminar) {
            if (usuario.getTipo() == EnumUsuario.SUPERADMIN) {
                ctrl.eliminarCombustible(codigoActual);
            } else {
                if (comandos.tienePermiso(usuario, obj, "Borrar")) {
                    ctrl.eliminarCombustible(codigoActual);
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para Borror registro");
                }
            }
            cargarTabla();
            vista.btEliminar.setEnabled(false);
            vista.btGuardar.setEnabled(false);
            vista.btNuevo.setEnabled(true);
        }

        if (e.getSource() == vista.btSalir) {
            vista.dispose();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int fila = vista.tabla.getSelectedRow();
            if (fila != -1) {
                Combustible combu = modelo.getDatoAt(fila);
                vista.btNuevo.setEnabled(true);
                vista.btEliminar.setEnabled(true);
                vista.btGuardar.setEnabled(true);
                codigoActual = combu.getCodigo();
                mostrarFilaTabla(combu);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e
    ) {

    }

    @Override
    public void keyPressed(KeyEvent e
    ) {

    }

    @Override
    public void keyReleased(KeyEvent e
    ) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt
    ) {
        if ("date".equals(evt.getPropertyName())) {
            cargarTabla();
            // Manejar el cambio de la fecha aquí
            // Puedes obtener la nueva fecha con: (Date) evt.getNewValue()
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() == vista.cbCamion){
            vista.cbCamion.showPopup();
        }

    }

    @Override
    public void focusLost(FocusEvent e
    ) {

    }

    private Combustible crearCombustible() {
        Combustible combu = new Combustible();
        combu.setCamion((Camion) vista.cbCamion.getSelectedItem());
        combu.setEstacion(vista.txEstacion.getText());
        combu.setEstado(vista.chEstado.isSelected());
        combu.setFecha(vista.dcFecha.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        combu.setHora(LocalTime.parse(vista.txHora.getText(), formato));
        combu.setImporte(Double.parseDouble(vista.txImporte.getText().replace(".", "").replace(",", ".")));
        combu.setLitros(Double.parseDouble(vista.txLitros.getText().replace(".", "").replace(",", ".")));
        combu.setNroFactura(vista.txFactura.getText());
        combu.setTipo(vista.txTipo.getText());
        combu.setCodigo(codigoActual);
        return combu;
    }

    private void cargarCamiones() {
        List<Camion> lista = ctrl.traerListaCamiones();
        for (Camion camion : lista) {
            if (camion.getEstado()) {
                vista.cbCamion.addItem(camion);
            }
        }
    }

    private void modelarTabla() {
        vista.tabla.setModel(modelo);
        vista.tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ?
        vista.tabla.getColumnModel().getColumn(0).setPreferredWidth(100);
        vista.tabla.getColumnModel().getColumn(1).setPreferredWidth(130);
        vista.tabla.getColumnModel().getColumn(2).setPreferredWidth(110);
        vista.tabla.getColumnModel().getColumn(3).setPreferredWidth(110);
        cargarTabla();
    }

    private void cargarTabla() {
        List<Combustible> lista = new ArrayList();
        modelo.setData(lista);
        LocalDate fecha = vista.dcFecha.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        lista = ctrl.traerCargasCombustiblesConFecha(fecha);
        for (Combustible combustible : lista) {
            modelo.agregarDato(combustible);
        }
        cargaCheckEstado(true);
    }

    private void cargaCheckEstado(boolean b) {
        vista.chEstado.setSelected(b);
        if (b) {
            vista.chEstado.setBackground(Color.GREEN);
            vista.chEstado.setText("Cargado / Correcto");
        } else {
            vista.chEstado.setBackground(Color.RED);
            vista.chEstado.setText("Marcado Como Eliminado");
        }
    }

    private void mostrarFilaTabla(Combustible combu) {
        vista.cbCamion.setSelectedItem(combu.getCamion());
        vista.txFactura.setText(combu.getNroFactura());
        vista.txEstacion.setText(combu.getEstacion());
        // Define el formato que deseas (con segundos)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        vista.txHora.setText(combu.getHora().format(formatter));
        DecimalFormat formato = new DecimalFormat("#,##0.00");
        vista.txImporte.setText(formato.format(combu.getImporte()));
        vista.txLitros.setText(formato.format(combu.getLitros()));
        vista.txTipo.setText(combu.getTipo());
        cargaCheckEstado(combu.getEstado());
    }
    
   

}

class CombustiblesTableModel extends AbstractTableModel {

    private List<Combustible> entregas;
    private String[] columnNames = {"Fecha", "Estacion", "Factura", "Importe"};
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public CombustiblesTableModel() {
        entregas = new ArrayList<>();

    }

    public void agregarDato(Combustible combustible) {
        entregas.add(combustible);
        fireTableRowsInserted(entregas.size() - 1, entregas.size() - 1);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return entregas.size();
    }

    public Object getValueAt(int row, int col) {
        Combustible entrega = entregas.get(row);
        switch (col) {
            case 0:
                return entrega.getFecha().format(dateFormatter); // Formatear la fecha;
            case 1:
                return entrega.getEstacion();
            case 2:
                return entrega.getNroFactura();
            case 3:
                return formatearImporte(entrega.getImporte()); // Formatear el importe
            default:
                return null;
        }
    }

    public Combustible getDatoAt(int row) {
        return entregas.get(row);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setData(List<Combustible> entregas) {
        this.entregas = entregas;
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
