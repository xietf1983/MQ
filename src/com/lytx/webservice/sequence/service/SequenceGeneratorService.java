package com.lytx.webservice.sequence.service;

import java.sql.SQLException;
import java.util.Date;

import com.lytx.webservice.sequence.model.SeqObject;

public interface SequenceGeneratorService {
	/**
	 * ��ȡ�¸�����ֵ
	 * 
	 * @return
	 */
	public long getSequenceNext(String seqName) throws SQLException;

	
}
