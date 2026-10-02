package com.damian.almacen.services.ventas;

import com.damian.almacen.dto.ventas.VentaRequest;
import com.damian.almacen.dto.ventas.VentaResponse;
import com.damian.almacen.entities.DetalleVenta;
import com.damian.almacen.entities.Producto;
import com.damian.almacen.entities.Sucursal;
import com.damian.almacen.entities.Venta;
import com.damian.almacen.enums.EstadoVenta;
import com.damian.almacen.exceptions.RecursoNoEncontradoException;
import com.damian.almacen.mapper.VentaMapper;
import com.damian.almacen.repositories.ProductoRepository;
import com.damian.almacen.repositories.SucursalRepository;
import com.damian.almacen.repositories.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class VentasServiceImpl implements VentasService{

    private final VentaRepository ventaRepository;

    private final VentaMapper ventaMapper;

    private final SucursalRepository sucursalRepository;

    private final ProductoRepository productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listar(String estadoVentaP) {

        log.info("Listando ventas...");

        EstadoVenta estadoVenta = estadoVentaP == null ? EstadoVenta.REGISTRADA :
                EstadoVenta.obtenerEstadoVentaPorDescripcion(estadoVentaP);

        return ventaRepository.findAllByEstadoVenta(estadoVenta).stream()
                .map(ventaMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtenerPorIdActiva(Long id) {

        log.info("Obteniendo venta con el ID: {}", id);

        Venta venta = ventaRepository.findByIdAndEstadoVenta(id, EstadoVenta.REGISTRADA).orElseThrow(
                () -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + id)
        );

        return ventaMapper.entidadAResponse(venta);
    }

    @Override
    public VentaResponse registrar(VentaRequest request) {

        log.info("Registrando venta...");

        Sucursal sucursal = obtenerSucursalOException(request.idSucursal());

        Venta venta = Venta.crear(sucursal);

        request.productos().forEach(p-> {
            Producto producto = obtenerProductoOException(p.idProducto());
            DetalleVenta detalleVenta = DetalleVenta.crear(
                    producto,
                    p.cantidadProducto()
            );
            venta.agregarDetalle(detalleVenta);
            producto.descontarCantidad(p.cantidadProducto());
        });

        ventaRepository.save(venta);

        log.info("Se registró una nueva venta");

        return ventaMapper.entidadAResponse(venta);
    }

    @Override
    public VentaResponse cancelar(Long id) {

        Venta venta = obtenerVentaException(id);
        log.info("Cancelando venta...");

        venta.cancelar();
        log.info("Venta cancelada con ID: " + id);

        return ventaMapper.entidadAResponse(venta);
    }

    private Venta obtenerVentaException(Long id){
        log.info("Obteniendo venta por ID: {}", id);
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta no encontrada con ID: " + id));
    }

    private Sucursal obtenerSucursalOException(Long id){
        log.info("Obteniendo sucursal con ID: {}", id);
        return sucursalRepository.findById(id)
                .orElseThrow(()-> new RecursoNoEncontradoException("Sucursal no encontrada con ID: " + id));
    }

    private Producto obtenerProductoOException(Long id) {
        log.info("Obteniendo producto con ID: {}", id);
        return productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));
    }
}
