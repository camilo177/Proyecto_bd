package com.basesdedatos.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.basesdedatos.config.DatabaseConnection;
import com.basesdedatos.model.Clientes;

public class ClienteRepository implements Repository<Clientes>, RepositoryC<Clientes> {

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance();
    }

    @Override
    public List<Clientes> findAll() throws SQLException {
        List<Clientes> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Statement myStat = getConnection().createStatement()) {
            ResultSet myResultSet = myStat.executeQuery(sql);
            while (myResultSet.next()) {
                Clientes cliente = createCliente(myResultSet);
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    private Clientes createCliente(ResultSet myResult) throws SQLException {
        Clientes cliente = new Clientes();
        cliente.setClientes_ID(myResult.getInt("Clientes_ID"));
        cliente.setNombre(myResult.getString("Nombre"));
        cliente.setApellido(myResult.getString("Apellido"));
        cliente.setDireccion(myResult.getString("Direccion"));
        cliente.setContacto(myResult.getString("Contacto"));
        return cliente;
    }

    @Override
    public Clientes getById(Integer id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE clientes_ID = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createCliente(resultSet);
            }
        }
        return null;
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE clientes_ID = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void save(Clientes cliente) throws SQLException {
        if (cliente.getClientes_ID() == null) {
            String sql = "INSERT INTO clientes (nombre, apellido, direccion, contacto) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
                statement.setString(1, cliente.getNombre());
                statement.setString(2, cliente.getApellido());
                statement.setString(3, cliente.getDireccion());
                statement.setString(4, cliente.getContacto());
                statement.executeUpdate();
            }
        } else {
            // Update an existing record
            String sql = "UPDATE clientes SET nombre = ?, apellido = ?, direccion = ?, contacto = ? WHERE clientes_ID = ?";
            try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
                statement.setString(1, cliente.getNombre());
                statement.setString(2, cliente.getApellido());
                statement.setString(3, cliente.getDireccion());
                statement.setString(4, cliente.getContacto());
                statement.setInt(5, cliente.getClientes_ID());
                statement.executeUpdate();
            }
        }
    }

    @Override
    public Integer CountClientes() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM clientes";
        try (Statement statement = getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }
        return 0; // Return 0 if no rows found
    }
}
