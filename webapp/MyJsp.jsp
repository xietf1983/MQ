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
	String byetedata = "000000bd038444c4110b21360002";
	org.apache.commons.codec.binary.Hex.encodeHexString(DealDataUtil.intToByteArray(59002390l));
	System.out.print(org.apache.commons.codec.binary.Hex.encodeHexString(DealDataUtil.intToByteArray(59002390l)));
	if (byetedata != null) {
		byte[] infodata = Test.hexStr2Bytes(byetedata);
		DealDataUtil.dealStolen(infodata);
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

</html>
