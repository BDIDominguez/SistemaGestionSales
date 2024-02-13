package entidades;

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
@FieldDefaults(level=AccessLevel.PRIVATE)
@Entity
@Table(name = "CamionesOdometro")
@NamedQueries({
    @NamedQuery(
            name = "CamionOdometro.findByFecha",
            query = "SELECT e FROM CamionOdometro e WHERE e.fecha = :fecha"
    ),
    @NamedQuery(
            name = "CamionOdometro.findByFechaCamion",
            query = "SELECT e FROM CamionOdometro e WHERE e.fecha = :fecha and e.codigoCamion = :codigo"
    ),
    @NamedQuery(
            name = "CamionOdometro.findByCamion",
            query = "SELECT e FROM CamionOdometro e WHERE e.codigoCamion = :codigo order by e.codigo"
    )
})
public class CamionOdometro implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int codigo;
    @Column
    int codigoCamion;
    double odometro;
    double teorico;
    Boolean estado;
    Boolean igualados;
    LocalDate fecha;

    
    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        CamionOdometro camion = (CamionOdometro) obj;
        return codigo == camion.codigo;
    }
    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
    
    /* METODOS DE LA CLASE */    
    public void sumarTeorico(double viaje){
        this.teorico = this.teorico + viaje;
    }
        
}
