package com.liveyc.rabbitmq.publish;

import java.nio.ByteBuffer;
import java.util.List;

import org.apache.log4j.Logger;

import com.liveyc.rabbitmq.AbnormalConsumer;
import com.liveyc.rabbitmq.DealDataUtil;
import com.lytx.webservice.dbnotify.model.DbnotifyModel;
import com.lytx.webservice.dbnotify.service.DbnotifyServiceUtil;
import com.lytx.webservice.electrombile.service.ElectrombileServiceUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class BlacklistPublish {
	private static ConnectionFactory factory;
	private String queue;
	private String exchange;
	private static Logger iLog = Logger.getLogger(BlacklistPublish.class);
	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public static ConnectionFactory getFactory() {
		return factory;
	}

	public void setFactory(ConnectionFactory factory) {
		this.factory = factory;
	}

	private Channel channel;

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

	public BlacklistPublish(ConnectionFactory factory) {
		this.factory = factory;
	}

	public void afterPropertiesSet() {
		try {
			while (DbnotifyServiceUtil.getService() == null) {
				Thread.sleep(1000);

			}
		} catch (Exception e) {
		}
		Thread consumerThread = new Thread(new QueueBlacklist());
		consumerThread.start();
	}

	class QueueBlacklist implements Runnable {

		@Override
		public void run() {
			while (true) {
				if (channel == null) {
					try { // start consuming messages. Auto acknowledge
							// messages.
						connection = factory.newConnection();
						channel = connection.createChannel();

						channel.exchangeDeclare(getExchange(), "fanout", true);
						runing = true;
						// Thread.sleep(6000);
					} catch (Exception e) {
						// e.printStackTrace();
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
						runing = false;

					}
				}
				try {
					List<DbnotifyModel> list = DbnotifyServiceUtil.getService().getDbnotifyModelList(80);
					if (list != null && list.size() > 0) {
						for (DbnotifyModel d : list) {
							long value = 0;
							try {
								value = (Long.parseLong(d.getCnt()));
							} catch (Exception e) {

							}
							int sendvalue = 1;
							if (d.getType()!=1) {
								sendvalue = 2;
							}
							if (value > 0) {
								ByteBuffer buffer = ByteBuffer.allocate(5);
								buffer.put((byte) sendvalue);
								buffer.put(DealDataUtil.intToByteArray(value));
								try {
									iLog.error("������������Ϣ"+org.apache.commons.codec.binary.Hex.encodeHexString(buffer.array()));
									channel.basicPublish(getExchange(), getQueue(), MessageProperties.MINIMAL_PERSISTENT_BASIC, buffer.array());
									DbnotifyServiceUtil.getService().deleteDbnotifyModel(d.getKey(), 80);
								} catch (Exception e) {
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
								}
							}

						}
					}

				} catch (Exception ex) {
					int a = 0;
				}
				try {
					Thread.sleep(6000);
				} catch (Exception ex) {

				}

			}

		}
	}
}
