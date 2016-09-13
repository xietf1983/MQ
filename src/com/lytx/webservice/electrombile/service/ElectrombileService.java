package com.lytx.webservice.electrombile.service;

import java.util.HashMap;
import java.util.List;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.model.BycleBlack;
import com.lytx.webservice.electrombile.model.BycleInfoShort;
import com.lytx.webservice.electrombile.model.BycleLostRecord;
import com.lytx.webservice.electrombile.model.BycleStationModel;
import com.lytx.webservice.electrombile.model.SoapDbNotify;
import com.lytx.webservice.electrombile.model.TmsSms;
import com.lytx.webservice.electrombile.model.TrackBycleShort;
import com.lytx.webservice.electrombile.model.TrackedBycle;
import com.lytx.webservice.sequence.service.SequenceGeneratorServiceUtil;

public interface ElectrombileService {
	public BycleStationModel getBycleStationModel(String code);

	public BycleInfoShort getBycleInfoShort(String fdId);

	public void addBycleAlarmModel(BycleAlarmModel bycleAlarmModel);

	public void batchinsertBycleAlarmList(List<BycleAlarmModel> alarmlist);

	public void batchinsertTrackedBycleAlarmList(List<BycleAlarmModel> alarmlist);

	public void addBycleTrackedRecord(BycleAlarmModel bycleAlarmModel);

	public List<TrackBycleShort> getTrackBycleShortByRownum(int start, int rowspan);

	public TrackedBycle getTrackedBycleByRuleId(String ruleId);

	public BycleAlarmModel findBycleHandleAlarm(String fdId, String startTime, String endTime);

	public boolean addBycleHandleAlarm(BycleAlarmModel model);

	public void deletecudbnotifyByType(int type);

	public void deleteDbNotify(String key, int type);

	public List<SoapDbNotify> queryTrackeddbNotify(int type);

	public TrackBycleShort getTrackBycleShort(String fdId);

	public BycleAlarmModel getbycleTrackedHandle(BycleAlarmModel model);

	public boolean addbycleStationTracked(BycleAlarmModel model);

	public boolean addToTmsSms(TmsSms tms,boolean update);

	public TmsSms findTmsSms(BycleAlarmModel m);

	public boolean addBycleBlack(BycleBlack b, BycleLostRecord lost);

	public String getCaseIdNext(String areaid);
	
	public boolean findBycleWhite(BycleAlarmModel model);
}
