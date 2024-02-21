package controladores;

import entidades.Camion;
import entidades.Cliente;
import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.Entrega;
import entidades.EnumUsuario;
import entidades.Morro;
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
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import vistas.VistaEntregas;
import vistas.VistaPantallaPrincipal;

public class ControladorVistaEntregas implements ActionListener, ListSelectionListener, KeyListener, PropertyChangeListener, FocusListener {

    VistaPantallaPrincipal menu;
    VistaEntregas vista;
    ComandosComunes comandos = new ComandosComunes();
    Usuario usuario;
    Controladora ctrl = new Controladora();
    Objeto obj;
    EntregasTableModel modelo = new EntregasTableModel();
    boolean nuevoEntrega = false;  // bandera que indica que este registro es nuevo, para poder saber si se guarda o edita el registro!!!

    public ControladorVistaEntregas(VistaPantallaPrincipal menu, VistaEntregas vista, Usuario usuario, Objeto obj) {
        this.menu = menu;
        this.vista = vista;
        this.usuario = usuario;
        this.obj = obj;

        // Agregando Botones a escuchar
        vista.btEliminar.addActionListener(this);
        vista.btGuardar.addActionListener(this);
        vista.btNuevo.addActionListener(this);
        vista.btSalir.addActionListener(this);

        // Agregando Text a escuchar
        vista.txSerie.addActionListener(this);
        vista.txTeorico.addActionListener(this);

        // Agregnado Combos a escuchar
        vista.cbCliente.addActionListener(this);
        vista.cbMorro.addActionListener(this);

        // Agregando los Tabla a escuchar
        vista.tabla.getSelectionModel().addListSelectionListener(this);

        // jCalendars
        vista.dcFecha.addPropertyChangeListener("date", this);

        // Capturando precionado de teclas
        vista.txRemito.addKeyListener(this);
        vista.txSerie.addKeyListener(this);
        vista.txTeorico.addKeyListener(this);

        // Focus Listener
        vista.txRemito.addFocusListener(this);
        vista.txTeorico.addFocusListener(this);
        vista.txSerie.addFocusListener(this);

    }

