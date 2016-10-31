package com.lytx.analysis.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.lytx.analysis.model.RuleModel;
import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.model.BycleBlack;
import com.lytx.webservice.electrombile.model.BycleInfoShort;
import com.lytx.webservice.electrombile.model.BycleStationModel;
import com.lytx.webservice.electrombile.model.TrackBycleShort;
import com.lytx.webservice.electrombile.service.ElectrombileServiceUtil;
import com.lytx.webservice.sequence.service.SequenceGeneratorServiceUtil;
import com.lytx.webservice.util.DateUtil;

public class BycleAlarmRuleUtil {
	private static byte[] mutex = new byte[0];
	static Date lastBuildDate = null;
	static private Logger iLog = Logger.getLogger(BycleAlarmRuleUtil.class);
	static Map<String, RuleModel> rules = new HashMap();

	public static void buildRules() {
		iLog.error("BycleAlarmRuleServiceUtil.bulidRules");
		synchronized (mutex) {
			if (lastBuildDate == null || new Date().getTime() - lastBuildDate.getTime() > DateUtil.MINUTE * 10) {
				lastBuildDate = new Date();
				rules.clear();
				List<RuleModel> list = BycleAlarmRuleServiceUtil.getAllEnableRules();
				for (RuleModel r : list) {
					String xmldate = new String(r.getRuleConfig());
					try {
						JSONObject json = JSONObject.fromObject(xmldate);
						String type = json.getString("TYPE");
						if (type != null && !type.equals(""))
							r.setType(Integer.parseInt(type));
						if (type.equals("1")) {
							r.setType(Integer.parseInt(type));
							String strLastWaring = json.getString("LASTDAYWARING");
							r.setWaringCount(Integer.parseInt(strLastWaring));
						} else if (type.equals("2")) {
							String strIncomeingTime = json.getString("INCOMINGTIME");
							r.setIncomeingTime(Integer.parseInt(strIncomeingTime));
						}
						rules.put(r.getRuleId(), r);
					} catch (Exception ex) {

					}
				}
			}

		}
	}

	public static boolean match(BycleAlarmModel by, Date now) {
		// boolean ret = false;
		buildRules();
		boolean ret = false;
		//iLog.error("rules-list" + rules);
		TrackBycleShort tshort = ElectrombileServiceUtil.getService().getTrackBycleShort(by.getFdId());
		boolean candelete = true;
		if (ElectrombileServiceUtil.getService().findBycleWhite(by)) {
			//iLog.error("白名单" + by.getFdId());
			BycleAlarmRuleServiceUtil.addbycleAlarmPreDealHis(by, null, true);
			return false;
		}
		if (by.getAreaId() != null) {
			String firstAreaId = BycleAlarmRuleServiceUtil.getFirstAreaId(by.getAreaId());
			List<String> list = BycleAlarmRuleServiceUtil.getRules(firstAreaId);
			if (list != null && list.size() > 0) {
				for (String s : list) {
					RuleModel r = rules.get(s);
					if (r != null && r.getType() == 1) {
						// 获取24小时前的异常过车数量
						int countValue = BycleAlarmRuleServiceUtil.getWaringCountLastDay(by);
						if (countValue > r.getWaringCount()) {
							ret = true;
							BycleAlarmRuleServiceUtil.addbycleAlarmPreDealHis(by, r.getRuleId(), true);
							break;
						}

					}
					if (r != null && r.getType() == 2) {
						long a = r.getIncomeingTime();
						Date end = new Date(by.getCreateDate().getTime() + a * 60 * 1000);
						if (now.getTime() > end.getTime()) {
							boolean b = BycleAlarmRuleServiceUtil.judgementNormalAlfer(DateUtil.toString(by.getCreateDate()), DateUtil.toString(end), by);
							if (b) {
								BycleAlarmRuleServiceUtil.addbycleAlarmPreDealHis(by, r.getRuleId(), true);
								ret = true;
								break;
							}
						} else {
							candelete = false;
							continue;
						}
					}

				}
				if (candelete && !ret) {
					BycleBlack b = new BycleBlack();
					BycleInfoShort bycleInfoShort = ElectrombileServiceUtil.getService().getBycleInfoShort(by.getFdId());
					if (bycleInfoShort != null) {
						by.setBycleOwner(bycleInfoShort.getOwner());
						by.setUserTel(bycleInfoShort.getUserTel());
					}
					//by.setAlarmId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.bycleseqName));
					b.setActNo(by.getActNo());
					b.setAlarmId(by.getAlarmId());
					b.setAlarmMemo(by.getAlarmMemo());
					b.setAlarmTime(by.getAlarmTime());
					b.setAlarmType(by.getAlarmType());
					BycleStationModel stationModel =ElectrombileServiceUtil.getService().getBycleStationModel(by.getStationId());
					if(stationModel!= null){
						by.setAreaCode(stationModel.getAreaCode());
					}
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
					BycleAlarmRuleServiceUtil.addbycleAlarmPreDealHis(by, null, true);
				}

			} else {
				BycleBlack b = new BycleBlack();
				BycleInfoShort bycleInfoShort = ElectrombileServiceUtil.getService().getBycleInfoShort(by.getFdId());
				if (bycleInfoShort != null) {
					by.setBycleOwner(bycleInfoShort.getOwner());
					by.setUserTel(bycleInfoShort.getUserTel());
				}
				BycleStationModel stationModel =ElectrombileServiceUtil.getService().getBycleStationModel(by.getStationId());
				if(stationModel!= null){
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
				BycleAlarmRuleServiceUtil.addbycleAlarmPreDealHis(by, null, true);

			}

		}else{
			BycleAlarmRuleServiceUtil.addbycleAlarmPreDealHis(by, null, true);
		}

		return ret;

	}
}
