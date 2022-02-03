package com.abvertx.web.message;
public abstract class AbstractUpMessage extends AbstractMessage{
    @Override
    protected void decodeMessage() {
        decodeBody();
    }

    @Override
    protected void encodeMessage() {}

    protected abstract void decodeBody();
}