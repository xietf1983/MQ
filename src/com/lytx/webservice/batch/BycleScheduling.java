package com.lytx.webservice.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BycleScheduling {
	private static Log iLog = LogFactory.getLog(BycleScheduling.class.getName());

	public void afterPropertiesSet() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {

		}
		new Timer().schedule(new BycleMainThread(), 1000);
	}
}
