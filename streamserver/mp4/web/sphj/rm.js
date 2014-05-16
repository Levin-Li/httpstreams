// 修复document.getElementById
document.getElementById = (function(fn) {
	return function() {
		return fn.apply(document, arguments);
	};
})(document.getElementById);

document.onkeydown = onkey;
function onkey() {
	if ((window.event.ctrlKey) && (window.event.altKey) && (window.event.shiftKey) && (window.event.keyCode == 191)) {
		document.getElementById("logs").style.display = document.getElementById("logs").style.display == "block" ? "none" : "block";
	}
}

var video_params = {};
video_params.service = "rtmp://v/rm";
video_params.type = 0;//播放模式，0：1+1;1:画中画
video_params.stream0 = {
	id : "2",
	name : "张三"
};
video_params.stream1 = {
	id : "2",
	name : "李四"
};

video_params.micEncodeQuality = 10;
video_params.micRate = 44;
video_params.framesPerPacket = 5;
video_params.const_video_width = 320;
video_params.const_video_height = 240;
video_params.small_video_width = 200;
video_params.small_video_height = 200;
video_params.video_fps = 30;
video_params.video_quality = 90;

function OnFlashLoad(str) {
	document.getElementById("rm").FLog("rvms swf v3.0");
	var b = document.getElementById("rm").FSetParams(video_params);
	if (!b) {
		ErrorAlert('参数错误');
	}
	PublishLocal();
	PlayRemote();
}

function FLog(str) {
	document.getElementById("logs").innerHTML += str + "<br/>";
}

function ErrorAlert(str) {
	alert(str);
}

function PublishLocal() {
	document.getElementById("rm").FStartPublish();
}

function PlayRemote() {
	document.getElementById("rm").FStartPlay(0);
}

function ChangeMode() {
	document.getElementById("rm").FChangeMode();
}
function StartRecord() {
	document.getElementById("rm").FStartRecord();
}
function StopRecord() {
	document.getElementById("rm").FStopRecord();
}
function Disconnect() {
	document.getElementById("rm").FDisconnect();
}
