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
import com.lytx.webservice.electrombile.model.TrackBycleShort;
import com.lytx.webservice.electrombile.service.ElectrombileServiceUtil;
import com.lytx.webservice.electrombile.util.TrackedBycleUntil;

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
	public static ThreadPoolExecutor sendMsgthreadPool = new ThreadPoolExecutor(50, 80, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20), new ThreadPoolExecutor.CallerRunsPolicy());
	public static ThreadPoolExecutor abnormalMsgthreadPool = new ThreadPoolExecutor(10, 20, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20), new ThreadPoolExecutor.CallerRunsPolicy());
	public static ThreadPoolExecutor trackdedMsgthreadPool = new ThreadPoolExecutor(20, 20, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20), new ThreadPoolExecutor.CallerRunsPolicy());

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
		List<BycleAlarmModel> abnoramalList = new ArrayList();
		for (int j = 0; j < maxLength; j++) {
			// 设防移位 || 断电移位。增加到预警表
			BycleAlarmModel b = (BycleAlarmModel) e.dequeue();
			if ((b.getFdNoElecTag() == 1 && b.getFdMoveTag() == 1) || (b.getFdLockTag() == 1 && b.getFdMoveTag() == 1) || (b.getFdMoveTag() == 1 && b.getFdNoElecTag() == 1)) {
				// 断电移动与锁定移动时，增加到预警
				List<TrackBycleShort> list2 = TrackedBycleUntil.match(b);
				if (list2 == null || list2.size() == 0 || list2.get(0).getType() == 0) {
					// 去掉设防移已经插入的数据
					if (b.getFdMoveTag() == 1 && b.getFdLockTag() == 1) {

					} else {
						b.setType(0);
						abnoramalList.add(b);
					}

				}

			}
			list.add(b);
		}

		if (list.size() > 0) {
			// 轨迹数据
			sendMsgthreadPool.execute(new ThreadBycleAlarm(list));
			// new ThreadBycleAlarm(list).start();
		}
		if (abnoramalList != null && abnoramalList.size() > 0) {
			// 异常数据
			abnormalMsgthreadPool.execute(new ThreadBycleAbnormalAlarm(abnoramalList));
		}
		if (list != null && list.size() > 0) {
			// 黑名单
			trackdedMsgthreadPool.execute(new TrackedBycleMatchThread(list));
		}

	}
}
