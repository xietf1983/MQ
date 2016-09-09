package com.lytx.webservice.scheduling;

public class TrackedBycleScheduling {
	public void afterPropertiesSet() {
		new TrackedBycleJob().start();// Ö´ÐÐ
		new DbNotifyChangeJob().start();
	}

}
