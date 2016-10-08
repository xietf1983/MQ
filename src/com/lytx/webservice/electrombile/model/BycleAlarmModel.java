package com.lytx.webservice.electrombile.model;

import java.util.Date;

public class BycleAlarmModel {
	private String plateNo;
	private Long alarmId;
	private String fdId;
	private Date alarmTime;
	private Date receiveTime = new Date();

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}
	private Date createDate;
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	private String fdStatus;
	private Integer fdLowElecTag;
	private Integer fdMoveTag;
	private Integer fdLockTag;
	private Integer fdChannel;
	private String stationId;
	private String stationName;
	private Long longitude;
	private Long latitude;
	private Integer lngExt;
	private Integer latExt;
	private String vehicleDirect;
	private String areaId;
	private String bycleOwner;
	private Integer type;
	private String trackDesc;
	private Long trackId;
	private Integer dealStatus;
	private String alarmMemo;
	private String trackMan;
	private String caseId;
	private Integer fdNoElecTag;
	private String ruleId;
	private String platenoArea;
	private String actNo;
	private String status;
	private Integer alarmType;
	private String modifytab;
	private String areaCode;
	private String userTel;

	public String getUserTel() {
		return userTel;
	}

	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getModifytab() {
		return modifytab;
	}

	public void setModifytab(String modifytab) {
		this.modifytab = modifytab;
	}

	public Integer getAlarmType() {
		return alarmType;
	}

	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActNo() {
		return actNo;
	}

	public void setActNo(String actNo) {
		this.actNo = actNo;
	}

	public String getPlatenoArea() {
		return platenoArea;
	}

	public void setPlatenoArea(String platenoArea) {
		this.platenoArea = platenoArea;
	}

	public Long getBycleid() {
		return bycleid;
	}

	public void setBycleid(Long bycleid) {
		this.bycleid = bycleid;
	}

	private Long bycleid;

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getFdNoElecTag() {
		return fdNoElecTag;
	}

	public void setFdNoElecTag(Integer fdNoElecTag) {
		this.fdNoElecTag = fdNoElecTag;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public Long getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(Long alarmId) {
		this.alarmId = alarmId;
	}

	public String getFdId() {
		return fdId;
	}

	public void setFdId(String fdId) {
		this.fdId = fdId;
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

	public Integer getFdLowElecTag() {
		return fdLowElecTag;
	}

	public void setFdLowElecTag(Integer fdLowElecTag) {
		this.fdLowElecTag = fdLowElecTag;
	}

	public Integer getFdMoveTag() {
		return fdMoveTag;
	}

	public void setFdMoveTag(Integer fdMoveTag) {
		this.fdMoveTag = fdMoveTag;
	}

	public Integer getFdLockTag() {
		return fdLockTag;
	}

	public void setFdLockTag(Integer fdLockTag) {
		this.fdLockTag = fdLockTag;
	}

	public Integer getFdChannel() {
		return fdChannel;
	}

	public void setFdChannel(Integer fdChannel) {
		this.fdChannel = fdChannel;
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
