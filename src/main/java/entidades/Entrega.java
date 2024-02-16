package entidades;

import jakarta.persistence.Cacheable;
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
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Cacheable(false)
@Entity
@Table(name = "entregas")
@NamedQueries({
    @NamedQuery(
            name = "Entrega.findByFechaSalida",
            query = "SELECT e FROM Entrega e WHERE e.fecsalida = :fechaSalida"
    ),
    @NamedQuery(
            name = "Entrega.findByRemito",
            query = "SELECT e FROM Entrega e WHERE e.remito = :remito"
    ),
    @NamedQuery(
            name = "Entrega.findBetweenSalidas",
            query = "SELECT e FROM Entrega e WHERE e.fecsalida >= :fechaDesde and e.fecsalida <= :fechaHasta"
    ),
    @NamedQuery(
            name = "Entrega.findBetweenEntrega",
            query = "SELECT e FROM Entrega e WHERE e.fecentrega >= :fechaDesde and e.fecentrega <= :fechaHasta"
    ),
    @NamedQuery(
            name = "Entrega.findByClienteAConfirmar",
            query = "SELECT e FROM Entrega e WHERE e.cliente = :cliente AND e.etapa = 0 ORDER BY e.remito, e.fecsalida"
    )

})

public class Entrega implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int codigo;
    @Column
    String remito;
    double teorico;
    double confirmado;
    Boolean estado;
    LocalDate fecentrega;
    LocalDate fecsalida;
    String factura;
    int etapa; // 0 cargada en viaje -- 1 llego a destino y se confirmo el peso ya no se puede corregir --- 3 ya Facturado y Cobrado!!

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "camion_id")
    Camion camion;

    @ManyToOne
    @JoinColumn(name = "morro_id")
    Morro morro;

}
