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
        jMenu2 = new javax.swing.JMenu();
        menuProveedores = new javax.swing.JMenu();
        itemProveedores = new javax.swing.JMenuItem();
        menuUsuarios = new javax.swing.JMenu();
        itemObjetos = new javax.swing.JMenuItem();
        itemPermisos = new javax.swing.JMenuItem();
        itemUsuarios = new javax.swing.JMenuItem();
        itemCambiarUsuario = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        itemMorros = new javax.swing.JMenuItem();
        itemCargaDiariaMorros = new javax.swing.JMenuItem();
        itemClientes = new javax.swing.JMenuItem();
        itemEntregas = new javax.swing.JMenuItem();
        itemConfirmarEntregas = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
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

        menuArchivos.setText("Archivos");
        menuArchivos.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N

        itemSalir.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemSalir.setText("Salir");
        menuArchivos.add(itemSalir);

        jMenuBar1.add(menuArchivos);

        jMenu2.setText("Productos");
        jMenu2.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        jMenuBar1.add(jMenu2);

        menuProveedores.setText("Proveedores");
        menuProveedores.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N

        itemProveedores.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemProveedores.setText("Proveedores");
        menuProveedores.add(itemProveedores);

        jMenuBar1.add(menuProveedores);

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

        jMenu1.setText("Morros");
        jMenu1.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N

        itemMorros.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemMorros.setText("Crear Morros");
        jMenu1.add(itemMorros);

        itemCargaDiariaMorros.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemCargaDiariaMorros.setText("Producción Diaria");
        jMenu1.add(itemCargaDiariaMorros);

        itemClientes.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemClientes.setText("Clientes");
        jMenu1.add(itemClientes);

        itemEntregas.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemEntregas.setText("Envio a Clientes");
        jMenu1.add(itemEntregas);

        itemConfirmarEntregas.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemConfirmarEntregas.setText("Confirmar Entrega");
        jMenu1.add(itemConfirmarEntregas);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Combustibles");
        jMenu3.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N

        itemCamiones.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemCamiones.setText("Camiones Existentes");
        jMenu3.add(itemCamiones);

        itemChoferes.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemChoferes.setText("Choferes Existentes");
        jMenu3.add(itemChoferes);

        itemCargaCombustibles.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemCargaCombustibles.setText("Cargas de Combustibles");
        jMenu3.add(itemCargaCombustibles);

        itemOdometro.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_DOWN_MASK));
        itemOdometro.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        itemOdometro.setText("Control Odometro");
        jMenu3.add(itemOdometro);

        jMenuBar1.add(jMenu3);

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
    private javax.swing.JMenuItem itemOdometro;
    public javax.swing.JMenuItem itemPermisos;
    public javax.swing.JMenuItem itemProveedores;
    public javax.swing.JMenuItem itemSalir;
    public javax.swing.JMenuItem itemUsuarios;
    private javax.swing.JMenu jMenu1;
    public javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    public javax.swing.JMenu menuArchivos;
    public javax.swing.JMenu menuProveedores;
    public javax.swing.JMenu menuUsuarios;
    // End of variables declaration//GEN-END:variables
}
