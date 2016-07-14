package com.springmq;

import java.nio.ByteBuffer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpringAmqp {

	public static void main(String[] args) throws Exception {

		AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { "rabbitmq-spring.xml", "base-spring.xml" });
		//.getBeanDefinitionNames();
		RabbitTemplate template = (RabbitTemplate) ctx.getBean("rabbitTemplate");

		long start = System.currentTimeMillis();
		ByteBuffer buffer = ByteBuffer.allocate(13);
		buffer.putInt(2);
		buffer.putInt(6000);
		buffer.put((byte) (12));
		buffer.put((byte) (12));
		buffer.put((byte) (12));
		buffer.put((byte) (12));
		buffer.put((byte) 125);
		//template.setRoutingKey("ExceptionAlarmRabbitMQ");
		
		while (true) {
			//template.convertAndSend("foo", "1234");
			//template.send(exchange, routingKey, message)
			//template.setQueue("alarm");
			//template.s
			//template.
			//template.send("ExceptionAlarmRabbitMQ", new Message(buffer.array(), MessagePropertiesBuilder.newInstance().setContentType(MessageProperties.CONTENT_TYPE_BYTES).build()));
			//Thread.sleep(10);
		}
		//long end = System.currentTimeMillis();
		//System.out.println(end - start);

	}
}
