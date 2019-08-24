<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
    <%@page import="com.its.common.utils.SecurityUtils"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%> 
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>首页</title>
<link rel="stylesheet" href="${contextPath}/styles/css/bootstrap.css" type="text/css">
<link href="${contextPath}/styles/assets/font-awesome/css/font-awesome.css" rel="stylesheet" />
<link rel="stylesheet" href="${contextPath}/styles/css/banner/css/default1.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/banner/css/sangarSlider.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/reset.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/index0.css" type="text/css">
<link href="${contextPath}/styles/img/favicon.ico" type="image/x-icon" rel="icon" />
<script>document.createElement('nav');</script>
<script src="${contextPath}/js/jquery.js"></script>
</head>
<body>
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
    <a class="navbar-brand logoPhone" href="${contextPath}/index"><img class="brand-logo logo_indexLogo" src="${contextPath}/styles/img/y-tran-phone.png" /></a>
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
        <li class="page-scroll"><a href="${contextPath}/index/showProduct">产品中心</a></li>
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
        <li class="page-scroll"><a href="${contextPath}/index/showProduct">产品中心</a></li>
        <li class="page-scroll"><a href="${contextPath}/index/contact">联系我们</a></li>
        
		
		
        <li class="page-scroll memu_mar1"><a href="${contextPath}/management/index">登录</a></li>
        <li class="page-scroll menulogin"><a href="${contextPath}/register/infoFillPage" >免费注册</a></li>
        
      </ul>
    </div>
    <%} %>
    </div>
  </nav>

 <!-- 头部结束-->
 <!-- banner开始-->
  <script>
  $('#sangarBanner').hide();
  </script>
  <div class="slider" >
      <div class='sangar-slideshow-container' id='sangarBanner'>
        <div class='sangar-content-wrapper'>
          <div class='sangar-content'><img src='${contextPath}/styles/img/banner1.jpg' /></div>
          <div class='sangar-content'><img src='${contextPath}/styles/img/banner2.jpg' /></div>
          
        </div>
      </div>
   </div>
 <!-- banner结束-->
 <!-- main1开始-->
 <div class="main1">
   <div class="container">
   <div class="row">
     <div class="col-sm-4 main_menu main_menu1" >
       <a href="${contextPath}/index/about"><img src="${contextPath}/styles/img/m1.png" class="img1"/><img src="${contextPath}/styles/img/m1o.png" class="img1o"/>
       <h3>关于Y-tran</h3>
       <small>全面了解Y-tran</small></a>
     </div>
     <div class="col-sm-4 main_menu main_menu2" >
       <a href="products.html" ><img src="${contextPath}/styles/img/m3.png" class="img1"/><img src="${contextPath}/styles/img/m3o.png" class="img1o"/>
       <h3>产品中心</h3>
       <small>详细了解MES系统</small></a>
     </div>
     <div class="col-sm-4 main_menu main_menu3">
       <a href="${contextPath}/index/contact"><img src="${contextPath}/styles/img/m2.png" class="img1"/><img src="${contextPath}/styles/img/m2o.png" class="img1o"/>
       <h3>联系Y-tran</h3>
       <small style="color:#e4dacb">欢迎前来参观</small></a>
     </div>

   </div>
   </div>
 
 </div>
 <!-- main1结束-->
 
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
          <div class="foot_bottom_left" >
          
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
	   <li><a href="${contextPath}/index" class="first"><i class="fa fa-home"></i>首页</a></li>
	   <li><a href="${contextPath}/index/about"><i class="fa fa-list"></i>关于我们</a></li>
	   <li><a href="${contextPath}/index/showProduct"><i class="fa fa-cog"></i>产品中心</a></li>
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
 
</body>
</html>