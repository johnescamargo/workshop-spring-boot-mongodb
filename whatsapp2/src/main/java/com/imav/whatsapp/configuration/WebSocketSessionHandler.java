package com.imav.whatsapp.configuration;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;

import com.imav.whatsapp.model.OutgoingMessage;

@Component
public class WebSocketSessionHandler extends StompSessionHandlerAdapter {

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return OutgoingMessage.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		System.out.println("Received: " + ((OutgoingMessage) payload).getContent());
	}

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload,
			Throwable exception) {
		super.handleException(session, command, headers, payload, exception);
	}

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		boolean resp = session.isConnected();
		if(resp) {
			System.out.println("Connected to WebSocket StompSession - Java Client");
		} else {
			System.out.println("Disconnected to WebSocket StompSession - Java Client");
		}
	}

}