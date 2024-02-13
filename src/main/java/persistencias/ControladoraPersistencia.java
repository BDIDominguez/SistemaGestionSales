package persistencias;

import entidades.Camion;
import entidades.CamionOdometro;
import entidades.CargaMorro;
import entidades.Chofer;
import entidades.Cliente;
import entidades.Combustible;
import entidades.Entrega;
import entidades.Morro;
import entidades.Objeto;
import entidades.Permiso;
import entidades.Proveedor;
import entidades.Usuario;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import persistencias.exceptions.NonexistentEntityException;

/**
 * @author Dario
 */
public class ControladoraPersistencia {

    ObjetoJpaController objJpa;
    PermisoJpaController perJpa;
    UsuarioJpaController userJpa;
    ProveedorJpaController provJpa;
    CamionJpaController camionJpa;
    ChoferJpaController choferJpa;
    ClienteJpaController clienteJpa;
    CombustibleJpaController combustibleJpa;
    EntregaJpaController entregaJpa;
    MorroJpaController morroJpa;
    ConsultasJPA consultasJpa;
    CargaMorroJpaController cargaMorroJpa;
    CamionOdometroJpaController odometroJpa;

    public ControladoraPersistencia() {
        EntityManagerFactory emf = getEntityManagerFactory();
        objJpa = new ObjetoJpaController(emf);
        perJpa = new PermisoJpaController(emf);
        userJpa = new UsuarioJpaController(emf);
        provJpa = new ProveedorJpaController(emf);
        camionJpa = new CamionJpaController(emf);
        choferJpa = new ChoferJpaController(emf);
        clienteJpa = new ClienteJpaController(emf);
        combustibleJpa = new CombustibleJpaController(emf);
        entregaJpa = new EntregaJpaController(emf);
        morroJpa = new MorroJpaController(emf);
        consultasJpa = new ConsultasJPA(emf);
        cargaMorroJpa = new CargaMorroJpaController(emf);
        odometroJpa = new CamionOdometroJpaController(emf);
    
    }
    // ----------------- OTRAS CONSULTAS -------------
    public LocalDate consultarFechaServidor(){
        return consultasJpa.consultarFechaServidor();
    }

    public String ultimoNumeroRemitoEntrega(){
        return consultasJpa.ultimoNumeroRemitoEntrega();
    }
    public List<Combustible> traerCargasCombustiblesConFecha(LocalDate fecha){
        return consultasJpa.traerCargasCombustiblesConFecha(fecha);
    }
    public Combustible existeFacturaCargaCombustible(String factura){
        return consultasJpa.existeFacturaCargaCombustible(factura);
    }
    public List<CargaMorro> traerCargasMorroConFecha(LocalDate fecha){
        return consultasJpa.traerCargasMorroConFecha(fecha);
    }
    public List<CargaMorro> traerCargasMorroConFechaMorro(LocalDate fecha, int codigo){
        return consultasJpa.traerCargasMorroConFechaMorro(fecha,codigo);
    }
    
    public List<Entrega> traerEntregaPorClienteAConfirmar(Cliente cliente){
        return consultasJpa.traerEntregaPorClienteAConfirmar(cliente);
    }
    public List<CamionOdometro> traerCamionOdometroPorCamion(int codigo){
        return consultasJpa.traerCamionOdometroPorCamion(codigo);
    }
    public List<CamionOdometro> traerCamionOdometroPorFecha(LocalDate fecha){
        return consultasJpa.traerCamionOdometroPorFecha(fecha);
    }
    public List<CamionOdometro> traerCamionOdometroPorFechaCamion(LocalDate fecha, int codigo){
        return consultasJpa.traerCamionOdometroPorFechaCamino(fecha,codigo);
    }

    // -----------------   OBJETO ------------------
    public void crearObjeto(Objeto obj) {
        objJpa.create(obj);
    }

