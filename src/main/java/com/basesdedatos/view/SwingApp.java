package com.basesdedatos.view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.basesdedatos.model.Clientes;
import com.basesdedatos.model.Pedidos;
import com.basesdedatos.model.Productos;
import com.basesdedatos.repository.ClienteRepository;
import com.basesdedatos.repository.PedidoRepository;
import com.basesdedatos.repository.ProductoRepository;
import com.basesdedatos.repository.Repository;

public class SwingApp extends JFrame {
    private final Repository<Clientes> clienteRepository;
    private final Repository<Pedidos> pedidoRepository;
    private final Repository<Productos> productoRepository;

    private final JTable clientesTable;
    private final JTable pedidosTable;
    private final JTable productosTable;

    public SwingApp() {
        setTitle("Gestión de Clientes, Pedidos y Productos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        clienteRepository = new ClienteRepository();
        pedidoRepository = new PedidoRepository();
        productoRepository = new ProductoRepository();

        clientesTable = new JTable();
        JScrollPane clientesScrollPane = new JScrollPane(clientesTable);

        pedidosTable = new JTable();
        JScrollPane pedidosScrollPane = new JScrollPane(pedidosTable);

        productosTable = new JTable();
        JScrollPane productosScrollPane = new JScrollPane(productosTable);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Clientes", clientesScrollPane);
        tabbedPane.addTab("Pedidos", pedidosScrollPane);
        tabbedPane.addTab("Productos", productosScrollPane);
        add(tabbedPane, BorderLayout.CENTER);

        JButton addClienteButton = new JButton("Agregar Cliente");
        JButton addPedidoButton = new JButton("Agregar Pedido");
        JButton addProductoButton = new JButton("Agregar Producto");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addClienteButton);
        buttonPanel.add(addPedidoButton);
        buttonPanel.add(addProductoButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addClienteButton.addActionListener(e -> {
            try {
                addCliente();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        addPedidoButton.addActionListener(e -> {
            try {
                addPedido();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        addProductoButton.addActionListener(e -> {
            try {
                addProducto();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        listClientes();
        listPedidos();
        listProductos();
    }

    private void addCliente() throws SQLException {
        JTextField nombreField = new JTextField();
        JTextField apellidoField = new JTextField();
        JTextField direccionField = new JTextField();
        JTextField contactoField = new JTextField();

        Object[] fields = {
            "Nombre:", nombreField,
            "Apellido:", apellidoField,
            "Dirección:", direccionField,
            "Contacto:", contactoField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Agregar Cliente", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Clientes cliente = new Clientes();
            cliente.setNombre(nombreField.getText());
            cliente.setApellido(apellidoField.getText());
            cliente.setDireccion(direccionField.getText());
            cliente.setContacto(contactoField.getText());

            clienteRepository.save(cliente);

            listClientes();

            JOptionPane.showMessageDialog(this, "Cliente agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addPedido() throws SQLException {
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
            Pedidos pedido = new Pedidos();
            pedido.setProducto_ID(productoRepository.getById(Integer.parseInt(productoIdField.getText())));
            pedido.setCliente_ID(clienteRepository.getById(Integer.parseInt(clienteIdField.getText())));

            // Convertir la cadena de fecha y hora en un objeto LocalDateTime
            pedido.setFechaPedido(LocalDateTime.parse(fechaPedidoField.getText()));
            pedido.setPrecio_Total(Double.parseDouble(precioTotalField.getText()));

            pedidoRepository.save(pedido);

            listPedidos();

            JOptionPane.showMessageDialog(this, "Pedido agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void addProducto() throws SQLException {
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
            Productos producto = new Productos();
            producto.setNombreProducto(nombreProductoField.getText());
            producto.setDescripcion(descripcionField.getText());
            producto.setPrecio(Double.parseDouble(precioField.getText()));
            producto.setStock_Disponible(Boolean.parseBoolean(stockField.getText()));

            productoRepository.save(producto);

            listProductos();

            JOptionPane.showMessageDialog(this, "Producto agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void listClientes() {
        try {
            List<Clientes> clientes = clienteRepository.findAll();

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Nombre");
            tableModel.addColumn("Apellido");
            tableModel.addColumn("Dirección");
            tableModel.addColumn("Contacto");

            for (Clientes cliente : clientes) {
                Object[] dataRow = {
                    cliente.getClientes_ID(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getDireccion(),
                    cliente.getContacto()
                };
                tableModel.addRow(dataRow);
            }
            clientesTable.setModel(tableModel);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener los datos de los clientes", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listPedidos() {
        try {
            List<Pedidos> pedidos = pedidoRepository.findAll();

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("ID del Producto");
            tableModel.addColumn("ID del Cliente");
            tableModel.addColumn("Fecha del Pedido");
            tableModel.addColumn("Precio Total");

            for (Pedidos pedido : pedidos) {
                Object[] dataRow = {
                    pedido.getPedidos_ID(),
                    pedido.getProducto_ID(),
                    pedido.getCliente_ID(),
                    pedido.getFechaPedido(),
                    pedido.getPrecio_Total()
                };
                tableModel.addRow(dataRow);
            }
            pedidosTable.setModel(tableModel);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener los datos de los pedidos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listProductos() {
        try {
            List<Productos> productos = productoRepository.findAll();

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Nombre del Producto");
            tableModel.addColumn("Descripción");
            tableModel.addColumn("Precio");
            tableModel.addColumn("Stock Disponible");

            for (Productos producto : productos) {
                Object[] dataRow = {
                    producto.getProducto_ID(),
                    producto.getNombreProducto(),
                    producto.getDescripcion(),
                    producto.getPrecio(),
                    producto.isStock_Disponible()
                };
                tableModel.addRow(dataRow);
            }
            productosTable.setModel(tableModel);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener los datos de los productos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
