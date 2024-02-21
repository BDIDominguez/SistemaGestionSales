package vistas;
public class VistaPantallaPrincipal extends javax.swing.JFrame {

    public VistaPantallaPrincipal() {
        initComponents();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fondo = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuArchivos = new javax.swing.JMenu();
        itemSalir = new javax.swing.JMenuItem();
        menuProductos = new javax.swing.JMenu();
        menuProveedores = new javax.swing.JMenu();
        itemProveedores = new javax.swing.JMenuItem();
        menuUsuarios = new javax.swing.JMenu();
        itemObjetos = new javax.swing.JMenuItem();
        itemPermisos = new javax.swing.JMenuItem();
        itemUsuarios = new javax.swing.JMenuItem();
        itemCambiarUsuario = new javax.swing.JMenuItem();
        menuMorro = new javax.swing.JMenu();
        itemMorros = new javax.swing.JMenuItem();
        itemCargaDiariaMorros = new javax.swing.JMenuItem();
        itemClientes = new javax.swing.JMenuItem();
        itemEntregas = new javax.swing.JMenuItem();
        itemConfirmarEntregas = new javax.swing.JMenuItem();
        menuCombustible = new javax.swing.JMenu();
        itemCamiones = new javax.swing.JMenuItem();
        itemChoferes = new javax.swing.JMenuItem();
        itemCargaCombustibles = new javax.swing.JMenuItem();
        itemOdometro = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Gestion");

        javax.swing.GroupLayout fondoLayout = new javax.swing.GroupLayout(fondo);
        fondo.setLayout(fondoLayout);
        fondoLayout.setHorizontalGroup(
            fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 963, Short.MAX_VALUE)
        );
        fondoLayout.setVerticalGroup(
            fondoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 539, Short.MAX_VALUE)
        );

        menuArchivos.setMnemonic('a');
        menuArchivos.setText("Archivos");
        menuArchivos.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N

        itemSalir.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemSalir.setMnemonic('s');
        itemSalir.setText("Salir");
        menuArchivos.add(itemSalir);

        jMenuBar1.add(menuArchivos);

        menuProductos.setMnemonic('p');
        menuProductos.setText("Productos");
        menuProductos.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jMenuBar1.add(menuProductos);

        menuProveedores.setMnemonic('r');
        menuProveedores.setText("Proveedores");
        menuProveedores.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N

        itemProveedores.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemProveedores.setText("Proveedores");
        menuProveedores.add(itemProveedores);

        jMenuBar1.add(menuProveedores);

        menuUsuarios.setMnemonic('u');
        menuUsuarios.setText("Usuarios");
        menuUsuarios.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N

        itemObjetos.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemObjetos.setText("Objetos");
        menuUsuarios.add(itemObjetos);

        itemPermisos.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemPermisos.setText("Permisos");
        menuUsuarios.add(itemPermisos);

        itemUsuarios.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemUsuarios.setText("Usuarios");
        menuUsuarios.add(itemUsuarios);

        itemCambiarUsuario.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemCambiarUsuario.setText("Cambiar Usuario");
        menuUsuarios.add(itemCambiarUsuario);

        jMenuBar1.add(menuUsuarios);

        menuMorro.setMnemonic('m');
        menuMorro.setText("Morros");
        menuMorro.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N

        itemMorros.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemMorros.setMnemonic('c');
        itemMorros.setText("Crear Morros");
        menuMorro.add(itemMorros);

        itemCargaDiariaMorros.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemCargaDiariaMorros.setMnemonic('p');
        itemCargaDiariaMorros.setText("Producción Diaria");
        menuMorro.add(itemCargaDiariaMorros);

        itemClientes.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemClientes.setMnemonic('l');
        itemClientes.setText("Clientes");
        menuMorro.add(itemClientes);

        itemEntregas.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemEntregas.setMnemonic('e');
        itemEntregas.setText("Envio a Clientes");
        menuMorro.add(itemEntregas);

        itemConfirmarEntregas.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemConfirmarEntregas.setMnemonic('o');
        itemConfirmarEntregas.setText("Confirmar Entrega");
        menuMorro.add(itemConfirmarEntregas);

        jMenuBar1.add(menuMorro);

        menuCombustible.setMnemonic('c');
        menuCombustible.setText("Combustibles");
        menuCombustible.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N

        itemCamiones.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemCamiones.setMnemonic('a');
        itemCamiones.setText("Camiones Existentes");
        menuCombustible.add(itemCamiones);

        itemChoferes.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemChoferes.setMnemonic('h');
        itemChoferes.setText("Choferes Existentes");
        menuCombustible.add(itemChoferes);

        itemCargaCombustibles.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemCargaCombustibles.setMnemonic('r');
        itemCargaCombustibles.setText("Cargas de Combustibles");
        menuCombustible.add(itemCargaCombustibles);

        itemOdometro.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemOdometro.setMnemonic('t');
        itemOdometro.setText("Control Odometro");
        menuCombustible.add(itemOdometro);

        jMenuBar1.add(menuCombustible);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fondo)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(fondo)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
       try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VistaPantallaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaPantallaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaPantallaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaPantallaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VistaPantallaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JDesktopPane fondo;
    public javax.swing.JMenuItem itemCambiarUsuario;
    public javax.swing.JMenuItem itemCamiones;
    public javax.swing.JMenuItem itemCargaCombustibles;
    public javax.swing.JMenuItem itemCargaDiariaMorros;
    public javax.swing.JMenuItem itemChoferes;
    public javax.swing.JMenuItem itemClientes;
    public javax.swing.JMenuItem itemConfirmarEntregas;
    public javax.swing.JMenuItem itemEntregas;
    public javax.swing.JMenuItem itemMorros;
    public javax.swing.JMenuItem itemObjetos;
    public javax.swing.JMenuItem itemOdometro;
    public javax.swing.JMenuItem itemPermisos;
    public javax.swing.JMenuItem itemProveedores;
    public javax.swing.JMenuItem itemSalir;
    public javax.swing.JMenuItem itemUsuarios;
    private javax.swing.JMenuBar jMenuBar1;
    public javax.swing.JMenu menuArchivos;
    private javax.swing.JMenu menuCombustible;
    private javax.swing.JMenu menuMorro;
    public javax.swing.JMenu menuProductos;
    public javax.swing.JMenu menuProveedores;
    public javax.swing.JMenu menuUsuarios;
    // End of variables declaration//GEN-END:variables
}
