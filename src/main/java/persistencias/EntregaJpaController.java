/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencias;

import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import entidades.Cliente;
import entidades.Camion;
import entidades.Entrega;
import entidades.Morro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import persistencias.exceptions.NonexistentEntityException;

/**
 *
 * @author Dario
 */
public class EntregaJpaController implements Serializable {

    public EntregaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Entrega entrega) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente = entrega.getCliente();
            if (cliente != null) {
                cliente = em.getReference(cliente.getClass(), cliente.getCodigo());
                entrega.setCliente(cliente);
            }
            Camion camion = entrega.getCamion();
            if (camion != null) {
                camion = em.getReference(camion.getClass(), camion.getCodigo());
                entrega.setCamion(camion);
            }
            Morro morro = entrega.getMorro();
            if (morro != null) {
                morro = em.getReference(morro.getClass(), morro.getCodigo());
                entrega.setMorro(morro);
            }
            em.persist(entrega);
            if (cliente != null) {
                cliente.getEntrega().add(entrega);
                cliente = em.merge(cliente);
            }
            if (camion != null) {
                camion.getEntregas().add(entrega);
                camion = em.merge(camion);
            }
            if (morro != null) {
                morro.getEntregas().add(entrega);
                morro = em.merge(morro);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Entrega entrega) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entrega persistentEntrega = em.find(Entrega.class, entrega.getCodigo());
            Cliente clienteOld = persistentEntrega.getCliente();
            Cliente clienteNew = entrega.getCliente();
            Camion camionOld = persistentEntrega.getCamion();
            Camion camionNew = entrega.getCamion();
            Morro morroOld = persistentEntrega.getMorro();
            Morro morroNew = entrega.getMorro();
            if (clienteNew != null) {
                clienteNew = em.getReference(clienteNew.getClass(), clienteNew.getCodigo());
                entrega.setCliente(clienteNew);
            }
            if (camionNew != null) {
                camionNew = em.getReference(camionNew.getClass(), camionNew.getCodigo());
                entrega.setCamion(camionNew);
            }
            if (morroNew != null) {
                morroNew = em.getReference(morroNew.getClass(), morroNew.getCodigo());
                entrega.setMorro(morroNew);
            }
            entrega = em.merge(entrega);
            if (clienteOld != null && !clienteOld.equals(clienteNew)) {
                clienteOld.getEntrega().remove(entrega);
                clienteOld = em.merge(clienteOld);
            }
            if (clienteNew != null && !clienteNew.equals(clienteOld)) {
                clienteNew.getEntrega().add(entrega);
                clienteNew = em.merge(clienteNew);
            }
            if (camionOld != null && !camionOld.equals(camionNew)) {
                camionOld.getEntregas().remove(entrega);
                camionOld = em.merge(camionOld);
            }
            if (camionNew != null && !camionNew.equals(camionOld)) {
                camionNew.getEntregas().add(entrega);
                camionNew = em.merge(camionNew);
            }
            if (morroOld != null && !morroOld.equals(morroNew)) {
                morroOld.getEntregas().remove(entrega);
                morroOld = em.merge(morroOld);
            }
            if (morroNew != null && !morroNew.equals(morroOld)) {
                morroNew.getEntregas().add(entrega);
                morroNew = em.merge(morroNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = entrega.getCodigo();
                if (findEntrega(id) == null) {
                    throw new NonexistentEntityException("The entrega with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Entrega entrega;
            try {
                entrega = em.getReference(Entrega.class, id);
                entrega.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entrega with id " + id + " no longer exists.", enfe);
            }
            Cliente cliente = entrega.getCliente();
            if (cliente != null) {
                cliente.getEntrega().remove(entrega);
                cliente = em.merge(cliente);
            }
            Camion camion = entrega.getCamion();
            if (camion != null) {
                camion.getEntregas().remove(entrega);
                camion = em.merge(camion);
            }
            Morro morro = entrega.getMorro();
            if (morro != null) {
                morro.getEntregas().remove(entrega);
                morro = em.merge(morro);
            }
            em.remove(entrega);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Entrega> findEntregaEntities() {
        return findEntregaEntities(true, -1, -1);
    }

    public List<Entrega> findEntregaEntities(int maxResults, int firstResult) {
        return findEntregaEntities(false, maxResults, firstResult);
    }

    private List<Entrega> findEntregaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Entrega.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Entrega findEntrega(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Entrega.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntregaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Entrega> rt = cq.from(Entrega.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
