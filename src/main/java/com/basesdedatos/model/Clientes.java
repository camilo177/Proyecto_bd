package com.basesdedatos.model;

public class Clientes {
    private Integer Clientes_ID;
    private String Apellido;
    private String Nombre;
    private String Direccion;
    private String Contacto;
    
    public Clientes(){   
    }

    public Clientes(Integer Clientes_ID) {
        this.Clientes_ID = Clientes_ID;
    }

    public Clientes(Integer Clientes_ID, String Apellido, String Nombre, String Direccion, String Contacto) {
        this.Clientes_ID = Clientes_ID;
        this.Apellido = Apellido;
        this.Nombre = Nombre;
        this.Direccion = Direccion;
        this.Contacto = Contacto;
    }


    public Integer getClientes_ID() {
        return Clientes_ID;
    }

    public String getApellido() {
        return Apellido;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getDireccion() {
        return Direccion;
    }

    public String getContacto() {
        return Contacto;
    }

    public void setClientes_ID(Integer clientes_ID) {
        Clientes_ID = clientes_ID;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public void setContacto(String contacto) {
        Contacto = contacto;
    }

    @Override
    public String toString() {
        return "Clientes [Clientes_ID=" + Clientes_ID + ", Apellido=" + Apellido + ", Nombre=" + Nombre + ", Direccion="
                + Direccion + ", Contacto=" + Contacto + "]";
    }

    }
