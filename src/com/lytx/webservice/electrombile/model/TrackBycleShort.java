package com.lytx.webservice.electrombile.model;

public class TrackBycleShort {
	private String fdid;
	private String ruleId;
	private String caseId;
	private String status;
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private int type;

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getFdid() {
		return fdid;
	}

	public void setFdid(String fdid) {
		this.fdid = fdid;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public TrackBycleShort() {

	}

	public TrackBycleShort(String fdid, String ruleId) {
		this.fdid = fdid;
		this.ruleId = ruleId;
	}

}
