<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>
<%--
      登记预约记录
 --%>
<div class="middle layer">
	<s:token />
	<div class="main_aside  fl">
		<table class="table_01">
			<tr>
				<td class="col_first">姓 名</td>
				<td class="col_second"><input type="text" name="name" id="appoint-name"
					class="inp_text_01 " />
				</td>
				<td></td>
			</tr>
			<tr>
				<td>身份证号</td>
				<td><input type="text" name="cid" id="appoint-cid" class="inp_text_01" />
				</td>
				<td></td>
			</tr>
			<tr>
				<td><em>*</em>选择接访人</td>
				<td>
					<div class="search_menu" id="xzjfr">
						<div class="search_value_box">
							<span class="sp_mousedown" id="sp_mousedown3"></span> <span
								class="sp_gain_value" id="appoint-lenderId"></span>
						</div>
						<!-- search_value_box end -->
						<input type="hidden" name="lenderId" id="appoint-lenderId" value="" class="sp_gain_textfield"/>
						<dl class="search_con_nav" >
							<dt>接访人</dt>
							<dd value="1">张三</dd>
							<dd value="2">李四</dd>
							<dd value="3">王五</dd>
							<dd value="4">赵6</dd>
							<dd value="5">洪7</dd>
						</dl>
					</div> <!-- search_menu end --></td>
				<td></td>
			</tr>
			<tr>
				<td><em>*</em>手机号码</td>
				<td><input type="text" name="phone" id="appoint-phone" class="inp_text_01" />
				</td>
				<td><span class="sp_get_msg  sp_get_msg_selected" id="appoint-acceptPhoneMessage">接收短信通知</span>
				</td>
			</tr>
			<tr>
				<td><em>*</em>接访日期</td>
				<td><input type="text" name="appointmentDate"
					class="inp_text_01  inp_text_date" id="appoint-appointmentDate"  onfocus="HS_setDate(this)"
					id="datepicker" />
				</td>
				<td></td>
			</tr>
			<tr>
				<td>上访原因</td>
				<td><textarea name="descript" class="textarea_01" id="appoint-descript"></textarea></td>
				<td></td>
			</tr>
			<tr>
				<td>上访时长</td>
				<td>
					<div class="search_menu" id="sfsc">
						<div class="search_value_box">
							<span class="sp_mousedown" id="sp_mousedown4"></span> <span
								class="sp_gain_value" id="appoint-appointmentDate"></span>
						</div>
						<!-- search_value_box end -->
						<input type="hidden" name="timeLength" id="appoint-timeLength" value="" class="sp_gain_textfield"/>
						<dl class="search_con_nav">
							<dt>时长</dt>
							<dd value="1">1小时</dd>
							<dd value="2">2小时</dd>
							<dd value="3">3小时</dd>
							<dd value="4">4小时</dd>
							<dd value="5">5小时</dd>
						</dl>
					</div> <!-- search_menu end --></td>
				<td></td>
			</tr>
			<tr>
				<td>语音输入</td>
				<td><a href="javascript:void(0);" class="a_btn_01"><em
						class="em_01">开始录音</em> </a>
				</td>
				<td></td>
			</tr>
			<tr>
				<td>文件上传</td>
				<td><a href="javascript:void(0);" class="a_btn_01"><em
						class="em_02">选择文件</em> </a>
				</td>
				<td></td>
			</tr>

		</table>
	</div>
	<div class="sub_aside fr">
		<table class="table_02">
			<tr>
				<td colspan="2" class="pl30">注意事项</td>

			</tr>
			<tr>
				<td colspan="2" class="pl30">带<em>*</em>为必填选项</td>
			</tr>
			<tr>
				<td colspan="2"><span class="sp_fj">附件</span>
				</td>
			</tr>
			<tr>
				<td class="col_first">接访人日程：</td>
				<td class="td_link"><a href="javascript:void(0);">日程安排.doc</a>
				</td>
			</tr>
			<tr>
				<td class="col_first">录音：</td>
				<td class="td_link">
					<p>
						<a href="javascript:void(0);" class="">录音1.mp3</a><span
							class="sp_close"></span>
					</p>
					<p>
						<a href="javascript:void(0);">录音2.mp3</a><span class="sp_close"></span>
					</p>
					<p>
						<a href="javascript:void(0);">录音3.mp3</a><span class="sp_close"></span>
					</p></td>
			</tr>
			<tr>
				<td class="col_first">上传材料：</td>
				<td class="td_link">
					<p>
						<a href="javascript:void(0);">材料.wmv</a><span class="sp_close"></span>
					</p>
					<p>
						<a href="javascript:void(0);">上访资料.doc</a><span class="sp_close"></span>
					</p>
					<p>
						<a href="javascript:void(0);">录音.doc</a><span class="sp_close"></span>
					</p></td>
			</tr>
		</table>
	</div>

<!-- middle end -->
<div class="bottom">
	<div class="bottom_in layer">
		<a href="javascript:saveAppoint();" class="a_btn_02" id="saveBtn">确 定</a><a
			href="javascript:void(0);" class="a_btn_03" id="cancleBtn">取消</a>
	</div>
</div>


<script type="text/javascript">
function saveAppoint() {
	function onSuccess(xsId, queryCode) {
		var sphjUrl = '<s:url namespace="/meet" action="sphj" />';
		
		// alert("查询码:" + queryCode);
		// window.location.href = sphjUrl + "?xsId=" + xsId + "&queryCode=" + queryCode;
	}
	
	/**
	 * @params errors 
       {
			errorCode:[{reason1, reason2}],
			errorCode2:[{reason1, reason2}]
       }
	 */
	function onFail(errors){
		
	}
	
	$.ajax({
		url:'<s:url namespace="/" action="makeAppoint"/>',
		type: 'POST',
		data:{
			name:$("#appoint-name").val(),
			cid:$("#appoint-cid").val(),
			phone:$("#appoint-phone").val(),
			acceptPhoneMessage:$("#appoint-acceptPhoneMessage").hasClass("sp_get_msg_selected"),
			lenderId:$("#appoint-lenderId").val(),
			appointmentDate:$("#appoint-appointmentDate").val(),
			descript:$("#appoint-descript").val(),
			timeLength:$("#appoint-timeLength").val(),
		}
	}).done(function(data, textStatus, jqXHR){
		if (data.success) {
			onSuccess(data.xsId, data.cxm);
		} else {
			onFail(data.errors);
		}
	}).fail(function(){
		onFail({
			"connect":["connect.fail"]
		});
	});
}

</script>
