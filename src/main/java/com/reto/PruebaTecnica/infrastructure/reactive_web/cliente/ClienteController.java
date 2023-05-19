package com.reto.PruebaTecnica.infrastructure.reactive_web.cliente;

import com.reto.PruebaTecnica.domain.usecase.cliente.ClienteUseCase;
import com.reto.PruebaTecnica.infrastructure.reactive_web.DTOMapper;
import com.reto.PruebaTecnica.infrastructure.reactive_web.cliente.dto.ClienteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@CrossOrigin(
        origins = {"http://localhost:4200"},
        methods = {RequestMethod.GET, RequestMethod.PATCH, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}
)
@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    @Autowired
    ClienteUseCase clienteUseCase;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ClienteDTO> verTodosLosClientes() {
        return clienteUseCase.obtenerClientes()
                .map(cliente -> DTOMapper.convertirClienteAClienteDTO(cliente));
    }

    @GetMapping("/{id}")
    public Mono<ClienteDTO> encontrarClientePorId(@PathVariable Integer id) {
        return clienteUseCase.obtenerPor(id)
                .map(cliente -> DTOMapper.convertirClienteAClienteDTO(cliente));
    }

    @Transactional
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Mono<ClienteDTO> crearCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        return clienteUseCase.guardar(DTOMapper.convertirClienteDTOACliente(clienteDTO))
                .map(cliente -> DTOMapper.convertirClienteAClienteDTO(cliente));
    }

    @Transactional
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarCliente(@PathVariable Integer id) {
        clienteUseCase.inactivarPor(id);
    }

    @Transactional
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ClienteDTO> editarCliente(@Valid @RequestBody ClienteDTO clienteDTO, @PathVariable Integer id) {
        return clienteUseCase.actualizar(id, DTOMapper.convertirClienteDTOACliente(clienteDTO))
                .map(cliente -> DTOMapper.convertirClienteAClienteDTO(cliente));
    }





}
