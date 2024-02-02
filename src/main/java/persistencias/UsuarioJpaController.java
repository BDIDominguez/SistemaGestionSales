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
import entidades.Permiso;
import entidades.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import persistencias.exceptions.NonexistentEntityException;

/**
 *
 * @author Dario
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        if (usuario.getPermiso() == null) {
            usuario.setPermiso(new ArrayList<Permiso>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Permiso> attachedPermiso = new ArrayList<Permiso>();
            for (Permiso permisoPermisoToAttach : usuario.getPermiso()) {
                permisoPermisoToAttach = em.getReference(permisoPermisoToAttach.getClass(), permisoPermisoToAttach.getCodigo());
                attachedPermiso.add(permisoPermisoToAttach);
            }
            usuario.setPermiso(attachedPermiso);
            em.persist(usuario);
            for (Permiso permisoPermiso : usuario.getPermiso()) {
                Usuario oldUsuarioOfPermisoPermiso = permisoPermiso.getUsuario();
                permisoPermiso.setUsuario(usuario);
                permisoPermiso = em.merge(permisoPermiso);
                if (oldUsuarioOfPermisoPermiso != null) {
                    oldUsuarioOfPermisoPermiso.getPermiso().remove(permisoPermiso);
                    oldUsuarioOfPermisoPermiso = em.merge(oldUsuarioOfPermisoPermiso);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getCodigo());
            List<Permiso> permisoOld = persistentUsuario.getPermiso();
            List<Permiso> permisoNew = usuario.getPermiso();
            List<Permiso> attachedPermisoNew = new ArrayList<Permiso>();
            for (Permiso permisoNewPermisoToAttach : permisoNew) {
                permisoNewPermisoToAttach = em.getReference(permisoNewPermisoToAttach.getClass(), permisoNewPermisoToAttach.getCodigo());
                attachedPermisoNew.add(permisoNewPermisoToAttach);
            }
            permisoNew = attachedPermisoNew;
            usuario.setPermiso(permisoNew);
            usuario = em.merge(usuario);
            for (Permiso permisoOldPermiso : permisoOld) {
                if (!permisoNew.contains(permisoOldPermiso)) {
                    permisoOldPermiso.setUsuario(null);
                    permisoOldPermiso = em.merge(permisoOldPermiso);
                }
            }
            for (Permiso permisoNewPermiso : permisoNew) {
                if (!permisoOld.contains(permisoNewPermiso)) {
                    Usuario oldUsuarioOfPermisoNewPermiso = permisoNewPermiso.getUsuario();
                    permisoNewPermiso.setUsuario(usuario);
                    permisoNewPermiso = em.merge(permisoNewPermiso);
                    if (oldUsuarioOfPermisoNewPermiso != null && !oldUsuarioOfPermisoNewPermiso.equals(usuario)) {
                        oldUsuarioOfPermisoNewPermiso.getPermiso().remove(permisoNewPermiso);
                        oldUsuarioOfPermisoNewPermiso = em.merge(oldUsuarioOfPermisoNewPermiso);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = usuario.getCodigo();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<Permiso> permiso = usuario.getPermiso();
            for (Permiso permisoPermiso : permiso) {
                permisoPermiso.setUsuario(null);
                permisoPermiso = em.merge(permisoPermiso);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
