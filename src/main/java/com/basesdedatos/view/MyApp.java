package com.basesdedatos.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.basesdedatos.model.Clientes;
import com.basesdedatos.model.Pedidos;
import com.basesdedatos.model.Productos;
import com.basesdedatos.repository.ClienteRepository;
import com.basesdedatos.repository.PedidoRepository;
import com.basesdedatos.repository.ProductoRepository;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

public class MyApp extends JFrame {
    private ClienteRepository clienteRepository;
    private PedidoRepository pedidoRepository;
    private ProductoRepository productoRepository;

    private DefaultTableModel tableModel;
    private JTable dataTable;
    private JScrollPane scrollPane;

    public MyApp() {
        clienteRepository = new ClienteRepository();
        pedidoRepository = new PedidoRepository();
        productoRepository = new ProductoRepository();

        // Create table model and table
        tableModel = new DefaultTableModel();
        dataTable = new JTable(tableModel);
        scrollPane = new JScrollPane(dataTable);

        // Create Swing components and add them to the JFrame
        JButton btnGetClientes = new JButton("Get All Clientes");
        JButton btnGetPedidos = new JButton("Get All Pedidos");
        JButton btnGetProductos = new JButton("Get All Productos");
        JButton btnAddPedido = new JButton("Add Pedido");
        JButton btnAddProducto = new JButton("Add Producto");
        JButton btnCountPedidosButton = new JButton("Count Pedidos");
        JButton btnListClientDetails = new JButton("List Client Details");
        JButton btnCountPedidosPorCliente = new JButton("Count Pedidos by Clientes");
        JButton btnConsultarDetallesPedidos = new JButton("Consult Details of Pedidos");
        JButton btnCountClientes = new JButton("Count Clients");
        JButton btnListClientesConPedidos = new JButton("List Clients with Pedidos");
        JButton btnGetStockProductosDisponibles = new JButton("Get Stock available Productos");
        JButton btnListarProductosDescripcionPrecio = new JButton("List Productos with Descripción and Precio");
        JButton btnProductosPrecioSuperior = new JButton("Productos price > input");
        JButton btnSumarTotalPedidos = new JButton("Sumar Total de Pedidos");






        JPanel panel = new JPanel(new GridLayout(0, 3)); // GridLayout de 3 columnas
        panel.add(btnGetClientes);
        panel.add(btnGetPedidos);
        panel.add(btnGetProductos);
        panel.add(btnAddProducto);
        panel.add(btnCountPedidosButton);
        panel.add(btnListClientDetails);
        panel.add(btnCountPedidosPorCliente);
        panel.add(btnConsultarDetallesPedidos);
        panel.add(btnCountClientes);
        panel.add(btnListClientesConPedidos);
        panel.add(btnGetStockProductosDisponibles);
        panel.add(btnListarProductosDescripcionPrecio);
        panel.add(btnProductosPrecioSuperior);
        panel.add(btnSumarTotalPedidos);




        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Register action listeners
        btnGetClientes.addActionListener(e -> fetchData(() -> clienteRepository.findAll()));
        btnGetPedidos.addActionListener(e -> fetchData(() -> pedidoRepository.findAll()));
        btnGetProductos.addActionListener(e -> fetchData(() -> productoRepository.findAll()));
        btnAddPedido.addActionListener(e -> addPedido());
        btnAddProducto.addActionListener(e -> addProducto());
        btnCountPedidosButton.addActionListener(e -> countPedidos());
        btnListClientDetails.addActionListener(e -> listClientDetails());
        btnCountPedidosPorCliente.addActionListener(e -> countPedidosByClientes());
        btnConsultarDetallesPedidos.addActionListener(e -> consultDetailsOfPedidos());
        btnCountClientes.addActionListener(e -> countClientes());
        btnListClientesConPedidos.addActionListener(e -> listarClientesConPedidos());
        btnGetStockProductosDisponibles.addActionListener(e -> listarStockProductosDisponibles());
        btnListarProductosDescripcionPrecio.addActionListener(e -> listarProductosDescripcionPrecio());
        btnProductosPrecioSuperior.addActionListener(e -> productosPrecioSuperior());
        btnSumarTotalPedidos.addActionListener(e -> sumarTotalPedidos());





        // Set JFrame properties
        setTitle("My App");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the JFrame on the screen
        setVisible(true);
    }

