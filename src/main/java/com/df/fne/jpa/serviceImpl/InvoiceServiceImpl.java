package com.df.fne.jpa.serviceImpl;

import com.df.fne.core.domaines.InvoiceDto;
import com.df.fne.core.domaines.InvoiceItemsDto;
import com.df.fne.core.exceptions.NotFoundException;
import com.df.fne.core.mappers.InvoiceMapper;
import com.df.fne.core.services.InvoiceService;
import com.df.fne.infras.CertificateDgeService;
import com.df.fne.jpa.entities.Invoice;
import com.df.fne.jpa.entities.InvoiceItems;
import com.df.fne.jpa.entities.ItemTax;
import com.df.fne.jpa.repositories.InvoiceRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final CertificateDgeService certificateDgeService;

    public InvoiceServiceImpl(InvoiceMapper invoiceMapper , InvoiceRepository invoiceRepository , CertificateDgeService certificateDgeService){
        this.invoiceMapper = invoiceMapper;
        this.invoiceRepository = invoiceRepository;
        this.certificateDgeService = certificateDgeService;
    }

    @Override
    @Transactional
    public InvoiceDto create(InvoiceDto invoiceDto) {

        Invoice invoice = invoiceMapper.toEntity(invoiceDto);

        if (invoice.getItems() != null) {
            for (InvoiceItems item : invoice.getItems()) {
                item.setInvoice(invoice);

                List<InvoiceItemsDto.CustomTaxDto> customTaxesDto =
                        invoiceDto.getItems().stream()
                                .filter(i -> i.getItemRef().equals(item.getItemRef()))
                                .findFirst()
                                .map(InvoiceItemsDto::getCustomTaxes)
                                .orElse(Collections.emptyList());

                for (InvoiceItemsDto.CustomTaxDto ctDto : customTaxesDto) {
                    ItemTax tax = new ItemTax();
                    tax.setName(ctDto.getName());
                    tax.setAmount(ctDto.getAmount());
                    item.addCustomTax(tax);
                }
            }
        }

        Invoice savedInvoice = invoiceRepository.save(invoice);

        Map<String, Object> response = certificateDgeService.certificate(savedInvoice);
        System.out.println("response DGE : " + response);

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            String bodyJson = (String) response.get("body");
            Map<String, Object> body = mapper.readValue(bodyJson, Map.class);

            String ncc = (String) body.get("ncc");
            String reference = (String) body.get("reference");
            String fneToken = (String) body.get("token");

            Map<String, Object> invoiceResp = (Map<String, Object>) body.get("invoice");
            String fneId = (String) invoiceResp.get("id");
            String clientNcc = (String) invoiceResp.get("clientNcc");

            savedInvoice.setFneId(fneId);
            savedInvoice.setFneToken(fneToken);
            savedInvoice.setFneCc(clientNcc != null ? clientNcc : ncc);
            savedInvoice.setFneReference(reference);

            savedInvoice.setResponseDgi(bodyJson);

            Object status = response.get("status");
            if (status != null) {
                savedInvoice.setStatusFne("CERTIFICATED");
            }

        } catch (Exception e) {
            System.out.println("Parsing FNE error: " + e.getMessage());
            savedInvoice.setResponseDgi(response.toString());
        }

        return invoiceMapper.toDto(savedInvoice);
    }







    @Override
    public InvoiceDto update(InvoiceDto invoiceDto, UUID id) {
        // 1️⃣ Récupérer la facture existante
        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found"));

        // 2️⃣ Mettre à jour les champs simples
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
        existingInvoice.setRne(invoiceDto.isRne());
        existingInvoice.setNtsNumberReceipt(invoiceDto.getNtsNumberReceipt());
        existingInvoice.setStatusFne(invoiceDto.getStatusFne());
        existingInvoice.setFneMessageReturn(invoiceDto.getFneMessageReturn());
        existingInvoice.setFneReference(invoiceDto.getFneReference());
        existingInvoice.setFneToken(invoiceDto.getFneToken());
        existingInvoice.setFneId(invoiceDto.getFneId());
        existingInvoice.setFneCc(invoiceDto.getFneCc());
        existingInvoice.setFneBalanceSticker(invoiceDto.getFneBalanceSticker());

        // 3️⃣ Synchroniser les items
        existingInvoice.getItems().clear(); // supprime les anciens items (orphanRemoval=true)
        if (invoiceDto.getItems() != null) {
            for (InvoiceItemsDto itemDto : invoiceDto.getItems()) {
                InvoiceItems item = new InvoiceItems();
                item.setItemRef(itemDto.getItemRef());
                item.setDescription(itemDto.getDescription());
                item.setQuantity(itemDto.getQuantity());
                item.setUnitPrice(itemDto.getUnitPrice());
                item.setDiscountAmount(itemDto.getDiscountAmount());
                item.setMeasureUnit(itemDto.getMeasureUnit());

                item.setInvoice(existingInvoice); // lien FK

                // Ajouter les taxes personnalisées
                if (itemDto.getCustomTaxes() != null) {
                    for (InvoiceItemsDto.CustomTaxDto ctDto : itemDto.getCustomTaxes()) {
                        ItemTax tax = new ItemTax();
                        tax.setName(ctDto.getName());
                        tax.setAmount(ctDto.getAmount());
                        tax.setInvoiceItem(item); // lien FK
                        if (item.getCustomTaxes() == null) {
                            item.setCustomTaxes(new ArrayList<>());
                        }
                        item.getCustomTaxes().add(tax);
                    }
                }

                // Ajouter l'item à la facture
                existingInvoice.getItems().add(item);
            }
        }

        // 4️⃣ Sauvegarder la facture avec cascade
        Invoice savedInvoice = invoiceRepository.save(existingInvoice);

        // 5️⃣ Retourner le DTO mis à jour
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
