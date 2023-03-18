package Vista;

import Modelo.Cliente;
import Modelo.ClienteDAO;
import Modelo.Config;
import Modelo.Detalle;
import Modelo.Eventos;
import Modelo.Productos;
import Modelo.ProductosDAO;
import Modelo.Proveedor;
import Modelo.ProveedorDAO;
import Modelo.Venta;
import Modelo.VentaDAO;
import Modelo.login;
import Reportes.Excel;
import Reportes.Graficos;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.awt.Desktop;
//import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author godie
 */
public final class Sistema extends javax.swing.JFrame {

    // Capturamos la fecha actual para nuestra venta
    Date fechaVenta = new Date();
    String fechaActual = new SimpleDateFormat("dd/MM/yyyy").format(fechaVenta);
    //Instanciamos las clases Clinete y ClienteDAO.
    Cliente cl = new Cliente();
    ClienteDAO cliDao = new ClienteDAO();
    //Instanciamos las clases Proveedor y ProveedorDAO.
    Proveedor pro = new Proveedor();
    ProveedorDAO proDao = new ProveedorDAO();
    //Instanciamos las clases Productos y ProductosDAO.
    Productos producto = new Productos();
    ProductosDAO productoDao = new ProductosDAO();
    // Instanciamos las clases Venta y VentaDAO.
    Venta venta = new Venta();
    VentaDAO vdao = new VentaDAO();
    // Instanciamos la clase Detalle.
    Detalle dv = new Detalle();
    // Instanceamos la clse config.
    Config confi = new Config();
    // Lamamos la clase Eventos para relizar las vaidaciones a nuestros campos de texto.
    Eventos validar = new Eventos();
    // Instanciamos el DefaultTableModel y su variable modelo;
    DefaultTableModel modelo = new DefaultTableModel();
    //Creamos una variable de tipo entero para el item.
    int item;
    // Creamos variable de tipo double para el total a pagar.
    double Totalpagar = 0.0;
    DefaultTableModel dftm = new DefaultTableModel();

    public Sistema() {
        initComponents();
        this.setLocationRelativeTo(null);
    }
    public Sistema(login privilegios)
    {
        initComponents();
        this.setLocationRelativeTo(null);
        txtIdPro.setVisible(false);
        txtIdCliente.setVisible(false);
        txtIdProveedor.setVisible(false);
        cbxProveedor.addItem("Selecione una Opcion");
        txtIdProducto.setVisible(false);
        txtIdVenta.setVisible(false);
        txtIdConfig.setVisible(false);
        txtTelefonoClienteVenta.setVisible(false);
        txtDireccionClienteVenta.setVisible(false);
        txtRazonClienteVenta.setVisible(false);
        //Hacemos que el cbxProveedor se autocomplete cuando escribimos sobre el JComboBox, hacemos uso de la librería swingx-all-1.6.4.jar
        AutoCompleteDecorator.decorate(cbxProveedor);
        //Ejecutamos el método ConsultarProveedor.
        productoDao.ConsultarProveedor(cbxProveedor);
        ListarConfig();
        // Aquí le asignamos las vistas al asistente.
        if (privilegios.getRol().equals("Asistente")) {
            // Aquí bloquemos los botones para el asistente
            btnProveedor.setEnabled(false);
            //btnProveedor.setVisible(false);
            //btnProducto.setEnabled(false);
            //btnProducto.setVisible(false);
            btnConfig.setEnabled(false);
            //btnConfig.setVisible(false);
            btnActualizarProducto.setEnabled(false);
            btnEliminarProducto.setEnabled(false);
            btnRegistrarUsuarios.setEnabled(false);
            // Aquí capturamos el nombre del usuario logiado en nuestro sistema.
            lblVendedor.setText(privilegios.getNombre());
        }else{
            // Si es administrador tendrá acceso a todos los botones y funciones del sistema.
            lblVendedor.setText(privilegios.getNombre());
        }
    }

    //Creamos el método que nos permita limpiar las cajas de texto del formulario cliente al momento de que demos clip en el botón Guardar.
    public void Limpiar() {
        txtIdCliente.setText("");
        txtCedulaCliente.setText("");
        txtNombreCliente.setText("");
        txtTelefonoCliente.setText("");
        txtDireccionCliente.setText("");
        txtRazonSocialCliente.setText("");
    }

    //Creamos el método que nos permita limpiar las cajas de texto del formulario proveedor al momento de que demos clip en el botón Guardar.
    public void LimpiarProveedor() {
        txtIdProveedor.setText("");
        txtDocProveedor.setText("");
        txtNomProveedor.setText("");
        txtTelProveedor.setText("");
        txtDirProveedor.setText("");
        txtRazonSoProveedor.setText("");
    }

    public void LimpiarProducto() {
        //Con este método limpiamos todas las cajas de texto del formulario productos.
        txtIdProducto.setText("");
        txtCodigoProducto.setText("");
        txtDescripcionProducto.setText("");
        cbxProveedor.setSelectedIndex(0);
        txtStockProducto.setText("");
        txtPrecioPeroducto.setText("");
    }

    //Creamos el mètodo que nos va a peermitir listar los clientes que están almacenados en la base de datos.
    public void ListarCliente() {
        List<Cliente> listacliente = cliDao.ListarCliente();
        //Luego de llamar el método ListarCliente de la clase ClienteDAO procecdemos a almacenar los resultados en la variable modelo del DefaultTableModel.
        modelo = (DefaultTableModel) tblClientes.getModel();
        //Creamos un arreglo de tipo Object de 6 posiciones.
        Object[] obj = new Object[6];
        //Con un buque for utilizamos la variable listacliente de la List.
        for (int i = 0; i < listacliente.size(); i++) {
            //Usuamos la variable obj.
            obj[0] = listacliente.get(i).getId_cliente();
            obj[1] = listacliente.get(i).getCedula();
            obj[2] = listacliente.get(i).getNombre();
            obj[3] = listacliente.get(i).getTelefono();
            obj[4] = listacliente.get(i).getDireccion();
            obj[5] = listacliente.get(i).getRazon();
            //Pasamos agregarlo al modelo.
            modelo.addRow(obj);
        }
        //Agregamos el modelo a la Tabla Clientes.
        tblClientes.setModel(modelo);
    }

    //Creamos el mètodo que nos va a peermitir listar los proveedores que están almacenados en la base de datos.
    public void ListarProveedor() {
        List<Proveedor> listaprov = proDao.ListarProveedor();
        //Luego de llamar el método ListarProveedor de la clase ProveedorDAO procecdemos a almacenar los resultados en la variable modelo del DefaultTableModel.
        modelo = (DefaultTableModel) tblProveedor.getModel();
        //Creamos un arreglo de tipo Object de 6 posiciones.
        Object[] obj = new Object[6];
        //Con un buque for utilizamos la variable listacliente de la List.
        for (int i = 0; i < listaprov.size(); i++) {
            //Usuamos la variable obj.
            obj[0] = listaprov.get(i).getId_proveedor();
            obj[1] = listaprov.get(i).getNit();
            obj[2] = listaprov.get(i).getNombre();
            obj[3] = listaprov.get(i).getTelefono();
            obj[4] = listaprov.get(i).getDireccion();
            obj[5] = listaprov.get(i).getRazon();
            //Pasamos agregarlo al modelo.
            modelo.addRow(obj);
        }
        //Agregamos el modelo a la Tabla Clientes.
        tblProveedor.setModel(modelo);
    }

    //Creamos el mètodo que nos va a peermitir listar los productos que están almacenados en la base de datos.
    public void ListarProductos() {
        List<Productos> lisproducto = productoDao.ListarProductos();
        //Luego de llamar el método ListarProductos de la clase ProductosDAO procecdemos a almacenar los resultados en la variable modelo del DefaultTableModel.
        modelo = (DefaultTableModel) tblProductos.getModel();
        //Creamos un arreglo de tipo Object de 6 posiciones.
        Object[] obj = new Object[6];
        //Con un buque for utilizamos la variable listacliente de la List.
        for (int i = 0; i < lisproducto.size(); i++) {
            //Usuamos la variable obj.
            obj[0] = lisproducto.get(i).getId_producto();
            obj[1] = lisproducto.get(i).getCodigo();
            obj[2] = lisproducto.get(i).getDescripcion();
            obj[3] = lisproducto.get(i).getProveedor();
            obj[4] = lisproducto.get(i).getStock();
            obj[5] = lisproducto.get(i).getPrecio();
            //Pasamos agregarlo al modelo.
            modelo.addRow(obj);
        }
        //Agregamos el modelo a la Tabla Productos.
        tblProductos.setModel(modelo);
    }

    public void ListarConfig() {
        confi = productoDao.BuscarDatos();
        // A los campos de texto le concatenamos los datos del negocio almacenados en la base de datos sistemaventa.
        txtIdConfig.setText("" + confi.getId());
        txtNitEmpresa.setText("" + confi.getNit());
        txtNombreEmpresa.setText("" + confi.getNombre());
        txtTelefonoEmpresa.setText("" + confi.getTelefono());
        txtDireccionEmpresa.setText("" + confi.getDireccion());
        txtRazonSocialEmpresa.setText("" + confi.getRazon());
    }

    //Creamos el mètodo que nos va a peermitir listar los ventas que están almacenadas en la base de datos.
    public void ListarVentas() {
        List<Venta> listaventa = vdao.ListarVentas();
        //Luego de llamar el método ListarVentas de la clase VentaDAO procecdemos a almacenar los resultados en la variable modelo del DefaultTableModel.
        modelo = (DefaultTableModel) tblVentas.getModel();
        //Creamos un arreglo de tipo Object de 4 posiciones.
        Object[] obj = new Object[4];
        //Con un buque for utilizamos la variable listaventa de la List para recorrer la lista.
        for (int i = 0; i < listaventa.size(); i++) {
            //Usuamos la variable obj.
            obj[0] = listaventa.get(i).getId_venta();
            obj[1] = listaventa.get(i).getCliente();
            obj[2] = listaventa.get(i).getVendedor();
            obj[3] = listaventa.get(i).getTotal();
            //Pasamos agregarlo al modelo.
            modelo.addRow(obj);
        }
        //Agregamos el modelo a la Tabla tblVentas.
        tblVentas.setModel(modelo);
    }

