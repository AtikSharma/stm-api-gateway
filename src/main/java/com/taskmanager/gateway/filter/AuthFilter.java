package com.taskmanager.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.taskmanager.common.constants.CommonConstants;
import com.taskmanager.common.util.JwtUtils;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

	private JwtUtils jwtUtils;

	@Autowired
	AuthFilter(JwtUtils jwtUtils) {
		super(Config.class);
		this.jwtUtils = jwtUtils;
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

			if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
			}

			String authorizationHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

			jwtUtils.validateAuthorizationHeader(authorizationHeader);

			return chain.filter(exchange.mutate().build()).then(Mono.fromRunnable(() -> {
			}));
		};
	}
}
