package com.df.fne.presenter.controller;

import com.df.fne.core.services.TvaService;
import com.df.fne.presenter.response.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tva")
public class TvaController {

    private final TvaService tvaService;

    public TvaController(TvaService tvaService){
        this.tvaService = tvaService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(
                GenericResponse.success(tvaService.getAll(), "Tva list")
        );
    }
}
