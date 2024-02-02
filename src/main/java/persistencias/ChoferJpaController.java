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
import entidades.Chofer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import persistencias.exceptions.NonexistentEntityException;

/**
 *
 * @author Dario
 */
public class ChoferJpaController implements Serializable {

    public ChoferJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Chofer chofer) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Camion camion = chofer.getCamion();
            if (camion != null) {
                camion = em.getReference(camion.getClass(), camion.getCodigo());
                chofer.setCamion(camion);
            }
            em.persist(chofer);
            if (camion != null) {
                Chofer oldChoferOfCamion = camion.getChofer();
                if (oldChoferOfCamion != null) {
                    oldChoferOfCamion.setCamion(null);
                    oldChoferOfCamion = em.merge(oldChoferOfCamion);
                }
                camion.setChofer(chofer);
                camion = em.merge(camion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Chofer chofer) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Chofer persistentChofer = em.find(Chofer.class, chofer.getCodigo());
            Camion camionOld = persistentChofer.getCamion();
            Camion camionNew = chofer.getCamion();
            if (camionNew != null) {
                camionNew = em.getReference(camionNew.getClass(), camionNew.getCodigo());
                chofer.setCamion(camionNew);
            }
            chofer = em.merge(chofer);
            if (camionOld != null && !camionOld.equals(camionNew)) {
                camionOld.setChofer(null);
                camionOld = em.merge(camionOld);
            }
            if (camionNew != null && !camionNew.equals(camionOld)) {
                Chofer oldChoferOfCamion = camionNew.getChofer();
                if (oldChoferOfCamion != null) {
                    oldChoferOfCamion.setCamion(null);
                    oldChoferOfCamion = em.merge(oldChoferOfCamion);
                }
                camionNew.setChofer(chofer);
                camionNew = em.merge(camionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = chofer.getCodigo();
                if (findChofer(id) == null) {
                    throw new NonexistentEntityException("The chofer with id " + id + " no longer exists.");
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
            Chofer chofer;
            try {
                chofer = em.getReference(Chofer.class, id);
                chofer.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The chofer with id " + id + " no longer exists.", enfe);
            }
            Camion camion = chofer.getCamion();
            if (camion != null) {
                camion.setChofer(null);
                camion = em.merge(camion);
            }
            em.remove(chofer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Chofer> findChoferEntities() {
        return findChoferEntities(true, -1, -1);
    }

    public List<Chofer> findChoferEntities(int maxResults, int firstResult) {
        return findChoferEntities(false, maxResults, firstResult);
    }

    private List<Chofer> findChoferEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Chofer.class));
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

    public Chofer findChofer(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Chofer.class, id);
        } finally {
            em.close();
        }
    }

    public int getChoferCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Chofer> rt = cq.from(Chofer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