    public void eliminarObjeto(int id) {
        try {
            objJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarObjeto(Objeto obj) {
        try {
            objJpa.edit(obj);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Objeto traerObjeto(int id) {
        return objJpa.findObjeto(id);
    }

    public List<Objeto> traerListaObjetos() {
        return objJpa.findObjetoEntities();
    }
    
    // -----------------   CargaMorro ------------------
    public void crearCargaMorro(CargaMorro obj) {
        cargaMorroJpa.create(obj);
    }

    public void eliminarCargaMorro(int id) {
        try {
            cargaMorroJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarCargaMorro(CargaMorro obj) {
        try {
            cargaMorroJpa.edit(obj);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CargaMorro traerCargaMorro(int id) {
        return cargaMorroJpa.findCargaMorro(id);
    }

    public List<CargaMorro> traerListaCargaMorros() {
        return cargaMorroJpa.findCargaMorroEntities();
    }

    // -----------------   PERMISO ------------------
    public void crearPermiso(Permiso per) {
        perJpa.create(per);
    }

    public void eliminarPermiso(int id) {
        try {
            perJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarPermiso(Permiso per) {
        try {
            perJpa.edit(per);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Permiso traerPermiso(int id) {
        return perJpa.findPermiso(id);
    }

    public List<Permiso> traerListaPermisos() {
        return perJpa.findPermisoEntities();
    }
    

    // -----------------   USUARIOS   ------------------
    public void crearUsuario(Usuario user) {
        userJpa.create(user);
    }

    public void eliminarUsuario(int id) {
        try {
            userJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarUsuario(Usuario user) {
        try {
            userJpa.edit(user);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    public Usuario traerUsuario(int id) {
        return userJpa.findUsuario(id);
    }

    public List<Usuario> traerListaUsuarios() {
        return userJpa.findUsuarioEntities();
    }
    
    // -----------------   PROVEEDOR   ------------------
    
    public void crearProveedor(Proveedor prov) {
        provJpa.create(prov);
    }

    public void eliminarProveedor(int id) {
        try {
            provJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarProveedor(Proveedor prov) {
        try {
            provJpa.edit(prov);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    public Proveedor traerProveedor(int id) {
        return provJpa.findProveedor(id);
    }

    public List<Proveedor> traerListaProveedor() {
        return provJpa.findProveedorEntities();
    }
    
    // -----------------   MORRO   ------------------
    public void crearMorro(Morro morro) {
            morroJpa.create(morro);
    }

    public void eliminarMorro(int id) {
        try {
            morroJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarMorro(Morro morro) {
        try {
            morroJpa.edit(morro);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Morro traerMorro(int id) {
        return morroJpa.findMorro(id);
    }

    public List<Morro> traerListaMorros() {
        return morroJpa.findMorroEntities();
    }
    
    
    // -----------------   ENTREGA   ------------------
    public void crearEntrega(Entrega entrega) {
            entregaJpa.create(entrega);
    }

    public void eliminarEntrega(int id) {
        try {
            entregaJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarEntrega(Entrega entrega) {
        try {
            entregaJpa.edit(entrega);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Entrega traerEntrega(int id) {
        return entregaJpa.findEntrega(id);
    }

    public List<Entrega> traerListaEntregas() {
        return entregaJpa.findEntregaEntities();
    }
    
    public List<Entrega> traerListaEntregasPorFechaSalida(LocalDate fechasalida){
        return consultasJpa.traerEntregasPorFechaSalida(fechasalida);
    }
    
    public boolean existeNumeroRemito(String numero){
        return consultasJpa.existeNumeroRemito(numero);
    }
    
    
    // -----------------   COMBUSTIBLE   ------------------
    public void crearCombustible(Combustible combu) {
        try {
            combustibleJpa.create(combu);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarCombustible(int id) {
        try {
            combustibleJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarCombustible(Combustible combu) {
        try {
            combustibleJpa.edit(combu);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Combustible traerCombustible(int id) {
        return combustibleJpa.findCombustible(id);
    }

    public List<Combustible> traerListaCombustibles() {
        return combustibleJpa.findCombustibleEntities();
    }
    

    // -----------------   CLIENTE   ------------------
    public void crearCliente(Cliente cliente) {
        clienteJpa.create(cliente);
    }

    public void eliminarCliente(int id) {
        try {
            clienteJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarCliente(Cliente cliente) {
        try {
            clienteJpa.edit(cliente);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Cliente traerCliente(int id) {
        return clienteJpa.findCliente(id);
    }

    public List<Cliente> traerListaClientes() {
        return clienteJpa.findClienteEntities();
    }
    
    // -----------------   CAMION   ------------------
    public void crearCamion(Camion camion) {
        camionJpa.create(camion);
    }

    public void eliminarCamion(int id) {
        try {
            camionJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarCamion(Camion camion) {
        try {
            camionJpa.edit(camion);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Camion traerCamion(int id) {
        return camionJpa.findCamion(id);
    }

    public List<Camion> traerListaCamiones() {
        return camionJpa.findCamionEntities();
    }
    
    // -----------------   CHOFER   ------------------
    public void crearChofer(Chofer chofer) {
        choferJpa.create(chofer);
    }

    public void eliminarChofer(int id) {
        try {
            choferJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarChofer(Chofer chofer) {
        try {
            choferJpa.edit(chofer);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Chofer traerChofer(int id) {
        return choferJpa.findChofer(id);
    }

    public List<Chofer> traerListaChoferes() {
        return choferJpa.findChoferEntities();
    }
    
    // -----------------   CAMION ODOMETRO ---------------
    public void crearCamionOdometro(CamionOdometro odo) {
        odometroJpa.create(odo);
    }

    public void eliminarCamionOdometro(int id) {
        try {
            odometroJpa.destroy(id);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarCamionOdometro(CamionOdometro odo) {
        try {
            odometroJpa.edit(odo);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CamionOdometro traerCamionOdometro(int id) {
        return odometroJpa.findCamionOdometro(id);
    }

    public List<CamionOdometro> traerListaCamionOdometros() {
        return odometroJpa.findCamionOdometroEntities();
    }
    
       
    // ------------------ CODIGO PARA CARGAR CONFIGURACION DEL CONFIG.PROPERTIE --------------------------

    private Properties loadProperties() {
        Properties prop = new Properties();
        // Intenta cargar el archivo config.properties del directorio actual
        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
        } catch (IOException e) {
            // Si no se encuentra en el directorio actual, intenta cargar desde los recursos
            try (InputStream resourceInput = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (resourceInput == null) {
                    System.out.println("No se pudo encontrar el archivo de propiedades.");
                    return prop;
                }
                prop.load(resourceInput);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return prop;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        Properties prop = loadProperties();
        String url = prop.getProperty("jdbc.url");
        String user = prop.getProperty("jdbc.user");
        String password = prop.getProperty("jdbc.password");
        

        // Configura la fábrica de EntityManager con los datos del archivo de propiedades
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("SistemaGestionUP", createProperties(url, user, password));
        return emf;
    }

    private Map<String, String> createProperties(String url, String user, String password) {
        Map<String, String> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.url", url);
        properties.put("jakarta.persistence.jdbc.user", user);
        properties.put("jakarta.persistence.jdbc.password", password);
        properties.put("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
        properties.put("jakarta.persistence.schema-generation.database.action", "create");
        return properties;
    }
} // Fin de la Clase
