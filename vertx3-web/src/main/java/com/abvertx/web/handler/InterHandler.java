package com.abvertx.web.handler;

import com.abvertx.web.message.AbstractUpMessage;

import io.vertx.core.http.HttpServerResponse;

public interface InterHandler {
    void handler(AbstractUpMessage up, HttpServerResponse resp);
    short getMessageId();
}