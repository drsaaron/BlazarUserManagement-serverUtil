/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.blazartech.blazarusermanagement.products.serverutil;

import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.Status;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author scott
 */
@ExtendWith(MockitoExtension.class)
public class ActuatorServiceBasedHealthIndicatorTest {

    private static final Logger logger = LoggerFactory.getLogger(ActuatorServiceBasedHealthIndicatorTest.class);

    private static final String URL = "url1";

    @Mock
    private RestTemplate restTemplate;
    
    private ActuatorServiceBasedHealthIndicator instance;
    
    public ActuatorServiceBasedHealthIndicatorTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        
        instance = new ActuatorServiceBasedHealthIndicator(URL, restTemplate);
                
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of health method, of class ActuatorServiceBasedHealthIndicator.
     */
    @Test
    public void testHealth_up() {
        logger.info("health_up");
        
        HealthResponse hr1 = new HealthResponse();
        hr1.setStatus("UP");
        hr1.setHealthCheckURL("hc1");
        
        Mockito.when(restTemplate.getForObject(URL, HealthResponse.class)).thenReturn(hr1);
        
        Health h = instance.health();
        logger.info("h = {}", h);
        logger.info("status = {}", h.getStatus().getCode());
        
        Map<String, Object> details = h.getDetails();
        
        assertEquals(Status.UP, h.getStatus());
        assertEquals(hr1.getStatus(), details.get("status"));
        assertEquals(URL, details.get("healthCheckURL"));
    }

    @Test
    public void testHealth_down() {
        logger.info("health_down");
        
        HealthResponse hr2 = new HealthResponse();
        hr2.setStatus("DOWN");
        hr2.setHealthCheckURL("hc2");
        
        Mockito.when(restTemplate.getForObject(URL, HealthResponse.class)).thenReturn(hr2);
        
        Health h = instance.health();
        logger.info("h = {}", h);
        logger.info("status = {}", h.getStatus().getCode());
        
        Map<String, Object> details = h.getDetails();
        
        assertEquals(Status.DOWN, h.getStatus());
        assertEquals(hr2.getStatus(), details.get("status"));
        assertEquals(URL, details.get("healthCheckURL"));
    }
    
    @Test
    public void testHealth_down_exception() {
        logger.info("health_down_exception");
        
        HealthResponse hr3 = null;
        
        Mockito.when(restTemplate.getForObject(URL, HealthResponse.class)).thenReturn(hr3);
        
        Health h = instance.health();
        logger.info("h = {}", h);
        logger.info("status = {}", h.getStatus().getCode());
        logger.info("error details = {}", h.getDetails().get("error"));
        
        String errorDetail = (String) h.getDetails().get("error");
        String expectedError = RestClientException.class.getName() + ": Unable to read health from " + URL;
        
        assertEquals(Status.DOWN, h.getStatus());
        assertEquals(expectedError, errorDetail);
    }
}
