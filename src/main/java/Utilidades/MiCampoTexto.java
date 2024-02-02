package Utilidades;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class MiCampoTexto extends JTextField {

    public MiCampoTexto() {
        super();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) { // Solo carga Caracteres Letras no permite el ingreso de otros datos
                char c = e.getKeyChar();
                if (!(Character.isLetterOrDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_SPACE)) {
                    e.consume();
                }
            }
        });
        // Seleccionar todo el contenido cuando se lo selecciona
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                selectAll();
            }
        });
        // Evitar la opcion de pegar texto
        setTransferHandler(null);

    }

}
