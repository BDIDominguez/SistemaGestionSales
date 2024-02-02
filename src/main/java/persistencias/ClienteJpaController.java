/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencias;

import entidades.Cliente;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import entidades.Entrega;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import persistencias.exceptions.NonexistentEntityException;

/**
 *
 * @author Dario
 */
public class ClienteJpaController implements Serializable {

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) {
        if (cliente.getEntrega() == null) {
            cliente.setEntrega(new ArrayList<Entrega>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Entrega> attachedEntrega = new ArrayList<Entrega>();
            for (Entrega entregaEntregaToAttach : cliente.getEntrega()) {
                entregaEntregaToAttach = em.getReference(entregaEntregaToAttach.getClass(), entregaEntregaToAttach.getCodigo());
                attachedEntrega.add(entregaEntregaToAttach);
            }
            cliente.setEntrega(attachedEntrega);
            em.persist(cliente);
            for (Entrega entregaEntrega : cliente.getEntrega()) {
                Cliente oldClienteOfEntregaEntrega = entregaEntrega.getCliente();
                entregaEntrega.setCliente(cliente);
                entregaEntrega = em.merge(entregaEntrega);
                if (oldClienteOfEntregaEntrega != null) {
                    oldClienteOfEntregaEntrega.getEntrega().remove(entregaEntrega);
                    oldClienteOfEntregaEntrega = em.merge(oldClienteOfEntregaEntrega);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getCodigo());
            List<Entrega> entregaOld = persistentCliente.getEntrega();
            List<Entrega> entregaNew = cliente.getEntrega();
            List<Entrega> attachedEntregaNew = new ArrayList<Entrega>();
            for (Entrega entregaNewEntregaToAttach : entregaNew) {
                entregaNewEntregaToAttach = em.getReference(entregaNewEntregaToAttach.getClass(), entregaNewEntregaToAttach.getCodigo());
                attachedEntregaNew.add(entregaNewEntregaToAttach);
            }
            entregaNew = attachedEntregaNew;
            cliente.setEntrega(entregaNew);
            cliente = em.merge(cliente);
            for (Entrega entregaOldEntrega : entregaOld) {
                if (!entregaNew.contains(entregaOldEntrega)) {
                    entregaOldEntrega.setCliente(null);
                    entregaOldEntrega = em.merge(entregaOldEntrega);
                }
            }
            for (Entrega entregaNewEntrega : entregaNew) {
                if (!entregaOld.contains(entregaNewEntrega)) {
                    Cliente oldClienteOfEntregaNewEntrega = entregaNewEntrega.getCliente();
                    entregaNewEntrega.setCliente(cliente);
                    entregaNewEntrega = em.merge(entregaNewEntrega);
                    if (oldClienteOfEntregaNewEntrega != null && !oldClienteOfEntregaNewEntrega.equals(cliente)) {
                        oldClienteOfEntregaNewEntrega.getEntrega().remove(entregaNewEntrega);
                        oldClienteOfEntregaNewEntrega = em.merge(oldClienteOfEntregaNewEntrega);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = cliente.getCodigo();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<Entrega> entrega = cliente.getEntrega();
            for (Entrega entregaEntrega : entrega) {
                entregaEntrega.setCliente(null);
                entregaEntrega = em.merge(entregaEntrega);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
