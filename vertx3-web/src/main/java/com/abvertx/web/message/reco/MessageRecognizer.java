package com.abvertx.web.message.reco;

import com.abvertx.web.constants.HandlerCode;
import com.abvertx.web.handler.demo.DemoRequest;
import com.abvertx.web.message.MessageFactory;

/**
 * 
 * @date Feb 3, 2022 10:03:58 PM
 *
 * @author 大鱼
 *
 */
public class MessageRecognizer implements IMessageRecognizer {
    @Override
    public MessageFactory recognize(short messageId) {
    	switch (messageId){
	        case HandlerCode.DEMO_V1: 
	        	return new DemoRequest();
	        default:
	        	return null;
        }
    }
}