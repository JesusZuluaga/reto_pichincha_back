package com.reto.PruebaTecnica.infrastructure.reactive_web.cuenta;

import com.reto.PruebaTecnica.domain.usecase.cuenta.CuentaUseCase;
import com.reto.PruebaTecnica.infrastructure.reactive_web.DTOMapper;
import com.reto.PruebaTecnica.infrastructure.reactive_web.cuenta.dto.CuentaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@CrossOrigin(
        origins = "*",
        methods = {RequestMethod.GET, RequestMethod.PATCH, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
)
@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    @Autowired
    CuentaUseCase cuentaUseCase;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<CuentaDTO> verTodosLasCuentas() {
        return cuentaUseCase.obtenerCuentas()
                .map(cuenta -> DTOMapper.convertirCuentaACuentaDTO(cuenta));
    }


    @GetMapping("/{id}")
    public Mono<CuentaDTO> encontrarCuentaPorId(@PathVariable Integer id) {
        return cuentaUseCase.obtenerCuentaPor(id)
                .map(cuenta -> DTOMapper.convertirCuentaACuentaDTO(cuenta));
    }

    @Transactional
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<CuentaDTO> crearCuenta(@Valid @RequestBody CuentaDTO cuenta) {
        return  cuentaUseCase.guardar(DTOMapper.convertirCuentaDTOACuenta(cuenta))
                .map(cuentaGuardada -> DTOMapper.convertirCuentaACuentaDTO(cuentaGuardada));
    }

    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void eliminarCuenta(@PathVariable Integer id) {
        cuentaUseCase.eliminar(id);
    }


    @Transactional
    @PutMapping("/{id}")
    public Mono<CuentaDTO> editarCuenta(@Valid @RequestBody CuentaDTO cuenta, @PathVariable Integer id) {
        return cuentaUseCase.actualizar(id, DTOMapper.convertirCuentaDTOACuenta(cuenta))
                .map(cuentaGuardada -> DTOMapper.convertirCuentaACuentaDTO(cuentaGuardada));
    }
}
