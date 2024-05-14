package com.basesdedatos.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.basesdedatos.config.DatabaseConnection;
import com.basesdedatos.model.Clientes;
import com.basesdedatos.model.Pedidos;
import com.basesdedatos.model.Productos;

public class PedidoRepository implements Repository<Pedidos>, RepositoryPe<Pedidos> {

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance();
    }

    @Override
    public List<Pedidos> findAll() throws SQLException {
        List<Pedidos> pedidosList = new ArrayList<>();
        String query = "SELECT * FROM Pedidos";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Pedidos pedido = mapResultSetToPedidos(resultSet);
                pedidosList.add(pedido);
            }
        }
        return pedidosList;
    }

    @Override
    public Pedidos getById(Integer id) throws SQLException {
        String query = "SELECT * FROM Pedidos WHERE Pedidos_ID = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPedidos(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public void save(Pedidos entidad) throws SQLException {
        // TODO: Implement save method
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void delete(Integer id) throws SQLException {
        // TODO: Implement delete method
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Integer CountPedidos(Clientes cliente) throws SQLException { 
        String query = "SELECT COUNT(*) AS NumPedidos FROM Pedidos WHERE Clientes_ID = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, cliente.getClientes_ID());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("NumPedidos");
                }
            }
        }
        return 0;
    }

    @Override
    public List<String> listarDetallesClientes() throws SQLException {
        List<String> detallesClientes = new ArrayList<>();
        String query = "SELECT * FROM Clientes";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String detalles = resultSet.getInt("Clientes_ID") + " | " +
                                  resultSet.getString("Nombre") + " | " +
                                  resultSet.getString("Apellido") + " | " +
                                  resultSet.getString("Direccion") + " | " +
                                  resultSet.getString("Contacto");
                detallesClientes.add(detalles);
            }
        }
        return detallesClientes;
    }

    @Override
    public Map<String, Integer> contarPedidosPorCliente() throws SQLException {
        Map<String, Integer> pedidosPorCliente = new HashMap<>();
        String query = "SELECT c.Nombre, COUNT(p.Pedidos_ID) AS NumPedidos " +
                       "FROM Clientes c " +
                       "JOIN Pedidos p ON c.Clientes_ID = p.Cliente_ID " +
                       "GROUP BY c.Nombre";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String clienteNombre = resultSet.getString("Nombre");
                int numPedidos = resultSet.getInt("NumPedidos");
                pedidosPorCliente.put(clienteNombre, numPedidos);
            }
        }
        return pedidosPorCliente;
    }

    @Override
    public List<String> consultarDetallesPedidos() throws SQLException {
        List<String> detallesPedidos = new ArrayList<>();
        String query = "SELECT " +
                            "Pedidos.Pedidos_ID, " +
                            "Clientes.Nombre AS Nombre_Cliente, " +
                            "Clientes.Apellido AS Apellido_Cliente, " +
                            "Clientes.Direccion AS Direccion_Cliente, " +
                            "Clientes.Contacto AS Contacto_Cliente, " +
                            "Pedidos.Fecha_Pedido, " +
                            "Pedidos.Estado AS Estado_Pedido, " +
                            "Pedidos.Metodo_Pago, " +
                            "Pedidos.Precio_Total, " +
                            "Entregas.Fecha_Entrega, " +
                            "Entregas.Estado AS Estado_Entrega, " +
                            "Entregas.Direccion AS Direccion_Entrega " +
                       "FROM " +
                            "Pedidos " +
                       "INNER JOIN " +
                            "Clientes ON Pedidos.Cliente_ID = Clientes.Clientes_ID " +
                       "INNER JOIN " +
                            "Entregas ON Pedidos.Pedidos_ID = Entregas.Pedido_ID";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String detallesPedido = resultSet.getInt("Pedidos_ID") + " | " +
                                        resultSet.getString("Nombre_Cliente") + " " +
                                        resultSet.getString("Apellido_Cliente") + " | " +
                                        resultSet.getString("Direccion_Cliente") + " | " +
                                        resultSet.getString("Contacto_Cliente") + " | " +
                                        resultSet.getTimestamp("Fecha_Pedido") + " | " +
                                        resultSet.getBoolean("Estado_Pedido") + " | " +
                                        resultSet.getString("Metodo_Pago") + " | " +
                                        resultSet.getDouble("Precio_Total") + " | " +
                                        resultSet.getTimestamp("Fecha_Entrega") + " | " +
                                        resultSet.getBoolean("Estado_Entrega") + " | " +
                                        resultSet.getString("Direccion_Entrega");
                detallesPedidos.add(detallesPedido);
            }
        }
        return detallesPedidos;
    }

    @Override
    public List<Productos> mostrarProductosNoPedidos() throws SQLException {
        List<Productos> productosNoPedidos = new ArrayList<>();
        String query = "SELECT * FROM Productos WHERE Productos_ID NOT IN (SELECT DISTINCT Pedido_ID FROM Entregas)";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Productos producto = new Productos(
                        resultSet.getInt("Productos_ID"),
                        resultSet.getString("Nombre_Producto"),
                        resultSet.getString("Descripcion"),
                        resultSet.getDouble("Precio"),
                        resultSet.getInt("Stock_Disponibles") == 1
                );
                productosNoPedidos.add(producto);
            }
        }
        return productosNoPedidos;
    }

    // Helper method to map ResultSet to Pedidos object
    private Pedidos mapResultSetToPedidos(ResultSet resultSet) throws SQLException {
        Pedidos pedido = new Pedidos();
        pedido.setPedidos_ID(resultSet.getInt("Pedidos_ID"));
        pedido.setProducto_ID(getProductosById(resultSet.getInt("Producto_ID")));
        pedido.setCliente_ID(getClientesById(resultSet.getInt("Cliente_ID")));
        pedido.setFechaPedido(resultSet.getTimestamp("Fecha_Pedido").toLocalDateTime());
        pedido.setEstado(resultSet.getBoolean("Estado"));
        pedido.setPrecio_Total(resultSet.getDouble("Precio_Total"));
        return pedido;
    }

    // Helper method to retrieve Productos by ID
    private Productos getProductosById(int id) throws SQLException {
        // Implement this method to retrieve Productos by ID from the database
        // Return a Productos object based on the provided ID
        return null;
    }

    // Helper method to retrieve Clientes by ID
    private Clientes getClientesById(int id) throws SQLException {
        // Implement this method to retrieve Clientes by ID from the database
        // Return a Clientes object based on the provided ID
        return null;
    }
}

