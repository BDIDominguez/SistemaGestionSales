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
import entidades.Proveedor;
import entidades.Contacto;
import entidades.Correo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import persistencias.exceptions.NonexistentEntityException;

/**
 *
 * @author Dario
 */
public class CorreoJpaController implements Serializable {

    public CorreoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Correo correo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor proveedor = correo.getProveedor();
            if (proveedor != null) {
                proveedor = em.getReference(proveedor.getClass(), proveedor.getCodigo());
                correo.setProveedor(proveedor);
            }
            Contacto contacto = correo.getContacto();
            if (contacto != null) {
                contacto = em.getReference(contacto.getClass(), contacto.getCodigo());
                correo.setContacto(contacto);
            }
            em.persist(correo);
            if (proveedor != null) {
                proveedor.getCorreo().add(correo);
                proveedor = em.merge(proveedor);
            }
            if (contacto != null) {
                contacto.getCorreo().add(correo);
                contacto = em.merge(contacto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Correo correo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Correo persistentCorreo = em.find(Correo.class, correo.getCodigo());
            Proveedor proveedorOld = persistentCorreo.getProveedor();
            Proveedor proveedorNew = correo.getProveedor();
            Contacto contactoOld = persistentCorreo.getContacto();
            Contacto contactoNew = correo.getContacto();
            if (proveedorNew != null) {
                proveedorNew = em.getReference(proveedorNew.getClass(), proveedorNew.getCodigo());
                correo.setProveedor(proveedorNew);
            }
            if (contactoNew != null) {
                contactoNew = em.getReference(contactoNew.getClass(), contactoNew.getCodigo());
                correo.setContacto(contactoNew);
            }
            correo = em.merge(correo);
            if (proveedorOld != null && !proveedorOld.equals(proveedorNew)) {
                proveedorOld.getCorreo().remove(correo);
                proveedorOld = em.merge(proveedorOld);
            }
            if (proveedorNew != null && !proveedorNew.equals(proveedorOld)) {
                proveedorNew.getCorreo().add(correo);
                proveedorNew = em.merge(proveedorNew);
            }
            if (contactoOld != null && !contactoOld.equals(contactoNew)) {
                contactoOld.getCorreo().remove(correo);
                contactoOld = em.merge(contactoOld);
            }
            if (contactoNew != null && !contactoNew.equals(contactoOld)) {
                contactoNew.getCorreo().add(correo);
                contactoNew = em.merge(contactoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = correo.getCodigo();
                if (findCorreo(id) == null) {
                    throw new NonexistentEntityException("The correo with id " + id + " no longer exists.");
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
            Correo correo;
            try {
                correo = em.getReference(Correo.class, id);
                correo.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The correo with id " + id + " no longer exists.", enfe);
            }
            Proveedor proveedor = correo.getProveedor();
            if (proveedor != null) {
                proveedor.getCorreo().remove(correo);
                proveedor = em.merge(proveedor);
            }
            Contacto contacto = correo.getContacto();
            if (contacto != null) {
                contacto.getCorreo().remove(correo);
                contacto = em.merge(contacto);
            }
            em.remove(correo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Correo> findCorreoEntities() {
        return findCorreoEntities(true, -1, -1);
    }

    public List<Correo> findCorreoEntities(int maxResults, int firstResult) {
        return findCorreoEntities(false, maxResults, firstResult);
    }

    private List<Correo> findCorreoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Correo.class));
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

    public Correo findCorreo(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Correo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCorreoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Correo> rt = cq.from(Correo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
