package com.reto.PruebaTecnica.domain.model.persona.gateways;


import com.reto.PruebaTecnica.domain.model.persona.Persona;
import reactor.core.publisher.Mono;

public interface PersonaRepository {
    Mono<Persona> save(Persona persona);
    Mono<Persona> findById(int personaId);
    Mono<Persona> encontrarPersonaPorId(Integer id);

    Mono<Persona> encontrarPorTipoYNumeroDocumento(String tipoDocumento, String numeroDocumento);
}
