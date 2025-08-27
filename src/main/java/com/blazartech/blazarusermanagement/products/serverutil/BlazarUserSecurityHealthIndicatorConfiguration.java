/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.blazarusermanagement.products.serverutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author AAR1069
 */
//@Configuration
public class BlazarUserSecurityHealthIndicatorConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(BlazarUserSecurityHealthIndicatorConfiguration.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${blazartech.user.management.service.healthUrl}")
    private String url;
    
    @Bean
    public HealthIndicator blazarUserSecurityHealthIndicator() {
        return new ActuatorServiceBasedHealthIndicator(url, restTemplate);
    }

}
