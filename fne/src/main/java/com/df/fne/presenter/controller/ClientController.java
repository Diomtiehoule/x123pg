package com.df.fne.presenter.controller;

import com.df.fne.core.domaines.ClientDto;
import com.df.fne.core.services.ClientService;
import com.df.fne.presenter.response.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ClientDto clientDto){
        return ResponseEntity.ok(
                GenericResponse.success(clientService.create(clientDto) , "Client Created Successfully")
        );
    }

    @GetMapping("/find")
    public ResponseEntity<?> findClientByNameReasonSocial(@RequestParam String nameReasonSocial){
        return ResponseEntity.ok(
                GenericResponse.success(clientService.findByNameReasonSocial(nameReasonSocial) , "Client found")
        );
    }


    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(
                GenericResponse.success(clientService.getAll(), "Client list")
        );
    }
}
