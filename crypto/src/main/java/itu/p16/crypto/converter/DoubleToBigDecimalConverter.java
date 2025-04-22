package itu.p16.crypto.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Converter(autoApply = true)
public class DoubleToBigDecimalConverter implements AttributeConverter<Double, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Double attribute) {
        if (attribute != null) {
            return BigDecimal.valueOf(attribute).setScale(2, RoundingMode.HALF_UP);
        }
        return null;
    }

    @Override
    public Double convertToEntityAttribute(BigDecimal dbData) {
        if (dbData != null) {
            return dbData.doubleValue();
        }
        return null;
    }
}

