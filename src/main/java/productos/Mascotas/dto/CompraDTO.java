package productos.Mascotas.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Registro de Orden */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraDTO {

    private Long id;

    @NotBlank(message = "El cliente es obligatorio")
    private String cliente;

    @NotBlank(message = "Producto obligatorio")
    private String producto;

    @NotNull(message = "Cantidad de producto obligatoria, debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "Precio de producto(s) es obligatorio")
    private BigDecimal precioUnitario;
    
    private BigDecimal total;
    private EstadoCompra estado;

    @JsonFormat(pattern = "yy-MM-dd HH:mm")
    private LocalDateTime fechaCreacion;
}
