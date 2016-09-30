package com.lytx.analysis.service;

import java.util.Date;
import java.util.List;

import com.lytx.analysis.model.RuleModel;
import com.lytx.webservice.electrombile.model.BycleAlarmModel;

public interface BycleAlarmRuleService {
	/**
	 * ��ȡ�������������Ӧ����1
	 * 
	 * @param by
	 * @return
	 */
	public int getWaringCountLastDay(BycleAlarmModel by);

	/**
	 * ��ȡ���ݿ��ʱ��
	 * 
	 * @return
	 */
	public Date getDbDateTime();

	/**
	 * @param startDate
	 * @param endDate
	 * @param by
	 * @return
	 */
	public boolean judgementNormalAlfer(String startDate, String endDate, BycleAlarmModel by);

	/**
	 * ���ӵ���
	 */
	public void addbycleAlarmPreDealHis(BycleAlarmModel by, String ruleId, boolean delete);

	public List<RuleModel> getAllEnableRules();

	public List<String> getRules(String areaId);

	public String getFirstAreaId(String areaId);
	
	public List<BycleAlarmModel> getBycleAlarmPreDeal();
}
