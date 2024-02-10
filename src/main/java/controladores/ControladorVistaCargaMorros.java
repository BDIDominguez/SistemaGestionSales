package controladores;

import entidades.CargaMorro;
import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.EnumUsuario;
import entidades.Morro;
import entidades.Objeto;
import entidades.Usuario;
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
import vistas.VistaCargaMorros;
import vistas.VistaPantallaPrincipal;

public class ControladorVistaCargaMorros implements ActionListener, ListSelectionListener, KeyListener, PropertyChangeListener, FocusListener {

    VistaPantallaPrincipal menu;
    VistaCargaMorros vista;
    ComandosComunes comandos;
    Usuario usuario;
    Controladora ctrl;
    Objeto obj;
    CargaMorroTableModel modelo = new CargaMorroTableModel();
    CargaMorro cargaMorro;

    public ControladorVistaCargaMorros(VistaPantallaPrincipal menu, VistaCargaMorros vista, ComandosComunes comandos, Usuario usuario, Controladora ctrl, Objeto obj) {
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

        // escuchando los Text
        vista.txPileta.addActionListener(this);
        vista.txPileta.addActionListener(this);

        // Escuchando Focus
        vista.txPileta.addFocusListener(this);
        vista.txBarrido.addFocusListener(this);

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
        cargaComboBox();
        configuraTabla();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btSalir) {
            vista.dispose();
        }
        if (e.getSource() == vista.btNuevo) {
            if (comandos.tienePermiso(usuario, obj, "Crear")) {
                cargaMorro = null;
                vista.txBarrido.setText("0.0");
                vista.txObs.setText("");
                vista.txPileta.setText("0.0");
                vista.txTotal.setText("0.0");
                vista.chEstado.setSelected(true);
                vista.btEliminar.setEnabled(false);
                vista.btGuardar.setEnabled(true);
                vista.btNuevo.setEnabled(false);
                vista.cbCamion.requestFocus();
            } else {
                JOptionPane.showMessageDialog(vista, "No tienes permiso para Crear nuevas Cargas");
            }
        }
        if (e.getSource() == vista.btGuardar) {
            if (cargaMorro == null) {
                if (comandos.tienePermiso(usuario, obj, "Crear")) {
                    CargaMorro carga = crearCargaMorro();
                    ctrl.crearCargaMorro(carga);
                    Morro morro = ctrl.traerMorro(carga.getCodigoMorro()); // Se trae el morro para poder cargar su Stock
                    morro.agregarAlMorro(carga.getTotal(),carga.getPileta(),carga.getBarrido()); // Se agrega al Stock actual del morro el total de la carga actual
                    ctrl.editarMorro(morro); // Se Guarga el Morro con su Stock Modificado
                    vista.btEliminar.setEnabled(false);
                    vista.btGuardar.setEnabled(false);
                    vista.btNuevo.setEnabled(true);
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para Crear nuevas Cargas");
                }
            } else {
                if (comandos.tienePermiso(usuario, obj, "Actualizar")) {
                    CargaMorro carga = crearCargaMorro();
                    carga.setCodigo(cargaMorro.getCodigo());
                    ctrl.editarCargaMorro(carga);   // al ser una modificacion tenemos que Actualizar atributo Sock del morro para llevar bien la cuenta ya sea porque se anulo la carga o se modifico el monto
                    if (carga.getEstado()) { // No se modifico el estado por ende se comprueban las cantidades
                        if (carga.getTotal() != cargaMorro.getTotal()) { // si los valores sondistintos se requiere corregir el stock ya que primero se descontaron 2 pero luego se descontaron 3 por ende el stock cambio de forma distinta
                            Morro morro = ctrl.traerMorro(carga.getCodigoMorro());
                            morro.quitarDelMorro(cargaMorro.getTotal(),cargaMorro.getPileta(),cargaMorro.getBarrido()); // se descuenta lo anteriormente agregado
                            morro.agregarAlMorro(carga.getTotal(),carga.getPileta(),carga.getBarrido()); // Se carga en el Morro la nueva cantida de Ingreso en el Morro
                        }
                    }else{ // Se Anulo la Carga por ende se debe quitar todo lo agergado
                        Morro morro = ctrl.traerMorro(carga.getCodigoMorro());
                        morro.quitarDelMorro(cargaMorro.getTotal(),cargaMorro.getPileta(),cargaMorro.getBarrido()); // se descuenta lo anteriormente agregado
                    }
                    vista.btEliminar.setEnabled(false);
                    vista.btGuardar.setEnabled(false);
                    vista.btNuevo.setEnabled(true);
                    cargarTabla();
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para Crear nuevas Cargas");
                }
            }
        }
        if (e.getSource() == vista.btEliminar) {
            if (usuario.getTipo() == EnumUsuario.SUPERADMIN) {
                ctrl.eliminarCargaMorro(cargaMorro.getCodigo()); // Si se elimina correctamente se debe corregir el Stock en el morro
                Morro morro = ctrl.traerMorro(cargaMorro.getCodigoMorro());
                morro.quitarDelMorro(cargaMorro.getTotal(),cargaMorro.getPileta(),cargaMorro.getBarrido()); // Se quita lo que supuestamente este movimiento Agrego
                ctrl.editarMorro(morro); //Se Guarda el Morro con el Stock Corregido
                cargarTabla();
            } else {
                if (comandos.tienePermiso(usuario, obj, "Borrar")) {
                    cargaMorro.setDetalles(cargaMorro.getDetalles() + "** Eliminado por el Usuario: " + usuario.getNombre() + "**");
                    cargaMorro.setEstado(false);
                    Morro morro = ctrl.traerMorro(cargaMorro.getCodigoMorro());
                    ctrl.editarCargaMorro(cargaMorro);
                    morro.quitarDelMorro(cargaMorro.getTotal(),cargaMorro.getPileta(),cargaMorro.getBarrido()); // Se quita lo que supuestamente este movimiento Agrego
                    ctrl.editarMorro(morro); //Se Guarda el Morro con el Stock Corregido
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
                cargaMorro = modelo.getDatoAt(fila);
                vista.btNuevo.setEnabled(true);
                vista.btEliminar.setEnabled(true);
                vista.btGuardar.setEnabled(true);
                mostrarCargaMorro();
            }
        }
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("date".equals(evt.getPropertyName())) {
            cargarTabla();
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() == vista.txPileta || e.getSource() == vista.txBarrido) {
            totalizarProduccionDiaria();
        }
    }

