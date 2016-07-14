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

public class AbnormalMainThread extends TimerTask {
	public static boolean ret = false;
	private static Logger iLog = Logger.getLogger(TrailMainThread.class);
	public static long runtimes = 0;
	private static String entablespace = "tablespace";
	private static String enextend = "extend";
	private static String zhextend = "À©Õ¹";
	private static String zhtablespace = "±í¿Õ¼ä";
	public static boolean ifull = false;
	public static long runTimes;
	public static Date date = new Date();

	public void run() {
		while (true) {
			try {
				vehicleRun();
				Thread.sleep(500);
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
		EventQueue e = AbnormalMessageTask.getInstance().getAbnormalMessageQue();
		int maxLength = 500;
		int size = e.size();
		if (size < 500) {
			maxLength = size;
		}
		List<byte[]> list = new ArrayList();
		for (int j = 0; j < maxLength; j++) {
			list.add((byte[]) e.dequeue());
		}

		if (list.size() > 0) {
			new ThreadAbnormalMessage(list).start();
		}

	}
}
