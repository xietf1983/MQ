package com.liveyc.mq;

import java.nio.ByteBuffer;
import java.util.HashMap;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;

import com.liveyc.rabbitmq.DealDataUtil;

public class Test2 {

	/** * @param args * @throws SQLException * @throws IOException */
	public static void main(String[] args) throws Exception {
		Producer producer = new Producer("ExceptionAlarmRabbitMQ");
		ByteBuffer buffer = ByteBuffer.allocate(13);
		buffer.putInt(2);
		buffer.putInt(1234567890);
		buffer.put((byte) (12));
		buffer.put((byte) (12));
		buffer.put((byte) (12));
		buffer.put((byte) (12));
		buffer.put((byte) 125);
		producer.sendMessage(buffer.array());
	}

}
