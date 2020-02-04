package com.github.wladox.moneytransfer.util;

import com.google.gson.Gson;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.PathTemplateMatch;

import java.nio.charset.Charset;

public class Helper {

    private static final Gson gson = new Gson();

    public static <T> T getFrom(HttpServerExchange exchange, Class<T> clazz) {
        final StringBuffer sb = new StringBuffer();
        exchange.getRequestReceiver().receiveFullString((exchange1, msg) -> sb.append(msg));
        return gson.fromJson(sb.toString(), clazz);
    }

    public static String toJson(Object object) {
        return Helper.gson.toJson(object);
    }


    /**
     * Extracts path variable value from request URI
     *
     * @param exchange Client connection object
     * @return parsed value
     */
    public static String getAccountId(HttpServerExchange exchange) {
        PathTemplateMatch pathMatch = exchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY);
        return pathMatch.getParameters().get("accountId");
    }

    /**
     *
     * Sends response body as JSON
     *
     * @param exchange Client connection object
     * @param object Response body payload
     */
    public static void sendResponseBody(HttpServerExchange exchange, Object object) {
        exchange.getResponseSender().send(Helper.toJson(object), Charset.forName("UTF-8"));
    }
}
