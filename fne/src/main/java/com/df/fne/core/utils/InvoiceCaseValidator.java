package com.df.fne.core.utils;

import com.df.fne.core.domaines.InvoiceDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class InvoiceCaseValidator implements ConstraintValidator<ValidInvoiceCase, InvoiceDto> {

    @Override
    public boolean isValid(InvoiceDto dto, ConstraintValidatorContext context) {

        if (dto == null) return true;

        boolean valid = true;

        context.disableDefaultConstraintViolation();

        if(dto.isRne()){
            if(dto.getRneReceipt() == null || dto.getRneReceipt().isBlank()){
                context.buildConstraintViolationWithTemplate("rneReceipt is required when isRne is true")
                        .addPropertyNode("rneReceipt")
                        .addConstraintViolation();
                valid = false;
            }
        }

        if (dto.getTemplate() != null) {

            switch (dto.getTemplate()) {
                case B2B:
                    if (dto.getClientNcc() == null || dto.getClientNcc().isBlank()) {
                        context.buildConstraintViolationWithTemplate("ClientNcc is required for B2B")
                                .addPropertyNode("clientNcc")
                                .addConstraintViolation();
                        valid = false;
                    }
                    break;

                case B2F:
                    if (dto.getCurrency() == null) {
                        context.buildConstraintViolationWithTemplate("Currency is required for B2F")
                                .addPropertyNode("currency")
                                .addConstraintViolation();
                        valid = false;
                    }
                    if (dto.getCurrencyRate() <= 0) {
                        context.buildConstraintViolationWithTemplate("CurrencyRate must be > 0 for B2F")
                                .addPropertyNode("currencyRate")
                                .addConstraintViolation();
                        valid = false;
                    }
                    break;
            }
        }

        return valid;
    }
}
