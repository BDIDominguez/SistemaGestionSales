/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencias;

import entidades.CargaMorro;
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
public class CargaMorroJpaController implements Serializable {

    public CargaMorroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CargaMorro cargaMorro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cargaMorro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CargaMorro cargaMorro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cargaMorro = em.merge(cargaMorro);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = cargaMorro.getCodigo();
                if (findCargaMorro(id) == null) {
                    throw new NonexistentEntityException("The cargaMorro with id " + id + " no longer exists.");
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
            CargaMorro cargaMorro;
            try {
                cargaMorro = em.getReference(CargaMorro.class, id);
                cargaMorro.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cargaMorro with id " + id + " no longer exists.", enfe);
            }
            em.remove(cargaMorro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CargaMorro> findCargaMorroEntities() {
        return findCargaMorroEntities(true, -1, -1);
    }

    public List<CargaMorro> findCargaMorroEntities(int maxResults, int firstResult) {
        return findCargaMorroEntities(false, maxResults, firstResult);
    }

    private List<CargaMorro> findCargaMorroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CargaMorro.class));
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

    public CargaMorro findCargaMorro(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CargaMorro.class, id);
        } finally {
            em.close();
        }
    }

    public int getCargaMorroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CargaMorro> rt = cq.from(CargaMorro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
