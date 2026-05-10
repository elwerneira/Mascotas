package productos.mascotas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import productos.mascotas.dto.CompraDTO;
import productos.mascotas.dto.CrearCompraDTO;
import productos.mascotas.dto.EstadoCompra;
import productos.mascotas.entity.MascotaEntity;
import productos.mascotas.repository.MascotasRepository;

@ExtendWith(MockitoExtension.class)
class ComprasServiceTest {

    @Mock
    private MascotasRepository repository;

    @InjectMocks
    private ComprasService service;

    private CrearCompraDTO request;

    @BeforeEach
    void setUp() {
        request = CrearCompraDTO.builder()
                .cliente("Juan Perez")
                .producto("Alimento premium")
                .cantidad(3)
                .precioUnitario(BigDecimal.valueOf(5000))
                .build();
    }

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(repository);
    }

    @Test
    void crearDebeGuardarCompraConEstadoPendienteYTotalCalculado() {
        when(repository.save(any(MascotaEntity.class))).thenAnswer(invocation -> {
            MascotaEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        CompraDTO resultado = service.crear(request);

        assertEquals(1L, resultado.getId());
        assertEquals("Juan Perez", resultado.getCliente());
        assertEquals("Alimento premium", resultado.getProducto());
        assertEquals(EstadoCompra.PENDIENTE, resultado.getEstado());
        assertEquals(BigDecimal.valueOf(15000), resultado.getTotal());
        verify(repository).save(any(MascotaEntity.class));
    }

    @Test
    void eliminarDebeRetornarTrueYEliminarCuandoExisteLaCompra() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        boolean resultado = service.eliminar(id);

        assertTrue(resultado);
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    void obtenerPorIdDebeRetornarCompraCuandoExiste() {
        Long id = 1L;
        MascotaEntity entity = new MascotaEntity();
        entity.setId(id);
        entity.setCliente("Juan Perez");
        entity.setProducto("Alimento premium");
        entity.setCantidad(3);
        entity.setPrecioUnitario(BigDecimal.valueOf(5000));
        entity.setTotal(BigDecimal.valueOf(15000));
        entity.setEstado(EstadoCompra.PENDIENTE);

        when(repository.findById(id)).thenReturn(Optional.of(entity));

        CompraDTO resultado = service.obtenerPorId(id);

        assertEquals(id, resultado.getId());
        assertEquals("Juan Perez", resultado.getCliente());
        assertEquals("Alimento premium", resultado.getProducto());
        assertEquals(BigDecimal.valueOf(15000), resultado.getTotal());
        verify(repository).findById(id);
    }

    @Test
    void obtenerTodaDebeRetornarComprasConvertidasADto() {
        MascotaEntity entity = crearEntity(1L, EstadoCompra.PENDIENTE);
        when(repository.findAll()).thenReturn(List.of(entity));

        List<CompraDTO> resultado = service.obtenerToda();

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals("Juan Perez", resultado.get(0).getCliente());
        assertEquals(EstadoCompra.PENDIENTE, resultado.get(0).getEstado());
        verify(repository).findAll();
    }

    @Test
    void actualizarDebeModificarCompraExistenteYRecalcularTotal() {
        Long id = 1L;
        MascotaEntity entity = crearEntity(id, EstadoCompra.PENDIENTE);
        CompraDTO requestActualizacion = CompraDTO.builder()
                .cliente("Maria Lopez")
                .producto("Arena sanitaria")
                .cantidad(2)
                .precioUnitario(BigDecimal.valueOf(7500))
                .estado(EstadoCompra.PAGADA)
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(any(MascotaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompraDTO resultado = service.actualizar(id, requestActualizacion);

        assertEquals("Maria Lopez", resultado.getCliente());
        assertEquals("Arena sanitaria", resultado.getProducto());
        assertEquals(BigDecimal.valueOf(15000), resultado.getTotal());
        assertEquals(EstadoCompra.PAGADA, resultado.getEstado());
        verify(repository).findById(id);
        verify(repository).save(any(MascotaEntity.class));
    }

    @Test
    void actualizarDebeRetornarNullCuandoNoExisteLaCompra() {
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        CompraDTO resultado = service.actualizar(id, CompraDTO.builder().build());

        assertEquals(null, resultado);
        verify(repository).findById(id);
    }

    @Test
    void cancelarDebeCambiarEstadoACanceladaCuandoExisteLaCompra() {
        Long id = 1L;
        MascotaEntity entity = crearEntity(id, EstadoCompra.PENDIENTE);
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(repository.save(any(MascotaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompraDTO resultado = service.cancelar(id);

        assertEquals(EstadoCompra.CANCELADA, resultado.getEstado());
        verify(repository).findById(id);
        verify(repository).save(any(MascotaEntity.class));
    }

    @Test
    void eliminarDebeRetornarFalseCuandoNoExisteLaCompra() {
        Long id = 99L;
        when(repository.existsById(id)).thenReturn(false);

        boolean resultado = service.eliminar(id);

        assertFalse(resultado);
        verify(repository).existsById(id);
    }

    private MascotaEntity crearEntity(Long id, EstadoCompra estado) {
        MascotaEntity entity = new MascotaEntity();
        entity.setId(id);
        entity.setCliente("Juan Perez");
        entity.setProducto("Alimento premium");
        entity.setCantidad(3);
        entity.setPrecioUnitario(BigDecimal.valueOf(5000));
        entity.setTotal(BigDecimal.valueOf(15000));
        entity.setEstado(estado);
        return entity;
    }
}
