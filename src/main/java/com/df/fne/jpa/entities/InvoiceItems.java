package com.df.fne.jpa.entities;

import com.df.fne.core.audits.UserDateAudit;
import com.df.fne.core.utils.InvoiceItemsUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "InvoiceItems")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceItems extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    private String itemRef;
    @Column(columnDefinition = "TEXT")
    private String description;
    private int quantity;
    private int unitPrice;
    private int discountAmount;
    private String measureUnit;

    @ElementCollection
    @CollectionTable(name = "InvoiceItemTaxes", joinColumns = @JoinColumn(name = "invoice_item_id"))
    private List<String> taxes = new ArrayList<>();

    @OneToMany(mappedBy = "invoiceItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemTax> customTaxes = new ArrayList<>();

    private int amount; // montant total de la ligne

    public void addCustomTax(ItemTax tax) {
        if (customTaxes == null) {
            customTaxes = new ArrayList<>();
        }
        customTaxes.add(tax);
        tax.setInvoiceItem(this); // lie le FK
    }


    public void removeCustomTax(ItemTax tax) {
        customTaxes.remove(tax);
        tax.setInvoiceItem(null);
    }

}

