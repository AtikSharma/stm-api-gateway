package com.taskmanager.gateway.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GatewayConstants {
    public static final String HEADER_FIELD_AUTHORIZATION = "Authorization";
    public static final String HEADER_FIELD_CORRELATION_ID = "Correlation-Id";
}
