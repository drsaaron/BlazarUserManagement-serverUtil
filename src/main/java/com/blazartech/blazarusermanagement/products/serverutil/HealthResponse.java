/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.blazartech.blazarusermanagement.products.serverutil;

import java.io.Serializable;

/**
 *
 * @author AAR1069
 */
public class HealthResponse implements Serializable {
    
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    private String healthCheckURL;

    public String getHealthCheckURL() {
        return healthCheckURL;
    }

    public void setHealthCheckURL(String healthCheckURL) {
        this.healthCheckURL = healthCheckURL;
    }
    
}
