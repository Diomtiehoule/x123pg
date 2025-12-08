package com.df.fne.core.domaines;

import lombok.Data;

@Data
public class TvaDto {
    private Long id;
    private String libelle;
    private int percentage;
}
