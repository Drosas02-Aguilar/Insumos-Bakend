package com.Insumos.Servicios.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "UGTP_TBL_ZONA_TARIFA")
public class Zonatarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_zona")
    private int idzona;

    @Column(name = "clave_zona", nullable = false, unique = true)
    private String clavezona;

    public int getIdzona() {
        return idzona;
    }

    public void setIdzona(int idzona) {
        this.idzona = idzona;
    }

    public String getClavezona() {
        return clavezona;
    }

    public void setClavezona(String clavezona) {
        this.clavezona = clavezona;
    }

}
