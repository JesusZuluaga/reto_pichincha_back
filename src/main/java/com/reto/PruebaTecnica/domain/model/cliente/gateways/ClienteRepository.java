package com.reto.PruebaTecnica.domain.model.cliente.gateways;


import com.reto.PruebaTecnica.domain.model.cliente.Cliente;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface ClienteRepository {
    Flux<Cliente> encontrarClientes(String estado);

    Mono<Cliente> encontrarPorId(Integer id);

    Mono<Cliente> guardarCliente(Cliente cliente);
}
