package com.lytx.webservice.electrombile.model;

public class BycleStationModel {
	public String stationId;
	public String stationName;
	public Long longitude;
	public Long latitude;
	public Long range;
	public String areaId;
	public String createrUserId;
	public String channelId;
	public String channelName;
	public String areaCode;
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public Long getLongitude() {
		return longitude;
	}
	public void setLongitude(Long longitude) {
		this.longitude = longitude;
	}
	public Long getLatitude() {
		return latitude;
	}
	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}
	public Long getRange() {
		return range;
	}
	public void setRange(Long range) {
		this.range = range;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getCreaterUserId() {
		return createrUserId;
	}
	public void setCreaterUserId(String createrUserId) {
		this.createrUserId = createrUserId;
	}

}
