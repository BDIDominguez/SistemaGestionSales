package entidades;

import java.time.LocalDate;
import java.util.List;
import persistencias.ControladoraPersistencia;

/**
* @author Dario
*/
public class Controladora {
    ControladoraPersistencia ctrl = new ControladoraPersistencia();
    
    // ----------------- OTRAS CONSULTAS  CONSULTASJPA   ----------
    public LocalDate consultaFechaServidor(){
        return ctrl.consultarFechaServidor();
    }
    public String ultimoNumeroRemitoEntrega(){
        return ctrl.ultimoNumeroRemitoEntrega();
    }
    public List<Combustible> traerCargasCombustiblesConFecha(LocalDate fecha){
        return ctrl.traerCargasCombustiblesConFecha(fecha);
    }
    public boolean existeFacturaCargaCombustible(String factura){
        return ctrl.existeFacturaCargaCombustible(factura);
    }
    
    // -----------------   OBJETO ------------------
    public void crearObjeto(Objeto obj){
        ctrl.crearObjeto(obj);
    }
    public void eliminarObjeto(int id){
        ctrl.eliminarObjeto(id);
    }
    public void editarObjeto(Objeto obj){
        ctrl.editarObjeto(obj);
    }
    public Objeto traerObjeto(int id){
        Objeto obj = ctrl.traerObjeto(id);
        if (obj == null){
            ComandosComunes comandos = new ComandosComunes();
            comandos.crearObjetos();
            obj = traerObjeto(id);
        }
        return obj;
    }
    public List<Objeto> traerListaObjetos(){
        return ctrl.traerListaObjetos();
    }
    
    // -----------------   OBVJETO ------------------
    public void crearPermiso(Permiso per){
        ctrl.crearPermiso(per);
    }
    public void eliminarPermiso(int id){
        ctrl.eliminarPermiso(id);
    }
    public void editarPermiso(Permiso per){
        ctrl.editarPermiso(per);
    }
    public Permiso traerPermiso(int id){
        return ctrl.traerPermiso(id);
    }
    public List<Permiso> traerListaPermisos(){
        return ctrl.traerListaPermisos();
    }
    
    // -----------------   USUARIO    ------------------
    public void crearUsuario(Usuario user){
        ctrl.crearUsuario(user);
    }
    public void eliminarUsuario(int id){
        ctrl.eliminarUsuario(id);
    }
    public void editarUsuario(Usuario user){
        ctrl.editarUsuario(user);
    }
    public Usuario traerUsuario(int id){
        return ctrl.traerUsuario(id);
    }
    public List<Usuario> traerListaUsuarios(){
        return ctrl.traerListaUsuarios();
    }
    
    // -----------------   PROVEEDORES    ------------------
    
    public void crearProveedor(Proveedor prov){
        ctrl.crearProveedor(prov);
    }
    public void eliminarProveedor(int id){
        ctrl.eliminarProveedor(id);
    }
    public void editarProveedor(Proveedor prov){
        ctrl.editarProveedor(prov);
    }
    public Proveedor traerProveedor(int id){
        return ctrl.traerProveedor(id);
    }
    public List<Proveedor> traerListaProveedor(){
        return ctrl.traerListaProveedor();
    }
    
    // -----------------   CAMION   ------------------
    public void crearCamion(Camion camion){
        ctrl.crearCamion(camion);
    }
    public void eliminarCamion(int id){
        ctrl.eliminarCamion(id);
    }
    public void editarCamion(Camion obj){
        ctrl.editarCamion(obj);
    }
    public Camion traerCamion(int id){
        return ctrl.traerCamion(id);
    }
    public List<Camion> traerListaCamiones(){
        return ctrl.traerListaCamiones();
    }
    
    // -----------------   CHOFER   ------------------
    public void crearChofer(Chofer chofer){
        ctrl.crearChofer(chofer);
    }
    public void eliminarChofer(int id){
        ctrl.eliminarChofer(id);
    }
    public void editarChofer(Chofer chofer){
        ctrl.editarChofer(chofer);
    }
    public Chofer traerChofer(int id){
        return ctrl.traerChofer(id);
    }
    public List<Chofer> traerListaChoferes(){
        return ctrl.traerListaChoferes();
    }
    
    // -----------------   CLIENTE   ------------------
    public void crearCliente(Cliente cliente){
        ctrl.crearCliente(cliente);
    }
    public void eliminarCliente(int id){
        ctrl.eliminarCliente(id);
    }
    public void editarCliente(Cliente cliente){
        ctrl.editarCliente(cliente);
    }
    public Cliente traerCliente(int id){
        return ctrl.traerCliente(id);
    }
    public List<Cliente> traerListaClientes(){
        return ctrl.traerListaClientes();
    }
    
    // -----------------   COMBUSTIBLE   ------------------
    public void crearCombustible(Combustible combustible){
        ctrl.crearCombustible(combustible);
    }
    public void eliminarCombustible(int id){
        ctrl.eliminarCombustible(id);
    }
    public void editarCombustible(Combustible combustible){
        ctrl.editarCombustible(combustible);
    }
    public Combustible traerCombustible(int id){
        return ctrl.traerCombustible(id);
    }
    public List<Combustible> traerListaCombustibles(){
        return ctrl.traerListaCombustibles();
    }
    
    // -----------------   ENTREGA   ------------------
    public void crearEntrega(Entrega entrega){
        ctrl.crearEntrega(entrega);
    }
    public void eliminarEntrega(int id){
        ctrl.eliminarEntrega(id);
    }
    public void editarEntrega(Entrega entrega){
        ctrl.editarEntrega(entrega);
    }
    public Entrega traerEntrega(int id){
        return ctrl.traerEntrega(id);
    }
    public List<Entrega> traerListaEntregas(){
        return ctrl.traerListaEntregas();
    }
    public List<Entrega> traerListaEntregasPorFechaSalida(LocalDate fecha){
        return ctrl.traerListaEntregasPorFechaSalida(fecha);
    }
    public boolean existeNumeroRemito(String remito){
        return ctrl.existeNumeroRemito(remito);
    }
           
    
    // -----------------   MORRO   ------------------
    public void crearMorro(Morro morro){
        ctrl.crearMorro(morro);
    }
    public void eliminarMorro(int id){
        ctrl.eliminarMorro(id);
    }
    public void editarMorro(Morro morro){
        ctrl.editarMorro(morro);
    }
    public Morro traerMorro(int id){
        return ctrl.traerMorro(id);
    }
    public List<Morro> traerListaMorros(){
        return ctrl.traerListaMorros();
    }
    
} // Fin de la Clase
