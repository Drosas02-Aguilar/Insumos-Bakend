package com.Insumos.Servicios.Excel;

import java.math.BigDecimal;
import java.util.Date;

public class FilaExcel {
        private Date    fecha;
    private String  claveContrato;
    private String  nombreUsuario;
    private String  claveNodoRec;
    private String  descNodoRec;
    private String  claveNodoEnt;
    private String  descNodoEnt;
    private String  claveZonaIny;
    private String  claveZonaExt;
    private BigDecimal qtyNomRec;
    private BigDecimal qtyAsigRec;
    private BigDecimal qtyNomEnt;
    private BigDecimal qtyAsigEnt;
    private BigDecimal gasExceso;
    private BigDecimal tarifaExcesoFirme;
    private BigDecimal tarifaUsoInt;
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClaveNodoRec() {
        return claveNodoRec;
    }

    public void setClaveNodoRec(String claveNodoRec) {
        this.claveNodoRec = claveNodoRec;
    }

    public String getDescNodoRec() {
        return descNodoRec;
    }

    public void setDescNodoRec(String descNodoRec) {
        this.descNodoRec = descNodoRec;
    }

    public String getClaveNodoEnt() {
        return claveNodoEnt;
    }

    public void setClaveNodoEnt(String claveNodoEnt) {
        this.claveNodoEnt = claveNodoEnt;
    }

    public String getDescNodoEnt() {
        return descNodoEnt;
    }

    public void setDescNodoEnt(String descNodoEnt) {
        this.descNodoEnt = descNodoEnt;
    }

    public String getClaveZonaIny() {
        return claveZonaIny;
    }

    public void setClaveZonaIny(String claveZonaIny) {
        this.claveZonaIny = claveZonaIny;
    }

    public String getClaveZonaExt() {
        return claveZonaExt;
    }

    public void setClaveZonaExt(String claveZonaExt) {
        this.claveZonaExt = claveZonaExt;
    }

    public BigDecimal getQtyNomRec() {
        return qtyNomRec;
    }

    public void setQtyNomRec(BigDecimal qtyNomRec) {
        this.qtyNomRec = qtyNomRec;
    }

    public BigDecimal getQtyAsigRec() {
        return qtyAsigRec;
    }

    public void setQtyAsigRec(BigDecimal qtyAsigRec) {
        this.qtyAsigRec = qtyAsigRec;
    }

    public BigDecimal getQtyNomEnt() {
        return qtyNomEnt;
    }

    public void setQtyNomEnt(BigDecimal qtyNomEnt) {
        this.qtyNomEnt = qtyNomEnt;
    }

    public BigDecimal getQtyAsigEnt() {
        return qtyAsigEnt;
    }

    public void setQtyAsigEnt(BigDecimal qtyAsigEnt) {
        this.qtyAsigEnt = qtyAsigEnt;
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

    public BigDecimal getTarifaUsoInt() {
        return tarifaUsoInt;
    }

    public void setTarifaUsoInt(BigDecimal tarifaUsoInt) {
        this.tarifaUsoInt = tarifaUsoInt;
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
