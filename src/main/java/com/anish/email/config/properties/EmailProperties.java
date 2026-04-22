package com.anish.email.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.mail")
public record EmailProperties(
        String host,
        int port,
        String username,
        String password,
        String from,
        String alias,
        Security security,
        Performance performance,
        boolean debug
) {

    public record Security(
            Ssl ssl,
            StartTls starttls
    ) {}

    public record Ssl(
            String trust,
            String protocols
    ) {}

    public record StartTls(
            boolean enable,
            boolean required
    ) {}

    public record Performance(
            boolean pool,
            int connectionTimeout,
            int timeout,
            int writeTimeout
    ) {}
}