package com.damian.almacen.services.productos;

import com.damian.almacen.dto.productos.ProductoRequest;
import com.damian.almacen.dto.productos.ProductoResponse;
import com.damian.almacen.entities.Producto;
import com.damian.almacen.enums.Categoria;
import com.damian.almacen.exceptions.RecursoNoEncontradoException;
import com.damian.almacen.mapper.ProductoMapper;
import com.damian.almacen.repositories.ProductoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductoServiceImpl implements ProductoService{

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<ProductoResponse> listar(String nombre, String categoria, BigDecimal precioMin, BigDecimal precioMax) {

        log.info("Listando todos los productos");

        return productoRepository.findAll().stream()
                .map(productoMapper::entidadResponse).toList();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return productoMapper.entidadResponse(obtenerProductoException(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {

        log.info("Registrando un producto...");

        Producto producto = productoMapper.requestAEntidad(request,
                Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim())
        );

        productoRepository.save(producto);

        log.info("Nuevo producto {} registrado", producto.getNombre());

        return productoMapper.entidadResponse(producto);
    }

    @Override
    public ProductoResponse actualizar(ProductoRequest request, Long id) {

        Producto producto = obtenerProductoException(id);

        log.info("Actualizando producto con id {}", id);

        producto.actualizar(
                request.nombre(),
                Categoria.obtenerCategoriaPorDescripcion(request.categoria().trim()),
                request.precio(),
                request.cantidad()
        );

        productoRepository.saveAndFlush(producto);

        log.info("Producto con id {} actualizado correctamente", id);

        return productoMapper.entidadResponse(producto);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = obtenerProductoException(id);

        log.info("Eliminando producto con id {}", id);

        productoRepository.delete(producto);
        productoRepository.flush();

        log.info("Producto con id {} eliminado correctamente", id);
    }

    private Producto obtenerProductoException(Long id){
        log.info("Buscando producto con id: {}", id);

        return productoRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException(
                        "producto no encontrado con id: " + id
                )
        );
    }
}
