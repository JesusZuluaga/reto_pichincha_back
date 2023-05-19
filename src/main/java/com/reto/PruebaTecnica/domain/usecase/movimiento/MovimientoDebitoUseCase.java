package com.reto.PruebaTecnica.domain.usecase.movimiento;

import com.reto.PruebaTecnica.domain.model.common.ex.BusinessException;
import com.reto.PruebaTecnica.domain.model.cuenta.gateways.CuentaRepository;
import com.reto.PruebaTecnica.domain.model.movimiento.Movimiento;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Component
public class MovimientoDebitoUseCase {

    @Autowired
    CuentaRepository cuentaRepository;

    public Mono<Movimiento> construirMovimientoDebito(Movimiento movimiento) {
        if (movimiento.getId() == null) {
            return construirNuevoMovimiento(movimiento
                    .toBuilder()
                    .valorMovimiento(movimiento.getValorMovimiento() * -1L)
                    .build());
        }
        return construirReversionMovimiento(movimiento
                .toBuilder()
                .valorMovimiento(movimiento.getValorMovimiento() * -1L)
                .build());
    }

    private Mono<Movimiento> construirNuevoMovimiento(Movimiento movimiento) {
        validarSaldoDisponibleEnCuenta(movimiento);
        return cuentaRepository.encontrarCuentaPorId(movimiento.getCuenta().getId())
                .map(cuentaConsultada -> movimiento
                        .toBuilder()
                        .saldo(cuentaConsultada.getSaldoInicial() + movimiento.getValorMovimiento())
                        .saldoAnterior(cuentaConsultada.getSaldoInicial())
                        .cuenta(movimiento.getCuenta()
                                .toBuilder()
                                .saldoInicial(movimiento.getSaldo())
                                .build())
                        .build());
    }

    private Mono<Movimiento> construirReversionMovimiento(Movimiento movimiento) {
        validarSaldoDisponibleEnCuentaAlRevertir(movimiento);
        return Mono.just(movimiento
                .toBuilder()
                .saldo(movimiento.getSaldoAnterior() + movimiento.getValorMovimiento())
                .cuenta(movimiento.getCuenta()
                        .toBuilder()
                        .saldoInicial(movimiento.getSaldo())
                        .build())
                .build());
    }

    private void validarSaldoDisponibleEnCuenta(Movimiento movimiento) {
        cuentaRepository.encontrarCuentaPorId(movimiento.getCuenta().getId())
                .doOnNext(cuentaConsultada -> {
                    if (cuentaConsultada.getSaldoInicial() + movimiento.getValorMovimiento() < 0L) {
                        new BusinessException(BusinessException.Type.SALDO_INFERIOR_CERO);
                    }
                });
    }

    private void validarSaldoDisponibleEnCuentaAlRevertir(Movimiento movimiento) {
        if (movimiento.getSaldoAnterior() + movimiento.getValorMovimiento() < 0L) {
            throw new BusinessException(BusinessException.Type.SALDO_INFERIOR_CERO);
        }
    }
}
