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
        JButton btnUpdatePedido = new JButton("Update Pedido");
        JButton btnDeletePedido = new JButton("Delete Pedido");

        JPanel panel = new JPanel();
        panel.add(btnGetClientes);
        panel.add(btnGetPedidos);
        panel.add(btnGetProductos);
        panel.add(btnAddPedido);
        panel.add(btnUpdatePedido);
        panel.add(btnDeletePedido);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Register action listeners
        btnGetClientes.addActionListener(e -> fetchData(() -> clienteRepository.findAll()));
        btnGetPedidos.addActionListener(e -> fetchData(() -> pedidoRepository.findAll()));
        btnGetProductos.addActionListener(e -> fetchData(() -> productoRepository.findAll()));
        btnAddPedido.addActionListener(e -> addPedido());
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
        // Implement addPedido functionality
        // You can create a dialog for user input and then call the repository method to add the pedido
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
