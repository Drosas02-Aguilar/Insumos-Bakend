package com.Insumos.Servicios.DTO;

import java.math.BigDecimal;
import java.util.Date;

public class KardexResultado {

    private Date fecha;
    private String claveContrato;
    private String usuario;
    private String nodoRecepcion;
    private String descNodoRecepcion;
    private String nodoEntrega;
    private String descNodoEntrega;
    private String zonaInyeccion;
    private String zonaExtraccion;
    private BigDecimal qtyNomRecepcion;
    private BigDecimal qtyAsigRecepcion;
    private BigDecimal qtyNomEntrega;
    private BigDecimal qtyAsigEntrega;
    private BigDecimal gasExceso;
    private BigDecimal tarifaExcesoFirme;
    private BigDecimal tarifaUsoInterrumpible;
    private BigDecimal cargoUso;
    private BigDecimal cargoGasExceso;
    private BigDecimal totalFacturar;

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getClaveContrato() {
        return claveContrato;
    }

    public void setClaveContrato(String claveContrato) {
        this.claveContrato = claveContrato;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNodoRecepcion() {
        return nodoRecepcion;
    }

    public void setNodoRecepcion(String nodoRecepcion) {
        this.nodoRecepcion = nodoRecepcion;
    }

    public String getDescNodoRecepcion() {
        return descNodoRecepcion;
    }

    public void setDescNodoRecepcion(String descNodoRecepcion) {
        this.descNodoRecepcion = descNodoRecepcion;
    }

    public String getNodoEntrega() {
        return nodoEntrega;
    }

    public void setNodoEntrega(String nodoEntrega) {
        this.nodoEntrega = nodoEntrega;
    }

    public String getDescNodoEntrega() {
        return descNodoEntrega;
    }

    public void setDescNodoEntrega(String descNodoEntrega) {
        this.descNodoEntrega = descNodoEntrega;
    }

    public String getZonaInyeccion() {
        return zonaInyeccion;
    }

    public void setZonaInyeccion(String zonaInyeccion) {
        this.zonaInyeccion = zonaInyeccion;
    }

    public String getZonaExtraccion() {
        return zonaExtraccion;
    }

    public void setZonaExtraccion(String zonaExtraccion) {
        this.zonaExtraccion = zonaExtraccion;
    }

    public BigDecimal getQtyNomRecepcion() {
        return qtyNomRecepcion;
    }

    public void setQtyNomRecepcion(BigDecimal qtyNomRecepcion) {
        this.qtyNomRecepcion = qtyNomRecepcion;
    }

    public BigDecimal getQtyAsigRecepcion() {
        return qtyAsigRecepcion;
    }

    public void setQtyAsigRecepcion(BigDecimal qtyAsigRecepcion) {
        this.qtyAsigRecepcion = qtyAsigRecepcion;
    }

    public BigDecimal getQtyNomEntrega() {
        return qtyNomEntrega;
    }

    public void setQtyNomEntrega(BigDecimal qtyNomEntrega) {
        this.qtyNomEntrega = qtyNomEntrega;
    }

    public BigDecimal getQtyAsigEntrega() {
        return qtyAsigEntrega;
    }

    public void setQtyAsigEntrega(BigDecimal qtyAsigEntrega) {
        this.qtyAsigEntrega = qtyAsigEntrega;
    }

    public BigDecimal getGasExceso() {
        return gasExceso;
    }

    public void setGasExceso(BigDecimal gasExceso) {
        this.gasExceso = gasExceso;
    }

    public BigDecimal getTarifaExcesoFirme() {
        return tarifaExcesoFirme;
    }

    public void setTarifaExcesoFirme(BigDecimal tarifaExcesoFirme) {
        this.tarifaExcesoFirme = tarifaExcesoFirme;
    }

    public BigDecimal getTarifaUsoInterrumpible() {
        return tarifaUsoInterrumpible;
    }

    public void setTarifaUsoInterrumpible(BigDecimal tarifaUsoInterrumpible) {
        this.tarifaUsoInterrumpible = tarifaUsoInterrumpible;
    }

    public BigDecimal getCargoUso() {
        return cargoUso;
    }

    public void setCargoUso(BigDecimal cargoUso) {
        this.cargoUso = cargoUso;
    }

    public BigDecimal getCargoGasExceso() {
        return cargoGasExceso;
    }

    public void setCargoGasExceso(BigDecimal cargoGasExceso) {
        this.cargoGasExceso = cargoGasExceso;
    }

    public BigDecimal getTotalFacturar() {
        return totalFacturar;
    }

    public void setTotalFacturar(BigDecimal totalFacturar) {
        this.totalFacturar = totalFacturar;
    }
    
    

}
