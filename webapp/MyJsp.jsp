<%@page import="com.liveyc.mq.Test"%>
<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@page import="com.rabbitmq.client.Channel"%>
<%@page import="com.rabbitmq.client.Connection"%>
<%@page import="com.rabbitmq.client.ConnectionFactory"%>
<%@page import="com.lytx.webservice.batch.BycleTask"%>
<%@page import="com.lytx.webservice.electrombile.model.BycleAlarmModel"%>
<%@page import="com.lytx.webservice.electrombile.model.BycleInfoShort"%>
<%@page
	import="com.lytx.webservice.electrombile.model.BycleStationModel"%>
<%@page import="com.lytx.webservice.electrombile.model.TrackBycleShort"%>
<%@page
	import="com.lytx.webservice.electrombile.service.ElectrombileServiceUtil"%>
<%@page
	import="com.lytx.webservice.sequence.service.SequenceGeneratorServiceUtil"%>
<%@page import="com.liveyc.rabbitmq.DealDataUtil"%>
<%@page import="java.io.*"%>
<%
	org.apache.log4j.Logger iLog = org.apache.log4j.Logger.getLogger(com.liveyc.rabbitmq.DealDataUtil.class);
	Date now = new Date();
	try {
		BycleStationModel by = new BycleStationModel();
		by.setStationId("461");
		String dbcode = "461";
		by.setAreaId("331004");
		byte[] infodata = Test.hexStr2Bytes("01020385270aff0001");
		DealDataUtil.dealinfodata(infodata, dbcode, now, by, 1);
		//DealDataUtil.dealTrailData(infodata);
		Thread.sleep(10000);
	} catch (Exception ex) {
		iLog.error(ex.toString());
		try {
			Thread.sleep(5000);
		} catch (Exception exx) {
		}
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

</html>

