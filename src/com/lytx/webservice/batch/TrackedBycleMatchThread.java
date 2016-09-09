package com.lytx.webservice.batch;

import java.util.List;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.model.TrackBycleShort;
import com.lytx.webservice.electrombile.service.ElectrombileServiceUtil;
import com.lytx.webservice.electrombile.util.TrackedBycleUntil;
import com.lytx.webservice.sequence.service.SequenceGeneratorServiceUtil;

public class TrackedBycleMatchThread extends Thread {
	private List<BycleAlarmModel> alarmlist;

	public TrackedBycleMatchThread(List<BycleAlarmModel> _list) {
		alarmlist = _list;
	}

	public void run() {
		try {
			for (BycleAlarmModel b : alarmlist) {
				List<TrackBycleShort> list = TrackedBycleUntil.match(b);
				if (list != null && list.size() > 0) {
					b.setType(1);
					b.setAlarmId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.bycleseqName));
					b.setRuleId(list.get(0).getRuleId());
					b.setCaseId(list.get(0).getCaseId());
					ElectrombileServiceUtil.getService().addBycleHandleAlarm(b);
					BycleAlarmModel model = ElectrombileServiceUtil.getService().getbycleTrackedHandle(b);
					if (model != null && model.getActNo() != null && !model.getActNo().equals("") && b.getAreaId() != null && !b.getAreaId().equals("")) {
						b.setActNo(model.getActNo());
						ElectrombileServiceUtil.getService().addbycleStationTracked(b);
					}
				}
			}

		} catch (Exception ex) {

		}

	}
}
