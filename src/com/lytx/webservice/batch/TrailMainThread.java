package com.lytx.webservice.batch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.service.ElectrombileServiceUtil;

import com.lytx.webservice.util.DateUtil;

public class TrailMainThread extends TimerTask {
	public static boolean ret = false;
	private static Logger iLog = Logger.getLogger(TrailMainThread.class);
	public static long runtimes = 0;
	private static String entablespace = "tablespace";
	private static String enextend = "extend";
	private static String zhextend = "��չ";
	private static String zhtablespace = "���ռ�";
	public static boolean ifull = false;
	public static long runTimes;
	public static Date date = new Date();
	public static ThreadPoolExecutor sendMsgthreadPool = new ThreadPoolExecutor(50, 80, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20), new ThreadPoolExecutor.CallerRunsPolicy());
	public void run() {
		while (true) {
			try {
				vehicleRun();
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
		EventQueue e = TrailMessageTask.getInstance().getTrailMessageQue();
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
			sendMsgthreadPool.execute(new ThreadTrail(list));
		}
		

	}
}
