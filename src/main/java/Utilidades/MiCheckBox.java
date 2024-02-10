package Utilidades;
import javax.swing.JCheckBox;
import java.awt.Color;
import java.awt.Font;

public class MiCheckBox extends JCheckBox {

    public MiCheckBox() {
        super("Activo");  // Texto predeterminado al crear el JCheckBox
        setFont(new Font("Arial", Font.BOLD, 16));  // Fuente bold, tamaño 16
        actualizarApariencia();  // Método para actualizar la apariencia inicial

        // Agregamos un listener para manejar los cambios en el valor del JCheckBox
        addActionListener(e -> {
            actualizarApariencia();
        });
    }

    private void actualizarApariencia() {
        if (isSelected()) {
            setBackground(Color.GREEN);  // Color de fondo verde cuando está seleccionado (true)
            setText("Activo");
        } else {
            setBackground(Color.RED);  // Color de fondo rojo cuando no está seleccionado (false)
            setText("Eliminado");
        }
    }
    
    @Override
    public void setSelected(boolean estado){
        super.setSelected(estado);
        actualizarApariencia();
    }
    
    
}
