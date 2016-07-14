package com.liveyc.rabbitmq;

import org.springframework.amqp.core.Message;

public class ThreadStole extends Thread {
	// @Override
	private byte[] beytedata;
	static private int threadCounts = 0;

	public ThreadStole(byte[] data) {
		synchronized (ThreadStole.class) {
			// 线程总数加1
			threadCounts++;
		}
		this.beytedata = data;
	}

	public void run() {
		try {
			DealDataUtil.dealStolen(beytedata);
		} finally {
			synchronized (ThreadStole.class) {
				threadCounts--;
			}
		}
	}

	static public int getThreadCounts() {
		synchronized (ThreadStole.class) {
			return threadCounts;
		}
	}
}
