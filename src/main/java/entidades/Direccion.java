package entidades;

import jakarta.persistence.Cacheable;
import java.io.Serializable;
import jakarta.persistence.Column;
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
@Cacheable(false)
@Entity
@Table (name = "Direcciones")
public class Direccion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int codigo;
    @Column
    String designacion;
    String provincia;
    String departamento;
    String localidad;
    String barrio;
    String calle;
    int altura;
    int piso;
    String otros;
    Boolean estado;
    @ManyToOne
    @JoinColumn(name = "proveedor_id") 
    Proveedor proveedor;
    @ManyToOne
    @JoinColumn(name = "contacto_id")
    Contacto contacto;
    
    
}
