package allcount.poc.core.configuration;

import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.mapper.OpenBankingBankToSimulationMapper;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * Configuration for the Open Banking Authorization integration tests.
 */
@Profile("open-banking-integration-test")
@Configuration
public class OpenBankingIntegrationTestConfiguration {

    /**
     * Creates a mock of the OpenBankingBankToBaseUriMapper.
     *
     * @return the mock of the OpenBankingBankToBaseUriMapper
     */
    @Bean
    @Primary
    public OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper() {
        return Mockito.mock(OpenBankingBankToBaseUriMapper.class);
    }

    /**
     * Creates a mock of the OpenBankingBankToBaseUriMapper.
     *
     * @return the mock of the OpenBankingBankToBaseUriMapper
     */
    @Bean
    @Primary
    public OpenBankingBankToSimulationMapper openBankingBankToSimulationMapper() {
        return Mockito.mock(OpenBankingBankToSimulationMapper.class);
    }
}
