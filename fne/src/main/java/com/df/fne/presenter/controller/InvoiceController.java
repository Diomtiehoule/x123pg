package com.df.fne.presenter.controller;

import com.df.fne.core.domaines.InvoiceDto;
import com.df.fne.core.services.InvoiceService;
import com.df.fne.presenter.response.GenericResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/invoice")
@Validated
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService){
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody  InvoiceDto invoiceDto){
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

    @GetMapping("/research")
    public ResponseEntity<?> filterInvoices(
            @RequestParam(required = false) String template,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String pos) {

        return ResponseEntity.ok(
                GenericResponse.success(invoiceService.filterInvoice(template, company, pos), "Invoices found")
        );
    }
}
