package com.imav.whatsapp.configuration;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {
	private StompSession session;

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		this.setSession(session);
		System.out.println("Session done!");
	}

	public StompSession getSession() {
		return session;
	}

	public void setSession(StompSession session) {
		this.session = session;
	}

}
