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
	String byetedata = "00190800000393070f2a217e2b1700000901020384de1640000277001908000003a5070f2a217e2b17000009010203848daa20000155001908000003cf070f2a217e2b17000009010203848526200002e0001908000004ce070f2a217e2b1700000904020384fb505000020d0019080000039a070f2a217e2b17000009000203855347200001f200190800000398070f2a217e2b1700000901020384e44e2000015a001908000003d3070f2a217e2b17000009010203849c7c40000253001908000003a5070f2a217e2b17000009000203848daa20000156001908000003c7070f2a217e2b1700000900020384f4d9000001e000190800000574070f2a217e2b1700000901020385a84d20000196";
	//FileInputStream f = new FileInputStream("E:\\TOMCATSOA\\222.txt");
	//BufferedReader dr = new BufferedReader(new InputStreamReader(f));

	byte[] infodata = Test.hexStr2Bytes(byetedata);
	com.liveyc.rabbitmq.DealDataUtil.dealTrailData(infodata);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

</html>

