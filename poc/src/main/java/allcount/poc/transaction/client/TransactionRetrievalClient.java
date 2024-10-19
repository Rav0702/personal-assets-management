package allcount.poc.transaction.client;

import allcount.poc.openbankingoauth.mapper.OpenBankingBankToBaseUriMapper;
import allcount.poc.openbankingoauth.object.enums.OpenBankingBankEnum;
import allcount.poc.openbankingoauth.service.LoggingFilter;
import allcount.poc.transaction.mapper.OpenBankingBankToTransactionUriMapper;
import allcount.poc.transaction.object.dto.TransactionListDto;
import allcount.poc.transaction.provider.OpenBankingListTransactionResponseToDtoMapperProvider;
import allcount.poc.transaction.service.OpenBankingTransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;
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

    private static final Logger LOG = Logger.getLogger(OpenBankingTransactionService.class.getName());
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
     * @param bank        (required) Enum representing the bank.
     * @param iban        (required) IBAN of the account.
     * @param accessToken (required) Access token.
     * @return A list of transactions.
     */
    public TransactionListDto listTransactions(@NonNull OpenBankingBankEnum bank, @NonNull String iban, @NonNull String accessToken) {
        return listTransactions(bank, iban, accessToken, null, null, null, null);
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
    public TransactionListDto listTransactions(
            @NonNull OpenBankingBankEnum bank, @NonNull String iban, @NonNull String accessToken,
            @Nullable LocalDate bookingDateFrom, @Nullable LocalDate bookingDateTo, @Nullable Integer limit, @Nullable Integer offset
    ) {
        LOG.info("Listing transactions from iban: " + iban + " for bank: " + bank
                + " parameters: bookingDateFrom=" + bookingDateFrom + ", bookingDateTo=" + bookingDateTo + ", limit=" + limit + ", offset=" + offset);

        WebTarget webTarget = this.client
                .target(openBankingBankToBaseUriMapper.getBaseUri(bank) + openBankingBankToTransactionUriMapper.getListTransactionsUri(bank))
                .queryParam("iban", iban);

        if (bookingDateFrom != null) {
            webTarget = webTarget.queryParam("bookingDateFrom", bookingDateFrom.toString());
        }

        if (bookingDateTo != null) {
            webTarget = webTarget.queryParam("bookingDateTo", bookingDateTo.toString());
        }

        if (limit != null) {
            webTarget = webTarget.queryParam("limit", limit);
        }

        if (offset != null) {
            webTarget = webTarget.queryParam("offset", offset);
        }

        Response response = webTarget
                .request()
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .get();

        JsonNode body = response.readEntity(JsonNode.class);

        LOG.info("Received response of size " + body.size());

        return listTransactionResponseToDtoMapperProvider.getMapper(bank).mapToDto(body);
    }
}