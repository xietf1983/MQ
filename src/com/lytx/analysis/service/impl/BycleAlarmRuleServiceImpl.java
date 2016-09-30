package com.lytx.analysis.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lytx.analysis.model.RuleModel;
import com.lytx.analysis.service.BycleAlarmRuleService;
import com.lytx.analysis.service.persistence.BycleAlarmRulePersistence;
import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.model.BycleInfoShort;
import com.lytx.webservice.electrombile.model.BycleStationModel;
import com.lytx.webservice.electrombile.model.TrackBycleShort;
import com.lytx.webservice.electrombile.service.persistence.ElectrombilePersistence;
import com.lytx.webservice.util.DateUtil;

public class BycleAlarmRuleServiceImpl implements BycleAlarmRuleService {
	private BycleAlarmRulePersistence persistence;
	public static Map<String, List<String>> ruleMap = new HashMap();
	public static Map<String, String> firstMap = new HashMap();
	public static Date lastBulidDate = new Date();

	public BycleAlarmRulePersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(BycleAlarmRulePersistence persistence) {
		this.persistence = persistence;
	}

	public int getWaringCountLastDay(BycleAlarmModel by) {
		return getPersistence().getWaringCountLastDay(by);

	}

	public Date getDbDateTime() {
		return getPersistence().getDbDateTime();
	}

	public boolean judgementNormalAlfer(String startDate, String endDate, BycleAlarmModel by) {
		return getPersistence().judgementNormalAlfer(startDate, endDate, by);
	}

	public void addbycleAlarmPreDealHis(BycleAlarmModel by, String ruleId, boolean delete) {
		getPersistence().addbycleAlarmPreDealHis(by, ruleId, delete);

	}

	public List<String> getRules(String areaId) {
		if (new Date().getTime() - lastBulidDate.getTime() > DateUtil.DAY) {
			ruleMap.clear();
			lastBulidDate = new Date();
		}
		if (ruleMap.get(areaId) != null) {
			return ruleMap.get(areaId);
		} else {
			List<String> list = getPersistence().getRules(areaId);
			if (list != null && list.size() > 0) {
				ruleMap.put(areaId, list);
			}
			return list;
		}
	}

	public List<RuleModel> getAllEnableRules() {
		return getPersistence().getAllEnableRules();
	}

	public String getFirstAreaId(String areaId) {

		String ret = null;
		if (firstMap.get(areaId) != null) {
			return firstMap.get(areaId);
		} else {
			ret = getPersistence().getFirstAreaId(areaId);
			if (ret != null) {
				firstMap.put(areaId, ret);
			}

		}
		if (new Date().getTime() - lastBulidDate.getTime() > DateUtil.DAY) {
			firstMap.clear();
			lastBulidDate = new Date();
		}
		return ret;

	}

	public List<BycleAlarmModel> getBycleAlarmPreDeal() {
		return getPersistence().getBycleAlarmPreDeal();
	}

}
