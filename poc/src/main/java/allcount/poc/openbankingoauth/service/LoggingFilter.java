package allcount.poc.openbankingoauth.service;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logging filter for Jakarta client.
 */
public class LoggingFilter implements ClientRequestFilter {
    private static final Logger LOG = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    public void filter(ClientRequestContext requestContext) {
        LOG.log(Level.INFO, requestContext.getMethod());
        LOG.log(Level.INFO, requestContext.getUri().toString());

        if (requestContext.hasEntity()) {
            LOG.log(Level.INFO, requestContext.getEntity().getClass().getName());
            LOG.log(Level.INFO, requestContext.getEntity().toString());
        }
    }
}