    // Creamos método par limpiar los campos de texto de los datos dol produnto de la venta.
    private void LimpiarVenta() {
        txtCodProdVenta.setText("");
        txtDescripcionVenta.setText("");
        txtCantidadVenta.setText("");
        txtPrecioVenta.setText("");
        txtStockDisponible.setText("");
        txtIdVenta.setText("");
    }

    //Creamos método limpiar Table.
    public void LimpiarTable() {
        //Utilizamos un bucle for para recorrer la tabla.
        for (int i = 0; i < modelo.getRowCount(); i++) {
            //Removemos el modelo.
            modelo.removeRow(i);
            //Restamos el i para evitar así que cuando se de clip reperidamente en el botón Clientes los datos de de la tabla se repitan se repitan.
            i = i - 1;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnProveedor = new javax.swing.JButton();
        btnNuevaVenta = new javax.swing.JButton();
        btnClientes = new javax.swing.JButton();
        btnProducto = new javax.swing.JButton();
        btnVentas = new javax.swing.JButton();
        btnConfig = new javax.swing.JButton();
        btnRegistrarUsuarios = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnEliminarVenta = new javax.swing.JButton();
        txtDescripcionVenta = new javax.swing.JTextField();
        txtCantidadVenta = new javax.swing.JTextField();
        txtPrecioVenta = new javax.swing.JTextField();
        txtStockDisponible = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNuevaVenta = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtCedulaClienteVenta = new javax.swing.JTextField();
        txtNombresClienteVenta = new javax.swing.JTextField();
        btnGenerarVenta = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        txtTelefonoClienteVenta = new javax.swing.JTextField();
        txtDireccionClienteVenta = new javax.swing.JTextField();
        txtRazonClienteVenta = new javax.swing.JTextField();
        txtIdPro = new javax.swing.JTextField();
        txtCodProdVenta = new javax.swing.JTextField();
        lblVendedor = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtCedulaCliente = new javax.swing.JTextField();
        txtNombreCliente = new javax.swing.JTextField();
        txtTelefonoCliente = new javax.swing.JTextField();
        txtDireccionCliente = new javax.swing.JTextField();
        txtRazonSocialCliente = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        btnGuardarCliente = new javax.swing.JButton();
        btnActualizarCliente = new javax.swing.JButton();
        btnEliminarCliente = new javax.swing.JButton();
        btnNuevoCliente = new javax.swing.JButton();
        txtIdCliente = new javax.swing.JTextField();
        lblMensajeCliente = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtDocProveedor = new javax.swing.JTextField();
        txtNomProveedor = new javax.swing.JTextField();
        txtTelProveedor = new javax.swing.JTextField();
        txtDirProveedor = new javax.swing.JTextField();
        txtRazonSoProveedor = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblProveedor = new javax.swing.JTable();
        btnGuardarProveedor = new javax.swing.JButton();
        btnActualizarProveedor = new javax.swing.JButton();
        btnEliminarProveedor = new javax.swing.JButton();
        btnAgregarProveedor = new javax.swing.JButton();
        txtIdProveedor = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txtCodigoProducto = new javax.swing.JTextField();
        txtDescripcionProducto = new javax.swing.JTextField();
        txtStockProducto = new javax.swing.JTextField();
        cbxProveedor = new javax.swing.JComboBox<>();
        btnGuardarProducto = new javax.swing.JButton();
        btnActualizarProducto = new javax.swing.JButton();
        btnEliminarProducto = new javax.swing.JButton();
        btnNuevoProducto = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblProductos = new javax.swing.JTable();
        txtPrecioPeroducto = new javax.swing.JTextField();
        btnReporteProductoExcel = new javax.swing.JButton();
        txtIdProducto = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblVentas = new javax.swing.JTable();
        btnReporteVentasPdf = new javax.swing.JButton();
        txtIdVenta = new javax.swing.JTextField();
        btnGraficos = new javax.swing.JButton();
        miFecha = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        txtNitEmpresa = new javax.swing.JTextField();
        txtNombreEmpresa = new javax.swing.JTextField();
        txtTelefonoEmpresa = new javax.swing.JTextField();
        txtDireccionEmpresa = new javax.swing.JTextField();
        txtRazonSocialEmpresa = new javax.swing.JTextField();
        btnModificarDatosEmpresa = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        txtIdConfig = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(0, 153, 102));

        btnProveedor.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/proveedor.png"))); // NOI18N
        btnProveedor.setText("Proveedor");
        btnProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProveedorActionPerformed(evt);
            }
        });

        btnNuevaVenta.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnNuevaVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Nventa.png"))); // NOI18N
        btnNuevaVenta.setText("Nueva Venta");
        btnNuevaVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevaVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevaVentaActionPerformed(evt);
            }
        });

        btnClientes.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Clientes.png"))); // NOI18N
        btnClientes.setText("Clientes");
        btnClientes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientesActionPerformed(evt);
            }
        });

        btnProducto.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/producto.png"))); // NOI18N
        btnProducto.setText("Productos");
        btnProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductoActionPerformed(evt);
            }
        });

        btnVentas.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/compras.png"))); // NOI18N
        btnVentas.setText("Ventas");
        btnVentas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasActionPerformed(evt);
            }
        });

        btnConfig.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/config.png"))); // NOI18N
        btnConfig.setText("Config");
        btnConfig.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigActionPerformed(evt);
            }
        });

        btnRegistrarUsuarios.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        btnRegistrarUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Clientes.png"))); // NOI18N
        btnRegistrarUsuarios.setText("Usuarios");
        btnRegistrarUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarUsuariosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRegistrarUsuarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnClientes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNuevaVenta, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .addComponent(btnProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnProducto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnVentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnConfig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(214, Short.MAX_VALUE)
                .addComponent(btnNuevaVenta)
                .addGap(18, 18, 18)
                .addComponent(btnClientes)
                .addGap(18, 18, 18)
                .addComponent(btnProducto)
                .addGap(18, 18, 18)
                .addComponent(btnVentas)
                .addGap(18, 18, 18)
                .addComponent(btnProveedor)
                .addGap(18, 18, 18)
                .addComponent(btnRegistrarUsuarios)
                .addGap(18, 18, 18)
                .addComponent(btnConfig)
                .addGap(45, 45, 45))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 210, 610));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/encabezado.png"))); // NOI18N
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 0, 920, 150));

        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel2.setText("Código");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel3.setText("Descripción");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel4.setText("Cantidad");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel5.setText("Precio");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel6.setText("Stock");

        btnEliminarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        btnEliminarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarVentaActionPerformed(evt);
            }
        });

        txtDescripcionVenta.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        txtCantidadVenta.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtCantidadVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCantidadVentaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCantidadVentaKeyTyped(evt);
            }
        });

        txtPrecioVenta.setEditable(false);
        txtPrecioVenta.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        txtStockDisponible.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        tblNuevaVenta.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblNuevaVenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CODIGO", "DESCRICIÓN", "CANTIDAD", "PRECIO", "TOTAL"
            }
        ));
        jScrollPane1.setViewportView(tblNuevaVenta);
        if (tblNuevaVenta.getColumnModel().getColumnCount() > 0) {
            tblNuevaVenta.getColumnModel().getColumn(0).setPreferredWidth(30);
            tblNuevaVenta.getColumnModel().getColumn(1).setPreferredWidth(135);
            tblNuevaVenta.getColumnModel().getColumn(2).setPreferredWidth(25);
            tblNuevaVenta.getColumnModel().getColumn(3).setPreferredWidth(30);
            tblNuevaVenta.getColumnModel().getColumn(4).setPreferredWidth(30);
        }

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("CEDULA");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("NOMBRES");

        txtCedulaClienteVenta.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtCedulaClienteVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCedulaClienteVentaKeyPressed(evt);
            }
        });

        txtNombresClienteVenta.setEditable(false);
        txtNombresClienteVenta.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        btnGenerarVenta.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        btnGenerarVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/print.png"))); // NOI18N
        btnGenerarVenta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGenerarVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarVentaActionPerformed(evt);
            }
        });

        jLabel9.setBackground(new java.awt.Color(0, 0, 0));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/money.png"))); // NOI18N
        jLabel9.setText("Total a pagar");

        lblTotal.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N

        txtTelefonoClienteVenta.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtTelefonoClienteVenta.setText(" ");

        txtDireccionClienteVenta.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtDireccionClienteVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionClienteVentaActionPerformed(evt);
            }
        });

        txtRazonClienteVenta.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        txtCodProdVenta.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtCodProdVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodProdVentaActionPerformed(evt);
            }
        });
        txtCodProdVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCodProdVentaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodProdVentaKeyReleased(evt);
            }
        });

        lblVendedor.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lblVendedor.setText("Pruebas Java");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(txtCedulaClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtNombresClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnGenerarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(txtTelefonoClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtDireccionClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtRazonClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 105, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txtCodProdVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txtDescripcionVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtStockDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEliminarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdPro, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(62, 150, Short.MAX_VALUE))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(200, 200, 200)
                .addComponent(lblVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtIdPro, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCodProdVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(jLabel6)
                                .addComponent(jLabel4))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnEliminarVenta))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtDescripcionVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtStockDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(txtCantidadVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGenerarVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtCedulaClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNombresClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTelefonoClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtDireccionClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtRazonClienteVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addComponent(lblVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("", jPanel2);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel11.setText("Documento:");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel12.setText("Nombres:");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel13.setText("Teléfono:");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel14.setText("Dirección:");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel15.setText("Razón Social:");

        txtCedulaCliente.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        txtNombreCliente.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtNombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNombreClienteKeyTyped(evt);
            }
        });

        txtTelefonoCliente.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtTelefonoCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelefonoClienteKeyTyped(evt);
            }
        });

        txtDireccionCliente.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        txtRazonSocialCliente.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtRazonSocialCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRazonSocialClienteActionPerformed(evt);
            }
        });
        txtRazonSocialCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRazonSocialClienteKeyTyped(evt);
            }
        });

        tblClientes.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "DOCUMENTO", "NOMBRES", "TELÉFONO", "DIRECCIÓN", "RAZON SOCIAL"
            }
        ));
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblClientes);
        if (tblClientes.getColumnModel().getColumnCount() > 0) {
            tblClientes.getColumnModel().getColumn(0).setPreferredWidth(10);
            tblClientes.getColumnModel().getColumn(1).setPreferredWidth(65);
            tblClientes.getColumnModel().getColumn(2).setPreferredWidth(120);
            tblClientes.getColumnModel().getColumn(3).setPreferredWidth(70);
            tblClientes.getColumnModel().getColumn(4).setPreferredWidth(150);
            tblClientes.getColumnModel().getColumn(5).setPreferredWidth(50);
        }

        btnGuardarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo.png"))); // NOI18N
        btnGuardarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarClienteActionPerformed(evt);
            }
        });

        btnActualizarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        btnActualizarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarClienteActionPerformed(evt);
            }
        });

        btnEliminarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        btnEliminarCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarClienteActionPerformed(evt);
            }
        });

        btnNuevoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/nuevo.png"))); // NOI18N
        btnNuevoCliente.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoClienteActionPerformed(evt);
            }
        });

        txtIdCliente.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        lblMensajeCliente.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        lblMensajeCliente.setForeground(new java.awt.Color(51, 255, 0));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(lblMensajeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnGuardarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnEliminarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnActualizarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnNuevoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26, 26, 26))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(136, 136, 136)
                                .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(44, 44, 44))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel15))
                                .addGap(6, 6, 6)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtDireccionCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                                    .addComponent(txtRazonSocialCliente)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtCedulaCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                                    .addComponent(txtNombreCliente)
                                    .addComponent(txtTelefonoCliente))))))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel11)
                                .addComponent(txtCedulaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtTelefonoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtDireccionCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtRazonSocialCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardarCliente)
                            .addComponent(btnActualizarCliente))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEliminarCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnNuevoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblMensajeCliente)
                .addContainerGap(174, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("", jPanel3);

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel16.setText("Documento");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel17.setText("Nombre");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel18.setText("Teléfono");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel19.setText("Dirección");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel20.setText("Razón Social");

        txtDocProveedor.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        txtNomProveedor.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtNomProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNomProveedorKeyTyped(evt);
            }
        });

        txtTelProveedor.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtTelProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtTelProveedorKeyTyped(evt);
            }
        });

        txtDirProveedor.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        txtRazonSoProveedor.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtRazonSoProveedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtRazonSoProveedorKeyTyped(evt);
            }
        });

        tblProveedor.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        tblProveedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "DOCUMENTO", "NOMBRE", "TELÉFONO", "DIRECCIÓN", "RAZON SOCIAL"
            }
        ));
        tblProveedor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProveedorMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblProveedor);
        if (tblProveedor.getColumnModel().getColumnCount() > 0) {
            tblProveedor.getColumnModel().getColumn(0).setPreferredWidth(7);
            tblProveedor.getColumnModel().getColumn(1).setPreferredWidth(35);
            tblProveedor.getColumnModel().getColumn(2).setPreferredWidth(80);
            tblProveedor.getColumnModel().getColumn(3).setPreferredWidth(25);
            tblProveedor.getColumnModel().getColumn(4).setPreferredWidth(80);
            tblProveedor.getColumnModel().getColumn(5).setPreferredWidth(70);
        }

        btnGuardarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo.png"))); // NOI18N
        btnGuardarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProveedorActionPerformed(evt);
            }
        });

        btnActualizarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        btnActualizarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarProveedorActionPerformed(evt);
            }
        });

        btnEliminarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        btnEliminarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProveedorActionPerformed(evt);
            }
        });

        btnAgregarProveedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/nuevo.png"))); // NOI18N
        btnAgregarProveedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAgregarProveedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProveedorActionPerformed(evt);
            }
        });

        txtIdProveedor.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnGuardarProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEliminarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnActualizarProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAgregarProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel20)
                            .addComponent(jLabel19)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDocProveedor, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addComponent(txtNomProveedor)
                            .addComponent(txtTelProveedor)
                            .addComponent(txtDirProveedor)
                            .addComponent(txtRazonSoProveedor))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(118, 118, 118)
                .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(txtDocProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(txtNomProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTelProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(txtDirProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtRazonSoProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnActualizarProveedor)
                            .addComponent(btnGuardarProveedor))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnEliminarProveedor)
                            .addComponent(btnAgregarProveedor))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIdProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(173, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("", jPanel4);

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel21.setText("Código");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel22.setText("Descripción");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel23.setText("Stock");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel24.setText("Precio");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel25.setText("Proveedor");

        txtCodigoProducto.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        txtDescripcionProducto.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        txtStockProducto.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtStockProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStockProductoKeyTyped(evt);
            }
        });

        cbxProveedor.setEditable(true);
        cbxProveedor.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        btnGuardarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/GuardarTodo.png"))); // NOI18N
        btnGuardarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGuardarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarProductoActionPerformed(evt);
            }
        });

        btnActualizarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        btnActualizarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnActualizarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarProductoActionPerformed(evt);
            }
        });

        btnEliminarProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/eliminar.png"))); // NOI18N
        btnEliminarProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEliminarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarProductoActionPerformed(evt);
            }
        });

        btnNuevoProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/nuevo.png"))); // NOI18N
        btnNuevoProducto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnNuevoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoProductoActionPerformed(evt);
            }
        });

        tblProductos.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        tblProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CODIGO", "DESCRIPCIÓN", "PROVEEDOR", "CANTIDAD", "PRECIO"
            }
        ));
        tblProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblProductos);
        if (tblProductos.getColumnModel().getColumnCount() > 0) {
            tblProductos.getColumnModel().getColumn(0).setPreferredWidth(10);
            tblProductos.getColumnModel().getColumn(1).setPreferredWidth(30);
            tblProductos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblProductos.getColumnModel().getColumn(3).setPreferredWidth(60);
            tblProductos.getColumnModel().getColumn(4).setPreferredWidth(15);
            tblProductos.getColumnModel().getColumn(5).setPreferredWidth(20);
        }

        txtPrecioPeroducto.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        txtPrecioPeroducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrecioPeroductoKeyTyped(evt);
            }
        });

        btnReporteProductoExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/excel.png"))); // NOI18N
        btnReporteProductoExcel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReporteProductoExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteProductoExcelActionPerformed(evt);
            }
        });

        txtIdProducto.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addGap(36, 36, 36)
                                .addComponent(txtCodigoProducto))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addGap(24, 24, 24)
                                .addComponent(txtStockProducto)
                                .addGap(18, 18, 18)
                                .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addGap(33, 33, 33)
                                .addComponent(txtPrecioPeroducto))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jLabel25)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnGuardarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnActualizarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnNuevoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(71, 71, 71)
                                .addComponent(btnReporteProductoExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(txtCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(txtDescripcionProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(cbxProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(txtStockProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(txtPrecioPeroducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardarProducto)
                            .addComponent(btnActualizarProducto))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEliminarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnNuevoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReporteProductoExcel)))
                .addGap(53, 53, 53))
        );

        jTabbedPane1.addTab("", jPanel5);

        tblVentas.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        tblVentas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "CLIENTE", "VENDEDDOR", "TOTAL"
            }
        ));
        tblVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblVentasMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblVentas);
        if (tblVentas.getColumnModel().getColumnCount() > 0) {
            tblVentas.getColumnModel().getColumn(0).setPreferredWidth(15);
            tblVentas.getColumnModel().getColumn(1).setPreferredWidth(60);
            tblVentas.getColumnModel().getColumn(2).setPreferredWidth(60);
            tblVentas.getColumnModel().getColumn(3).setPreferredWidth(40);
        }

        btnReporteVentasPdf.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/pdf.png"))); // NOI18N
        btnReporteVentasPdf.setText(" ");
        btnReporteVentasPdf.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReporteVentasPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteVentasPdfActionPerformed(evt);
            }
        });

        btnGraficos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/torta.png"))); // NOI18N
        btnGraficos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGraficosActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel10.setText("Selecionar");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 908, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(btnReporteVentasPdf)
                        .addGap(18, 18, 18)
                        .addComponent(txtIdVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnGraficos)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(miFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnReporteVentasPdf)
                        .addComponent(txtIdVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnGraficos))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(miFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(116, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("", jPanel6);

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel26.setText("NIT");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel27.setText("NOMBRE EMPRESA");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel28.setText("TELÉFONO");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel29.setText("DIRECCIÓN");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        jLabel30.setText("RAZON SOCIAL");

        txtNitEmpresa.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        txtNombreEmpresa.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        txtTelefonoEmpresa.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        txtDireccionEmpresa.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        txtRazonSocialEmpresa.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N

        btnModificarDatosEmpresa.setBackground(new java.awt.Color(0, 204, 255));
        btnModificarDatosEmpresa.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        btnModificarDatosEmpresa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Img/Actualizar (2).png"))); // NOI18N
        btnModificarDatosEmpresa.setText("Actualizar");
        btnModificarDatosEmpresa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnModificarDatosEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarDatosEmpresaActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel31.setText("DATOS DE LA EMPRESA");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addGap(162, 162, 162))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(txtNitEmpresa)
                                .addGap(43, 43, 43)))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel27)
                            .addComponent(txtNombreEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(69, 69, 69)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addComponent(txtTelefonoEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29)
                            .addComponent(txtDireccionEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(66, 66, 66)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel30)
                            .addComponent(txtRazonSocialEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE))))
                .addGap(72, 72, 72))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(230, 230, 230)
                        .addComponent(jLabel31))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(txtIdConfig, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(130, 130, 130)
                        .addComponent(btnModificarDatosEmpresa)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel31)
                .addGap(28, 28, 28)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNitEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTelefonoEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDireccionEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRazonSocialEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnModificarDatosEmpresa)
                    .addComponent(txtIdConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(200, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("", jPanel7);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 120, 920, 490));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProveedorActionPerformed
        //Llamamos el método limpiar tabla.
        LimpiarTable();
