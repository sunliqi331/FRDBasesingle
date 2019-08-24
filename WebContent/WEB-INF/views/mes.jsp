<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
    <%@page import="com.its.common.utils.SecurityUtils"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%> 
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>PDM SYSTEM</title>
<meta name="keywords" content="量具,影像测量仪,测量机 " />
<meta name="description" content="无锡悦创智能科技有限公司专业从事量具,检具以及测量机的生产和销售，欢迎来电咨询. " />
<link rel="stylesheet" href="${contextPath}/styles/css/swiper.min.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/bootstrap.css" type="text/css">
<link href="${contextPath}/styles/assets/font-awesome/css/font-awesome.css" rel="stylesheet" />
<link rel="stylesheet" href="${contextPath}/styles/css/banner/css/default1.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/banner/css/sangarSlider.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/reset.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/index0.css" type="text/css">
<link href="${contextPath}/styles/img/favicon.ico" type="image/x-icon" rel="icon" />
<style type="text/css">  
    .swiper-slide {
      height:30% !important;
      text-align: center;
      font-size: 18px;
      background: #fff;

      /* Center slide text vertically */
      display: -webkit-box;
      display: -ms-flexbox;
      display: -webkit-flex;
      display: flex;
      -webkit-box-pack: center;
      -ms-flex-pack: center;
      -webkit-justify-content: center;
      justify-content: center;
      -webkit-box-align: center;
      -ms-flex-align: center;
      -webkit-align-items: center;
      align-items: center;
    }
</style>
</head>
<body style=" background: #eee">
 <div id="container" style=" background: #eee">
 <!-- 头部开始-->
 <nav class="navbar navbar-default navbar-fixed-top menu ">
    <div class="container">
      <div class="navbar-header page-scroll">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#menu-navbar-collapse-1">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
   <a class="navbar-brand" href="${contextPath}/index"><img class="brand-logo logo_index1" src="${contextPath}/styles/img/y-tran-logo.png" /></a>
    <%HttpSession s = request.getSession(); 
        	Object obj = s.getAttribute("shiroUser");
        	if(obj != null){
        %>
     <div class="menu_phone">
	  <ul>
	    <li ><a href="${contextPath}/management/index">平台入口</a></li>        
	  </ul>
	</div>
	</div>
	<div class="collapse navbar-collapse" id="menu-navbar-collapse-1">
      <ul  class="nav navbar-nav navbar-right menulist">
        <li class="page-scroll"><a href="${contextPath}/index/about" >关于我们</a></li>
        <li class="page-scroll"><a href="${contextPath}/index/showProduct" class="first">产品中心</a></li>
        <li class="page-scroll"><a href="${contextPath}/index/contact">联系我们</a></li>
        <li class="page-scroll memu_mar" ><a href="${contextPath}/management/index">平台入口</a></li>
      </ul>
      </div>
      <%}else{ %>
      <div class="menu_phone">
	  <ul>
	    <li ><a href="${contextPath}/register/infoFillPage" >免费注册</a></li>
	    <li ><a href="${contextPath}/management/index">登录</a></li>
        
	  </ul>
	</div>
            </div>
 
   <div class="collapse navbar-collapse" id="menu-navbar-collapse-1">
      <ul  class="nav navbar-nav navbar-right menulist">
        <li class="page-scroll"><a href="${contextPath}/index/about" >关于我们</a></li>
        <li class="page-scroll"><a href="${contextPath}/index/showProduct" class="first">产品中心</a></li>
        <li class="page-scroll"><a href="${contextPath}/index/contact">联系我们</a></li>
        
		
		
        <li class="page-scroll memu_mar1"><a href="${contextPath}/management/index">登录</a></li>
        <li class="page-scroll menulogin"><a href="${contextPath}/register/infoFillPage" >免费注册</a></li>
        
      </ul>
    </div>
    <%} %>
   
  </nav>
  
 <!-- 头部结束-->
 <!-- banner开始-->
  <div class="slider" style=" background: #040001; text-align: center;" >
      <div class='sangar-slideshow-container' id='sangarBanner1'>
        <div class='sangar-content-wrapper'>
          <div class='sangar-content'><img src='${contextPath}/styles/img/banner_in2.jpg' /></div>
      
          
        </div>
      </div>
   </div>
 <!-- banner结束-->
<!-- 平台开始-->
  <div class="plat" >
    <div class="container" >
<!--       <div class="about_title"> -->
<!--         <h2>产品中心</h2> -->
<!--         <p>products</p> -->
<!--       </div> -->
      <div class="product_menu clearfix">
        <ul>
          <li class="cur"><a href="${contextPath}/index/mes">MES</a></li>
<%--          <li><a href="${contextPath}/index/ecmall">ECMall</a></li>
           <li><a href="${contextPath}/index/spc">SPC</a></li> --%>
          <li><p><a id="monitorId" data-toggle="modal" href="#example" style="background:#bf1a20;color:white;">实时监控展示</a></p></li> 
        </ul>
      </div>
      <div class="about_content clearfix">
　　MES系统即制造企业生产过程执行系统，是一套面向制造企业车间执行层的生产信息化管理系统。MES 可以为企业提供包括制造数据管理、计划排程管理、生产调度管理、库存管理、质量管理、人力资源管理、工作中心/设备管理、工具工装管理、采购管理、成本管理、项目看板管理、生产过程控制、底层数据集成分析、上层数据集成分解等管理模块，为企业打造一个扎实、可靠、全面、可行的制造协同管理平台。
     <br/><img src='${contextPath}/styles/img/mes.jpg' class="img-responsive" style="float:right; "/> <br/>
 <strong>MES系统常见功能编辑</strong> <br/>
