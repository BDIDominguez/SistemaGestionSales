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
import entidades.Correo;
import entidades.Proveedor;
import java.util.ArrayList;
import java.util.List;
import entidades.Telefono;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import persistencias.exceptions.NonexistentEntityException;

/**
 *
 * @author Dario
 */
public class ProveedorJpaController implements Serializable {

    public ProveedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proveedor proveedor) {
        if (proveedor.getCorreo() == null) {
            proveedor.setCorreo(new ArrayList<Correo>());
        }
        if (proveedor.getTelefono() == null) {
            proveedor.setTelefono(new ArrayList<Telefono>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Correo> attachedCorreo = new ArrayList<Correo>();
            for (Correo correoCorreoToAttach : proveedor.getCorreo()) {
                correoCorreoToAttach = em.getReference(correoCorreoToAttach.getClass(), correoCorreoToAttach.getCodigo());
                attachedCorreo.add(correoCorreoToAttach);
            }
            proveedor.setCorreo(attachedCorreo);
            List<Telefono> attachedTelefono = new ArrayList<Telefono>();
            for (Telefono telefonoTelefonoToAttach : proveedor.getTelefono()) {
                telefonoTelefonoToAttach = em.getReference(telefonoTelefonoToAttach.getClass(), telefonoTelefonoToAttach.getCodigo());
                attachedTelefono.add(telefonoTelefonoToAttach);
            }
            proveedor.setTelefono(attachedTelefono);
            em.persist(proveedor);
            for (Correo correoCorreo : proveedor.getCorreo()) {
                Proveedor oldProveedorOfCorreoCorreo = correoCorreo.getProveedor();
                correoCorreo.setProveedor(proveedor);
                correoCorreo = em.merge(correoCorreo);
                if (oldProveedorOfCorreoCorreo != null) {
                    oldProveedorOfCorreoCorreo.getCorreo().remove(correoCorreo);
                    oldProveedorOfCorreoCorreo = em.merge(oldProveedorOfCorreoCorreo);
                }
            }
            for (Telefono telefonoTelefono : proveedor.getTelefono()) {
                Proveedor oldProveedorOfTelefonoTelefono = telefonoTelefono.getProveedor();
                telefonoTelefono.setProveedor(proveedor);
                telefonoTelefono = em.merge(telefonoTelefono);
                if (oldProveedorOfTelefonoTelefono != null) {
                    oldProveedorOfTelefonoTelefono.getTelefono().remove(telefonoTelefono);
                    oldProveedorOfTelefonoTelefono = em.merge(oldProveedorOfTelefonoTelefono);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proveedor proveedor) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor persistentProveedor = em.find(Proveedor.class, proveedor.getCodigo());
            List<Correo> correoOld = persistentProveedor.getCorreo();
            List<Correo> correoNew = proveedor.getCorreo();
            List<Telefono> telefonoOld = persistentProveedor.getTelefono();
            List<Telefono> telefonoNew = proveedor.getTelefono();
            List<Correo> attachedCorreoNew = new ArrayList<Correo>();
            for (Correo correoNewCorreoToAttach : correoNew) {
                correoNewCorreoToAttach = em.getReference(correoNewCorreoToAttach.getClass(), correoNewCorreoToAttach.getCodigo());
                attachedCorreoNew.add(correoNewCorreoToAttach);
            }
            correoNew = attachedCorreoNew;
            proveedor.setCorreo(correoNew);
            List<Telefono> attachedTelefonoNew = new ArrayList<Telefono>();
            for (Telefono telefonoNewTelefonoToAttach : telefonoNew) {
                telefonoNewTelefonoToAttach = em.getReference(telefonoNewTelefonoToAttach.getClass(), telefonoNewTelefonoToAttach.getCodigo());
                attachedTelefonoNew.add(telefonoNewTelefonoToAttach);
            }
            telefonoNew = attachedTelefonoNew;
            proveedor.setTelefono(telefonoNew);
            proveedor = em.merge(proveedor);
            for (Correo correoOldCorreo : correoOld) {
                if (!correoNew.contains(correoOldCorreo)) {
                    correoOldCorreo.setProveedor(null);
                    correoOldCorreo = em.merge(correoOldCorreo);
                }
            }
            for (Correo correoNewCorreo : correoNew) {
                if (!correoOld.contains(correoNewCorreo)) {
                    Proveedor oldProveedorOfCorreoNewCorreo = correoNewCorreo.getProveedor();
                    correoNewCorreo.setProveedor(proveedor);
                    correoNewCorreo = em.merge(correoNewCorreo);
                    if (oldProveedorOfCorreoNewCorreo != null && !oldProveedorOfCorreoNewCorreo.equals(proveedor)) {
                        oldProveedorOfCorreoNewCorreo.getCorreo().remove(correoNewCorreo);
                        oldProveedorOfCorreoNewCorreo = em.merge(oldProveedorOfCorreoNewCorreo);
                    }
                }
            }
            for (Telefono telefonoOldTelefono : telefonoOld) {
                if (!telefonoNew.contains(telefonoOldTelefono)) {
                    telefonoOldTelefono.setProveedor(null);
                    telefonoOldTelefono = em.merge(telefonoOldTelefono);
                }
            }
            for (Telefono telefonoNewTelefono : telefonoNew) {
                if (!telefonoOld.contains(telefonoNewTelefono)) {
                    Proveedor oldProveedorOfTelefonoNewTelefono = telefonoNewTelefono.getProveedor();
                    telefonoNewTelefono.setProveedor(proveedor);
                    telefonoNewTelefono = em.merge(telefonoNewTelefono);
                    if (oldProveedorOfTelefonoNewTelefono != null && !oldProveedorOfTelefonoNewTelefono.equals(proveedor)) {
                        oldProveedorOfTelefonoNewTelefono.getTelefono().remove(telefonoNewTelefono);
                        oldProveedorOfTelefonoNewTelefono = em.merge(oldProveedorOfTelefonoNewTelefono);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = proveedor.getCodigo();
                if (findProveedor(id) == null) {
                    throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.");
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
            Proveedor proveedor;
            try {
                proveedor = em.getReference(Proveedor.class, id);
                proveedor.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.", enfe);
            }
            List<Correo> correo = proveedor.getCorreo();
            for (Correo correoCorreo : correo) {
                correoCorreo.setProveedor(null);
                correoCorreo = em.merge(correoCorreo);
            }
            List<Telefono> telefono = proveedor.getTelefono();
            for (Telefono telefonoTelefono : telefono) {
                telefonoTelefono.setProveedor(null);
                telefonoTelefono = em.merge(telefonoTelefono);
            }
            em.remove(proveedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proveedor> findProveedorEntities() {
        return findProveedorEntities(true, -1, -1);
    }

    public List<Proveedor> findProveedorEntities(int maxResults, int firstResult) {
        return findProveedorEntities(false, maxResults, firstResult);
    }

    private List<Proveedor> findProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedor.class));
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

    public Proveedor findProveedor(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedor> rt = cq.from(Proveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
