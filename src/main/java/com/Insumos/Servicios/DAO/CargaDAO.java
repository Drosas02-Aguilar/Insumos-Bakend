package com.Insumos.Servicios.DAO;

import com.Insumos.Servicios.Excel.FilaExcel;
import com.Insumos.Servicios.Exception.ServiceResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class CargaDAO {

    @Autowired
    private EntityManager entityManager;

    private String CellString(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING ->
                cell.getStringCellValue().trim();
            case NUMERIC ->
                String.valueOf((long) cell.getNumericCellValue());
            default ->
                "";
        };
    }

    private BigDecimal CellDecimal(Cell cell) {
        if (cell == null) {
            return BigDecimal.ZERO;
        }
        return switch (cell.getCellType()) {
            case NUMERIC ->
                BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING -> {
                try {
                    yield new BigDecimal(cell.getStringCellValue().trim());
                } catch (Exception e) {
                    yield BigDecimal.ZERO;
                }
            }
            default ->
                BigDecimal.ZERO;
        };
    }

    public List<FilaExcel> LeerExcel(MultipartFile archivo) throws Exception {
        List<FilaExcel> filas = new ArrayList<>();

        Workbook workbook = new XSSFWorkbook(archivo.getInputStream());   
        Sheet sheet = workbook.getSheetAt(0);
        
        
        

        for (int i = 2; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            Cell celdaFecha = row.getCell(1);
            Cell celdaContrato = row.getCell(2);
            Cell celdaUsuario = row.getCell(3);

            if (celdaFecha == null || celdaFecha.getCellType() == CellType.BLANK) {
                continue;
            }
            if (celdaContrato == null || celdaUsuario == null) {
                continue;
            }
            if (CellString(celdaContrato).isEmpty() || CellString(celdaUsuario).isEmpty()) {
                continue;
            }

            FilaExcel fila = new FilaExcel();
            fila.setFecha(celdaFecha.getDateCellValue());
            fila.setClaveContrato(CellString(celdaContrato));
            fila.setNombreUsuario(CellString(celdaUsuario));
            fila.setClaveNodoRec(CellString(row.getCell(4)));
            fila.setDescNodoRec(CellString(row.getCell(5)));
            fila.setClaveNodoEnt(CellString(row.getCell(6)));
            fila.setDescNodoEnt(CellString(row.getCell(7)));
            fila.setClaveZonaIny(CellString(row.getCell(8)));
            fila.setClaveZonaExt(CellString(row.getCell(9)));
            fila.setQtyNomRec(CellDecimal(row.getCell(10)));
            fila.setQtyAsigRec(CellDecimal(row.getCell(11)));
            fila.setQtyNomEnt(CellDecimal(row.getCell(12)));
            fila.setQtyAsigEnt(CellDecimal(row.getCell(13)));
            fila.setGasExceso(CellDecimal(row.getCell(14)));
            fila.setTarifaExcesoFirme(CellDecimal(row.getCell(15)));
            fila.setTarifaUsoInt(CellDecimal(row.getCell(16)));
            fila.setCargoUso(CellDecimal(row.getCell(17)));
            fila.setCargoGasExceso(CellDecimal(row.getCell(18)));
            fila.setTotalFacturar(CellDecimal(row.getCell(19)));
            filas.add(fila);
        }

        workbook.close();
        return filas;
    }

    public Set<String> ExtraerZonas(List<FilaExcel> filas) {
        Set<String> zonas = new LinkedHashSet<>();

        for (FilaExcel fila : filas) {
            zonas.add(fila.getClaveZonaIny());
            zonas.add(fila.getClaveZonaExt());
        }

        return zonas;
    }

    public Map<String, String> ExtraerNodos(List<FilaExcel> filas) {
        Map<String, String> nodos = new LinkedHashMap<>();
        for (FilaExcel fila : filas) {
            nodos.putIfAbsent(fila.getClaveNodoRec(), fila.getDescNodoRec());
            nodos.putIfAbsent(fila.getClaveNodoEnt(), fila.getClaveNodoEnt());

        }
        return nodos;
    }

    public Set<String> ExtraerUsuarios(List<FilaExcel> filas) {
        Set<String> usuarios = new LinkedHashSet<>();
        for (FilaExcel fila : filas) {
            usuarios.add(fila.getNombreUsuario());
        }

        return usuarios;

    }

    public Map<String, String> ExtraerContratos(List<FilaExcel> filas) {
        Map<String, String> contratos = new LinkedHashMap<>();
        for (FilaExcel fila : filas) {
            contratos.putIfAbsent(fila.getClaveContrato(), fila.getNombreUsuario());
        }
        return contratos;
    }

    @Transactional
    public void InsertarZonas(Set<String> zonas) {
        for (String clave : zonas) {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UGTP_SP_CARGA_ZONA");
            query.registerStoredProcedureParameter("p_clave", String.class, ParameterMode.IN);
            query.setParameter("p_clave", clave);
            query.execute();

        }

    }

    @Transactional
    public void InsertarNodos(Map<String, String> nodos) {
        for (Map.Entry<String, String> entry : nodos.entrySet()) {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UGTP_SP_CARGA_NODO");
            query.registerStoredProcedureParameter("p_clave", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_desc", String.class, ParameterMode.IN);
            query.setParameter("p_clave", entry.getKey());
            query.setParameter("p_desc", entry.getValue());
            query.execute();
        }
    }

    @Transactional
    public void InsertarUsuario(Set<String> usuarios) {
        for (String nombre : usuarios) {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UGTP_SP_CARGA_USUARIO");
            query.registerStoredProcedureParameter("p_nombre", String.class, ParameterMode.IN);
            query.setParameter("p_nombre", nombre);
            query.execute();

        }
    }

    @Transactional
    public void InsertarContratos(Map<String, String> contratos) {
        for (Map.Entry<String, String> entry : contratos.entrySet()) {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UGTP_SP_CARGA_CONTRATO");

            query.registerStoredProcedureParameter("p_clave_contrato", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_nombre_usuario", String.class, ParameterMode.IN);
            query.setParameter("p_clave_contrato", entry.getKey());
            query.setParameter("p_nombre_usuario", entry.getValue());
            query.execute();
        }

    }

    public Map<String, Long> CargarIdsZonas() {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UGTP_SP_GET_IDS_ZONAS");
        query.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);
        query.execute();

        Map<String, Long> map = new HashMap<>();
        for (Object row : query.getResultList()) {
            Object[] r = (Object[]) row;
            map.put(r[1].toString().toUpperCase(), ((Number) r[0]).longValue());
        }

        return map;

    }

    public Map<String, Long> CargarIdsNodos() {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UGTP_SP_GET_IDS_NODOS");
        query.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);
        query.execute();

        Map<String, Long> map = new HashMap<>();
        for (Object row : query.getResultList()) {
            Object[] r = (Object[]) row;
            map.put(r[1].toString().toUpperCase(), ((Number) r[0]).longValue());
        }

        return map;
    }

    public Map<String, Long> CargarIdsContratos() {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UGTP_SP_GET_IDS_CONTRATOS");

        query.registerStoredProcedureParameter("p_cursor", void.class, ParameterMode.REF_CURSOR);
        query.execute();

        Map<String, Long> map = new HashMap<>();
        for (Object row : query.getResultList()) {
            Object[] r = (Object[]) row;
            map.put(r[1].toString().toUpperCase(), ((Number) r[0]).longValue());

        }
        return map;

    }
    
    
    
  
    
    
    
    
    public int InsertarKardex(List<FilaExcel> filas,
        Map<String, Long> idsZonas,
        Map<String, Long> idsNodos,
        Map<String, Long> idsContratos) {

    int[] errores = {0};

        Session session = entityManager.unwrap(org.hibernate.Session.class);

    session.doWork(conn -> {
        String sql = "{call UGTP_SP_CARGA_KARDEX(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

        for (FilaExcel fila : filas) {
            try {
                Long idContrato = idsContratos.get(fila.getClaveContrato().toUpperCase());
                Long idNodoRec  = idsNodos.get(fila.getClaveNodoRec().toUpperCase());
                Long idNodoEnt  = idsNodos.get(fila.getClaveNodoEnt().toUpperCase());
                Long idZonaIny  = idsZonas.get(fila.getClaveZonaIny().toUpperCase());
                Long idZonaExt  = idsZonas.get(fila.getClaveZonaExt().toUpperCase());

                if (idContrato == null || idNodoRec == null || idNodoEnt == null
                        || idZonaIny == null || idZonaExt == null) {
                    errores[0]++;
                    continue;
                }

                try (java.sql.CallableStatement cs = conn.prepareCall(sql)) {
                    cs.setDate(1,  new java.sql.Date(fila.getFecha().getTime()));
                    cs.setLong(2,  idContrato);
                    cs.setLong(3,  idNodoRec);
                    cs.setLong(4,  idNodoEnt);
                    cs.setLong(5,  idZonaIny);
                    cs.setLong(6,  idZonaExt);
                    cs.setBigDecimal(7,  fila.getQtyNomRec());
                    cs.setBigDecimal(8,  fila.getQtyAsigRec());
                    cs.setBigDecimal(9,  fila.getQtyNomEnt());
                    cs.setBigDecimal(10, fila.getQtyAsigEnt());
                    cs.setBigDecimal(11, fila.getGasExceso());
                    cs.setBigDecimal(12, fila.getTarifaExcesoFirme());
                    cs.setBigDecimal(13, fila.getTarifaUsoInt());
                    cs.setBigDecimal(14, fila.getCargoUso());
                    cs.setBigDecimal(15, fila.getCargoGasExceso());
                    cs.setBigDecimal(16, fila.getTotalFacturar());
                    cs.execute();
                }

            } catch (Exception ex) {
                errores[0]++;
            }
        }
    });

    return errores[0];
}



