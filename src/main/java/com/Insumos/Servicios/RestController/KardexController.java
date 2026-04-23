package com.Insumos.Servicios.RestController;

import com.Insumos.Servicios.DTO.KardexResultado;
import com.Insumos.Servicios.Entity.NodoComercial;
import com.Insumos.Servicios.Exception.ServiceResult;
import com.Insumos.Servicios.Service.KardexService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kardex")
public class KardexController {

    @Autowired
    private KardexService kardexService;

    @GetMapping("/contratos")
    public ResponseEntity<ServiceResult<String>> getContratosPorUsuario(@RequestParam String usuario) {
        ServiceResult<String> result = new ServiceResult<>();
        try {

            List<String> data = kardexService.getContratosPorUsuario(usuario);
            result.correct = true;
            result.status = 200;
            result.objects = data;
            result.message = "Contratos encontrados";
        } catch (Exception ex) {
            result.status = 500;
            result.ErrorMesage = ex.getLocalizedMessage();
            result.ex = ex;

        }
        return ResponseEntity.status(result.status).body(result);
    }

    @GetMapping("/usuario")
    public ResponseEntity<ServiceResult<String>> getUsuarioPorContrato(@RequestParam String contrato) {
        ServiceResult<String> result = new ServiceResult<>();
        try {

            String data = kardexService.getUsuarioPorContrato(contrato);
            if (data != null) {

                result.correct = true;
                result.status = 200;
                result.object = data;
            } else {
                result.status = 404;
                result.ErrorMesage = "Contrato no encontrado";

            }
        } catch (Exception ex) {
            result.status = 500;
            result.ErrorMesage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return ResponseEntity.status(result.status).body(result);
    }

    @GetMapping("/nodo-recepcion")
    public ResponseEntity<ServiceResult<KardexResultado>> getInfoPorNodoRecepcion(@RequestParam String clave) {
        ServiceResult<KardexResultado> result = new ServiceResult<>();
        try {
            List<KardexResultado> data = kardexService.getinfoPorNodoRecepcion(clave);

            if (data != null) {
                result.correct = true;
                result.status = 200;
                result.objects = data;
            } else {
                result.correct = false;
                result.status = 404;
                result.ErrorMesage = "No se encontro el elemento buscado";
            }

        } catch (Exception ex) {
            result.correct = false;
            result.status = 500;
            result.ErrorMesage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return ResponseEntity.status(result.status).body(result);
    }

    @GetMapping("/nodos-entrega")
    public ResponseEntity<ServiceResult<KardexResultado>> getInfoPorNodosEntrega(
            @RequestParam String c1, @RequestParam String c2, @RequestParam String c3) {
        ServiceResult<KardexResultado> result = new ServiceResult<>();
        try {

            List<KardexResultado> data = kardexService.getInfoPorNodosEntrega(c1, c2, c3);
            if (data != null) {
                result.correct = true;
                result.status = 200;
                result.objects = data;

            } else {
                result.correct = false;
                result.status = 404;
            }

        } catch (Exception ex) {
            result.status = 500;
            result.ErrorMesage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return ResponseEntity.status(result.status).body(result);
    }

    @GetMapping("/nodos/recepcion")
    public ResponseEntity<ServiceResult<NodoComercial>> getNodosRecepcion() {
        ServiceResult<NodoComercial> result = new ServiceResult<>();
        try {
            List<NodoComercial> data = kardexService.getNodosRecepcion();
            if (data != null) {
                result.correct = true;
                result.status = 200;
                result.objects = data;
            } else {
                result.status = 400;
                result.correct = false;
            }
        } catch (Exception ex) {
            result.status = 500;
            result.ErrorMesage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return ResponseEntity.status(result.status).body(result);
    }

    @GetMapping("/nodos/entrega")
    public ResponseEntity<ServiceResult<NodoComercial>> getNodosEntrega() {
        ServiceResult<NodoComercial> result = new ServiceResult<>();
        try {
            List<NodoComercial> data = kardexService.getNodosEntrega();

            if (data != null) {
                result.correct = true;
                result.status = 200;
                result.objects = data;
            } else {
                result.status = 404;
                result.correct = false;
            }

        } catch (Exception ex) {
            result.status = 500;
            result.ErrorMesage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return ResponseEntity.status(result.status).body(result);
    }

    @GetMapping("/zona-inyeccion")
    public ResponseEntity<ServiceResult<KardexResultado>> getInfoPorZonaInyeccion(@RequestParam String zona) {
        ServiceResult<KardexResultado> result = new ServiceResult<>();
        try {
            List<KardexResultado> data = kardexService.getInfoPorZonaInyeccion(zona);
            if (data != null) {
                result.correct = true;
                result.status = 200;
                result.objects = data;
            } else {
                result.correct = false;
                result.status = 404;
            }
        } catch (Exception ex) {
            result.status = 500;
            result.ErrorMesage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return ResponseEntity.status(result.status).body(result);
    }

    @GetMapping("/zonas-extraccion")
    public ResponseEntity<ServiceResult<KardexResultado>> getInfoPorZonasExtraccion(
            @RequestParam String z1, @RequestParam String z2, @RequestParam String z3) {
        ServiceResult<KardexResultado> result = new ServiceResult<>();
        try {
            List<KardexResultado> data = kardexService.getInfoPorZonasExtraccion(z1, z2, z3);
            if (data != null) {
                result.correct = true;
                result.status = 200;
                result.objects = data;
            } else {
                result.correct = false;
                result.status = 404;
            }

        } catch (Exception ex) {
            result.status = 500;
            result.ErrorMesage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return ResponseEntity.status(result.status).body(result);
    }

    @GetMapping("/contratos-multiples")
    public ResponseEntity<ServiceResult<KardexResultado>> getInfoPorContratos(
            @RequestParam String c1, @RequestParam String c2, @RequestParam String c3,
            @RequestParam String c4
    ) {
        ServiceResult<KardexResultado> result = new ServiceResult<>();
        try {
            List<KardexResultado> data = kardexService.getInfoPorContratos(c1, c2, c3, c4);
            if (data != null) {

                result.correct = true;
                result.status = 200;
                result.objects = data;

            } else {
                result.correct = false;
                result.status = 404;

            }
        } catch (Exception ex) {
            result.status = 500;
            result.ErrorMesage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return ResponseEntity.status(result.status).body(result);
    }

    @GetMapping("/total-facturar")
    public ResponseEntity<ServiceResult<BigDecimal>> getTotalFacturar(
            @RequestParam String usuario,
            @RequestParam(defaultValue = "2021") Integer anio,
            @RequestParam(defaultValue = "1") Integer mes) {
        ServiceResult<BigDecimal> result = new ServiceResult<>();
        try {
            BigDecimal total = kardexService.getTotalFacturarUsuario(usuario, anio, mes);
            result.correct = true;
            result.status = 200;
            result.object = total;
        } catch (Exception ex) {
            result.status = 500;
            result.ErrorMesage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return ResponseEntity.status(result.status).body(result);
    }
    
    
    
     @GetMapping("/promedio-nom-recepcion")
    public ResponseEntity<ServiceResult<BigDecimal>> getPromedioNomRecepcion(
            @RequestParam String usuario,
            @RequestParam(defaultValue = "2021") Integer anio,
            @RequestParam(defaultValue = "1")    Integer mes) {
        ServiceResult<BigDecimal> result = new ServiceResult<>();
        try {
            BigDecimal promedio = kardexService.getPromedioNombreRecepcionUsuario(usuario, anio, mes);
            result.correct = true;
            result.status = 200;
            result.object = promedio;
        } catch (Exception ex) {
            result.correct = false;
            result.status = 500;
            result.ErrorMesage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return ResponseEntity.status(result.status).body(result);
    }

}
