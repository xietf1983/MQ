package com.lytx.webservice.sequence.service.persistence;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.mybatis.spring.support.SqlSessionDaoSupport;

public class SequenceGenerator extends SqlSessionDaoSupport {
	static private final Logger log = Logger.getLogger(SequenceGenerator.class);
	private static ConcurrentHashMap<String, Seq> seqCache = new ConcurrentHashMap<String, Seq>();
	private static byte[] mutex = new byte[0];

	/**
	 * 根据传入的关键字取序号
	 * 
	 * @param sSeqName
	 * @return 序号
	 * @exception SQLException
	 */
	public long getSequenceNext(String seqName) throws SQLException {
		return getSequenceNext(seqName, (short) 1000);
	}

	public long getSequenceNext(String seqName, short cacheSize) {
		synchronized (mutex) {
			Seq seq;
			seq = seqCache.get(seqName);
			if (seq == null) {
				seq = seqCache.get(seqName);
				if (seq == null) {
					seq = new Seq();
					seqCache.put(seqName, seq);
				}
			}
			seq.value += seq.interval;
			if (seq.value <= seq.maxValue) {
				return seq.value;
			} else {
				return getSequenceNext(seqName, seq, cacheSize);
			}
		}

	}

	private long getSequenceNext(String sSeqName, Seq seq, short cacheSize) {
		if (cacheSize < 1) {
			throw new java.lang.IllegalArgumentException("批读取的序列号个数不能小于1");
		}

		long max;
		short interval;

		try {

			Map param = new HashMap<String, String>();
			param.put("seqname", sSeqName);
			//param.put("maxvalue", 0);
			param.put("cacheSize", cacheSize);
			//param.put("outpar", 0);
			getSqlSession().selectOne("get_getsequence", param);
			max =Long.parseLong(String.valueOf( param.get("maxvalue")));
			interval=Short.parseShort(String.valueOf( param.get("outpar")));
			//interval = cstmt.getShort(3);
			seq.maxValue = max;
			seq.value = max - (cacheSize - 1) * interval;
			seq.interval = interval;
			int a= 0;
		} catch (Exception sqle) {
			System.out.print(sqle.toString());
		}
		/*
		 * seq.maxValue = max; seq.value = max - (cacheSize - 1) * interval;
		 * seq.interval = interval; // seq.count = cacheSize; // seq.pos = 1;
		 */
		return seq.value;
	}

	private static class Seq {
		// int count;
		// int pos;
		long maxValue;
		int interval = 1;
		long value;
	}
}
