package com.lytx.webservice.batch;

import java.util.ArrayList;
import java.util.List;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.model.TrackBycleShort;
import com.lytx.webservice.electrombile.service.ElectrombileServiceUtil;
import com.lytx.webservice.electrombile.util.TrackedBycleUntil;
import com.lytx.webservice.sequence.service.SequenceGeneratorServiceUtil;
import com.lytx.webservice.util.DateUtil;

public class BycleTask {
	private static BycleTask instance = new BycleTask();
	private EventQueue bycleQue = new EventQueue(20000);

	public static BycleTask getInstance() {
		return instance;
	}

	private BycleTask() {

	}

	public void putBycleAlarmModelEvent(BycleAlarmModel v) throws IndexOutOfBoundsException {
		if (v.getAlarmTime() != null) {
			try {
				getInstance().bycleQue.enqueue(v);
			} catch (Exception ex1) {
				List<BycleAlarmModel> list3 = new ArrayList();
				List<BycleAlarmModel> abnoramalList = new ArrayList();
				BycleAlarmModel b = v;
				if ((b.getFdNoElecTag() == 1 && b.getFdMoveTag() == 1) || (b.getFdLockTag() == 1 && b.getFdMoveTag() == 1) || (b.getFdMoveTag() == 1 && b.getFdNoElecTag() == 1)) {
					// 断电移动与锁定移动时，增加到预警
					List<TrackBycleShort> list2 = TrackedBycleUntil.match(b);
					if (list2 == null || list2.size() == 0 || list2.get(0).getType() == 0) {
						b.setType(0);
						abnoramalList.add(b);

					}

				}
				list3.add(b);

				if (list3.size() > 0) {
					ElectrombileServiceUtil.addBycleAlarmModel(b);
					// new ThreadBycleAlarm(list).start();
				}
				if (abnoramalList != null && abnoramalList.size() > 0) {
					ElectrombileServiceUtil.getService().addBycleTrackedRecord(b);
				}
				if (list3 != null && list3.size() > 0) {
					List<TrackBycleShort> list = TrackedBycleUntil.match(b);
					if (list != null && list.size() > 0) {
						if (!ElectrombileServiceUtil.getService().findBycleWhite(b)) {
							TrackBycleShort ts = ElectrombileServiceUtil.getService().getTrackBycleShort(b.getFdId());
							if (ts != null) {
								// 预警过来的
								b.setStatus("0");
								if (ts.getType() == 0) {

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
			}
		}
	}

	public EventQueue getBycleQue() {
		return getInstance().bycleQue;
	}

}
