package com.liveyc.mq;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class EndPoint {

	protected Channel channel;
	protected Connection connection;
	protected String endPointName;

	public EndPoint(String endpointName) {
		try {
			this.endPointName = endpointName;
			// Create a connection factory
			ConnectionFactory factory = new ConnectionFactory();
			// hostname of your rabbitmq server
			factory.setHost("10.123.30.74");
			factory.setPort(5672);
			factory.setUsername("U8bicycle");
			factory.setPassword("Jdkj@2016");
			factory.setAutomaticRecoveryEnabled(true);
			factory.setVirtualHost("/");
			factory.setNetworkRecoveryInterval(6000);
			//factory.setExceptionHandler(exceptionHandler)
			connection = factory.newConnection();
			channel = connection.createChannel();
		} catch (Exception ex) {

		}
	}

	public void close() throws Exception {
		this.channel.close();
		this.connection.close();
	}
}
