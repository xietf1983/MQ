package com.lytx.webservice.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StolenScheduling {
	private static Log iLog = LogFactory.getLog(StolenScheduling.class.getName());

	public void afterPropertiesSet() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {

		}
		new Timer().schedule(new StolenMainThread(), 100);
	}
}
