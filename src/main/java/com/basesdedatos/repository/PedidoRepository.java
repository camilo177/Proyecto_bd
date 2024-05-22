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
        String query = "SELECT * FROM pedidos";
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
    public void save(Pedidos pedido) throws SQLException {
        if (pedido.getPedidos_ID() == null) {
            String sql = "INSERT INTO Pedidos (Productos_ID, Cliente_ID, Fecha_Pedido, Estado, Precio_Total) VALUES (?, ?, ?, ?, ?)";
            try (Connection connection = getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, pedido.getCliente_ID().getClientes_ID());
                preparedStatement.setTimestamp(2, java.sql.Timestamp.valueOf(pedido.getFechaPedido()));
                preparedStatement.setBoolean(3, pedido.isEstado());
                preparedStatement.setDouble(4, pedido.getPrecio_Total());
                preparedStatement.setInt(5, pedido.getProducto_ID().getProductos_ID());
                preparedStatement.executeUpdate();
            }
        }
    }


    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM Pedidos WHERE Pedidos_ID = ?";
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    ////////////////////////////////////////////////////////
    // Methods from RepositoryPe //
    ////////////////////////////////////////////////////////

    @Override
    public Integer CountPedidos(Clientes cliente) throws SQLException { 
        String query = "SELECT COUNT(*) AS NumPedidos FROM Pedidos WHERE Cliente_ID = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, cliente.getClientes_ID());
             ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("NumPedidos");
                }
            
        }
        return 0;
    }

    @Override
    public List<Clientes> listarDetallesClientes() throws SQLException {
        List<Clientes> detallesClientes = new ArrayList<>();
        String query = "SELECT * FROM clientes WHERE pedidos > 0";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Clientes cliente = mapResultSetToClientes(resultSet);
                detallesClientes.add(cliente);
            }
        }
        return detallesClientes;
    }

    @Override
public Map<Clientes, Integer> contarPedidosPorCliente() throws SQLException {
    Map<Clientes, Integer> pedidosPorCliente = new HashMap<>();
    String query = "SELECT c.Clientes_ID, c.Nombre, c.Apellido, COUNT(p.Pedidos_ID) AS NumPedidos " +
                   "FROM Clientes c " +
                   "JOIN Pedidos p ON c.Clientes_ID = p.Cliente_ID " +
                   "GROUP BY c.Clientes_ID, c.Nombre, c.Apellido";
    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(query);
         ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
            Clientes cliente = new Clientes();
            cliente.setClientes_ID(resultSet.getInt("Clientes_ID"));
            cliente.setNombre(resultSet.getString("Nombre"));
            cliente.setApellido(resultSet.getString("Apellido"));
            int numPedidos = resultSet.getInt("NumPedidos");
            pedidosPorCliente.put(cliente, numPedidos);
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


    // Helper method to map ResultSet to Clientes object
    private Clientes mapResultSetToClientes(ResultSet resultSet) throws SQLException {
        Clientes cliente = new Clientes();
        cliente.setNombre(resultSet.getString("Nombre"));
        cliente.setApellido(resultSet.getString("Apellido"));
        cliente.setDireccion(resultSet.getString("Direccion"));
        cliente.setContacto(resultSet.getString("Contacto"));
        return cliente;
    }

    // Helper method to map ResultSet to Pedidos object
    private Pedidos mapResultSetToPedidos(ResultSet resultSet) throws SQLException {
        Pedidos pedido = new Pedidos();
        pedido.setPedidos_ID(resultSet.getInt("Pedidos_ID"));
        pedido.setFechaPedido(resultSet.getTimestamp("Fecha_Pedido").toLocalDateTime());
        pedido.setEstado(resultSet.getBoolean("Estado"));
        pedido.setPrecio_Total(resultSet.getDouble("Precio_Total"));
        return pedido;
    }

    private Productos getProductosById(Integer id) throws SQLException {
        String query = "SELECT * FROM Productos WHERE Productos_ID = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Productos(
                            resultSet.getInt("Productos_ID"),
                            resultSet.getString("Nombre_Producto"),
                            resultSet.getString("Descripcion"),
                            resultSet.getDouble("Precio"),
                            resultSet.getInt("Stock_Disponibles") == 1
                    );
                }
            }
        }
        return null;
    }

    private Clientes getClientesById(Integer id) throws SQLException {
        String query = "SELECT * FROM Clientes WHERE Clientes_ID = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToClientes(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public List<String> ListarClientesconPedidos() throws SQLException {
        List<String> clientesConPedidos = new ArrayList<>();
        String query = "SELECT DISTINCT c.Clientes_ID, c.Nombre, c.Apellido, c.Direccion, c.Contacto " +
                    "FROM clientes c " +
                    "JOIN pedidos p ON c.Clientes_ID = p.Cliente_ID";
        try (Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String clienteInfo = resultSet.getInt("Clientes_ID") + " | " +
                                    resultSet.getString("Nombre") + " " +
                                    resultSet.getString("Apellido") + " | " +
                                    resultSet.getString("Direccion") + " | " +
                                    resultSet.getString("Contacto");
                clientesConPedidos.add(clienteInfo);
            }
        }
        return clientesConPedidos;
    }

    @Override
    public Double sumarTotalPedidos() throws SQLException {
        String query = "SELECT SUM(Precio_Total) AS TotalSuma FROM Pedidos";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getDouble("TotalSuma");
            }
        }
        return 0.0;
    }

}

