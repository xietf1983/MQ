package com.lytx.webservice.batch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.service.ElectrombileServiceUtil;

import com.lytx.webservice.util.DateUtil;

public class BycleMainThread extends TimerTask {
	public static boolean ret = false;
	private static Logger iLog = Logger.getLogger(BycleMainThread.class);
	public static long runtimes = 0;
	private static String entablespace = "tablespace";
	private static String enextend = "extend";
	private static String zhextend = "扩展";
	private static String zhtablespace = "表空间";
	public static boolean ifull = false;
	public static long runTimes;
	public static Date date = new Date();
	private BycleAlarmModel soap = null;

	public void run() {
		while (true) {
			try {
				date = new Date();
				if (runtimes < Long.MAX_VALUE) {
					runtimes = runtimes + 1;
				} else {
					runtimes = 1;
				}
				if (runtimes % 5000 == 0 || ifull) {
					try {
						if (soap == null) {
							EventQueue e = BycleTask.getInstance().getBycleQue();
							soap = (BycleAlarmModel) e.dequeue();

						}
						if (soap != null) {
							ElectrombileServiceUtil.addBycleAlarmModel(soap);

						}
						ifull = false;
						soap = null;
					} catch (Exception e) {
						// 说明表空间满
						iLog.error(e.toString());
						if ((e.toString().indexOf(zhextend) > -1 && e.toString().indexOf(zhtablespace) > -1) || (e.toString().indexOf(entablespace) > -1 && e.toString().indexOf(enextend) > -1)) {
							ifull = true;
						} else {
							ifull = false;
							soap = null;
						}

					}
				}
				Random random = new Random();
				if (random.nextInt(100) == 50) {
					iLog.error("MainThread IS RUNNING");
				}
				if (!ifull) {
					vehicleRun();
				} else {
					EventQueue e = BycleTask.getInstance().getBycleQue();
					soap = (BycleAlarmModel) e.dequeue();
					if (soap != null) {
						ElectrombileServiceUtil.addBycleAlarmModel(soap);
						ifull = false;
						soap = null;
					}
				}
				Thread.sleep(100);
			} catch (Exception e) {
				try {
					Thread.sleep(1000);
				} catch (Exception exx) {

				}
				iLog.error(e.toString() + "WirelessMainThread  ERROR");
			}

		}
	}

	public void vehicleRun() {
		Random random = new Random();
		EventQueue e = BycleTask.getInstance().getBycleQue();
		int maxLength = 500;
		int size = e.size();
		if (size < 500) {
			maxLength = size;
		}
		if (random.nextInt(100) == 50) {
			iLog.error("当前过电瓶车车数量" + size + "----ThreadBycleAlarm" + ThreadBycleAlarm.numberThread.get());
		}
		List<BycleAlarmModel> list = new ArrayList();
		for (int j = 0; j < maxLength; j++) {
			list.add((BycleAlarmModel) e.dequeue());
		}

		if (list.size() > 0) {
			new ThreadBycleAlarm(list).start();
		}

	}
}