// Hacemos llamado al método ListarProveedor() de la clase ProveedorDAO
        ListarProveedor();
        jTabbedPane1.setSelectedIndex(2);
        //jTabbedPane1.setSelectedIndex(2); hace referencia a que cuando se presione el botón Proveedores de la vista Sistema se enlisten los proveedores en la tabla tblProveedor.
    }//GEN-LAST:event_btnProveedorActionPerformed

    private void btnEliminarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarClienteActionPerformed
        // Validamos si es diferente de vacío el campo txtIdCliente.
        if (!"".equals(txtIdCliente.getText())) {
            //Procecemos a eliminar, creamos unn variable de tipo int.
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro  que quiere eliminar definitivamente el cliente selecionado?. Esta acción no se puede revertir");
            if (pregunta == 0) {
                //Quiere decir que acepta eliminar el cliente de la  base de datos. Llamamos el método EliminarCliente della clase ClienteDAO.
                int id_cliente = Integer.parseInt(txtIdCliente.getText());
                cliDao.EliminarCliente(id_cliente);
                //Después de eliminar vamos a limpiar la tabla y a listarCliente.
                LimpiarTable();
                ListarCliente();

            }

        }
    }//GEN-LAST:event_btnEliminarClienteActionPerformed

    private void btnActualizarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarClienteActionPerformed
        // Verificamos con un if que el campo txtIdCliente no esté vacío.
        if ("".equals(txtIdCliente.getText())) {
            JOptionPane.showMessageDialog(null, "Debe selecionar una fila");
        } else {
            //Con un if verificamos si alguno de los campos están vacíos.
            if (!"".equals(txtCedulaCliente.getText()) || !"".equals(txtNombreCliente.getText()) || !"".equals(txtTelefonoCliente.getText()) || !"".equals(txtDireccionCliente.getText()) || !"".equals(txtRazonSocialCliente.getText())) {
                //Después de selecionar una fila obtenemos todos los datos del cliente en los Text Field.
                cl.setCedula(Integer.parseInt(txtCedulaCliente.getText()));
                cl.setNombre(txtNombreCliente.getText());
                cl.setTelefono(Integer.parseInt(txtTelefonoCliente.getText()));
                cl.setDireccion(txtDireccionCliente.getText());
                cl.setRazon(txtRazonSocialCliente.getText());
                cl.setId_cliente(Integer.parseInt(txtIdCliente.getText()));
                cliDao.ModificarCliente(cl);
                //Después de actualizar se va a limpiar la tabla.
                LimpiarTable();
                //Limpiar los campos de texto.
                Limpiar();
                //Listar Cliente.
                ListarCliente();
            } else {
                //Mostramos un mensaje si es que hay un campo vacío.
                JOptionPane.showMessageDialog(null, "Tiene campos vacíos, verifica por favor");
            }
        }
    }//GEN-LAST:event_btnActualizarClienteActionPerformed

    private void btnNuevoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoClienteActionPerformed
        //Llamamos el método limpiar cajas de texto.
        Limpiar();
    }//GEN-LAST:event_btnNuevoClienteActionPerformed

    private void btnAgregarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProveedorActionPerformed
        // Llamamos nuestro método LimpiarProveedor(), la momento de presionar el botón nuevo se limpiarán las cajas de texto.
        LimpiarProveedor();
    }//GEN-LAST:event_btnAgregarProveedorActionPerformed

    private void txtRazonSocialClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRazonSocialClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRazonSocialClienteActionPerformed

    private void btnActualizarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarProductoActionPerformed
        // Verificamos con un if que el campo txtIdProducto no esté vacío.
        if ("".equals(txtIdProducto.getText())) {
            JOptionPane.showMessageDialog(null, "Debe selecionar una fila");
        } else {
            //Con un if verificamos si los campos están vacíos.
            if (!"".equals(txtCodigoProducto.getText()) || !"".equals(txtDescripcionProducto.getText()) || !"".equals(cbxProveedor.getSelectedItem()) || !"".equals(txtStockProducto.getText()) || !"".equals(txtPrecioPeroducto.getText())) {
                //Después de selecionar una fila obtenemos todos los datos del producto en los Text Field.
                producto.setCodigo(txtCodigoProducto.getText());
                producto.setDescripcion(txtDescripcionProducto.getText());
                producto.setProveedor(cbxProveedor.getSelectedItem().toString());
                producto.setStock(Integer.parseInt(txtStockProducto.getText()));
                producto.setPrecio(Double.parseDouble(txtPrecioPeroducto.getText()));
                producto.setId_producto(Integer.parseInt(txtIdProducto.getText()));
                productoDao.ModificarProducto(producto);
                //Después de actualizar se va a limpiar la tabla.
                LimpiarTable();
                //Limpiar los campos de texto.
                LimpiarProducto();
                //Listar Cliente.
                ListarProductos();
            } else {
                //Mostramos un mensaje si es que hay un campo vacío.
                JOptionPane.showMessageDialog(null, "Tiene campos vacíos, verifica por favor");
            }
        }
    }//GEN-LAST:event_btnActualizarProductoActionPerformed

    private void btnEliminarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProductoActionPerformed
        // TODO add your handling code here:
        // Validamos si es diferente de vacío el campo txtIdProducto.
        if (!"".equals(txtIdProducto.getText())) {
            //Procecemos a eliminar, creamos unn variable de tipo int.
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro  que quiere eliminar definitivamente el Producto selecionado?. Esta acción no se puede revertir");
            if (pregunta == 0) {
                //Quiere decir que acepta eliminar el cproducto de la  base de datos. Llamamos el método EliminarProducto della clase ProductosDAO.
                int id_producto = Integer.parseInt(txtIdProducto.getText());
                productoDao.EliminarProducto(id_producto);
                //Después de eliminar vamos a limpiar la tabla y a ListarProductos.
                //Llamamos el método LimpiarTable
                LimpiarTable();
                //Llamamos el método ListarProductos
                ListarProductos();
                //Llamamos el método LimpiarProducto, limpiamos las cajas de texto.
                LimpiarProducto();

            }

        }
    }//GEN-LAST:event_btnEliminarProductoActionPerformed

    private void btnNuevoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoProductoActionPerformed
        // TODO add your handling code here:
        //Llamamos el método LimpiarProducto, limpiamos las cajas de texto.
        LimpiarProducto();
        txtCodigoProducto.requestFocus();
    }//GEN-LAST:event_btnNuevoProductoActionPerformed

    private void txtDireccionClienteVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionClienteVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionClienteVentaActionPerformed

    private void btnGuardarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarClienteActionPerformed
        // Verificamos que los campos de texto del formulario clientes no se encuentren vacíos, para ello utolizamos un if.
        if (!"".equals(txtCedulaCliente.getText()) || !"".equals(txtNombreCliente.getText()) || !"".equals(txtTelefonoCliente.getText()) || !"".equals(txtDireccionCliente.getText())) {
            //Si todos los campos de texto son diferentes de vacío entonces capturamos todos los datos para almacenarlos en la base de datos sistemaaventa en la tabla clientes.
            cl.setCedula(Integer.parseInt(txtCedulaCliente.getText()));
            cl.setNombre(txtNombreCliente.getText());
            cl.setTelefono(Integer.parseInt(txtTelefonoCliente.getText()));
            cl.setDireccion(txtDireccionCliente.getText());
            cl.setRazon(txtRazonSocialCliente.getText());
            //Usuamos la variable cliDao para acceder al método RegistrarCliente y le pasamos como parámetro la variable cl del modelo Cliente
            cliDao.RegistrarCliente(cl);
            //Mandamos un mensaje en un label
            lblMensajeCliente.setText("Cliente guardado!");
            //Limpiamos las cajas de texto con el método limpiar despues de guardar el nuevo cliente.
            LimpiarTable();
            //Después de eliminar vamos a limpiar la tabla y a listarCliente.
            ListarCliente();
            Limpiar();
        } else {
            //En caso de que no se guarde el Cliente mandamos un mensaje a travéz de un JOptionPane.showMessageDialog.
            JOptionPane.showMessageDialog(null, "Asegúrece de llenar todos los campos de texto del formulario");
            Limpiar();
        }
    }//GEN-LAST:event_btnGuardarClienteActionPerformed

    private void btnClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientesActionPerformed
        // Llamamos el método ListarCliente() que nos permite visiualizar los clientes almacenados en la base de datos en nuestra tabla de java.
        LimpiarTable();
        //LimpiarTable();método que nos evita que al cliquear reperidamente en el botón Clientes en la vista Sistema los datos de la tabla clientes se repitan infinitamente.
        ListarCliente();
        //ListarCliente(); ejecuta el método que muestra en un dataTable todos los clientes registrados en labase de datos sistemavemta.
        jTabbedPane1.setSelectedIndex(1);
        //jTabbedPane1.setSelectedIndex(1); hace referencia a que cuando se presione el botón Clientes de la vista Sistema se enlisten los clientes en la tabla tblClientes.
    }//GEN-LAST:event_btnClientesActionPerformed

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        // Capturamos el id de la fila selecionada para realizar distintas operaciones con los datos del cliente. Ejemple, eliminar, actualizar, etc.
        int fila = tblClientes.rowAtPoint(evt.getPoint());
        //Mostraremos los datos del cliente en el Text Field correspondiente.
        txtIdCliente.setText(tblClientes.getValueAt(fila, 0).toString());
        txtCedulaCliente.setText(tblClientes.getValueAt(fila, 1).toString());
        txtNombreCliente.setText(tblClientes.getValueAt(fila, 2).toString());
        txtTelefonoCliente.setText(tblClientes.getValueAt(fila, 3).toString());
        txtDireccionCliente.setText(tblClientes.getValueAt(fila, 4).toString());
        txtRazonSocialCliente.setText(tblClientes.getValueAt(fila, 5).toString());
    }//GEN-LAST:event_tblClientesMouseClicked

    private void btnGuardarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProveedorActionPerformed
        // Verificamos que los campos de texto del formulario proveedor no se encuentren vacíos, para ello utolizamos un if.
        if (!"".equals(txtDocProveedor.getText()) || !"".equals(txtNomProveedor.getText()) || !"".equals(txtTelProveedor.getText()) || !"".equals(txtDirProveedor.getText())) {
            //Si todos los campos de texto son diferentes de vacío entonces capturamos todos los datos para almacenarlos en la base de datos sistemaaventa en la tabla clientes.
            pro.setNit(txtDocProveedor.getText());
            pro.setNombre(txtNomProveedor.getText());
            pro.setTelefono(txtTelProveedor.getText());
            pro.setDireccion(txtDirProveedor.getText());
            pro.setRazon(txtRazonSoProveedor.getText());
            //Usamos la variable proDao para acceder al método RegistrarProveedor y le pasamos como parámetro la variable pro del modelo Proveedor
            proDao.RegistrarProveedor(pro);
            //Mandamos un mensaje en un JOptionPane.showMessageDialog.
            JOptionPane.showMessageDialog(null, "Proveedor guardado!");
            //Limpiamos las cajas de texto con el método lLimpiarProveedor() despues de guardar el nuevo proveedor.
            LimpiarProveedor();
            //Después de eliminar vamos a limpiar la tabla y a listarProveedor.
            LimpiarTable();
            ListarProveedor();
        } else {
            //En caso de que no se guarde el Proveedor mandamos un mensaje a travéz de un JOptionPane.showMessageDialog.
            JOptionPane.showMessageDialog(null, "Asegúrece de llenar todos los campos de texto del formulario");
            Limpiar();
        }
    }//GEN-LAST:event_btnGuardarProveedorActionPerformed

    private void btnEliminarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarProveedorActionPerformed
        // TODO add your handling code here:
        // Validamos si es diferente de vacío el campo txtIdProveedor.
        if (!"".equals(txtIdProveedor.getText())) {
            //Procecemos a eliminar, creamos unn variable de tipo int.
            int pregunta = JOptionPane.showConfirmDialog(null, "¿Está seguro  que quiere eliminar definitivamente el proveedor selecionado?. Esta acción no se puede revertir");
            if (pregunta == 0) {
                //Quiere decir que acepta eliminar el cliente de la  base de datos. Llamamos el método EliminarProveedor della clase ProveedorDAO.
                int id_proveedor = Integer.parseInt(txtIdProveedor.getText());
                proDao.EliminarProveedor(id_proveedor);
                //Después de eliminar vamos a limpiar la tabla y a ListarProveedor.
                LimpiarTable();
                ListarProveedor();

            }

        }
    }//GEN-LAST:event_btnEliminarProveedorActionPerformed

    private void tblProveedorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProveedorMouseClicked
        // TODO add your handling code here:
        // Capturamos el id de la fila selecionada para realizar distintas operaciones con los datos del proveedor. Ejemple, eliminar, actualizar, etc.
        int fila = tblProveedor.rowAtPoint(evt.getPoint());
        //Mostraremos los datos del proveedor en el Text Field correspondiente.
        txtIdProveedor.setText(tblProveedor.getValueAt(fila, 0).toString());
        txtDocProveedor.setText(tblProveedor.getValueAt(fila, 1).toString());
        txtNomProveedor.setText(tblProveedor.getValueAt(fila, 2).toString());
        txtTelProveedor.setText(tblProveedor.getValueAt(fila, 3).toString());
        txtDirProveedor.setText(tblProveedor.getValueAt(fila, 4).toString());
        txtRazonSoProveedor.setText(tblProveedor.getValueAt(fila, 5).toString());
    }//GEN-LAST:event_tblProveedorMouseClicked

    private void btnActualizarProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarProveedorActionPerformed
        // TODO add your handling code here:
        // Verificamos con un if que el campo txtIdProveedor no esté vacío.
        if ("".equals(txtIdProveedor.getText())) {
            JOptionPane.showMessageDialog(null, "Debe selecionar una fila");
        } else {
            //Con un if verificamos si alguno de los campos están vacíos.
            if (!"".equals(txtDocProveedor.getText()) || !"".equals(txtNomProveedor.getText()) || !"".equals(txtTelProveedor.getText()) || !"".equals(txtDirProveedor.getText()) || !"".equals(txtRazonSoProveedor.getText())) {
                //Después de selecionar una fila obtenemos todos los datos del cliente en los Text Field.
                pro.setNit(txtDocProveedor.getText());
                pro.setNombre(txtNomProveedor.getText());
                pro.setTelefono(txtTelProveedor.getText());
                pro.setDireccion(txtDirProveedor.getText());
                pro.setRazon(txtRazonSoProveedor.getText());
                pro.setId_proveedor(Integer.parseInt(txtIdProveedor.getText()));
                proDao.ModificarProveedor(pro);
                //Después de actualizar se va a limpiar la tabla.
                LimpiarTable();
                //Limpiar los campos de texto.
                LimpiarProveedor();
                //Listar Cliente.
                ListarProveedor();
            } else {
                //Mostramos un mensaje si es que hay un campo vacío.
                JOptionPane.showMessageDialog(null, "Tiene campos vacíos, verifica por favor");
            }
        }
    }//GEN-LAST:event_btnActualizarProveedorActionPerformed

    private void btnGuardarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarProductoActionPerformed
        // TODO add your handling code here:
        // Verificamos que los campos de texto del formulario productos no se encuentren vacíos, para ello utilizamos un if.
        if (!"".equals(txtCodigoProducto.getText()) || !"".equals(txtDescripcionProducto.getText()) || !"".equals(cbxProveedor.getSelectedItem()) || !"".equals(txtStockProducto.getText()) || !"".equals(txtPrecioPeroducto.getText())) {
            //Si todos los campos de texto son diferentes de vacío entonces capturamos todos los datos para almacenarlos en la base de datos sistemaaventa en la tabla productos.
            producto.setCodigo(txtCodigoProducto.getText());
            producto.setDescripcion(txtDescripcionProducto.getText());
            producto.setProveedor((String) cbxProveedor.getSelectedItem().toString());
            producto.setStock(Integer.parseInt(txtStockProducto.getText()));
            producto.setPrecio(Double.parseDouble(txtPrecioPeroducto.getText()));
            //Usamos la variable productoDao para acceder al método RegistrarProducto y le pasamos como parámetro la variable producto del modelo Productos.

            productoDao.RegistrarProducto(producto);
            //Mandamos un mensaje en un JOptionPane.showMessageDialog.
            JOptionPane.showMessageDialog(null, "Produto guardado!");
            //Limpiamos las cajas de texto con el método LimpiarProducto() despues de guardar el nuevo proveedor.
            LimpiarProducto();
            //Después de eliminar vamos a limpiar la tabla y a listarProveedor.
            LimpiarTable();
            ListarProductos();
        } else {
            //En caso de que no se guarde el Proveedor mandamos un mensaje a travéz de un JOptionPane.showMessageDialog.
            JOptionPane.showMessageDialog(null, "Asegúrece de llenar todos los campos de texto del formulario");
            Limpiar();
        }
    }//GEN-LAST:event_btnGuardarProductoActionPerformed

    private void tblProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosMouseClicked
        // Capturamos el id de la fila selecionada para realizar distintas operaciones con los datos del producto. Ejemple, eliminar, actualizar, etc.
        int fila = tblProductos.rowAtPoint(evt.getPoint());
        //Mostraremos los datos del producto en el Text Field correspondiente.
        txtIdProducto.setText(tblProductos.getValueAt(fila, 0).toString());
        txtCodigoProducto.setText(tblProductos.getValueAt(fila, 1).toString());
        txtDescripcionProducto.setText(tblProductos.getValueAt(fila, 2).toString());
        cbxProveedor.setSelectedItem(tblProductos.getValueAt(fila, 3).toString());
        txtStockProducto.setText(tblProductos.getValueAt(fila, 4).toString());
        txtPrecioPeroducto.setText(tblProductos.getValueAt(fila, 5).toString());
    }//GEN-LAST:event_tblProductosMouseClicked

    private void btnProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductoActionPerformed
        // TODO add your handling code here:
        //jTabbedPane1.setSelectedIndex(3); hace referencia a que cuando se presione el botón Productos de la vista Sistema se enlisten los productos en la tabla tblProductos.
        jTabbedPane1.setSelectedIndex(3);
        //Llamamos el método LimpiarTable().
        LimpiarTable();
        //Llamamos el método ListarProductos().
        ListarProductos();
    }//GEN-LAST:event_btnProductoActionPerformed

    private void btnReporteProductoExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteProductoExcelActionPerformed
        // Llamamos el método reporte Excel.
        Excel.reporte();
    }//GEN-LAST:event_btnReporteProductoExcelActionPerformed

    private void txtCodProdVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodProdVentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodProdVentaActionPerformed

    private void txtCodProdVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodProdVentaKeyPressed
        // Verificamos que el usuario haya presioionado la tecla enter.
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // Se verifica que el campo txtCodProdVenta no esté vacío.
            if (!"".equals(txtCodProdVenta.getText())) {
                // Creamos una variable String codigo como parámetro para enviarle al método BuscarProducto de la clase ProductosDAO.
                String codigo = txtCodProdVenta.getText();
                // Ejecutamos el método.
                producto = productoDao.BuscarProducto(codigo);
                // Verificamos si existe el producto, si el campo de texto txtDescripcionVenta es diferente de vacío.
                if (producto.getDescripcion() != null) {
                    // Mostramos los datos del producto.
                    txtDescripcionVenta.setText("" + producto.getDescripcion());
                    txtStockDisponible.setText("" + producto.getStock());
                    txtPrecioVenta.setText("" + producto.getPrecio());
                    // Pasamos el cursor al txtCantidadVenta, de ahí el usuario podrá disponer de la cantidad de produtos que solicite el cliente.
                    txtCantidadVenta.requestFocus();
                } else {
                    // De lo contrario el focus se mantendrá en le txtCodigoVenta. Las demás cajas de texto quedarán vacías si no hay producto
                    LimpiarVenta();
                    txtCodProdVenta.requestFocus();
                }
            } else {
                // Si se presiona la tecla enter sin selecionar un producto se mostrará un mensaje.
                JOptionPane.showMessageDialog(null, "Ingrese el código del Producto");
                // Eñ fpcis se ,amtemdrá em el txtCodigoVenta.requestFocus();
                txtCodProdVenta.requestFocus();
            }
        }
    }//GEN-LAST:event_txtCodProdVentaKeyPressed

    private void txtCodProdVentaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodProdVentaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodProdVentaKeyReleased

    private void txtCantidadVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadVentaKeyPressed
        // Hacemos la validación para que la cantidad solicitada por el cliente a la hora de relizar una venta no sea mayor al stock.
