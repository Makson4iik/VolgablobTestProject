/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */
package org.opensearch.rest.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.opensearch.core.rest.RestStatus;
import org.opensearch.rest.BytesRestResponse;
import org.opensearch.rest.RestResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StatBatteryService {
    public static RestResponse buildResponse(String funcName, String indexName) {
        try {
            String result = BatteryStats.HandleValue(funcName, new ArrayList<>());

            return new BytesRestResponse(RestStatus.OK, result);
        } catch (Exception e) {
            return new BytesRestResponse(RestStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private static ArrayList<Battery> parseBatteryInfoJson(String rawData){
        Gson gson = new Gson();

        Type listType = new TypeToken<ArrayList<Battery>>(){}.getType();

        return gson.fromJson(rawData, listType);
    }
}