    private void fetchData(DataFetcher dataFetcher) {
        try {
            List<?> data = dataFetcher.fetch();
            displayDataInTable(data);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(MyApp.this, "Error fetching data: " + ex.getMessage());
        }
    }

    private void displayDataInTable(List<?> data) {
        // Clear existing data
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        // Populate table with data
        if (!data.isEmpty()) {
            Object firstObject = data.get(0);
            if (firstObject instanceof Clientes) {
                // Handle Clientes data
                tableModel.addColumn("Clientes_ID");
                tableModel.addColumn("Nombre");
                tableModel.addColumn("Apellido");
                tableModel.addColumn("Direccion");
                tableModel.addColumn("Contacto");
                for (Object obj : data) {
                    Clientes cliente = (Clientes) obj;
                    tableModel.addRow(new Object[]{
                            cliente.getClientes_ID(),
                            cliente.getNombre(),
                            cliente.getApellido(),
                            cliente.getDireccion(),
                            cliente.getContacto()
                    });
                }
            } else if (firstObject instanceof Pedidos) {
                // Handle Pedidos data
                tableModel.addColumn("Pedidos_ID");
                tableModel.addColumn("Cliente_ID");
                tableModel.addColumn("Fecha_Pedido");
                tableModel.addColumn("Estado");
                tableModel.addColumn("Precio_Total");
                for (Object obj : data) {
                    Pedidos pedido = (Pedidos) obj;
                    tableModel.addRow(new Object[]{
                            pedido.getPedidos_ID(),
                            pedido.getCliente_ID(),
                            pedido.getFechaPedido(),
                            pedido.isEstado(),
                            pedido.getPrecio_Total()
                    });
                }
            } else if (firstObject instanceof Productos) {
                // Handle Productos data
                tableModel.addColumn("Productos_ID");
                tableModel.addColumn("Nombre_Producto");
                tableModel.addColumn("Descripcion");
                tableModel.addColumn("Precio");
                tableModel.addColumn("Stock_Disponibles");
                for (Object obj : data) {
                    Productos producto = (Productos) obj;
                    tableModel.addRow(new Object[]{
                            producto.getProductos_ID(),
                            producto.getNombre_Producto(),
                            producto.getDescripcion(),
                            producto.getPrecio(),
                            producto.isStock_Disponible()
                    });
                }
            }
        }
    }

