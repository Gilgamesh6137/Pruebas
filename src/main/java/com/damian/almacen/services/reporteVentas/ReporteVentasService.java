package com.damian.almacen.services.reporteVentas;

import com.damian.almacen.dto.reporteVentas.ReporteVentasResponse;

import java.util.List;

public interface ReporteVentasService {
    List<ReporteVentasResponse> listarReporteVentas();
}
