package com.lytx.webservice.batch;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.util.DateUtil;

public class StolenMessageTask {
	private static StolenMessageTask instance = new StolenMessageTask();
	private EventQueue bycleQue = new EventQueue(80000);

	public static StolenMessageTask getInstance() {
		return instance;
	}

	private StolenMessageTask() {

	}

	public void putStolenMessageEvent(byte[] v) throws IndexOutOfBoundsException {
		getInstance().bycleQue.enqueue(v);
	}

	public EventQueue getStolenMessageQue() {
		return getInstance().bycleQue;
	}

}
