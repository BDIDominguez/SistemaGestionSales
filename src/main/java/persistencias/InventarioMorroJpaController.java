/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencias;

import entidades.InventarioMorro;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import persistencias.exceptions.NonexistentEntityException;

/**
 *
 * @author Dario
 */
public class InventarioMorroJpaController implements Serializable {

    public InventarioMorroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(InventarioMorro inventarioMorro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(inventarioMorro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(InventarioMorro inventarioMorro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            inventarioMorro = em.merge(inventarioMorro);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = inventarioMorro.getCodigo();
                if (findInventarioMorro(id) == null) {
                    throw new NonexistentEntityException("The inventarioMorro with id " + id + " no longer exists.");
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
            InventarioMorro inventarioMorro;
            try {
                inventarioMorro = em.getReference(InventarioMorro.class, id);
                inventarioMorro.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The inventarioMorro with id " + id + " no longer exists.", enfe);
            }
            em.remove(inventarioMorro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<InventarioMorro> findInventarioMorroEntities() {
        return findInventarioMorroEntities(true, -1, -1);
    }

    public List<InventarioMorro> findInventarioMorroEntities(int maxResults, int firstResult) {
        return findInventarioMorroEntities(false, maxResults, firstResult);
    }

    private List<InventarioMorro> findInventarioMorroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InventarioMorro.class));
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

    public InventarioMorro findInventarioMorro(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(InventarioMorro.class, id);
        } finally {
            em.close();
        }
    }

    public int getInventarioMorroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InventarioMorro> rt = cq.from(InventarioMorro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
