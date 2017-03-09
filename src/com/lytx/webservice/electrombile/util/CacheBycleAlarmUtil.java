package com.lytx.webservice.electrombile.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.lytx.analysis.model.RuleModel;
import com.lytx.analysis.service.BycleAlarmRuleServiceUtil;
import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.model.BycleBlack;
import com.lytx.webservice.electrombile.model.BycleInfoShort;
import com.lytx.webservice.electrombile.model.BycleStationModel;
import com.lytx.webservice.electrombile.model.CacheBycleAlarm;
import com.lytx.webservice.electrombile.model.TrackBycleShort;
import com.lytx.webservice.electrombile.service.ElectrombileServiceUtil;

public class CacheBycleAlarmUtil {
	public static ConcurrentHashMap<String, CacheBycleAlarm> cacheModel = new ConcurrentHashMap();

	public static boolean mactResult(BycleAlarmModel by, List<RuleModel> ruleList) {
		if (ruleList == null || ruleList.size() == 0) {
			return false;
		}

		for (RuleModel r : ruleList) {
			int status = Integer.parseInt(by.getFdStatus()) & r.getFdstatus();
			if (status == r.getFdstatus()) {
				CacheBycleAlarm c = CacheBycleAlarmUtil.cacheModel.get(by.getFdId() + "-" + r.getRuleId());
				if (c == null) {
					c = new CacheBycleAlarm();
					c.setCacheCount(1);
					c.setFdid(by.getStationId());
					c.setLastStationId(by.getStationId());
					c.setLastActive((new Date()).getTime());

				} else {
					if (c.getLastStationId().equals(by.getStationId())) {
						c.setCacheCount(c.getCacheCount() + 1);
						c.setLastActive((new Date()).getTime());
					}

				}
				if (r.getStationNum() >= c.getCacheCount()) {
					/**
					 * 黑名单，不处理
					 */
					TrackBycleShort tshort = ElectrombileServiceUtil.getService().getTrackBycleShort(by.getFdId());
					if (tshort != null) {
						cacheModel.remove(by.getFdId() + "-" + r.getRuleId());
						return false;
					}
					/**
					 * 白名单。不处理
					 */

					if (ElectrombileServiceUtil.getService().findBycleWhite(by)) {
						cacheModel.remove(by.getFdId() + "-" + r.getRuleId());
						return false;
					}
					// 处理
					BycleBlack b = new BycleBlack();
					BycleInfoShort bycleInfoShort = ElectrombileServiceUtil.getService().getBycleInfoShort(by.getFdId());
					if (bycleInfoShort != null) {
						by.setBycleOwner(bycleInfoShort.getOwner());
						by.setUserTel(bycleInfoShort.getUserTel());
					}
					BycleStationModel stationModel = ElectrombileServiceUtil.getService().getBycleStationModel(by.getStationId());
					if (stationModel != null) {
						by.setAreaCode(stationModel.getAreaCode());
					}
					b.setActNo(by.getActNo());
					b.setAlarmId(by.getAlarmId());
					b.setAlarmMemo(by.getAlarmMemo());
					b.setAlarmTime(by.getAlarmTime());
					b.setAlarmType(by.getAlarmType());
					b.setAreaCode(by.getAreaCode());
					b.setAreaId(by.getAreaId());
					b.setBycleOwner(by.getBycleOwner());
					b.setCaseId(ElectrombileServiceUtil.getService().getCaseIdNext(by.getAreaCode()));
					b.setRuleId(UUID.randomUUID().toString());
					b.setBycleId(by.getBycleid());
					b.setFdId(by.getFdId());
					b.setPlatenoArea(by.getPlatenoArea());
					b.setPlateNo(by.getPlateNo());
					b.setBycleOwner(by.getBycleOwner());
					b.setAreaCode(by.getAreaCode());
					b.setStatus("0");
					b.setIssecret(0);
					b.setSouce("4");
					b.setBycleOwner(by.getBycleOwner());
					b.setAlarmPhone(by.getUserTel());
					b.setType(0);// 异常车辆库
					ElectrombileServiceUtil.getService().addBycleBlack(b, null);
					by.setCaseId(b.getCaseId());
					by.setRuleId(b.getRuleId());
					by.setType(0);
					by.setStatus("0");
					ElectrombileServiceUtil.getService().addBycleHandleAlarm(by);
					cacheModel.remove(by.getFdId() + "-" + r.getRuleId());
					return true;

				}

			} else {
				return false;
			}

		}
		return false;

	}

}
