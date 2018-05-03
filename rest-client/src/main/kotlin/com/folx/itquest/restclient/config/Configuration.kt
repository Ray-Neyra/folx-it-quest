package com.folx.itquest.restclient.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "itquest.restclient")
class BasePathConfigurationProperty {
    var basePath = ""
}

@Configuration
@ComponentScan("com.folx.itquest.restclient")
class RestClientConfiguration
