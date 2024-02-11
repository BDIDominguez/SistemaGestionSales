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
@Cacheable(false)
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

    public void agregarAlMorro(double total, double dpileta, double dbarrido) {
        this.stock = this.stock + total;
        agregarBarrido(dbarrido);
        agregarPileta(dpileta);
        if (this.pileta > 5000000000000.0 || this.barrido > 5000000000000.0){ // Si alguno de los dos supera este monto se recalculara los valoes minimos para mantener el % de cada uno
            recalcularBarridoPileta();
        }
    }

    public void quitarDelMorro(double total, double dpileta, double dbarrido) {
        this.stock = stock - total;
        quitarBarrido(dbarrido);
        quitarPileta(dpileta);
    }
    
    private void agregarBarrido(double cantidad){
        this.barrido = this.barrido + cantidad;
    }
    private void agregarPileta(double cantidad){
        this.pileta = this.pileta + cantidad;
    }
    private void quitarBarrido(double cantidad){
        this.barrido = this.barrido - cantidad;
    }
    private void quitarPileta(double cantidad){
        this.pileta = this.pileta - cantidad;
    }
    
    public void recalcularBarridoPileta(){
        double total =  this.barrido + this.pileta;
        double porBarrido = (this.barrido * 100 )/ total;
        double porPileta = (this.pileta * 100 )/ total;
        double nuevaPileta = (porPileta * 10 )/ 100;
        double nuevaBarrido = (porBarrido * 10 )/ 100;
        this.barrido = nuevaBarrido;
        this.pileta = nuevaPileta;
    }
}
