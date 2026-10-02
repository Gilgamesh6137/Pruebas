package com.damian.almacen.services.reporteVentas;

import com.damian.almacen.dto.reporteVentas.ReporteVentasResponse;
import com.damian.almacen.repositories.ReporteVentasRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReporteVentasServiceImpl implements ReporteVentasService{

    private final ReporteVentasRepository reporteVentasRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReporteVentasResponse> listarReporteVentas() {

        log.info("Listando reportes de ventas...");

        return reporteVentasRepository.obtenerReportes();
    }
}
