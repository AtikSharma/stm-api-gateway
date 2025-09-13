package com.taskmanager.gateway.filter;

import com.taskmanager.gateway.constants.GatewayConstants;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class HeaderFilter extends AbstractGatewayFilterFactory<HeaderFilter.Config> {

    HeaderFilter() {
        super(Config.class);
    }

    /**
     * This is an config class for the parameter of the apply method of @AuthFilter.
     */
    public static class Config {
        /**
         * This is an empty method which returns null.
         *
         * @return null;
         */
        String nothing() {
            return null;
        }
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String correlationId = exchange.getRequest().getHeaders()
                    .getFirst(GatewayConstants.HEADER_FIELD_CORRELATION_ID);

            // If it's not present, generate a new Correlation-ID and add it to the request
            if (correlationId == null) {
                String randomUUID = UUID.randomUUID().toString();
                exchange = exchange.mutate().request(exchange.getRequest().mutate()
                        .header(GatewayConstants.HEADER_FIELD_CORRELATION_ID, randomUUID).build()).build();
            }

            // Proceed with the chain
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                // You can add any post-processing logic here if needed
            }));
        };
    }
}
