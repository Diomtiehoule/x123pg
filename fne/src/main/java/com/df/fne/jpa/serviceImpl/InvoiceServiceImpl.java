package com.df.fne.jpa.serviceImpl;

import com.df.fne.core.domaines.InvoiceDto;
import com.df.fne.core.domaines.InvoiceItemsDto;
import com.df.fne.core.domaines.enums.PaymentMethod;
import com.df.fne.core.exceptions.NotFoundException;
import com.df.fne.core.mappers.InvoiceItemsMapper;
import com.df.fne.core.mappers.InvoiceMapper;
import com.df.fne.core.services.InvoiceService;
import com.df.fne.core.utils.RandomGenerator;
import com.df.fne.infras.CertificateDgeService;
import com.df.fne.jpa.entities.*;
import com.df.fne.jpa.repositories.InvoiceRepository;
import com.df.fne.jpa.repositories.UserRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceItemsMapper invoiceItemsMapper;
    private final CertificateDgeService certificateDgeService;
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public InvoiceServiceImpl(InvoiceMapper invoiceMapper , InvoiceRepository invoiceRepository , CertificateDgeService certificateDgeService , UserRepository userRepository , InvoiceItemsMapper invoiceItemsMapper){
        this.invoiceMapper = invoiceMapper;
        this.userRepository = userRepository;
        this.invoiceRepository = invoiceRepository;
        this.certificateDgeService = certificateDgeService;
        this.invoiceItemsMapper = invoiceItemsMapper;
    }

    @Override
    @Transactional
    public InvoiceDto create(InvoiceDto dto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("user connecté"+ auth);
        String username = auth.getName();
        System.out.println("username"+ username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        BusinessUnits bu = user.getBusinessUnits();

        Invoice invoice = invoiceMapper.toEntity(dto);

        invoice.setUser(user);
        invoice.setBusinessUnits(bu);


        attachCustomTaxes(invoice, dto);
        String invoiceNumber = RandomGenerator.generateRandomCode(10);

        invoice.setStatusFne("PENDING");
        invoice.setInvoiceNumber(invoiceNumber);

        Invoice savedInvoice = invoiceRepository.save(invoice);

        Map<String, Object> dgResponse = certificateDgeService.certificate(savedInvoice);
        updateInvoiceWithDgeResponse(savedInvoice, dgResponse);

        Invoice finalInvoice = invoiceRepository.save(savedInvoice);

        return invoiceMapper.toDto(finalInvoice);
    }


    private void attachCustomTaxes(Invoice invoice, InvoiceDto dto) {

        if (invoice.getItems() == null) return;

        for (InvoiceItems item : invoice.getItems()) {

            item.setInvoice(invoice);

            dto.getItems().stream()
                    .filter(i -> i.getItemRef().equals(item.getItemRef()))
                    .findFirst()
                    .map(InvoiceItemsDto::getCustomTaxes)
                    .ifPresent(customTaxes ->
                            customTaxes.forEach(t -> {
                                ItemTax tax = new ItemTax();
                                tax.setName(t.getName());
                                tax.setAmount(t.getAmount());
                                item.addCustomTax(tax);
                            })
                    );
        }
    }

    private void updateInvoiceWithDgeResponse(Invoice invoice, Map<String, Object> dgiResponse) {

        String bodyJson = (String) dgiResponse.get("body");

        try {
            Map<String, Object> body = mapper.readValue(bodyJson, Map.class);

            Map<String, Object> invoiceResp = (Map<String, Object>) body.get("invoice");

            invoice.setFneId((String) invoiceResp.get("id"));
            invoice.setFneToken((String) body.get("token"));
            invoice.setFneReference((String) body.get("reference"));
            invoice.setFneCc(
                    Optional.ofNullable((String) invoiceResp.get("clientNcc"))
                            .orElse((String) body.get("ncc"))
            );

            invoice.setStatusFne("CERTIFICATED");
            invoice.setResponseDgi(bodyJson);

        } catch (Exception e) {
            System.out.println("Error parsing DGE : " + e.getMessage());
            invoice.setStatusFne("FAILED");
            invoice.setResponseDgi(dgiResponse.toString());
        }
    }


    @Override
    @Transactional
    public InvoiceDto update(InvoiceDto invoiceDto, UUID id) {
        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found"));

        invoiceMapper.updateInvoiceFromDto(invoiceDto, existingInvoice);

        if (invoiceDto.getItems() != null) {
            existingInvoice.getItems().clear();
            invoiceDto.getItems().forEach(itemDto -> {
                InvoiceItems item = invoiceItemsMapper.toEntity(itemDto);
                item.setInvoice(existingInvoice);
                existingInvoice.getItems().add(item);
            });
        }

        Invoice savedInvoice = invoiceRepository.save(existingInvoice);
        return invoiceMapper.toDto(savedInvoice);
    }




    @Override
    public InvoiceDto get(UUID id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(()-> new NotFoundException("Invoice not found"));
        return invoiceMapper.toDto(invoice);
    }

    @Override
    public List<InvoiceDto> getAll() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoiceMapper.toDtoList(invoices);
    }

    @Override
    public void delete(UUID id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new NotFoundException("Invoice not found"));
        invoiceRepository.deleteById(invoice.getId());
    }
}
