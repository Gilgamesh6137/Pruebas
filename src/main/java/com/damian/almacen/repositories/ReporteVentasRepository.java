package com.damian.almacen.repositories;

import com.damian.almacen.dto.reporteVentas.ReporteVentasResponse;
import com.damian.almacen.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteVentasRepository extends JpaRepository<Venta, Long> {

    @Query(value = """
        SELECT new com.damian.almacen.dto.reporteVentas.ReporteVentasResponse(
            v.sucursal.id,
            v.sucursal.nombre,
            SUM(d.cantidadProducto * d.precioProducto),
            SUM(d.cantidadProducto)
        )
        FROM Venta v JOIN v.detalleVentas d
        WHERE v.estadoVenta = com.damian.almacen.enums.EstadoVenta.REGISTRADA
        GROUP BY v.sucursal.id, v.sucursal.nombre
""")
    List<ReporteVentasResponse> obtenerReportes();
}
