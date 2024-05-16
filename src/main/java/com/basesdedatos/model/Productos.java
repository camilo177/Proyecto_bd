package com.basesdedatos.model;

public class Productos {
    
    private Integer productos_ID;
    private String Nombre_Producto;
    private String Descripcion;
    private double Precio;
    private boolean Stock_Disponible;

    
    
    public Productos(Integer productos_ID, String nombre_Producto, String descripcion, double precio,
            boolean stock_Disponible) {
        this.productos_ID = productos_ID;
        Nombre_Producto = nombre_Producto;
        Descripcion = descripcion;
        Precio = precio;
        Stock_Disponible = stock_Disponible;
    }

    public Productos() {
    }

    public Integer getProductos_ID() {
        return productos_ID;
    }
    public void setProductos_ID(Integer productos_ID) {
        this.productos_ID = productos_ID;
    }
    public String getNombre_Producto() {
        return Nombre_Producto;
    }
    public void setNombre_Producto(String nombre_Producto) {
        Nombre_Producto = nombre_Producto;
    }
    public String getDescripcion() {
        return Descripcion;
    }
    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
    public double getPrecio() {
        return Precio;
    }
    public void setPrecio(double precio) {
        Precio = precio;
    }
    public boolean isStock_Disponible() {
        return Stock_Disponible;
    }
    public void setStock_Disponible(boolean stock_Disponible) {
        Stock_Disponible = stock_Disponible;
    }

    
}
