package com.lytx.webservice.electrombile.service.persistence;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.lytx.webservice.electrombile.model.BycleAlarmModel;
import com.lytx.webservice.electrombile.model.BycleBlack;
import com.lytx.webservice.electrombile.model.BycleInfoShort;
import com.lytx.webservice.electrombile.model.BycleLostRecord;
import com.lytx.webservice.electrombile.model.BycleStationModel;
import com.lytx.webservice.electrombile.model.SoapDbNotify;
import com.lytx.webservice.electrombile.model.TmsSms;
import com.lytx.webservice.electrombile.model.TrackBycleShort;
import com.lytx.webservice.electrombile.model.TrackedBycle;
import com.lytx.webservice.sequence.service.SequenceGeneratorServiceUtil;
import com.lytx.webservice.util.DateUtil;

public class ElectrombilePersistence extends SqlSessionDaoSupport {
	private static Logger iLog = Logger.getLogger(ElectrombilePersistence.class);
	private SqlSessionTemplate sqlSession;

	public boolean addBycleAlarm(BycleAlarmModel model) {
		try {
			getSqlSession().insert("bycleAlarm_insert", model);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	public boolean addBycleTrackedRecord(BycleAlarmModel model) {
		try {
			getSqlSession().insert("bycleTrackedRecordModel_insert", model);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	public void batchinsertBycleAlarmList(List<BycleAlarmModel> alarmlist) {
		// getSqlSession().insert("bulkbycleAlarm_insert", alarmlist);

		org.apache.ibatis.session.SqlSession s = getSqlSession();
		for (BycleAlarmModel b : alarmlist) {
			s.insert("bycleAlarm_insert", b);
		}

	}

	public void batchinsertBycleTrackedRecordList(List<BycleAlarmModel> alarmlist) {
		org.apache.ibatis.session.SqlSession s = getSqlSession();
		for (BycleAlarmModel b : alarmlist) {
			s.insert("bycleTrackedRecordModel_insert", b);
		}

	}

	public BycleStationModel fineOneBycleStationModel(String code) {
		Map map = new HashMap();
		if (code == null) {
			code = "";
		}
		String[] ids = code.split("-");
		if (ids.length > 1) {
			map.put("FDCHANNEL", ids[1]);
			map.put("ID", ids[0]);
		} else {
			map.put("FDCHANNEL", 0);
			map.put("ID", ids[0]);
		}
		return (BycleStationModel) getSqlSession().selectOne("bycleStation_findone", map);

	}

	public BycleInfoShort getBycleInfoShort(String fdId) {
		Map map = new HashMap();
		map.put("FDID", fdId);
		return (BycleInfoShort) getSqlSession().selectOne("bycleInfoShort_findone", map);
	}

	public List<TrackBycleShort> getTrackBycleShortByRownum(int start, int rowspan) {
		List<TrackBycleShort> list = null;
		HashMap parameter = new HashMap();
		parameter.put("STARTROW", start);
		parameter.put("ENDROW", start + rowspan);
		list = getSqlSession().selectList("queryTrackBycleShortByRownum", parameter);
		return list;

	}

	public TrackBycleShort getTrackBycleShort(String fdId) {
		Map map = new HashMap();
		map.put("FDID", fdId);
		return (TrackBycleShort) getSqlSession().selectOne("queryTrackBycleShort_findone", map);
	}

	public TrackedBycle getTrackedBycleByRuleId(String ruleId) {
		return (TrackedBycle) getSqlSession().selectOne("TrackedBycle_load", ruleId);
	}

	public boolean findBycleWhite(BycleAlarmModel model) {
		boolean ret = false;
		try {
			try {
				Map map = new HashMap();
				map.put("FDID", model.getFdId());
				Object o = getSqlSession().selectOne("findByclewhite_findone", map);
				if (o != null) {
					ret = true;
				}
			} catch (Exception ex) {
				// return null;
			}
		} catch (Exception ex) {
			return false;
		}
		return ret;
	}

	public boolean addBycleHandleAlarm(BycleAlarmModel model) {
		try {
			BycleAlarmModel s = findBycleHandleAlarm(model.getFdId(), null, null);
			if (s == null) {
				getSqlSession().insert("bycleHandle_insert", model);
			} else if (model.getType() == 1 && s.getType() == 0) {

			}
		} catch (Exception ex) {
			return false;
		}
		return true;
	}
	
	public boolean addbycleAlarmPreDeal(BycleAlarmModel model) {
		try {
			BycleAlarmModel s = findBycleAlarmPreDeal(model.getFdId(), null, null);
			if (s == null) {
				getSqlSession().insert("BycleAlarmPreDeal_insert", model);
			} else if (model.getType() == 1 && s.getType() == 0) {

			}
		} catch (Exception ex) {
			return false;
		}
		return true;
	}
	
	public BycleAlarmModel findBycleAlarmPreDeal(String fdId, String startTime, String endTime) {
		BycleAlarmModel bycleInfoShort = null;
		try {
			Map map = new HashMap();
			map.put("FDID", fdId);
			bycleInfoShort = getSqlSession().selectOne("queryBycleAlarmPreDeal", map);
			// return bycleInfoShort;
		} catch (Exception ex) {
			// return null;
		}
		return bycleInfoShort;
	}
	

	public void updatedBycleHandleAlarm(long alarmId) {
		HashMap parameter = new HashMap();
		parameter.put("ALARMID", alarmId);
		parameter.put("TYPE", 1);
		getSqlSession().delete("updatedBycleHandleAlarm_type", parameter);

	}

	public void deletecudbnotifyByType(int type) {
		getSqlSession().delete("deleteDbNotifyAllBytype", type);
	}

	public void deleteDbNotify(String key, int type) {
		HashMap parameter = new HashMap();
		parameter.put("KEY", key);
		parameter.put("TYPE", type);
		getSqlSession().delete("deleteDbNotifyByType", parameter);

	}

	public List<SoapDbNotify> queryTrackeddbNotify(int type) {
		return getSqlSession().selectList("queryTrackeddbNotifyBytype", type);
	}

	public BycleAlarmModel getbycleTrackedHandle(BycleAlarmModel model) {
		Map map = new HashMap();
		map.put("FDID", model.getFdId());
		return (BycleAlarmModel) getSqlSession().selectOne("querybycleTrackedHandle_findone", map);
	}

	public BycleAlarmModel findBycleHandleAlarm(String fdId, String startTime, String endTime) {
		BycleAlarmModel bycleInfoShort = null;
		try {
			Map map = new HashMap();
			map.put("FDID", fdId);
			bycleInfoShort = getSqlSession().selectOne("querybycleHandleBytime", map);
			// return bycleInfoShort;
		} catch (Exception ex) {
			// return null;
		}
		return bycleInfoShort;
	}

	public BycleAlarmModel findBycleBlack(String fdId) {
		BycleAlarmModel bycleInfoShort = null;
		try {
			Map map = new HashMap();
			map.put("FDID", fdId);
			bycleInfoShort = getSqlSession().selectOne("queryBycleBlack", map);
			// return bycleInfoShort;
		} catch (Exception ex) {
			// return null;
		}
		return bycleInfoShort;
	}

	public boolean addbycleStationTracked(BycleAlarmModel model) {
		try {
			Map map = new HashMap();
			map.put("FDID", model.getFdId());
			map.put("AREAID", model.getAreaId());
			Object object = getSqlSession().selectOne("bycleStationTracked_finenoe", map);
			if (object == null) {
				getSqlSession().insert("bycleStationTracked_insert", model);
			}
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	public String getCaseIdNext(String areaid) {
		String caseId = "";
		try {

			Map param = new HashMap<String, String>();
			param.put("areaid", areaid);
			// param.put("maxvalue", 0);
			param.put("caseid", "");
			// param.put("outpar", 0);
			getSqlSession().selectOne("get_caseId", param);
			caseId = String.valueOf(param.get("caseid"));
		} catch (Exception sqle) {
			System.out.print(sqle.toString());
		}
		/*
		 * seq.maxValue = max; seq.value = max - (cacheSize - 1) * interval;
		 * seq.interval = interval; // seq.count = cacheSize; // seq.pos = 1;
		 */
		return caseId;
	}

	public boolean addBycleBlack(BycleBlack b, BycleLostRecord lost) {

		BycleAlarmModel s = findBycleBlack(b.getFdId());
		if (s == null) {
			getSqlSession().insert("bycleBlack_insert", b);
			// getSqlSession().insert("bycleLostRecord_insert", lost);
		} else {
			b.setCaseId(s.getCaseId());
			b.setRuleId(s.getRuleId());
		}
		return true;
	}

	public TmsSms findTmsSms(BycleAlarmModel m) {
		TmsSms bycleInfoShort = null;
		try {
			Map map = new HashMap();
			map.put("FDID", m.getFdId());
			bycleInfoShort = getSqlSession().selectOne("tmsSmsFindOne", map);
			// return bycleInfoShort;
		} catch (Exception ex) {
			// return null;
		}
		return bycleInfoShort;
	}

	public boolean addToTmsSms(TmsSms t, boolean update) {
		if (t.getMobilePhone() != null && !t.getMobilePhone().equals("")) {
			if (update) {
				getSqlSession().insert("tmsSms_update", t);
			} else {
				getSqlSession().insert("tmsSms_insert", t);
			}
			getSqlSession().delete("tmsSms_delete", t);
		}

		return true;
	}
}
