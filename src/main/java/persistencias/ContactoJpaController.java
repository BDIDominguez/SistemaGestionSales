/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencias;

import entidades.Contacto;
import java.io.Serializable;
import jakarta.persistence.Query;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import entidades.Telefono;
import java.util.ArrayList;
import java.util.List;
import entidades.Correo;
import entidades.Direccion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import persistencias.exceptions.NonexistentEntityException;

/**
 *
 * @author Dario
 */
public class ContactoJpaController implements Serializable {

    public ContactoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contacto contacto) {
        if (contacto.getTelefono() == null) {
            contacto.setTelefono(new ArrayList<Telefono>());
        }
        if (contacto.getCorreo() == null) {
            contacto.setCorreo(new ArrayList<Correo>());
        }
        if (contacto.getDireccion() == null) {
            contacto.setDireccion(new ArrayList<Direccion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Telefono> attachedTelefono = new ArrayList<Telefono>();
            for (Telefono telefonoTelefonoToAttach : contacto.getTelefono()) {
                telefonoTelefonoToAttach = em.getReference(telefonoTelefonoToAttach.getClass(), telefonoTelefonoToAttach.getCodigo());
                attachedTelefono.add(telefonoTelefonoToAttach);
            }
            contacto.setTelefono(attachedTelefono);
            List<Correo> attachedCorreo = new ArrayList<Correo>();
            for (Correo correoCorreoToAttach : contacto.getCorreo()) {
                correoCorreoToAttach = em.getReference(correoCorreoToAttach.getClass(), correoCorreoToAttach.getCodigo());
                attachedCorreo.add(correoCorreoToAttach);
            }
            contacto.setCorreo(attachedCorreo);
            List<Direccion> attachedDireccion = new ArrayList<Direccion>();
            for (Direccion direccionDireccionToAttach : contacto.getDireccion()) {
                direccionDireccionToAttach = em.getReference(direccionDireccionToAttach.getClass(), direccionDireccionToAttach.getCodigo());
                attachedDireccion.add(direccionDireccionToAttach);
            }
            contacto.setDireccion(attachedDireccion);
            em.persist(contacto);
            for (Telefono telefonoTelefono : contacto.getTelefono()) {
                Contacto oldContactoOfTelefonoTelefono = telefonoTelefono.getContacto();
                telefonoTelefono.setContacto(contacto);
                telefonoTelefono = em.merge(telefonoTelefono);
                if (oldContactoOfTelefonoTelefono != null) {
                    oldContactoOfTelefonoTelefono.getTelefono().remove(telefonoTelefono);
                    oldContactoOfTelefonoTelefono = em.merge(oldContactoOfTelefonoTelefono);
                }
            }
            for (Correo correoCorreo : contacto.getCorreo()) {
                Contacto oldContactoOfCorreoCorreo = correoCorreo.getContacto();
                correoCorreo.setContacto(contacto);
                correoCorreo = em.merge(correoCorreo);
                if (oldContactoOfCorreoCorreo != null) {
                    oldContactoOfCorreoCorreo.getCorreo().remove(correoCorreo);
                    oldContactoOfCorreoCorreo = em.merge(oldContactoOfCorreoCorreo);
                }
            }
            for (Direccion direccionDireccion : contacto.getDireccion()) {
                Contacto oldContactoOfDireccionDireccion = direccionDireccion.getContacto();
                direccionDireccion.setContacto(contacto);
                direccionDireccion = em.merge(direccionDireccion);
                if (oldContactoOfDireccionDireccion != null) {
                    oldContactoOfDireccionDireccion.getDireccion().remove(direccionDireccion);
                    oldContactoOfDireccionDireccion = em.merge(oldContactoOfDireccionDireccion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Contacto contacto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Contacto persistentContacto = em.find(Contacto.class, contacto.getCodigo());
            List<Telefono> telefonoOld = persistentContacto.getTelefono();
            List<Telefono> telefonoNew = contacto.getTelefono();
            List<Correo> correoOld = persistentContacto.getCorreo();
            List<Correo> correoNew = contacto.getCorreo();
            List<Direccion> direccionOld = persistentContacto.getDireccion();
            List<Direccion> direccionNew = contacto.getDireccion();
            List<Telefono> attachedTelefonoNew = new ArrayList<Telefono>();
            for (Telefono telefonoNewTelefonoToAttach : telefonoNew) {
                telefonoNewTelefonoToAttach = em.getReference(telefonoNewTelefonoToAttach.getClass(), telefonoNewTelefonoToAttach.getCodigo());
                attachedTelefonoNew.add(telefonoNewTelefonoToAttach);
            }
            telefonoNew = attachedTelefonoNew;
            contacto.setTelefono(telefonoNew);
            List<Correo> attachedCorreoNew = new ArrayList<Correo>();
            for (Correo correoNewCorreoToAttach : correoNew) {
                correoNewCorreoToAttach = em.getReference(correoNewCorreoToAttach.getClass(), correoNewCorreoToAttach.getCodigo());
                attachedCorreoNew.add(correoNewCorreoToAttach);
            }
            correoNew = attachedCorreoNew;
            contacto.setCorreo(correoNew);
            List<Direccion> attachedDireccionNew = new ArrayList<Direccion>();
            for (Direccion direccionNewDireccionToAttach : direccionNew) {
                direccionNewDireccionToAttach = em.getReference(direccionNewDireccionToAttach.getClass(), direccionNewDireccionToAttach.getCodigo());
                attachedDireccionNew.add(direccionNewDireccionToAttach);
            }
            direccionNew = attachedDireccionNew;
            contacto.setDireccion(direccionNew);
            contacto = em.merge(contacto);
            for (Telefono telefonoOldTelefono : telefonoOld) {
                if (!telefonoNew.contains(telefonoOldTelefono)) {
                    telefonoOldTelefono.setContacto(null);
                    telefonoOldTelefono = em.merge(telefonoOldTelefono);
                }
            }
            for (Telefono telefonoNewTelefono : telefonoNew) {
                if (!telefonoOld.contains(telefonoNewTelefono)) {
                    Contacto oldContactoOfTelefonoNewTelefono = telefonoNewTelefono.getContacto();
                    telefonoNewTelefono.setContacto(contacto);
                    telefonoNewTelefono = em.merge(telefonoNewTelefono);
                    if (oldContactoOfTelefonoNewTelefono != null && !oldContactoOfTelefonoNewTelefono.equals(contacto)) {
                        oldContactoOfTelefonoNewTelefono.getTelefono().remove(telefonoNewTelefono);
                        oldContactoOfTelefonoNewTelefono = em.merge(oldContactoOfTelefonoNewTelefono);
                    }
                }
            }
            for (Correo correoOldCorreo : correoOld) {
                if (!correoNew.contains(correoOldCorreo)) {
                    correoOldCorreo.setContacto(null);
                    correoOldCorreo = em.merge(correoOldCorreo);
                }
            }
            for (Correo correoNewCorreo : correoNew) {
                if (!correoOld.contains(correoNewCorreo)) {
                    Contacto oldContactoOfCorreoNewCorreo = correoNewCorreo.getContacto();
                    correoNewCorreo.setContacto(contacto);
                    correoNewCorreo = em.merge(correoNewCorreo);
                    if (oldContactoOfCorreoNewCorreo != null && !oldContactoOfCorreoNewCorreo.equals(contacto)) {
                        oldContactoOfCorreoNewCorreo.getCorreo().remove(correoNewCorreo);
                        oldContactoOfCorreoNewCorreo = em.merge(oldContactoOfCorreoNewCorreo);
                    }
                }
            }
            for (Direccion direccionOldDireccion : direccionOld) {
                if (!direccionNew.contains(direccionOldDireccion)) {
                    direccionOldDireccion.setContacto(null);
                    direccionOldDireccion = em.merge(direccionOldDireccion);
                }
            }
            for (Direccion direccionNewDireccion : direccionNew) {
                if (!direccionOld.contains(direccionNewDireccion)) {
                    Contacto oldContactoOfDireccionNewDireccion = direccionNewDireccion.getContacto();
                    direccionNewDireccion.setContacto(contacto);
                    direccionNewDireccion = em.merge(direccionNewDireccion);
                    if (oldContactoOfDireccionNewDireccion != null && !oldContactoOfDireccionNewDireccion.equals(contacto)) {
                        oldContactoOfDireccionNewDireccion.getDireccion().remove(direccionNewDireccion);
                        oldContactoOfDireccionNewDireccion = em.merge(oldContactoOfDireccionNewDireccion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = contacto.getCodigo();
                if (findContacto(id) == null) {
                    throw new NonexistentEntityException("The contacto with id " + id + " no longer exists.");
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
            Contacto contacto;
            try {
                contacto = em.getReference(Contacto.class, id);
                contacto.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contacto with id " + id + " no longer exists.", enfe);
            }
            List<Telefono> telefono = contacto.getTelefono();
            for (Telefono telefonoTelefono : telefono) {
                telefonoTelefono.setContacto(null);
                telefonoTelefono = em.merge(telefonoTelefono);
            }
            List<Correo> correo = contacto.getCorreo();
            for (Correo correoCorreo : correo) {
                correoCorreo.setContacto(null);
                correoCorreo = em.merge(correoCorreo);
            }
            List<Direccion> direccion = contacto.getDireccion();
            for (Direccion direccionDireccion : direccion) {
                direccionDireccion.setContacto(null);
                direccionDireccion = em.merge(direccionDireccion);
            }
            em.remove(contacto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Contacto> findContactoEntities() {
        return findContactoEntities(true, -1, -1);
    }

    public List<Contacto> findContactoEntities(int maxResults, int firstResult) {
        return findContactoEntities(false, maxResults, firstResult);
    }

    private List<Contacto> findContactoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contacto.class));
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

    public Contacto findContacto(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contacto.class, id);
        } finally {
            em.close();
        }
    }

    public int getContactoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contacto> rt = cq.from(Contacto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
