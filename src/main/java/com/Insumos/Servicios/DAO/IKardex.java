package com.Insumos.Servicios.DAO;

import com.Insumos.Servicios.DTO.KardexResultado;
import com.Insumos.Servicios.Entity.NodoComercial;
import java.math.BigDecimal;
import java.util.List;

public interface IKardex {

    List<String> getContratosPorUsuario(String nombreusuario);

    String getUsuarioPorContrato(String clavecontrato);

    List<KardexResultado> getinfoPorNodoRecepcion(String clavenodo);

    List<KardexResultado> getInfoPorNodosEntrega(String clave1, String clave2, String clave3);

    List<NodoComercial> getNodosRecepcion();

    List<NodoComercial> getNodosEntrega();

    List<KardexResultado> getInfoPorZonaInyeccion(String clavezona);

    List<KardexResultado> getInfoPorZonasExtraccion(String zona1, String zona2, String zona3);

    List<KardexResultado> getInfoPorContratos(String c1, String c2, String c3, String c4);

    BigDecimal getTotalFacturarUsuario(String nombreusuario, Integer anio, Integer mes);

    BigDecimal getPromedioNombreRecepcionUsuario(String nombreusuario, Integer anio, Integer mes);

}
