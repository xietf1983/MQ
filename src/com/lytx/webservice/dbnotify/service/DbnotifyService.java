package com.lytx.webservice.dbnotify.service;

import java.util.List;

import com.lytx.webservice.dbnotify.model.DbnotifyModel;

public interface DbnotifyService {
	public List<DbnotifyModel> getDbnotifyModelList(int type);

	public boolean deleteDbnotifyModel(String id, int type);
}
