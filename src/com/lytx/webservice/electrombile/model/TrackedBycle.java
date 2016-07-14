package com.lytx.webservice.electrombile.model;

public class TrackedBycle {
	private String ruleId;
	private String roleId;
	private String plateNo;
	private String fdId;
	private String roleName;
	private String photoId;
	private String vehicleOwner;
	private String tracker;
	private String memo;
	private String creator;
	private Integer trackLevel;
	private String alarmPhone;
	private Integer isSecret;
	private String trackman;
	public String getRuleId() {
		return ruleId;
	}
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
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
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getPhotoId() {
		return photoId;
	}
	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}
	public String getVehicleOwner() {
		return vehicleOwner;
	}
	public void setVehicleOwner(String vehicleOwner) {
		this.vehicleOwner = vehicleOwner;
	}
	public String getTracker() {
		return tracker;
	}
	public void setTracker(String tracker) {
		this.tracker = tracker;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Integer getTrackLevel() {
		return trackLevel;
	}
	public void setTrackLevel(Integer trackLevel) {
		this.trackLevel = trackLevel;
	}
	public String getAlarmPhone() {
		return alarmPhone;
	}
	public void setAlarmPhone(String alarmPhone) {
		this.alarmPhone = alarmPhone;
	}
	public Integer getIsSecret() {
		return isSecret;
	}
	public void setIsSecret(Integer isSecret) {
		this.isSecret = isSecret;
	}
	public String getTrackman() {
		return trackman;
	}
	public void setTrackman(String trackman) {
		this.trackman = trackman;
	}
}