    private void addPedido() {
    JTextField productoIdField = new JTextField();
    JTextField clienteIdField = new JTextField();
    JTextField fechaPedidoField = new JTextField();
    JTextField precioTotalField = new JTextField();

    Object[] fields = {
        "ID del Producto:", productoIdField,
        "ID del Cliente:", clienteIdField,
        "Fecha del Pedido (YYYY-MM-DD HH:MM:SS):", fechaPedidoField,
        "Precio Total:", precioTotalField
    };

    int result = JOptionPane.showConfirmDialog(this, fields, "Agregar Pedido", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
        try {
            Pedidos pedido = new Pedidos();
            pedido.setProducto_ID(productoRepository.getById(Integer.parseInt(productoIdField.getText())));
            pedido.setCliente_ID(clienteRepository.getById(Integer.parseInt(clienteIdField.getText())));

            // Convertir la cadena de fecha y hora en un objeto LocalDateTime
            pedido.setFechaPedido(LocalDateTime.parse(fechaPedidoField.getText()));
            pedido.setPrecio_Total(Double.parseDouble(precioTotalField.getText()));

            pedidoRepository.save(pedido);

            JOptionPane.showMessageDialog(this, "Pedido agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException | DateTimeParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: Asegúrate de ingresar valores válidos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            
            e.printStackTrace();
        }
    }
}

private void addProducto() {
    JTextField nombreProductoField = new JTextField();
    JTextField descripcionField = new JTextField();
    JTextField precioField = new JTextField();
    JTextField stockField = new JTextField();

    Object[] fields = {
        "Nombre del Producto:", nombreProductoField,
        "Descripción:", descripcionField,
        "Precio:", precioField,
        "Stock:", stockField
    };

    int result = JOptionPane.showConfirmDialog(this, fields, "Agregar Producto", JOptionPane.OK_CANCEL_OPTION);
    if (result == JOptionPane.OK_OPTION) {
        try {
            Productos producto = new Productos();
            producto.setNombre_Producto(nombreProductoField.getText());
            producto.setDescripcion(descripcionField.getText());
            producto.setPrecio(Double.parseDouble(precioField.getText()));
            producto.setStock_Disponible(Boolean.parseBoolean(stockField.getText()));

            productoRepository.save(producto);

            JOptionPane.showMessageDialog(this, "Producto agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al agregar el producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al convertir el precio a número: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


       private void countPedidos() {
        try {
            // Create a dialog to get the Cliente ID
            String clienteIdStr = JOptionPane.showInputDialog(this, "Enter Cliente ID:");
            if (clienteIdStr != null) {
                int clienteId = Integer.parseInt(clienteIdStr);
                // Fetch the count of Pedidos for the specified Cliente
                int count = pedidoRepository.CountPedidos(new Clientes(clienteId));
                // Display the count in a dialog box
                JOptionPane.showMessageDialog(this, "Number of Pedidos for Cliente ID " + clienteId + ": " + count);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Cliente ID format", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error counting pedidos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listClientDetails() {
        try {
            // Fetch the details of all clients
            List<Clientes> clientDetails = clienteRepository.findAll();
            // Display the client details in the table
            displayDataInTable(clientDetails);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error listing client details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void countPedidosByClientes() {
        try {
            // Fetch the count of Pedidos for each client
            Map<Clientes, Integer> pedidosCountMap = pedidoRepository.contarPedidosPorCliente();
            // Clear the table and set column headers
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            tableModel.addColumn("Cliente_ID");
            tableModel.addColumn("Nombre");
            tableModel.addColumn("Apellido");
            tableModel.addColumn("Pedidos Count");
    
            // Populate the table with client details and their respective Pedidos counts
            for (Map.Entry<Clientes, Integer> entry : pedidosCountMap.entrySet()) {
                Clientes cliente = entry.getKey();
                Integer count = entry.getValue();
                tableModel.addRow(new Object[]{
                        cliente.getClientes_ID(),
                        cliente.getNombre(),
                        cliente.getApellido(),
                        count
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error counting pedidos by clientes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
        

    private void consultDetailsOfPedidos() {
        try {
            // Fetch the details of all Pedidos
            List<String> detallesPedidosList = pedidoRepository.consultarDetallesPedidos();
            
            // Clear the table and set column headers
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
            tableModel.addColumn("Pedidos_ID");
            tableModel.addColumn("Nombre_Cliente");
            tableModel.addColumn("Apellido_Cliente");
            tableModel.addColumn("Direccion_Cliente");
            tableModel.addColumn("Contacto_Cliente");
            tableModel.addColumn("Fecha_Pedido");
            tableModel.addColumn("Estado_Pedido");
            tableModel.addColumn("Metodo_Pago");
            tableModel.addColumn("Precio_Total");
            tableModel.addColumn("Fecha_Entrega");
            tableModel.addColumn("Estado_Entrega");
            tableModel.addColumn("Direccion_Entrega");
    
            // Populate the table with the details of Pedidos
            for (String detallesPedido : detallesPedidosList) {
                String[] detallesArray = detallesPedido.split(" \\| ");
                tableModel.addRow(detallesArray);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error consulting pedido details: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Placeholder methods for future implementation
    private void updatePedido() {
        // Implement updatePedido functionality
        // You can create a dialog for user input and then call the repository method to update the pedido
    }

    // Method to count clients
    private void countClientes() {
        try {
            int totalClientes = clienteRepository.CountClientes();
            JOptionPane.showMessageDialog(this, "Total Clients: " + totalClientes, "Client Count", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error counting clients: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    interface DataFetcher {
        List<?> fetch() throws SQLException;
    }

        // Method to list clients with pedidos
        private void listarClientesConPedidos() {
            try {
                List<String> clientes = pedidoRepository.ListarClientesconPedidos();
                if (clientes.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No clients with pedidos found", "Information", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Clear the table and set column headers
                    tableModel.setRowCount(0);
                    tableModel.setColumnCount(0);
                    tableModel.addColumn("Clientes_ID");
                    tableModel.addColumn("Nombre");
                    tableModel.addColumn("Apellido");
                    tableModel.addColumn("Direccion");
                    tableModel.addColumn("Contacto");
    
                    // Populate the table with client details
                    for (String cliente : clientes) {
                        String[] clienteData = cliente.split(" \\| ");
                        tableModel.addRow(clienteData);
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error listing clients with pedidos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void listarStockProductosDisponibles() {
            try {
                List<String> stockProductosDisponibles = productoRepository.listarStockProductosDisponibles();
                // Display the stock of available products in the table
                displayStockProductosDisponibles(stockProductosDisponibles);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(MyApp.this, "Error fetching stock de productos: " + ex.getMessage());
            }
        }
    
        private void displayStockProductosDisponibles(List<String> stockProductosDisponibles) {
            // Clear existing data
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
    
            // Populate table with stock data
            tableModel.addColumn("Nombre Producto");
            for (String producto : stockProductosDisponibles) {
                tableModel.addRow(new Object[]{producto});
            }
        }

        private void listarProductosDescripcionPrecio() {
            try {
                List<String> productosDescripcionPrecio = productoRepository.listarProductosDescripcionPrecio();
                displayProductosDescripcionPrecio(productosDescripcionPrecio);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(MyApp.this, "Error fetching products: " + ex.getMessage());
            }
        }
    
        private void displayProductosDescripcionPrecio(List<String> productosDescripcionPrecio) {
            // Limpiar datos existentes
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
    
            // Agregar columnas
            tableModel.addColumn("Nombre Producto");
            tableModel.addColumn("Descripción");
            tableModel.addColumn("Precio");
    
            // Poblar la tabla con datos de productos
            for (String producto : productosDescripcionPrecio) {
                String[] productoData = producto.split(" \\| ");
                tableModel.addRow(productoData);
            }
        }

        private void productosPrecioSuperior() {
            String precioStr = JOptionPane.showInputDialog(this, "Ingrese el valor de precio mínimo:");
            if (precioStr != null) {
                try {
                    double precio = Double.parseDouble(precioStr);
                    List<String> productosSuperiores = productoRepository.productosPrecioSuperior(precio);
                    displayProductosSuperiores(productosSuperiores);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error fetching products: " + ex.getMessage());
                }
            }
        }
        
        private void displayProductosSuperiores(List<String> productosSuperiores) {
            // Limpiar datos existentes
            tableModel.setRowCount(0);
            tableModel.setColumnCount(0);
        
            // Agregar columna
            tableModel.addColumn("Nombre Producto");
        
            // Poblar la tabla con datos de productos
            for (String producto : productosSuperiores) {
                tableModel.addRow(new Object[]{producto});
            }
        }
        
        private void sumarTotalPedidos() {
            try {
                // Obtener la suma total de los pedidos
                double totalPedidos = pedidoRepository.sumarTotalPedidos();
                // Mostrar la suma total en un cuadro de diálogo
                JOptionPane.showMessageDialog(this, "La suma total de los pedidos es: " + totalPedidos, "Suma Total de Pedidos", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al obtener la suma total de los pedidos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyApp::new);
    }
}
