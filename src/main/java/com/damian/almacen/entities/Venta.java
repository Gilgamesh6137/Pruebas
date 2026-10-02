package com.damian.almacen.entities;

import com.damian.almacen.enums.EstadoVenta;
import com.damian.almacen.exceptions.ConflictoException;
import com.damian.almacen.exceptions.DatoInvalidoException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VENTAS")
@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VENTA")
    private Long id;

    @Column(name = "ESTADO", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoVenta estadoVenta;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUCURSAL", nullable = false)
    private Sucursal sucursal;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalleVentas = new ArrayList<>();

    public void agregarDetalle(DetalleVenta detalleVenta){
        if (detalleVenta == null)
            throw new DatoInvalidoException("El detalle de venta no puede ser nulo");

        if (this.detalleVentas.contains(detalleVenta))
            throw new ConflictoException("El detalle de venta ya se encuentra registrado");

        this.detalleVentas.add(detalleVenta);
        detalleVenta.asignarVenta(this);
    }

    public void cancelar(){

        if (this.estadoVenta == EstadoVenta.CANCELADA)
            throw new ConflictoException("La venta ya está cancelada");

        this.detalleVentas.forEach(d-> d.getProducto().aumentarCantidad(d.getCantidadProducto()));

        this.estadoVenta = EstadoVenta.CANCELADA;
    }

    public BigDecimal obtenerTotalVenta(){
        return this.detalleVentas.stream()
                .map(d -> d.getPrecioProducto().multiply(BigDecimal.valueOf(d.getCantidadProducto())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Venta crear(Sucursal sucursal) {
        if(sucursal == null)
            throw new DatoInvalidoException("La sucursal es requerida");

        return Venta.builder()
                .estadoVenta(EstadoVenta.REGISTRADA)
                .fecha(LocalDate.now())
                .sucursal(sucursal)
                .build();
    }
}
