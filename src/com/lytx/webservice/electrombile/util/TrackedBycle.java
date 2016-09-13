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
	Map<String, TrackBycleShort> hp = new HashMap();
	List<TrackBycleShort> complexPlateNoList = new ArrayList<TrackBycleShort>();

	public TrackedBycle() {
		initTrackedMatrix();
		hp.clear();

	}

	/* ��ʼ�����ؾ��� */
	private void initTrackedMatrix() {
		hp.clear();
	}

	/* ���Ӳ��س��ƺ� */
	public void addPlateNo(TrackBycleShort plateNo) {
		// ��ȷ����
		hp.put(plateNo.getFdid(), plateNo);

	}

	public void removePlateNo(TrackBycleShort plateNo) {
		hp.remove(plateNo.getRuleId());

	}

	/* ��ղ��ؾ��� */
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

	/* ���ƺŲ���ƥ�� */
	public List<TrackBycleShort> match(BycleAlarmModel plateNo) {
		List<TrackBycleShort> matchList = new ArrayList<TrackBycleShort>();
		if (hp.get(plateNo.getFdId()) != null) {
			matchList.add(hp.get(plateNo.getFdId()));
		}
		return matchList;
	}

}
