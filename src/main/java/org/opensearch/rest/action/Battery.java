/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */
package org.opensearch.rest.action;

import com.google.gson.annotations.SerializedName;

public class Battery {
    @SerializedName("ups_adv_output_load")
    private Integer outputLoad;

    @SerializedName("ups_adv_battery_temperature")
    private Integer batTemperature;

    @SerializedName("host")
    private String host;

    @SerializedName("ups_adv_battery_run_time_remaining")
    private Integer batRuntime;

    @SerializedName("ups_adv_output_voltage")
    private Integer outputVoltage;

    public Battery() {
    }

    public Battery(Integer outputLoad, Integer batTemperature, String host, Integer batRuntime, Integer outputVoltage) {
        this.outputLoad = outputLoad;
        this.batTemperature = batTemperature;
        this.host = host;
        this.batRuntime = batRuntime;
        this.outputVoltage = outputVoltage;
    }

    public Integer getOutputLoad() {
        return outputLoad;
    }

    public void setOutputLoad(Integer outputLoad) {
        this.outputLoad = outputLoad;
    }

    public Integer getBatTemperature() {
        return batTemperature;
    }

    public void setBatTemperature(Integer batTemperature) {
        this.batTemperature = batTemperature;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getBatRuntime() {
        return batRuntime;
    }

    public void setBatRuntime(Integer batRuntime) {
        this.batRuntime = batRuntime;
    }

    public Integer getOutputVoltage() {
        return outputVoltage;
    }

    public void setOutputVoltage(Integer outputVoltage) {
        this.outputVoltage = outputVoltage;
    }
}
