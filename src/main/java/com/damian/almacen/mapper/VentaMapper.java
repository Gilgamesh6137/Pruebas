package com.damian.almacen.mapper;

import com.damian.almacen.dto.sucursales.SucursalRequest;
import com.damian.almacen.dto.ventas.VentaResponse;
import com.damian.almacen.entities.Venta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VentaMapper {
    private final DetalleVentaMapper detalleVentaMapper;
    private final SucursalMapper sucursalMapper;

    public VentaResponse entidadAResponse(Venta venta){

        return venta == null ? null : new VentaResponse(
                venta.getId(),
                venta.getFecha().toString(),
                venta.getEstadoVenta().getDescripcion(),
                sucursalMapper.entidadAResponse(venta.getSucursal()),
                venta.getDetalleVentas().stream().map(detalleVentaMapper::entidadAResponse).toList(),
                venta.obtenerTotalVenta()
        );
    }
}
