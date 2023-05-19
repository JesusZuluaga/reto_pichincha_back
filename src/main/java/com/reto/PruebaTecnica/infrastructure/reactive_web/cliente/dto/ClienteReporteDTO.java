package com.reto.PruebaTecnica.infrastructure.reactive_web.cliente.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ClienteReporteDTO implements Serializable {
    private String nombres;
    private String direccion;
    private String telefono;
    private String contrasena;
    private String estado;
    private PersonaDTO persona;
    private static final long serialVersionUID = 1L;
}
