package com.basesdedatos.repository;

import java.sql.SQLException;
import java.util.List;


public interface Repository2<T> {
    Integer CountClientes(Integer id) throws SQLException;

  
}
