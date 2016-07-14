package com.liveyc.rabbitmq.listener;

import java.util.Date;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.lytx.webservice.batch.BycleTask;
import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.model.BycleInfoShort;
import com.lytx.webservice.electrombile.model.BycleStationModel;
import com.lytx.webservice.electrombile.service.ElectrombileServiceUtil;
import com.lytx.webservice.sequence.service.SequenceGeneratorServiceUtil;

public class TrailListener implements MessageListener {
	// @Override
	public void onMessage(Message message) {
		long start = System.currentTimeMillis();
		if (message != null) {
			byte[] infodata = message.getBody();
			String dbcode = "";
			int datastart = 0;
			Date reciveTime = null;
			if (infodata != null && infodata.length > 9) {
				byte byteLenth = infodata[2];
				if (byteLenth == 10 && infodata.length > 19) {
					dbcode = "" + infodata[3] + infodata[4] + infodata[5] + infodata[6] + infodata[7] + infodata[8];
					datastart = 19;
					byte date = infodata[9];
					byte hour = infodata[10];
					byte minu = infodata[11];
					byte second = infodata[12];
					Date curretdate = new Date();
					if (curretdate.getDate() < date) {
						curretdate.setMonth(curretdate.getMonth() - 1);
					}
					curretdate.setDate(date);
					curretdate.setHours(hour);
					curretdate.setMinutes(minu);
					curretdate.setSeconds(second);
					BycleStationModel s = ElectrombileServiceUtil.getBycleStationModel(dbcode);
					if (s != null) {

					} else {
						return;
					}
					while (infodata.length > (datastart + 2)) {
						BycleAlarmModel by = new BycleAlarmModel();
						datastart = datastart + 1;
						byte[] bytecar = subBytes(infodata, datastart, 7);
						byte bytecarType = bytecar[0];// 标签种类
						datastart = datastart + 1;
						long carId = bytes2int(subBytes(bytecar, 1, 4));
						datastart = datastart + 4;
						String buffer = String.valueOf(carId);
						StringBuffer buffer2 = new StringBuffer();
						if (buffer.length() < 10) {
							for (int j = 0; j < 10 - buffer.length(); j++) {
								buffer2.append("0");
							}
						}
						buffer2.append(buffer);
						by.setFdId(buffer2.toString());
						int bytecarTypestatus = bytecar[5] & 0xff;
						datastart = datastart + 1;
						if (bytecarType == 1) {
							String test = byteToBit(bytecar[5]);
							by.setFdStatus(String.valueOf(bytecarTypestatus));
							if (test.substring(0, 1).equals("1")) {
								by.setFdLowElecTag(1);
							} else {
								by.setFdLowElecTag(0);
							}
							by.setFdMoveTag(0);
							by.setFdLockTag(0);
							by.setFdNoElecTag(0);
						} else if (bytecarType == 2) {
							String test = byteToBit(bytecar[5]);
							// System.out.println(test);
							by.setFdStatus(String.valueOf(bytecarTypestatus));
							if (test.substring(0, 1).equals("1")) {
								by.setFdLowElecTag(1);
							} else {
								by.setFdLowElecTag(0);
							}
							if (test.substring(1, 2).equals("1")) {
								by.setFdMoveTag(1);
							} else {
								by.setFdMoveTag(0);
							}
							if (test.substring(2, 3).equals("1")) {
								by.setFdLockTag(1);
							} else {
								by.setFdLockTag(0);
							}
							if (test.substring(3, 4).equals("1")) {
								by.setFdNoElecTag(1);
							} else {
								by.setFdNoElecTag(0);
							}

						}
						byte channelId = bytecar[6];

						datastart = datastart + 1;
						by.setFdChannel((int) channelId);
						by.setAlarmTime(curretdate);

						BycleInfoShort bs = ElectrombileServiceUtil.getBycleInfoShort(by.getFdId());
						if (bs != null) {
							by.setPlateNo(bs.getPlateNo());
							by.setPlatenoArea(bs.getPlatenoArea());
							by.setBycleid(bs.getId());
						} else {
							by.setBycleid(0l);
						}
						by.setStationName(s.getStationName());
						by.setLongitude(s.getLongitude());
						by.setLatitude(s.getLatitude());
						by.setAreaId(s.getAreaId());
						by.setType(0);
						by.setAlarmId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.bycleseqName));
						BycleTask.getInstance().putBycleAlarmModelEvent(by);
					}

				} else if (byteLenth == 6 && infodata.length > 15) {
					dbcode = "" + infodata[3] + infodata[4] + infodata[5] + infodata[6] + infodata[7] + infodata[8];
					datastart = 15;
					while (infodata.length > (datastart + 2)) {
						BycleAlarmModel by = new BycleAlarmModel();
						datastart = datastart + 1;
						byte[] bytecar = subBytes(infodata, datastart, 7);
						byte bytecarType = bytecar[0];// 标签种类
						datastart = datastart + 1;
						long carId = bytes2int(subBytes(bytecar, 1, 4));
						datastart = datastart + 4;
						String buffer = String.valueOf(carId);
						StringBuffer buffer2 = new StringBuffer();
						if (buffer.length() < 10) {
							for (int j = 0; j < 10 - buffer.length(); j++) {
								buffer2.append("0");
							}
						}
						buffer2.append(buffer);
						by.setFdId(buffer2.toString());
						int bytecarTypestatus = bytecar[5] & 0xff;
						datastart = datastart + 1;
						if (bytecarType == 1) {
							String test = byteToBit(bytecar[5]);
							by.setFdStatus(String.valueOf(bytecarTypestatus));
							if (test.substring(0, 1).equals("1")) {
								by.setFdLowElecTag(1);
							} else {
								by.setFdLowElecTag(0);
							}
							by.setFdMoveTag(0);
							by.setFdLockTag(0);
							by.setFdNoElecTag(0);
						} else if (bytecarType == 2) {
							String test = byteToBit(bytecar[5]);
							// System.out.println(test);
							by.setFdStatus(String.valueOf(bytecarTypestatus));
							if (test.substring(0, 1).equals("1")) {
								by.setFdLowElecTag(1);
							} else {
								by.setFdLowElecTag(0);
							}
							if (test.substring(1, 2).equals("1")) {
								by.setFdMoveTag(1);
							} else {
								by.setFdMoveTag(0);
							}
							if (test.substring(2, 3).equals("1")) {
								by.setFdLockTag(1);
							} else {
								by.setFdLockTag(0);
							}
							if (test.substring(3, 4).equals("1")) {
								by.setFdNoElecTag(1);
							} else {
								by.setFdNoElecTag(0);
							}

						}
						byte channelId = bytecar[6];
						BycleStationModel s = ElectrombileServiceUtil.getBycleStationModel(dbcode + "-" + channelId);
						if (s != null) {
							datastart = datastart + 1;
							by.setFdChannel((int) channelId);
							// by.setAlarmTime(curretdate);

							BycleInfoShort bs = ElectrombileServiceUtil.getBycleInfoShort(by.getFdId());
							if (bs != null) {
								by.setPlateNo(bs.getPlateNo());
							}
							by.setStationName(s.getStationName());
							by.setLongitude(s.getLongitude());
							by.setLatitude(s.getLatitude());
							by.setAreaId(s.getAreaId());
							by.setVehicleDirect(s.getChannelName());
							by.setAlarmId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.bycleseqName));
							datastart = datastart + 1;
							byte date = infodata[datastart];
							datastart = datastart + 1;
							byte hour = infodata[datastart];
							datastart = datastart + 1;
							byte minu = infodata[datastart];
							datastart = datastart + 1;
							byte second = infodata[datastart];
							Date curretdate = new Date();
							if (curretdate.getDate() < date) {
								curretdate.setMonth(curretdate.getMonth() - 1);
							}
							curretdate.setDate(date);
							curretdate.setHours(hour);
							curretdate.setMinutes(minu);
							curretdate.setSeconds(second);
							by.setAlarmTime(curretdate);
							BycleTask.getInstance().putBycleAlarmModelEvent(by);
						}
					}

				}

			}
		}
		long endstart = System.currentTimeMillis();
		System.out.println(endstart - start);
	}

	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		for (int i = begin; i < begin + count; i++)
			bs[i - begin] = src[i];
		return bs;
	}

	public static long bytes2int(byte[] bytes) {
		long num = bytes[3] & 0xFFl;
		num |= ((bytes[2] << 8) & 0xFF00l);
		num |= ((bytes[1] << 16) & 0xFF0000l);
		num |= ((bytes[0] << 24) & 0xFF000000l);
		return num;
	}

	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
	}

}
