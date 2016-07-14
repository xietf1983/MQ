package com.lytx.webservice.dbnotify.service.impl;

import java.util.List;

import com.lytx.webservice.dbnotify.model.DbnotifyModel;
import com.lytx.webservice.dbnotify.service.DbnotifyService;
import com.lytx.webservice.dbnotify.service.persistence.DbnotifyPersistence;

public class DbnotifyServiceImpl implements DbnotifyService {
	private DbnotifyPersistence persistence;

	public DbnotifyPersistence getPersistence() {
		return persistence;
	}

	public void setPersistence(DbnotifyPersistence persistence) {
		this.persistence = persistence;
	}

	@Override
	public List<DbnotifyModel> getDbnotifyModelList(int type) {
		return getPersistence().getDbnotifyModelList(type);
	}

	@Override
	public boolean deleteDbnotifyModel(String id, int type) {
		return getPersistence().deleteDbnotifyModel(id, type);
	}

}
