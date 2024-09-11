package org.opensearch.rest.action;

import org.opensearch.core.rest.RestStatus;
import org.opensearch.rest.BytesRestResponse;
import org.opensearch.rest.RestResponse;

public class StatBatteryService {
    public static RestResponse buildResponse(String funcName, String indexName) {
        try {
            String result = BatteryStats.HandleValue(funcName, indexName);

            return new BytesRestResponse(RestStatus.OK, result);
        } catch (Exception e) {
            return new BytesRestResponse(RestStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}