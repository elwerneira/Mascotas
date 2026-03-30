package productos.Mascotas.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import productos.Mascotas.dto.CompraDTO;
import productos.Mascotas.dto.CrearCompraDTO;
import productos.Mascotas.services.ComprasService;


@RestController
@Validated
@RequestMapping("/compras")
public class ComprasController {

    private final ComprasService service;

    public ComprasController(ComprasService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<List<CompraDTO>> obtenerTodas() {
        return ResponseEntity.ok(service.obtenerToda());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        CompraDTO compra = service.obterPorId(id);

        if (compra == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Orden de compra no encontrada. ID: " + id);
        }

        return ResponseEntity.ok(compra);
    }

    /**Consulta estado */
    @GetMapping("/{id}/estado")
    public ResponseEntity<?> obtenerEstado(@PathVariable Long id) {
        CompraDTO compra = service.obterPorId(id);

        if (compra == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Orden de compra no encontrada. ID: " + id);
        }

        return ResponseEntity.ok(Map.of("id", compra.getId(), "estado", compra.getEstado()));
    }

    /**Crea nueva orden */
    @PostMapping
    public ResponseEntity<CompraDTO> crear(@Valid @RequestBody CrearCompraDTO request) {
        CompraDTO creada = service.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    /**Actualiza datos ya generados */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody CompraDTO request) {
        CompraDTO actualizada = service.actualizar(id, request);

        if (actualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Orden de compra no encontrada. ID: " + id);
        }

        return ResponseEntity.ok(actualizada);
    }

    /**Cancela orden por cambio de estado*/
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        CompraDTO cancelada = service.cancelar(id);

        if (cancelada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Orden de compra no encontrada. ID: " + id);
        }

        return ResponseEntity.ok(cancelada);
    }
}
