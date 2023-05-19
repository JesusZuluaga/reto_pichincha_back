package com.reto.PruebaTecnica.domain.model.common;


import com.reto.PruebaTecnica.domain.model.cliente.Cliente;
import com.reto.PruebaTecnica.domain.model.common.ex.BusinessException;
import com.reto.PruebaTecnica.domain.model.cuenta.Cuenta;
import com.reto.PruebaTecnica.domain.model.movimiento.Movimiento;
import com.reto.PruebaTecnica.domain.model.persona.Persona;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static void verify(BusinessException.Type error) {
        if (nonNull(error)) {
            throw new BusinessException(error);
        }
    }

    public static BusinessException.Type required(BusinessException.Type[] exceptions, Object[] data) {
        BusinessException.Type required = null;
        for (int i = 0; i < data.length && isNull(required); i++) {
            boolean isRequired = (data[i] instanceof String && StringUtils.isEmpty((String) data[i])) || isNull(data[i]);

            if (isRequired) {
                required = exceptions[i];
            }
        }
        return required;
    }

    public static void validarPathConId (Integer idPath, Integer idObjeto){
        validarIdNulo(idObjeto);
        validarIdNulo(idPath);
        if (idPath != idObjeto) {
            throw new BusinessException(BusinessException.Type.PATH_NO_IGUAL_ID);
        }
    }

    public static Integer validarIdNulo(Integer id) {
        if (id == null || id < 0) {
            throw new BusinessException(BusinessException.Type.ID_NULL);
        }
        return id;
    }

    public static Persona validarCamposPersona(Persona persona) {
        if (StringUtils.isEmpty(persona.getNumeroDocumento(), persona.getNombre(), persona.getApellido(),
                persona.getDireccion(), persona.getTelefono())) {
            throw new BusinessException(BusinessException.Type.ERROR_CAMPO_NULL_PERSONA);
        }
        return persona;
    }

    public static Cliente validarCamposCliente(Cliente cliente) {
        if (Objects.isNull(cliente.getPersona()) ||
                StringUtils.isEmpty(cliente.getPassword(), cliente.getUsuario(), cliente.getEstado())) {
            throw new BusinessException(BusinessException.Type.ERROR_CAMPO_NULL_CLIENTE);
        }
        validarCamposPersona(cliente.getPersona());
        return cliente;
    }

    public static void validarCamposCuenta(Cuenta cuenta) {
        if (StringUtils.isEmpty(cuenta.getNumeroCuenta(), cuenta.getTipoCuenta(), cuenta.getEstado())
                || cuenta.getSaldoInicial() == null || Objects.isNull(cuenta.getCliente())){
            throw new BusinessException(BusinessException.Type.ERROR_CAMPO_NULL_CUENTA);
        }
        validarCamposCliente(cuenta.getCliente());
    }

    public static void validarCamposMovimiento(Movimiento movimiento) {
        if (
                movimiento.getFechaMovimiento() == null || movimiento.getValorMovimiento() == null
                        || movimiento.getTipoMovimiento() == null || movimiento.getSaldo() == null
                        || Objects.isNull(movimiento.getCuenta())
        ) {
            throw new BusinessException(BusinessException.Type.ERROR_CAMPO_NULL_MOVIMIENTO);
        }
        validarCamposCuenta(movimiento.getCuenta());
    }
}