//    @Transactional
//    public int InsertarKardex(List<FilaExcel> filas,
//            Map<String, Long> idsZonas,
//            Map<String, Long> idsNodos,
//            Map<String, Long> idsContratos) {
//
//        int errores = 0;
//
//        for (FilaExcel fila : filas) {
//            
//            ServiceResult result = new ServiceResult();
//            try {
//                Long idContrato = idsContratos.get(fila.getClaveContrato().toUpperCase());
//                Long idNodoRec = idsNodos.get(fila.getClaveNodoRec().toUpperCase());
//                Long idNodoEnt = idsNodos.get(fila.getClaveNodoEnt().toUpperCase());
//                Long idZonaIny = idsZonas.get(fila.getClaveZonaIny().toUpperCase());
//                Long idZonaExt = idsZonas.get(fila.getClaveZonaExt().toUpperCase());
//
//                if (idContrato == null || idNodoRec == null || idNodoEnt == null || idZonaIny == null || idZonaExt == null) {
//                    errores++;
//                    continue;
//                }
//
//                    StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UGTP_SP_CARGA_KARDEX");
//                query.registerStoredProcedureParameter("p_fecha", Date.class, ParameterMode.IN);
//                query.registerStoredProcedureParameter("p_id_contrato", Integer.class, ParameterMode.IN);
//                query.registerStoredProcedureParameter("p_id_nodo_rec", Integer.class, ParameterMode.IN);
//                query.registerStoredProcedureParameter("p_id_nodo_ent", Integer.class, ParameterMode.IN);
//                query.registerStoredProcedureParameter("p_id_zona_iny", Integer.class, ParameterMode.IN);
//                query.registerStoredProcedureParameter("p_id_zona_ext", Integer.class, ParameterMode.IN);
//                query.registerStoredProcedureParameter("p_qty_nom_rec", BigDecimal.class, ParameterMode.IN);
//                query.registerStoredProcedureParameter("p_qty_asig_rec", BigDecimal.class, ParameterMode.IN);
//                query.registerStoredProcedureParameter("p_qty_nom_ent", BigDecimal.class, ParameterMode.IN);
//                query.registerStoredProcedureParameter("p_qty_asig_ent", BigDecimal.class, ParameterMode.IN);
//                query.registerStoredProcedureParameter("p_gas_exceso", BigDecimal.class, ParameterMode.IN);
//                query.registerStoredProcedureParameter("p_tarifa_exc_firme", BigDecimal.class, ParameterMode.IN);
//                query.registerStoredProcedureParameter("p_tarifa_uso_int", BigDecimal.class, ParameterMode.IN);
//                query.registerStoredProcedureParameter("p_cargo_uso", BigDecimal.class, ParameterMode.IN);
//                query.registerStoredProcedureParameter("p_cargo_gas_exc", BigDecimal.class, ParameterMode.IN);
//                query.registerStoredProcedureParameter("p_total_facturar", BigDecimal.class, ParameterMode.IN);
//
//                query.setParameter("p_fecha", new Date(fila.getFecha().getTime()));
//                query.setParameter("p_id_contrato", idContrato);
//                query.setParameter("p_id_nodo_rec", idNodoRec);
//                query.setParameter("p_id_nodo_ent", idNodoEnt);
//                query.setParameter("p_id_zona_iny", idZonaIny);
//                query.setParameter("p_id_zona_ext", idZonaExt);
//                query.setParameter("p_qty_nom_rec", fila.getQtyNomRec());
//                query.setParameter("p_qty_asig_rec", fila.getQtyAsigRec());
//                query.setParameter("p_qty_nom_ent", fila.getQtyNomEnt());
//                query.setParameter("p_qty_asig_ent", fila.getQtyAsigEnt());
//                query.setParameter("p_gas_exceso", fila.getGasExceso());
//                query.setParameter("p_tarifa_exc_firme", fila.getTarifaExcesoFirme());
//                query.setParameter("p_tarifa_uso_int", fila.getTarifaUsoInt());
//                query.setParameter("p_cargo_uso", fila.getCargoUso());
//                query.setParameter("p_cargo_gas_exc", fila.getCargoGasExceso());
//                query.setParameter("p_total_facturar", fila.getTotalFacturar());
//                query.execute();
//
//            } catch (Exception ex) {
//                result.correct = false;
//                result.ErrorMesage = ex.getLocalizedMessage();
//               result.ex = ex;
//                errores++;
//            }
//        }
//        return errores;
//
//    }

}
