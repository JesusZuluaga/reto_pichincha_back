package com.reto.PruebaTecnica.infrastructure.sql_repository.cuenta;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface JPACuentaDataRepository extends CrudRepository<CuentaData, Integer> {
    CuentaData findByNumeroCuenta(String numeroCuenta);
    List<CuentaData> findByEstado(String estado);
}
