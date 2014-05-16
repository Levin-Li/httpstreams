/*javascript document*/
$(document).ready(
    function() {
      // 调整内容居中的函数
      function adjustHeight(ele) {
        var win, doc, main, blankHeight;
        win = $(window).height();
        doc = $(document).height();
        main = $(".main").innerHeight();
        win > main ? $(ele).height(doc) : $(ele).height(main);
        // 占位的高度
        blankHeight = ($(ele).height() - main) / 2 - 120;
        // 判断占位是否大于0
        if (blankHeight < 0) {
          return;
        } else {
          $(".m_blank_area").height(blankHeight);
        }
        ;
        $(".bj_img").css({
          height : "auto",
          width : $(window).width()
        });
      }
      ;
      // 自我执行
      adjustHeight(".contain");
      // 为浏览器绑定事件
      $(window).bind("scroll resize", function() {
        adjustHeight(".contain");
      });
      // 三个大按钮的事件绑定
      $(".user_link  a ").click(function() {
        $(this).addClass("current").siblings().removeClass("current");
        // 判断是main_m 是否显示
        var index = $(this).index();
        switch (index) {
        case 0:
          if ($(".main_m").is(":hidden")) {
            $(".main_m").slideDown(250);
            $(".m_blank_area").slideUp(250);
          }
          ;
          $("#column_01").show().siblings().hide();

          break;
        case 1:
          $(".content").animate({
            "marginTop" : -$(".contain").height()
          }, 300, function() {
            $(this).hide();
          });
          $("#content2").show();
          break;
        case 2:
          if ($(".main_m").is(":hidden")) {
            $(".main_m").slideDown(250);
            $(".m_blank_area").slideUp(250);
          }
          ;
          $("#column_03").show().siblings().hide();
          break;
        default:
          return;
        }
      });
      // 隐藏内容区域
      $(document).click(
          function(e) {
            if (e.target.className == "bj_img"
                || e.target.className == "main_t"
                || e.target.className == "m_blank_area") {
              $(".main_m").slideUp(250);
              $(".m_blank_area").slideDown(250);
            }
          });
      // 控制白色箭头的位置
      $(".user_link .a_bespoke").click(function() {
        adjustArrow(385);
      });
      $(".user_link .a_load").click(function() {
        adjustArrow(125);
      });
      $(".user_link .a_check").click(function() {
        adjustArrow(670);
      });
      // 调整箭头位置的函数
      function adjustArrow(num) {
        $(".column_01  .arrow").animate({
          left : num
        }, 400);
      }
      // 返回登陆页面
      $("#load3").click(function() {

        // main_m 隐藏
        $(".main_m").hide();
        // 占位显示
        $(".m_blank_area").show();
        // a移除状态
        $(".user_link a").removeClass("current");
        $(".content").show().animate({
          "marginTop" : 0
        }, 300, function() {
          $("#content2").hide();
        });
      });
      // 控制预约号码登录还是直接登录
      $(".jf_load_show .a_btn_orange").click(function() {
        showEle(".yy_load_show");
      });
      $(".yy_load_show .a_btn_blue").click(function() {
        showEle(".jf_load_show");
      });
      // 显示元素的函数
      function showEle(ele) {
        $(ele).show().siblings().hide();
        $(".arrow").show();
      }
      ;
     
      // 给 table_01 的first-child 增加className
      $(".table_01 tr td:first-child").addClass("col_first");

      // 模拟多选框
      $(".sp_get_msg").click(function() {
        if ($(this).hasClass("sp_get_msg_selected")) {
          $(this).removeClass("sp_get_msg_selected");
        } else {
          $(this).addClass("sp_get_msg_selected")
        }
        ;
      });
      // 点击小叉叉移除元素
      $(".td_link  .sp_close").click(function() {
        $(this).parent().remove();
      });
      // 点击小叉叉移除元素
      $(".layout1  .sp_close").click(function() {
        $(this).parent().remove();
      });
    });