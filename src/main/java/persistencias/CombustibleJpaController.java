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
import entidades.Camion;
import entidades.Combustible;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import persistencias.exceptions.NonexistentEntityException;

/**
 *
 * @author Dario
 */
public class CombustibleJpaController implements Serializable {

    public CombustibleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Combustible combustible) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Camion camion = combustible.getCamion();
            if (camion != null) {
                camion = em.getReference(camion.getClass(), camion.getCodigo());
                combustible.setCamion(camion);
            }
            em.persist(combustible);
            if (camion != null) {
                camion.getCargas().add(combustible);
                camion = em.merge(camion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Combustible combustible) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Combustible persistentCombustible = em.find(Combustible.class, combustible.getCodigo());
            Camion camionOld = persistentCombustible.getCamion();
            Camion camionNew = combustible.getCamion();
            if (camionNew != null) {
                camionNew = em.getReference(camionNew.getClass(), camionNew.getCodigo());
                combustible.setCamion(camionNew);
            }
            combustible = em.merge(combustible);
            if (camionOld != null && !camionOld.equals(camionNew)) {
                camionOld.getCargas().remove(combustible);
                camionOld = em.merge(camionOld);
            }
            if (camionNew != null && !camionNew.equals(camionOld)) {
                camionNew.getCargas().add(combustible);
                camionNew = em.merge(camionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = combustible.getCodigo();
                if (findCombustible(id) == null) {
                    throw new NonexistentEntityException("The combustible with id " + id + " no longer exists.");
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
            Combustible combustible;
            try {
                combustible = em.getReference(Combustible.class, id);
                combustible.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The combustible with id " + id + " no longer exists.", enfe);
            }
            Camion camion = combustible.getCamion();
            if (camion != null) {
                camion.getCargas().remove(combustible);
                camion = em.merge(camion);
            }
            em.remove(combustible);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Combustible> findCombustibleEntities() {
        return findCombustibleEntities(true, -1, -1);
    }

    public List<Combustible> findCombustibleEntities(int maxResults, int firstResult) {
        return findCombustibleEntities(false, maxResults, firstResult);
    }

    private List<Combustible> findCombustibleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Combustible.class));
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

    public Combustible findCombustible(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Combustible.class, id);
        } finally {
            em.close();
        }
    }

    public int getCombustibleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Combustible> rt = cq.from(Combustible.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