// Verificamos con un if que la tecla ENTER ha sido presionada.

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // Validamos que el campo de texto cantidad no esté vacío.
            if (!"".equals(txtCantidadVenta.getText())) {
                // Creamos variables para almacenar los datos.
                String codigo = txtCodProdVenta.getText();
                String descripcion = txtDescripcionVenta.getText();
                int cantidad = Integer.parseInt(txtCantidadVenta.getText());
                double precio = Double.parseDouble(txtPrecioVenta.getText());
                double total = cantidad * precio;
                int stock = Integer.parseInt(txtStockDisponible.getText());
                // Antes de utilizar el item verificamos que la cantidad no supere el stock disponible.
                if (stock >= cantidad) {
                    //Usamos la varible item y la incrementamos + 1.
                    item = item + 1;
                    // Creamos otro model de DefaultTableModel para que al momento de cambiar de vista entre Nueva Venta y Productos los datos de la tabla tblNuevaVenta no se borren, así podemos agregar productos a la tabla sin que los datos se borren.
                    dftm = (DefaultTableModel) tblNuevaVenta.getModel();
                    // Utilizamos un buque for para recorrer toda la tabla tblNuevaVenta.
                    for (int i = 0; i < tblNuevaVenta.getRowCount(); i++) {

                        // Se valida que el producto no se repita en la tabla tblNuevaVenta.
                        if (tblNuevaVenta.getValueAt(i, 1).equals(txtDescripcionVenta.getText())) {
                            // Enviamos mensaje.
                            JOptionPane.showMessageDialog(null, "El producto ya está registrado en la tabla");
                            // Retornamos para detener el proceso.
                            return;
                        }
                    }
                    // Creamos un ArrayList.
                    ArrayList lista = new ArrayList();
                    // Agreamos a la lista todo el producto.
                    lista.add(item);
                    lista.add(codigo);
                    lista.add(descripcion);
                    lista.add(cantidad);
                    lista.add(precio);
                    lista.add(total);
                    // Con un Object agregamos todos los productos a la tabla tblNuevaVenta.
                    Object[] o = new Object[5];
                    // Agregamos al Object la lista.
                    o[0] = lista.get(1);
                    o[1] = lista.get(2);
                    o[2] = lista.get(3);
                    o[3] = lista.get(4);
                    o[4] = lista.get(5);
                    // Todo le Object se lo agregamos al mosdelo.
                    dftm.addRow(o);
                    // El modelo lo agregamos a la tabla tblNuevaVenta.
                    tblNuevaVenta.setModel(dftm);
                    // Llamamos el método TotalPagar().
                    TotalPagar();
                    LimpiarVenta();
                    // El focus se transfiere al txtCodProdVenta.
                    txtCodProdVenta.requestFocus();
                    // Verificamos con un else si la camtidad supera al stock disponible.
                } else {
                    // Mandamos un mensaje con un JOptionPane.
                    JOptionPane.showMessageDialog(null, "Stock no disponible");
                }
            } else {
                // Si el stock es mayor a la cantidad envía el siguiente mensaje.
                JOptionPane.showMessageDialog(null, "Ingrese la cantidad");
            }
        }
    }//GEN-LAST:event_txtCantidadVentaKeyPressed

    private void btnEliminarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarVentaActionPerformed
        // Utilizamos el moodelo del DefaultTableModel.
        modelo = (DefaultTableModel) tblNuevaVenta.getModel();
        // Al medelo le quitamos una fila, esto permitirá quitar un producto de la tabla en caso de que el cliente no deseé llevar un producto ya ingresado.
        modelo.removeRow(tblNuevaVenta.getSelectedRow());
        // Llamamos el método Totalpagar para actualizar el total.
        TotalPagar();
        // Transferimos el focus al txtCodProdVenta.
        txtCodProdVenta.requestFocus();
    }//GEN-LAST:event_btnEliminarVentaActionPerformed

    private void txtCedulaClienteVentaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaClienteVentaKeyPressed
        // Aquí se hace el proceos de busccar un cliente para generar una nueva venta. Validamos con un if que la tecla ENTER fue presionada.
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            // Verificamos que el campo txtCedulaClienteVenta no esté vacío.
            if (!"".equals(txtCedulaClienteVenta.getText())) {
                int cedula = Integer.parseInt(txtCedulaClienteVenta.getText());
                // Llamamos al método BuscarCliente
                cl = cliDao.BuscarCliente(cedula);
                // Verificamos si el campo txtNombresClienteVenta es difente de vacío.
                if (cl.getNombre() != null) {
                    // Mostramos los datos del cliente en los campos de texto.
                    txtNombresClienteVenta.setText("" + cl.getNombre());
                    txtTelefonoClienteVenta.setText("" + cl.getTelefono());
                    txtDireccionClienteVenta.setText("" + cl.getDireccion());
                    txtRazonClienteVenta.setText("" + cl.getRazon());
                    // Si la respuesta el vacía enviamos un mensaje
                } else {
                    // Limpiamos el campo de texto 
                    txtCedulaClienteVenta.setText("");
                    txtCedulaClienteVenta.requestFocus();
                    JOptionPane.showMessageDialog(null, "El cliente no existe");
                }
            }
        }
    }//GEN-LAST:event_txtCedulaClienteVentaKeyPressed

    private void btnNuevaVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevaVentaActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_btnNuevaVentaActionPerformed

    private void btnGenerarVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarVentaActionPerformed
        // Veficamos si existe un producto y un cliente para poder realizar la nueva venta.
        if (tblNuevaVenta.getRowCount() > 0) {
            // Verificamos si exite el cliente
            if (!"".equals(txtNombresClienteVenta.getText())) {
                // Si todo es diferente de vacìo llamamos los siguientes métodos.
                //Llamado del método RegistrarVenta de la clase VentaDAO.
                RegistrarVenta();
                //Llamado del método RegistrarDetalle de la clase VentaDAO.
                RegistrarDetalle();
                //Llamado del método ActualizarStock de la clase ProductoDAO.
                ActualizarStock();
                // Llamamos el método generar pdf vanta.
                pdf();
                LimpiarTblNuevaVenta();
                txtCedulaClienteVenta.setText("");
                txtNombresClienteVenta.setText("");
                txtTelefonoClienteVenta.setText("");
                txtDireccionClienteVenta.setText("");
                txtRazonClienteVenta.setText("");
                lblTotal.setText("");
                // Usamos un else para mostrar un mesaje si el cliente nos existe.
            } else {
                JOptionPane.showMessageDialog(null, "Debes buscar un Cliente");
            }
            // Cuando no exite producto mandamos un mensaje.
        } else {
            JOptionPane.showMessageDialog(null, "Debes insertar por lo menos un Producto");
        }
    }//GEN-LAST:event_btnGenerarVentaActionPerformed

    private void txtCantidadVentaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCantidadVentaKeyTyped
        // Llamamos el método validar para que este campo  de texto solo reciba números.
        validar.numberKeyPress(evt);
    }//GEN-LAST:event_txtCantidadVentaKeyTyped

    private void txtNombreClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreClienteKeyTyped
        // Llamamos el método validar para que este campo  de texto solo reciba caráteres mas no números.
        validar.textKeyPress(evt);
    }//GEN-LAST:event_txtNombreClienteKeyTyped

    private void txtTelefonoClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoClienteKeyTyped
        // Llamamos el método validar para que este campo  de texto solo reciba números.
        validar.numberKeyPress(evt);
    }//GEN-LAST:event_txtTelefonoClienteKeyTyped

    private void txtRazonSocialClienteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRazonSocialClienteKeyTyped
        // Llamamos el método validar para que este campo  de texto solo reciba caráteres mas no números.
        validar.textKeyPress(evt);
    }//GEN-LAST:event_txtRazonSocialClienteKeyTyped

    private void txtNomProveedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNomProveedorKeyTyped
        // Llamamos el método validar para que este campo  de texto solo reciba caráteres mas no números.
        validar.textKeyPress(evt);
    }//GEN-LAST:event_txtNomProveedorKeyTyped

    private void txtTelProveedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelProveedorKeyTyped
        // Llamamos el método validar para que este campo  de texto solo reciba números.
        validar.numberKeyPress(evt);
        // Validamos que el campo sólo reciba 10 números.
        if (txtTelProveedor.getText().length() >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txtTelProveedorKeyTyped

    private void txtRazonSoProveedorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRazonSoProveedorKeyTyped
        // Llamamos el método validar para que este campo  de texto solo reciba números.
        validar.textKeyPress(evt);
    }//GEN-LAST:event_txtRazonSoProveedorKeyTyped

    private void txtStockProductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStockProductoKeyTyped
        // Llamamos el método validar para que este campo  de texto solo reciba números.
        validar.numberKeyPress(evt);
    }//GEN-LAST:event_txtStockProductoKeyTyped

    private void txtPrecioPeroductoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrecioPeroductoKeyTyped
        // Llamamos el método validar para que este campo  de texto solo reciba números decimales.
        validar.numberDecimalKeyPress(evt, txtPrecioPeroducto);
    }//GEN-LAST:event_txtPrecioPeroductoKeyTyped

    private void btnModificarDatosEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarDatosEmpresaActionPerformed
        // Verificamos con un if que el campo txtIdConfig no esté vacío.
        if (!"".equals(txtIdConfig.getText()) || !"".equals(txtNitEmpresa.getText()) || !"".equals(txtNombreEmpresa.getText()) || !"".equals(txtTelefonoEmpresa.getText()) || !"".equals(txtDireccionEmpresa.getText()) || !"".equals(txtRazonSocialEmpresa.getText())) {
            //Después de selecionar todos los datos del negocio en los Text Field.
            confi.setNit(txtNitEmpresa.getText());
            confi.setNombre(txtNombreEmpresa.getText());
            confi.setTelefono(txtTelefonoEmpresa.getText());
            confi.setDireccion(txtDireccionEmpresa.getText());
            confi.setRazon(txtRazonSocialEmpresa.getText());
            confi.setId(Integer.parseInt(txtIdConfig.getText()));
            productoDao.ModificarDatosNegocio(confi);
            // Enviamos mensaje
            JOptionPane.showMessageDialog(null, "Se modificaron los datos del Negocio");
            // Se llama el método limpiarconfig.
            ListarConfig();
        } else {
            //Mostramos un mensaje si es que hay un campo vacío.
            JOptionPane.showMessageDialog(null, "Tiene campos vacíos, verifica por favor");
        }
    }//GEN-LAST:event_btnModificarDatosEmpresaActionPerformed

    private void btnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(5);
    }//GEN-LAST:event_btnConfigActionPerformed

    private void btnVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasActionPerformed
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(4);
        LimpiarTable();
        ListarVentas();
    }//GEN-LAST:event_btnVentasActionPerformed

    private void tblVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblVentasMouseClicked
        // Con variable de tipo int obtenemos el valor de la fila selecionada.
        int fila = tblVentas.rowAtPoint(evt.getPoint());
        // Lo pasamos a txtIdVenta.
        txtIdVenta.setText(tblVentas.getValueAt(fila, 0).toString());
    }//GEN-LAST:event_tblVentasMouseClicked

    private void btnReporteVentasPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteVentasPdfActionPerformed
        try {
            // Al selecionar una venta y precionar el pdf la venta se abre.
            int id = Integer.parseInt(txtIdVenta.getText());
             // Hacemos una concatenacion para que se abra el documento pdf con el número de la venta.
            File file = new File("src/pdf/venta" + id + ".pdf");
            // Seabre el cocumento.
            Desktop.getDesktop().open(file);
        } catch (IOException ex) {
            Logger.getLogger(Sistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnReporteVentasPdfActionPerformed

    private void btnGraficosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGraficosActionPerformed
        // Llamamos la clase Graficos y le pasamos la variable fechaActual, que muestra la fecha actual del sistema.
        String fechaReporte = new SimpleDateFormat("dd/MM/yyyy").format(miFecha.getDate());
        Graficos.Graficar(fechaReporte);
    }//GEN-LAST:event_btnGraficosActionPerformed

    private void btnRegistrarUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarUsuariosActionPerformed
        // TODO add your handling code here:
        Registro reg = new Registro();
        reg.setVisible(true);
    }//GEN-LAST:event_btnRegistrarUsuariosActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sistema().setVisible(true);
            }
        });
    }

    // Creamos un método que permita actualizar el total a pagar en la tabla tblNuevaVenta. Cada que se agregue o quite un producto de la lista el totao a pagar se debe actualizar.
    private void TotalPagar() {
        // Verificamos la variable Totalpagar.
        Totalpagar = 0.0;
        // Obtenemos el número de filas de la tabla con la variable numFila.
        int numFila = tblNuevaVenta.getRowCount();
        // Con un buque for recorremos las filas de la tabla.
        for (int i = 0; i < numFila; i++) {
            // Se crea variable de tipo double calcular para sunar el valor de cada fila de la tabla y calcular el total a pagar.
            double calcular = Double.parseDouble(String.valueOf(tblNuevaVenta.getModel().getValueAt(i, 4).toString()));
            // Calculamos el total a pagar.
            Totalpagar = Totalpagar + calcular;
        }
        // El total a pagar lo mostramos en el lblTotal
        //lblTotal.setText("$ " + Totalpagar);

        //Creación de un formato con separadores de decimales y millares, con 2 decimales
//DecimalFormat formato = new DecimalFormat("#,###.00");
//String valorFormateado = formato.format(123456.789);
//Muestra en pantalla el valor 123.456,79 teniendo en cuenta que se usa la puntuación española
//System.out.println(valorFormateado);
        lblTotal.setText(String.format("$%.1f", +Totalpagar));
    }

    // Creamos un método que permite registrar una nueva venta en la base de datos.
    private void RegistrarVenta() {
        // Primeto vamos almacenar todos los valores en variables.
        String cliente = txtNombresClienteVenta.getText();
        String vendedor = lblVendedor.getText();
        double monto = Totalpagar;
        // Llamamos la clase Venta con su variable venta para acceder a los datos de la venta y almacenarlos en sus respectivas variables.
        venta.setCliente(cliente);
        venta.setVendedor(vendedor);
        venta.setTotal(monto);
        venta.setFecha(fechaActual);
        // Llamamos la clase VentaDAO variable vdao y accedemos a su método RegistrarVenta y le enviamos toda la clase Venta con su variable venta.
        vdao.RegistrarVenta(venta);
    }

    // Creamos un método que permite registrar el detalle venta en la base de datos.
    private void RegistrarDetalle() {
        // Para obtener el id de la venta declaramos una variable de tipo int y accedemos al método vdao.idVenta de clase VentaDAO.
        int id = vdao.idVenta();
        // Recorremos los valores de la tabla tblNuevaVenta con un buqeu for.
        for (int i = 0; i < tblNuevaVenta.getRowCount(); i++) {
            // Creamos variables para almacenar los resultados.
            String cod = tblNuevaVenta.getValueAt(i, 0).toString();
            int can = Integer.parseInt(tblNuevaVenta.getValueAt(i, 2).toString());
            double precio = Double.parseDouble(tblNuevaVenta.getValueAt(i, 3).toString());
            // Usamos la variable dv para acceder a los datos.
            dv.setCod_producto(cod);
            dv.setCantidad(can);
            dv.setPrecio(precio);
            dv.setId_venta(id);
            // Se llama la variable vdao para acceder al método RegistrarDetalle y le enviamos la variable dv de la clase Detalle.
            vdao.RegistrarDetalle(dv);
        }

    }

    // Creamos el método actualizar stock.
    public void ActualizarStock() {
        // Utilizamos un buque for para recorrer todos los productos que re nemos almacenados en nuesra tabla tblNuevaVenta.
        for (int i = 0; i < tblNuevaVenta.getRowCount(); i++) {
            // Creamos variables
            String codigo = tblNuevaVenta.getValueAt(i, 0).toString();
            int cantidad = Integer.parseInt(tblNuevaVenta.getValueAt(i, 2).toString());
            // Hacemos un llamado al método buscar codigo, está en ProductoDAO.
            producto = productoDao.BuscarProducto(codigo);
            // Creamos variable de tipo int para StokActual, con la variable producto accedemos al getStock() y se resta con la cantidad de la tabla.
            int StokActual = producto.getStock() - cantidad;
            // Se llama al método ActualizarStock de la clase VentaDAO.
            vdao.ActualizarStock(StokActual, codigo);
        }
    }

    //Creamos un método que me permite limpiar la tabla tblNuevaVenta despues de registrar la compra.
    public void LimpiarTblNuevaVenta() {
        dftm = (DefaultTableModel) tblNuevaVenta.getModel();
        // Obtenemos las filas
        int fila = tblNuevaVenta.getRowCount();
        // COn un bucle for recoremos todas las filas de la tabla.
        for (int i = 0; i < fila; i++) {
            // Removemos todas las filas
            dftm.removeRow(0);
        }
    }

    // Creamos un método que nos permite generar reportes en pdf.
    private void pdf() {
        // Creamos un try ctch para capturar las excepciones
        try {
            // Instanceamos la VentaDAO con su variable vdao.
            int id = vdao.idVenta();
            // Le damos la ruta de salida al archivo pdf.
            FileOutputStream archivo;
            // Indicamos la ruta y el nombre del archivo, concatenamos id
            File file = new File("src/pdf/venta" + id + ".pdf");
            // Usamos la variable archivo y dentro de ello le pasamos la ruta file.
            archivo = new FileOutputStream(file);
            // Creamos el documeneto.
            Document doc = new Document();
            // Creamos el documento pdf usando la librería iTeschart
            PdfWriter.getInstance(doc, archivo);
            // Abrimos el pdf
            doc.open();
            // Insertamos el logo del negocio
            Image img = Image.getInstance("src/img/logo_pdf.png");
            // Creamos un Paragraph para la fecha.
            Paragraph fecha = new Paragraph();
            // Definimos el tipo de fuente que vamos a utilizar en el documneto.
            Font negrita = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.BLUE);
            // Creamos una nueva línea
            fecha.add(Chunk.NEWLINE);
            // Agregmos la fecha del sistema y le ponemos formato con saltos de línea.
            Date date = new Date();
            fecha.add("Factura: " + id + "\nFecha: " + new SimpleDateFormat("dd-MM-yyyy").format(date) + "\n\n");
            // Creamos la tabla para el pdf, ponemos el encabezado y agregaomos 4 columnas.
            PdfPTable Encabezado = new PdfPTable(4);
            // Especificamos el tamaño para el encabezado, para que ocupe todo el ancho de la página le agregamos 100.
            Encabezado.setWidthPercentage(100);
            // Quitamos el borde de la tabla.
            Encabezado.getDefaultCell().setBorder(0);
            // Se crea el tamaño para cada una de las celdas de la tabla. Para ello creamos un arreglo de tipo float.
            float[] ColumnaEncabezado = new float[]{20f, 30f, 70f, 40f};
            // Pasamos el arreglo a nuestros encabezados.
            Encabezado.setWidths(ColumnaEncabezado);
            // Especificamos la posición.
            Encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

            // Comenzamos agregando información en cada una de sus celdas. Comenzamos agregando el logo del negocio.
            Encabezado.addCell(img);
            // Continuamos con los demás datos del negocio o empresa.
            String Nit = txtNitEmpresa.getText();
            String nombre = txtNombreEmpresa.getText();
            String telefono = txtTelefonoEmpresa.getText();
            String direccion = txtDireccionEmpresa.getText();
            String razon = txtRazonSocialEmpresa.getText();
            // Centramos los datos del negocio en el documento pdf.
            Encabezado.addCell("");
            // Agregamos los datos del negocio en las celdas, usamos concatenación.
            Encabezado.addCell("Nit: " + Nit + "\nNombre: " + nombre + "\nTeléfono: " + telefono
                    + "\nDirección: " + direccion + "\nRazónSocial: " + razon);
            Encabezado.addCell(fecha);
            // Agregamos todas las celdas al documento.
            doc.add(Encabezado);

// Cremao nuevo Paragraph par la información del cliente en el reporte de la vaenta.
            Paragraph cliente = new Paragraph();
            // Agregamos el cliente en una nueva límea
            cliente.add(Chunk.NEWLINE);
            // Agregamos el título para la información del cliente.
            cliente.add("\nDatos del Cliente");
            // Le agregamos el documento al cliente
            doc.add(cliente);

            // Se crea una tabla con 4 columnas para obteener los datos del cliente.
            PdfPTable tblCli = new PdfPTable(4);
            // Especificamos el tamaño para el encabezado, para que ocupe todo el ancho de la página le agregamos 100.
            tblCli.setWidthPercentage(100);
            // Quitamos el borde de la tabla.
            tblCli.getDefaultCell().setBorder(0);
            // Se crea el tamaño para cada una de las celdas de la tabla. Para ello creamos un arreglo de tipo float.
            float[] ColumnaCli = new float[]{20f, 50f, 30f, 40f};
            // Pasamos el arreglo a nuestros encabezados.
            tblCli.setWidths(ColumnaCli);
            // Especificamos la posición.
            tblCli.setHorizontalAlignment(Element.ALIGN_LEFT);
            // Agregamos los títulos en las celdas del cliente.
            PdfPCell cl1 = new PdfPCell(new Phrase("Cédula/Doc", negrita));
            PdfPCell cl2 = new PdfPCell(new Phrase("Nombre", negrita));
            PdfPCell cl3 = new PdfPCell(new Phrase("Teléfono", negrita));
            PdfPCell cl4 = new PdfPCell(new Phrase("Dirección", negrita));
            // A cada una de las celdas le quitamos el border.
            cl1.setBorder(0);
            cl2.setBorder(0);
            cl3.setBorder(0);
            cl4.setBorder(0);
            // Agregamos BackgroundColor a los datos del cliente
            cl1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cl2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cl3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cl4.setBackgroundColor(BaseColor.LIGHT_GRAY);
            // Agreagamos todas las celdas a la tabla.
            tblCli.addCell(cl1);
            tblCli.addCell(cl2);
            tblCli.addCell(cl3);
            tblCli.addCell(cl4);
            // Agregamos los datos del cliente que obtenemos de los campos de texto al mometo de generar una nueva venta.
            tblCli.addCell(txtCedulaClienteVenta.getText());
            tblCli.addCell(txtNombresClienteVenta.getText());
            tblCli.addCell(txtTelefonoClienteVenta.getText());
            tblCli.addCell(txtDireccionClienteVenta.getText());
            // Agregamos todo al documento
            doc.add(tblCli);

            // Agremos la informaciòn del producto que va a llevar el cliente al pdpf.
            // Se crea una tabla con 4 columnas para obteener los datos del producto.
            PdfPTable tblPro = new PdfPTable(4);
            // Especificamos el tamaño para el encabezado, para que ocupe todo el ancho de la página le agregamos 100.
            tblPro.setWidthPercentage(100);
            // Quitamos el borde de la tabla.
            tblPro.getDefaultCell().setBorder(0);
            // Se crea el tamaño para cada una de las celdas de la tabla. Para ello creamos un arreglo de tipo float.
            float[] ColumnaPro = new float[]{13f, 60f, 15f, 25f};
            // Pasamos el arreglo a nuestros encabezados.
            tblPro.setWidths(ColumnaPro);
            // Especificamos la posición.
            tblPro.setHorizontalAlignment(Element.ALIGN_LEFT);
            // Agregamos los títulos en las celdas del producto.
            PdfPCell Pro1 = new PdfPCell(new Phrase("Cantidad", negrita));
            PdfPCell Pro2 = new PdfPCell(new Phrase("Descripción", negrita));
            PdfPCell Pro3 = new PdfPCell(new Phrase("Precio U.", negrita));
            PdfPCell Pro4 = new PdfPCell(new Phrase("Total", negrita));
            // A cada una de las celdas le quitamos el border.
            Pro1.setBorder(0);
            Pro2.setBorder(0);
            Pro3.setBorder(0);
            Pro4.setBorder(0);
            // Agregamos BackgroundColor a todos los productoa
            Pro1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            Pro2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            Pro3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            Pro4.setBackgroundColor(BaseColor.LIGHT_GRAY);
            // Agreagamos todas las celdas a la tabla.
            tblPro.addCell(Pro1);
            tblPro.addCell(Pro2);
            tblPro.addCell(Pro3);
            tblPro.addCell(Pro4);
            // Agregamos un buque for para recorrer todos los productos que hayan en la tabla tblNuevaVenta.
            for (int i = 0; i < tblNuevaVenta.getRowCount(); i++) {
                // Agregamos variables.
                String descripcion = tblNuevaVenta.getValueAt(i, 1).toString();
                String cantidad = tblNuevaVenta.getValueAt(i, 2).toString();
                String precio = tblNuevaVenta.getValueAt(i, 3).toString();
                String total = tblNuevaVenta.getValueAt(i, 4).toString();
                // Agregamos los datos del producto que obtenemos de los campos de texto al mometo de generar una nueva venta.
                tblPro.addCell(cantidad);
                tblPro.addCell(descripcion);
                tblPro.addCell(precio);
                tblPro.addCell(total);
            }
            // Agregamos todo al documento
            doc.add(tblPro);

            // Creamos un Paragraph para traer los datos del negocio directamente desde la base de datos. Los datos son: El Nit, Nombre del negocio, Teléfono, Direccioón y la razón social. Estos datos los podemos ven en el encabezado de los reportes en pdf de nuestras nuevas ventas.
            Paragraph info = new Paragraph();
            // Agregamos este Paragraph en una nueva línea
            info.add(Chunk.NEWLINE);
            // Agregamos un título para el total a pagar de la venta, concatenamos la varible Totalpagar.
            info.add("Total a pagar: " + "$" + Totalpagar);
            // Alineamos el dato a la derecha del documento pdf.
            info.setAlignment(Element.ALIGN_RIGHT);
            // Lo agregamos al documento
            doc.add(info);

            // Creamos un Paragraph para la firma.
            Paragraph firma = new Paragraph();
            // Agregamos este Paragraph en una nueva línea
            firma.add(Chunk.NEWLINE);
            // Título para la firma del documento.
            firma.add("Cancelación y Firma");
            // Espacio para que el vendedor ponga su firma, usamos saltos de línea \n\n.
            firma.add("\n\n--------------------------------------------------");
            // Alineamos la firma al centro del documento pdf.
            firma.setAlignment(Element.ALIGN_CENTER);
            // Lo agregamos al documento el espacio de la firma.
            doc.add(firma);

            // Creamos un Paragraph para el mensaje.
            Paragraph mensaje = new Paragraph();
            // Agregamos este Paragraph en una nueva línea
            mensaje.add(Chunk.NEWLINE);
            // Título para el mensaje.
            mensaje.add("¡Gracias por Preferirnos!.");
            // Alineamos el mensaje centro del documento pdf.
            mensaje.setAlignment(Element.ALIGN_CENTER);
            // Lo agregamos el mensaje ala documento.
            doc.add(mensaje);

            // Cerramos el documento pdf.
            doc.close();
            // Cerramos el archivo.
            archivo.close();
            // Cada vez que se genere una nueva venta el pdf se debe abrir automaticamente, para ello se usa el método Desktop y le pasamos la ruta file.
            Desktop.getDesktop().open(file);
        } catch (DocumentException | IOException e) {
            // Se captura la excepción.
            System.err.println("Error al generar pdf " + e.toString());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarCliente;
    private javax.swing.JButton btnActualizarProducto;
    private javax.swing.JButton btnActualizarProveedor;
    private javax.swing.JButton btnAgregarProveedor;
    private javax.swing.JButton btnClientes;
    private javax.swing.JButton btnConfig;
    private javax.swing.JButton btnEliminarCliente;
    private javax.swing.JButton btnEliminarProducto;
    private javax.swing.JButton btnEliminarProveedor;
    private javax.swing.JButton btnEliminarVenta;
    private javax.swing.JButton btnGenerarVenta;
    private javax.swing.JButton btnGraficos;
    private javax.swing.JButton btnGuardarCliente;
    private javax.swing.JButton btnGuardarProducto;
    private javax.swing.JButton btnGuardarProveedor;
    private javax.swing.JButton btnModificarDatosEmpresa;
    private javax.swing.JButton btnNuevaVenta;
    private javax.swing.JButton btnNuevoCliente;
    private javax.swing.JButton btnNuevoProducto;
    private javax.swing.JButton btnProducto;
    private javax.swing.JButton btnProveedor;
    private javax.swing.JButton btnRegistrarUsuarios;
    private javax.swing.JButton btnReporteProductoExcel;
    private javax.swing.JButton btnReporteVentasPdf;
    private javax.swing.JButton btnVentas;
    private javax.swing.JComboBox<String> cbxProveedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblMensajeCliente;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblVendedor;
    private com.toedter.calendar.JDateChooser miFecha;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTable tblNuevaVenta;
    private javax.swing.JTable tblProductos;
    private javax.swing.JTable tblProveedor;
    private javax.swing.JTable tblVentas;
    private javax.swing.JTextField txtCantidadVenta;
    private javax.swing.JTextField txtCedulaCliente;
    private javax.swing.JTextField txtCedulaClienteVenta;
    private javax.swing.JTextField txtCodProdVenta;
    private javax.swing.JTextField txtCodigoProducto;
    private javax.swing.JTextField txtDescripcionProducto;
    private javax.swing.JTextField txtDescripcionVenta;
    private javax.swing.JTextField txtDirProveedor;
    private javax.swing.JTextField txtDireccionCliente;
    private javax.swing.JTextField txtDireccionClienteVenta;
    private javax.swing.JTextField txtDireccionEmpresa;
    private javax.swing.JTextField txtDocProveedor;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtIdConfig;
    private javax.swing.JTextField txtIdPro;
    private javax.swing.JTextField txtIdProducto;
    private javax.swing.JTextField txtIdProveedor;
    private javax.swing.JTextField txtIdVenta;
    private javax.swing.JTextField txtNitEmpresa;
    private javax.swing.JTextField txtNomProveedor;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNombreEmpresa;
    private javax.swing.JTextField txtNombresClienteVenta;
    private javax.swing.JTextField txtPrecioPeroducto;
    private javax.swing.JTextField txtPrecioVenta;
    private javax.swing.JTextField txtRazonClienteVenta;
    private javax.swing.JTextField txtRazonSoProveedor;
    private javax.swing.JTextField txtRazonSocialCliente;
    private javax.swing.JTextField txtRazonSocialEmpresa;
    private javax.swing.JTextField txtStockDisponible;
    private javax.swing.JTextField txtStockProducto;
    private javax.swing.JTextField txtTelProveedor;
    private javax.swing.JTextField txtTelefonoCliente;
    private javax.swing.JTextField txtTelefonoClienteVenta;
    private javax.swing.JTextField txtTelefonoEmpresa;
    // End of variables declaration//GEN-END:variables
}
