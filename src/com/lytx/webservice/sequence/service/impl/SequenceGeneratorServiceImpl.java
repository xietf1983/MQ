package com.lytx.webservice.sequence.service.impl;

import java.sql.SQLException;
import java.util.Date;

import com.lytx.webservice.sequence.model.SeqObject;
import com.lytx.webservice.sequence.service.SequenceGeneratorService;
import com.lytx.webservice.sequence.service.persistence.SequenceGenerator;


public class SequenceGeneratorServiceImpl implements SequenceGeneratorService {

	public long getSequenceNext(String seqName) throws SQLException {
		return getPersistence().getSequenceNext(seqName);
	}
	
	
	protected static SequenceGenerator _persistence;

	public SequenceGenerator getPersistence() {
		return _persistence;
	}

	public void setPersistence(SequenceGenerator persistence) {
		_persistence = persistence;
	}
}
