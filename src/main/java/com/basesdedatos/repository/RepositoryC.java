package com.basesdedatos.repository;

import java.sql.SQLException;
import java.util.List;


public interface RepositoryC<T> {
    Integer CountClientes(Integer id) throws SQLException;

  
}
