package com.lytx.webservice.scheduling;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lytx.webservice.dbnotify.service.DbnotifyServiceUtil;
import com.lytx.webservice.electrombile.model.TrackBycleShort;
import com.lytx.webservice.electrombile.service.ElectrombileServiceUtil;
import com.lytx.webservice.electrombile.util.TrackedBycleUntil;
import com.lytx.webservice.util.DateUtil;

public class TrackedBycleJob extends Thread {
	private static final int intexnal = 5000; // Ĭ�ϵĲ���
	private static Log iLog = LogFactory.getLog(TrackedBycleJob.class.getName());
	public ThreadPoolExecutor trackedMsgthreadPool = new ThreadPoolExecutor(2, 5, 1, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5), new ThreadPoolExecutor.CallerRunsPolicy());

	public void run() {
		// ��ʼ�����ؼ�¼
		while (true) {
			try {
				DbnotifyServiceUtil.getService().deleteDbnotifyModel(null, 80);
				TrackedBycleUntil.clear();
				iLog.error("���¼���");
				long start = System.currentTimeMillis();
				int startRow = 0;
				int endrow = startRow + intexnal;
				boolean bcon = true;
				int total = 0;
				while (bcon) {
					List<TrackBycleShort> list = ElectrombileServiceUtil.getService().getTrackBycleShortByRownum(startRow, intexnal);
					total = total + list.size();
					if (list != null && list.size() == intexnal) {
						bcon = true;
						startRow = endrow;
						endrow = startRow + intexnal;
						iLog.error("�Ѿ����غ���������" + total);

					} else {
						bcon = false;
					}
					if (list != null && list.size() > 0) {
						trackedMsgthreadPool.execute(new ThreadPoolTask(list));
					}

				}
				long end = System.currentTimeMillis();
				iLog.error("�ڳ����ؼ���ִ��ʱ��:" + (end - start));
				// System.out.println("ִ��ʱ��:" + (end - start));

			} catch (Exception ex) {
				iLog.error(ex.toString());
				ex.printStackTrace();
			}
			try {
				Thread.sleep(DateUtil.DAY);// ÿ��ִ��һ��
			} catch (Exception ex) {
				iLog.error(ex.toString());
				ex.printStackTrace();
			}
		}
	}

	public class ThreadPoolTask implements Runnable, Serializable {
		private List<TrackBycleShort> list;

		public ThreadPoolTask(List<TrackBycleShort> list) {
			this.list = list;
		}

		@Override
		public void run() {
			if (list != null && list.size() > 0) {
				for (TrackBycleShort alarmShort : list) {
					// �������
					TrackedBycleUntil.addPlateNo(alarmShort);
				}
			}

		}

	}
}
