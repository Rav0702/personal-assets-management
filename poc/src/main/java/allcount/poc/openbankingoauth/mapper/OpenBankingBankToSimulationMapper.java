package allcount.poc.openbankingoauth.mapper;

import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Mapper for the Open Banking banks to their simulation client IDs.
 */
@Component
public class OpenBankingBankToSimulationMapper {

    @Value("#{environment.DEUTSCHE_BANK_SIMULATION_CLIENT_ID}")
    protected String deutscheBankSimulationClientId;

    @Value("#{environment.DEUTSCHE_BANK_SIMULATION_CLIENT_SECRET}")
    protected String deutscheBankSimulationClientSecret;

    @Value("#{environment.NORIS_BANK_SIMULATION_CLIENT_ID}")
    protected String norisBankSimulationClientId;

    @Value("#{environment.NORIS_BANK_SIMULATION_CLIENT_SECRET}")
    protected String norisBankSimulationClientSecret;

    /**
     * Maps the bank to the simulation client ID.
     *
     * @param bank - the bank
     * @return the simulation client ID
     */
    public String mapToSimulationId(OpenBankingBankEnum bank) {
        return switch (bank) {
            case DEUTSCHE_BANK -> deutscheBankSimulationClientId;
            case NORIS_BANK -> norisBankSimulationClientId;
            default -> "Simulation client ID not found";
        };
    }

    /**
     * Maps the bank to the simulation client secret.
     *
     * @param bank - the bank
     * @return the simulation client secret
     */
    public String mapToSimulationSecret(OpenBankingBankEnum bank) {
        return switch (bank) {
            case DEUTSCHE_BANK -> deutscheBankSimulationClientSecret;
            case NORIS_BANK -> norisBankSimulationClientSecret;
            default -> "Simulation client secret not found";
        };
    }
}
