package allcount.poc.user.converter;

import allcount.poc.user.object.HashedPassword;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter for the HashedPassword attribute.
 */
@Converter
public class HashedPasswordAttributeConverter implements AttributeConverter<HashedPassword, String> {

    /**
     * Converts the HashedPassword attribute to a string.
     *
     * @param attribute HashedPassword attribute
     * @return string
     */
    @Override
    public String convertToDatabaseColumn(final HashedPassword attribute) {
        return attribute.toString();
    }

    /**
     * Converts a string to a HashedPassword attribute.
     *
     * @param dbData string
     * @return HashedPassword attribute
     */
    @Override
    public HashedPassword convertToEntityAttribute(final String dbData) {
        return new HashedPassword(dbData);
    }

}

