package com.lytx.webservice.batch;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.liveyc.rabbitmq.DealDataUtil;

public class ThreadStolen extends Thread {
	public static AtomicInteger numberThread = new AtomicInteger(0);
	private List<byte[]> alarmlist;

	public ThreadStolen(List<byte[]> _list) {
		alarmlist = _list;
	}

	public void run() {
		numberThread.getAndIncrement();
		try {
			for (byte[] infodata : alarmlist) {
				try {
					DealDataUtil.dealStolen(infodata);
				} catch (Exception ex) {
					int a=0;
				}
			}

		} catch (Exception ex) {
			// System.out.print(ex);

		}
		numberThread.getAndDecrement();
	}
}
