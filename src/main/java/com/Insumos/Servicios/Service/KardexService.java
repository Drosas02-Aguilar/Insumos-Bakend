package com.Insumos.Servicios.Service;

import com.Insumos.Servicios.DAO.IKardex;
import com.Insumos.Servicios.DTO.KardexResultado;
import com.Insumos.Servicios.Entity.NodoComercial;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class KardexService implements IKardex{

    @Autowired
    private EntityManager entityManager;

    private String ToString(Object o) {
        return o == null ? "" : o.toString();
    }

     private BigDecimal ToBigDecimal(Object o) {
        if (o == null) return BigDecimal.ZERO;
        if (o instanceof BigDecimal) return (BigDecimal) o;
        return new BigDecimal(o.toString());
    }
    
    
    private KardexResultado MapKaderdex(Object[] row){
        KardexResultado kardexResultado = new KardexResultado();
         kardexResultado.setFecha(row[0] instanceof java.sql.Date
                ? new Date(((java.sql.Date) row[0]).getTime()) : (Date) row[0]);
        kardexResultado.setClaveContrato(ToString(row[1]));
        kardexResultado.setUsuario(ToString(row[2]));
        kardexResultado.setNodoRecepcion(ToString(row[3]));
        kardexResultado.setDescNodoRecepcion(ToString(row[4]));
        kardexResultado.setNodoEntrega(ToString(row[5]));
        kardexResultado.setDescNodoEntrega(ToString(row[6]));
        kardexResultado.setZonaInyeccion(ToString(row[7]));
        kardexResultado.setZonaExtraccion(ToString(row[8]));
        kardexResultado.setQtyNomRecepcion(ToBigDecimal(row[9]));
        kardexResultado.setQtyAsigRecepcion(ToBigDecimal(row[10]));
        kardexResultado.setQtyNomEntrega(ToBigDecimal(row[11]));
        kardexResultado.setQtyAsigEntrega(ToBigDecimal(row[12]));
        kardexResultado.setGasExceso(ToBigDecimal(row[13]));
        kardexResultado.setTarifaExcesoFirme(ToBigDecimal(row[14]));
        kardexResultado.setTarifaUsoInterrumpible(ToBigDecimal(row[15]));
        kardexResultado.setCargoUso(ToBigDecimal(row[16]));
        kardexResultado.setCargoGasExceso(ToBigDecimal(row[17]));
        kardexResultado.setTotalFacturar(ToBigDecimal(row[18]));
        return kardexResultado;
    }

    @Override
    public List<String> getContratosPorUsuario(String nombreusuario) {

 StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("UGTP_SP_CONTRATOS_POR USUARIO");
               query.registerStoredProcedureParameter("p_nombre_usuario", String.class, ParameterMode.IN);
               query.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);
               query.setParameter("p_nombre_usuario", nombreusuario);
               query.execute();
               
        List<String> contratos = new ArrayList<>();
        for(Object row : query.getResultList()){
            contratos.add(ToString(row));
        }
    return contratos;
    }

    

    @Override
    public String getUsuarioPorContrato(String clavecontrato) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<KardexResultado> getinfoPorNodoRecepcion(String calvenodo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<KardexResultado> getInfoPorNodosEntrega(String clave1, String clave2, String clave3) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<NodoComercial> getNodosRecepcion() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<NodoComercial> getNodosEntrga() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<KardexResultado> getInfoPorZonaInyeccion(String clavezona) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<KardexResultado> getInfoPorZonasExtraccion(String zona1, String zona2, String zona3) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<KardexResultado> getInfoPorContratos(String c1, String c2, String c3, String c4) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal getTotalFacturarUsuario(String nombreusuario, Integer anio, Integer mes) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal getPromedioNombreRecepcionUsuario(String nombreusuario, Integer anio, Integer mes) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
   
    

}
