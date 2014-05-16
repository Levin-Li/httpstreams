<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.thunisoft.com/sphj/common-utils" prefix="u"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<!DOCTYPE HTML>
<html lang="en-US">
<head>
	<meta charset="UTF-8">
	<title>远程视频会见系统-<u:outCorpName /></title>
	<link rel="stylesheet" type="text/css" href="<c:url value='/pub/css/reset.css'/>" media="all" />
	<link rel="stylesheet" type="text/css" href="<c:url value='/pub/css/global.css'/>" media="all" />	
	<link rel="stylesheet" type="text/css" href="<c:url value='/pub/css/common.css'/>" media="all" />
	
	<%-- jquery 这种东西， 当然要放在第一行了 --%>
	<script type="text/javascript" src="<c:url value='/pub/js/jquery-1.8.2.min.js'/>" ></script>
</head>
<body>
	<div class="contain">
		<div class="content" style="display:;" id="content1">	
			<div class="div_bj_img">
				<img src="<c:url value='/pub/images/bj.jpg'/>" alt=""  style="width:100%;height:auto;" class="bj_img"/>
			</div>	
			<div class="main">
				<div class="m_blank_area"></div>
				<div class="main_t">
					<div class="logo"></div>
					<div class="user_link">
						<a href="javascript:void(0);"  class="a_load">接访登录</a>
						<a href="javascript:void(0);" class="a_bespoke">接访预约</a>
						<a href="javascript:void(0);" class="a_check">预约查询</a>
					</div>
				</div>
				<!-- main_t end -->
				<div class="main_m" style="display:none;">
					<div class="main_m_inner">
						<div class="column_01 " style="display:none;"  id="column_01">
							<div class="arrow"></div>
							<div class="jf_load_show" style="display:;">
								<div class="d_l">
									<p class="p_text">请输入验证码进入视频接访系统</p>
									<p class="p_yzm layer">
									<input type="text" name="" id="authCode"  class="inp_text_yzm" />
									<a href="javascript:void(0);"><img src="<c:url value='/yzm.jpg'/>" alt="" id="yzm-image"/></a>
									</p>
									
									<p class="p_btn layer"><a href="javascript:authCodeLogin(0);" class="a_load_blue">登&nbsp;&nbsp;&nbsp;录</a></p>
									
									
									<script type="text/javascript">
										// 生成验证码
										var yzmImg = $("#yzm-image");
										$("#yzm-image").click(function(){
										  $("#authCode").val("");
										  
										  var url = yzmImg.attr("src") ;
										  if (url.indexOf("?") > 0) {
										    url = url.substr(0, url.indexOf("?"));
										  }
										  yzmImg.attr("src", url + "?r=" + Math.random());
										});
										
										// 使用验证码登陆
										function authCodeLogin() {
										  $.ajax({
										    url:'<s:url namespace="/auth" action="login"/>',
											type:"POST",
											data: {
											  code:$("#authCode").val()
											}
										  }).done(function(data, textStatus, jqXHR) {
										     if (data.success) {
										       var sphjUrl = '<s:url namespace="/meet" action="sphj" />';
										   		
										   		window.location.href = sphjUrl;
										     } else {
										       yzmImg.click();
										     }
										  });
										}
										
										$("#authCode").keypress(function(e){
										  switch(e.which){
										  case 13 : //回车
										    authCodeLogin();
										   break;
										  }
										});
									</script>
								</div>

								<div class="d_r">
									<p class="p_btn layer"><a href="javascript:void(0);" class="a_btn_orange">预约码登录</a></p>
									<p class="p_text">没有预约码？</p>
									<p class="p_text"><a href="javascript:void(0);">接访预约</a></p>
								</div>
							</div>

							<!-- jf_load_show end -->
							<div class="yy_load_show" style="display:none;">
								<div class="d_l">
									<p class="p_text">请输入预约码进入视频接访系统</p>
									<p class="p_yzm layer"><input type="text" name="" id="login-appoint-code"  class="inp_text_yzm" /><font class="notice" style="display: none;">没有相关预约记录！</font></p>
									<p class="p_btn layer"><a href="javascript:appointCodeLogin(0);" class="a_load_orange">预约码登录</a></p>
								</div>
									
								<script type="text/javascript">
									// 预约码登录
									function appointCodeLogin() {
									  $(".yy_load_show .notice").hide();
									  
									  // 空数据不提交
									  var appointCode = $("#login-appoint-code").val();
									  if (appointCode.length > 0) {
										  $.ajax({
										    url:'<s:url namespace="/" action="appointLogin"/>',
											type:"POST",
											data: {
											  code:appointCode
											}
										  }).done(function(data, textStatus, jqXHR) {
										     if (data.success) {
										       var sphjUrl = '<s:url namespace="/meet" action="sphj" />';
										   		
										   		window.location.href = sphjUrl + "?xsId=" + data.xfxs.xsId + "&appointCode=" + data.xfxs.appointCode;
										     } else {
										       $(".yy_load_show .notice").show();
										     }
										  });
									  }
									}
									

									$("#login-appoint-code").keypress(function(e){
									  switch(e.which){
									  case 13 : //回车
									    appointCodeLogin();
									   break;
									  }
									});
								</script>
								
								<!-- d_l end -->
								<div class="d_r">
									<p class="p_btn  layer"><a href="javascript:void(0);" class="a_btn_blue">直接登录</a></p>
									<p class="p_text">直接填写验证码开始接访</p>
								</div>
								<!-- d_r end -->
								
								
							</div>
						<!-- yy_load_show end -->
						</div>
						<!-- column_01 end -->
						<div class="column_01 column_03 "  id="column_03" style="display:;">
							<div class="arrow"></div>	
							<div class="d_l">
								<p class="p_text">请输入预约码进入视频接访系统</p>
								<p class="p_yzm layer"><input type="text" name="" id="appoint-code"  class="inp_text_yzm" /></p>
								<p class="p_btn layer"><a href="javascript:queryAppointState();">查&nbsp;&nbsp;&nbsp;询</a></p>
							</div>

							<script type="text/javascript">
							 function queryAppointState() {
								// $("#check_going").show();
							    function onSuccess(state, decript) {
							      // 所有的都隐藏
							      $("#column_03 .d_r").hide();
							      
							      // 按条件显示
							      switch(state){
							      case 1: 
							        $("#column_03 #check_wait").show();
							        break;
							      case 2: 
							        $("#column_03 #check_going .time em").html("未实现！！");
							        $("#column_03 #check_going").show();
							        break;
							      case 4: 
							        $("#column_03 #check_failed").show();
							        break;
							      case 3: 
							      case 5:
							        onMsg(decript);
							        break;
							        
							      }
							 	}
							 	
							 	function onMsg(errors){

						    		$("#column_03 .d_r").hide();
						      
							 		$("#appoint-msg").html(errors ? errors : "未找到相关内容，请重试！");
							 		$("#check_others").show();
							 	}

								$.ajax({
								  url:'<s:url namespace="/" action="appointState"/>',
								  type:"POST",
								  data: {
								    appointCode:$("#appoint-code").val()
								  }								  
								}).done(function(data, textStatus, jqXHR){
									if (data.success) {
									  onSuccess(data.state, data.descript);
									} else {
									  onMsg(data.descript);
									}
								}).fail(function(){
								  onMsg();
								});
							  };
							</script>
							<!-- d_l end -->
							<div class="d_r  check_going"  style="display:none;"  id="check_going">
								<p class="p_bg"></p>
								<p class="p_text"><em>您的预约请求已通过申请!</em></p>
								<p class="p_text" class="time"><em>2014-7-2 13:00 ~ 14:30</em></p>
								<p class="p_text pt10">超过开始时间30分钟，视为过期!</p>
							</div>
							<!-- d_r end -->
							<div class="d_r  check_wait"  style="display:none;"  id="check_wait">
								<p class="p_bg"></p>
								<p class="p_text">您的预约请求正在审核中</p>
								<p class="p_text">请等待通知!!</p>
							</div>
							<!-- d_r end -->
							<div class="d_r check_failed"  style="display:none;"  id="check_failed">
								<p class="p_bg"></p>
								<p class="p_text">审核未通过，重新申请!!</p>
							</div>
							<!-- d_r end -->
							<div class="d_r check_failed"  style="display:none;"  id="check_others">
								<p class="p_bg"></p>
								<p class="p_text" id="appoint-msg">提示消息!!!</p>
							</div>
							<!-- d_r end -->
						</div>
						<!-- column_01 end -->
						</div>
					<!-- main_m_inner end -->
				</div>
				<!-- main_m end -->
				
			</div>
			<!-- main end -->
			<div class="main_b">
				<p><u:outCorpName />        技术支持:北京华宇信息技术有限公司</p>
			</div>
			<!-- main_b end -->
		</div>
		<!-- content end -->
		<div class="content2" style="display:none;"  id="content2">
			<div class="header">
				<div class="header_in">
					<h2 class="h2_01"><span class="sp_01">预约申请信息填写</span></h2>
					<span class="sp_load">远程视频会见系统<a href="javascript:void(0);"  id="load3">登录</a></span>
				</div>		
			</div>
			<!-- header end -->
			<jsp:include page="appoint.jsp">
				<jsp:param value="new" name="action"/>
			</jsp:include>
			
			
		</div>
		<!-- content2 end -->
		<div class="content2" style="display:none;" id="content3">
			<div class="header">
				<div class="header_in">
					<h2 class="h2_01"><span class="sp_01">预约申请信息填写</span></h2>
					<span class="sp_load">远程视频会见系统<a href="javascript:void(0);">登录</a></span>
				</div>		
			</div>
			<!-- header end -->
			<div class="middle layer">
				<div class="get_tel_msg">
					<p class="p_text1"><span class="sp_l">您获取的预约码为：</span><span class="sp_r"><em>5N76</em></span></p>
					<p class="p_text2"><span class="sp_l">您的预约信息为</span></p>
					<p class="p_text3"><span class="sp_l">手机号：</span> <span class="sp_r">15812345678</span></p>
					<p class="p_text4"><span class="sp_l">接访人：</span><span class="sp_r">张三</span></p>
				</div>
				<a href="javascript:void(0);" class="a_btn_04">修改预约信息</a>
			</div>
			<!-- middle end -->

		</div>
		<!-- content2 end -->
	</div>
	<!-- contain end -->
	
	
	
	<script type="text/javascript" src="<c:url value='/pub/js/datepicker.js"'/>"></script>
	<script type="text/javascript" src="<c:url value='/pub/js/addSelect.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/pub/js/global.js'/>"></script>
	<script type="text/javascript">
		$("#xzjfr").addSelect({
			triggerEleId:"sp_mousedown3",
			searchContentContain:".search_con_nav",	
			addHoverClass:"dd_hover",
			getValueClass:".sp_gain_value"
		});
		$("#sfsc").addSelect({
			triggerEleId:"sp_mousedown4",
			searchContentContain:".search_con_nav",	
			addHoverClass:"dd_hover",
			getValueClass:".sp_gain_value"
		});

	
	</script>
	
		
	
</body>
</html>