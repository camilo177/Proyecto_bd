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
import com.basesdedatos.model.Productos;
import com.basesdedatos.model.Pedidos;

public class ProductoRepository implements Repository<Productos>, RepositoryPr {

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance();
    }

    @Override
    public List<Productos> findAll() throws SQLException {
        List<Productos> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Productos producto = createProducto(resultSet);
                productos.add(producto);
            }
        }
        return productos;
    }

    @Override
    public Productos getById(Integer id) throws SQLException {
        String sql = "SELECT * FROM productos WHERE producto_ID = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return createProducto(resultSet);
                }
            }
        }
        return null;
    }

    @Override
    public void save(Productos producto) throws SQLException {
        if (producto.getProducto_ID() == 0) {
            // Insert a new record
            String sql = "INSERT INTO productos (nombreProducto, descripcion, precio, stock_Disponible) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
                statement.setString(1, producto.getNombreProducto());
                statement.setString(2, producto.getDescripcion());
                statement.setDouble(3, producto.getPrecio());
                statement.setBoolean(4, producto.isStock_Disponible());
                statement.executeUpdate();
            }
        } else {
            // Update an existing record
            String sql = "UPDATE productos SET nombreProducto = ?, descripcion = ?, precio = ?, stock_Disponible = ? WHERE producto_ID = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
                statement.setString(1, producto.getNombreProducto());
                statement.setString(2, producto.getDescripcion());
                statement.setDouble(3, producto.getPrecio());
                statement.setBoolean(4, producto.isStock_Disponible());
                statement.setInt(5, producto.getProducto_ID());
                statement.executeUpdate();
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM productos WHERE producto_ID = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Productos createProducto(ResultSet resultSet) throws SQLException {
        Productos producto = new Productos();
        producto.setProducto_ID(resultSet.getInt("producto_ID"));
        producto.setNombreProducto(resultSet.getString("nombreProducto"));
        producto.setDescripcion(resultSet.getString("descripcion"));
        producto.setPrecio(resultSet.getDouble("precio"));
        producto.setStock_Disponible(resultSet.getBoolean("stock_Disponible"));
        return producto;
    }

    @Override
    public List<String> listarStockProductosDisponibles() throws SQLException {
        List<String> stockDisponible = new ArrayList<>();
        String sql = "SELECT nombreProducto FROM productos WHERE stock_Disponible = true";
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                stockDisponible.add(resultSet.getString("nombreProducto"));
            }
        }
        return stockDisponible;
    }

    @Override
    public List<String> productosPrecioSuperior() throws SQLException {
        List<String> productosSuperiores = new ArrayList<>();
        String sql = "SELECT nombreProducto FROM productos WHERE precio > 50";
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                productosSuperiores.add(resultSet.getString("nombreProducto"));
            }
        }
        return productosSuperiores;
    }

    @Override
    public List<String> listarProductosDescripcionPrecio() throws SQLException {
        List<String> productosDescripcionPrecio = new ArrayList<>();
        String sql = "SELECT Nombre_Producto, Descripcion, Precio FROM Productos";
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String producto = resultSet.getString("Nombre_Producto") + " | " +
                        resultSet.getString("Descripcion") + " | " +
                        resultSet.getDouble("Precio");
                productosDescripcionPrecio.add(producto);
            }
        }
        return productosDescripcionPrecio;
    }
}