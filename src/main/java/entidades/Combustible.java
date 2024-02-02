package entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Dario
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table (name="combustibles")
@NamedQueries({
    @NamedQuery(
            name = "Combustible.findByFecha",
            query = "SELECT e FROM Combustible e WHERE e.fecha = :fecha"
    ),
    @NamedQuery(
            name = "Combustible.findNumeroFactura",
            query = "SELECT e FROM Combustible e WHERE e.nroFactura = :factura"
    )

})
public class Combustible implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int codigo;
    @Column
    double importe;
    double litros;
    String tipo;
    LocalDate fecha;
    LocalTime hora;
    Boolean estado;
    String estacion;
    String nroFactura;
    @ManyToOne
    @JoinColumn(name = "camion_id")
    Camion camion;
        
}
