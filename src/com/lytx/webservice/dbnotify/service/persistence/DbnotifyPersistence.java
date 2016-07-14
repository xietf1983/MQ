package com.lytx.webservice.dbnotify.service.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.lytx.webservice.dbnotify.model.DbnotifyModel;

public class DbnotifyPersistence extends SqlSessionDaoSupport {
	private static Logger iLog = Logger.getLogger(DbnotifyPersistence.class);

	/**
	 * 根据类型获取通知
	 * 
	 * @param type
	 * @return
	 */
	public List<DbnotifyModel> getDbnotifyModelList(int type) {
		return getSqlSession().selectList("queryTrackeddbNotify", type);
	}

	/**
	 * 删除通知
	 * 
	 * @param type
	 * @return
	 */
	public boolean deleteDbnotifyModel(String id, int type) {
		Map map = new HashMap();
		try {
			map.put("TYPE", type);
			map.put("KEY", id);
			getSqlSession().delete("deleteDbNotify", map);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}
}
