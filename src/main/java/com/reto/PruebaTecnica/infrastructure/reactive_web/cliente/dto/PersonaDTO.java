package com.reto.PruebaTecnica.infrastructure.reactive_web.cliente.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class PersonaDTO implements Serializable {
    private Integer id;
    private String numeroDocumento;
    private String tipoDocumento;
    private String nombre;
    private String apellido;
    private String genero;
    private String direccion;
    private String telefono;

    private static final long serialVersionUID = 1L;
}
