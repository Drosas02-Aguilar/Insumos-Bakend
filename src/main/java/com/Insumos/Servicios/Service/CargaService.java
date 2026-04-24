package com.Insumos.Servicios.Service;

import com.Insumos.Servicios.DAO.CargaDAO;
import com.Insumos.Servicios.Excel.FilaExcel;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CargaService {

    @Autowired
    private CargaDAO cargaDAO;

    public String CargarExcel(MultipartFile archivo) throws Exception {

        List<FilaExcel> filas = cargaDAO.LeerExcel(archivo);
        int totalFilas = filas.size();
        
        
        Set<String> zonas = cargaDAO.ExtraerZonas(filas);
        Map<String,String> nodos = cargaDAO.ExtraerNodos(filas);
        Set<String> usuarios = cargaDAO.ExtraerUsuarios(filas);
        Map<String,String> contratos = cargaDAO.ExtraerContratos(filas);
        
        cargaDAO.InsertarZonas(zonas);
        cargaDAO.InsertarNodos(nodos);
        cargaDAO.InsertarUsuario(usuarios);
        
        cargaDAO.InsertarContratos(contratos);
        
        Map<String,Long> idsZonas = cargaDAO.CargarIdsZonas();
        Map<String,Long> idsNodos = cargaDAO.CargarIdsNodos();
        Map<String,Long> idsContratos = cargaDAO.CargarIdsContratos();
        
        int errores = cargaDAO.InsertarKardex(filas, idsZonas, idsNodos, idsContratos);
        
        
        int exitosas = totalFilas - errores;
        
        return "Carga completada."
                + " Total filas: "    + totalFilas
                + " | Insertadas: "   + exitosas
                + " | Con error: "    + errores
                + " | Zonas únicas: " + zonas.size()
                + " | Nodos únicos: " + nodos.size()
                + " | Usuarios: "     + usuarios.size()
                + " | Contratos: "    + contratos.size();
        
        
    }

}
