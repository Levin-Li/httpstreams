<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>HTTP流媒体服务器 V1.0</title>
</head>

<body><h5>统一音视频平台——HTTP流媒体服务器 V1.0</h5>

1、普通点播：默认拦截 web 目录下的  MP4、flv 文件。
2、作为代理服务器，视频地址格式为： 'http://localhost:8080/streamer/' + encodeURI("http://localhost:8080/streamer/flv/kuiba-0001.flv") 
</body>
</html>
