package allcount.poc.credential.converter;

import allcount.poc.credential.object.HashedPassword;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class HashedPasswordAttributeConverter
        implements AttributeConverter<HashedPassword, String> {


    @Override
    public String convertToDatabaseColumn(final HashedPassword attribute) {
        return attribute.toString();
    }

    @Override
    public HashedPassword convertToEntityAttribute(final String dbData) {
        return new HashedPassword(dbData);
    }

}

