package entidades;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Cacheable(false)
@Entity
@Table(name = "cargamorros")
@NamedQueries({
    @NamedQuery(
            name = "InventarioMorro.findByFecha",
            query = "SELECT e FROM InventarioMorro e WHERE e.fecha = :fecha"
    ),
    @NamedQuery(
            name = "InventarioMorro.findByFechaMorro",
            query = "SELECT e FROM InventarioMorro e WHERE e.fecha = :fecha and e.codigoMorro = :codigo"
    )
})
public class InventarioMorro implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int codigo;
    @Column
    int codigoMorro;
    @Column(length=1000)
    String detalles;
    double barrido;
    double pileta;
    Boolean estado;
    double total;
    LocalDate fecha;
        
    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        InventarioMorro morro = (InventarioMorro) obj;
        return codigo == morro.codigo;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(codigo);
    }
}
