package persistencias;

import entidades.CamionOdometro;
import entidades.CargaMorro;
import entidades.Cliente;
import entidades.Combustible;
import entidades.Entrega;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.sql.Timestamp;


import java.util.List;

public class ConsultasJPA {

    private EntityManagerFactory emf;
    private EntityManager em = null;

    public ConsultasJPA(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<Entrega> traerEntregasPorFechaSalida(LocalDate fechaSalida) {
        em = getEntityManager();
        try {
            TypedQuery<Entrega> query = em.createNamedQuery("Entrega.findByFechaSalida", Entrega.class);
            query.setParameter("fechaSalida", fechaSalida);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    public boolean existeNumeroRemito(String numero) {
        em = getEntityManager();
        try {
            TypedQuery<Entrega> query = em.createNamedQuery("Entrega.findByRemito", Entrega.class);
            query.setParameter("remito", numero);
            boolean respuesta = false;
            List<Entrega> resultados = query.getResultList();
            for (Entrega resultado : resultados) {
                if (resultado.getEstado() == true){
                    respuesta = true;
                    break;
                }
            }
            return respuesta;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public LocalDate consultarFechaServidor() {
        em = getEntityManager();
        try {
            Query query = em.createNativeQuery("SELECT NOW() AS current_datetime");
            Timestamp serverTimestamp = (Timestamp) query.getSingleResult();
            LocalDate localDate = serverTimestamp.toLocalDateTime().toLocalDate();
            return localDate;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public String ultimoNumeroRemitoEntrega() {
        em = getEntityManager();
        try {
            Query query = em.createNativeQuery("SELECT remito FROM entregas ORDER BY remito DESC LIMIT 1");
            Object resultado = query.getSingleResult();
            if (resultado != null) {
                // Si hay un resultado, conviértelo a String y devuelve el valor.
                return resultado.toString();
            } else {
                // Si no hay resultados, la tabla podría estar vacía, y puedes manejarlo como desees.
                return "0001-00000001"; // O lanza una excepción, devuelve un valor predeterminado, etc.
            }
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public List<Combustible> traerCargasCombustiblesConFecha(LocalDate fecha) {
        em = getEntityManager();
        try {
            TypedQuery<Combustible> query = em.createNamedQuery("Combustible.findByFecha", Combustible.class);
            query.setParameter("fecha", fecha);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public Combustible existeFacturaCargaCombustible(String factura) {
        em = getEntityManager();
        try {
            TypedQuery<Combustible> query = em.createNamedQuery("Combustible.findNumeroFactura", Combustible.class);
            query.setParameter("factura", factura);
            Combustible combu = new Combustible();
            combu.setCodigo(-1);
            List<Combustible> lista = query.getResultList();
            for (Combustible combustible : lista) {
                if (combustible.getEstado()){
                    combu = combustible;
                }
            }
            return combu;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    public List<CargaMorro> traerCargasMorroConFecha(LocalDate fecha) {
        em = getEntityManager();
        try {
            TypedQuery<CargaMorro> query = em.createNamedQuery("CargaMorro.findByFecha", CargaMorro.class);
            query.setParameter("fecha", fecha);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    public List<CargaMorro> traerCargasMorroConFechaMorro(LocalDate fecha, int codigo) {
        em = getEntityManager();
        try {
            TypedQuery<CargaMorro> query = em.createNamedQuery("CargaMorro.findByFechaMorro", CargaMorro.class);
            query.setParameter("fecha", fecha);
            query.setParameter("codigo", codigo);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    public List<Entrega> traerEntregaPorClienteAConfirmar(Cliente cliente) {
        em = getEntityManager();
        try {
            TypedQuery<Entrega> query = em.createNamedQuery("Entrega.findByClienteAConfirmar", Entrega.class);
            query.setParameter("cliente", cliente);
            return query.getResultList();
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
    /*******************  CAMION ODOMETRO  ***********************/
     
    public List<CamionOdometro> traerCamionOdometroPorCamion(int codigo){
        em = getEntityManager();
        try{
            TypedQuery<CamionOdometro> query = em.createNamedQuery("CamionOdometro.findByCamion",CamionOdometro.class);
            query.setParameter("codigo",codigo);
            return query.getResultList();
        }finally{
            if (em != null && em.isOpen()){
                em.close();
            }
        }
    }
    public List<CamionOdometro> traerCamionOdometroPorFecha(LocalDate fecha){
        em = getEntityManager();
        try{
            TypedQuery<CamionOdometro> query = em.createNamedQuery("CamionOdometro.findByFecha",CamionOdometro.class);
            query.setParameter("fecha",fecha);
            return query.getResultList();
        }finally{
            if (em != null && em.isOpen()){
                em.close();
            }
        }
    }
    public List<CamionOdometro> traerCamionOdometroPorFechaCamino(LocalDate fecha,int codigo){
        em = getEntityManager();
        try{
            TypedQuery<CamionOdometro> query = em.createNamedQuery("CamionOdometro.findByFechaCamion",CamionOdometro.class);
            query.setParameter("fecha",fecha);
            query.setParameter("codigo",codigo);
            return query.getResultList();
        }finally{
            if (em != null && em.isOpen()){
                em.close();
            }
        }
    }

}
