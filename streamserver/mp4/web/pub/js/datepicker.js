// JavaScript Document
function HS_DateAdd(interval,number,date){
    number = parseInt(number);
    if (typeof(date)=="string"){var date = new Date(date.split("-")[0],date.split("-")[1],date.split("-")[2])}
    if (typeof(date)=="object"){var date = date}
    switch(interval){
    case "y":return new Date(date.getFullYear()+number,date.getMonth(),date.getDate()); break;
    case "m":return new Date(date.getFullYear(),date.getMonth()+number,checkDate(date.getFullYear(),date.getMonth()+number,date.getDate())); break;
    case "d":return new Date(date.getFullYear(),date.getMonth(),date.getDate()+number); break;
    case "w":return new Date(date.getFullYear(),date.getMonth(),7*number+date.getDate()); break;
    }
}
function checkDate(year,month,date){
    var enddate = ["31","28","31","30","31","30","31","31","30","31","30","31"];
    var returnDate = "";
    if (year%4==0){enddate[1]="29"}
    if (date>enddate[month]){returnDate = enddate[month]}else{returnDate = date}
    return returnDate;
}//欢迎来到站长特效网，我们的网址是www.zzjs.net，很好记，zz站长，js就是js特效，本站收集大量高质量js代码，还有许多广告代码下载。
function WeekDay(date){
    var theDate;
    if (typeof(date)=="string"){theDate = new Date(date.split("-")[0],date.split("-")[1],date.split("-")[2]);}
    if (typeof(date)=="object"){theDate = date}
    return theDate.getDay();
}
function HS_calender(){
    var lis = "";
    var style = "";
    /*可以把下面的css剪切出去独立一个css文件zzjs.net*/
    style +="<style type='text/css'>";
    style +=".calender { width:218px; height:auto; font-size:12px; margin-right:14px; background:url(images/date_bg.gif) repeat-x 0 0 #f9f9f9; border:1px solid #acb8c4; }";
    style +=".calender ul {list-style-type:none; margin:0; padding:0;}";
    style +=".calender .day {  height:19px;line-height:19px;font-size:11px; border-bottom:1px solid #acb9c1;}";
    style +=".calender .day li { float:left; width:14%; height:19px; line-height:19px; text-align:center}";
	style +=".calender .date li{ float:left;border-right:1px solid #e5e5e5;border-bottom:1px solid #e5e5e5; width:30px; height:29px; line-height:29px;text-align:center}";
    style +=".calender li a { text-decoration:none; font-family:Tahoma; font-size:11px; color:#2b2b2b}";
    style +=".calender li a:hover { color:#2b2b2b; text-decoration:none}";
	style +=".calender li a.today { text-decoration:none; font-family:Tahoma; font-size:11px; color:#fff}";
    style +=".calender li a:hover.today { color:#ffffff; text-decoration:none}";
    style +=".calender li a.hasArticle {font-weight:bold; color:#f60 !important}";
    style +=".lastMonthDate, .nextMonthDate {color:#bbb;font-size:11px}";
    style +=".selectThisYear a, .selectThisMonth a{text-decoration:none; margin:0 2px; color:#000; font-weight:bold}";
    style +=".calender .LastMonth, .calender .NextMonth{ text-decoration:none; color:#000; font-size:18px; font-weight:bold; line-height:16px;}";
    style +=".calender .LastMonth { float:left;margin:5px 0px 0px 10px;display:inline;}";
    style +=".calender .NextMonth { float:right;margin:5px 10px 0px 0px;display:inline;}";
    style +=".calenderBody {clear:both}";
    style +=".calenderTitle {text-align:center;height:19px;font-family='宋体';letter-spacing:2px; line-height:19px; clear:both}";
    style +=".today { width:100%;height:29px;line-height:29px;float:left;text-align:center;background:url(images/hover.jpg) no-repeat 0 0; }";
    style +=".today a { color:#ffffff; }";
    style +=".calenderBottom {clear:both; padding: 3px 0; text-align:left}";
    style +=".calenderBottom a {text-decoration:none; margin:2px !important; font-weight:bold; color:#000}";
    style +=".calenderBottom a.closeCalender{float:right}";
    style +=".closeCalenderBox {float:right; border:1px solid #000; background:#fff; font-size:9px; width:11px; height:11px; line-height:11px; text-align:center;overflow:hidden; font-weight:normal !important}";
    style +="</style>";
    var now;
    if (typeof(arguments[0])=="string"){
        selectDate = arguments[0].split("-");
        var year = selectDate[0];
        var month = parseInt(selectDate[1])-1+"";
        var date = selectDate[2];
        now = new Date(year,month,date);
    }else if (typeof(arguments[0])=="object"){
        now = arguments[0];
    }
    var lastMonthEndDate = HS_DateAdd("d","-1",now.getFullYear()+"-"+now.getMonth()+"-01").getDate();
    var lastMonthDate = WeekDay(now.getFullYear()+"-"+now.getMonth()+"-01");
    var thisMonthLastDate = HS_DateAdd("d","-1",now.getFullYear()+"-"+(parseInt(now.getMonth())+1).toString()+"-01");
    var thisMonthEndDate = thisMonthLastDate.getDate();
    var thisMonthEndDay = thisMonthLastDate.getDay();
    var todayObj = new Date();
    today = todayObj.getFullYear()+"-"+todayObj.getMonth()+"-"+todayObj.getDate();
    for (i=0; i<lastMonthDate; i++){  // Last Month's Date
        lis = "<li class='lastMonthDate'>"+lastMonthEndDate+"</li>" + lis;
        lastMonthEndDate--;
    }
    for (i=1; i<=thisMonthEndDate; i++){ // Current Month's Date
        if(today == now.getFullYear()+"-"+now.getMonth()+"-"+i){
            var todayString = now.getFullYear()+"-"+(parseInt(now.getMonth())+1).toString()+"-"+i;
            lis += "<li><a href=javascript:void(0) class='today' onclick='_selectThisDay(this)' title='"+now.getFullYear()+"-"+(parseInt(now.getMonth())+1)+"-"+i+"'>"+i+"</a></li>";
        }else{
            lis += "<li><a href=javascript:void(0) onclick='_selectThisDay(this)' title='"+now.getFullYear()+"-"+(parseInt(now.getMonth())+1)+"-"+i+"'>"+i+"</a></li>";
        }
    }
    var j=1;
    for (i=thisMonthEndDay; i<6; i++){  // Next Month's Date
        lis += "<li class='nextMonthDate'>"+j+"</li>";
        j++;
    }
    lis += style;
    var CalenderTitle = "<a href='javascript:void(0)' class='NextMonth' onclick=HS_calender(HS_DateAdd('m',1,'"+now.getFullYear()+"-"+now.getMonth()+"-"+now.getDate()+"'),this) title='Next Month'><img src='images/right.jpg' /></a>";
    CalenderTitle += "<a href='javascript:void(0)' class='LastMonth' onclick=HS_calender(HS_DateAdd('m',-1,'"+now.getFullYear()+"-"+now.getMonth()+"-"+now.getDate()+"'),this) title='Previous Month'><img src='images/left.jpg' /></a>";
    CalenderTitle += "<span class='selectThisYear'><a href='javascript:void(0)' onclick='CalenderselectYear(this)' title='Click here to select other year' >"+now.getFullYear()+"</a></span>年<span class='selectThisMonth'><a href='javascript:void(0)' onclick='CalenderselectMonth(this)' title='Click here to select other month'>"+(parseInt(now.getMonth())+1).toString()+"</a></span>月";
    if (arguments.length>1){
        arguments[1].parentNode.parentNode.getElementsByTagName("ul")[1].innerHTML = lis;
        arguments[1].parentNode.innerHTML = CalenderTitle;
    }else{
        var CalenderBox = style+"<div class='calender'><div class='calenderTitle'>"+CalenderTitle+"</div><div class='calenderBody'><ul class='day'><li>日</li><li>一</li><li>二</li><li>三</li><li>四</li><li>五</li><li>六</li></ul><ul class='date' id='thisMonthDate'>"+lis+"</ul></div><div class='calenderBottom'><a href='javascript:void(0)' class='closeCalender' onclick='closeCalender(this)'>×</a><span><span><a href=javascript:void(0) onclick='_selectThisDay(this)' title='"+todayString+"'>今天 </a></span></span></div></div>";
        return CalenderBox;
    }
}
function _selectThisDay(d){
    var boxObj = d.parentNode.parentNode.parentNode.parentNode.parentNode;
        boxObj.targetObj.value = d.title;
        boxObj.parentNode.removeChild(boxObj);
}
function closeCalender(d){
    var boxObj = d.parentNode.parentNode.parentNode;
        boxObj.parentNode.removeChild(boxObj);
}//欢迎来到站长特效网，我们的网址是www.zzjs.net，很好记，zz站长，js就是js特效，本站收集大量高质量js代码，还有许多广告代码下载。
function CalenderselectYear(obj){
        var opt = "";
        var thisYear = obj.innerHTML;
        for (i=1970; i<=2020; i++){
            if (i==thisYear){
                opt += "<option value="+i+" selected>"+i+"</option>";
            }else{
                opt += "<option value="+i+">"+i+"</option>";
            }
        }
        opt = "<select onblur='selectThisYear(this)' onchange='selectThisYear(this)' style='font-size:11px'>"+opt+"</select>";
        obj.parentNode.innerHTML = opt;
}
function selectThisYear(obj){
    HS_calender(obj.value+"-"+obj.parentNode.parentNode.getElementsByTagName("span")[1].getElementsByTagName("a")[0].innerHTML+"-1",obj.parentNode);
}//欢迎来到站长特效网，我们的网址是www.zzjs.net，很好记，zz站长，js就是js特效，本站收集大量高质量js代码，还有许多广告代码下载。
function CalenderselectMonth(obj){
        var opt = "";
        var thisMonth = obj.innerHTML;
        for (i=1; i<=12; i++){
            if (i==thisMonth){
                opt += "<option value="+i+" selected>"+i+"</option>";
            }else{
                opt += "<option value="+i+">"+i+"</option>";
            }
        }
        opt = "<select onblur='selectThisMonth(this)' onchange='selectThisMonth(this)' style='font-size:11px'>"+opt+"</select>";
        obj.parentNode.innerHTML = opt;
}//欢迎来到站长特效网，我们的网址是www.zzjs.net，很好记，zz站长，js就是js特效，本站收集大量高质量js代码，还有许多广告代码下载。
function selectThisMonth(obj){
    HS_calender(obj.parentNode.parentNode.getElementsByTagName("span")[0].getElementsByTagName("a")[0].innerHTML+"-"+obj.value+"-1",obj.parentNode);
}
function HS_setDate(inputObj){
    var calenderObj = document.createElement("span");
    calenderObj.innerHTML = HS_calender(new Date());
    calenderObj.style.position = "absolute";
	calenderObj.id='bb';
    calenderObj.targetObj = inputObj;
    inputObj.parentNode.insertBefore(calenderObj,inputObj.nextSibling);
}
function none(){
    document.getElementById('bb').style.display='none';
}//欢迎来到站长特效网，我们的网址是www.zzjs.net，很好记，zz站长，js就是js特效，本站收集大量高质量js代码，还有许多广告代码下载