package com.Insumos.Servicios.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "UGTP_TBL_NODO_COMERCIAL")
public class NodoComercial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nodo")
    private int idnodo;
    
    @Column(name="clave_nodo", nullable =  false, unique =  true)
    private String clavenodo;
    
    @Column(name = "descripcion", nullable = false, unique = true)
    private String descripcion;

    public int getIdnodo() {
        return idnodo;
    }

    public void setIdnodo(int idnodo) {
        this.idnodo = idnodo;
    }

    public String getClavenodo() {
        return clavenodo;
    }

    public void setClavenodo(String clavenodo) {
        this.clavenodo = clavenodo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
            
}
