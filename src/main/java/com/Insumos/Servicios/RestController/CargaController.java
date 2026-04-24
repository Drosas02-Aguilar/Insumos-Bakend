package com.Insumos.Servicios.RestController;

import com.Insumos.Servicios.Exception.ServiceResult;
import com.Insumos.Servicios.Service.CargaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/carga")
public class CargaController {

    @Autowired
    private CargaService cargaService;

    @PostMapping(value = "/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ServiceResult<String>> cargarExcel(
            @RequestParam("archivo") MultipartFile archivo) {
        ServiceResult<String> result = new ServiceResult<>();
        try {
            if (archivo.isEmpty()) {
                result.correct = false;
                result.status = 400;
                result.ErrorMesage = "El archivo está vacío.";
                return ResponseEntity.status(result.status).body(result);
            }
            String resumen = cargaService.CargarExcel(archivo);
            result.correct = true;
            result.status = 200;
            result.object = resumen;
        } catch (Exception ex) {
            result.correct = false;
            result.status = 500;
            result.ErrorMesage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return ResponseEntity.status(result.status).body(result);
    }
}

