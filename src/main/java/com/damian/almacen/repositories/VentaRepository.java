package com.damian.almacen.repositories;

import com.damian.almacen.entities.Venta;
import com.damian.almacen.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    Optional<Venta> findByIdAndEstadoVenta(Long id, EstadoVenta estadoVenta);
}
