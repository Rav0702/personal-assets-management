package allcount.poc.transaction.client;

import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.service.LoggingFilter;
import allcount.poc.transaction.mapper.OpenBankingBankToTransactionUriMapper;
import allcount.poc.transaction.object.dto.TransactionDto;
import allcount.poc.transaction.provider.OpenBankingListTransactionResponseToDtoMapperProvider;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

/**
 * Client for retrieving from the Bank.
 */
@Service
public class TransactionRetrievalClient {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_AUTH_SCHEME = "Bearer";
    private static final String QUERY_PARAM_IBAN = "iban";
    private static final String QUERY_PARAM_BOOKING_DATE_FROM = "bookingDateFrom";
    private static final String QUERY_PARAM_BOOKING_DATE_TO = "bookingDateTo";
    private static final String QUERY_PARAM_LIMIT = "limit";
    private static final String QUERY_PARAM_OFFSET = "offset";

    private static final Logger LOG = Logger.getLogger(TransactionRetrievalClient.class.getName());
    private final transient Client client;
    private final transient OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper;
    private final transient OpenBankingBankToTransactionUriMapper openBankingBankToTransactionUriMapper;
    private final transient OpenBankingListTransactionResponseToDtoMapperProvider listTransactionResponseToDtoMapperProvider;

    /**
     * Constructor.
     */
    @Autowired
    public TransactionRetrievalClient(
            OpenBankingBankToBaseUriMapper openBankingBankToBaseUriMapper,
            OpenBankingBankToTransactionUriMapper openBankingBankToTransactionUriMapper,
            OpenBankingListTransactionResponseToDtoMapperProvider listTransactionResponseToDtoMapperProvider
    ) {
        this.client = ClientBuilder.newBuilder().build().register(new LoggingFilter());
        this.openBankingBankToBaseUriMapper = openBankingBankToBaseUriMapper;
        this.openBankingBankToTransactionUriMapper = openBankingBankToTransactionUriMapper;
        this.listTransactionResponseToDtoMapperProvider = listTransactionResponseToDtoMapperProvider;
    }

    /**
     * List transactions.
     *
     * @param bank            (required) Enum representing the bank.
     * @param iban            (required) IBAN of the account.
     * @param accessToken     (required) Access token.
     * @param bookingDateFrom (optional) Start date of the booking date.
     * @param bookingDateTo   (optional) End date of the booking date.
     * @param limit           (optional) Limit of the transactions.
     * @param offset          (optional) Offset of the transactions.
     * @return A list of transactions.
     */
    public List<TransactionDto> listTransactions(
            @NonNull OpenBankingBankEnum bank, @NonNull String iban, @NonNull String accessToken,
            @Nullable LocalDate bookingDateFrom, @Nullable LocalDate bookingDateTo, @Nullable Integer limit, @Nullable Integer offset
    ) {
        LOG.info("Listing transactions from iban: " + iban + " for bank: " + bank
                + " parameters: bookingDateFrom=" + bookingDateFrom + ", bookingDateTo=" + bookingDateTo + ", limit=" + limit + ", offset=" + offset);

        WebTarget webTarget = this.client
                .target(openBankingBankToBaseUriMapper.getBaseUri(bank) + openBankingBankToTransactionUriMapper.getListTransactionsUri(bank))
                .queryParam(QUERY_PARAM_IBAN, iban);

        if (bookingDateFrom != null) {
            webTarget = webTarget.queryParam(QUERY_PARAM_BOOKING_DATE_FROM, bookingDateFrom.toString());
        }

        if (bookingDateTo != null) {
            webTarget = webTarget.queryParam(QUERY_PARAM_BOOKING_DATE_TO, bookingDateTo.toString());
        }

        if (limit != null) {
            webTarget = webTarget.queryParam(QUERY_PARAM_LIMIT, limit);
        }

        if (offset != null) {
            webTarget = webTarget.queryParam(QUERY_PARAM_OFFSET, offset);
        }

        Response response = webTarget
                .request()
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_AUTH_SCHEME + " " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .get();

        LOG.info("Response status: " + response.getStatus());

        JsonNode body = response.readEntity(JsonNode.class);

        return listTransactionResponseToDtoMapperProvider.getMapper(bank).mapToDto(body);
    }
}