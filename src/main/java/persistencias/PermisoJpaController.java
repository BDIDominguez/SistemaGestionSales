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
import entidades.Usuario;
import entidades.Objeto;
import entidades.Permiso;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import persistencias.exceptions.NonexistentEntityException;

/**
 *
 * @author Dario
 */
public class PermisoJpaController implements Serializable {

    public PermisoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permiso permiso) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario = permiso.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getCodigo());
                permiso.setUsuario(usuario);
            }
            Objeto objeto = permiso.getObjeto();
            if (objeto != null) {
                objeto = em.getReference(objeto.getClass(), objeto.getCodigo());
                permiso.setObjeto(objeto);
            }
            em.persist(permiso);
            if (usuario != null) {
                usuario.getPermiso().add(permiso);
                usuario = em.merge(usuario);
            }
            if (objeto != null) {
                objeto.getPermisos().add(permiso);
                objeto = em.merge(objeto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permiso permiso) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permiso persistentPermiso = em.find(Permiso.class, permiso.getCodigo());
            Usuario usuarioOld = persistentPermiso.getUsuario();
            Usuario usuarioNew = permiso.getUsuario();
            Objeto objetoOld = persistentPermiso.getObjeto();
            Objeto objetoNew = permiso.getObjeto();
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getCodigo());
                permiso.setUsuario(usuarioNew);
            }
            if (objetoNew != null) {
                objetoNew = em.getReference(objetoNew.getClass(), objetoNew.getCodigo());
                permiso.setObjeto(objetoNew);
            }
            permiso = em.merge(permiso);
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getPermiso().remove(permiso);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getPermiso().add(permiso);
                usuarioNew = em.merge(usuarioNew);
            }
            if (objetoOld != null && !objetoOld.equals(objetoNew)) {
                objetoOld.getPermisos().remove(permiso);
                objetoOld = em.merge(objetoOld);
            }
            if (objetoNew != null && !objetoNew.equals(objetoOld)) {
                objetoNew.getPermisos().add(permiso);
                objetoNew = em.merge(objetoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = permiso.getCodigo();
                if (findPermiso(id) == null) {
                    throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.");
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
            Permiso permiso;
            try {
                permiso = em.getReference(Permiso.class, id);
                permiso.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permiso with id " + id + " no longer exists.", enfe);
            }
            Usuario usuario = permiso.getUsuario();
            if (usuario != null) {
                usuario.getPermiso().remove(permiso);
                usuario = em.merge(usuario);
            }
            Objeto objeto = permiso.getObjeto();
            if (objeto != null) {
                objeto.getPermisos().remove(permiso);
                objeto = em.merge(objeto);
            }
            em.remove(permiso);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Permiso> findPermisoEntities() {
        return findPermisoEntities(true, -1, -1);
    }

    public List<Permiso> findPermisoEntities(int maxResults, int firstResult) {
        return findPermisoEntities(false, maxResults, firstResult);
    }

    private List<Permiso> findPermisoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permiso.class));
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

    public Permiso findPermiso(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permiso.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermisoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permiso> rt = cq.from(Permiso.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
