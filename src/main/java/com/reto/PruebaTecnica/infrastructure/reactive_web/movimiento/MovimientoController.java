package com.reto.PruebaTecnica.infrastructure.reactive_web.movimiento;

import com.reto.PruebaTecnica.domain.usecase.movimiento.MovimientoUseCase;
import com.reto.PruebaTecnica.infrastructure.reactive_web.DTOMapper;
import com.reto.PruebaTecnica.infrastructure.reactive_web.movimiento.dto.MovimientoDTO;
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
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    @Autowired
    MovimientoUseCase movimientoUseCase;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<MovimientoDTO> verTodosLosMovimientos() {
        return movimientoUseCase.obtenerMovimientos()
                .map(movimiento -> DTOMapper.movimientoAMovimientoDTO(movimiento));
    }

    @GetMapping("/{id}")
    public Mono<MovimientoDTO> encontrarPorId(@PathVariable Integer id) {
        return movimientoUseCase.encontrarPorId(id)
                .map(movimiento -> DTOMapper.movimientoAMovimientoDTO(movimiento));
    }

    @Transactional
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<MovimientoDTO> crearMovimiento(@Valid @RequestBody MovimientoDTO movimientoDTO) {
        return movimientoUseCase.guardarMovimiento(DTOMapper.movimientoDTOAMovimiento(movimientoDTO))
                .map(movimiento -> DTOMapper.movimientoAMovimientoDTO(movimiento));
    }

    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void eliminarMovimiento(@PathVariable Integer id) {
        movimientoUseCase.eliminarMovimiento(id);
    }

    @Transactional
    @PutMapping("/{id}")
    public Mono<MovimientoDTO> editarCliente(@Valid @RequestBody MovimientoDTO movimientoDTO, @PathVariable Integer id) {
        return movimientoUseCase.actualizarMovimiento(id, DTOMapper.movimientoDTOAMovimiento(movimientoDTO))
                .map(movimiento -> DTOMapper.movimientoAMovimientoDTO(movimiento));
    }
}
