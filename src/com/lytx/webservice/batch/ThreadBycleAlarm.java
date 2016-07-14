package com.lytx.webservice.batch;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.service.ElectrombileServiceUtil;

public class ThreadBycleAlarm extends Thread {
	private List<BycleAlarmModel> alarmlist;
	public static AtomicInteger numberThread = new AtomicInteger(0);

	public ThreadBycleAlarm(List<BycleAlarmModel> _list) {
		alarmlist = _list;
	}

	public void run() {
		numberThread.getAndIncrement();
		try {

			ElectrombileServiceUtil.batchinsertBycleAlarmList(alarmlist);
		} catch (Exception ex) {
			System.out.print(ex);

		}
		numberThread.getAndDecrement();
	}

}
