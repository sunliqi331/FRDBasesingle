<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
    <%@page import="com.its.common.utils.SecurityUtils"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%> 
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>产品介绍</title>
<link rel="stylesheet" href="${contextPath}/styles/css/bootstrap.css" type="text/css">
<link href="${contextPath}/styles/assets/font-awesome/css/font-awesome.css" rel="stylesheet" />
<link rel="stylesheet" href="${contextPath}/styles/css/banner/css/default1.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/banner/css/sangarSlider.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/reset.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/index0.css" type="text/css">
<link href="${contextPath}/styles/img/favicon.ico" type="image/x-icon" rel="icon" />
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
        <li class="page-scroll"><a href="${contextPath}/index/about">关于我们</a></li>
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
  <div class="slider" style=" background: #040001; text-align: center;">
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
      <div class="row">
        <div class="col-md-6 plat_bg ">
         <a href="${contextPath}/index/mes"> <div class="plat_img">
             <img src="${contextPath}/styles/img/plat1.png" class="pimg1"/><img src="${contextPath}/styles/img/plat1o.png" class="pimg1o"/>
            <h3>PDM SYSTEM</h3>
            <small>专业领先、高性能、高可用</small>
            
          </div>
          <p>
            制造数据管理、计划排程管理、生产调度管理、库存管理、质量管理、人力资源管理、设备管理、工具工装管理
          </p>
          </a>
        </div>
        <div class="col-md-6 plat_bg ">
          <a href="${contextPath}/index/ecmall"><div class="plat_img">
            <img src="${contextPath}/styles/img/plat2.png" class="pimg1"/><img src="${contextPath}/styles/img/plat2o.png" class="pimg1o"/>
            <h3>ECMall</h3>
            <small>专业领先、高性能、高可用</small>
           
          </div>
          <p>
            中国首家以B2C（京东、天猫）、C2C（淘宝）、O2O（苏宁易购、美团）、B2B（阿里巴巴）等模式于一体的综合型运营平台
          </p>
          </a> 
        </div>
<!--         <div class="col-md-4 plat_bg"> -->
<!--           <div class="plat_img"> -->
<%--              <a href="${contextPath}/index/spc"><img src="${contextPath}/styles/img/plat3.png" class="pimg1"/><img src="${contextPath}/styles/img/plat3o.png" class="pimg1o"/> --%>
<!--             <h3>SPC</h3> -->
<!--             <small>专业领先、高性能、高可用</small> -->
<!--             </a> -->
<!--           </div> -->
<!--          <p> -->
<!--           统计过程控制（简称SPC）是一种借助数理统计方法的过程控制工具。它对生产过程进行分析评价，根据反馈信息及时发现系统性因素出现的征兆... -->
<!--           </p> -->
          
<!--         </div> -->
      
    
   
     
      
      </div>
    
    </div>
  
  </div>
  
 <!-- 平台结束-->

 <!-- 注册开始-->

 <!-- 注册结束-->
 <!-- 底部开始-->
 <div style=" clear:both"></div>
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
 
</body>
</html>