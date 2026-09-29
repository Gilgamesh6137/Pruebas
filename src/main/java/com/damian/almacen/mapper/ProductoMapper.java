package com.damian.almacen.mapper;

import com.damian.almacen.dto.productos.ProductoRequest;
import com.damian.almacen.dto.productos.ProductoResponse;
import com.damian.almacen.entities.Producto;
import com.damian.almacen.enums.Categoria;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto requestAEntidad(ProductoRequest request, Categoria categoria){

        return request == null ? null : Producto.crear(
                request.nombre(),
                categoria,
                request.precio(),
                request.cantidad()
        );
    }

    public ProductoResponse entidadResponse(Producto producto){

        return producto == null ? null : new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getCategoria().getDescripcion(),
                producto.getPrecio(),
                producto.getCantidad()
        );
    }
}
