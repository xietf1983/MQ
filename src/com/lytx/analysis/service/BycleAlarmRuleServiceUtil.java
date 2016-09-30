package com.lytx.analysis.service;

import java.util.Date;
import java.util.List;

import com.lytx.analysis.model.RuleModel;
import com.lytx.webservice.electrombile.model.BycleAlarmModel;

public class BycleAlarmRuleServiceUtil {
	private static BycleAlarmRuleService _service;

	public static BycleAlarmRuleService getService() {
		if (_service == null) {
			throw new RuntimeException("BycleAlarmRuleService is not set");
		}
		return _service;
	}

	public void setService(BycleAlarmRuleService service) {
		_service = service;
	}

	public static List<RuleModel> getAllEnableRules() {
		return getService().getAllEnableRules();
	}

	public static List<String> getRules(String areaId) {
		return getService().getRules(areaId);
	}

	public static String getFirstAreaId(String areaId) {
		return getService().getFirstAreaId(areaId);
	}

	public static int getWaringCountLastDay(BycleAlarmModel by) {
		return getService().getWaringCountLastDay(by);
	}

	public static boolean judgementNormalAlfer(String startDate, String endDate, BycleAlarmModel by) {
		return getService().judgementNormalAlfer(startDate, endDate, by);
	}

	public static void addbycleAlarmPreDealHis(BycleAlarmModel by, String ruleId, boolean delete) {
		getService().addbycleAlarmPreDealHis(by, ruleId, delete);
	}

	public static Date getDbDateTime() {
		return getService().getDbDateTime();
	}

	public static List<BycleAlarmModel> getBycleAlarmPreDeal() {
		return getService().getBycleAlarmPreDeal();
	}

}
