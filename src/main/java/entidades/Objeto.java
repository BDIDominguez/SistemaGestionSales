package entidades;

import jakarta.persistence.Cacheable;
import java.io.Serializable;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Objects;
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
@Table (name = "Objetos")
public class Objeto implements Serializable {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int codigo;
    String nombre;
    Boolean estado;
    @OneToMany (mappedBy = "objeto", cascade = CascadeType.ALL, fetch = FetchType.LAZY) 
    List<Permiso> permisos;
    
     @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Objeto objeto = (Objeto) obj;
        return codigo == objeto.codigo;  // Ajusta según la estructura de tu clase
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);  // Ajusta según la estructura de tu clase
    }
    
}
