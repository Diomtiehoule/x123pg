package com.df.fne.jpa.serviceImpl;

import com.df.fne.core.domaines.InvoiceDto;
import com.df.fne.core.domaines.InvoiceItemsDto;
import com.df.fne.core.exceptions.NotFoundException;
import com.df.fne.core.mappers.InvoiceMapper;
import com.df.fne.core.services.InvoiceService;
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
    private final CertificateDgeService certificateDgeService;
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public InvoiceServiceImpl(InvoiceMapper invoiceMapper , InvoiceRepository invoiceRepository , CertificateDgeService certificateDgeService , UserRepository userRepository){
        this.invoiceMapper = invoiceMapper;
        this.userRepository = userRepository;
        this.invoiceRepository = invoiceRepository;
        this.certificateDgeService = certificateDgeService;
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

        invoice.setStatusFne("PENDING");

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
    public InvoiceDto update(InvoiceDto invoiceDto, UUID id) {

        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found"));

        existingInvoice.setInvoiceNumber(invoiceDto.getInvoiceNumber());
        existingInvoice.setInvoiceDate(invoiceDto.getInvoiceDate());
        existingInvoice.setInvoiceType(invoiceDto.getInvoiceType());
        existingInvoice.setSource(invoiceDto.getSource());
        existingInvoice.setPaymentMethod(invoiceDto.getPaymentMethod());
        existingInvoice.setTemplate(invoiceDto.getTemplate());
        existingInvoice.setPointOfSale(invoiceDto.getPointOfSale());
        existingInvoice.setCurrency(invoiceDto.getCurrency());
        existingInvoice.setCurrencyRate(invoiceDto.getCurrencyRate());
        existingInvoice.setEstablishment(invoiceDto.getEstablishment());
        existingInvoice.setClientSellerName(invoiceDto.getClientSellerName());
        existingInvoice.setCommercialMessage(invoiceDto.getCommercialMessage());
        existingInvoice.setFooter(invoiceDto.getFooter());
        existingInvoice.setRne(invoiceDto.isRne());
        existingInvoice.setRneReceipt(invoiceDto.getRneReceipt());
        existingInvoice.setStatusFne(invoiceDto.getStatusFne());
        existingInvoice.setFneMessageReturn(invoiceDto.getFneMessageReturn());
        existingInvoice.setFneReference(invoiceDto.getFneReference());
        existingInvoice.setFneToken(invoiceDto.getFneToken());
        existingInvoice.setFneId(invoiceDto.getFneId());
        existingInvoice.setFneCc(invoiceDto.getFneCc());
        existingInvoice.setFneBalanceSticker(invoiceDto.getFneBalanceSticker());

        existingInvoice.getItems().clear();
        if (invoiceDto.getItems() != null) {
            for (InvoiceItemsDto itemDto : invoiceDto.getItems()) {
                InvoiceItems item = new InvoiceItems();
                item.setItemRef(itemDto.getItemRef());
                item.setDescription(itemDto.getDescription());
                item.setQuantity(itemDto.getQuantity());
                item.setUnitPrice(itemDto.getUnitPrice());
                item.setDiscountAmount(itemDto.getDiscountAmount());
                item.setMeasureUnit(itemDto.getMeasureUnit());

                item.setInvoice(existingInvoice);


                if (itemDto.getCustomTaxes() != null) {
                    for (InvoiceItemsDto.CustomTaxDto ctDto : itemDto.getCustomTaxes()) {
                        ItemTax tax = new ItemTax();
                        tax.setName(ctDto.getName());
                        tax.setAmount(ctDto.getAmount());
                        tax.setInvoiceItem(item);
                        if (item.getCustomTaxes() == null) {
                            item.setCustomTaxes(new ArrayList<>());
                        }
                        item.getCustomTaxes().add(tax);
                    }
                }

                existingInvoice.getItems().add(item);
            }
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
