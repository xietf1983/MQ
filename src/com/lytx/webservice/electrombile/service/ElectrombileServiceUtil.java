package com.lytx.webservice.electrombile.service;

import java.util.List;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.model.BycleInfoShort;
import com.lytx.webservice.electrombile.model.BycleStationModel;

public class ElectrombileServiceUtil {
	private static ElectrombileService _service;

	public static ElectrombileService getService() {
		if (_service == null) {
			throw new RuntimeException("ElectrombileService is not set");
		}
		return _service;
	}

	public void setService(ElectrombileService service) {
		_service = service;
	}

	public static BycleStationModel getBycleStationModel(String code) {
		return getService().getBycleStationModel(code);

	}

	public static BycleInfoShort getBycleInfoShort(String fdId) {
		return getService().getBycleInfoShort(fdId);

	}

	public static void addBycleAlarmModel(BycleAlarmModel bycleAlarmModel) {
		getService().addBycleAlarmModel(bycleAlarmModel);

	}

	public static void batchinsertBycleAlarmList(List<BycleAlarmModel> alarmlist) {
		getService().batchinsertBycleAlarmList(alarmlist);

	}

	public static void batchinsertTrackedBycleAlarmList(List<BycleAlarmModel> alarmlist) {
		getService().batchinsertTrackedBycleAlarmList(alarmlist);

	}

}
