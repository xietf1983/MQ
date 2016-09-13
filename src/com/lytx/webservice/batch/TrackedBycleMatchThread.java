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
					if (!ElectrombileServiceUtil.getService().findBycleWhite(b)) {
						TrackBycleShort ts = ElectrombileServiceUtil.getService().getTrackBycleShort(b.getFdId());
						if (ts != null) {
							// Ô¤¾¯¹ýÀ´µÄ
							b.setStatus("0");
							if (ts.getType() == 0) {
								continue;
							} else {
								b.setType(1);
								b.setAlarmId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.bycleseqName));
								b.setRuleId(ts.getRuleId());
								b.setCaseId(ts.getCaseId());
								// b.setCaseId(list.get(0).getCaseId());
								ElectrombileServiceUtil.getService().addBycleTrackedRecord(b);
								ElectrombileServiceUtil.getService().addBycleHandleAlarm(b);
							}
						}
					}
				}
			}

		} catch (Exception ex) {

		}

	}
}
