package com.reto.PruebaTecnica.infrastructure.sql_repository.persona;

import com.reto.PruebaTecnica.domain.model.persona.Persona;
import com.reto.PruebaTecnica.domain.model.persona.gateways.PersonaRepository;
import com.reto.PruebaTecnica.infrastructure.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PersonaRepositoryAdapter implements PersonaRepository {

    @Autowired
    PersonaDataRepository repository;

    @Override
    public Mono<Persona> save(Persona persona) {
        return Mono.fromCallable(() -> repository.save(DataMapper.convertirPersonaAPersonaData(persona)))
                .map(personaData -> DataMapper.convertirPersonaDataAPersona(personaData))
                .onErrorResume(throwable -> Mono.error(throwable.getCause()));
    }

    @Override
    public Mono<Persona> findById(int personaId) {
        return Mono.fromCallable(() -> repository.findById(personaId))
                .flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty))
                .map(personaData -> DataMapper.convertirPersonaDataAPersona(personaData))
                .onErrorResume(throwable -> Mono.error(throwable.getCause()));
    }

    @Override
    public Mono<Persona> encontrarPersonaPorId(Integer id) {
        return Mono.fromCallable(() -> repository.findById(id))
                .flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty))
                .map(personaData -> DataMapper.convertirPersonaDataAPersona(personaData))
                .onErrorResume(throwable -> Mono.error(throwable.getCause()));
    }

    @Override
    public Mono<Persona> encontrarPorTipoYNumeroDocumento(String tipoDocumento, String numeroDocumento) {
        return Mono.fromCallable(() -> repository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento))
                .map(personaData -> DataMapper.convertirPersonaDataAPersona(personaData))
                .onErrorResume(throwable -> Mono.error(throwable.getCause()));
    }
}
