package allcount.poc.shared.mapper;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Mapper for converting JSON response to a DTO.
 *
 * @param <T> the type of the DTO.
 */
public interface JsonResponseToDtoMapper<T> {
    /**
     * Maps the response to the DTO.
     *
     * @param response json body of the response.
     * @return the DTO.
     */
    T mapToDto(JsonNode response);
}

