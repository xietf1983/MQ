package com.liveyc.mq;

import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;

public class Producer extends EndPoint {
	public Producer(String endPointName) throws Exception {
		super(endPointName);
	}

	public void sendMessage(byte[] object) throws Exception {
		// channel.basicPublish("", endPointName, null,
		// SerializationUtils.serialize(object));
		// channel.
		//channel.queueDeclare(endPointName, false, false, true, null);
		channel.basicPublish("", endPointName, null, object);
	}
}
