package com.df.fne.presenter.request;

import java.util.List;

public record CertificateInvoiceRequest(

        // Champs obligatoires
        String invoiceType,
        String paymentMethod,
        String template,
        boolean isRne,

        // Champs conditionnels
        String rne,                     // Obligatoire si isRne = true
        String clientNcc,               // Obligatoire si template = B2B
        String foreignCurrency,         // Peut être null
        Double foreignCurrencyRate,     // Obligatoire si foreignCurrency != null

        // Infos client
        String clientCompanyName,
        String clientPhone,
        String clientEmail,
        String clientSellerName,

        // Informations commerce
        String pointOfSale,
        String establishment,

        // Messages optionnels
        String commercialMessage,
        String footer,

        // TVA obligatoire
        String taxes,

        // Articles obligatoires
        List<ItemRequest> items,

        // Taxes personnelles optionnelles
        List<CustomTaxRequest> customTaxes,

        // Remise total HT (optionnel)
        Double discount

) {

    // ==== VALIDATIONS PERSONNALISÉES ====
    public CertificateInvoiceRequest {
        // Condition : RNE obligatoire si isRne = true
        if (isRne && (rne == null || rne.isBlank())) {
            throw new IllegalArgumentException("Le champ 'rne' est obligatoire lorsque 'isRne' est true.");
        }

        // Condition : clientNcc obligatoire si template = B2B
        if ("B2B".equalsIgnoreCase(template) && (clientNcc == null || clientNcc.isBlank())) {
            throw new IllegalArgumentException("Le champ 'clientNcc' est obligatoire lorsque 'template' est 'B2B'.");
        }

        // Condition : foreignCurrencyRate obligatoire si foreignCurrency non null
        if (foreignCurrency != null && (foreignCurrencyRate == null || foreignCurrencyRate == 0)) {
            throw new IllegalArgumentException("Le champ 'foreignCurrencyRate' est obligatoire lorsque 'foreignCurrency' est renseigné.");
        }

        // Condition : items obligatoire
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("La liste 'items' est obligatoire et ne peut pas être vide.");
        }
    }


    // ======= SOUS-RECORDS =======

    public record ItemRequest(
            String reference,          // optionnel
            String description,        // obligatoire
            double quantity,           // obligatoire
            double amount,             // obligatoire
            Double discount,           // optionnel
            String measurementUnit     // optionnel
    ) {
        public ItemRequest {
            if (description == null || description.isBlank()) {
                throw new IllegalArgumentException("L'attribut 'description' est obligatoire pour un item.");
            }
            if (quantity <= 0) {
                throw new IllegalArgumentException("'quantity' doit être supérieur à zéro.");
            }
            if (amount <= 0) {
                throw new IllegalArgumentException("'amount' doit être supérieur à zéro.");
            }
        }
    }

    public record CustomTaxRequest(
            String name,       // obligatoire si customTaxes non vide
            double amount      // obligatoire si customTaxes non vide
    ) {
        public CustomTaxRequest {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Chaque 'customTax.name' est obligatoire.");
            }
            if (amount <= 0) {
                throw new IllegalArgumentException("Chaque 'customTax.amount' doit être positif.");
            }
        }
    }
}
