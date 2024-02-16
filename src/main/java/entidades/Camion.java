package entidades;

import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level=AccessLevel.PRIVATE)
@Cacheable(false)
@Entity
@Table(name = "Camiones")
public class Camion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int codigo;
    @Column
    String patente;
    double odometro;
    double teorico;
    @OneToOne (mappedBy = "camion", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    Chofer chofer;
    @OneToMany(mappedBy = "camion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Combustible> cargas;
    @OneToMany(mappedBy = "camion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Entrega> entregas;
    Boolean estado;
    
    @Override
    public String toString() {
        return patente ;
    }
    
    public double diferenciaTeorica(){
        return odometro-teorico;
    }
    
    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        Camion camion = (Camion) obj;
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
