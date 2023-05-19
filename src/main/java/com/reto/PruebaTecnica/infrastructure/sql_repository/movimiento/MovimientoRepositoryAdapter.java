package com.reto.PruebaTecnica.infrastructure.sql_repository.movimiento;

import com.reto.PruebaTecnica.domain.model.movimiento.Movimiento;
import com.reto.PruebaTecnica.domain.model.movimiento.gateways.MovimientoRepository;
import com.reto.PruebaTecnica.infrastructure.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class MovimientoRepositoryAdapter implements MovimientoRepository {

    @Autowired
    MovimientoDataRepository repository;

    @Override
    public Flux<Movimiento> verMovimientos() {
        return Flux.fromIterable(repository.findAll())
                .map(DataMapper::convertirMovimientoDataAMovimiento)
                .onErrorResume(throwable -> Mono.error(throwable.getCause()));
    }

    @Override
    public Mono<Movimiento> encontrarPorId(Integer id) {
        return Mono.fromCallable(() -> repository.findById(id))
                .flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty))
                .map(DataMapper::convertirMovimientoDataAMovimiento)
                .onErrorResume(throwable -> Mono.error(throwable.getCause()));
    }

    @Override
    public Mono<Movimiento> crearMovimiento(Movimiento movimiento) {
        return Mono.fromCallable(() -> repository.save(DataMapper.convertirMovimientoAMovimientoData(movimiento)))
                .map(DataMapper::convertirMovimientoDataAMovimiento)
                .onErrorResume(throwable -> Mono.error(throwable.getCause()));
    }

    @Override
    public Flux<Movimiento> generarReporteEntreFechas(Date inicio, Date fin) {
        return Flux.fromIterable(repository.findByFechaMovimientoBetween(inicio, fin))
                .map(DataMapper::convertirMovimientoDataAMovimiento)
                .onErrorResume(throwable -> Mono.error(throwable.getCause()));
    }

    @Override
    public Mono<String> deleteById(Integer id) {
        try {
            repository.deleteById(id);
            return Mono.just("ELIMINADO");
        }catch (Exception e){
            return Mono.error(e);
        }
    }

    @Override
    public Flux<Movimiento> encontrarMovimientosPorCuentaAsociada(Integer id) {
        return Flux.fromIterable(repository.findByCuentaDataId(id))
                .map(DataMapper::convertirMovimientoDataAMovimiento)
                .onErrorResume(throwable -> Mono.error(throwable.getCause()));
    }
}
