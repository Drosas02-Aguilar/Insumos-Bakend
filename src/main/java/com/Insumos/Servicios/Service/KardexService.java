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
public class KardexService implements IKardex {

    @Autowired
    private EntityManager entityManager;

    private String ToString(Object o) {
        return o == null ? "" : o.toString();
    }

    private BigDecimal ToBigDecimal(Object o) {
        if (o == null) {
            return BigDecimal.ZERO;
        }
        if (o instanceof BigDecimal) {
            return (BigDecimal) o;
        }
        return new BigDecimal(o.toString());
    }

    private KardexResultado MapKaderdex(Object[] row) {
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
        for (Object row : query.getResultList()) {
            contratos.add(ToString(row));
        }
        return contratos;
    }

    @Override
    public String getUsuarioPorContrato(String clavecontrato) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("UGTP_SP_USUARIO_POR_CONTRATO");
        query.registerStoredProcedureParameter("p_clave_contrato", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);
        query.setParameter("p_clave_contrato", clavecontrato);
        query.execute();

        List<?> rows = query.getResultList();
        return rows.isEmpty() ? null : ToString(rows.get(0));

    }

    @Override
    public List<KardexResultado> getinfoPorNodoRecepcion(String clavenodo) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("UGTP_SP_INFO_NODO_RECEPCION");
        query.registerStoredProcedureParameter("p_clave_nod", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);
        query.setParameter("p_clave_nodo", clavenodo);
        query.execute();

        List<KardexResultado> result = new ArrayList<>();
        for (Object row : query.getResultList()) {
            result.add(MapKaderdex((Object[]) row));
        }
        return result;
    }

    @Override
    public List<KardexResultado> getInfoPorNodosEntrega(String clave1, String clave2, String clave3) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("UGTP_SP_INFO_NODOS_ENTREGA");
        query.registerStoredProcedureParameter("p_clave1", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_clave2", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_clave3", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);
        query.setParameter("p_clave1", clave1);
        query.setParameter("p_clave2", clave2);
        query.setParameter("p_clave3", clave3);
        query.execute();

        List<KardexResultado> result = new ArrayList<>();
        for (Object row : query.getResultList()) {
            Object[] r = (Object[]) row;
            KardexResultado kardex = new KardexResultado();
            kardex.setClaveContrato(ToString(r[0]));
            kardex.setUsuario(ToString(r[1]));
            kardex.setNodoRecepcion(ToString(r[2]));
            kardex.setDescNodoRecepcion(ToString(r[3]));
            kardex.setNodoEntrega(ToString(r[4]));
            kardex.setDescNodoEntrega(ToString(r[5]));
            result.add(kardex);

        }
        return result;
    }

    @Override
    public List<NodoComercial> getNodosRecepcion() {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("UGTP_SP_NODOS_RECEPCION");
        query.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);
        query.execute();

        List<NodoComercial> result = new ArrayList<>();
        for (Object row : query.getResultList()) {
            Object[] r = (Object[]) row;
            NodoComercial nodoComercial = new NodoComercial();
            nodoComercial.setClavenodo(ToString(r[0]));
            nodoComercial.setDescripcion(ToString(r[1]));
            result.add(nodoComercial);
        }
        return result;
    }

    @Override
    public List<NodoComercial> getNodosEntrega() {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UGTP_SP_NODOS_ENTREGA");
        query.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);
        query.execute();

        List<NodoComercial> result = new ArrayList<>();
        for (Object row : query.getResultList()) {
            Object[] r = (Object[]) row;

            NodoComercial nodoComercial = new NodoComercial();
            nodoComercial.setClavenodo(ToString(r[0]));
            nodoComercial.setDescripcion(ToString(r[1]));
            result.add(nodoComercial);

        }
        return result;
    }

    @Override
    public List<KardexResultado> getInfoPorZonaInyeccion(String clavezona) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UGTP_SP_INFO_ZONA_INYECCION");
        query.registerStoredProcedureParameter("p_clave_zona", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);
        query.setParameter("p_clave_zona", clavezona);
        query.execute();

        List<KardexResultado> result = new ArrayList<>();
        for (Object row : query.getResultList()) {
            result.add(MapKaderdex((Object[]) row));
        }
        return result;
    }

    @Override
    public List<KardexResultado> getInfoPorZonasExtraccion(String zona1, String zona2, String zona3) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UGTP_SP_INFO_ZONAS_EXTRACCION");
        query.registerStoredProcedureParameter("p_zona1", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_zona2", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_zona3", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);
        query.setParameter("p_zona1", zona1);
        query.setParameter("p_zona2", zona2);
        query.setParameter("p_zona3", zona3);
        query.execute();

        List<KardexResultado> result = new ArrayList<>();
        for (Object row : query.getResultList()) {
            result.add(MapKaderdex((Object[]) row));
        }
        return result;

    }

    @Override
    public List<KardexResultado> getInfoPorContratos(String c1, String c2, String c3, String c4) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UGTP_SP_INFO_CONTRATOS");
        query.registerStoredProcedureParameter("p_contrato1", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_contrato2", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_contrato3", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_contrato4", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);
        query.setParameter("p_contrato1", c1);
        query.setParameter("p_contrato2", c2);
        query.setParameter("p_contrato3", c3);
        query.setParameter("p_contrato4", c4);
        query.execute();

        List<KardexResultado> result = new ArrayList<>();
        for (Object row : query.getResultList()) {
            result.add(MapKaderdex((Object[]) row));
        }
        return result;
    }

    @Override
    public BigDecimal getTotalFacturarUsuario(String nombreusuario, Integer anio, Integer mes) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UGTP_SP_TOTAL_FACTURAR_USUARIO");

        query.registerStoredProcedureParameter("p_nombre_usuario", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_anio", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_mes", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_total", BigDecimal.class, ParameterMode.OUT);
        query.setParameter("p_nombre_usuario", nombreusuario);
        query.setParameter("p_anio", anio);
        query.setParameter("p_mes", mes);
        query.execute();

        Object valor = query.getOutputParameterValue("p_total");
        return ToBigDecimal(valor);

    }

    @Override
    public BigDecimal getPromedioNombreRecepcionUsuario(String nombreusuario, Integer anio, Integer mes) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UGTP_SP_PROMEDIO_NOM_RECEPCION");
        query.registerStoredProcedureParameter("p_nombre_usuario", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_anio", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_mes", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_promedio", BigDecimal.class, ParameterMode.OUT);
        query.setParameter("p_nombre_usuario", nombreusuario);
        query.setParameter("p_anio", anio);
        query.setParameter("p_mes", mes);
        query.execute();

        Object valor = query.getOutputParameterValue("p_promedio");
        return ToBigDecimal(valor);

    }

}
