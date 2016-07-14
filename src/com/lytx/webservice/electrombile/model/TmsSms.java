package com.lytx.webservice.electrombile.model;

import java.util.Date;

public class TmsSms {
	private Long id;
	private String fdid;
	private String plateno;
	private String platenoArea;
	private String owner;
	private String mobilePhone;
	private String smscontent;
	private Integer status;
	private Date createDate;
	private Date sendDate;
	private String smsType;
	private String sendUser;
	private String stationId;
	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFdid() {
		return fdid;
	}
	public void setFdid(String fdid) {
		this.fdid = fdid;
	}
	public String getPlateno() {
		return plateno;
	}
	public void setPlateno(String plateno) {
		this.plateno = plateno;
	}
	public String getPlatenoArea() {
		return platenoArea;
	}
	public void setPlatenoArea(String platenoArea) {
		this.platenoArea = platenoArea;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getSmscontent() {
		return smscontent;
	}
	public void setSmscontent(String smscontent) {
		this.smscontent = smscontent;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getSendDate() {
		return sendDate;
	}
	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}
	public String getSmsType() {
		return smsType;
	}
	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}
	public String getSendUser() {
		return sendUser;
	}
	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}
}
