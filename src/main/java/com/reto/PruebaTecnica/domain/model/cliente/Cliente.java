package com.reto.PruebaTecnica.domain.model.cliente;

import com.reto.PruebaTecnica.domain.model.cuenta.Cuenta;
import com.reto.PruebaTecnica.domain.model.persona.Persona;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Cliente {
    private Integer id;
    private String password;
    private String usuario;
    public String estado;
    private Persona persona;
    private List<Cuenta> cuentaDataList;
}
