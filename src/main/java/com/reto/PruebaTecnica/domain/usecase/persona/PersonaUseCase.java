package com.reto.PruebaTecnica.domain.usecase.persona;

import com.reto.PruebaTecnica.domain.model.cliente.Cliente;
import com.reto.PruebaTecnica.domain.model.common.ex.BusinessException;
import com.reto.PruebaTecnica.domain.model.persona.Persona;
import com.reto.PruebaTecnica.domain.model.persona.gateways.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Component
public class PersonaUseCase {

    private final PersonaRepository personaRepository;

    public Mono<Persona> encontrarPorTipoYNumeroDocumento(String tipoDocumento, String numeroDocumento) {
        return personaRepository.encontrarPorTipoYNumeroDocumento(tipoDocumento, numeroDocumento);
    }

    public Mono<Persona> findById(int personaId) {
        return personaRepository.findById(personaId);
    }

    public Mono<Persona> guardarPersona(Persona persona) {
        return personaRepository.save(persona);
    }

    public Mono<Persona> editarPersona(Cliente cliente, Persona persona) {
        if (cliente.getPersona().getId() == null) {
            throw new BusinessException(BusinessException.Type.ERROR_CAMPO_NULL_PERSONA);
        }
        return guardarPersona(persona
                .toBuilder()
                .nombre(cliente.getPersona().getNombre())
                .apellido(cliente.getPersona().getApellido())
                .direccion(cliente.getPersona().getDireccion())
                .telefono(cliente.getPersona().getTelefono())
                .build());
    }

}
