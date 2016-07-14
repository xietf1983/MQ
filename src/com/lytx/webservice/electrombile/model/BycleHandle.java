package com.lytx.webservice.electrombile.model;

import java.util.Date;

public class BycleHandle {
	private long alarmId;
	private String caseId;
	private String plateNo;
	private String fdId;
	private String stationId;
	private String stationName;
	private Date alarmTime;
	private String fdStatus;
	private Long longitude;
	private Long latitude;
	private Integer lngExt;
	private Integer latExt;
	private String vehicleDirect;
	private String  areaId;
	private String bycleOwner;
	private Integer type;
	private  String trackDesc;
	private Long trackId;
	private Integer dealStatus;
	private  String alarmMemo;
	private String trackMan;
	public long getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(long alarmId) {
		this.alarmId = alarmId;
	}
	public String getCaseId() {
		return caseId;
	}
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public String getFdId() {
		return fdId;
	}
	public void setFdId(String fdId) {
		this.fdId = fdId;
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
	public Date getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}
	public String getFdStatus() {
		return fdStatus;
	}
	public void setFdStatus(String fdStatus) {
		this.fdStatus = fdStatus;
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
	public Integer getLngExt() {
		return lngExt;
	}
	public void setLngExt(Integer lngExt) {
		this.lngExt = lngExt;
	}
	public Integer getLatExt() {
		return latExt;
	}
	public void setLatExt(Integer latExt) {
		this.latExt = latExt;
	}
	public String getVehicleDirect() {
		return vehicleDirect;
	}
	public void setVehicleDirect(String vehicleDirect) {
		this.vehicleDirect = vehicleDirect;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getBycleOwner() {
		return bycleOwner;
	}
	public void setBycleOwner(String bycleOwner) {
		this.bycleOwner = bycleOwner;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getTrackDesc() {
		return trackDesc;
	}
	public void setTrackDesc(String trackDesc) {
		this.trackDesc = trackDesc;
	}
	public Long getTrackId() {
		return trackId;
	}
	public void setTrackId(Long trackId) {
		this.trackId = trackId;
	}
	public Integer getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(Integer dealStatus) {
		this.dealStatus = dealStatus;
	}
	public String getAlarmMemo() {
		return alarmMemo;
	}
	public void setAlarmMemo(String alarmMemo) {
		this.alarmMemo = alarmMemo;
	}
	public String getTrackMan() {
		return trackMan;
	}
	public void setTrackMan(String trackMan) {
		this.trackMan = trackMan;
	}
}
