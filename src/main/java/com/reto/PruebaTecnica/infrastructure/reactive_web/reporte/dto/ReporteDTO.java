package com.reto.PruebaTecnica.infrastructure.reactive_web.reporte.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
public class ReporteDTO implements Serializable {
    private Integer idMovimiento;
    private String movimiento;
    private Long valorMovimiento;
    private Date fechaMovimiento;
    private String cliente;
    private String tipo;
    private Long saldoInicial;
    private String estado;
    private Long saldoDisponible;


    private static final long serialVersionUID = 1L;
}
