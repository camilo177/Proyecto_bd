package com.basesdedatos.model;

import java.time.LocalDateTime;

public class Pedidos {
    
    private Integer Pedidos_ID;
    private Productos producto_ID;
    private Clientes Cliente_ID;
    private LocalDateTime fechaPedido;
    private boolean estado;
    private String metodo_Pago;
    private double precio_Total;

    public Pedidos() {
    }

    public Pedidos(int pedidos_ID, Productos producto_ID, Clientes Cliente_ID, LocalDateTime fechaPedido, boolean estado, String metodo_Pago, double precio_Total) {
        this.Pedidos_ID = pedidos_ID;
        this.producto_ID = producto_ID;
        this.Cliente_ID = Cliente_ID;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.metodo_Pago = metodo_Pago;
        this.precio_Total = precio_Total;
    }

    

    public Productos getProducto_ID() {
        return producto_ID;
    }

    public void setProducto_ID(Productos producto_ID) {
        this.producto_ID = producto_ID;
    }

    public Clientes getCliente_ID() {
        return Cliente_ID;
    }

    public void setCliente_ID(Clientes Cliente_ID) {
        this.Cliente_ID = Cliente_ID;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getMetodo_Pago() {
        return metodo_Pago;
    }

    public void setMetodo_Pago(String metodo_Pago) {
        this.metodo_Pago = metodo_Pago;
    }

    public double getPrecio_Total() {
        return precio_Total;
    }

    public void setPrecio_Total(double precio_Total) {
        this.precio_Total = precio_Total;
    }

    public Integer getPedidos_ID() {
        return Pedidos_ID;
    }

    public void setPedidos_ID(Integer pedidos_ID) {
        Pedidos_ID = pedidos_ID;
    }        
    
}