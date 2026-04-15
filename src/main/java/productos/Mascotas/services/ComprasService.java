package productos.Mascotas.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import productos.Mascotas.Entity.MascotaEntity;
import productos.Mascotas.Repository.MascotasRespository;
import productos.Mascotas.dto.CompraDTO;
import productos.Mascotas.dto.CrearCompraDTO;
import productos.Mascotas.dto.EstadoCompra;

@Service
public class ComprasService {

    private final MascotasRespository repository;

    public ComprasService(MascotasRespository repository) {
        this.repository = repository;
    }

    /**Muestra las ordenes*/
    public List<CompraDTO> obtenerToda() {
        return repository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    /**Buscar por id*/
    public CompraDTO obterPorId(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    /**Crea orden pendiente*/
    public CompraDTO crear(CrearCompraDTO request) {
        MascotaEntity nuevaCompra = new MascotaEntity();
        nuevaCompra.setCliente(request.getCliente());
        nuevaCompra.setProducto(request.getProducto());
        nuevaCompra.setCantidad(request.getCantidad());
        nuevaCompra.setPrecioUnitario(request.getPrecioUnitario());
        nuevaCompra.setTotal(calcularTotal(request.getCantidad(), request.getPrecioUnitario()));
        nuevaCompra.setEstado(EstadoCompra.PENDIENTE);
        nuevaCompra.setFechaCreacion(LocalDateTime.now());

        return toDto(repository.save(nuevaCompra));
    }

    /**Actualizacion*/
    public CompraDTO actualizar(Long id, CompraDTO request) {
        MascotaEntity compraExistente = repository.findById(id).orElse(null);

        if (compraExistente == null) {
            return null;
        }

        compraExistente.setCliente(request.getCliente());
        compraExistente.setProducto(request.getProducto());
        compraExistente.setCantidad(request.getCantidad());
        compraExistente.setPrecioUnitario(request.getPrecioUnitario());
        compraExistente.setTotal(calcularTotal(request.getCantidad(), request.getPrecioUnitario()));

        if (request.getEstado() != null) {
            compraExistente.setEstado(request.getEstado());
        }

        return toDto(repository.save(compraExistente));
    }

    /**Cambio de orden*/
    public CompraDTO cancelar(Long id) {
        MascotaEntity compraExistente = repository.findById(id).orElse(null);

        if (compraExistente == null) {
            return null;
        }

        compraExistente.setEstado(EstadoCompra.CANCELADA);
        return toDto(repository.save(compraExistente));
    }

    /**Elimina orden*/
    public boolean eliminar(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    /**Calcula total*/
    private BigDecimal calcularTotal(Integer cantidad, BigDecimal precioUnitario) {
        if (cantidad == null || precioUnitario == null) {
            return BigDecimal.ZERO;
        }

        return precioUnitario.multiply(BigDecimal.valueOf(cantidad.longValue()));
    }

    private CompraDTO toDto(MascotaEntity entity) {
        return CompraDTO.builder()
                .id(entity.getId())
                .cliente(entity.getCliente())
                .producto(entity.getProducto())
                .cantidad(entity.getCantidad())
                .precioUnitario(entity.getPrecioUnitario())
                .total(entity.getTotal())
                .estado(entity.getEstado())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }
}
