package com.reto.PruebaTecnica.infrastructure;

import com.reto.PruebaTecnica.domain.model.cliente.Cliente;
import com.reto.PruebaTecnica.domain.model.cuenta.Cuenta;
import com.reto.PruebaTecnica.domain.model.movimiento.Movimiento;
import com.reto.PruebaTecnica.domain.model.persona.Persona;
import com.reto.PruebaTecnica.infrastructure.sql_repository.cliente.ClienteData;
import com.reto.PruebaTecnica.infrastructure.sql_repository.cuenta.CuentaData;
import com.reto.PruebaTecnica.infrastructure.sql_repository.movimiento.MovimientoData;
import com.reto.PruebaTecnica.infrastructure.sql_repository.persona.PersonaData;

public class DataMapper {

    private DataMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Movimiento convertirMovimientoDataAMovimiento(MovimientoData movimientoData) {
        return Movimiento.builder()
                .id(movimientoData.getId())
                .fechaMovimiento(movimientoData.getFechaMovimiento())
                .tipoMovimiento(movimientoData.getTipoMovimiento())
                .valorMovimiento(movimientoData.getValorMovimiento())
                .saldo(movimientoData.getSaldo())
                .saldoAnterior(movimientoData.getSaldoAnterior())
                .cuenta(convertirCuentaDataACuenta(movimientoData.getCuentaData()))
                .build();
    }

    public static MovimientoData convertirMovimientoAMovimientoData(Movimiento movimiento) {
        return MovimientoData.builder()
                .id(movimiento.getId())
                .fechaMovimiento(movimiento.getFechaMovimiento())
                .tipoMovimiento(movimiento.getTipoMovimiento())
                .valorMovimiento(movimiento.getValorMovimiento())
                .saldo(movimiento.getSaldo())
                .saldoAnterior(movimiento.getSaldoAnterior())
                .cuentaData(convertirCuentaACuentaData(movimiento.getCuenta()))
                .build();
    }

    public static Cuenta convertirCuentaDataACuenta(CuentaData cuentaData) {
        return Cuenta.builder()
                .id(cuentaData.getId())
                .numeroCuenta(cuentaData.getNumeroCuenta())
                .tipoCuenta(cuentaData.getTipoCuenta())
                .saldoInicial(cuentaData.getSaldoInicial())
                .estado(cuentaData.getEstado())
                .cliente(convertirClienteDataACliente(cuentaData.getClienteData()))
                .build();
    }

    public static CuentaData convertirCuentaACuentaData(Cuenta cuenta) {
        return CuentaData.builder()
                .id(cuenta.getId())
                .numeroCuenta(cuenta.getNumeroCuenta())
                .tipoCuenta(cuenta.getTipoCuenta())
                .saldoInicial(cuenta.getSaldoInicial())
                .estado(cuenta.getEstado())
                .clienteData(convertirClienteAClienteData(cuenta.getCliente()))
                .build();
    }

    public static Cliente convertirClienteDataACliente(ClienteData clienteData) {
        return Cliente.builder()
                .id(clienteData.getId())
                .password(clienteData.getPassword())
                .usuario(clienteData.getUsuario())
                .password(clienteData.getPassword())
                .estado(clienteData.getEstado())
                .persona(convertirPersonaDataAPersona(clienteData.getPersonaData()))
                .build();
    }

    public static ClienteData convertirClienteAClienteData(Cliente cliente) {
        return ClienteData.builder()
                .id(cliente.getId())
                .estado(cliente.getEstado())
                .password(cliente.getPassword())
                .usuario(cliente.getUsuario())
                .password(cliente.getPassword())
                .personaData(convertirPersonaAPersonaData(cliente.getPersona()))
                .build();
    }

    public static Persona convertirPersonaDataAPersona(PersonaData personaData) {
        return Persona.builder()
                .id(personaData.getId())
                .numeroDocumento(personaData.getNumeroDocumento())
                .tipoDocumento(personaData.getTipoDocumento())
                .nombre(personaData.getNombre())
                .apellido(personaData.getApellido())
                .genero(personaData.getGenero())
                .direccion(personaData.getDireccion())
                .telefono(personaData.getTelefono())
                .build();
    }

    public static PersonaData convertirPersonaAPersonaData(Persona persona) {
        return PersonaData.builder()
                .id(persona.getId())
                .numeroDocumento(persona.getNumeroDocumento())
                .tipoDocumento(persona.getTipoDocumento())
                .nombre(persona.getNombre())
                .apellido(persona.getApellido())
                .genero(persona.getGenero())
                .direccion(persona.getDireccion())
                .telefono(persona.getTelefono())
                .build();
    }


}
