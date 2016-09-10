package com.lytx.webservice.scheduling;

import java.util.List;

import org.apache.log4j.Logger;

import com.lytx.webservice.dbnotify.model.DbnotifyModel;
import com.lytx.webservice.dbnotify.service.DbnotifyServiceUtil;
import com.lytx.webservice.electrombile.model.TrackBycleShort;
import com.lytx.webservice.electrombile.util.TrackedBycleUntil;
import com.lytx.webservice.util.DateUtil;

/**
 * 
 * @author Administrator
 * 
 */
public class DbNotifyChangeJob extends Thread {
	private static boolean init = true;
	private static boolean run = false;
	static private Logger iLog = Logger.getLogger(DbNotifyChangeJob.class);

	public void run() {
		while (true) {
			try {
				List<DbnotifyModel> list = DbnotifyServiceUtil.getService().getDbnotifyModelList(80);
				if (list != null && list.size() > 0) {
					for (DbnotifyModel d : list) {
						if (d.getType() == 1) {
							TrackedBycleUntil.addPlateNo(new TrackBycleShort(d.getCnt(), d.getKey()));
							DbnotifyServiceUtil.getService().deleteDbnotifyModel(d.getKey(), 80);
						} else if (d.getType() == 3) {
							TrackedBycleUntil.remove(new TrackBycleShort(d.getCnt(), d.getKey()));
							DbnotifyServiceUtil.getService().deleteDbnotifyModel(d.getKey(), 80);
						} else {
							TrackedBycleUntil.remove(new TrackBycleShort(d.getCnt(), d.getKey()));
							TrackedBycleUntil.addPlateNo(new TrackBycleShort(d.getCnt(), d.getKey()));
							DbnotifyServiceUtil.getService().deleteDbnotifyModel(d.getKey(), 80);
						}
					}
				}
			} catch (Exception ex) {
				run = false;
				iLog.error("DbNotifyChangeJob runing" + ex.toString());
				ex.printStackTrace();
			}
			try {
				Thread.sleep(DateUtil.SECOND * 5);

			} catch (Exception ex) {

			}
		}

	}
}
