package entidades;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Dario
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "morros")
public class Morro implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int codigo;
    @Column
    String nombre;
    String detalles;
    double barrido;
    double pileta;
    Boolean estado;
    double stock;
    
    @OneToMany(mappedBy = "morro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Entrega> Entregas;
   
    
    public double totalMorro(){
        return barrido+pileta;
    }
    
    @Override
    public String toString() {
        return nombre ;
    }
    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()){
            return false;
        }
        Morro morro = (Morro) obj;
        return codigo == morro.codigo;
    }
    @Override
    public int hashCode(){
        return Objects.hash(codigo);
    }
}
