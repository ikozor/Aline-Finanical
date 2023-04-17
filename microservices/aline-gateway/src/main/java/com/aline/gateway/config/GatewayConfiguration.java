package com.aline.gateway.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.route.builder.UriSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class GatewayConfiguration {

    private final GatewayConfigurationProperties gatewayProperties;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();

        final String pathPrefix = gatewayProperties.getPathPrefix();
        final String serviceHost = gatewayProperties.getServiceHost();

        log.info("Routing requests to {}", serviceHost);

        final Function<GatewayFilterSpec, UriSpec> rewritePath = r -> r.rewritePath(pathPrefix + "/(?<segment>.*)", "/${segment}");

        gatewayProperties.getRoutes().forEach(route -> routes.route(route.getId(),
                r -> r.path(route.getPaths().stream().map(path -> path.replace("@", pathPrefix))
                                    .collect(Collectors.toList())
                                    .toArray(new String[0]))
                        .filters(rewritePath)
                        .uri(serviceHost + ":" + route.getPort())
                )
        );

        return routes.build();
    }

    @Bean
    public CorsWebFilter corsConfiguration() {
        final CorsConfiguration corsConfiguration = new CorsConfiguration();

        log.info("Allowing origins: {}", String.join(", ", gatewayProperties.getPortalOrigins()));

        corsConfiguration.setAllowedOrigins(gatewayProperties.getPortalOrigins());
        corsConfiguration.setAllowedMethods(Collections.singletonList(CorsConfiguration.ALL));
        corsConfiguration.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS,
                HttpHeaders.CONTENT_TYPE));
        corsConfiguration.setExposedHeaders(Collections.singletonList(HttpHeaders.AUTHORIZATION));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(source);

    }

}
