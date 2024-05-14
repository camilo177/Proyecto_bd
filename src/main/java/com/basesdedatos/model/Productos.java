package com.basesdedatos.model;

public class Productos {
    
    private int producto_ID;
    private String nombreProducto;
    private String descripcion;
    private double precio;
    private boolean stock_Disponible;

    public Productos() {
    }

    public Productos(int producto_ID, String nombreProducto, String descripcion, double precio, boolean stock_Disponible) {
        this.producto_ID = producto_ID;
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock_Disponible = stock_Disponible;
    }

    public int getProducto_ID() {
        return producto_ID;
    }

    public void setProducto_ID(int producto_ID) {
        this.producto_ID = producto_ID;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isStock_Disponible() {
        return stock_Disponible;
    }

    public void setStock_Disponible(boolean stock_Disponible) {
        this.stock_Disponible = stock_Disponible;
    }
    
}
