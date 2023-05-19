package com.reto.PruebaTecnica.domain.usecase.cuenta;

import com.reto.PruebaTecnica.domain.model.cliente.gateways.ClienteRepository;
import com.reto.PruebaTecnica.domain.model.common.ValidationUtils;
import com.reto.PruebaTecnica.domain.model.common.ex.BusinessException;
import com.reto.PruebaTecnica.domain.model.cuenta.Cuenta;
import com.reto.PruebaTecnica.domain.model.cuenta.gateways.CuentaRepository;
import com.reto.PruebaTecnica.domain.model.persona.gateways.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;


@RequiredArgsConstructor
@Component
public class CuentaUseCase {

    public static final String INACTIVO = "INACTIVO";
    public static final String ACTIVO = "ACTIVO";
    private final CuentaRepository cuentaRepository;
    private final PersonaRepository personaRepository;
    private final ClienteRepository clienteRepository;


    public Flux<Cuenta> obtenerCuentas() {
        return cuentaRepository.encontrarCuentas(ACTIVO);
    }

    public Mono<Cuenta> obtenerCuentaPor(Integer id) {
        ValidationUtils.validarIdNulo(id);
        return cuentaRepository.encontrarCuentaPorId(id)
                .map(cuentaEncontrada -> {
                    if (Objects.isNull(cuentaEncontrada) || !cuentaEncontrada.getEstado().equalsIgnoreCase(ACTIVO)) {
                        throw new BusinessException(BusinessException.Type.CUENTA_NO_ENCONTRADA);
                    }
                    return cuentaEncontrada;
                });
    }


    public Mono<Cuenta> guardar(Cuenta cuenta) {
        ValidationUtils.validarCamposCuenta(cuenta);
        validarTipoCuenta(cuenta.getTipoCuenta());
        validarSiExisteClienteYPersona(cuenta);
        validarSiExisteCuenta(cuenta);
        return cuentaRepository.guardarCuenta(cuenta);
    }


    public Mono<String> eliminar(Integer id) {
        return obtenerCuentaPor(id)
                .flatMap(cuentaEncontrada -> cuentaRepository.guardarCuenta(cuentaEncontrada
                        .toBuilder()
                        .estado(INACTIVO)
                        .build()))
                .thenReturn("Eliminado")
                .onErrorResume(Mono::error);
    }

    public Mono<Cuenta> actualizar(Integer id, Cuenta cuenta) {
        ValidationUtils.validarIdNulo(id);
        ValidationUtils.validarCamposCuenta(cuenta);
        validarTipoCuenta(cuenta.getTipoCuenta());
        return cuentaRepository.buscarPorNumeroCuenta(cuenta.getNumeroCuenta())
                .flatMap(cuentaEnBaseDeDatos -> {
                    if (Objects.isNull(cuentaEnBaseDeDatos) || !cuentaEnBaseDeDatos.getEstado().equalsIgnoreCase(ACTIVO)) {
                        throw new BusinessException(BusinessException.Type.CUENTA_NO_ENCONTRADA);
                    }
                    return cuentaRepository.guardarCuenta(cuentaEnBaseDeDatos
                            .toBuilder()
                            .tipoCuenta(cuenta.getTipoCuenta())
                            .estado(cuenta.getEstado())
                            .saldoInicial(cuenta.getSaldoInicial())
                            .build());
                });
    }

    private void validarTipoCuenta(String tipoCuenta) {
        if (!tipoCuenta.equalsIgnoreCase("AHORRO") && !tipoCuenta.equalsIgnoreCase("CORRIENTE")) {
            throw new BusinessException(BusinessException.Type.TIPO_CUENTA_NO_VALIDO);
        }
    }

    private void validarSiExisteCuenta(Cuenta cuenta) {
        cuentaRepository.buscarPorNumeroCuenta(cuenta.getNumeroCuenta())
                .map(cuentaEncontrada -> {
                    if (Objects.nonNull(cuentaEncontrada)) {
                        throw new BusinessException(BusinessException.Type.CUENTA_YA_EXISTE);
                    }
                    return null;
                });
    }

    private void validarSiExisteClienteYPersona(Cuenta cuenta) {
        personaRepository.encontrarPorTipoYNumeroDocumento(
                cuenta.getCliente().getPersona().getTipoDocumento(),
                cuenta.getCliente().getPersona().getNumeroDocumento()
        ).map(existePersona -> {
            if (Objects.isNull(existePersona)) {
                throw new BusinessException(BusinessException.Type.ERROR_PERSONA_NO_REGISTRADA);
            }
            return null;
        });
        clienteRepository.encontrarPorId(cuenta.getCliente().getId())
                .map(existeCliente -> {
                    if (Objects.isNull(existeCliente)) {
                        throw new BusinessException(BusinessException.Type.ERROR_CLIENTE_NO_REGISTRADO);
                    }
                    return null;
                });
    }


}
