package com.damian.almacen.services.sucursales;

import com.damian.almacen.dto.sucursales.SucursalRequest;
import com.damian.almacen.dto.sucursales.SucursalResponse;
import com.damian.almacen.entities.Sucursal;
import com.damian.almacen.exceptions.ConflictoException;
import com.damian.almacen.exceptions.RecursoNoEncontradoException;
import com.damian.almacen.mapper.SucursalMapper;
import com.damian.almacen.repositories.SucursalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SucursalServiceImpl implements SucursalService{

    private final SucursalRepository sucursalRepository;

    private final SucursalMapper sucursalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> listar() {

        log.info("Listando todas las sucursales");

        return sucursalRepository.findAll().stream().map(sucursalMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalResponse obtenerPorId(Long id) {
        return sucursalMapper.entidadAResponse(obtenerSucursalException(id));
    }

    @Override
    public SucursalResponse registrar(SucursalRequest request) {

        log.info("Registrando nueva sucursal...");

        validarDatosUnicos(request);

        Sucursal sucursal = sucursalMapper.requestAEntidad(request);
        sucursalRepository.save(sucursal);

        log.info("Nueva sucursal registrada {}", sucursal.getNombre());

        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public SucursalResponse actualizar(SucursalRequest request, Long id) {

        Sucursal sucursal = obtenerSucursalException(id);

        validarCambiosUnicos(request, id);

        log.info("Actualizando sucursal con id {}", id);

        sucursal.actualizar(
                request.nombre(),
                request.direccion()
        );

        sucursalRepository.saveAndFlush(sucursal);

        log.info("Sucursal con id {} actualizada correctamente", id);

        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public void eliminar(Long id) {

        Sucursal sucursal = obtenerSucursalException(id);

        log.info("Eliminando sucursal con id {}", id);

        sucursalRepository.delete(sucursal);
        sucursalRepository.flush();

        log.info("Sucursal con id {} eliminada correctamente", id);
    }

    private Sucursal obtenerSucursalException(Long id){
        log.info("Buscando sucursal con id: {}", id);

        return sucursalRepository.findById(id).orElseThrow(
                () -> new RecursoNoEncontradoException("sucursal no encontrada con id: " + id)
        );
    }

    private void validarDatosUnicos(SucursalRequest request){

        log.info("Validando nombre único...");

        if (sucursalRepository.existsByNombreIgnoreCase(request.nombre().trim()))
            throw new ConflictoException(
                    "Ya existe una sucursal con el nombre de: " + request.nombre()
            );
    }

    private void validarCambiosUnicos(SucursalRequest request, Long id){

        log.info("Validando cambio en nombre único...");

        if (sucursalRepository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(), id))
            throw new ConflictoException(
                    "Ya existe una sucursal con el nombre de: " + request.nombre()
            );
    }
}
