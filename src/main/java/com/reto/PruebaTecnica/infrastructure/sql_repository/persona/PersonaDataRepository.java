package com.reto.PruebaTecnica.infrastructure.sql_repository.persona;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PersonaDataRepository extends CrudRepository<PersonaData, Integer> {
    PersonaData findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);
}
