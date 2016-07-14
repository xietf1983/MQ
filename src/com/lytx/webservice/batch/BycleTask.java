package com.lytx.webservice.batch;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.util.DateUtil;

public class BycleTask {
	private static BycleTask instance = new BycleTask();
	private EventQueue bycleQue = new EventQueue(20000);

	public static BycleTask getInstance() {
		return instance;
	}

	private BycleTask() {

	}

	public void putBycleAlarmModelEvent(BycleAlarmModel v) throws IndexOutOfBoundsException {
		getInstance().bycleQue.enqueue(v);
	}

	public EventQueue getBycleQue() {
		return getInstance().bycleQue;
	}

}
