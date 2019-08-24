<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
    <%@page import="com.its.common.utils.SecurityUtils"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%> 
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>关于悦创</title>
<meta name="keywords" content="量具,影像测量仪,测量机 " />
<meta name="description" content="无锡悦创智能科技有限公司专业从事量具,检具以及测量机的生产和销售，欢迎来电咨询. " />
<link rel="stylesheet" href="${contextPath}/styles/css/bootstrap.css" type="text/css">
<link href="${contextPath}/styles/assets/font-awesome/css/font-awesome.css" rel="stylesheet" />
<link rel="stylesheet" href="${contextPath}/styles/css/banner/css/default1.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/banner/css/sangarSlider.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/reset.css" type="text/css">
<link rel="stylesheet" href="${contextPath}/styles/css/index0.css" type="text/css">
<link href="${contextPath}/styles/img/favicon.ico" type="image/x-icon" rel="icon" />
</head>
<body style=" background: #eee">
 <div id="container" >
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
        <li class="page-scroll"><a href="${contextPath}/index/about" class="first">关于我们</a></li>
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
        <li class="page-scroll"><a href="${contextPath}/index/about" class="first">关于我们</a></li>
        <li class="page-scroll"><a href="${contextPath}/index/showProduct">产品中心</a></li>
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
          <div class='sangar-content'><img src='${contextPath}/styles/img/banner_in1.jpg' /></div>
      
          
        </div>
      </div>
   </div>
 <!-- banner结束-->
<!-- 平台开始-->
  <div class="plat">
    <div class="container" >
<!--       <div class="about_title"> -->
<!--         <h2>关于我们</h2> -->
<!--         <p>About us</p> -->
<!--       </div> -->
      <div class="about_content">
　　悦创公司是一家专业从事设计、制造、销售工业自动化检测仪器的高新技术企业，主要为密封件、接插件、PCB板、TFT屏幕、磁性元件、太阳能网版、电子器件、五金冲压等生产制造企业提供高精度、高效率的检测设备，成为航天七○四所、富士达科技、京东方、倍科电器、欧菲光、鸿瑞光显、比亚迪、格特拉克、博格华纳、华晨瑞安等企业的主要供应商。<br/><br/>
<div class="about_img"><img src="${contextPath}/styles/img/about.jpg" class="img-responsive "/><br/></div>
　　经过十余年的努力，公司拥有专利权24项，其中7项发明专利，17项新型专利，顺利通过ISO9001:2008质量体系认证和德国Q-DAS技术认证，悦创已经发展成为国内工业自动化检测仪器领域一家具有出色竞争能力的快速成长企业，公司在研发、生产和产品服务一体化、技术、产品、市场、管理团队以及项目等方面具有较强的竞争优势。
<br/><br/>
　　目前我公司已为国内大多数汽车厂动力系统公司或其配套供应商均有合作供货，如：上海大众汽车有限公司、上海通用汽车有限公司、格特拉克（江西）传动系统股份有限公司、上海通用东岳动力总成有限公司、大众变速器（上海）有限公司、上海汽车变速箱公司、东风汽车公司、东风康明斯发动机有限公司、神龙汽车有限公司、一汽解放公司无锡柴油机分公司、一汽解放公司大连柴油机分公司、一汽海马汽车有限公司、长安福特马自达发动机有限公司等单位，包括整条零件加工线量检具或自动测量设备的设计与制造，并获得用户的好评。
      
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
	   <li><a href="${contextPath}/index/about" class="first"><i class="fa fa-list"></i>关于我们</a></li>
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