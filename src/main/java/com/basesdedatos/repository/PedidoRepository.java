package com.basesdedatos.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.basesdedatos.config.DatabaseConnection;
import com.basesdedatos.model.Actor;

public class ActorRepository2 implements Repository<Actor> {

    private Connection geConnection()throws SQLException{
        return DatabaseConnection.getInstance();
    }
    @Override
    public List<Actor> findAll() throws SQLException {
        List<Actor> actors = new ArrayList<>();
        String sql = "Select * from actor";
        try(Statement myStat = geConnection().createStatement())
        {
            ResultSet myResultSet = myStat.executeQuery(sql);
            while (myResultSet.next()) {
                Actor actor = createActor(myResultSet);
                actors.add(actor);
            }
        } 
        
        return actors;
    }

    @Override
    public Actor getById(Integer id) throws SQLException {
        Actor actor = null;
        String sql  = "Select * from actor where actor_id = ?";
        try(PreparedStatement myStat = geConnection().prepareStatement(sql)){
            myStat.setInt(1, id);
            try(ResultSet myRes = myStat.executeQuery()){
                if(myRes.next()){   
                    actor = createActor(myRes);
                }
            }
        }
        return actor;
    }

    @Override
    public void save(Actor actor) throws SQLException {
        String sql;
        if(actor.getActor_id()!= null && actor.getActor_id()>0) {
            sql = "UPDATE actor SET first_name =?, last_name=? WHERE actor_id= ?";
        }else {
            sql = "INSERT INTO actor (first_name, last_name) values(?,?)";
        }

       
        try(PreparedStatement myStat = geConnection().prepareStatement(sql)){
            myStat.setString(1, actor.getFirst_name());
            myStat.setString(2, actor.getLast_name());
            if(actor.getActor_id() != null && actor.getActor_id()>0) {
                myStat.setInt(3, actor.getActor_id());
            }
            myStat.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try(PreparedStatement myStamt = geConnection().prepareStatement("DELETE FROM actor WHERE actor_id=?")){
            myStamt.setInt(1,id);
            myStamt.executeUpdate();
        }
    }

    private Actor createActor(ResultSet myResult)throws SQLException{
        Actor actor = new Actor();
        actor.setActor_id(myResult.getInt("actor_id"));
        actor.setFirst_name(myResult.getString("first_name"));
        actor.setLast_name(myResult.getString("last_name"));
        return actor;
    }
    @Override
    public Integer CountClientes(Integer id) throws SQLException {
        SELECT COUNT(*) AS NumClientes FROM Clientes;

    }
    
}
