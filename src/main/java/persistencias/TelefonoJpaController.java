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
import entidades.Telefono;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import persistencias.exceptions.NonexistentEntityException;

/**
 *
 * @author Dario
 */
public class TelefonoJpaController implements Serializable {

    public TelefonoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Telefono telefono) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor proveedor = telefono.getProveedor();
            if (proveedor != null) {
                proveedor = em.getReference(proveedor.getClass(), proveedor.getCodigo());
                telefono.setProveedor(proveedor);
            }
            Contacto contacto = telefono.getContacto();
            if (contacto != null) {
                contacto = em.getReference(contacto.getClass(), contacto.getCodigo());
                telefono.setContacto(contacto);
            }
            em.persist(telefono);
            if (proveedor != null) {
                proveedor.getTelefono().add(telefono);
                proveedor = em.merge(proveedor);
            }
            if (contacto != null) {
                contacto.getTelefono().add(telefono);
                contacto = em.merge(contacto);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Telefono telefono) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Telefono persistentTelefono = em.find(Telefono.class, telefono.getCodigo());
            Proveedor proveedorOld = persistentTelefono.getProveedor();
            Proveedor proveedorNew = telefono.getProveedor();
            Contacto contactoOld = persistentTelefono.getContacto();
            Contacto contactoNew = telefono.getContacto();
            if (proveedorNew != null) {
                proveedorNew = em.getReference(proveedorNew.getClass(), proveedorNew.getCodigo());
                telefono.setProveedor(proveedorNew);
            }
            if (contactoNew != null) {
                contactoNew = em.getReference(contactoNew.getClass(), contactoNew.getCodigo());
                telefono.setContacto(contactoNew);
            }
            telefono = em.merge(telefono);
            if (proveedorOld != null && !proveedorOld.equals(proveedorNew)) {
                proveedorOld.getTelefono().remove(telefono);
                proveedorOld = em.merge(proveedorOld);
            }
            if (proveedorNew != null && !proveedorNew.equals(proveedorOld)) {
                proveedorNew.getTelefono().add(telefono);
                proveedorNew = em.merge(proveedorNew);
            }
            if (contactoOld != null && !contactoOld.equals(contactoNew)) {
                contactoOld.getTelefono().remove(telefono);
                contactoOld = em.merge(contactoOld);
            }
            if (contactoNew != null && !contactoNew.equals(contactoOld)) {
                contactoNew.getTelefono().add(telefono);
                contactoNew = em.merge(contactoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = telefono.getCodigo();
                if (findTelefono(id) == null) {
                    throw new NonexistentEntityException("The telefono with id " + id + " no longer exists.");
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
            Telefono telefono;
            try {
                telefono = em.getReference(Telefono.class, id);
                telefono.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The telefono with id " + id + " no longer exists.", enfe);
            }
            Proveedor proveedor = telefono.getProveedor();
            if (proveedor != null) {
                proveedor.getTelefono().remove(telefono);
                proveedor = em.merge(proveedor);
            }
            Contacto contacto = telefono.getContacto();
            if (contacto != null) {
                contacto.getTelefono().remove(telefono);
                contacto = em.merge(contacto);
            }
            em.remove(telefono);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Telefono> findTelefonoEntities() {
        return findTelefonoEntities(true, -1, -1);
    }

    public List<Telefono> findTelefonoEntities(int maxResults, int firstResult) {
        return findTelefonoEntities(false, maxResults, firstResult);
    }

    private List<Telefono> findTelefonoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Telefono.class));
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

    public Telefono findTelefono(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Telefono.class, id);
        } finally {
            em.close();
        }
    }

    public int getTelefonoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Telefono> rt = cq.from(Telefono.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
