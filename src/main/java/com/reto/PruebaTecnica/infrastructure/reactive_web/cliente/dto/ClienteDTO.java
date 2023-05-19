package com.reto.PruebaTecnica.infrastructure.reactive_web.cliente.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ClienteDTO   implements Serializable {
    private Integer id;
    private String password;
    private String usuario;
    private String estado;
    private PersonaDTO persona;
    private static final long serialVersionUID = 1L;
}
