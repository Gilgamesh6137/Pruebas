package com.damian.almacen.controllers;

import com.damian.almacen.docs.ProblemaDoc;
import com.damian.almacen.dto.ventas.VentaRequest;
import com.damian.almacen.dto.ventas.VentaResponse;
import com.damian.almacen.services.ventas.VentasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name = "Ventas", description = "Gestión de ventas")
// Errores que pueden ocurrir en cualquier endpoint:
@ApiResponse(
        responseCode = "400", description = "Datos o parámetros inválidos",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
@ApiResponse(responseCode = "500", description = "Error interno del servidor",
        content = @Content(mediaType = "application/problem+json",
                schema = @Schema(implementation = ProblemaDoc.class)))
public class VentaController {

    private final VentasService ventasService;

    @GetMapping
    @Operation(summary = "Listar ventas registradas y canceladas",
            description = "Obtiene todas las ventas registradas y canceladas segun se indique")
    @ApiResponse(responseCode = "200", description = "Listado obtenido")
    @ApiResponse(responseCode = "409", description = "El estado de las ventas no es válido",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<List<VentaResponse>> listar(
            @Parameter(description = "Escribir el estado de la venta [ REGISTRADA | CANCELADA ]", example = "REGISTRADA")
            @RequestParam(required = false) String estadoVenta){

        return ResponseEntity.ok(ventasService.listar(estadoVenta));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener ventas por ID")
    @ApiResponse(responseCode = "200", description = "venta encontrada")
    @ApiResponse(responseCode = "404", description = "La venta no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> obtenerPorId(
            @Parameter(description = "Id de la venta", example = "1")
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id
    ){
        return ResponseEntity.ok(ventasService.obtenerPorIdActiva(id));
    }

    @PostMapping
    @Operation(summary = "Registrar una venta")
    @ApiResponse(responseCode = "201", description = "Venta creada")
    @ApiResponse(responseCode = "409", description = "Conflicto con datos de la venta",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> registrar(@Valid @RequestBody VentaRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(ventasService.registrar(request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar una venta con su id", description = "Asignar el identificador de la venta")
    @ApiResponse(responseCode = "204", description = "venta cancelada correctamente")
    @ApiResponse(responseCode = "404", description = "La venta no existe",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    @ApiResponse(responseCode = "409", description = "Conflicto con datos de la venta",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemaDoc.class)))
    public ResponseEntity<VentaResponse> cancelar(
            @Parameter(description = "Identificador de la venta", example = "1")
            @PathVariable @Positive(message = "El identificador debe ser positivo") Long id
    ) {
        ventasService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
