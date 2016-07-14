package com.lytx.webservice.batch;

import java.util.Timer;

public class AbnormalScheduling {
	public void afterPropertiesSet() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {

		}
		new Timer().schedule(new AbnormalMainThread(), 100);
	}
	
}
