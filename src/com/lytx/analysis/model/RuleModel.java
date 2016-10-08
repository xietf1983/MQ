package com.lytx.analysis.model;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;

public class RuleModel {
	private boolean enable;
	private int type;
	private String ruleName;
	private byte[] ruleConfig;
	private int incomeingTime;// µ•Œª∑÷÷”

	public int getIncomeingTime() {
		return incomeingTime;
	}

	public void setIncomeingTime(int incomeingTime) {
		this.incomeingTime = incomeingTime;
	}

	public int getWaringCount() {
		return waringCount;
	}

	public void setWaringCount(int waringCount) {
		this.waringCount = waringCount;
	}

	private int waringCount;

	public byte[] getRuleConfig() {
		return ruleConfig;
	}

	public void setRuleConfig(byte[] ruleConfig) {
		this.ruleConfig = ruleConfig;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	private String ruleId;

}
