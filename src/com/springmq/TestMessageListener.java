package com.springmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

public class TestMessageListener implements MessageListener {

	@Override
	public void onMessage(Message arg0) {
		System.out.print(arg0.getBody());

	}

}
