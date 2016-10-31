package com.lytx.webservice.electrombile.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.model.BycleBlack;
import com.lytx.webservice.electrombile.model.BycleInfoShort;
import com.lytx.webservice.electrombile.model.BycleLostRecord;
import com.lytx.webservice.electrombile.model.BycleStationModel;
import com.lytx.webservice.electrombile.model.SoapDbNotify;
import com.lytx.webservice.electrombile.model.TmsSms;
import com.lytx.webservice.electrombile.model.TrackBycleShort;
import com.lytx.webservice.electrombile.model.TrackedBycle;
import com.lytx.webservice.electrombile.service.ElectrombileService;
import com.lytx.webservice.electrombile.service.persistence.ElectrombilePersistence;
import com.lytx.webservice.sequence.service.SequenceGeneratorServiceUtil;
import com.lytx.webservice.util.DateUtil;

public class ElectrombileServiceImpl implements ElectrombileService {
	private ElectrombilePersistence persistence;

	public static Map<String, BycleInfoShort> fdidCodeMap = new HashMap();
	public static Map<String, BycleStationModel> stationCodeMap = new HashMap();
	public static Map<String, TrackBycleShort> trackBycleMap = new HashMap();
	public static Date lastFDDate = new Date();
	public static Date lastSTDate = new Date();
	public static Map<String, String> tmsDatemap = new HashMap();

	public ElectrombilePersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(ElectrombilePersistence persistence) {
		this.persistence = persistence;
	}

	public String getCaseIdNext(String areaid) {
		return getPersistence().getCaseIdNext(areaid);
	}

	@Override
	public BycleStationModel getBycleStationModel(String code) {
		BycleStationModel bycleInfoShort = null;
		if (new Date().getTime() - lastSTDate.getTime() > DateUtil.HOUR) {
			stationCodeMap.clear();
			lastSTDate = new Date();
		}
		if (stationCodeMap.get(code) != null) {
			return stationCodeMap.get(code);
		} else {
			bycleInfoShort = getPersistence().fineOneBycleStationModel(code);
			if (bycleInfoShort != null) {
				stationCodeMap.put(code, bycleInfoShort);
			}
		}
		return bycleInfoShort;
	}

	@Override
	public BycleInfoShort getBycleInfoShort(String fdId) {
		BycleInfoShort bycleInfoShort = null;
		if (new Date().getTime() - lastFDDate.getTime() > DateUtil.HOUR) {
			fdidCodeMap.clear();
			lastFDDate = new Date();
		}
		if (fdidCodeMap.get(fdId) != null) {
			return fdidCodeMap.get(fdId);
		} else {
			bycleInfoShort = getPersistence().getBycleInfoShort(fdId);
			if (bycleInfoShort != null) {
				fdidCodeMap.put(fdId, bycleInfoShort);
			}
		}
		return bycleInfoShort;
	}

	@Override
	public TrackBycleShort getTrackBycleShort(String fdId) {
		return getPersistence().getTrackBycleShort(fdId);
	}

	@Override
	public void addBycleAlarmModel(BycleAlarmModel bycleAlarmModel) {
		getPersistence().addBycleAlarm(bycleAlarmModel);

	}

	@Override
	public void batchinsertBycleAlarmList(List<BycleAlarmModel> alarmlist) {
		getPersistence().batchinsertBycleAlarmList(alarmlist);

	}

	@Override
	public void batchinsertTrackedBycleAlarmList(List<BycleAlarmModel> alarmlist) {
		getPersistence().batchinsertBycleTrackedRecordList(alarmlist);

	}

	@Override
	public List<TrackBycleShort> getTrackBycleShortByRownum(int start, int rowspan) {
		return getPersistence().getTrackBycleShortByRownum(start, rowspan);
	}

	@Override
	public TrackedBycle getTrackedBycleByRuleId(String ruleId) {
		return getPersistence().getTrackedBycleByRuleId(ruleId);
	}

	public void addBycleTrackedRecord(BycleAlarmModel bycleAlarmModel) {
		getPersistence().addBycleTrackedRecord(bycleAlarmModel);
	}

	public BycleAlarmModel findBycleHandleAlarm(String fdId, String startTime, String endTime) {
		return getPersistence().findBycleHandleAlarm(fdId, startTime, endTime);
	}

	public boolean addBycleHandleAlarm(BycleAlarmModel model) {
		return getPersistence().addBycleHandleAlarm(model);
	}

	public void deletecudbnotifyByType(int type) {
		getPersistence().deletecudbnotifyByType(type);
	}

	public void deleteDbNotify(String key, int type) {
		getPersistence().deleteDbNotify(key, type);
	}

	public List<SoapDbNotify> queryTrackeddbNotify(int type) {
		return getPersistence().queryTrackeddbNotify(type);
	}

	public BycleAlarmModel getbycleTrackedHandle(BycleAlarmModel model) {
		return getPersistence().getbycleTrackedHandle(model);
	}

	public boolean addbycleStationTracked(BycleAlarmModel model) {
		return getPersistence().addbycleStationTracked(model);
	}

	public TmsSms findTmsSms(TmsSms tms) {
		return getPersistence().findTmsSms(tms);
	}

	public boolean addBycleBlack(BycleBlack b, BycleLostRecord lost) {
		return getPersistence().addBycleBlack(b, lost);
	}

	public boolean addToTmsSms(TmsSms tms, boolean update) {
		TmsSms mm = null;
		String lastTime = "";
		update = false;
		if (tmsDatemap.get(tms.getFdid()) != null) {
			lastTime = tmsDatemap.get(tms.getFdid());
			update = true;
		} else {
			mm = getPersistence().findTmsSms(tms);
			if (mm != null) {
				lastTime = DateUtil.toString(mm.getCreateDate());
				update = true;
			}
		}
		if (lastTime == null || lastTime.equals("")) {
			getPersistence().addToTmsSms(tms, update);
			tmsDatemap.put(tms.getFdid(), DateUtil.toString(tms.getCreateDate()).replaceAll("-", ""));

		} else {
			if (Long.parseLong(DateUtil.toString(tms.getCreateDate()).replaceAll("-", "")) > Long.parseLong(DateUtil.toString(tms.getCreateDate()).replaceAll("-", ""))) {
				if (mm != null && mm.getStatus() == 0) {

				} else {
					getPersistence().addToTmsSms(tms, update);
					tmsDatemap.put(tms.getFdid(), DateUtil.toString(tms.getCreateDate()).replaceAll("-", ""));
				}
			}

		}
		return true;

	}

	public boolean findBycleWhite(BycleAlarmModel model) {
		return getPersistence().findBycleWhite(model);
	}

	public boolean addbycleAlarmPreDeal(BycleAlarmModel model) {
		return getPersistence().addbycleAlarmPreDeal(model);
	}

}
