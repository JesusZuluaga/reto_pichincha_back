package com.reto.PruebaTecnica.infrastructure.sql_repository.cliente;

import com.reto.PruebaTecnica.infrastructure.sql_repository.cuenta.CuentaData;
import com.reto.PruebaTecnica.infrastructure.sql_repository.persona.PersonaData;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "cliente")
public class ClienteData{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Size(min = 4, max = 4, message = "El password debe tener 4 números.")
    @Column(name = "password")
    private String password;

    @NotEmpty
    @Size(min = 5, max = 25)
    @Column(name = "usuario", unique = true)
    private String usuario;

    @NotEmpty
    @Size(min = 5, max = 15)
    @Column(name = "estado")
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", referencedColumnName = "id")
    private PersonaData personaData;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clienteData")
    private List<CuentaData> cuentaDataList;
}
