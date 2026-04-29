package productos.mascotas.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

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
import productos.mascotas.repository.MascotasRespository;

@ExtendWith(MockitoExtension.class)
class ComprasServiceTest {

    @Mock
    private MascotasRespository repository;

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
}
