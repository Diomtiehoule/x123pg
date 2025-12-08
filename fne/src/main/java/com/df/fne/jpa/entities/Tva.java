package com.df.fne.jpa.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "Tva")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String libelle;
    private int percentage;
}