    public void iniciar() {
        menu.fondo.add(vista);
        vista.setVisible(true);
        menu.fondo.moveToFront(vista);
        vista.requestFocus();
        vista.btGuardar.setEnabled(false);
        vista.btEliminar.setEnabled(false);
        vista.dcFecha.setDate(new Date());
        cargarClientes();
        cargarMorros();
        cargarCamiones();
        modelarTabla();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btSalir) {
            vista.dispose();
        }
        if (e.getSource() == vista.btNuevo) {
            if (comandos.tienePermiso(usuario, obj, "Crear")) {
                vista.dcFecha.setDate(new Date());
                String remito = ctrl.ultimoNumeroRemitoEntrega();
                String cadena;
                if (remito.equalsIgnoreCase("01")) {
                    cadena = "00000001";
                } else {
                    String[] comp = remito.split("-");
                    vista.txSerie.setText(comp[0]);
                    int numero = Integer.parseInt(comp[1]);
                    numero = numero + 1;
                    cadena = numero + "";
                    int cantidadDeCeros = 8 - cadena.length();
                    cadena = String.format("%0" + cantidadDeCeros + "d%s", 0, cadena);
                }
                vista.txRemito.setText(cadena);
                vista.txTeorico.setText("0,00");
                vista.btGuardar.setEnabled(true);
                vista.btEliminar.setEnabled(false);
                vista.btNuevo.setEnabled(false);
                nuevoEntrega = true;
                vista.txRemito.setBackground(Color.WHITE);
                vista.cbCliente.requestFocus();
            } else {
                JOptionPane.showMessageDialog(vista, "No tienes permiso para crear Nuevos");
            }
        }
        if (e.getSource() == vista.btGuardar) {
            if (nuevoEntrega) {
                if (comandos.tienePermiso(usuario, obj, "Crear")) {
                    Entrega entrega = new Entrega();
                    entrega.setCodigo(1);
                    Camion camion = (Camion) vista.cbCamion.getSelectedItem();
                    Cliente cliente = (Cliente) vista.cbCliente.getSelectedItem();
                    camion.sumarTeorico(cliente.getDistancia() * 2);
                    ctrl.editarCamion(camion);
                    entrega.setCamion(camion);
                    entrega.setCliente(cliente);
                    entrega.setConfirmado(0);
                    entrega.setFecentrega(LocalDate.now());
                    entrega.setFecsalida(vista.dcFecha.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    entrega.setMorro((Morro) vista.cbMorro.getSelectedItem());
                    entrega.setRemito(vista.txSerie.getText() + "-" + vista.txRemito.getText());
                    entrega.setTeorico(vista.txTeorico.getValorDouble());
                    entrega.setEtapa(0);
                    entrega.setFactura("");
                    entrega.setEstado(true);
                    ctrl.crearEntrega(entrega);
                    JOptionPane.showMessageDialog(vista, "Se Cargo la engtrega de forma correcta!");
                    cargarTablas();
                    vista.btGuardar.setEnabled(false);
                    vista.btNuevo.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para crear Nuevos");
                }
            } else { // Tengo que guardar las modificaciones XD
                if (comandos.tienePermiso(usuario, obj, "Actualizar")) {
                    int fila = vista.tabla.getSelectedRow();
                    if (fila != -1) {
                        Entrega entrega = modelo.getEntregaAt(fila);
                        String remito = entrega.getRemito();
                        entrega.setCamion((Camion) vista.cbCamion.getSelectedItem());
                        entrega.setCliente((Cliente) vista.cbCliente.getSelectedItem());
                        entrega.setFecsalida(vista.dcFecha.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        entrega.setMorro((Morro) vista.cbMorro.getSelectedItem());
                        entrega.setEstado(true);
                        entrega.setTeorico(vista.txTeorico.getValorDouble());
                        String nuevoRemito = vista.txSerie.getText() + "-" + vista.txRemito.getText();
                        boolean actualizar = true;
                        if (!remito.equalsIgnoreCase(nuevoRemito)) {
                            if (ctrl.existeNumeroRemito(nuevoRemito)) {
                                JOptionPane.showMessageDialog(vista, "El nuevo Numero de Remito ya esta en uso!!");

                                actualizar = false;
                            }
                        }
                        if (actualizar) {
                            entrega.setRemito(nuevoRemito);
                            ctrl.editarEntrega(entrega);
                            cargarTablas();
                            modelo.actualizarDatosEnFila(fila);
                        }
                    } else {
                        JOptionPane.showMessageDialog(vista, "No se puede generar el Objeto Entrega para su modificacion");
                    }
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para Modificar");
                }
                vista.btEliminar.setEnabled(false);
                vista.btGuardar.setEnabled(false);
                vista.btNuevo.setEnabled(true);
            }

        }
        if (e.getSource() == vista.btEliminar) {
            if (usuario.getTipo() == EnumUsuario.SUPERADMIN) {
                int fila = vista.tabla.getSelectedRow();
                if (fila != -1) {
                    Entrega entrega = modelo.getEntregaAt(fila);
                    fila = entrega.getCodigo();
                    ctrl.eliminarEntrega(fila);
                } else {
                    JOptionPane.showMessageDialog(vista, "No se puede consultar codigo de Objeto, para eliminar");
                }
            } else {
                if (comandos.tienePermiso(usuario, obj, "Borrar")) {
                    int fila = vista.tabla.getSelectedRow();
                    if (fila != -1) {
                        Entrega entrega = modelo.getEntregaAt(fila);
                        entrega.setEstado(false);
                        ctrl.editarEntrega(entrega);

                    } else {
                        JOptionPane.showMessageDialog(vista, "No se puede consultar codigo de Objeto, para eliminar");
                    }
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permisos para Eliminar");
                }
            }
            vista.btEliminar.setEnabled(false);
            vista.btGuardar.setEnabled(false);
            vista.btNuevo.setEnabled(true);
            cargarTablas();
        }

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int fila = vista.tabla.getSelectedRow();
            if (fila != -1) {
                Entrega entrega = modelo.getEntregaAt(fila);
                vista.btEliminar.setEnabled(true);
                vista.btGuardar.setEnabled(true);
                nuevoEntrega = false;
                mostrarEntrega(entrega);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == vista.txSerie) {
            char c = e.getKeyChar();
            if (!(c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || Character.isDigit(c))) {
                e.consume();
            }
        }
        if (e.getSource() == vista.txRemito) {
            char c = e.getKeyChar();
            if (!(c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || Character.isDigit(c))) {
                e.consume();
            }
        }
        if (e.getSource() == vista.txTeorico) {
            char c = e.getKeyChar();
            if (!(c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || Character.isDigit(c) || (c == '.' && vista.txTeorico.getText().indexOf('.') == -1))) {
                e.consume();
            }
        }
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
            cargarTablas();
            // Manejar el cambio de la fecha aquí
            // Puedes obtener la nueva fecha con: (Date) evt.getNewValue()
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() == vista.txRemito) {
            vista.txRemito.selectAll();
        }
        if (e.getSource() == vista.txSerie) {
            vista.txSerie.selectAll();
        }
        if (e.getSource() == vista.txTeorico) {
            vista.txTeorico.selectAll();
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() == vista.txRemito) {
            if (vista.txRemito.getText().length() < 8) { // Configuramos que este relleno de ceros y que sean 8 digitos
                String cadena = vista.txRemito.getText();
                int cantidadDeCeros = 8 - cadena.length();
                vista.txRemito.setText(String.format("%0" + cantidadDeCeros + "d%s", 0, cadena));
            }
            String remito = vista.txSerie.getText() + "-" + vista.txRemito.getText();
            if (ctrl.existeNumeroRemito(remito) && nuevoEntrega) {
                JOptionPane.showMessageDialog(vista, "Ese numero de Remito ya fue Cargado!!!");
                vista.btGuardar.setEnabled(false);
                vista.txRemito.requestFocus();
            } else {
                if (vista.btGuardar.isEnabled()) {
                    vista.btGuardar.setEnabled(true);
                }
            }
        }
        if (e.getSource() == vista.txSerie) {
            if (vista.txSerie.getText().length() < 4) {
                String cadena = vista.txSerie.getText();
                int cantidadDeCeros = 4 - cadena.length();
                vista.txSerie.setText(String.format("%0" + cantidadDeCeros + "d%s", 0, cadena));
            }
        }
    }

