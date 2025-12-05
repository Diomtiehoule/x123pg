package com.df.fne.presenter.controller;

import com.df.fne.core.domaines.InvoiceDto;
import com.df.fne.core.services.InvoiceService;
import com.df.fne.presenter.response.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService){
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody  InvoiceDto invoiceDto){
        return ResponseEntity.ok(
                GenericResponse.success(invoiceService.create(invoiceDto), "Invoice Created successfully")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody InvoiceDto invoiceDto , @PathVariable UUID id){
        return ResponseEntity.ok(
                GenericResponse.success(invoiceService.update(invoiceDto , id) , "Invoice Updated Successfully")
        );
    }

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(
                GenericResponse.success(invoiceService.getAll(), "Invoice List")
        );
    }
}
