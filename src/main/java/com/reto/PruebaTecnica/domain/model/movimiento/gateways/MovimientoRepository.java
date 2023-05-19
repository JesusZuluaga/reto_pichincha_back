package com.reto.PruebaTecnica.domain.model.movimiento.gateways;


import com.reto.PruebaTecnica.domain.model.movimiento.Movimiento;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;


public interface MovimientoRepository {
    Flux<Movimiento> verMovimientos();

    Mono<Movimiento> encontrarPorId(Integer id);

    Mono<Movimiento>  crearMovimiento(Movimiento movimiento);

    Flux<Movimiento> generarReporteEntreFechas(Date inicio, Date fin);

    Mono<String> deleteById(Integer id);

    Flux<Movimiento> encontrarMovimientosPorCuentaAsociada(Integer id);
}
