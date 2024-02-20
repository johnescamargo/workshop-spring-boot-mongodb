package com.imav.whatsapp.configuration;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import com.imav.whatsapp.model.OutgoingMessage;

public class WebSocketSessionHandler extends StompSessionHandlerAdapter {

	@Override
	public Class<OutgoingMessage> getPayloadType(StompHeaders headers) {
		return OutgoingMessage.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		System.out.println("Received: " + ((OutgoingMessage) payload).getContent());
	}
}