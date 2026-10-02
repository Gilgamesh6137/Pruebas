package com.damian.almacen.dto.reporteVentas;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Datos del reporte de ventas")
public record ReporteVentasResponse(
    @Schema(description = "ID de la sucursal", example = "1")
    Long idSucursal,

    @Schema(description = "Nombre de la sucursal", example = "Tienda Norte")
    String nombreSucursal,

    @Schema(description = "Total de ventas de la sucursal", example = "1127527.00")
    BigDecimal totalVentas,

    @Schema(description = "Cantidad de productos vendidos en la sucursal", example = "200")
    Long cantidadProductosVendidos
){}
