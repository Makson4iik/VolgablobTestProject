/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */
package org.opensearch.rest.action;

import org.opensearch.client.node.NodeClient;
import org.opensearch.core.rest.RestStatus;
import org.opensearch.rest.BytesRestResponse;
import org.opensearch.rest.RestResponse;

public class StatBatteryService {
    public static RestResponse buildResponse(String funcName, String indexName, NodeClient client) {
        try {
            BatteryStats stats = new BatteryStats(client);

            String result = stats.HandleValue(funcName, indexName);

            return new BytesRestResponse(RestStatus.OK, result);
        } catch (Exception e) {
            return new BytesRestResponse(RestStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }
}