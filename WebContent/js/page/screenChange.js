    // 全屏动作
    function fullScreen(){
        $("#sidebar").hide();
        $("header").hide();
        $("#container").find(".removeble").removeClass("main-content");
        $("#container").find(".breadcrumb").hide();
        // $("#container").find(".searchBar").hide();
        $(".monitor_content").css("margin-top","0");
        $("#monitorPainter").css('height',$(window).height());
        $("#monitorPainter").css('width',$(window).width());
        $(".main-wrap").css({"top":"0px","left":"0px"});
        $(".main-body").css("padding","3%");

        // $(".result_left").hide();
        //$(".result_right").css("width",$(window).width());
        $(".result_left").css("width","175px");
        $(".searchtext").css("width","100px");
        $(".result_right").css("width","85%");




    }

    // 屏幕恢复
    function minScreen(){
        $("#sidebar").show();
        $("header").show();
        $("#container").find(".removeble").addClass("main-content");
        $("#container").find(".breadcrumb").show();
        $("#container").find(".searchBar").show();

        $(".main-wrap").css({"top":"40px","left":"15px"});
        $(".main-body").css("padding","15px 15px 15px 15px");
        $(".monitor_content").css("margin-top","20px");
        $(".result_left").css("width","220px");
        $(".searchtext").css("width","143px");
        $(".result_right").width($(".main-body:last").width()-220);
        // $(".result_left").show();
        /* var right_width;
        if(document.body.clientWidth>767){
             right_width=$(".main-body:last").width()-250;  
        } else{
             right_width= document.body.clientWidth-30; 
        }
        $(".result_right").width(right_width); */
    }