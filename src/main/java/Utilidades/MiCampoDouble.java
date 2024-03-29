package Utilidades;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import javax.swing.JTextField;

public class MiCampoDouble extends JTextField {

    private static final DecimalFormat formatoDecimal = new DecimalFormat("#,##0.00");

    public MiCampoDouble() {
        super();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) { // Solo carga Caracteres Letras no permite el ingreso de otros datos
                char c = e.getKeyChar();
                /*
                if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || (c == '.' && getText().indexOf('.') == -1))) {
                    e.consume();
                }
                 */
                // Verificar si es un dígito, punto, retroceso o eliminar
                if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE || (c == '.' && getText().indexOf('.') == -1))) {
                    e.consume();
                } else if (c == '.' && getText().contains(".")) {
                    // Bloquear más de un punto decimal
                    e.consume();
                //} else if (getText().contains(".") && getText().substring(getText().indexOf(".") + 1).length() >= 2) {
                } else if (getText().contains(".") && !isAllTextSelected() && getText().substring(getText().indexOf(".") + 1).length() >= 2) {
                    // Bloquear más de dos decimales
                    e.consume();
                }
            }
        });
        // Método para verificar si todo el texto está seleccionado
    
        // Seleccionar todo el contenido cuando se lo selecciona
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                formatearTexto();
            }

            @Override
            public void focusGained(FocusEvent e) {
                selectAll();
            }

        });
        // Evitar la opcion de pegar texto
        setTransferHandler(null);

    }
    private boolean isAllTextSelected() {
        return getSelectionStart() == 0 && getSelectionEnd() == getText().length();
    }

    private void formatearTexto() {
        try {
            String texto = getText();
            if (texto.contains(",")){
                texto = texto.replace(".", "").replace(",", ".");
            }
            double numero = Double.parseDouble(texto);
            setText(formatoDecimal.format(numero));
        } catch (NumberFormatException ex) {
            setText("0.00");
        }
    }
}
