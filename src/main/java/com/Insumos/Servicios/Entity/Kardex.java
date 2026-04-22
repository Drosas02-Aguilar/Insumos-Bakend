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
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "UGTP_TBL_KARDEX")

public class Kardex {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_kardex")
    private int idkardex;
    @Column(name = "fecha", nullable = false)
    private Date fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_contrato", nullable = false)
    private Contrato contrato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nodo_recepcion", nullable = false)
    private NodoComercial nodorecepcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nodo_entrega", nullable = false)
    private NodoComercial nodoentrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_zona_inyeccion", nullable = false)
    private Zonatarifa zonainyeccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_zona_extraccion", nullable = false)
    private Zonatarifa zonaextraccion;

    @Column(name = "qty_nom_recepcion", precision = 18, scale = 3)
    private BigDecimal qtynombrerecepcion;
    
    @Column(name= "qty_asig_recepcion", precision = 18, scale = 3)
    private BigDecimal qtyasigrecepcion;
    
    @Column(name="qty_nom_entrega", precision = 18, scale = 3)
    private BigDecimal qtynombreentrega;
    
    @Column(name="qty_asig_entrega", precision = 18, scale = 3)
    private BigDecimal qtyasigentrega;
    
    @Column(name="gas_exceso", precision = 18, scale = 3)
    private BigDecimal gasexceso;
    
    @Column(name="tarifa_exceso_firme", precision = 18, scale = 6)
    private BigDecimal tarifaexcesofirme;
    
    @Column(name="tarifa_uso_interrrumpible", precision = 18, scale = 6)
    private BigDecimal tarifausointerrrumpible;
    
    @Column(name="cargo_uso", precision = 18, scale = 2)
    private BigDecimal cargouso;
    
    @Column(name="cargo_gas_exceso", precision = 18, scale = 2)
    private BigDecimal cargogasexceso;
    
    @Column(name="total_facturar",precision = 18,scale = 2)
    private BigDecimal totalfacturar;

    public int getIdkardex() {
        return idkardex;
    }

    public void setIdkardex(int idkardex) {
        this.idkardex = idkardex;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public NodoComercial getNodorecepcion() {
        return nodorecepcion;
    }

    public void setNodorecepcion(NodoComercial nodorecepcion) {
        this.nodorecepcion = nodorecepcion;
    }

    public NodoComercial getNodoentrega() {
        return nodoentrega;
    }

    public void setNodoentrega(NodoComercial nodoentrega) {
        this.nodoentrega = nodoentrega;
    }

    public Zonatarifa getZonainyeccion() {
        return zonainyeccion;
    }

    public void setZonainyeccion(Zonatarifa zonainyeccion) {
        this.zonainyeccion = zonainyeccion;
    }

    public Zonatarifa getZonaextraccion() {
        return zonaextraccion;
    }

    public void setZonaextraccion(Zonatarifa zonaextraccion) {
        this.zonaextraccion = zonaextraccion;
    }

    public BigDecimal getQtynombrerecepcion() {
        return qtynombrerecepcion;
    }

    public void setQtynombrerecepcion(BigDecimal qtynombrerecepcion) {
        this.qtynombrerecepcion = qtynombrerecepcion;
    }

    public BigDecimal getQtyasigrecepcion() {
        return qtyasigrecepcion;
    }

    public void setQtyasigrecepcion(BigDecimal qtyasigrecepcion) {
        this.qtyasigrecepcion = qtyasigrecepcion;
    }

    public BigDecimal getQtynombreentrega() {
        return qtynombreentrega;
    }

    public void setQtynombreentrega(BigDecimal qtynombreentrega) {
        this.qtynombreentrega = qtynombreentrega;
    }

    public BigDecimal getQtyasigentrega() {
        return qtyasigentrega;
    }

    public void setQtyasigentrega(BigDecimal qtyasigentrega) {
        this.qtyasigentrega = qtyasigentrega;
    }

    public BigDecimal getGasexceso() {
        return gasexceso;
    }

    public void setGasexceso(BigDecimal gasexceso) {
        this.gasexceso = gasexceso;
    }

    public BigDecimal getTarifaexcesofirme() {
        return tarifaexcesofirme;
    }

    public void setTarifaexcesofirme(BigDecimal tarifaexcesofirme) {
        this.tarifaexcesofirme = tarifaexcesofirme;
    }

    public BigDecimal getCargouso() {
        return cargouso;
    }

    public void setCargouso(BigDecimal cargouso) {
        this.cargouso = cargouso;
    }

    public BigDecimal getCargogasexceso() {
        return cargogasexceso;
    }

    public void setCargogasexceso(BigDecimal cargogasexceso) {
        this.cargogasexceso = cargogasexceso;
    }

    public BigDecimal getTotalfacturar() {
        return totalfacturar;
    }

    public void setTotalfacturar(BigDecimal totalfacturar) {
        this.totalfacturar = totalfacturar;
    }

    public BigDecimal getTarifausointerrrumpible() {
        return tarifausointerrrumpible;
    }

    public void setTarifausointerrrumpible(BigDecimal tarifausointerrrumpible) {
        this.tarifausointerrrumpible = tarifausointerrrumpible;
    }
    
    
    
    
}