    private void totalizarProduccionDiaria() {
        double a = vista.txPileta.getValorDouble() + vista.txBarrido.getValorDouble();
        vista.txTotal.setText(a + "");
    }

    private void configuraTabla() {
        vista.tabla.setModel(modelo);
        vista.tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ?
        vista.tabla.getColumnModel().getColumn(0).setPreferredWidth(90);
        vista.tabla.getColumnModel().getColumn(1).setPreferredWidth(123);
        vista.tabla.getColumnModel().getColumn(2).setPreferredWidth(123);
        vista.tabla.getColumnModel().getColumn(3).setPreferredWidth(141);
        cargarTabla();
    }

    private void cargarTabla() {
        List<CargaMorro> lista = new ArrayList();
        modelo.setData(lista);
        LocalDate fecha = vista.dcFecha.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        lista = ctrl.traerCargasMorroConFecha(fecha);
        double total = 0.0;
        for (CargaMorro cm : lista) { // Solo se podra ver los que estan marcados como activos ya que cuando los Eliminas los Eliminas. tener en cuenta que el programa
            if (cm.getEstado()) {     // no contempla el stock en caso de pasarlo de desactivado a activado!!!!
                modelo.agregarDato(cm);
                total = total + cm.getTotal();
            }
        }
        vista.txTotalDiario.setText(total + "");
    }

    private void mostrarCargaMorro() {
        vista.txBarrido.setText(cargaMorro.getBarrido() + "");
        vista.txPileta.setText(cargaMorro.getPileta() + "");
        vista.txTotal.setText(cargaMorro.getTotal() + "");
        vista.txObs.setText(cargaMorro.getDetalles());
        vista.chEstado.setSelected(cargaMorro.getEstado());
        vista.cbCamion.setSelectedItem(ctrl.traerMorro(cargaMorro.getCodigoMorro()));
    }

    private void cargaComboBox() {
        List<Morro> lista = ctrl.traerListaMorros();
        for (Morro cm : lista) {
            if (cm.getEstado()) {
                vista.cbCamion.addItem(cm);
            }
        }
    }

    private CargaMorro crearCargaMorro() {
        CargaMorro carga = new CargaMorro();
        carga.setCodigo(1);
        carga.setBarrido(vista.txBarrido.getValorDouble());
        Morro morro = (Morro) vista.cbCamion.getSelectedItem();
        carga.setCodigoMorro(morro.getCodigo());
        carga.setDetalles(vista.txObs.getText());
        carga.setEstado(vista.chEstado.isSelected());
        carga.setFecha(vista.dcFecha.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        carga.setPileta(vista.txPileta.getValorDouble());
        carga.setTotal(vista.txTotal.getValorDouble());
        return carga;
    }
}

class CargaMorroTableModel extends AbstractTableModel {

    private List<CargaMorro> lista;
    private String[] columnNames = {"Cod. Morro", "Barrido", "Pileta", "Total"};
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public CargaMorroTableModel() {
        lista = new ArrayList<>();

    }

    public void agregarDato(CargaMorro combustible) {
        lista.add(combustible);
        fireTableRowsInserted(lista.size() - 1, lista.size() - 1);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return lista.size();
    }

    public Object getValueAt(int row, int col) {
        CargaMorro obj = lista.get(row);
        switch (col) {
            case 0:
                //return obj.getFecha().format(dateFormatter); // Formatear la fecha;
                return obj.getCodigoMorro();
            case 1:
                return formatearImporte(obj.getBarrido());
            case 2:
                return formatearImporte(obj.getPileta());
            case 3:
                return formatearImporte(obj.getTotal()); // Formatear el importe
            default:
                return null;
        }
    }

    public CargaMorro getDatoAt(int row) {
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

    public void setData(List<CargaMorro> obj) {
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
