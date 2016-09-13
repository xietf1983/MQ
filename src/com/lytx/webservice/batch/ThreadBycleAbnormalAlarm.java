package com.lytx.webservice.batch;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.service.ElectrombileServiceUtil;
import com.lytx.webservice.electrombile.service.persistence.ElectrombilePersistence;

/**
 * 预警信息插入
 * 
 * @author dell
 * 
 */
public class ThreadBycleAbnormalAlarm extends Thread {
	private List<BycleAlarmModel> alarmlist;
	public static AtomicInteger numberThread = new AtomicInteger(0);
	private static Logger iLog = Logger.getLogger(ThreadBycleAbnormalAlarm.class);

	public ThreadBycleAbnormalAlarm(List<BycleAlarmModel> _list) {
		alarmlist = _list;
	}

	public void run() {
		numberThread.getAndIncrement();
		try {

			ElectrombileServiceUtil.batchinsertTrackedBycleAlarmList(alarmlist);
		} catch (Exception ex) {
			iLog.error("批量插入失败" + ex.toString() + "逐个插入");
			for (BycleAlarmModel b : alarmlist) {
				ElectrombileServiceUtil.getService().addBycleTrackedRecord(b);
			}

		}
		numberThread.getAndDecrement();
	}

}
