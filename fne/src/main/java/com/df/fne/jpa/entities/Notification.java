package com.df.fne.jpa.entities;

import com.df.fne.core.audits.UserDateAudit;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "Notifications")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String channel;
    private String destinataire;
    private String statut;
    private String downloadLink;
    private LocalDateTime sendDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

}
