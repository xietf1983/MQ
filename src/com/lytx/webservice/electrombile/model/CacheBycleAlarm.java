package com.lytx.webservice.electrombile.model;

public class CacheBycleAlarm {
	private String fdid;

	public String getFdid() {
		return fdid;
	}

	public void setFdid(String fdid) {
		this.fdid = fdid;
	}

	public String getLastStationId() {
		return lastStationId;
	}

	public void setLastStationId(String lastStationId) {
		this.lastStationId = lastStationId;
	}

	public int getCacheCount() {
		return cacheCount;
	}

	public void setCacheCount(int cacheCount) {
		this.cacheCount = cacheCount;
	}

	private String lastStationId;
	private int cacheCount;

	private long lastActive;

	public long getLastActive() {
		return lastActive;
	}

	public void setLastActive(long lastActive) {
		this.lastActive = lastActive;
	}

}
