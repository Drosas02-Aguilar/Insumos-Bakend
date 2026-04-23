package com.Insumos.Servicios.DAO;

import com.Insumos.Servicios.Excel.FilaExcel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            FilaExcel fila = new FilaExcel();
            fila.setFecha(row.getCell(0).getDateCellValue());
            fila.setClaveContrato(CellString(row.getCell(1)));
            fila.setNombreUsuario(CellString(row.getCell(2)));
            fila.setClaveNodoRec(CellString(row.getCell(3)));
            fila.setDescNodoRec(CellString(row.getCell(4)));
            fila.setClaveNodoEnt(CellString(row.getCell(5)));
            fila.setDescNodoEnt(CellString(row.getCell(6)));
            fila.setClaveZonaIny(CellString(row.getCell(7)));
            fila.setClaveZonaExt(CellString(row.getCell(8)));
            fila.setQtyNomRec(CellDecimal(row.getCell(9)));
            fila.setQtyAsigRec(CellDecimal(row.getCell(10)));
            fila.setQtyNomEnt(CellDecimal(row.getCell(11)));
            fila.setQtyAsigEnt(CellDecimal(row.getCell(12)));
            fila.setGasExceso(CellDecimal(row.getCell(13)));
            fila.setTarifaExcesoFirme(CellDecimal(row.getCell(14)));
            fila.setTarifaUsoInt(CellDecimal(row.getCell(15)));
            fila.setCargoUso(CellDecimal(row.getCell(16)));
            fila.setCargoGasExceso(CellDecimal(row.getCell(17)));
            fila.setTotalFacturar(CellDecimal(row.getCell(18)));
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
            query.setParameter("P_clave", clave);
            query.execute();

        }

    }
    
    
    @Transactional
    public void InsertarNodos(Map<String,String> nodos){
        for(Map.Entry<String,String> entry : nodos.entrySet()){
    StoredProcedureQuery query = entityManager.createStoredProcedureQuery("UGTP_SP_CARGA_NODO");
    query.registerStoredProcedureParameter("p_clave", String.class, ParameterMode.IN);
    query.registerStoredProcedureParameter("p_desc", String.class, ParameterMode.IN);
    query.execute();
        }
    }
    


}
