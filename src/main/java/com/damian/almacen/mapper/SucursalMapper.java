package com.damian.almacen.mapper;

import com.damian.almacen.dto.sucursales.SucursalRequest;
import com.damian.almacen.dto.sucursales.SucursalResponse;
import com.damian.almacen.entities.Sucursal;
import org.springframework.stereotype.Component;

@Component
public class SucursalMapper {

    public Sucursal requestAEntidad(SucursalRequest request){

        return request == null ? null : Sucursal.crear(
                request.nombre(),
                request.direccion()
        );
    }

    public SucursalResponse entidadResponse(Sucursal sucursal){

        return sucursal == null ? null : new SucursalResponse(
                sucursal.getId(),
                sucursal.getNombre(),
                sucursal.getDireccion()
        );
    }
}
