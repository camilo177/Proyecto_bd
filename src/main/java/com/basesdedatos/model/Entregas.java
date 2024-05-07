package com.basesdedatos.model;

import java.time.LocalDateTime;

public class Entregas {
    private Integer Entregas_ID;
    private String Direccion;
    private Boolean Estado;
    private Pedidos Pedido_ID;   
    private LocalDateTime Fecha_Entrega;

    public Entregas() {
    }
    public Entregas(Integer entregas_ID, String direccion, Boolean estado, Pedidos pedido_ID, LocalDateTime fecha_Entrega) {
        Entregas_ID = entregas_ID;
        Direccion = direccion;
        Estado = estado;
        Pedido_ID = pedido_ID;
        Fecha_Entrega = fecha_Entrega;
    }

    public Integer getEntregas_ID() {
        return Entregas_ID;
    }
    public String getDireccion() {
        return Direccion;
    }
    public Boolean getEstado() {
        return Estado;
    }
    public Pedidos getPedido_ID() {
        return Pedido_ID;
    }
    public LocalDateTime getFecha_Entrega() {
        return Fecha_Entrega;
    }
    public void setEntregas_ID(Integer entregas_ID) {
        Entregas_ID = entregas_ID;
    }
    public void setDireccion(String direccion) {
        Direccion = direccion;
    }
    public void setEstado(Boolean estado) {
        Estado = estado;
    }
    public void setPedido_ID(Pedidos pedido_ID) {
        Pedido_ID = pedido_ID;
    }
    public void setFecha_Entrega(LocalDateTime fecha_Entrega) {
        Fecha_Entrega = fecha_Entrega;
    } 
    

}


