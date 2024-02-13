package controladores;

import entidades.Camion;
import entidades.CamionOdometro;
import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.EnumUsuario;
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
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import vistas.VistaConfirmarOdometro;
import vistas.VistaPantallaPrincipal;

public class ControladorVistaConfirmarOdometro implements ActionListener, ListSelectionListener, PropertyChangeListener {

    VistaPantallaPrincipal menu;
    VistaConfirmarOdometro vista;
    ComandosComunes comandos;
    Usuario usuario;
    Controladora ctrl;
    Objeto obj;
    CamionOdometroTableModel modelo = new CamionOdometroTableModel();
    CamionOdometro objActual = null;

    public ControladorVistaConfirmarOdometro(VistaPantallaPrincipal menu, VistaConfirmarOdometro vista, ComandosComunes comandos, Usuario usuario, Controladora ctrl, Objeto obj) {
        this.menu = menu;
        this.vista = vista;
        this.comandos = comandos;
        this.usuario = usuario;
        this.obj = obj;
        this.ctrl = ctrl;
        // Escuchando los Botones
        vista.btSalir.addActionListener(this);
        vista.btEliminar.addActionListener(this);
        vista.btGuardar.addActionListener(this);
        vista.btNuevo.addActionListener(this);

        // Agregando los Tabla a escuchar
        vista.tabla.getSelectionModel().addListSelectionListener(this);

        // jCalendars
        vista.dcFecha.addPropertyChangeListener("date", this);

        // ComboBox
        vista.cbCamion.addActionListener(this);

    }

    public void iniciar() {
        menu.fondo.add(vista);
        vista.setVisible(true);
        menu.fondo.moveToFront(vista);
        vista.requestFocus();
        vista.dcFecha.setDate(new Date());
        vista.btNuevo.setEnabled(true);
        configurarCombo();
        configuraTabla();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btNuevo) {
            if (comandos.tienePermiso(usuario, obj, "Crear")) {
                objActual = null;
                vista.btGuardar.setEnabled(true);
                vista.txOdometro.requestFocus();
            } else {
                JOptionPane.showMessageDialog(vista, "No tienes permiso para crear Nuevos");
            }
        }

        if (e.getSource() == vista.btGuardar) {
            if (objActual == null) {
                if (comandos.tienePermiso(usuario, obj, "Crear")) {
                    CamionOdometro odometro = crearOdometro();
                    ctrl.crearCamionOdometro(odometro);
                    Camion camion = (Camion) vista.cbCamion.getSelectedItem();
                    System.out.println("Guardando en el Camion los Kilometros");
                    System.out.println("Teorico Odometro " + camion.getOdometro());
                    System.out.println("Odometro Odometro " + camion.getTeorico());
                    camion.setOdometro(odometro.getOdometro());
                    if (odometro.getIgualados()) {
                        camion.setTeorico(odometro.getOdometro());
                        vista.chIgualar.setSelected(false);
                    }
                    System.out.println("Teorico Camion " + camion.getOdometro());
                    System.out.println("Odometro Camion " + camion.getTeorico());
                    ctrl.editarCamion(camion);
                    vista.btEliminar.setEnabled(false);
                    vista.btGuardar.setEnabled(false);
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para crear Nuevos");
                }
            } else {
                if (comandos.tienePermiso(usuario, obj, "Actualizar")) {

                    vista.btEliminar.setEnabled(false);
                    System.out.println("Se Ejecuto btGuardar - actualizar ");
                    vista.btGuardar.setEnabled(false);

                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para Modificar");
                }
            }
        }

        if (e.getSource() == vista.btEliminar) {
            if (usuario.getTipo() == EnumUsuario.SUPERADMIN) {
                ctrl.eliminarCamionOdometro(objActual.getCodigo());
            } else {
                if (comandos.tienePermiso(usuario, obj, "Borrar")) {
                    CamionOdometro odometro = ctrl.traerCamionOdometro(objActual.getCodigo());
                    odometro.setEstado(false);
                    ctrl.editarCamionOdometro(odometro);
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para Borror registro");
                }
            }
            cargarTabla();
            vista.btEliminar.setEnabled(false);
            vista.btGuardar.setEnabled(false);
        }

