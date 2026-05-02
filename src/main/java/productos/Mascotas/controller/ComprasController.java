package productos.mascotas.controller;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import productos.mascotas.dto.CompraDTO;
import productos.mascotas.dto.CrearCompraDTO;
import productos.mascotas.services.ComprasService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@Validated
@RequestMapping("/compras")
public class ComprasController {

    private final ComprasService service;

    public ComprasController(ComprasService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<CollectionModel<CompraDTO>> obtenerTodas() {
        List<CompraDTO> compras = service.obtenerToda().stream()
                .map(this::agregarLinks)
                .toList();

        CollectionModel<CompraDTO> response = CollectionModel.of(compras);
        response.add(linkTo(methodOn(ComprasController.class).obtenerTodas()).withSelfRel());
        response.add(linkTo(methodOn(ComprasController.class).crear(null)).withRel("crear"));

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        CompraDTO compra = service.obterPorId(id);

        if (compra == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Orden de compra no encontrada. ID: " + id);
        }

        return ResponseEntity.ok(agregarLinks(compra));
    }

    @GetMapping("/{id}/estado")
    public ResponseEntity<?> obtenerEstado(@PathVariable Long id) {
        CompraDTO compra = service.obterPorId(id);

        if (compra == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Orden de compra no encontrada. ID: " + id);
        }

        EntityModel<Map<String, Object>> response = EntityModel.of(
                Map.of("id", compra.getId(), "estado", compra.getEstado()));
        response.add(linkTo(methodOn(ComprasController.class).obtenerEstado(id)).withSelfRel());
        response.add(linkTo(methodOn(ComprasController.class).obtenerPorId(id)).withRel("compra"));

        return ResponseEntity.ok(response);
    }

    /**Crea nueva orden */
    @PostMapping
    public ResponseEntity<CompraDTO> crear(@Valid @RequestBody CrearCompraDTO request) {
        CompraDTO creada = service.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(agregarLinks(creada));
    }

    /**Actualiza datos ya generados */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody CompraDTO request) {
        CompraDTO actualizada = service.actualizar(id, request);

        if (actualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Orden de compra no encontrada. ID: " + id);
        }

        return ResponseEntity.ok(agregarLinks(actualizada));
    }

    /**Cancela orden por cambio de estado*/
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        CompraDTO cancelada = service.cancelar(id);

        if (cancelada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Orden de compra no encontrada. ID: " + id);
        }

        return ResponseEntity.ok(agregarLinks(cancelada));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        boolean eliminada = service.eliminar(id);

        if (!eliminada) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Orden no encontrada. ID: " + id);
        }

        return ResponseEntity.noContent().build();
    }

    private CompraDTO agregarLinks(CompraDTO compra) {
        Long id = compra.getId();
        compra.add(linkTo(methodOn(ComprasController.class).obtenerPorId(id)).withSelfRel());
        compra.add(linkTo(methodOn(ComprasController.class).obtenerTodas()).withRel("compras"));
        compra.add(linkTo(methodOn(ComprasController.class).obtenerEstado(id)).withRel("estado"));
        compra.add(linkTo(methodOn(ComprasController.class).actualizar(id, null)).withRel("actualizar"));
        compra.add(linkTo(methodOn(ComprasController.class).cancelar(id)).withRel("cancelar"));
        compra.add(linkTo(methodOn(ComprasController.class).eliminar(id)).withRel("eliminar"));
        return compra;
    }
}