-现场管理细度：由按天变为按分钟/秒<br/>
-现场数据采集：由人手录入变为扫描、快速准确采集<br/>
-电子看板管理：由人工统计发布变为自动采集、自动发布<br/>
-仓库物料存放：模糊、杂散变为透明、规整<br/>
-生产任务分配：人工变为自动分配、产能平衡<br/>
-仓库管理：人工、数据滞后变为系统指导、及时、准确<br/>
-责任追溯：困难、模糊变为清晰、正确<br/>
-绩效统计评估：靠残缺数据估计变为凭准确数据分析<br/>
-统计分析：按不同时间/机种/生产线等多角度分析对比<br/>


      </div>
    
    </div>
  
  </div>
<!-- 模态框+轮播 -->
<div class="container">  
  <div id="example" class="modal fade">
      <div class="modal-dialog">
         <div class="modal-content">
            <!-- 模态框 -->  
            <div id="myCarousel" class="swiper-container">  
			    <div class="swiper-wrapper">
			      <div class="swiper-slide"><img class="img-responsive" src='${contextPath}/styles/img/index/cg.jpg' /></div>
                  <div class="swiper-slide"><img class="img-responsive"  src='${contextPath}/styles/img/index/spc.jpg' /></div>
                  <div class="swiper-slide"><img class="img-responsive"  src='${contextPath}/styles/img/index/grr.jpg' /></div>
                  <div class="swiper-slide"><img class="img-responsive"  src='${contextPath}/styles/img/index/chanpinquxian.jpg' /></div>
                  <div class="swiper-slide"><img class="img-responsive"  src='${contextPath}/styles/img/index/driveralerm.jpg' /></div>
                  <div class="swiper-slide"><img class="img-responsive"  src='${contextPath}/styles/img/index/driveralermmonitor.jpg' /></div>
                  <div class="swiper-slide"><img class="img-responsive"  src='${contextPath}/styles/img/index/nenghaoshishimonitor.jpg' /></div>
			    </div>
		    <!-- Add Pagination -->
		    <div class="swiper-pagination"></div>
            </div>  
         </div>  
      </div>  
   </div> 
 <!-- 平台结束-->

 <!-- 注册开始-->

 <!-- 注册结束-->
 <!-- 底部开始-->
      <div class="foot">
      <div class="container">
<!--         <div class="foot_top"> -->
<!--           <div class="row"> -->
<%--           <div class=" col-sm-4    foot_phone"><img src="${contextPath}/styles/img/phone.png" /><span>售前咨询<br/>95187转1</span></div> --%>
<!--           <div class=" col-sm-8 foot_des">全方位的购买咨询<span>|</span>精准的配置推荐<span>|</span>灵活的价格方案<span>|</span>一对一贴心服务</div> -->
<!--           </div> -->
<!--         </div> -->

        
        <div class="foot_bottom" style="text-align:center;">
          <div class="foot_bottom_left">
          
<!--          <div class="foot_menu"><a href="#">其它入口</a></div> -->
         
         <div class="copy">Copyright © 2016 无锡悦创智能科技有限公司   苏ICP备17038704号 </div>
         </div>
        
        
</div>
       
      </div>
    
   </div>
 <!-- 底部结束-->
 </div>
  </div>
 <div class="clear"></div>
 <div class="foot_menu_fixed">
     <ul>
	   <li><a href="${contextPath}/index" ><i class="fa fa-home"></i>首页</a></li>
	   <li><a href="${contextPath}/index/about"><i class="fa fa-list"></i>关于我们</a></li>
	   <li><a href="${contextPath}/index/showProduct" class="first"><i class="fa fa-cog"></i>产品中心</a></li>
	   <li><a href="${contextPath}/index/contact"><i class="fa fa-phone"></i>联系我们</a></li>
	 </ul>
   
   </div>
  <script src="${contextPath}/js/jquery.js"></script>
  <script src="${contextPath}/js/bootstrap.min.js"></script>
  <!--banner-->
  <script src="${contextPath}/styles/css/banner/jquery.js"></script>
  <script src="${contextPath}/styles/css/banner/jquery.touchSwipe.min.js"></script>
  <script src="${contextPath}/styles/css/banner/sangarSlider/sangarSetupBulletNav.js"></script>
  <script src="${contextPath}/styles/css/banner/imagesloaded.min.js"></script>
  <script src="${contextPath}/styles/css/banner/sangarSlider.js"></script>
  <script src="${contextPath}/js/homepage.js"></script>
  <script src="${contextPath}/js/swiper.min.js"></script>
<!-- <script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/jquery.touchswipe/1.6.17/jquery.touchSwipe.js"></script> -->
  <script type="text/javascript">
  $(document).ready(function(){
	    var swiper = new Swiper('.swiper-container', {
	        slidesPerView: 4,
	        centeredSlides: true,
	        spaceBetween: 30,
	        grabCursor: true,
	        pagination: {
	          el: '.swiper-pagination',
	          clickable: true,
	        },
	      });
	$("#monitorId").click();
  });

  /*
  $(document).ready(function(){

      //手势右滑 回到上一个画面
      $('#myCarousel').bind('swiperight swiperightup swiperightdown',function(){
          $("#myCarousel").carousel('prev');
      })
      //手势左滑 进入下一个画面
      $('#myCarousel').bind('swipeleft swipeleftup swipeleftdown',function(){
          $("#myCarousel").carousel('next');
      }) 
	})*/
	</script>
  </script>
</body>
</html>