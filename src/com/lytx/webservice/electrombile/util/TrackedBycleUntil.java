package com.lytx.webservice.electrombile.util;

import java.util.List;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.model.TrackBycleShort;


public class TrackedBycleUntil {
	static TrackedBycle tv = new TrackedBycle();
	
	/*添加布控车辆*/
	public static void addPlateNo(TrackBycleShort plateNo){
		tv.addPlateNo(plateNo);
	}
	
	/*车辆布控匹配函数*/
	public static List<TrackBycleShort> match(BycleAlarmModel plateNo){
		return tv.match(plateNo);
	}
	
	/*清空布控车辆数据*/
	public static void clear(){
		tv.clearTrackedMatrix();
	}
	public  static void remove(TrackBycleShort plateNo){
		tv.removePlateNo(plateNo);
		
	}
	
}
