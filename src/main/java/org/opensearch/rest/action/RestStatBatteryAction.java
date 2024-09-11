/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */
package org.opensearch.rest.action;

import org.opensearch.client.node.NodeClient;
import org.opensearch.rest.BaseRestHandler;
import org.opensearch.rest.BytesRestResponse;
import org.opensearch.rest.RestRequest;

import java.io.IOException;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static org.opensearch.rest.RestRequest.Method.POST;

public class RestStatBatteryAction extends BaseRestHandler {
    @Override
    public String getName() {
        return "rest_handler_stat_battery";
    }

    @Override
    public List<Route> routes() {
        return unmodifiableList(List.of(
            new Route(POST, "/_plugins/stat_battery"))
        );
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {
        String func = request.hasContent()? request.contentParser().mapStrings().get("func"): "";
        String index = request.hasContent()? request.contentParser().mapStrings().get("index"): "";

        return channel -> {
            try {
                channel.sendResponse(StatBatteryService.buildResponse(func, index));
            } catch (final Exception e) {
                channel.sendResponse(new BytesRestResponse(channel, e));
            }
        };
    }
}
