package com.lytx.analysis.scheduling;

import java.util.Date;
import java.util.List;
import java.util.Timer;

import org.apache.log4j.Logger;

import com.lytx.analysis.service.BycleAlarmRuleServiceUtil;
import com.lytx.analysis.service.BycleAlarmRuleUtil;
import com.lytx.webservice.electrombile.model.BycleAlarmModel;

public class AnalysisScheduling {
	private static Logger iLog = Logger.getLogger(AnalysisScheduling.class);

	public void afterPropertiesSet() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {

		}
		Thread consumerThread = new Thread(new AnalysisSchedulingThread());
		consumerThread.start();
	}

	class AnalysisSchedulingThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				Date now = BycleAlarmRuleServiceUtil.getDbDateTime();
				try {
					List<BycleAlarmModel> list = BycleAlarmRuleServiceUtil.getBycleAlarmPreDeal();
					if (list != null && list.size() > 0) {
						for (BycleAlarmModel by : list) {
							BycleAlarmRuleUtil.match(by, now);
						}
					}
					Thread.sleep(5000);
				} catch (Exception ex) {
					iLog.error(ex.toString());
					try {
						Thread.sleep(5000);
					} catch (Exception exx) {
					}
				}
			}
		}
	}

}
