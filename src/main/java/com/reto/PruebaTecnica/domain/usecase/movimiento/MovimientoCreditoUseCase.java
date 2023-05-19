package com.reto.PruebaTecnica.domain.usecase.movimiento;

import com.reto.PruebaTecnica.domain.model.cuenta.gateways.CuentaRepository;
import com.reto.PruebaTecnica.domain.model.movimiento.Movimiento;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class MovimientoCreditoUseCase {

    @Autowired
    CuentaRepository cuentaRepository;

    public Mono<Movimiento> construirMovimientoCredito(Movimiento movimiento) {
        if (movimiento.getId() == null) {
            return construirNuevoMovimiento(movimiento);
        }
        return construirReversionMovimiento(movimiento);
    }

    private Mono<Movimiento> construirReversionMovimiento(Movimiento movimiento) {
        return Mono.just(movimiento
                .toBuilder()
                .saldo(movimiento.getSaldoAnterior() + movimiento.getValorMovimiento())
                .cuenta(movimiento.getCuenta()
                        .toBuilder()
                        .saldoInicial(movimiento.getSaldo())
                        .build())
                .build());
    }

    private Mono<Movimiento> construirNuevoMovimiento(Movimiento movimiento) {
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
}
