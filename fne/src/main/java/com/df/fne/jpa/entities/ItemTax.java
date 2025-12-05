package com.df.fne.jpa.entities;

import com.df.fne.core.audits.UserDateAudit;
import com.df.fne.core.domaines.enums.TaxeCategory;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "ItemTax")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemTax extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_item_id")
    private InvoiceItems invoiceItem;
}
