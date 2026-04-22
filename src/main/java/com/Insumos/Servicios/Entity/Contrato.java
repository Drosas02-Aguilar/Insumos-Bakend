package com.Insumos.Servicios.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="UGTP_TBL_CONTRATO")
public class Contrato {
    
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name="id_contrato")
    private int idcontrato;
    
    @Column(name="clave_contrato", nullable = false, unique = true)
    private String clavecontrato;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id_usuario", nullable = false)
    private Usuario usuario;

    public int getIdcontrato() {
        return idcontrato;
    }

    public void setIdcontrato(int idcontrato) {
        this.idcontrato = idcontrato;
    }

    public String getClavecontrato() {
        return clavecontrato;
    }

    public void setClavecontrato(String clavecontrato) {
        this.clavecontrato = clavecontrato;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    
    
    
    
}
