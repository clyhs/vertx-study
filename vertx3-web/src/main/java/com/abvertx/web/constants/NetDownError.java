package com.abvertx.web.constants;

import com.abvertx.web.message.AbstractDownMessage;

public class NetDownError extends AbstractDownMessage {
    public NetDownError(short requestId, HttpStatus status){
        this.messageId = requestId;
        this.resultCode = status.code();
    }

    @Override
    protected void encodeBody() {

    }
}