package com.basesdedatos.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.basesdedatos.model.Clientes;
import com.basesdedatos.model.Productos;


public interface RepositoryPe<T> {
    Integer CountPedidos(Clientes Nombre) throws SQLException;
    List<String> listarDetallesClientes() throws SQLException;
    Map<String, Integer> contarPedidosPorCliente() throws SQLException;
    List<String> consultarDetallesPedidos() throws SQLException;
    List<Productos> mostrarProductosNoPedidos() throws SQLException;
}