package com.df.fne.core.domaines;

import com.df.fne.core.audits.UserDateAudit;
import com.df.fne.core.domaines.enums.Currency;
import com.df.fne.core.domaines.enums.InvoiceTemplate;
import com.df.fne.core.domaines.enums.InvoiceType;
import com.df.fne.core.domaines.enums.PaymentMethod;
import com.df.fne.core.utils.ValidInvoiceCase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@ValidInvoiceCase
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@ToString
public class InvoiceDto extends UserDateAudit {

    private UUID id;
    private UserDto user;
    private BusinessUnitsDto businessUnits;
    private String clientNcc;
    private String invoiceNumber;
    private LocalDateTime invoiceDate;
    private InvoiceType invoiceType;
    private String source;
    private PaymentMethod paymentMethod;
    private InvoiceTemplate template;
    private String pointOfSale;
    private Currency currency;
    private double currencyRate;
    private String establishment;
    private boolean isRne = false;
    private String rneReceipt;
    private String clientPhone;
    private String clientCompanyName;
    private String clientEmail;
    private String clientSellerName;
    private String commercialMessage;
    private String footer;
    private int discount;
    private String statusFne = "PENDING";
    private String fneMessageReturn;
    private String responseDgi;
    private String fneReference;
    private String fneToken;
    private String fneId;
    private String fneCc;
    private int fneBalanceSticker;

    private List<InvoiceItemsDto> items = new ArrayList<>();
    private List<NotificationDto> notifications = new ArrayList<>();
}
