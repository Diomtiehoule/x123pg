package com.df.fne.jpa.entities;

import com.df.fne.core.audits.UserDateAudit;
import com.df.fne.core.domaines.enums.Currency;
import com.df.fne.core.domaines.enums.InvoiceTemplate;
import com.df.fne.core.domaines.enums.InvoiceType;
import com.df.fne.core.domaines.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "businessUnits_id")
    private BusinessUnits businessUnits;

    private String invoiceNumber;
    private LocalDateTime invoiceDate;

    @Enumerated(EnumType.STRING)
    private InvoiceType invoiceType;

    private String source;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private InvoiceTemplate template;

    @Column(columnDefinition = "TEXT")
    private String pointOfSale;

    @Enumerated(EnumType.STRING)
    private Currency currency;
    private double currencyRate;
    private String establishment;
    private boolean isRne = false;

    @Column(columnDefinition = "TEXT")
    private String ntsNumberReceipt;

    private String fneReference;
    @Column(columnDefinition = "TEXT")
    private String fneToken;
    private String fneId;
    private String fneCc;
    private String statusFne;
    @Column(columnDefinition = "TEXT")
    private String responseDgi;
    @Column(nullable = true)
    private int fneBalanceSticker;

    @Column(columnDefinition = "TEXT")
    private String fneMessageReturn;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItems> items = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    public void addItem(InvoiceItems item) {
        items.add(item);
        item.setInvoice(this);
    }

    public void removeItem(InvoiceItems item) {
        items.remove(item);
        item.setInvoice(null);
    }

}
