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
import com.basesdedatos.model.Productos;

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
        String sql = "SELECT * FROM productos WHERE Productos_ID = ?";
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
        if (producto.getProductos_ID() == null) {
            // Insert a new record
            String sql = "INSERT INTO productos (Nombre_Producto, Descripcion, Precio, Stock_Disponibles) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
                statement.setString(1, producto.getNombre_Producto());
                statement.setString(2, producto.getDescripcion());
                statement.setDouble(3, producto.getPrecio());
                statement.setBoolean(4, producto.isStock_Disponible());
                statement.executeUpdate();
            }
        } else {
            // Update an existing record
            String sql = "UPDATE productos SET Nombre_Producto = ?, Descripcion = ?, Precio = ?, Stock_Disponibles = ? WHERE Productos_ID = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
                statement.setString(1, producto.getNombre_Producto());
                statement.setString(2, producto.getDescripcion());
                statement.setDouble(3, producto.getPrecio());
                statement.setBoolean(4, producto.isStock_Disponible());
                statement.setInt(5, producto.getProductos_ID());
                statement.executeUpdate();
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM productos WHERE Productos_ID = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    private Productos createProducto(ResultSet resultSet) throws SQLException {
        Productos producto = new Productos();
        producto.setProductos_ID(resultSet.getInt("Productos_ID"));
        producto.setNombre_Producto(resultSet.getString("Nombre_Producto"));
        producto.setDescripcion(resultSet.getString("Descripcion"));
        producto.setPrecio(resultSet.getDouble("Precio"));
        producto.setStock_Disponible(resultSet.getBoolean("Stock_Disponibles"));
        return producto;
    }

    @Override
    public List<String> listarStockProductosDisponibles() throws SQLException {
        List<String> stockDisponible = new ArrayList<>();
        String sql = "SELECT Nombre_Producto FROM productos WHERE Stock_Disponibles = true";
        try (PreparedStatement statement = getConnection().prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                stockDisponible.add(resultSet.getString("Nombre_Producto"));
            }
        }
        return stockDisponible;
    }

    @Override
    public List<String> productosPrecioSuperior(double precio) throws SQLException {
        List<String> productosSuperiores = new ArrayList<>();
        String sql = "SELECT Nombre_Producto FROM productos WHERE Precio > ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setDouble(1, precio);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    productosSuperiores.add(resultSet.getString("Nombre_Producto"));
                }
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