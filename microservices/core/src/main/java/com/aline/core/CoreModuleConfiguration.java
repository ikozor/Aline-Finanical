package com.aline.core;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties
@EntityScan
public class CoreModuleConfiguration {
}
