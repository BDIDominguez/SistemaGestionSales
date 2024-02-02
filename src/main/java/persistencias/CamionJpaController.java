/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencias;

import entidades.Camion;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import entidades.Chofer;
import entidades.Combustible;
import java.util.ArrayList;
import java.util.List;
import entidades.Entrega;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import persistencias.exceptions.NonexistentEntityException;

/**
 *
 * @author Dario
 */
public class CamionJpaController implements Serializable {

    public CamionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Camion camion) {
        if (camion.getCargas() == null) {
            camion.setCargas(new ArrayList<Combustible>());
        }
        if (camion.getEntregas() == null) {
            camion.setEntregas(new ArrayList<Entrega>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Chofer chofer = camion.getChofer();
            if (chofer != null) {
                chofer = em.getReference(chofer.getClass(), chofer.getCodigo());
                camion.setChofer(chofer);
            }
            List<Combustible> attachedCargas = new ArrayList<Combustible>();
            for (Combustible cargasCombustibleToAttach : camion.getCargas()) {
                cargasCombustibleToAttach = em.getReference(cargasCombustibleToAttach.getClass(), cargasCombustibleToAttach.getCodigo());
                attachedCargas.add(cargasCombustibleToAttach);
            }
            camion.setCargas(attachedCargas);
            List<Entrega> attachedEntregas = new ArrayList<Entrega>();
            for (Entrega entregasEntregaToAttach : camion.getEntregas()) {
                entregasEntregaToAttach = em.getReference(entregasEntregaToAttach.getClass(), entregasEntregaToAttach.getCodigo());
                attachedEntregas.add(entregasEntregaToAttach);
            }
            camion.setEntregas(attachedEntregas);
            em.persist(camion);
            if (chofer != null) {
                Camion oldCamionOfChofer = chofer.getCamion();
                if (oldCamionOfChofer != null) {
                    oldCamionOfChofer.setChofer(null);
                    oldCamionOfChofer = em.merge(oldCamionOfChofer);
                }
                chofer.setCamion(camion);
                chofer = em.merge(chofer);
            }
            for (Combustible cargasCombustible : camion.getCargas()) {
                Camion oldCamionOfCargasCombustible = cargasCombustible.getCamion();
                cargasCombustible.setCamion(camion);
                cargasCombustible = em.merge(cargasCombustible);
                if (oldCamionOfCargasCombustible != null) {
                    oldCamionOfCargasCombustible.getCargas().remove(cargasCombustible);
                    oldCamionOfCargasCombustible = em.merge(oldCamionOfCargasCombustible);
                }
            }
            for (Entrega entregasEntrega : camion.getEntregas()) {
                Camion oldCamionOfEntregasEntrega = entregasEntrega.getCamion();
                entregasEntrega.setCamion(camion);
                entregasEntrega = em.merge(entregasEntrega);
                if (oldCamionOfEntregasEntrega != null) {
                    oldCamionOfEntregasEntrega.getEntregas().remove(entregasEntrega);
                    oldCamionOfEntregasEntrega = em.merge(oldCamionOfEntregasEntrega);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Camion camion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Camion persistentCamion = em.find(Camion.class, camion.getCodigo());
            Chofer choferOld = persistentCamion.getChofer();
            Chofer choferNew = camion.getChofer();
            List<Combustible> cargasOld = persistentCamion.getCargas();
            List<Combustible> cargasNew = camion.getCargas();
            List<Entrega> entregasOld = persistentCamion.getEntregas();
            List<Entrega> entregasNew = camion.getEntregas();
            if (choferNew != null) {
                choferNew = em.getReference(choferNew.getClass(), choferNew.getCodigo());
                camion.setChofer(choferNew);
            }
            List<Combustible> attachedCargasNew = new ArrayList<Combustible>();
            for (Combustible cargasNewCombustibleToAttach : cargasNew) {
                cargasNewCombustibleToAttach = em.getReference(cargasNewCombustibleToAttach.getClass(), cargasNewCombustibleToAttach.getCodigo());
                attachedCargasNew.add(cargasNewCombustibleToAttach);
            }
            cargasNew = attachedCargasNew;
            camion.setCargas(cargasNew);
            List<Entrega> attachedEntregasNew = new ArrayList<Entrega>();
            for (Entrega entregasNewEntregaToAttach : entregasNew) {
                entregasNewEntregaToAttach = em.getReference(entregasNewEntregaToAttach.getClass(), entregasNewEntregaToAttach.getCodigo());
                attachedEntregasNew.add(entregasNewEntregaToAttach);
            }
            entregasNew = attachedEntregasNew;
            camion.setEntregas(entregasNew);
            camion = em.merge(camion);
            if (choferOld != null && !choferOld.equals(choferNew)) {
                choferOld.setCamion(null);
                choferOld = em.merge(choferOld);
            }
            if (choferNew != null && !choferNew.equals(choferOld)) {
                Camion oldCamionOfChofer = choferNew.getCamion();
                if (oldCamionOfChofer != null) {
                    oldCamionOfChofer.setChofer(null);
                    oldCamionOfChofer = em.merge(oldCamionOfChofer);
                }
                choferNew.setCamion(camion);
                choferNew = em.merge(choferNew);
            }
            for (Combustible cargasOldCombustible : cargasOld) {
                if (!cargasNew.contains(cargasOldCombustible)) {
                    cargasOldCombustible.setCamion(null);
                    cargasOldCombustible = em.merge(cargasOldCombustible);
                }
            }
            for (Combustible cargasNewCombustible : cargasNew) {
                if (!cargasOld.contains(cargasNewCombustible)) {
                    Camion oldCamionOfCargasNewCombustible = cargasNewCombustible.getCamion();
                    cargasNewCombustible.setCamion(camion);
                    cargasNewCombustible = em.merge(cargasNewCombustible);
                    if (oldCamionOfCargasNewCombustible != null && !oldCamionOfCargasNewCombustible.equals(camion)) {
                        oldCamionOfCargasNewCombustible.getCargas().remove(cargasNewCombustible);
                        oldCamionOfCargasNewCombustible = em.merge(oldCamionOfCargasNewCombustible);
                    }
                }
            }
            for (Entrega entregasOldEntrega : entregasOld) {
                if (!entregasNew.contains(entregasOldEntrega)) {
                    entregasOldEntrega.setCamion(null);
                    entregasOldEntrega = em.merge(entregasOldEntrega);
                }
            }
            for (Entrega entregasNewEntrega : entregasNew) {
                if (!entregasOld.contains(entregasNewEntrega)) {
                    Camion oldCamionOfEntregasNewEntrega = entregasNewEntrega.getCamion();
                    entregasNewEntrega.setCamion(camion);
                    entregasNewEntrega = em.merge(entregasNewEntrega);
                    if (oldCamionOfEntregasNewEntrega != null && !oldCamionOfEntregasNewEntrega.equals(camion)) {
                        oldCamionOfEntregasNewEntrega.getEntregas().remove(entregasNewEntrega);
                        oldCamionOfEntregasNewEntrega = em.merge(oldCamionOfEntregasNewEntrega);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = camion.getCodigo();
                if (findCamion(id) == null) {
                    throw new NonexistentEntityException("The camion with id " + id + " no longer exists.");
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
            Camion camion;
            try {
                camion = em.getReference(Camion.class, id);
                camion.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The camion with id " + id + " no longer exists.", enfe);
            }
            Chofer chofer = camion.getChofer();
            if (chofer != null) {
                chofer.setCamion(null);
                chofer = em.merge(chofer);
            }
            List<Combustible> cargas = camion.getCargas();
            for (Combustible cargasCombustible : cargas) {
                cargasCombustible.setCamion(null);
                cargasCombustible = em.merge(cargasCombustible);
            }
            List<Entrega> entregas = camion.getEntregas();
            for (Entrega entregasEntrega : entregas) {
                entregasEntrega.setCamion(null);
                entregasEntrega = em.merge(entregasEntrega);
            }
            em.remove(camion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Camion> findCamionEntities() {
        return findCamionEntities(true, -1, -1);
    }

    public List<Camion> findCamionEntities(int maxResults, int firstResult) {
        return findCamionEntities(false, maxResults, firstResult);
    }

    private List<Camion> findCamionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Camion.class));
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

    public Camion findCamion(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Camion.class, id);
        } finally {
            em.close();
        }
    }

    public int getCamionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Camion> rt = cq.from(Camion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
