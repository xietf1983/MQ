package com.liveyc.rabbitmq;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import com.lytx.webservice.batch.AbnormalMessageTask;
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

public class AbnormalConsumer {
	private ConnectionFactory factory;
	private String queue;
	private Channel channel;
	private static long a = 0;
	// ExecutorService pool = Executors.newFixedThreadPool(20);
	private static Logger iLog = Logger.getLogger(AbnormalConsumer.class);

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

	public AbnormalConsumer(ConnectionFactory factory) {
		this.factory = factory;
	}

	public void afterPropertiesSet() {
		iLog.error("AbnormalConsumer -afterPropertiesSet -start");
		try {
			Thread.sleep(2000);
			while (ElectrombileServiceUtil.getService() == null) {
				Thread.sleep(1000);

			}
		} catch (Exception e) {
		}
		Thread consumerThread = new Thread(new QueueConsumer());

		consumerThread.start();

		iLog.error("AbnormalConsumer-consumerThread -start");
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
			// iLog.error("收到一异常信息" +
			// org.apache.commons.codec.binary.Hex.encodeHexString(body));*/

			try {
				//Thread.sleep(100);
			} catch (Exception ex) {
				// DealDataUtil.dealAbnormal(body);
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
								iLog.error("连接断开--AbnormalConsumer,重试");
								runing = false;
								
							}
						});
						channel.basicConsume(getQueue(), true, this);
						runing = true;
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
						Thread.sleep(60000);
					} catch (Exception ex) {
						iLog.error(ex.toString());

					}
				}

			}

		}
	}

}
