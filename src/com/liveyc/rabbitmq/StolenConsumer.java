package com.liveyc.rabbitmq;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.liveyc.rabbitmq.AbnormalConsumer.QueueConsumer;
import com.lytx.webservice.batch.StolenMessageTask;
import com.lytx.webservice.electrombile.service.ElectrombileServiceUtil;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

public class StolenConsumer {
	private ConnectionFactory factory;
	private String queue;
	private Channel channel;
	private static Logger iLog = Logger.getLogger(StolenConsumer.class);

	// ExecutorService pool = Executors.newFixedThreadPool(2);
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	private boolean runing = false;
	private Connection connection;

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public StolenConsumer(ConnectionFactory factory) {
		this.factory = factory;
	}

	public void afterPropertiesSet() {
		iLog.error("StolenConsumer -afterPropertiesSet -start");
		try {
			Thread.sleep(4000);
			while (ElectrombileServiceUtil.getService() == null) {
				Thread.sleep(1000);

			}
		} catch (Exception e) {
		}
		Thread consumerThread = new Thread(new QueueConsumer());

		consumerThread.start();

		iLog.error("StolenConsumer-consumerThread -start");

	}

	class QueueConsumer implements Runnable, Consumer {

		@Override
		public void handleConsumeOk(String consumerTag) {
			// TODO Auto-generated method stub

		}

		@Override
		public void handleCancelOk(String consumerTag) {
			// TODO Auto-generated method stub

		}

		@Override
		public void handleCancel(String consumerTag) throws IOException {
			// TODO Auto-generated method stub

		}

		@Override
		public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
			// TODO Auto-generated method stub

		}

		@Override
		public void handleRecoverOk(String consumerTag) {
			// TODO Auto-generated method stub

		}

		@Override
		public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body) throws IOException {
			// pool.
			// iLog.error("收到一偷盗信息"+org.apache.commons.codec.binary.Hex.encodeHexString(body));
			// iLog.error("收到一偷盗信息"+new String(body));
			/*
			 * try {
			 * //StolenMessageTask.getInstance().putStolenMessageEvent(body); }
			 * catch (Exception ex) { //DealDataUtil.dealStolen(body); }
			 */
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
				// DealDataUtil.dealStolen(body);
			}
			return;
		}

		@Override
		public void run() {

			while (true) {
				if (!runing) {
					try { // start consuming messages. Auto acknowledge
							// messages.
						connection = factory.newConnection();
						channel = connection.createChannel();
						// channel.queueDeclare(getQueue(), false, false, false,
						// null);
						connection.addShutdownListener(new ShutdownListener() {
							public void shutdownCompleted(ShutdownSignalException cause) {
								iLog.error("连接断开--StolenConsumer,重试");
								runing = false;
								
							}
						});
						channel.basicConsume(getQueue(), true, this);
						runing = true;
						iLog.error("连接成功");
						Thread.sleep(6000);
					} catch (Exception e) {
						runing = false;
						iLog.error(e.toString());
						e.printStackTrace();
						if (getChannel() != null) {
							try {
								channel.close();
							} catch (Exception ex) {
							}
						}
						if (getConnection() != null) {
							try {
								getConnection().close();
							} catch (Exception ex) {
							}
						}
						try {
							Thread.sleep(6000);
						} catch (Exception ex) {

						}
					}
				} else {
					try {
						Thread.sleep(6000);
					} catch (Exception ex) {
						iLog.error(ex.toString());

					}
				}

			}

		}
	}

}
