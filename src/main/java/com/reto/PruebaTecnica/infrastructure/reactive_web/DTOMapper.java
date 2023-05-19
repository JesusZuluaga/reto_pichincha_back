package com.reto.PruebaTecnica.infrastructure.reactive_web;

import com.reto.PruebaTecnica.domain.model.cliente.Cliente;
import com.reto.PruebaTecnica.domain.model.cuenta.Cuenta;
import com.reto.PruebaTecnica.domain.model.movimiento.Movimiento;
import com.reto.PruebaTecnica.domain.model.persona.Persona;
import com.reto.PruebaTecnica.infrastructure.reactive_web.cliente.dto.ClienteDTO;
import com.reto.PruebaTecnica.infrastructure.reactive_web.cliente.dto.PersonaDTO;
import com.reto.PruebaTecnica.infrastructure.reactive_web.cuenta.dto.CuentaDTO;
import com.reto.PruebaTecnica.infrastructure.reactive_web.movimiento.dto.MovimientoDTO;
import com.reto.PruebaTecnica.infrastructure.reactive_web.reporte.dto.ReporteDTO;

import java.util.ArrayList;
import java.util.List;

public class DTOMapper {
    private DTOMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static PersonaDTO convertirPersonaAPersonaDTO(Persona persona) {
        PersonaDTO personaDTO = new PersonaDTO();
        personaDTO.setId(persona.getId());
        personaDTO.setNumeroDocumento(persona.getNumeroDocumento());
        personaDTO.setTipoDocumento(persona.getTipoDocumento());
        personaDTO.setNombre(persona.getNombre());
        personaDTO.setApellido(persona.getApellido());
        personaDTO.setDireccion(persona.getDireccion());
        personaDTO.setTelefono(persona.getTelefono());
        personaDTO.setGenero(persona.getGenero());
        return personaDTO;
    }

    public static Persona convertirPersonaDTOAPersona(PersonaDTO personaDTO) {
        return Persona.builder()
                .id(personaDTO.getId() != null ? personaDTO.getId() : null)
                .numeroDocumento(personaDTO.getNumeroDocumento())
                .tipoDocumento(personaDTO.getTipoDocumento())
                .nombre(personaDTO.getNombre())
                .apellido(personaDTO.getApellido())
                .direccion(personaDTO.getDireccion())
                .telefono(personaDTO.getTelefono())
                .genero(personaDTO.getGenero())
                .build();
    }

    public static ClienteDTO convertirClienteAClienteDTO(Cliente cliente) {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(cliente.getId());
        clienteDTO.setPassword(cliente.getPassword());
        clienteDTO.setUsuario(cliente.getUsuario());
        clienteDTO.setEstado(cliente.getEstado());
        clienteDTO.setPersona(convertirPersonaAPersonaDTO(cliente.getPersona()));

        return clienteDTO;
    }

    public static Cliente convertirClienteDTOACliente(ClienteDTO clienteDTO) {
        return Cliente.builder()
                .id(clienteDTO.getId() != null ? clienteDTO.getId() : null)
                .password(clienteDTO.getPassword())
                .usuario(clienteDTO.getUsuario())
                .estado(clienteDTO.getEstado())
                .persona(convertirPersonaDTOAPersona(clienteDTO.getPersona()))
                .build();
    }


    public static CuentaDTO convertirCuentaACuentaDTO(Cuenta cuenta) {
        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setId(cuenta.getId());
        cuentaDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
        cuentaDTO.setTipoCuenta(cuenta.getTipoCuenta());
        cuentaDTO.setSaldoInicial(cuenta.getSaldoInicial());
        cuentaDTO.setEstado(cuenta.getEstado());
        cuentaDTO.setCliente(convertirClienteAClienteDTO(cuenta.getCliente()));
        return cuentaDTO;
    }

    public static Cuenta convertirCuentaDTOACuenta(CuentaDTO cuentaDTO) {
        return Cuenta.builder()
                .id(cuentaDTO.getId() != null ? cuentaDTO.getId() : null)
                .numeroCuenta(cuentaDTO.getNumeroCuenta())
                .numeroCuenta(cuentaDTO.getNumeroCuenta())
                .tipoCuenta(cuentaDTO.getTipoCuenta())
                .saldoInicial(cuentaDTO.getSaldoInicial())
                .estado(cuentaDTO.getEstado())
                .cliente(convertirClienteDTOACliente(cuentaDTO.getCliente()))
                .build();
    }


    public static MovimientoDTO movimientoAMovimientoDTO(Movimiento movimiento) {
        MovimientoDTO movimientoDTO = new MovimientoDTO();
        movimientoDTO.setId(movimiento.getId());
        movimientoDTO.setFechaMovimiento(movimiento.getFechaMovimiento());
        movimientoDTO.setTipoMovimiento(movimiento.getTipoMovimiento());
        movimientoDTO.setValorMovimiento(movimiento.getValorMovimiento());
        movimientoDTO.setSaldo(movimiento.getSaldo());
        movimientoDTO.setSaldoAnterior(movimiento.getSaldoAnterior());
        movimientoDTO.setCuenta(convertirCuentaACuentaDTO(movimiento.getCuenta()));

        return movimientoDTO;
    }

    public static Movimiento movimientoDTOAMovimiento(MovimientoDTO movimientoDTO) {
        return Movimiento.builder()
                .id(movimientoDTO.getId() != null ? movimientoDTO.getId() : null)
                .fechaMovimiento(movimientoDTO.getFechaMovimiento())
                .tipoMovimiento(movimientoDTO.getTipoMovimiento())
                .valorMovimiento(movimientoDTO.getValorMovimiento())
                .saldo(movimientoDTO.getSaldo())
                .saldoAnterior(movimientoDTO.getSaldoAnterior())
                .cuenta(convertirCuentaDTOACuenta(movimientoDTO.getCuenta()))
                .build();
    }

    public static ReporteDTO movimientoAReporteDTO(Movimiento movimiento) {
        ReporteDTO reporteDTO = new ReporteDTO();
        reporteDTO.setIdMovimiento(movimiento.getId());
        reporteDTO.setMovimiento(movimiento.getTipoMovimiento());
        reporteDTO.setValorMovimiento(movimiento.getValorMovimiento());
        reporteDTO.setFechaMovimiento(movimiento.getFechaMovimiento());
        reporteDTO.setCliente(movimiento.getCuenta().getCliente().getPersona().getNombre() + " " +
                movimiento.getCuenta().getCliente().getPersona().getApellido());
        reporteDTO.setTipo(movimiento.getCuenta().getTipoCuenta());
        reporteDTO.setSaldoInicial(
                movimiento.getSaldoAnterior()
        );
        reporteDTO.setEstado(movimiento.getCuenta().getEstado());
        reporteDTO.setSaldoDisponible(movimiento.getSaldo());

        return reporteDTO;
    }


}
