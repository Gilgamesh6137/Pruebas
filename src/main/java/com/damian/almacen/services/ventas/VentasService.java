package com.damian.almacen.services.ventas;

import com.damian.almacen.dto.ventas.VentaRequest;
import com.damian.almacen.dto.ventas.VentaResponse;

import java.util.List;

public interface VentasService {

    List<VentaResponse> listar();

    VentaResponse obtenerPorIdActiva(Long id);

    VentaResponse registrar(VentaRequest request);

    VentaResponse cancelar(Long id);
}
