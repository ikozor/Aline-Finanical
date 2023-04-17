package com.aline.gateway.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.gateway")
public class GatewayConfigurationProperties {

    /**
     * Routes for the gateway to map to.
     */
    private List<Route> routes;
    /**
     * The host of the microservices.
     */
    private String serviceHost;
    /**
     * The path prefix to be used with the rewrite path filter.
     */
    private String pathPrefix;
    /**
     * The front-end portal origins to allows cors for.
     */
    private List<String> portalOrigins;

    @Getter
    @Setter
    @ToString
    static class Route {
        /**
         * ID of the route (typically the microservice name)
         */
        private String id;
        /**
         * The port of the microservice
         */
        private int port;
        /**
         * The paths to map to this microservice
         */
        private List<String> paths;
    }

}
