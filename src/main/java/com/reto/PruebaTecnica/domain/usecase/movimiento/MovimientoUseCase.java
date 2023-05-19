package com.reto.PruebaTecnica.domain.usecase.movimiento;


import com.reto.PruebaTecnica.domain.model.cliente.gateways.ClienteRepository;
import com.reto.PruebaTecnica.domain.model.common.ValidationUtils;
import com.reto.PruebaTecnica.domain.model.common.ex.BusinessException;
import com.reto.PruebaTecnica.domain.model.cuenta.Cuenta;
import com.reto.PruebaTecnica.domain.model.cuenta.gateways.CuentaRepository;
import com.reto.PruebaTecnica.domain.model.movimiento.Movimiento;
import com.reto.PruebaTecnica.domain.model.movimiento.gateways.MovimientoRepository;
import com.reto.PruebaTecnica.domain.model.persona.gateways.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;


@RequiredArgsConstructor
@Component
public class MovimientoUseCase {

    public static final String DEBITO = "DEBITO";
    public static final String CREDITO = "CREDITO";
    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final PersonaRepository personaRepository;
    private final ClienteRepository clienteRepository;
    @Autowired
    MovimientoDebitoUseCase movimientoDebitoUseCase;
    @Autowired
    MovimientoCreditoUseCase movimientoCreditoUseCase;

    public Flux<Movimiento> obtenerMovimientos() {
        return movimientoRepository.verMovimientos();
    }

    public Mono<Movimiento> encontrarPorId(Integer id) {
        ValidationUtils.validarIdNulo(id);
        return movimientoRepository.encontrarPorId(id)
                .map(movimientoEncontrado -> {
                    if (Objects.isNull(movimientoEncontrado)) {
                        throw new BusinessException(BusinessException.Type.MOVIMIENTO_NO_ENCONTRADO);
                    }
                    return movimientoEncontrado;
                });
    }

    public Mono<Movimiento> guardarMovimiento(Movimiento movimiento) {
        ValidationUtils.validarCamposMovimiento(movimiento);
        validarEstructuraMovimiento(movimiento);
        if (movimiento.getValorMovimiento() <= 0L) {
            throw new BusinessException(BusinessException.Type.MOVIMIENTO_NO_PERMITIDO);
        }
        return aplicarMovimiento(movimiento)
                .flatMap(movimientoAplicado -> guardarNuevoSaldoEnCuenta(movimientoAplicado.getCuenta())
                        .flatMap(cuentaConNuevoSaldo -> movimientoRepository.crearMovimiento(movimientoAplicado
                                .toBuilder()
                                .build())));
    }

    public Mono<String> eliminarMovimiento(Integer id) {
        return encontrarPorId(id)
                .flatMap(movimiento -> movimientoRepository.deleteById(id))
                .thenReturn("Eliminado")
                .onErrorResume(Mono::error);
    }

    public Mono<Movimiento> actualizarMovimiento(Integer id, Movimiento movimiento) {
        ValidationUtils.validarPathConId(id, movimiento.getId());
        validarSiEsUltimoMovimiento(movimiento);
        return encontrarPorId(id)
                .flatMap(movimientoEnBaseDatos -> guardarMovimiento(movimientoEnBaseDatos
                        .toBuilder()
                        .fechaMovimiento(movimiento.getFechaMovimiento())
                        .tipoMovimiento(movimiento.getTipoMovimiento())
                        .valorMovimiento(movimiento.getValorMovimiento())
                        .saldo(movimiento.getSaldoAnterior())
                        .build()));
    }

    private Mono<Movimiento> aplicarMovimiento(Movimiento movimiento) {
        switch (movimiento.getTipoMovimiento().toUpperCase()) {

            case DEBITO:
                return movimientoDebitoUseCase.construirMovimientoDebito(movimiento);

            case CREDITO:
                return movimientoCreditoUseCase.construirMovimientoCredito(movimiento);

            default:
                throw new BusinessException(BusinessException.Type.TIPO_MOVIMIENTO_NO_VALIDO);
        }
    }

    private Mono<Cuenta> guardarNuevoSaldoEnCuenta(Cuenta cuenta) {
        return cuentaRepository.guardarCuenta(cuenta);
    }

    private void validarEstructuraMovimiento(Movimiento movimiento) {
        personaRepository.encontrarPorTipoYNumeroDocumento(
                movimiento.getCuenta().getCliente().getPersona().getTipoDocumento(),
                movimiento.getCuenta().getCliente().getPersona().getNumeroDocumento()
        ).map(existePersona -> {
            if (Objects.isNull(existePersona)) {
                throw new BusinessException(BusinessException.Type.ERROR_PERSONA_NO_REGISTRADA);
            }
            return null;
        });
        clienteRepository.encontrarPorId(movimiento.getCuenta().getCliente().getId())
                .map(existeCliente -> {
                    if (Objects.isNull(existeCliente)) {
                        throw new BusinessException(BusinessException.Type.ERROR_CLIENTE_NO_REGISTRADO);
                    }
                    return null;
                });
        cuentaRepository.buscarPorNumeroCuenta(movimiento.getCuenta().getNumeroCuenta())
                .map(existeCuenta -> {
                    if (Objects.isNull(existeCuenta)) {
                        throw new BusinessException(BusinessException.Type.ERROR_CUENTA_NO_REGISTRADO);
                    }
                    return null;
                });
    }

    private void validarSiEsUltimoMovimiento(Movimiento movimiento) {
        movimientoRepository.encontrarMovimientosPorCuentaAsociada(movimiento.getCuenta().getId())
                .map(movimientoActual -> {
                    if (movimientoActual.getId() > movimiento.getId()) {
                        throw new BusinessException(BusinessException.Type.MOVIMIENTO_NO_ES_ULTIMO);
                    }
                    return null;
                });
    }
}
