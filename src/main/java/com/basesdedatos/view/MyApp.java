package com.basesdedatos.view;

import javax.swing.*;

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

    private JTextArea txtAreaOutput;

    public MyApp() {
        clienteRepository = new ClienteRepository();
        pedidoRepository = new PedidoRepository();
        productoRepository = new ProductoRepository();

        // Create Swing components and add them to the JFrame
        JButton btnGetClientes = new JButton("Get All Clientes");
        JButton btnGetPedidos = new JButton("Get All Pedidos");
        JButton btnGetProductos = new JButton("Get All Productos");
        JButton btnCountClientes = new JButton("Count Clientes");
        JButton btnCountPedidos = new JButton("Count Pedidos");
        JButton btnListarClientesPedidos = new JButton("Listar Clientes y Pedidos");

        txtAreaOutput = new JTextArea(20, 60);
        JScrollPane scrollPane = new JScrollPane(txtAreaOutput);

        JPanel panel = new JPanel();
        panel.add(btnGetClientes);
        panel.add(btnGetPedidos);
        panel.add(btnGetProductos);
        panel.add(btnCountClientes);
        panel.add(btnCountPedidos);
        panel.add(btnListarClientesPedidos);

        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Register action listeners
        btnGetClientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchData(() -> clienteRepository.findAll());
            }
        });

        btnGetPedidos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchData(() -> pedidoRepository.findAll());
            }
        });

        btnGetProductos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchData(() -> productoRepository.findAll());
            }
        });

        btnCountClientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchCount(() -> clienteRepository.CountClientes());
            }
        });

        btnCountPedidos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchCount(() -> clienteRepository.CountClientes());
            }
        });

        btnListarClientesPedidos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fetchListClientesPedidos();
            }
        });

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
            txtAreaOutput.setText(data.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(MyApp.this, "Error fetching data: " + ex.getMessage());
        }
    }

    private void fetchCount(CountFetcher countFetcher) {
        try {
            int count = countFetcher.count();
            txtAreaOutput.setText("Count: " + count);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(MyApp.this, "Error counting: " + ex.getMessage());
        }
    }

    private void fetchListClientesPedidos() {
        try {
            Map<String, Integer> clientesPedidos = pedidoRepository.contarPedidosPorCliente();
            txtAreaOutput.setText("Clientes y Pedidos:\n" + clientesPedidos.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(MyApp.this, "Error fetching data: " + ex.getMessage());
        }
    }

    interface DataFetcher {
        List<?> fetch() throws SQLException;
    }

    interface CountFetcher {
        int count() throws SQLException;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MyApp();
            }
        });
    }
}
