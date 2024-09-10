package org.opensearch;

import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

enum StatFunction{
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
    public static void HandlePrint(String funcName, String filePath) throws Exception {
		StatFunction statFunc = StatFunction.ValidValueOf(funcName.toUpperCase());

		if (statFunc == null) {
			throw new Exception("Invalid function type: " + funcName);
		}

		ArrayList<Battery> batStats = parseBatteryInfoJson(readFileAsString(filePath));

		switch (statFunc) {
			case AVG:
				avgBatStats(batStats);
				break;
			case MAX:
				maxBatStats(batStats);
				break;
			case VALUES:
				valuesBatStats(batStats);
				break;
			}
    }
	
    // values by 'host' field
    private static void valuesBatStats(ArrayList<Battery> batStats) {
		HashSet<String> hosts = new HashSet<>();
    	
    	for(Battery batStat : batStats){
    	    String val = batStat.getHost();
    	    if (val != null) {
    			hosts.add(val);
    	    }
    	}
    	
    	System.out.println(hosts);
    }

    // max by 'ups_adv_output_voltage' field
    private static void maxBatStats(ArrayList<Battery> batStats) {
		int max = Integer.MIN_VALUE;

		for(Battery batStat : batStats){
			Integer val = batStat.getOutputVoltage();

			if (val != null && val > max) {
				max = val;
			}
		}

		System.out.println(max);
    }

    // avg by 'ups_adv_battery_run_time_remaining' field
    private static void avgBatStats(ArrayList<Battery> batStats) {
		long avg = 0;

		for(Battery batStat : batStats){
			Integer val = batStat.getBatRuntime();

			if (val != null) {
				avg += val;
			}
		}

		System.out.println((float)avg/batStats.size());
    }

    private static ArrayList<Battery> parseBatteryInfoJson(String rawData){
		Gson gson = new Gson();

		Type listType = new TypeToken<ArrayList<Battery>>(){}.getType();

		return gson.fromJson(rawData, listType);
    }
	
    public static String readFileAsString(String fileName) throws Exception{
		return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}
