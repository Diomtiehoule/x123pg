package com.df.fne.core.mappers;

import com.df.fne.core.domaines.InvoiceItemsDto;
import com.df.fne.jpa.entities.InvoiceItems;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {ItemTaxMapper.class})
public interface InvoiceItemsMapper {
    InvoiceItemsDto toDto(InvoiceItems lineInvoice);
    InvoiceItems toEntity(InvoiceItemsDto lineInvoiceDto);
    List<InvoiceItemsDto> toDtoList(List<InvoiceItems> lineInvoices);
    List<InvoiceItems> toEntityList(List<InvoiceItemsDto> lineInvoicesDto);
}
