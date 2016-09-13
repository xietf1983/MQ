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
	String byetedata = "001908000002380c132d227e2b1700000901020385270a0000017a001908000001730c132d227e2b17000009000203845e15000002390019080000013e0c132d227e2b17000009000203853ce44000024b001908000001900c132d217e2b1700000904020384c0d9600001b0001908000000530c132d227e2b1700000901020384c9f8200002ca";
	//FileInputStream f = new FileInputStream("E:\\TOMCATSOA\\222.txt");
	//BufferedReader dr = new BufferedReader(new InputStreamReader(f));

	byte[] infodata = Test.hexStr2Bytes(byetedata);
	com.liveyc.rabbitmq.DealDataUtil.dealTrailData(infodata);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

</html>

