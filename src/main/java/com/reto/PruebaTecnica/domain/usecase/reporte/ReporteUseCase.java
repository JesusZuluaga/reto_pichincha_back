package com.reto.PruebaTecnica.domain.usecase.reporte;

import com.reto.PruebaTecnica.domain.model.common.StringUtils;
import com.reto.PruebaTecnica.domain.model.common.ex.BusinessException;
import com.reto.PruebaTecnica.domain.model.movimiento.Movimiento;
import com.reto.PruebaTecnica.domain.model.movimiento.gateways.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RequiredArgsConstructor
@Component
public class ReporteUseCase {

    private final MovimientoRepository movimientoRepository;

    public Flux<Movimiento> generarReporteEntreFechas(String inicio, String fin) {
        validarExisteFecha(inicio, fin);
        Date fechaInicio = convertirFechaStringADate(inicio);
        Date fechaFinal = convertirFechaStringADate(fin);
        if (fechaInicio.after(fechaFinal)) {
            throw new BusinessException(BusinessException.Type.FECHA_INICIAL_MAYOR);
        }
        return movimientoRepository.generarReporteEntreFechas(fechaInicio, fechaFinal);
    }

    private Date convertirFechaStringADate(String fechaString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date fechaDate;
        try {
            fechaDate = formatter.parse(fechaString);
        } catch (ParseException e) {
            throw new BusinessException(BusinessException.Type.FORMATO_FECHA_INVALID);
        }
        return fechaDate;
    }


    private void validarExisteFecha(String inicio, String fin) {
        if (StringUtils.isEmpty(inicio, fin)) {
            throw new BusinessException(BusinessException.Type.FECHA_PARAMETRO_NO_ENCONTRADO);
        }
    }
}
