package com.lytx.webservice.batch;


public class TrailMessageTask {
	private static TrailMessageTask instance = new TrailMessageTask();
	private EventQueue bycleQue = new EventQueue(80000);

	public static TrailMessageTask getInstance() {
		return instance;
	}

	private TrailMessageTask() {

	}

	public void putTrailMessageEvent(byte[] v) throws IndexOutOfBoundsException {
		getInstance().bycleQue.enqueue(v);
	}

	public EventQueue getTrailMessageQue() {
		return getInstance().bycleQue;
	}

}
