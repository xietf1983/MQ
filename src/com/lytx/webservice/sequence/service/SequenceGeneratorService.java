package com.lytx.webservice.sequence.service;

import java.sql.SQLException;
import java.util.Date;

import com.lytx.webservice.sequence.model.SeqObject;

public interface SequenceGeneratorService {
	/**
	 * 获取下个主键值
	 * 
	 * @return
	 */
	public long getSequenceNext(String seqName) throws SQLException;

	
}
