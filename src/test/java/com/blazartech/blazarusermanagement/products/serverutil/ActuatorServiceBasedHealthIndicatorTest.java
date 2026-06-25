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
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.Status;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author scott
 */
@ExtendWith(SpringExtension.class)
public class ActuatorServiceBasedHealthIndicatorTest {

    private static final Logger logger = LoggerFactory.getLogger(ActuatorServiceBasedHealthIndicatorTest.class);

    @TestConfiguration
    public static class ActuatorServiceBasedHealthIndicatorTestConfiguration {
        
        @Bean
        public ActuatorServiceBasedHealthIndicator instance(RestTemplate restTemplate) {
            return new ActuatorServiceBasedHealthIndicator("blah", restTemplate);
        }
    }
    
    private static final String URL1 = "url1";
    private static final String URL2 = "url2";
    private static final String URL3 = "url3";

    @MockitoBean
    private RestTemplate restTemplate;
    
    @Autowired
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
        
        HealthResponse hr1 = new HealthResponse();
        hr1.setHealthCheckURL(URL1);
        hr1.setStatus("UP");
        
        HealthResponse hr2 = new HealthResponse();
        hr2.setHealthCheckURL(URL2);
        hr2.setStatus("DOWN");
        
        HealthResponse hr3 = null;
                
        Mockito.when(restTemplate.getForObject(URL1, HealthResponse.class)).thenReturn(hr1);
        Mockito.when(restTemplate.getForObject(URL2, HealthResponse.class)).thenReturn(hr2);
        Mockito.when(restTemplate.getForObject(URL3, HealthResponse.class)).thenReturn(hr3);
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
        
        Health h = instance.checkHealthFromURL(URL1);
        logger.info("h = {}", h);
        logger.info("status = {}", h.getStatus().getCode());
        
        Map<String, Object> details = h.getDetails();
        
        assertEquals(Status.UP, h.getStatus());
        assertEquals("UP", details.get("status"));
        assertEquals(URL1, details.get("healthCheckURL"));
    }

    @Test
    public void testHealth_down() {
        logger.info("health_down");
        
        Health h = instance.checkHealthFromURL(URL2);
        logger.info("h = {}", h);
        logger.info("status = {}", h.getStatus().getCode());
        
        Map<String, Object> details = h.getDetails();
        
        assertEquals(Status.DOWN, h.getStatus());
        assertEquals("DOWN", details.get("status"));
        assertEquals(URL2, details.get("healthCheckURL"));
    }
    
    @Test
    public void testHealth_down_exception() {
        logger.info("health_down_exception");
        
        Health h = instance.checkHealthFromURL(URL3);
        logger.info("h = {}", h);
        logger.info("status = {}", h.getStatus().getCode());
        logger.info("error details = {}", h.getDetails().get("error"));
        
        String errorDetail = (String) h.getDetails().get("error");
        String expectedError = RestClientException.class.getName() + ": Unable to read health from " + URL3;
        
        assertEquals(Status.DOWN, h.getStatus());
        assertEquals(expectedError, errorDetail);
    }
}
