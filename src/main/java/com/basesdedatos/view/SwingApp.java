package com.basesdedatos.view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.List;
import java.util.jar.JarEntry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.basesdedatos.model.Actor;
import com.basesdedatos.repository.ActorRepository;
import com.basesdedatos.repository.Repository;
import com.mysql.cj.xdevapi.JsonArray;

public class SwingApp extends JFrame{
    private final Repository<Actor> ActorRepository;
    private final JTable ActorTable;

    public SwingApp(){
        setTitle("Gestion Sakila");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,300);

        ActorTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(ActorTable);
        add(scrollPane,BorderLayout.CENTER);

        JButton addButton = new JButton("Agregar");
        JButton updateButton = new JButton("Actualizar");
        JButton deleteButton = new JButton("Borrar");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel,BorderLayout.SOUTH);

        ActorRepository = new ActorRepository();

        listActors();

        addButton.addActionListener(e-> {
            try {
               addActor();
            } catch (Exception ex) {
                ex.printStackTrace(); 
            }
        });

        updateButton.addActionListener(e-> {
            try {
                actualizarActor();
            } catch (Exception ex) {
                ex.printStackTrace(); 
            }
        });

        deleteButton.addActionListener(e-> {
            try {
                eliminarEmpleado();
            } catch (Exception ex) {
                ex.printStackTrace(); 
            }
        });

    }

    private void addActor() throws SQLException {
        JTextField first_name = new JTextField();
        JTextField last_name = new JTextField();

        Object[] fields= {
            "name", first_name,
            "lastname", last_name,
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "add actor", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {

            Actor actor = new Actor();
            actor.setFirst_name(first_name.getText());
            actor.setLast_name(last_name.getText());

            ActorRepository.save(actor);

            listActors();

            JOptionPane.showMessageDialog(this, "Empleado agregado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    
    private void actualizarActor() {
        String actorIDSTR = JOptionPane.showInputDialog(this, "Ingrese el ID del actor a actualizar:", "Actualizar Actor", JOptionPane.QUESTION_MESSAGE);
        if (actorIDSTR != null) {
            try {
                int actorID = Integer.parseInt(actorIDSTR);

                Actor actor = ActorRepository.getById(actorID);

                if (actor != null) {
                    JTextField nombreField = new JTextField(actor.getFirst_name());
                    JTextField apellidoField = new JTextField(actor.getLast_name());
                
                    Object[] fields = {
                            "Name:", nombreField,
                            "Last_name:", apellidoField,
                    };

                    int confirmResult = JOptionPane.showConfirmDialog(this, fields, "Actualizar Actor", JOptionPane.OK_CANCEL_OPTION);
                    if (confirmResult == JOptionPane.OK_OPTION) {
                        actor.setFirst_name(nombreField.getText());
                        actor.setLast_name(apellidoField.getText());
                        ActorRepository.save(actor);

                        listActors();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún empleado con el ID especificado", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido para el ID", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al obtener los datos del empleado de la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminarEmpleado() {
        String empleadoIdStr = JOptionPane.showInputDialog(this, "Ingrese el ID a eliminar:", "Eliminar Actor", JOptionPane.QUESTION_MESSAGE);
        if (empleadoIdStr != null) {
            try {
                int empleadoId = Integer.parseInt(empleadoIdStr);

                int confirmResult = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el actor?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirmResult == JOptionPane.YES_OPTION) {
                    ActorRepository.delete(empleadoId);

                    listActors();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Ingrese un valor para el ID del actor", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void listActors() {
        try {
            List<Actor> actors = ActorRepository.findAll();

            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Primer Nombre");

            for (Actor actor : actors) {
                Object[] dataRow = {
                    actor.getActor_id(),
                    actor.getFirst_name()
                };
                tableModel.addRow(dataRow);
            }
            ActorTable.setModel(tableModel);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,"Error getAll","Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

