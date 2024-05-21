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
        JButton btnUpdatePedido = new JButton("Update Pedido");
        JButton btnDeletePedido = new JButton("Delete Pedido");

        JPanel panel = new JPanel();
        panel.add(btnGetClientes);
        panel.add(btnGetPedidos);
        panel.add(btnGetProductos);
        panel.add(btnAddPedido);
        panel.add(btnAddProducto);
        panel.add(btnUpdatePedido);
        panel.add(btnDeletePedido);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Register action listeners
        btnGetClientes.addActionListener(e -> fetchData(() -> clienteRepository.findAll()));
        btnGetPedidos.addActionListener(e -> fetchData(() -> pedidoRepository.findAll()));
        btnGetProductos.addActionListener(e -> fetchData(() -> productoRepository.findAll()));
        btnAddPedido.addActionListener(e -> addPedido());
        btnAddProducto.addActionListener(e -> addProducto());
        btnUpdatePedido.addActionListener(e -> updatePedido());
        btnDeletePedido.addActionListener(e -> deletePedido());

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


    private void updatePedido() {
        // Implement updatePedido functionality
        // You can create a dialog for user input and then call the repository method to update the pedido
    }

    private void deletePedido() {
        // Implement deletePedido functionality
        // You can prompt the user for confirmation and then call the repository method to delete the pedido
    }

    interface DataFetcher {
        List<?> fetch() throws SQLException;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyApp::new);
    }
}
