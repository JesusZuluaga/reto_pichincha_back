package com.reto.PruebaTecnica.infrastructure.reactive_web.movimiento.dto;

import com.reto.PruebaTecnica.infrastructure.reactive_web.cuenta.dto.CuentaDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class MovimientoDTO implements Serializable {
    private Integer id;
    private Date fechaMovimiento;
    private String tipoMovimiento;
    private Long valorMovimiento;
    private Long saldo;
    private Long saldoAnterior;
    private CuentaDTO cuenta;
    private static final long serialVersionUID = 1L;
}
