package com.lytx.webservice.electrombile.model;

public class BycleInfoShort {
	private String plateNo;
	private String fdid;
	private Long id;
	private String  platenoArea;
	private Integer type;
	private String userTel;
	private String owner;
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getUserTel() {
		return userTel;
	}
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPlatenoArea() {
		return platenoArea;
	}
	public void setPlatenoArea(String platenoArea) {
		this.platenoArea = platenoArea;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public String getFdid() {
		return fdid;
	}
	public void setFdid(String fdid) {
		this.fdid = fdid;
	}
}