        if (e.getSource() == vista.btSalir) {
            vista.dispose();
        }

        if (e.getSource() == vista.cbCamion) {
            Camion camion = (Camion) vista.cbCamion.getSelectedItem();
            if (camion != null) {
                vista.txOdometro.setText(camion.getOdometro() + "");
                vista.txTeorico.setText(camion.getTeorico() + "");
                vista.chEstado.setSelected(true);
                vista.chIgualar.setSelected(false);
                cargarTabla();
            }
        }

    }

    @Override
    public void valueChanged(ListSelectionEvent e
    ) {
        if (!e.getValueIsAdjusting()) {
            int fila = vista.tabla.getSelectedRow();
            if (fila != -1) {
                objActual = modelo.getDatoAt(fila);
                vista.btEliminar.setEnabled(true);
                vista.btGuardar.setEnabled(true);
                mostrarObjActual();
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt
    ) {
    }

    private void configurarCombo() {
        List<Camion> lista = ctrl.traerListaCamiones();
        for (Camion camion : lista) {
            if (camion.getEstado()) {
                vista.cbCamion.addItem(camion);
            }
        }
    }

    private void limpiarComboCamion() {
        vista.cbCamion.removeAllItems();
    }

    private void configuraTabla() {
        vista.tabla.setModel(modelo);
        vista.tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ?
        vista.tabla.getColumnModel().getColumn(0).setPreferredWidth(90);
        vista.tabla.getColumnModel().getColumn(1).setPreferredWidth(90);
        vista.tabla.getColumnModel().getColumn(2).setPreferredWidth(100);
        vista.tabla.getColumnModel().getColumn(3).setPreferredWidth(100);
        cargarTabla();
    }

    private void cargarTabla() {
        Camion camion = (Camion) vista.cbCamion.getSelectedItem();
        List<CamionOdometro> lista = ctrl.traerCamionOdometroPorCamion(camion.getCodigo());
        modelo.setData(lista);
    }

    private void mostrarObjActual() {
        //Camion camion = ctrl.traerCamion(objActual.getCodigoCamion());
        //vista.cbCamion.setSelectedItem(camion);
        vista.dcFecha.setDate(Date.from(objActual.getFecha().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        vista.txOdometro.setText(objActual.getOdometro() + "");
        vista.txTeorico.setText(objActual.getTeorico() + "");
        vista.chIgualar.setSelected(objActual.getIgualados());
        vista.chEstado.setSelected(objActual.getEstado());
        vista.tabla.requestFocus();
    }

    private CamionOdometro crearOdometro() {
        CamionOdometro odometro = new CamionOdometro();
        Camion camion = (Camion) vista.cbCamion.getSelectedItem();
        if (objActual == null) {
            odometro.setCodigo(1);
            odometro.setCodigoCamion(camion.getCodigo());
        } else {
            odometro.setCodigo(objActual.getCodigo());
            odometro.setCodigoCamion(objActual.getCodigoCamion());
        }
        odometro.setEstado(vista.chEstado.isSelected());
        odometro.setFecha(vista.dcFecha.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        odometro.setIgualados(vista.chIgualar.isSelected());
        odometro.setOdometro(vista.txOdometro.getValorDouble());
        odometro.setTeorico(vista.txOdometro.getValorDouble());
        return odometro;
    }

} // FIN CLASE CONTROLADOR

class CamionOdometroTableModel extends AbstractTableModel {

    private List<CamionOdometro> lista;
    private String[] columnNames = {"Fecha", "Odometro", "Teorico", "Igualado"};
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public CamionOdometroTableModel() {
        lista = new ArrayList<>();

    }

    public void agregarDato(CamionOdometro obj) {
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
        CamionOdometro obj = lista.get(row);
        switch (col) {
            case 0:
                return obj.getFecha().format(dateFormatter); // Formatear la fecha;
            case 1:
                return formatearImporte(obj.getOdometro()); // Formatear el importe
            case 2:
                return formatearImporte(obj.getTeorico()); // Formatear el importe
            case 3:
                return obj.getIgualados();
            default:
                return null;
        }
    }

    public CamionOdometro getDatoAt(int row) {
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

    public void setData(List<CamionOdometro> obj) {
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
