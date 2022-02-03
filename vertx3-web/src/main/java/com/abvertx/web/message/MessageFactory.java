package com.abvertx.web.message;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;

public interface MessageFactory {
    void decode(short apiCode, JsonObject body, HttpServerRequest request);

    void encode();
}