package com.lytx.webservice.batch;

public class AbnormalMessageTask {
	private static AbnormalMessageTask instance = new AbnormalMessageTask();
	private EventQueue bycleQue = new EventQueue(80000);

	public static AbnormalMessageTask getInstance() {
		return instance;
	}

	private AbnormalMessageTask() {

	}

	public void putAbnormalMessageEvent(byte[] v) throws IndexOutOfBoundsException {
		getInstance().bycleQue.enqueue(v);
	}

	public EventQueue getAbnormalMessageQue() {
		return getInstance().bycleQue;
	}

}
