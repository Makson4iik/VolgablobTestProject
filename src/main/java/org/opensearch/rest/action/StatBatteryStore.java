/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */
package org.opensearch.rest.action;

import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.node.NodeClient;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.SearchHit;

import java.sql.Timestamp;
import java.util.ArrayList;

public class StatBatteryStore {
    private final NodeClient openSearchClient;

    StatBatteryStore(NodeClient client) {
        this.openSearchClient = client;
    }

    public ArrayList<Battery> getAllDocsByIndex(String index) {
        //QueryBuilder matchAllQuery = QueryBuilders.matchAllQuery();
        SearchResponse response = openSearchClient.prepareSearch(index)
                .setQuery(QueryBuilders.matchAllQuery())
                .execute()
                .actionGet();
        ArrayList<Battery> result = new ArrayList<>();

        for (SearchHit hit : response.getHits().getHits()) {
            result.add(new Battery(
                    (Integer)hit.getSourceAsMap().get("ups_adv_output_load"),
                    (Integer)hit.getSourceAsMap().get("ups_adv_battery_temperature"),
                    //java.sql.Timestamp.valueOf(formatter.format((String)hit.getSourceAsMap().get("@timestamp")).getTime()),
                    new Timestamp(System.currentTimeMillis()),
                    (String)hit.getSourceAsMap().get("host"),
                    (Integer)hit.getSourceAsMap().get("ups_adv_battery_run_time_remaining"),
                    (Integer)hit.getSourceAsMap().get("ups_adv_output_voltage")
            ));
        }

        return result;
    }
}