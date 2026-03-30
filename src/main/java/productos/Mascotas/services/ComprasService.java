package productos.Mascotas.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import productos.Mascotas.dto.CompraDTO;
import productos.Mascotas.dto.CrearCompraDTO;
import productos.Mascotas.dto.EstadoCompra;


@Service
public class ComprasService {

    private final List<CompraDTO> compras = new ArrayList<>();
    private final AtomicLong secuenciaId = new AtomicLong(3);

    /***Datos iniciales*/
    public ComprasService() {
        compras.add(CompraDTO.builder()
                .id(1L)
                .cliente("Cliente 1")
                .producto("Alimento")
                .cantidad(2)
                .precioUnitario(new BigDecimal("15000"))
                .total(new BigDecimal("30000"))
                .estado(EstadoCompra.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .build());
        compras.add(CompraDTO.builder()
                .id(2L)
                .cliente("Cliente 2")
                .producto("Kit de aseo")
                .cantidad(1)
                .precioUnitario(new BigDecimal("17500"))
                .total(new BigDecimal("17500"))
                .estado(EstadoCompra.PAGADA)
                .fechaCreacion(LocalDateTime.now())
                .build());
        compras.add(CompraDTO.builder()
                .id(3L)
                .cliente("Cliente 3")
                .producto("Juguete")
                .cantidad(3)
                .precioUnitario(new BigDecimal("7000"))
                .total(new BigDecimal("21000"))
                .estado(EstadoCompra.ENVIADA)
                .fechaCreacion(LocalDateTime.now())
                .build());
        compras.add(CompraDTO.builder()
                .id(4L)
                .cliente("cliente 4")
                .producto("cama")
                .cantidad(1)
                .precioUnitario(new BigDecimal("27000"))
                .total(new BigDecimal("27"))
                .estado(EstadoCompra.CANCELADA)
                .fechaCreacion(LocalDateTime.now())
                .build());
    }

    /**Muestra las ordenes*/
    public List<CompraDTO> obtenerToda() {
        return compras;
    }

    /**Buscar por id*/
    public CompraDTO obterPorId(Long id) {
        for (CompraDTO compra : compras) {
            if (compra.getId().equals(id)) {
                return compra;
            }
        }

        return null;
    }

    /**Crea orden pendiente*/
    public CompraDTO crear(CrearCompraDTO request) {
        CompraDTO nuevaCompra = CompraDTO.builder()
                .id(secuenciaId.incrementAndGet())
                .cliente(request.getCliente())
                .producto(request.getProducto())
                .cantidad(request.getCantidad())
                .precioUnitario(request.getPrecioUnitario())
                .total(calcularTotal(request.getCantidad(), request.getPrecioUnitario()))
                .estado(EstadoCompra.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .build();

        compras.add(nuevaCompra);
        return nuevaCompra;
    }

    /**Actualizacion*/
    public CompraDTO actualizar(Long id, CompraDTO request) {
        CompraDTO compraExistente = obterPorId(id);

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

        return compraExistente;
    }

    /**Cambio de orden*/
    public CompraDTO cancelar(Long id) {
        CompraDTO compraExistente = obterPorId(id);

        if (compraExistente == null) {
            return null;
        }

        compraExistente.setEstado(EstadoCompra.CANCELADA);
        return compraExistente;
    }

    /**Calcula total*/
    private BigDecimal calcularTotal(Integer cantidad, BigDecimal precioUnitario) {
        if (cantidad == null || precioUnitario == null) {
            return BigDecimal.ZERO;
        }

        return precioUnitario.multiply(BigDecimal.valueOf(cantidad.longValue()));
    }
}
