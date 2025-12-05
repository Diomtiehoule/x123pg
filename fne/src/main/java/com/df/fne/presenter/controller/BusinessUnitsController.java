package com.df.fne.presenter.controller;

import com.df.fne.core.domaines.BusinessUnitsDto;
import com.df.fne.core.services.BusinessUnitService;
import com.df.fne.presenter.response.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business-unit")
public class BusinessUnitsController {

    private final BusinessUnitService businessUnitService;

    public BusinessUnitsController(BusinessUnitService businessUnitService){
        this.businessUnitService = businessUnitService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BusinessUnitsDto businessUnitsDto){
        return ResponseEntity.ok(
                GenericResponse.success(businessUnitService.create(businessUnitsDto) , "BusinessUnit created successfully")
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(
                GenericResponse.success(businessUnitService.getAll() , "BusinessUnit List")
        );
    }
}
