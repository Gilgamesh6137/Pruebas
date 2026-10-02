package com.damian.almacen.repositories;

import com.damian.almacen.entities.Producto;
import com.damian.almacen.enums.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Query("""
        SELECT p FROM Producto p
        WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%',:nombre,'%')))
        AND (:categoria IS NULL OR p.categoria = :categoria)
        AND ((:precioMin IS NULL OR :precioMax IS NULL) OR p.precio BETWEEN :precioMin AND :precioMax)
""")
    List<Producto> buscarPorFiltrosOpcionales(
            @Param("nombre") String nombre,
            @Param("categoria") Categoria categoria,
            @Param("precioMin")BigDecimal precioMin,
            @Param("precioMax") BigDecimal precioMax
    );
}
