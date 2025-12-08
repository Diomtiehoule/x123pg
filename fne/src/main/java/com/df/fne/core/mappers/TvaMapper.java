package com.df.fne.core.mappers;

import com.df.fne.core.domaines.TvaDto;
import com.df.fne.jpa.entities.Tva;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TvaMapper {
    TvaDto toDto(Tva tva);
    Tva toEntity(TvaDto tvaDto);
    List<TvaDto> toDtoList(List<Tva> tvaList);
    List<Tva> toEntityList(List<TvaDto> tvaDtoList);
}
