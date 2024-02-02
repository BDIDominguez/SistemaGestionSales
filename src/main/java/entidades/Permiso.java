package entidades;

import java.io.Serializable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Dario
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
@Entity
@Table (name = "Permisos")
public class Permiso implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int codigo;
    Boolean ingresar;
    Boolean crear;
    Boolean leer;
    Boolean actualizar;
    Boolean borrar;
    Boolean Imprimir;
    Boolean estado;
    @ManyToOne
    @JoinColumn(name = "usuario_codigo" ) 
    Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "objeto_codigo")
    Objeto objeto;

   
}
