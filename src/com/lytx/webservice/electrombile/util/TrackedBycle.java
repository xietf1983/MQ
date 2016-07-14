package com.lytx.webservice.electrombile.util;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.model.TrackBycleShort;

public class TrackedBycle {
	Map<String, List<TrackBycleShort>> hp = new HashMap();
	List<TrackBycleShort> complexPlateNoList = new ArrayList<TrackBycleShort>();

	public TrackedBycle() {
		initTrackedMatrix();
		hp.clear();

	}

	/* 初始化布控矩阵 */
	private void initTrackedMatrix() {
		hp.clear();
		complexPlateNoList.clear();
	}

	/* 增加布控车牌号 */
	public void addPlateNo(TrackBycleShort plateNo) {
		// 精确布控

		if (plateNo.getFdid() != null && plateNo.getFdid().indexOf("*") < 0 && plateNo.getFdid().indexOf("?") < 0) {
			if (hp.get(plateNo.getFdid()) == null) {
				List<TrackBycleShort> list = new ArrayList();
				list.add(plateNo);
				hp.put(plateNo.getFdid(), list);
			} else {
				List<TrackBycleShort> list = hp.get(plateNo.getFdid());
				boolean ret = false;
				for (TrackBycleShort p : list) {
					if (p.getRuleId().equals(plateNo.getRuleId())) {
						ret = true;
						break;
					}
				}
				if (!ret) {
					list.add(plateNo);
					hp.put(plateNo.getFdid(), list);
				}

			}
		} else if (plateNo.getFdid() != null && (plateNo.getFdid().indexOf("*") > -1 || plateNo.getFdid().indexOf("?") > -1)) {
			this.complexPlateNoList.add(plateNo);
		}

	}

	public void removePlateNo(TrackBycleShort plateNo) {
		if (plateNo != null && plateNo.getFdid() != null) {
			if (plateNo.getFdid().indexOf("*") > -1 || plateNo.getFdid().indexOf("?") > -1) {
				// 模糊
				for (TrackBycleShort p : this.complexPlateNoList) {
					if (p.getRuleId().equals(plateNo.getRuleId())) {
						this.complexPlateNoList.remove(p);
						break;
					}
				}

			} else {
				if (hp.containsKey(plateNo.getFdid())) {
					List<TrackBycleShort> list = hp.get(plateNo.getFdid());
					Iterator<TrackBycleShort> it = list.iterator();
					while (it.hasNext()) {
						TrackBycleShort data = it.next();
						if (data.getRuleId().equals(plateNo.getRuleId())) {
							it.remove();
							list.remove(data);
						}
					}

					if (list.size() < 1) {
						list = null;
						hp.remove(plateNo.getRuleId());
					} else {
						hp.put(plateNo.getRuleId(), list);
					}
				}

			}
		}

	}

	/* 清空布控矩阵 */
	public void clearTrackedMatrix() {
		initTrackedMatrix();
		// complexPlateNoList = new ArrayList<PlateNo>();
	}

	public String complexPlateNoToRE(String complexPlateNo) {
		StringBuilder sb = new StringBuilder();
		if (complexPlateNo != null && complexPlateNo.length() > 0) {
			if (complexPlateNo.charAt(0) != '*') {
				sb.append('^');
			}
			for (int i = 0; i < complexPlateNo.length(); i++) {
				char oneChar = complexPlateNo.charAt(i);
				if (oneChar == '*') {
					if (i == 0) {
						// do nothing
					} else if (i == complexPlateNo.length() - 1) {
						// do nothing
					} else {
						sb.append(".*");
					}
				} else if (oneChar == '?') {
					sb.append(".");
				} else {
					sb.append('[').append(oneChar).append("_]");
				}
			}
			if (complexPlateNo.charAt(complexPlateNo.length() - 1) != '*') {
				sb.append('$');
			}
			if (sb.length() == 0) {
				sb.append('.');
			}
		}
		return sb.toString().toUpperCase();
	}

	/* 车牌号布控匹配 */
	public List<TrackBycleShort> match(BycleAlarmModel plateNo) {
		List<TrackBycleShort> matchList = new ArrayList<TrackBycleShort>();
		if (hp.get(plateNo.getFdId()) != null) {
			List<TrackBycleShort> list = hp.get(plateNo.getFdId());
			for (TrackBycleShort p : list) {
				matchList.add(p);
			}
		}
		for (int i = 0; i < this.complexPlateNoList.size(); i++) {
			String regEx = complexPlateNoToRE(this.complexPlateNoList.get(i).getFdid());
			Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(plateNo.getFdId());
			boolean b = m.find();
			if (b) {
				matchList.add(this.complexPlateNoList.get(i));
			}
		}
		return matchList;
	}

}
