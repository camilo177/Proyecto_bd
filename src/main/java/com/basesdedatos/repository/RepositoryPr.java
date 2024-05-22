package com.basesdedatos.repository;

import java.sql.SQLException;
import java.util.List;

public interface RepositoryPr {

    List<String> listarStockProductosDisponibles() throws SQLException;
    List<String> productosPrecioSuperior(double precio) throws SQLException;
    List<String> listarProductosDescripcionPrecio() throws SQLException;

}