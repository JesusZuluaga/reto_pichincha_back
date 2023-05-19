package com.reto.PruebaTecnica.infrastructure.sql_repository.cuenta;

import com.reto.PruebaTecnica.domain.model.cuenta.Cuenta;
import com.reto.PruebaTecnica.domain.model.cuenta.gateways.CuentaRepository;
import com.reto.PruebaTecnica.infrastructure.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class CuentaDataReactiveRepositoryAdapter implements CuentaRepository {


    @Autowired
    JPACuentaDataRepository repository;

    @Override
    public Flux<Cuenta> encontrarCuentas(String estado) {
        return Flux.fromIterable(repository.findByEstado(estado))
                .map(DataMapper::convertirCuentaDataACuenta)
                .onErrorResume(throwable -> Mono.error(throwable.getCause()));
    }

    @Override
    public Mono<Cuenta> encontrarCuentaPorId(Integer id) {
        return Mono.fromCallable(() -> repository.findById(id))
                .flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty))
                .map(DataMapper::convertirCuentaDataACuenta)
                .onErrorResume(throwable -> Mono.error(throwable));
    }

    @Override
    public Mono<Cuenta> guardarCuenta(Cuenta cuenta) {
        return Mono.fromCallable(() -> repository.save(DataMapper.convertirCuentaACuentaData(cuenta)))
                .map(DataMapper::convertirCuentaDataACuenta)
                .onErrorResume(throwable -> Mono.error(throwable.getCause()));
    }

    @Override
    public Mono<Cuenta> buscarPorNumeroCuenta(String numeroCuenta) {
        return Mono.fromCallable(() -> repository.findByNumeroCuenta(numeroCuenta))
                .map(DataMapper::convertirCuentaDataACuenta)
                .onErrorResume(throwable -> Mono.error(throwable.getCause()));
    }
}
