package com.liveyc.rabbitmq.listener;

import java.util.Date;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.model.BycleInfoShort;
import com.lytx.webservice.electrombile.model.BycleStationModel;
import com.lytx.webservice.electrombile.model.TrackBycleShort;
import com.lytx.webservice.electrombile.service.ElectrombileServiceUtil;

public class StolenAlarmLisener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		if (message != null) {
			byte[] infodata = message.getBody();
			if (infodata != null && infodata.length % 14 == 0) {
				int start = 0;
				while (infodata.length > start) {
					BycleAlarmModel by = new BycleAlarmModel();
					String dbcode = "" + infodata[0 + start] + infodata[1 + start] + infodata[2 + start] + infodata[3 + start] + infodata[4 + start] + infodata[5 + start];
					long carId = bytes2int(subBytes(infodata, 6 + start, 4));

					byte date = infodata[10 + start];
					byte hour = infodata[11 + start];
					byte minu = infodata[12 + start];
					byte second = infodata[13 + start];
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
						start = start + 14;
						continue;
					}
					BycleInfoShort bs = ElectrombileServiceUtil.getBycleInfoShort(by.getFdId());
					if (bs != null) {
						by.setPlateNo(bs.getPlateNo());
						by.setBycleid(bs.getId());

					}
					by.setStationName(s.getStationName());
					by.setLongitude(s.getLongitude());
					by.setLatitude(s.getLatitude());
					by.setAreaId(s.getAreaId());
					by.setType(1);
					String buffer = String.valueOf(carId);
					StringBuffer buffer2 = new StringBuffer();
					if (buffer.length() < 10) {
						for (int j = 0; j < 10 - buffer.length(); j++) {
							buffer2.append("0");
						}
					}
					by.setFdId(buffer2.toString());
					by.setFdStatus("0");
					by.setFdLowElecTag(0);
					by.setFdMoveTag(0);
					by.setFdLockTag(0);
					by.setFdNoElecTag(0);
					by.setFdChannel((int) 1);
					by.setAlarmTime(curretdate);
					TrackBycleShort t = ElectrombileServiceUtil.getService().getTrackBycleShort(by.getFdId());
					if (t != null && t.getCaseId() != null) {
						by.setRuleId(t.getRuleId());
						by.setCaseId(t.getCaseId());
						ElectrombileServiceUtil.getService().addBycleTrackedRecord(by);
						ElectrombileServiceUtil.getService().addBycleHandleAlarm(by);
						BycleAlarmModel model = ElectrombileServiceUtil.getService().getbycleTrackedHandle(by);
						if (model != null && model.getActNo() != null && !model.getActNo().equals("")) {
							by.setAlarmId(model.getAlarmId());
							ElectrombileServiceUtil.getService().addbycleStationTracked(by);
						}
					}
					start = start + 14;

				}
			}
		}

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
