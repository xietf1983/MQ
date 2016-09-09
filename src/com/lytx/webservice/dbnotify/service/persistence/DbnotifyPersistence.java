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
	 * �������ͻ�ȡ֪ͨ
	 * 
	 * @param type
	 * @return
	 */
	public List<DbnotifyModel> getDbnotifyModelList(int type) {
		return getSqlSession().selectList("queryTrackeddbNotify", type);
	}

	/**
	 * ɾ��֪ͨ
	 * 
	 * @param type
	 * @return
	 */
	public boolean deleteDbnotifyModel(String id, int type) {
		Map map = new HashMap();
		try {
			map.put("TYPE", type);
			if (id != null && !id.equals("")) {
				map.put("KEY", id);
			}
			getSqlSession().delete("deleteDbNotify", map);
		} catch (Exception ex) {
			iLog.error("ɾ����Ϣʧ��"+ex.toString());
			return false;
		}
		return true;
	}
}
