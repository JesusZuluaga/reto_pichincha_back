package com.reto.PruebaTecnica.domain.usecase.cliente;

import com.reto.PruebaTecnica.domain.model.cliente.Cliente;
import com.reto.PruebaTecnica.domain.model.cliente.gateways.ClienteRepository;
import com.reto.PruebaTecnica.domain.model.common.ValidationUtils;
import com.reto.PruebaTecnica.domain.model.common.ex.BusinessException;
import com.reto.PruebaTecnica.domain.usecase.persona.PersonaUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;


@RequiredArgsConstructor
@Component
public class ClienteUseCase {

    public static final String ESTADO_INACTIVO = "INACTIVO";
    public static final String ESTADO_ACTIVO = "ACTIVO";
    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    PersonaUseCase personaUseCase;

    public Flux<Cliente> obtenerClientes() {
        return clienteRepository.encontrarClientes(ESTADO_ACTIVO);
    }

    public Mono<Cliente> obtenerPor(Integer id) {
        return Mono.just(ValidationUtils.validarIdNulo(id))
                .flatMap(integer -> clienteRepository.encontrarPorId(id))
                .map(cliente -> {
                    if (Objects.isNull(cliente) || !cliente.getEstado().equalsIgnoreCase(ESTADO_ACTIVO)) {
                        throw new BusinessException(BusinessException.Type.ERROR_CLIENTE_NO_REGISTRADO);
                    }
                    return cliente;
                });
    }


    public Mono<Cliente> guardar(Cliente cliente) {
        return Mono.just(ValidationUtils.validarCamposCliente(cliente))
                .flatMap(clienteValidado -> personaUseCase.guardarPersona(cliente.getPersona())
                        .flatMap(personaCreada -> clienteRepository.guardarCliente(cliente
                                .toBuilder()
                                .persona(personaCreada)
                                .build())));
    }


    public Mono<String> inactivarPor(Integer id) {
        return obtenerPor(id)
                .flatMap(cliente -> clienteRepository.guardarCliente(cliente
                        .toBuilder()
                        .estado(ESTADO_INACTIVO)
                        .build()))
                .thenReturn("CLIENTE HA SIDO INACTIVADO")
                .onErrorResume(Mono::error);
    }

    public Mono<Cliente> actualizar(Integer id, Cliente cliente) {
        return Mono.just(ValidationUtils.validarCamposCliente(cliente))
                .flatMap(clienteEnBusqueda -> personaUseCase.encontrarPorTipoYNumeroDocumento(
                        clienteEnBusqueda.getPersona().getTipoDocumento(), clienteEnBusqueda.getPersona().getNumeroDocumento()))
                .flatMap(personaEnBaseDatos -> personaUseCase.editarPersona(cliente, personaEnBaseDatos))
                .flatMap(persona -> obtenerPor(id)
                        .flatMap(clienteEnBaseDeDatos -> clienteRepository.guardarCliente(clienteEnBaseDeDatos
                                .toBuilder()
                                .password(cliente.getPassword())
                                .persona(persona)
                                .build())));
    }

}
