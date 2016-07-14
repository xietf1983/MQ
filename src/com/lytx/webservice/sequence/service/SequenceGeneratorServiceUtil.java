package com.lytx.webservice.sequence.service;

import java.sql.SQLException;
import java.util.Date;

import com.lytx.webservice.sequence.model.SeqObject;

public class SequenceGeneratorServiceUtil {
	public static String bycleseqName = "byclealarmid";
	public static String commonseq = "commonseq";
	public static long getSequenceNext(String seqName) {

		long nextvalue = 0;
		try {
			nextvalue = getService().getSequenceNext(seqName);
		} catch (SQLException ex) {
		}
		return nextvalue;
	}

	private static SequenceGeneratorService _service;

	public static SequenceGeneratorService getService() {
		if (_service == null) {
			throw new RuntimeException("SequenceGeneratorService is not set");
		}

		return _service;
	}

	public void setService(SequenceGeneratorService service) {
		_service = service;
	}

}