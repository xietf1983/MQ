package com.lytx.webservice.dbnotify.service;

import java.util.List;

import com.lytx.webservice.dbnotify.model.DbnotifyModel;

public class DbnotifyServiceUtil {
	private static DbnotifyService _service;

	public static DbnotifyService getService() {
		if (_service == null) {
			throw new RuntimeException("DbnotifyService is not set");
		}
		return _service;
	}

	public void setService(DbnotifyService service) {
		_service = service;
	}

	public List<DbnotifyModel> getDbnotifyModelList(int type) {
		return getService().getDbnotifyModelList(type);

	}

	public boolean deleteDbnotifyModel(String id, int type) {
		return getService().deleteDbnotifyModel(id, type);
	}

}
