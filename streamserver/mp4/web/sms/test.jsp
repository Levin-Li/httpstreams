<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="u" uri="http://www.thunisoft.com/sphj/common-utils"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="/pub/css/nec/reset.css" type="text/css" rel="stylesheet" />
<link href="/pub/css/nec/function.css" type="text/css" rel="stylesheet" />
<link href="/pub/css/nec/media.css" type="text/css" rel="stylesheet" />
<style type="text/css">
/* 两列右侧自适应布局 */
.g-bd1 {
	margin: 0 0 10px;
}

.g-sd1 {
	position: relative;
	float: left;
	width: 190px;
	margin-right: -190px;
}

.g-mn1 {
	float: right;
	width: 100%;
}

.g-mn1c {
	margin-left: 200px;
}

/* 下拉式菜单-默认左对齐 */
.u-menu {
	display: none;
	position: absolute;
	z-index: 100;
	top: 100%;
	left: 0;
	*zoom: 1;
	*width: 100px;
	margin: 1px 0 0;
	overflow: hidden;
	border: 1px solid #d0d0d0;
	border-width: 1px 0;
	line-height: 1.5;
	font-size: 12px;
	background: #fff;
}

.u-menu li {
	border: 1px solid #d0d0d0;
	border-width: 0 1px;
	*vertical-align: top;
}

.u-menu li a {
	display: block;
	*zoom: 1;
	padding: 7px 12px 6px;
	margin: -1px 0;
	border-top: 1px solid #e8e8e8;
	overflow: hidden;
	*vertical-align: top;
	word-wrap: normal;
	white-space: nowrap;
	_white-space: normal;
	text-overflow: ellipsis;
	color: #333;
}

.u-menu li a:hover {
	color: #333;
	background: #f7f7f7;
}

.u-menu li a:active {
	background: #f0f0f0;
}
/* 显示 */
.u-menu-show {
	display: block;
}
/* 分组 */
.u-menu-gp li.menusep,.u-menu-gp li.menusep .menuline {
	height: 0;
	line-height: 0;
	font-size: 0;
	overflow: hidden;
}

.u-menu-gp li.menusep {
	padding: 5px 0;
}

.u-menu-gp li.menusep .menuline {
	display: block;
	border: 0;
	border-top: 1px solid #e8e8e8;
}

.u-menu-gp li a {
	border: 0;
}
/* 宽度限制 */
.u-menu-min {
	min-width: 100%;
}

.u-menu-max {
	max-width: 100%;
}
/* 右对齐 */
.u-menu-rt {
	left: auto;
	right: 0;
}

/* 标题文本-默认大小继承 */
.u-tt {
	font-family: 'microsoft yahei', sans-serif;
}
/* 较小 */
.u-tt-sm {
	font-size: 12px;
}
/* 中等 */
.u-tt-md {
	font-size: 14px;
}
/* 较大 */
.u-tt-lg {
	font-size: 16px;
}
/* 很大 */
.u-tt-xl {
	font-size: 18px;
}

/* 含标题和Tab的模块头部-默认不定宽 */
.m-hd {
	height: 35px;
	line-height: 35px;
	padding: 1px 0 0;
	border-bottom: 1px solid #ddd;
}

.m-hd h2,.m-hd ul,.m-hd li {
	float: left;
	display: inline;
}

.m-hd h2 {
	margin: 0 20px 0 0;
	font-size: 16px;
}

.m-hd ul {
	margin-left: -2px;
}

.m-hd li {
	position: relative;
	margin: -1px 0 0 2px;
}

.m-hd li a,.m-hd li a:hover {
	text-decoration: none;
	color: #666;
}

.m-hd li a {
	float: left;
	padding: 0 15px;
	border: 1px solid #ddd;
	border-bottom: 0;
	text-align: center;
	font-size: 14px;
	background: #f8f8f8;
}

.m-hd li a:hover {
	background: #fff;
}

.m-hd li.z-crt a {
	position: relative;
	padding-bottom: 1px;
	margin-bottom: -1px;
	background: #fff;
}

.m-hd .more {
	float: right;
	margin-left: 10px;
}
/* 有背景 */
.m-hd-bg {
	padding: 11px 10px 0;
	background: #f7f7f7;
}
/* tab无缝 */
.m-hd-sl li {
	margin-left: -1px;
}

.m-hd-sl li a {
	border-radius: 0;
}
/* tab居右 */
.m-hd-rt h2 {
	margin-right: 0;
}

.m-hd-rt ul {
	float: right;
}
/* tab较小 */
.m-hd-sm {
	height: 25px;
	line-height: 25px;
}

.m-hd-sm h2 {
	font-size: 14px;
}

.m-hd-sm li a {
	padding: 0 10px;
	font-size: 12px;
}
/* tab定宽 */
.m-hd-fw li a {
	width: 78px;
	padding: 0;
}
</style>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>聊天室:<u:outCorpName /></title>
</head>
<body>

	<div class="g-bd1 f-cb">
		<div class="g-sd1 ">

			<h2 class="u-tt u-tt-md">上访人列表</h2>

			<ul class="u-menu u-menu-max u-menu-show">
			 <s:iterator value="friends" status="st">
				<li><a href="#"><s:property value="id"/></a></li>
			 </s:iterator>  
			</ul>

		</div>
		<div class="g-mn1">
			<div class="g-mn1c">

				<div class="m-hd">
					<h2 class="u-tt">会话</h2>
					<ul>
						<li class="z-crt"><a href="#">默认：居左，不定宽</a>
						</li>
						<li><a href="#">“标题”和“更多”可删</a>
						</li>
						<li><a href="#">扩展类可以自由组合</a>
						</li>
						<li><a href="#">标签标签</a>
						</li>
					</ul>
				</div>


			</div>
		</div>
	</div>

</body>
</html>