package com.reto.PruebaTecnica.infrastructure.sql_repository.cliente;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface JPAClientDataRepository extends CrudRepository<ClienteData, Integer> {
    @Query("select c from ClienteData c join fetch c.personaData ")
    List<ClienteData> findByEstado(String estado);
}
