/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */
package org.opensearch.rest.action;

import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.node.NodeClient;
import org.opensearch.search.aggregations.AggregationBuilder;
import org.opensearch.search.aggregations.AggregationBuilders;
import org.opensearch.search.aggregations.bucket.terms.Terms;
import org.opensearch.search.aggregations.metrics.InternalAvg;
import org.opensearch.search.aggregations.metrics.InternalMax;
import org.opensearch.search.builder.SearchSourceBuilder;

import java.util.ArrayList;
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
    private final NodeClient openSearchClient;

    final String ADV_BATTERY_RUN_TIME_REMAINING = "ups_adv_battery_run_time_remaining";
    final String ADV_OUTPUT_VOLTAGE = "ups_adv_output_voltage";
    final String HOST = "host.keyword";

    BatteryStats(NodeClient client) {
        this.openSearchClient = client;
    }

    public String HandleValue(String funcName, String indexName) throws Exception {
        StatFunction statFunc = StatFunction.ValidValueOf(funcName.toUpperCase(Locale.getDefault()));

        if (statFunc == null) {
            throw new Exception("Invalid function type: " + funcName);
        }

        SearchRequest request = new SearchRequest(indexName);
        request.source(composeAggregationBuilders(statFunc));

        SearchResponse searchResponse = openSearchClient.search(request).actionGet();

        switch (statFunc) {
            case AVG:
                return Double.toString(avgBatStats(searchResponse));
            case MAX:
                return Double.toString(maxBatStats(searchResponse));
            case VALUES:
                return valuesBatStats(searchResponse).toString();
            default:
                throw new Exception("Unexpected error in handle function type: " + funcName);
        }
    }

    // avg by 'ups_adv_battery_run_time_remaining' field
    private double avgBatStats(SearchResponse searchResponse) {
        InternalAvg aggregation = searchResponse.getAggregations().get("avg" + ADV_BATTERY_RUN_TIME_REMAINING);

        return aggregation.getValue();
    }

    // max by 'ups_adv_output_voltage' field
    private double maxBatStats(SearchResponse searchResponse) {
        InternalMax aggregation = searchResponse.getAggregations().get("max" + ADV_OUTPUT_VOLTAGE);

        return aggregation.getValue();
    }

    // values by 'host' field
    private ArrayList<String> valuesBatStats(SearchResponse searchResponse) {
        ArrayList<String> hosts = new ArrayList<>();
        Terms aggregation = searchResponse.getAggregations().get("values" + HOST);

        for (Terms.Bucket entry : aggregation.getBuckets()) {
            hosts.add((String) entry.getKey());
        }

        return hosts;
    }

    private SearchSourceBuilder composeAggregationBuilders(StatFunction statFunc) throws Exception {
        AggregationBuilder aggBuilder;
        switch (statFunc) {
            case AVG:
                aggBuilder = AggregationBuilders.
                        avg("avg" + ADV_BATTERY_RUN_TIME_REMAINING).
                        field(ADV_BATTERY_RUN_TIME_REMAINING);
                break;
            case MAX:
                aggBuilder = AggregationBuilders.
                        max("max" + ADV_OUTPUT_VOLTAGE).
                        field(ADV_OUTPUT_VOLTAGE);
                break;
            case VALUES:
                aggBuilder = AggregationBuilders.
                        terms("values" + HOST).
                        field(HOST);
                break;
            default:
                throw new Exception("Unexpected error in handle function type: " + statFunc.name());
        }

        return new SearchSourceBuilder().aggregation(aggBuilder).size(0);
    }
}
