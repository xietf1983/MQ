package com.liveyc.rabbitmq;

import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.lytx.webservice.batch.BycleTask;
import com.lytx.webservice.batch.TrailMainThread;
import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.model.BycleBlack;
import com.lytx.webservice.electrombile.model.BycleInfoShort;
import com.lytx.webservice.electrombile.model.BycleLostRecord;
import com.lytx.webservice.electrombile.model.BycleStationModel;
import com.lytx.webservice.electrombile.model.TmsSms;
import com.lytx.webservice.electrombile.model.TrackBycleShort;
import com.lytx.webservice.electrombile.service.ElectrombileServiceUtil;
import com.lytx.webservice.electrombile.util.TrackedBycleUntil;
import com.lytx.webservice.sequence.service.SequenceGeneratorServiceUtil;
import com.lytx.webservice.util.DateUtil;

public class DealDataUtil {
	private static Logger iLog = Logger.getLogger(DealDataUtil.class);

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

	public static int bytes2Short(byte[] bytes) {
		int num = bytes[1] & 0xFF;
		num |= ((bytes[0] << 8) & 0xFF00);
		return num;
	}

	public static byte[] intToByteArray(long a) {
		return new byte[] { (byte) ((a >> 24) & 0xFF), (byte) ((a >> 16) & 0xFF), (byte) ((a >> 8) & 0xFF), (byte) (a & 0xFF) };
	}

	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
	}

	public static void dealStolen(byte[] infodata) {
		if (infodata != null && infodata.length % 15 == 0) {
			int start = 0;
			while (infodata.length > start) {
				try {
					BycleAlarmModel by = new BycleAlarmModel();
					String dbcode = String.valueOf(bytes2int(subBytes(infodata, 0 + start, 4)));
					long carId = bytes2int(subBytes(infodata, 4 + start, 4));

					byte date = infodata[8 + start];
					byte hour = infodata[9 + start];
					byte minu = infodata[10 + start];
					byte second = infodata[11 + start];
					int alarmType = infodata[12 + start];
					int channel = infodata[13 + start];
					int modifytab = infodata[14 + start];
					// String modifytab = byteToBit(infodata[14 + start]);
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
						start = start + 15;
						continue;
					}
					BycleInfoShort bs = null;
					by.setBycleid(0l);
					by.setAlarmType(alarmType);
					by.setFdStatus(modifytab + "");
					by.setStationId(s.getStationId());
					by.setStationName(s.getStationName());
					by.setLongitude(s.getLongitude());
					by.setLatitude(s.getLatitude());
					by.setAreaId(s.getAreaId());
					by.setAreaCode(s.getAreaCode());
					by.setType(1);
					String buffer = String.valueOf(carId);
					StringBuffer buffer2 = new StringBuffer();
					/*
					 * if (buffer.length() < 10) { for (int j = 0; j < 10 -
					 * buffer.length(); j++) { buffer2.append("0"); } }
					 */
					buffer2.append(buffer);
					by.setFdId(buffer2.toString());
					bs = ElectrombileServiceUtil.getBycleInfoShort(by.getFdId());
					if (bs != null) {
						by.setPlateNo(bs.getPlateNo());
						by.setPlatenoArea(bs.getPlatenoArea());
						by.setBycleOwner(bs.getOwner());
						by.setBycleid(bs.getId());
						by.setUserTel(bs.getUserTel());

					}
					// by.setFdStatus("0");
					by.setFdLowElecTag(0);
					by.setFdMoveTag(0);
					by.setFdLockTag(0);
					by.setFdNoElecTag(0);
					by.setFdChannel(channel);
					by.setAlarmTime(curretdate);
					by.setType(1);
					by.setStatus("1");
					by.setAlarmId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.bycleseqName));
					TrackBycleShort t = ElectrombileServiceUtil.getService().getTrackBycleShort(by.getFdId());
					if (t != null && t.getCaseId() != null && by.getBycleid() > 0) {
						by.setRuleId(t.getRuleId());
						by.setCaseId(t.getCaseId());
						ElectrombileServiceUtil.getService().addBycleTrackedRecord(by);
						ElectrombileServiceUtil.getService().addBycleHandleAlarm(by);
						BycleAlarmModel model = ElectrombileServiceUtil.getService().getbycleTrackedHandle(by);
						if (model != null && model.getActNo() != null && !model.getActNo().equals("") && by.getAreaId() != null && !by.getAreaId().equals("")) {
							by.setActNo(model.getActNo());
							ElectrombileServiceUtil.getService().addbycleStationTracked(by);
						}
						TmsSms tt = ElectrombileServiceUtil.getService().findTmsSms(by);
						if (tt != null && by.getUserTel() != null && !by.getUserTel().equals("")) {
							if (tt.getCreateDate() != null && tt.getCreateDate().getTime() < (new Date().getTime() - DateUtil.MINUTE) && !by.getStationId().equals(tt.getStationId())) {
								tt.setCreateDate(new Date());
								tt.setFdid(by.getFdId());
								tt.setStatus(0);
								tt.setSmsType(2 + "");
								tt.setStationId(by.getStationId());
								tt.setPlatenoArea(by.getPlatenoArea());
								StringBuffer bufferContent = new StringBuffer();
								if (by.getBycleOwner() != null) {
									bufferContent.append(by.getBycleOwner());
									bufferContent.append(",");
								} else {
									// bufferContent.append("");
								}
								bufferContent.append("您的电动车");
								bufferContent.append(by.getPlatenoArea());
								bufferContent.append(by.getPlateNo());
								bufferContent.append("现在位于");
								bufferContent.append(by.getStationName());
								bufferContent.append("，请尽快确认!");
								tt.setStationId(by.getStationId());
								// 您的电动车LHxxxxxx现在位于xxxxxx（所在位置），请尽快确认！
								tt.setSmscontent(bufferContent.toString());
								tt.setOwner(by.getBycleOwner());
								tt.setMobilePhone(by.getUserTel());
								// ElectrombileServiceUtil.getService().addToTmsSms(tt,
								// true);
							}
						} else {
							if (by.getUserTel() != null && !by.getUserTel().equals("")) {
								tt = new TmsSms();
								tt.setId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.commonseq));
								tt.setCreateDate(new Date());
								StringBuffer bufferContent = new StringBuffer();
								if (by.getBycleOwner() != null) {
									bufferContent.append(by.getBycleOwner());
									bufferContent.append(",");
								} else {
									// bufferContent.append("");
								}
								tt.setStationId(by.getStationId());
								bufferContent.append("您的电动车");
								bufferContent.append(by.getPlatenoArea());
								bufferContent.append(by.getPlateNo());
								bufferContent.append("现在位于");
								bufferContent.append(by.getStationName());
								bufferContent.append("，请尽快确认!");
								tt.setFdid(by.getFdId());
								tt.setStatus(0);
								tt.setPlateno(by.getPlateNo());
								tt.setStationId(by.getStationId());
								tt.setSmsType("2" + "");
								tt.setPlatenoArea(by.getPlatenoArea());
								tt.setSmscontent(bufferContent.toString());
								tt.setOwner(by.getBycleOwner());
								tt.setMobilePhone(by.getUserTel());
								// ElectrombileServiceUtil.getService().addToTmsSms(tt,
								// false);
							}
							// t.setOwner(owner)
						}

					} else {
						// ElectrombileServiceUtil.getService().addBycleTrackedRecord(by);
					}
					start = start + 15;

				} catch (Exception ex) {
					start = start + 15;
				}
				// start = start + 14;

			}
		}

	}

	public static void dealAbnormal(byte[] infodata) {
		if (infodata != null && infodata.length % 15 == 0) {
			int start = 0;
			while (infodata.length > start) {
				try {
					BycleAlarmModel by = new BycleAlarmModel();
					String dbcode = String.valueOf(bytes2int(subBytes(infodata, 0 + start, 4)));
					long carId = bytes2int(subBytes(infodata, 4 + start, 4));

					byte date = infodata[8 + start];
					byte hour = infodata[9 + start];
					byte minu = infodata[9 + start];
					byte second = infodata[11 + start];
					int alarmType = infodata[12 + start];
					int channel = infodata[13 + start];
					int modifytab = infodata[14 + start];
					// String modifytab = byteToBit(infodata[14 + start]);
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
						start = start + 15;
						continue;
					}
					BycleInfoShort bs = null;
					by.setStationId(dbcode);
					by.setStationName(s.getStationName());
					by.setAreaCode(s.getAreaCode());
					by.setLongitude(s.getLongitude());
					by.setLatitude(s.getLatitude());
					by.setAreaId(s.getAreaId());
					String buffer = String.valueOf(carId);
					StringBuffer buffer2 = new StringBuffer();
					/*
					 * if (buffer.length() < 10) { for (int j = 0; j < 10 -
					 * buffer.length(); j++) { buffer2.append("0"); } }
					 */
					buffer2.append(buffer);
					by.setFdId(buffer2.toString());
					try {
						bs = ElectrombileServiceUtil.getBycleInfoShort(by.getFdId());
					} catch (Exception ex) {
						int a = 0;
					}
					by.setBycleid(0l);
					if (bs != null) {
						by.setPlateNo(bs.getPlateNo());
						by.setPlatenoArea(bs.getPlatenoArea());
						by.setBycleOwner(bs.getOwner());
						by.setBycleid(bs.getId());
						by.setUserTel(bs.getUserTel());

					}
					// by.setFdChannel(fdChannel)
					by.setAlarmType(alarmType);
					by.setFdStatus(modifytab + "");
					// String modifytab = byteToBit(infodata[14 + start]);
					// by.setModifytab(modifytab);
					by.setFdLowElecTag(0);
					by.setFdMoveTag(0);
					by.setFdLockTag(0);
					by.setFdNoElecTag(0);
					by.setFdChannel(channel);
					by.setAlarmTime(curretdate);
					by.setType(0);
					int bytecarTypestatus = infodata[14 + start] & 0xff;
					// by.setFdStatus(bytecarTypestatus+"");
					String test = byteToBit(infodata[14 + start]);
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
					by.setType(0);
					by.setAlarmId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.bycleseqName));
					ElectrombileServiceUtil.getService().addBycleTrackedRecord(by);
					// ElectrombileServiceUtil.getService().addBycleHandleAlarm(by);
					start = start + 15;
					if (by.getFdNoElecTag() == 1 && by.getAreaId() != null && by.getBycleid() != null && by.getBycleid() > 0 && by.getUserTel() != null && !by.getUserTel().equals("")) {
						TmsSms tt = ElectrombileServiceUtil.getService().findTmsSms(by);
						if (tt != null && by.getUserTel() != null && !by.getUserTel().equals("")) {
							// t.setId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.commonseq));
							if (new Date().getTime() - tt.getCreateDate().getTime() > DateUtil.HOUR * 12 && tt.getStatus() != 0) {
								tt.setCreateDate(new Date());
								tt.setFdid(by.getFdId());
								tt.setStatus(0);
								tt.setSmsType(2 + "");
								tt.setPlatenoArea(by.getPlatenoArea());
								StringBuffer bufferContent = new StringBuffer();
								if (by.getBycleOwner() != null) {
									bufferContent.append(by.getBycleOwner());
									bufferContent.append(",");
								} else {
									// bufferContent.append("");
								}
								tt.setPlatenoArea(by.getPlatenoArea());
								tt.setPlateno(by.getPlateNo());
								tt.setStationId(by.getStationId());
								bufferContent.append("您的电动车");
								bufferContent.append(by.getPlatenoArea());
								bufferContent.append(by.getPlateNo());
								bufferContent.append("车身防盗标签断电了，为避免影响到车辆的正常使用，请经尽快修复或致电防盗标签热线电话18006768976！");
								// bufferContent.append(by.getStationName());
								// bufferContent.append("，请尽快确认!");

								// 您的电动车LHxxxxxx车身防盗标签断电了，为避免影响到车辆的正常使用，请经尽快修复或致电防盗标签热线电话18006768976！
								tt.setSmscontent(bufferContent.toString());
								tt.setOwner(by.getBycleOwner());
								tt.setMobilePhone(by.getUserTel());
								// ElectrombileServiceUtil.getService().addToTmsSms(tt,
								// true);
							}
						} else {
							tt = new TmsSms();
							tt.setId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.commonseq));
							tt.setCreateDate(new Date());
							tt.setFdid(by.getFdId());
							tt.setStatus(0);
							tt.setSmsType("2" + "");
							tt.setPlatenoArea(by.getPlatenoArea());
							StringBuffer bufferContent = new StringBuffer();
							if (by.getBycleOwner() != null) {
								bufferContent.append(by.getBycleOwner());
								bufferContent.append(",");
							} else {
								// bufferContent.append("");
							}
							tt.setPlatenoArea(by.getPlatenoArea());
							tt.setPlateno(by.getPlateNo());
							tt.setStationId(by.getStationId());
							bufferContent.append("您的电动车");
							bufferContent.append(by.getPlatenoArea());
							bufferContent.append(by.getPlateNo());
							bufferContent.append("车身防盗标签断电了，为避免影响到车辆的正常使用，请经尽快修复或致电防盗标签热线电话18006768976！");
							// bufferContent.append(by.getStationName());
							tt.setSmscontent(bufferContent.toString());
							tt.setOwner(by.getBycleOwner());
							tt.setMobilePhone(by.getUserTel());
							// ElectrombileServiceUtil.getService().addToTmsSms(tt,
							// false);
							// t.setOwner(owner)
						}
					}

				} catch (Exception ex) {
					start = start + 15;
				}
			}
		}
	}

	public static void dealinfodata(byte[] infodata, String dbcode, Date curretdate, BycleStationModel s, int type) {
		if (s != null && s.getStationId() != null) {
			int datastart = 0;
			if (type == 1) {
				// 有时间的
				while (datastart < infodata.length - 1) {
					BycleAlarmModel by = new BycleAlarmModel();
					byte[] bytecar = subBytes(infodata, datastart, 9);
					datastart = datastart + 9;
					byte gjlx = bytecar[0];// 标签种类
					byte bytecarType = bytecar[1];// 标签种类
					long carId = bytes2int(subBytes(bytecar, 2, 4));
					String buffer = String.valueOf(carId);
					StringBuffer buffer2 = new StringBuffer();
					/*
					 * if (buffer.length() < 10) { for (int j = 0; j < 10 -
					 * buffer.length(); j++) { buffer2.append("0"); } }
					 */
					buffer2.append(buffer);
					by.setFdId(buffer2.toString());
					by.setStationId(dbcode);
					int bytecarTypestatus = bytecar[6] & 0xff;
					if (bytecarType == 1) {
						String test = byteToBit(bytecar[6]);
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
						String test = byteToBit(bytecar[6]);
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

					} else {
						by.setFdMoveTag(0);
						by.setFdLockTag(0);
						by.setFdNoElecTag(0);
						by.setStatus("0");
						// continue;
					}
					if (bytecarType == 1 || bytecarType == 2) {

					} else {
						continue;
					}
					byte modifytab = bytecar[7];
					by.setModifytab(byteToBit(modifytab));
					byte channelId = bytecar[8];// 通达
					by.setFdChannel((int) channelId);
					by.setAlarmTime(curretdate);

					BycleInfoShort bs = ElectrombileServiceUtil.getBycleInfoShort(by.getFdId());
					// BycleInfoShort bs = null;
					if (bs != null) {
						by.setPlateNo(bs.getPlateNo());
						by.setPlatenoArea(bs.getPlatenoArea());
						by.setBycleOwner(bs.getOwner());
						by.setBycleid(bs.getId());
						by.setUserTel(bs.getUserTel());
					} else {
						by.setBycleid(0l);
					}
					by.setStationName(s.getStationName());
					by.setLongitude(s.getLongitude());
					by.setLatitude(s.getLatitude());
					by.setAreaId(s.getAreaId());
					by.setAreaCode(s.getAreaCode());
					by.setType(0);
					by.setAlarmType((int) gjlx);
					by.setAlarmId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.bycleseqName));
					// by.setAlarmId(1l);
					// 断电与加锁移位

					if (by.getFdMoveTag() == 1 && by.getFdLockTag() == 1) {
						BycleAlarmModel m = by;
						if (m.getAreaCode() != null && m.getBycleid() != null && m.getBycleid() > 0) {
							iLog.error("增加到黑名单:" + m.getFdId());
							BycleBlack b = new BycleBlack();
							b.setActNo(m.getActNo());
							b.setAlarmId(m.getAlarmId());
							b.setAlarmMemo(m.getAlarmMemo());
							b.setAlarmTime(m.getAlarmTime());
							b.setAlarmType(m.getAlarmType());
							b.setAreaCode(m.getAreaCode());
							b.setAreaId(m.getAreaId());
							b.setBycleOwner(m.getBycleOwner());
							b.setCaseId(ElectrombileServiceUtil.getService().getCaseIdNext(m.getAreaCode()));
							b.setRuleId(UUID.randomUUID().toString());
							b.setBycleId(m.getBycleid());
							b.setFdId(m.getFdId());
							b.setBycleOwner(m.getBycleOwner());
							b.setAlarmPhone(m.getUserTel());
							b.setPlatenoArea(m.getPlatenoArea());
							b.setPlateNo(m.getPlateNo());
							b.setBycleOwner(m.getBycleOwner());
							b.setAreaCode(m.getAreaCode());
							b.setStatus("0");
							b.setType(0);
							b.setIssecret(0);
							b.setSouce("4");
							// 其他潮日志表
							BycleLostRecord lost = new BycleLostRecord();
							lost.setCaseid(b.getAreaId());
							lost.setId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.commonseq));
							lost.setFdId(m.getFdId());
							lost.setBycleId(m.getBycleid());
							lost.setPlatenoArea(m.getPlatenoArea());
							lost.setPlateNo(m.getPlateNo());
							lost.setCreatedate(new Date());
							lost.setLostStatus(0);
							lost.setStatus(0);
							lost.setSubmitDate(new Date());
							lost.setCaseid(b.getCaseId());
							// lost.setCaseid(caseid)
							lost.setIsclose(0);
							lost.setIsclassical(0);
							lost.setFeestatus(0);

							// TrackedBycleUntil.addPlateNo(new
							// TrackBycleShort(b.getFdId(), b.getRuleId()));
						
							if (!ElectrombileServiceUtil.getService().findBycleWhite(by)) {
								ElectrombileServiceUtil.getService().addBycleBlack(b, lost);
								by.setCaseId(b.getCaseId());
								by.setRuleId(b.getRuleId());
								by.setType(0);
								ElectrombileServiceUtil.getService().addBycleTrackedRecord(by);
								ElectrombileServiceUtil.getService().addBycleHandleAlarm(by);
							}
						}
					}
					if (by.getFdNoElecTag() == 1 && by.getAreaId() != null && by.getBycleid() != null && by.getBycleid() > 0 && by.getUserTel() != null && !by.getUserTel().equals("")) {
						// 美隔12小时候发送短信
						TmsSms t = ElectrombileServiceUtil.getService().findTmsSms(by);
						if (t != null) {
							// t.setId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.commonseq));
							if (new Date().getTime() - t.getCreateDate().getTime() > DateUtil.HOUR * 12 && t.getStatus() != 0) {
								t.setCreateDate(new Date());
								t.setFdid(by.getFdId());
								t.setStatus(0);
								t.setSmsType(type + "");
								t.setPlatenoArea(by.getPlatenoArea());
								// t.setSmscontent("电瓶车在时间" +
								// DateUtil.dateToString(by.getAlarmTime()) +
								// by.getStationName() + "处在状态异常 ，请确认是否被盗！");
								t.setOwner(by.getBycleOwner());
								t.setMobilePhone(by.getUserTel());
								StringBuffer bufferContent = new StringBuffer();
								if (by.getBycleOwner() != null) {
									bufferContent.append(by.getBycleOwner());
									bufferContent.append(",");
								} else {
									// bufferContent.append("");
								}
								t.setPlatenoArea(by.getPlatenoArea());
								t.setPlateno(by.getPlateNo());
								t.setStationId(by.getStationId());
								bufferContent.append("您的电动车");
								bufferContent.append(by.getPlatenoArea());
								bufferContent.append(by.getPlateNo());
								bufferContent.append("车身防盗标签断电了，为避免影响到车辆的正常使用，请经尽快修复或致电防盗标签热线电话18006768976！");
								// bufferContent.append(by.getStationName());
								// bufferContent.append("，请尽快确认!");

								// 您的电动车LHxxxxxx车身防盗标签断电了，为避免影响到车辆的正常使用，请经尽快修复或致电防盗标签热线电话18006768976！
								t.setSmscontent(bufferContent.toString());
								// ElectrombileServiceUtil.getService().addToTmsSms(t,
								// true);
							}
						} else {
							t = new TmsSms();
							t.setId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.commonseq));
							t.setCreateDate(new Date());
							t.setFdid(by.getFdId());
							t.setStatus(0);
							t.setSmsType(type + "");
							t.setPlatenoArea(by.getPlatenoArea());
							StringBuffer bufferContent = new StringBuffer();
							if (by.getBycleOwner() != null) {
								bufferContent.append(by.getBycleOwner());
								bufferContent.append(",");
							} else {
								// bufferContent.append("");
							}
							t.setPlatenoArea(by.getPlatenoArea());
							t.setPlateno(by.getPlateNo());
							t.setStationId(by.getStationId());
							bufferContent.append("您的电动车");
							bufferContent.append(by.getPlatenoArea());
							bufferContent.append(by.getPlateNo());
							bufferContent.append("车身防盗标签断电了，为避免影响到车辆的正常使用，请经尽快修复或致电防盗标签热线电话18006768976！");
							// bufferContent.append(by.getStationName());
							// bufferContent.append("，请尽快确认!");

							// 您的电动车LHxxxxxx车身防盗标签断电了，为避免影响到车辆的正常使用，请经尽快修复或致电防盗标签热线电话18006768976！
							t.setSmscontent(bufferContent.toString());
							// t.setSmscontent("电瓶车在时间" +
							// DateUtil.dateToString(by.getAlarmTime()) +
							// by.getStationName() + "处在状态异常 ，请确认是否被盗！");
							t.setOwner(by.getBycleOwner());
							t.setMobilePhone(by.getUserTel());
							// ElectrombileServiceUtil.getService().addToTmsSms(t,
							// false);
							// t.setOwner(owner)
						}
					}

					BycleTask.getInstance().putBycleAlarmModelEvent(by);

				}
			} else if (type == 2) {
				while (datastart < infodata.length - 1) {
					BycleAlarmModel by = new BycleAlarmModel();
					byte[] bytecar = subBytes(infodata, datastart, 13);
					datastart = datastart + 13;
					byte gjlx = bytecar[0];// 标签种类
					byte bytecarType = bytecar[1];// 标签种类
					long carId = bytes2int(subBytes(bytecar, 2, 4));
					String buffer = String.valueOf(carId);
					StringBuffer buffer2 = new StringBuffer();
					buffer2.append(buffer);
					by.setFdId(buffer2.toString());
					by.setStationId(dbcode);
					int bytecarTypestatus = bytecar[6] & 0xff;
					if (bytecarType == 1) {
						String test = byteToBit(bytecar[6]);
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
						String test = byteToBit(bytecar[6]);
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

					} else {
						by.setFdMoveTag(0);
						by.setFdLockTag(0);
						by.setFdNoElecTag(0);
						by.setStatus("0");
						// continue ;
					}
					byte modifytab = bytecar[7];
					by.setModifytab(byteToBit(modifytab));
					byte channelId = bytecar[8];// 通达
					by.setFdChannel((int) channelId);
					by.setAlarmTime(curretdate);
					curretdate = new Date();
					byte date = infodata[datastart];
					datastart = datastart + 1;
					byte hour = infodata[datastart];
					datastart = datastart + 1;
					byte minu = infodata[datastart];
					datastart = datastart + 1;
					byte second = infodata[datastart];
					datastart = datastart + 1;
					if (bytecarType == 1 || bytecarType == 2) {

					} else {
						continue;
					}
					if (curretdate.getDate() < date) {
						curretdate.setMonth(curretdate.getMonth() - 1);
					}
					curretdate.setDate(date);
					curretdate.setHours(hour);
					curretdate.setMinutes(minu);
					curretdate.setSeconds(second);
					BycleInfoShort bs = ElectrombileServiceUtil.getBycleInfoShort(by.getFdId());
					// BycleInfoShort bs = new BycleInfoShort();
					if (bs != null) {
						by.setPlateNo(bs.getPlateNo());
						by.setPlatenoArea(bs.getPlatenoArea());
						by.setBycleOwner(bs.getOwner());
						by.setBycleid(bs.getId());
						by.setUserTel(bs.getUserTel());
						// by.setAreaCode(areaCode)
					} else {
						by.setBycleid(0l);
					}
					by.setStationName(s.getStationName());
					by.setLongitude(s.getLongitude());
					by.setLatitude(s.getLatitude());
					by.setAreaId(s.getAreaId());
					by.setAreaCode(s.getAreaCode());
					by.setType(0);
					by.setAlarmType((int) gjlx);
					by.setAlarmId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.bycleseqName));
					// if (by.getBycleid() > 0) {
					BycleTask.getInstance().putBycleAlarmModelEvent(by);
					// }
					// 断电与加锁移位
					// by.setAlarmId(0l);
					if (by.getFdMoveTag() == 1 && by.getFdLockTag() == 1) {
						BycleAlarmModel m = by;
						if (m.getAreaCode() != null && m.getBycleid() != null && m.getBycleid() > 0) {
							BycleBlack b = new BycleBlack();
							b.setActNo(m.getActNo());
							b.setAlarmId(m.getAlarmId());
							b.setAlarmMemo(m.getAlarmMemo());
							b.setAlarmTime(m.getAlarmTime());
							b.setAlarmType(m.getAlarmType());
							b.setAreaCode(m.getAreaCode());
							b.setAreaId(m.getAreaId());
							b.setBycleOwner(m.getBycleOwner());
							b.setCaseId(ElectrombileServiceUtil.getService().getCaseIdNext(m.getAreaCode()));
							b.setRuleId(UUID.randomUUID().toString());
							b.setBycleId(m.getBycleid());
							b.setFdId(m.getFdId());
							b.setPlatenoArea(m.getPlatenoArea());
							b.setPlateNo(m.getPlateNo());
							b.setBycleOwner(m.getBycleOwner());
							b.setAreaCode(m.getAreaCode());
							b.setStatus("0");
							b.setIssecret(0);
							b.setSouce("4");
							b.setBycleOwner(m.getBycleOwner());
							b.setAlarmPhone(m.getUserTel());
							b.setType(0);// 异常车辆库
							// 其他潮日志表
							BycleLostRecord lost = new BycleLostRecord();
							// lost.setCaseid(b.getAreaId());
							lost.setId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.commonseq));
							lost.setFdId(m.getFdId());
							lost.setBycleId(m.getBycleid());
							lost.setPlatenoArea(m.getPlatenoArea());
							lost.setPlateNo(m.getPlateNo());
							lost.setCreatedate(new Date());
							lost.setLostStatus(0);
							lost.setStatus(0);
							lost.setSubmitDate(new Date());
							lost.setCaseid(b.getCaseId());
							// lost.setCaseid(caseid)
							lost.setIsclose(0);
							lost.setIsclassical(0);
							lost.setFeestatus(0);
							//ElectrombileServiceUtil.getService().addBycleBlack(b, lost);
							// TrackedBycleUntil.addPlateNo(new
							// TrackBycleShort(b.getFdId(), b.getRuleId()));
							if (!ElectrombileServiceUtil.getService().findBycleWhite(by)) {
								ElectrombileServiceUtil.getService().addBycleBlack(b, lost);
								by.setCaseId(b.getCaseId());
								by.setRuleId(b.getRuleId());
								by.setType(0);
								ElectrombileServiceUtil.getService().addBycleTrackedRecord(by);
								ElectrombileServiceUtil.getService().addBycleHandleAlarm(by);
							}
						}
					}
					if (by.getFdNoElecTag() == 1 && by.getAreaId() != null && by.getBycleid() != null && by.getBycleid() > 0 && by.getUserTel() != null && !by.getUserTel().equals("")) {
						// 美隔12小时候发送短信
						TmsSms t = ElectrombileServiceUtil.getService().findTmsSms(by);
						if (t != null) {
							// t.setId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.commonseq));
							if (new Date().getTime() - t.getCreateDate().getTime() > DateUtil.HOUR * 12 && t.getStatus() != 0)
								t.setCreateDate(new Date());
							t.setFdid(by.getFdId());
							t.setStatus(0);
							t.setSmsType(type + "");
							t.setPlatenoArea(by.getPlatenoArea());
							t.setSmscontent("电瓶车在时间" + DateUtil.dateToString(by.getAlarmTime()) + by.getStationName() + "处在状态异常 ，请确认是否被盗！");
							t.setOwner(by.getBycleOwner());
							t.setMobilePhone(by.getUserTel());
							// ElectrombileServiceUtil.getService().addToTmsSms(t,
							// true);
						} else {
							t = new TmsSms();
							t.setId(SequenceGeneratorServiceUtil.getSequenceNext(SequenceGeneratorServiceUtil.commonseq));
							t.setCreateDate(new Date());
							t.setFdid(by.getFdId());
							t.setStatus(0);
							t.setSmsType(type + "");
							t.setPlatenoArea(by.getPlatenoArea());
							t.setSmscontent("电瓶车在时间" + DateUtil.dateToString(by.getAlarmTime()) + by.getStationName() + "处在状态异常 ，请确认是否被盗！");
							t.setOwner(by.getBycleOwner());
							t.setMobilePhone(by.getUserTel());
							// ElectrombileServiceUtil.getService().addToTmsSms(t,
							// false);
							// t.setOwner(owner)
						}
					}

				}
			}
		}
	}

	public static void dealTrailData(byte[] infodata) {
		int start = 0;
		while (start < infodata.length - 1) {
			start = start + 2;
			byte byteLenth = infodata[start];
			// 数据包长度
			start = start + 1;
			if (byteLenth == 8) {
				//
				String dbcode = "";
				dbcode = String.valueOf(bytes2int(subBytes(infodata, start, 4)));
				start = start + 4;
				byte date = infodata[start];
				start = start + 1;
				byte hour = infodata[start];
				start = start + 1;
				byte minu = infodata[start];
				start = start + 1;
				byte second = infodata[10];
				start = start + 1;
				Date curretdate = new Date();
				if (curretdate.getDate() < date) {
					curretdate.setMonth(curretdate.getMonth() - 1);
				}
				curretdate.setDate(date);
				curretdate.setHours(hour);
				curretdate.setMinutes(minu);
				curretdate.setSeconds(second);
				// start = start + 4;
				// 时间
				BycleStationModel s = ElectrombileServiceUtil.getBycleStationModel(dbcode);
				start = start + 4;
				// 去掉前面的四个字节
				int infollength = bytes2Short((subBytes(infodata, start, 2)));
				// 轨迹数据长度
				start = start + 2;
				// 增加2个字节
				byte[] infodatas = subBytes(infodata, start, infollength);
				dealinfodata(infodatas, dbcode, curretdate, s, 1);
				start = start + infollength;
				// 增加数据的长度
				start = start + 1;
				// 校验位
			} else if (byteLenth == 4) {
				String dbcode = "";
				dbcode = String.valueOf(bytes2int(subBytes(infodata, start, 4)));
				start = start + 4;
				// 基站ID
				/*
				 * byte date = infodata[7]; byte hour = infodata[8]; byte minu =
				 * infodata[9]; byte second = infodata[10]; Date curretdate =
				 * new Date(); if (curretdate.getDate() < date) {
				 * curretdate.setMonth(curretdate.getMonth() - 1); }
				 * curretdate.setDate(date); curretdate.setHours(hour);
				 * curretdate.setMinutes(minu); curretdate.setSeconds(second);
				 */
				// start = start + 4;
				BycleStationModel s = ElectrombileServiceUtil.getBycleStationModel(dbcode);
				start = start + 4;
				int infollength = bytes2Short((subBytes(infodata, start, 2)));
				start = start + 2;
				byte[] infodatas = subBytes(infodata, start, infollength);
				dealinfodata(infodatas, dbcode, null, s, 2);
				start = start + infollength;
				start = start + 1;
			} else {
				break;
			}

		}

	}
}
