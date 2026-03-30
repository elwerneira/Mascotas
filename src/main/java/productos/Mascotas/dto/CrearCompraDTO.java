package productos.Mascotas.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Validaciones. */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearCompraDTO {

    @NotBlank(message = "El cliente es obligatorio")
    private String cliente;

    @NotBlank(message = "Produco Obligario")
    private String producto;

    @NotNull(message = "Cantidad de producto obligatoria")
    @Positive(message = "Cantidad debe ser mayo a 0")
    private Integer cantidad;

    @NotNull(message = "Precio de producto(s) es obligatorio")
    @DecimalMin(value = "1", message = "El precio por unidad debe ser mayor a 1")
    private BigDecimal precioUnitario;
}
