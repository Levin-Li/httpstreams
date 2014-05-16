/*javascript document*/

/*************************************************************************************************

addFocus		   start

 ************************************************************************************************/
;
(function ($, window) {
	//给jQuery添加方法
	$.fn.addFocus = function (options) {
		//合并传入与默认的参数
		var opts = $.extend({}, $.fn.addFocus.defaultOpts, options || {});
		//实现方法
		$(this).each(function () {
			var that = $(this);
			that.focus(function () {
				var value = that.val();
				if (value == opts.textContent) {
					that.val("").css("color", opts.textColor);
					if (opts.whetherAddClass) {
						that.addClass(opts.newClass);
					} else {
						return;
					}
				} else {
					if (opts.whetherAddClass) {
						that.addClass(opts.newClass);
					} else {
						return;
					}
				};
			}).blur(function () {
				var value = that.val();
				if (value == "") {
					that.val(opts.textContent).css("color", opts.defaultColor);
					if (opts.whetherAddClass) {
						that.removeClass(opts.newClass);
					} else {
						return;
					}
				} else {
					if (opts.whetherAddClass) {
						that.removeClass(opts.newClass);
					} else {
						return;
					}
				};
			}).trigger("blur");
		});
	};
	/*
	 *	默认参数
	 *	textContent						默认搜索框文本
	 *	defaultColor					默认的文本颜色值
	 *	textColor						输入后的文本颜色值
	 *	whetherAddClass					boolean值   true为允许允许在获取焦点时增加新class失去焦点移除新增的class
	 *	newClass 						class类名
	 */
	$.fn.addFocus.defaultOpts = {
		textContent : "输入文书关键词查找",
		defaultColor : "#bababa",
		textColor : "#333",
		whetherAddClass : false,
		newClass : ""
	};
})(jQuery, window);