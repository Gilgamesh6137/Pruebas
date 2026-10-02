package com.damian.almacen.controllers;

import com.damian.almacen.docs.ProblemaDoc;
import com.damian.almacen.dto.reporteVentas.ReporteVentasResponse;
import com.damian.almacen.services.reporteVentas.ReporteVentasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reporte-ventas")
@RequiredArgsConstructor
@Tag(name = "Reportes de ventas", description = "Gestión de reportes de ventas")
@ApiResponse(responseCode = "400", description = "Datos o parametros invalidos",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class ReporteVentas {

    private final ReporteVentasService reporteVentasService;

    @GetMapping
    @Operation(summary = "Listar reporte de ventas de cada sucursal")
    @ApiResponse(responseCode = "200", description = "Reportes listados")
    @ApiResponse(responseCode = "409", description = "El estado del reporte de ventas no es válido",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<List<ReporteVentasResponse>> listarReporteVentas() {
        return ResponseEntity.ok(reporteVentasService.listarReporteVentas());
    }
}
