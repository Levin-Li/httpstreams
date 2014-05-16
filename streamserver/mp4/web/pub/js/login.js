/*javascript document*/
$(document).ready(function(){
		//  调整内容居中的函数
		function  adjustHeight(ele){		
			var win,doc,main;
				win=$(window).height();
				doc=$(document).height();
				main=$(".login").innerHeight(); 
				win>main ? $(ele).height(doc): $(ele).height(main);
				$(".login").css({
						"top":(win-main)/2-40
					});
				$(".bj_img").css({
						height:"auto",
						width:$(window).width()
					});	
		};
		// 自我执行	
		adjustHeight(".contain");
		// 为浏览器绑定事件
		$(window).bind("scroll resize",function(){
				adjustHeight(".contain");
			});
});