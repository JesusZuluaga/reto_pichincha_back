package com.reto.PruebaTecnica.infrastructure.reactive_web.cuenta.dto;

import com.reto.PruebaTecnica.infrastructure.reactive_web.cliente.dto.ClienteDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CuentaDTO implements Serializable {
    private Integer id;
    private String numeroCuenta;
    private String tipoCuenta;
    private Long saldoInicial;
    private String estado;
    private ClienteDTO cliente;

    private static final long serialVersionUID = 1L;
}
