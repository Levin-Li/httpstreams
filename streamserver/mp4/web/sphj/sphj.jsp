<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="u" uri="http://www.thunisoft.com/sphj/common-utils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en-US">
<head>
	<title><u:outCorpName /></title>
	<link rel="stylesheet" type="text/css" href="<c:url value='/pub/css/reset.css'/>" media="all" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/pub/css/global.css'/>" media="all" />	
	<link rel="stylesheet" type="text/css" href="<c:url value='/pub/css/common.css'/>" media="all" />
	
	<script type="text/javascript" src="<c:url value='/sphj/swfobject.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/pub/js/detect.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/sphj/rm.js'/>"></script>
	<script type="text/javascript">
            var swfVersionStr = "11.1.0";
            var xiSwfUrlStr = "playerProductInstall.swf";
            var flashvars = {};
            var params = {};
            params.quality = "high";
            params.bgcolor = "#ffffff";
            params.allowscriptaccess = "sameDomain";
            params.allowfullscreen = "true";
            var attributes = {};
            attributes.id = "rm";
            attributes.name = "rm";
            attributes.align = "middle";
            swfobject.embedSWF(
                "<c:url value='/sphj/rm.swf'/>", "flashContent", 
                "640", "480", 
                swfVersionStr, xiSwfUrlStr, 
                flashvars, params, attributes);
            // JavaScript enabled so display the flashContent div in case it is not replaced with a swf object.
            swfobject.createCSS("#flashContent", "display:block;text-align:left;");
    </script>
</head>
<body>
	<div class="contain">
			<div class="header2">
				<a href="javascript:void(0);" class="a_logo">浙江省瑞安市人民检察院</a>
				<a href="index.html" class="a_exit">退出</a>
			</div>
			<!-- header2 end -->
			<div class="main2">
				<div class="layout1">
					<div class="layer">
						<a href="javascript:void(0);" class="a_btn_01"><em class="em_03">编辑信息</em></a>
						<a href="javascript:void(0);" class="a_btn_01"><em class="em_04">添加附件</em></a>
						<span>
							<a href="javascript:void(0);" class="a_fj">附件1.doc</a>
							<span class="sp_close"></span>
						</span>
						<span>
							<a href="javascript:void(0);" class="a_fj">附件2.doc</a>
							<span class="sp_close"></span>
						</span>
					</div>
				</div>
				
				<div id="logs"></div>
				<!-- layout1 end -->
				<div class="layout2">
					<h2 class="h2_02"><span class="sp_01">接访中</span></h2>
					<div class="video">
						<h3 class="h3_01">
							<span class="sp_l">上访人：李四&nbsp;&nbsp;&nbsp;&nbsp;接访人：张三</span>
							<span class="sp_r">12:10  20:20</span>
						</h3>
						<div class="video_box">
							<div id="flashContent">
								<p>
									本系统需要Adobe Flash Player 11.1.0及以上版本支持，请根据弹出的提示框下载安装，
									如未弹出请手工点击Adobe Flash Player下载安装。安装完成后需要重启浏览器。
								</p>
								<script>
								var downFlashUrl;
								if (isIE) {
									downFlashUrl = "<c:url value='/flash/flashplayer_activex_13_0_0_214.1400033744.exe'/>";
								} else {
									downFlashUrl = "<c:url value='/flash/install_flash_player_13_plugin-notie-13.0.0.214.1400038407.exe'/>";
								}
								document.write("<a href=\"" + downFlashUrl + "\">");
								document.write("点击下载Flash Player");
								document.write("</a>");
								</script>
							</div>
						</div>
					</div>
					<!-- video end -->
				</div>
				<!-- layout2 end -->
			</div>
			<!-- main2 end -->
			
			<div class="footer">
				<div class="footer_in">
					<p>技术支持&nbsp;&nbsp;&nbsp;&nbsp;北京华宇信息技术有限公司</p>
				</div>
			</div>
			<!-- footer end -->	
	</div>
	<!-- contain end -->
	<script type="text/javascript" src="<c:url value='/pub/js/jquery-1.8.2.min.js'/>"></script>	
	<script type="text/javascript" src="<c:url value='/pub/js/global.js'/>"></script>
</body>
</html>