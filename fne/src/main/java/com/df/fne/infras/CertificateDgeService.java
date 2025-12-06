package com.df.fne.infras;

import com.df.fne.jpa.entities.Invoice;
import com.df.fne.jpa.entities.InvoiceItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CertificateDgeService {

    private static final String API_KEY = "1y8qdk8AoWbINhfY3TbuwaetpmiVwm6O";
    private static final String BASE_URL_API = "http://54.247.95.108/ws";

    @Autowired
    private RestTemplate restTemplate;

    public CertificateDgeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> certificate(Invoice invoice) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        Map<String, Object> payload = new HashMap<>();


        payload.put("invoiceType", invoice.getInvoiceType());
        payload.put("paymentMethod", invoice.getPaymentMethod());
        payload.put("template", invoice.getTemplate());
        payload.put("isRne", invoice.isRne());

        if (invoice.isRne()) {
            payload.put("rne", invoice.getRneReceipt());
        }

        payload.put("clientCompanyName", invoice.getClientCompanyName());
        payload.put("clientPhone", invoice.getClientPhone());
        payload.put("clientEmail", invoice.getClientEmail());
        payload.put("clientSellerName", invoice.getClientSellerName());
        payload.put("commercialMessage", invoice.getCommercialMessage());
        payload.put("footer", invoice.getFooter());
        payload.put("discount", invoice.getDiscount());

        if ("B2B".equalsIgnoreCase(invoice.getTemplate().toString())) {
            payload.put("clientNcc", invoice.getClientNcc());
        }

        payload.put("pointOfSale", invoice.getPointOfSale());
        payload.put("establishment", invoice.getEstablishment());

        if (invoice.getCurrency() != null) {
            payload.put("foreignCurrency", invoice.getCurrency());
            payload.put("foreignCurrencyRate", invoice.getCurrencyRate());
        } else {
            payload.put("foreignCurrencyRate", 0);
        }


        payload.put("taxes", "TVA");


        List<Map<String, Object>> jsonItems = new ArrayList<>();

        for (InvoiceItems item : invoice.getItems()) {
            Map<String, Object> jsonItem = new HashMap<>();

            jsonItem.put("description", item.getDescription());
            jsonItem.put("quantity", item.getQuantity());
            jsonItem.put("amount", item.getAmount());

            if (item.getItemRef() != null)
                jsonItem.put("reference", item.getItemRef());

            if (item.getMeasureUnit() != null)
                jsonItem.put("measurementUnit", item.getMeasureUnit());

            jsonItem.put("taxes", item.getTaxes());

            jsonItems.add(jsonItem);
        }

        payload.put("items", jsonItems);


        System.out.println("=== Payload envoyé à DGE ===");
        System.out.println(payload);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        Map<String, Object> result = new HashMap<>();
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    BASE_URL_API + "/external/invoices/sign",
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            result.put("status", response.getStatusCodeValue());
            result.put("body", response.getBody());

            System.out.println("=== Réponse DGE OK ===");
            System.out.println(response.getBody());

        } catch (HttpClientErrorException e) {
            System.err.println("=== Erreur DGE ===");
            System.err.println(e.getResponseBodyAsString());

            result.put("status", e.getStatusCode().value());
            result.put("error", e.getResponseBodyAsString());

        } catch (Exception e) {
            System.err.println("=== Erreur interne ===");
            System.err.println(e.getMessage());
            result.put("status", 500);
            result.put("error", "Erreur interne: " + e.getMessage());
        }

        return result;
    }
}
