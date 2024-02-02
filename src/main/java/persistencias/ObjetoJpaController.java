/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencias;

import entidades.Objeto;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import entidades.Permiso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import persistencias.exceptions.NonexistentEntityException;

/**
 *
 * @author Dario
 */
public class ObjetoJpaController implements Serializable {

    public ObjetoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Objeto objeto) {
        if (objeto.getPermisos() == null) {
            objeto.setPermisos(new ArrayList<Permiso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Permiso> attachedPermisos = new ArrayList<Permiso>();
            for (Permiso permisosPermisoToAttach : objeto.getPermisos()) {
                permisosPermisoToAttach = em.getReference(permisosPermisoToAttach.getClass(), permisosPermisoToAttach.getCodigo());
                attachedPermisos.add(permisosPermisoToAttach);
            }
            objeto.setPermisos(attachedPermisos);
            em.persist(objeto);
            for (Permiso permisosPermiso : objeto.getPermisos()) {
                Objeto oldObjetoOfPermisosPermiso = permisosPermiso.getObjeto();
                permisosPermiso.setObjeto(objeto);
                permisosPermiso = em.merge(permisosPermiso);
                if (oldObjetoOfPermisosPermiso != null) {
                    oldObjetoOfPermisosPermiso.getPermisos().remove(permisosPermiso);
                    oldObjetoOfPermisosPermiso = em.merge(oldObjetoOfPermisosPermiso);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Objeto objeto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Objeto persistentObjeto = em.find(Objeto.class, objeto.getCodigo());
            List<Permiso> permisosOld = persistentObjeto.getPermisos();
            List<Permiso> permisosNew = objeto.getPermisos();
            List<Permiso> attachedPermisosNew = new ArrayList<Permiso>();
            for (Permiso permisosNewPermisoToAttach : permisosNew) {
                permisosNewPermisoToAttach = em.getReference(permisosNewPermisoToAttach.getClass(), permisosNewPermisoToAttach.getCodigo());
                attachedPermisosNew.add(permisosNewPermisoToAttach);
            }
            permisosNew = attachedPermisosNew;
            objeto.setPermisos(permisosNew);
            objeto = em.merge(objeto);
            for (Permiso permisosOldPermiso : permisosOld) {
                if (!permisosNew.contains(permisosOldPermiso)) {
                    permisosOldPermiso.setObjeto(null);
                    permisosOldPermiso = em.merge(permisosOldPermiso);
                }
            }
            for (Permiso permisosNewPermiso : permisosNew) {
                if (!permisosOld.contains(permisosNewPermiso)) {
                    Objeto oldObjetoOfPermisosNewPermiso = permisosNewPermiso.getObjeto();
                    permisosNewPermiso.setObjeto(objeto);
                    permisosNewPermiso = em.merge(permisosNewPermiso);
                    if (oldObjetoOfPermisosNewPermiso != null && !oldObjetoOfPermisosNewPermiso.equals(objeto)) {
                        oldObjetoOfPermisosNewPermiso.getPermisos().remove(permisosNewPermiso);
                        oldObjetoOfPermisosNewPermiso = em.merge(oldObjetoOfPermisosNewPermiso);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = objeto.getCodigo();
                if (findObjeto(id) == null) {
                    throw new NonexistentEntityException("The objeto with id " + id + " no longer exists.");
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
            Objeto objeto;
            try {
                objeto = em.getReference(Objeto.class, id);
                objeto.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The objeto with id " + id + " no longer exists.", enfe);
            }
            List<Permiso> permisos = objeto.getPermisos();
            for (Permiso permisosPermiso : permisos) {
                permisosPermiso.setObjeto(null);
                permisosPermiso = em.merge(permisosPermiso);
            }
            em.remove(objeto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Objeto> findObjetoEntities() {
        return findObjetoEntities(true, -1, -1);
    }

    public List<Objeto> findObjetoEntities(int maxResults, int firstResult) {
        return findObjetoEntities(false, maxResults, firstResult);
    }

    private List<Objeto> findObjetoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Objeto.class));
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

    public Objeto findObjeto(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Objeto.class, id);
        } finally {
            em.close();
        }
    }

    public int getObjetoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Objeto> rt = cq.from(Objeto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
