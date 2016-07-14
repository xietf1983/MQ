package com.liveyc.rabbitmq;

import org.springframework.amqp.core.Message;

public class ThreadTrail extends Thread {
	// @Override
	private byte[] beytedata;

	public ThreadTrail(byte[] data) {
		this.beytedata = data;
	}

	public void run() {
		DealDataUtil.dealTrailData(beytedata);
	}
}
