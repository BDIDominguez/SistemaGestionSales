package entidades;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Dario
 */

@Data
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
@Cacheable(false)
@Entity
@Table (name = "Contactos")
public class Contacto implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int codigo;
    @Column
    String nombre;
    String apellido;
    String dni;
    Boolean estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contacto", fetch = FetchType.EAGER)
    List<Telefono> telefono;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contacto", fetch = FetchType.EAGER)
    List<Correo> correo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "contacto", fetch = FetchType.EAGER)
    List<Direccion> direccion;
   
}