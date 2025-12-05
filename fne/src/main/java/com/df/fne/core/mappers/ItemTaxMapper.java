package com.df.fne.core.mappers;

import com.df.fne.core.domaines.ItemTaxDto;
import com.df.fne.jpa.entities.ItemTax;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemTaxMapper {
    ItemTaxDto toDto(ItemTax itemTax);
    ItemTax toEntity(ItemTaxDto itemTaxDto);
    List<ItemTaxDto> toDtoList(List<ItemTax> itemTaxList);
    List<ItemTax> toEntityList(List<ItemTaxDto> itemTaxDtoList);
}
