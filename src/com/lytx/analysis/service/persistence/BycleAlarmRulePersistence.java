package com.lytx.analysis.service.persistence;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.lytx.analysis.model.RuleModel;
import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.util.DateUtil;

public class BycleAlarmRulePersistence extends SqlSessionDaoSupport {
	// 24小时前报警数量
	public int getWaringCountLastDay(BycleAlarmModel by) {
		Map map = new HashMap();
		map.put("FDID", by.getFdId());
		map.put("PLATENO", by.getPlateNo());
		String date = DateUtil.toString(new Date(by.getAlarmTime().getTime() - DateUtil.DAY));
		map.put("STARTTIME", date + " 00:00:00");
		map.put("ENDTIME", date + " 23:59:59");
		Long countValue = (Long) getSqlSession().selectOne("getWaringCountLastDay", map);
		return countValue.intValue();
	}

	public Date getDbDateTime() {
		String date = (String) getSqlSession().selectOne("getgetDbDateTime", null);
		return DateUtil.stringToDate(date);
	}

	public static boolean waringstatus(BycleAlarmModel b) {
		if ((b.getFdNoElecTag() == 1) || (b.getFdLockTag() == 1 && b.getFdMoveTag() == 1)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 判断最近5分钟是否正常
	 * 
	 * @param startDate
	 * @param endDate
	 * @param by
	 * @return
	 */
	public boolean judgementNormalAlfer(String startDate, String endDate, BycleAlarmModel by) {
		boolean ret = false;
		Map map = new HashMap();
		map.put("FDID", by.getFdId());
		map.put("PLATENO", by.getPlateNo());
		map.put("STARTTIME", startDate);
		map.put("ENDTIME", endDate);
		List<BycleAlarmModel> listData = getSqlSession().selectList("judgementNormalAlfer", map);
		if (listData != null && listData.size() > 0) {
			for (BycleAlarmModel b : listData) {
				if (!(waringstatus(b))) {
					ret = true;
					break;

				}
			}
		}
		return ret;
	}

	/**
	 * 
	 * @param by
	 * @param ruleId
	 */
	public void addbycleAlarmPreDealHis(BycleAlarmModel by, String ruleId, boolean delete) {
		if (ruleId != null) {
			by.setRuleId(ruleId);
			try {
				getSqlSession().insert("bycleAlarmPreDealHis_insert", by);
			} catch (Exception ex) {

			}
		}
		
		if (delete) {
			// 删除待处理表
			getSqlSession().delete("bycleAlarmPreDeal_delete", by);
		}

	}

	public List<String> getRules(String areaId) {
		return getSqlSession().selectList("bycleRules_selectByAreaId", areaId);

	}

	public String getFirstAreaId(String areaId) {
		return getSqlSession().selectOne("bycleRules_selectFirstAreaId", areaId);
	}

	/**
	 * 获取所有的规则表
	 * 
	 * @return
	 */
	public List<RuleModel> getAllEnableRules() {
		return getSqlSession().selectList("bycleRules_getAllEnableRules", null);
	}

	/**
	 * 获取待处理的设备
	 * 
	 * @return
	 */
	public List<BycleAlarmModel> getBycleAlarmPreDeal() {
		return getSqlSession().selectList("bycleAlarmPreDeal_select", null);
	}
}
