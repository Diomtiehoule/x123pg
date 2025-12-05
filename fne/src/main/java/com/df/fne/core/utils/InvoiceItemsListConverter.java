package com.df.fne.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Converter
public class InvoiceItemsListConverter implements AttributeConverter<List<InvoiceItemsUtils>, PGobject> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public PGobject convertToDatabaseColumn(List<InvoiceItemsUtils> invoiceItems) {
        if (invoiceItems == null || invoiceItems.isEmpty()) {
            return null;
        }

        try {
            PGobject pgObject = new PGobject();
            pgObject.setType("jsonb");
            pgObject.setValue(objectMapper.writeValueAsString(invoiceItems));
            return pgObject;
        } catch (JsonProcessingException | SQLException e) {
            throw new IllegalArgumentException("Error converting invoiceItems list to JSON", e);
        }
    }

    @Override
    public List<InvoiceItemsUtils> convertToEntityAttribute(PGobject dbData) {
        if (dbData == null || dbData.getValue() == null) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(
                    dbData.getValue(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, InvoiceItemsUtils.class)
            );
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON to invoiceItems list", e);
        }
    }
}