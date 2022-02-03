package com.abvertx.web.message.reco;

import com.abvertx.web.message.MessageFactory;

public interface IMessageRecognizer {
    MessageFactory recognize(short messageId);
}