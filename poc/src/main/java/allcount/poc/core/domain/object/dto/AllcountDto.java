package allcount.poc.core.domain.object.dto;

import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for the Allcount.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AllcountDto implements Serializable {
    protected UUID id;
}
