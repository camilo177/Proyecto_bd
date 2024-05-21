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

    @Override
    public Clientes getById(Integer id) throws SQLException {
        Clientes clientes = null;
        String sql = "SELECT * FROM clientes WHERE Clientes_ID = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return createCliente(resultSet);
            }
        }
        return clientes;
    }

    @Override
    public void save(Clientes cliente) throws SQLException {
       String sql;
       if(cliente.getClientes_ID() != null && cliente.getClientes_ID() > 0){
            sql = "UPDATE clientes SET Nombre = ?, Apellido = ?, Direccion = ?, Contacto = ? WHERE Clientes_ID = ?";
         } else {
            sql = "INSERT INTO clientes (Nombre, Apellido, Direccion, Contacto) VALUES (?, ?, ?, ?)";
         }
        try (PreparedStatement statement = getConnection().prepareStatement(sql)) {
            statement.setString(1, cliente.getNombre());
            statement.setString(2, cliente.getApellido());
            statement.setString(3, cliente.getDireccion());
            statement.setString(4, cliente.getContacto());
            if(cliente.getClientes_ID() != null && cliente.getClientes_ID() > 0){
                statement.setInt(5, cliente.getClientes_ID());
            }
            statement.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

        
    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement("DELETE FROM clientes WHERE Clientes_ID = ?")) {
            statement.setInt(1, id);
            statement.executeUpdate();
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

    private Clientes createCliente(ResultSet myResult) throws SQLException {
        Clientes cliente = new Clientes();
        cliente.setClientes_ID(myResult.getInt("Clientes_ID"));
        cliente.setNombre(myResult.getString("Nombre"));
        cliente.setApellido(myResult.getString("Apellido"));
        cliente.setDireccion(myResult.getString("Direccion"));
        cliente.setContacto(myResult.getString("Contacto"));
        return cliente;
    }

}
