package com.reto.PruebaTecnica.infrastructure.sql_repository.cliente;

import com.reto.PruebaTecnica.domain.model.cliente.Cliente;
import com.reto.PruebaTecnica.domain.model.cliente.gateways.ClienteRepository;
import com.reto.PruebaTecnica.infrastructure.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ClienteDataReactiveRepository implements ClienteRepository {

    @Autowired
    JPAClientDataRepository repository;

    @Override
    public Flux<Cliente> encontrarClientes(String estado) {
        return Flux.fromIterable(repository.findByEstado(estado))
                .map(DataMapper::convertirClienteDataACliente)
                .onErrorResume(Mono::error);
    }

    @Override
    public Mono<Cliente> encontrarPorId(Integer id) {
        return Mono.fromCallable(() -> repository.findById(id))
                .flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty))
                .map(DataMapper::convertirClienteDataACliente)
                .onErrorResume(throwable -> Mono.error(throwable));
    }

    @Override
    public Mono<Cliente> guardarCliente(Cliente cliente) {
        return Mono.fromCallable(() -> repository.save(DataMapper.convertirClienteAClienteData(cliente)))
                .map(clienteData -> DataMapper.convertirClienteDataACliente(clienteData))
                .onErrorResume(throwable -> Mono.error(throwable));
    }
}
