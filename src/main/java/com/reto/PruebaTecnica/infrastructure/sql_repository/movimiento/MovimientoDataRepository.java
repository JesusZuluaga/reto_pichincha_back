package com.reto.PruebaTecnica.infrastructure.sql_repository.movimiento;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.List;

@Repository
public interface MovimientoDataRepository extends CrudRepository<MovimientoData, Integer> {
    List<MovimientoData> findByFechaMovimientoBetween(Date inicio, Date fin);

    List<MovimientoData> findByCuentaDataId(Integer id);
}
