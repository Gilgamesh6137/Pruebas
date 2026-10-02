package com.damian.almacen.entities;

import com.damian.almacen.exceptions.DatoInvalidoException;
import com.damian.almacen.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "DETALLES_VENTAS")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DETALLE_VENTA")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VENTA", nullable = false)
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PRODUCTO", nullable = false)
    private Producto producto;

    @Column(name = "CANTIDAD_PRODUCTO", nullable = false)
    private Integer cantidadProducto;

    @Column(name = "PRECIO_PRODUCTO", nullable = false)
    private BigDecimal precioProducto;

    public void asignarVenta(Venta venta){
        if (venta == null)
            throw new DatoInvalidoException("La venta es requerida");

        this.venta = venta;
    }

    public BigDecimal obtenerSubTotal(){
        return this.precioProducto.multiply(BigDecimal.valueOf(this.cantidadProducto));
    }

    public static DetalleVenta crear(Producto producto, Integer cantidadProducto
    ){
        if(producto == null)
            throw new DatoInvalidoException("El producto es requerido");

        ValoresNumericosUtils.validarNumeroRequerido(cantidadProducto, "La cantidad del producto es requerida");
        ValoresNumericosUtils.validarEnteroPositivo(cantidadProducto, "La cantidad del producto debe ser positiva");

        return DetalleVenta.builder()
                .producto(producto)
                .cantidadProducto(cantidadProducto)
                .precioProducto(producto.getPrecio())
                .build();

    }
}
