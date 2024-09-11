/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */
package org.opensearch.rest.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

enum StatFunction {
    AVG,
    MAX,
    VALUES;

    public static StatFunction ValidValueOf(String name) {
        for (StatFunction e : values()) {
            if (e.name().equals(name)) {
                return e;
            }
        }
        return null;
    }
}

public class BatteryStats {
    public static String HandleValue(String funcName, ArrayList<Battery> batStats) throws Exception {
        StatFunction statFunc = StatFunction.ValidValueOf(funcName.toUpperCase(Locale.getDefault()));

        if (statFunc == null) {
            throw new Exception("Invalid function type: " + funcName);
        }

        switch (statFunc) {
            case AVG:
                return Float.toString(avgBatStats(batStats));
            case MAX:
                return Integer.toString(maxBatStats(batStats));
            case VALUES:
                return valuesBatStats(batStats);
            default:
                throw new Exception("Unexpected error in handle function type: " + funcName);
        }
    }

    // values by 'host' field
    private static String valuesBatStats(ArrayList<Battery> batStats) {
        HashSet<String> hosts = new HashSet<>();

        for (Battery batStat : batStats) {
            String val = batStat.getHost();
            if (val != null) {
                hosts.add(val);
            }
        }

        return hosts.toString();
    }

    // max by 'ups_adv_output_voltage' field
    private static int maxBatStats(ArrayList<Battery> batStats) {
        int max = Integer.MIN_VALUE;

        for (Battery batStat : batStats) {
            Integer val = batStat.getOutputVoltage();

            if (val != null && val > max) {
                max = val;
            }
        }

        return max;
    }

    // avg by 'ups_adv_battery_run_time_remaining' field
    private static float avgBatStats(ArrayList<Battery> batStats) {
        long avg = 0;

        for (Battery batStat : batStats) {
            Integer val = batStat.getBatRuntime();

            if (val != null) {
                avg += val;
            }
        }

        return (float) avg / batStats.size();
    }
}
