package controladores;

import entidades.Camion;
import entidades.CamionOdometro;
import entidades.ComandosComunes;
import entidades.Controladora;
import entidades.Objeto;
import entidades.Usuario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import vistas.VistaConfirmarOdometro;
import vistas.VistaPantallaPrincipal;

public class ControladorVistaConfirmarOdometro implements ActionListener, ListSelectionListener, PropertyChangeListener {

    VistaPantallaPrincipal menu;
    VistaConfirmarOdometro vista;
    ComandosComunes comandos;
    Usuario usuario;
    Controladora ctrl;
    Objeto obj;
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
        vista.btNuevo.addActionListener(this);

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
        vista.dcFecha.setEnabled(false);
        vista.txOdometro.setEnabled(false);
        vista.chIgualar.setEnabled(false);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btNuevo) {
            if (vista.btNuevo.getText().equalsIgnoreCase("Nuevo")) { // Cuando se preciona el boton Nuevo este prepara todo y cambia el Texto a Guardar Para Guardar los cambios
                if (comandos.tienePermiso(usuario, obj, "Crear")) {
                    objActual = null;
                    vista.chIgualar.setSelected(false);
                    vista.txOdometro.requestFocus();
                    vista.dcFecha.setEnabled(true);
                    vista.txOdometro.setEnabled(true);
                    vista.chIgualar.setEnabled(true);
                    vista.btNuevo.setText("Guardar");
                } else {
                    JOptionPane.showMessageDialog(vista, "No tienes permiso para crear Nuevos");
                }
            } else { // Cuando el Texto del Boton Dice Guardar Guarda los cambios echos al Odometro
                if (objActual == null) {
                    if (comandos.tienePermiso(usuario, obj, "Crear")) {
                        CamionOdometro odometro = crearOdometro();
                        ctrl.crearCamionOdometro(odometro);
                        Camion camion = (Camion) vista.cbCamion.getSelectedItem();
                        camion.setOdometro(odometro.getOdometro());
                        if (odometro.getIgualados()) {
                            camion.setTeorico(odometro.getOdometro());
                            vista.chIgualar.setSelected(false);
                        }
                        ctrl.editarCamion(camion);
                        actualizarCombo();
                        vista.dcFecha.setEnabled(false);
                        vista.txOdometro.setEnabled(false);
                        vista.chIgualar.setEnabled(false);
                        vista.btNuevo.setText("Nuevo");
                    } else {
                        JOptionPane.showMessageDialog(vista, "No tienes permiso para crear Nuevos");
                    }
                } else {
                    /*   // No Tiene Opcion de Edicion ya que es muy complejo regresar las cosas para Atraz!!!
                    if (comandos.tienePermiso(usuario, obj, "Actualizar")) {
                        System.out.println("Se Ejecuto btGuardar - actualizar ");
                    } else {
                        JOptionPane.showMessageDialog(vista, "No tienes permiso para Modificar");
                    }
                    */
                }
            }

        }

        if (e.getSource() == vista.btSalir) {
            vista.dispose();
        }

        if (e.getSource() == vista.cbCamion) {
            Camion camion = (Camion) vista.cbCamion.getSelectedItem();
            if (camion != null) {
                vista.txOdometro.setText(camion.getOdometro() + "");
                vista.txTeorico.setText(camion.getTeorico() + "");
                vista.chIgualar.setSelected(false);
            }
        }

    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
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
        odometro.setEstado(true);
        odometro.setFecha(vista.dcFecha.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        odometro.setIgualados(vista.chIgualar.isSelected());
        odometro.setOdometro(vista.txOdometro.getValorDouble());
        odometro.setTeorico(vista.txOdometro.getValorDouble());
        return odometro;
    }

    private void actualizarCombo() {
        Camion camion = (Camion) vista.cbCamion.getSelectedItem();
        limpiarComboCamion();
        configurarCombo();
        vista.cbCamion.setSelectedItem(camion);
    }

} // FIN CLASE CONTROLADOR
