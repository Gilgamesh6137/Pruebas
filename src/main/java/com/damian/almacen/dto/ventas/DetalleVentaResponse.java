package com.damian.almacen.dto.ventas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Detalle de un producto dentro de una venta")
public record DetalleVentaResponse(

        @Schema(name = "Id del prodcuto", example = "1")
        Long idProducto,

        @Schema(name = "Nombre del prodcuto", example = "Laptop Gamer")
        String nombreProducto,

        @Schema(name = "Cantidad de unidades del prodcuto", example = "10")
        Integer cantidadProducto,

        @Schema(name = "Precio unitario del prodcuto", example = "1500.00")
        BigDecimal precioProducto,

        @Schema(name = "Subtotal del prodcuto", example = "15000.00")
        BigDecimal subtotal
) {}
