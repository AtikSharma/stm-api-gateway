package com.taskmanager.gateway.filter;

import java.util.UUID;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import com.taskmanager.common.RequestContext;

import reactor.core.publisher.Mono;

@Component
public class RequestFilter extends AbstractGatewayFilterFactory<RequestFilter.Config> {

	RequestFilter() {
		super(Config.class);
	}

	/**
	 * This is an config class for the parameter of the apply method of @AuthFilter.
	 *
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
					.getFirst(RequestContext.HEADER_FIELD_CORRELATION_ID);

			// If it's not present, generate a new Correlation-ID and add it to the request
			if (correlationId == null) {
				String randomUUID = UUID.randomUUID().toString();
				exchange = exchange.mutate().request(exchange.getRequest().mutate()
						.header(RequestContext.HEADER_FIELD_CORRELATION_ID, randomUUID).build()).build();
			}

			// Proceed with the chain
			return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				// You can add any post-processing logic here if needed
			}));
		};
	}
}
