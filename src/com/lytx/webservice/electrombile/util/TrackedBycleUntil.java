package com.lytx.webservice.electrombile.util;

import java.util.List;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.model.TrackBycleShort;


public class TrackedBycleUntil {
	static TrackedBycle tv = new TrackedBycle();
	
	/*��Ӳ��س���*/
	public static void addPlateNo(TrackBycleShort plateNo){
		tv.addPlateNo(plateNo);
	}
	
	/*��������ƥ�亯��*/
	public static List<TrackBycleShort> match(BycleAlarmModel plateNo){
		return tv.match(plateNo);
	}
	
	/*��ղ��س�������*/
	public static void clear(){
		tv.clearTrackedMatrix();
	}
	public  static void remove(TrackBycleShort plateNo){
		tv.removePlateNo(plateNo);
		
	}
	
}
