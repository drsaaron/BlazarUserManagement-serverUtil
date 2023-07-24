/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.blazarusermanagement.products.serverutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * a health indicator that will call an actuator health service for another
 * application.
 *
 * @author AAR1069
 */
public class ActuatorServiceBasedHealthIndicator implements HealthIndicator {

    private static final Logger logger = LoggerFactory.getLogger(ActuatorServiceBasedHealthIndicator.class);

    private final String healthURL;
    private final RestTemplate restTemplate;

    public ActuatorServiceBasedHealthIndicator(String healthURL, RestTemplate restTemplate) {
        this.healthURL = healthURL;
        this.restTemplate = restTemplate;
    }

    private Health checkHeathFromURL() {
        // can we successfully query the health URL?
        try {
            HealthResponse healthResponse = restTemplate.getForObject(healthURL, HealthResponse.class);

            // if we're here, all good.
            if (healthResponse != null) {
                switch (healthResponse.getStatus()) {
                    case "UP":
                        return Health.up().withDetail("status", healthResponse.getStatus()).withDetail("healthCheckURL", healthURL).build();
                    case "DOWN":
                        return Health.down().withDetail("status", healthResponse.getStatus()).withDetail("healthCheckURL", healthURL).build();
                }
            } else {
                throw new RestClientException("Unable to read health from " + healthURL);
            }
        } catch (RestClientException e) {
            return Health.down().withException(e).build();
        }

        // make compiler happy
        return null;
    }

    @Override
    public Health health() {
        return checkHeathFromURL();
    }
}
