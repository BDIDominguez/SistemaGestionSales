/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencias;

import entidades.CamionOdometro;
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
public class CamionOdometroJpaController implements Serializable {

    public CamionOdometroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CamionOdometro camionOdometro) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(camionOdometro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CamionOdometro camionOdometro) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            camionOdometro = em.merge(camionOdometro);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = camionOdometro.getCodigo();
                if (findCamionOdometro(id) == null) {
                    throw new NonexistentEntityException("The camionOdometro with id " + id + " no longer exists.");
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
            CamionOdometro camionOdometro;
            try {
                camionOdometro = em.getReference(CamionOdometro.class, id);
                camionOdometro.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The camionOdometro with id " + id + " no longer exists.", enfe);
            }
            em.remove(camionOdometro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CamionOdometro> findCamionOdometroEntities() {
        return findCamionOdometroEntities(true, -1, -1);
    }

    public List<CamionOdometro> findCamionOdometroEntities(int maxResults, int firstResult) {
        return findCamionOdometroEntities(false, maxResults, firstResult);
    }

    private List<CamionOdometro> findCamionOdometroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CamionOdometro.class));
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

    public CamionOdometro findCamionOdometro(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CamionOdometro.class, id);
        } finally {
            em.close();
        }
    }

    public int getCamionOdometroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CamionOdometro> rt = cq.from(CamionOdometro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
