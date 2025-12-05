package com.df.fne.core.domaines;

import com.df.fne.core.audits.UserDateAudit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationDto extends UserDateAudit {
    private Long id;
    private String channel;
    private String destinataire;
    private String statut;
    private String downloadLink;
    private LocalDateTime sendDate;
    private UUID invoiceId;
}