    private void cargarClientes() {
        List<Cliente> lista = ctrl.traerListaClientes();
        for (Cliente cliente : lista) {
            vista.cbCliente.addItem(cliente);
        }
    }

    private void cargarMorros() {
        List<Morro> lista = ctrl.traerListaMorros();
        for (Morro morro : lista) {
            vista.cbMorro.addItem(morro);
        }

    }

    private void cargarCamiones() {
        List<Camion> lista = ctrl.traerListaCamiones();
        for (Camion camion : lista) {
            vista.cbCamion.addItem(camion);
        }

    }

    private void modelarTabla() {
        vista.tabla.setModel(modelo);
        vista.tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 538
        vista.tabla.getColumnModel().getColumn(0).setPreferredWidth(298);
        vista.tabla.getColumnModel().getColumn(1).setPreferredWidth(90);
        vista.tabla.getColumnModel().getColumn(2).setPreferredWidth(150);
        cargarTablas();
    }

    private void cargarTablas() { // Se consultara las Entregas con la misma fecha del campo dcFecha osea las del dia al que se quiere agregar
        List<Entrega> lista = new ArrayList();
        modelo.setData(lista);
        LocalDate fecha = vista.dcFecha.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        lista = ctrl.traerListaEntregasPorFechaSalida(fecha);
        for (Entrega entrega : lista) {
            if (entrega.getEtapa() == 0) {
                modelo.agregarEntregas(entrega);
            }
        }
        vista.txRemito.setBackground(Color.WHITE);
    }

    private void mostrarEntrega(Entrega entrega) {
        // Obtener la zona horaria por defecto del sistema
        ZoneId defaultZoneId = ZoneId.systemDefault();
        // Convertir LocalDate a Date
        Date fecha = Date.from(entrega.getFecsalida().atStartOfDay(defaultZoneId).toInstant());
        vista.dcFecha.setDate(fecha);
        vista.cbCliente.setSelectedItem(entrega.getCliente());
        String[] comp = entrega.getRemito().split("-");
        vista.txSerie.setText(comp[0]);
        vista.txRemito.setText(comp[1]);
        vista.cbMorro.setSelectedItem(entrega.getMorro());
        vista.cbCamion.setSelectedItem(entrega.getCamion());
        vista.txTeorico.setText(entrega.getTeorico() + " ");
        if (entrega.getEstado()) {
            vista.txRemito.setBackground(Color.WHITE);
        } else {
            vista.txRemito.setBackground(Color.RED);
        }
    }

}

class EntregasTableModel extends AbstractTableModel {

    private List<Entrega> entregas;
    private String[] columnNames = {"Cliente", "Toneladas", "Remito"};

    public EntregasTableModel() {
        entregas = new ArrayList<>();

    }

    public void agregarEntregas(Entrega entrega) {
        entregas.add(entrega);
        fireTableRowsInserted(entregas.size() - 1, entregas.size() - 1);
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return entregas.size();
    }

    public Object getValueAt(int row, int col) {
        Entrega entrega = entregas.get(row);
        switch (col) {
            case 0:
                return entrega.getCliente().getNombre();
            case 1:
                return formatearImporte(entrega.getTeorico());
            case 2:
                return entrega.getRemito();
            default:
                return null;
        }
    }
  
    public Entrega getEntregaAt(int row) {
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

    public void setData(List<Entrega> entregas) {
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
