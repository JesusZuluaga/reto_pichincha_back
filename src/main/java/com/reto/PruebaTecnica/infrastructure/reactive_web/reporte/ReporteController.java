package com.reto.PruebaTecnica.infrastructure.reactive_web.reporte;

import com.reto.PruebaTecnica.domain.usecase.reporte.ReporteUseCase;
import com.reto.PruebaTecnica.infrastructure.reactive_web.DTOMapper;
import com.reto.PruebaTecnica.infrastructure.reactive_web.reporte.dto.ReporteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;


@CrossOrigin(
        origins = "*",
        methods = {RequestMethod.GET}
)
@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    @Autowired
    ReporteUseCase reporteUseCase;

    @GetMapping
    public Flux<ReporteDTO> generarReporte(String inicio, String fin) {
        return reporteUseCase.generarReporteEntreFechas(inicio, fin)
                .map(movimiento -> DTOMapper.movimientoAReporteDTO(movimiento));
    }

}
