package com.liveyc.mq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.liveyc.rabbitmq.DealDataUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Test {

	public Test() throws Exception {
		QueueConsumer consumer = new QueueConsumer("queue");
		Thread consumerThread = new Thread(consumer);
		consumerThread.start();

	}

	/** * @param args * @throws SQLException * @throws IOException */
	public static void main(String[] args) throws Exception {
		Long a =59084070l;
		byte [] aa =DealDataUtil.intToByteArray(a);
		//org.apache.commons.codec.binary.Hex.encodeHexString(aa)
		System.out.print(org.apache.commons.codec.binary.Hex.encodeHexString(aa));
		//org.apache.commons.codec.binary.Hex.encodeHexString(a.toHexString(16))
		
	}

	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		for (int i = begin; i < begin + count; i++)
			bs[i - begin] = src[i];
		return bs;
	}

	public static int bytes2Short(byte[] bytes) {
		int num = bytes[1] & 0xFF;
		num |= ((bytes[0] << 8) & 0xFF00);
		return num;
	}

	public static void dealmsg(String msg) {
		byte[] data = hexStr2Bytes(msg);
		com.liveyc.rabbitmq.DealDataUtil.dealAbnormal(data);
	}

	public static byte[] hex2byte(String hex) {
		String digital = "0123456789ABCDEF";
		char[] hex2char = hex.toCharArray();
		byte[] bytes = new byte[hex.length() / 2];
		int temp;
		for (int i = 0; i < bytes.length; i++) {
			temp = digital.indexOf(hex2char[2 * i]) * 16;
			temp += digital.indexOf(hex2char[2 * i + 1]);
			bytes[i] = (byte) (temp & 0xff);
		}
		return bytes;
	}

	public static byte[] hexStr2Bytes(String src) {
		/* 对输入值进行规范化整理 */
		src = src.trim().replace(" ", "").toUpperCase();
		// 处理值初始化
		int m = 0, n = 0;
		int iLen = src.length() / 2; // 计算长度
		byte[] ret = new byte[iLen]; // 分配存储空间

		for (int i = 0; i < iLen; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = (byte) (Integer.decode("0x" + src.substring(i * 2, m) + src.substring(m, n)) & 0xFF);
		}
		return ret;
	}
}
