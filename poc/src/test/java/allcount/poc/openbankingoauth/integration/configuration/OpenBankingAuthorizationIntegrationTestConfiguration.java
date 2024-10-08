package allcount.poc.openbankingoauth.integration.configuration;

import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * Configuration for the Open Banking Authorization integration tests.
 */
@Profile("open-banking-authorization-integration-test")
@Configuration
public class OpenBankingAuthorizationIntegrationTestConfiguration {

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
}
