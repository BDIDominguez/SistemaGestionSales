package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import vistas.VistaInicioSesion;
import entidades.Controladora;
import entidades.Usuario;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JOptionPane;
import vistas.VistaPantallaPrincipal;

public class ControladorVistaInicioSesion implements ActionListener, FocusListener, KeyListener {

    VistaInicioSesion vista;
    Usuario usuario;
    ControladorVistaPantallaPrincipal menu;
       
    
    public ControladorVistaInicioSesion(VistaInicioSesion vista, ControladorVistaPantallaPrincipal menu) {
        this.vista = vista;
        this.menu = menu;
        

        vista.btIngresar.addActionListener(this);
        vista.btSalir.addActionListener(this);
        vista.txClave.addActionListener(this);
        vista.txNombre.addActionListener(this);

        vista.txNombre.addFocusListener(this);
        vista.txClave.addFocusListener(this);
        
        vista.txClave.addKeyListener(this);
        vista.txNombre.addKeyListener(this);

        // Impide que se pueda Copiar el Contenido o pegar en el mismo
        vista.txClave.setTransferHandler(null);
        vista.txNombre.setTransferHandler(null);

    }

    public Usuario iniciar() {
        // setLocationRelativeTo(null)
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);

        return usuario;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btSalir) {
            vista.dispose();
        }
        if (e.getSource() == vista.btIngresar) {
            botonIngresar();
        }

    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() == vista.txNombre) {
            vista.txNombre.selectAll();
        }
        if (e.getSource() == vista.txClave) {
            vista.txClave.selectAll();
        }
    }

    @Override
    public void focusLost(FocusEvent e) {

    }

    private void botonIngresar() {
        Boolean existe = false;
        Usuario user = new Usuario();
        user.setEstado(false);
        Controladora persis = new Controladora();
        List<Usuario> lista = persis.traerListaUsuarios();
        for (Usuario usuario : lista) {
            if (usuario.getNombre().equals(vista.txNombre.getText().trim()) && usuario.getClave().equals(vista.txClave.getText().trim())) {
                user = usuario;
                existe = true;
                break;
            }
        }
        usuario = user;
        if (existe && user.getEstado()) {
            if (menu == null){
                VistaPantallaPrincipal menu = new VistaPantallaPrincipal();
                ControladorVistaPantallaPrincipal ctrl = new ControladorVistaPantallaPrincipal(menu, user);
                ctrl.iniciar();
            }else{
                menu.cambioDeUsuario(user);
            }
            vista.dispose();
        } else {
            JOptionPane.showMessageDialog(vista, "El Usuario no esta Habilitado");
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getSource() == vista.txNombre) {
            char c = e.getKeyChar();
            if (!(Character.isLetter(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                e.consume();
            }
        }
        if (e.getSource() == vista.txClave) {
            char tecla = e.getKeyChar();

            // Verificar si la tecla presionada es Enter
            if (tecla == '\n') {
                botonIngresar();
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
}
