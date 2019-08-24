<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
    <%@page import="com.its.common.utils.SecurityUtils"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%> 
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>联系悦创</title>
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
        <li class="page-scroll"><a href="${contextPath}/index/showProduct" >产品中心</a></li>
        <li class="page-scroll"><a href="${contextPath}/index/contact" class="first">联系我们</a></li>
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
        <li class="page-scroll"><a href="${contextPath}/index/showProduct" >产品中心</a></li>
        <li class="page-scroll"><a href="${contextPath}/index/contact" class="first">联系我们</a></li>
        
		
		
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
          <div class='sangar-content'><img src='${contextPath}/styles/img/banner_in3.jpg' /></div>
      
          
        </div>
      </div>
   </div>
 <!-- banner结束-->
<!-- 平台开始-->
  <div class="plat contact_content">
    <div class="container" >
<!--       <div class="about_title"> -->
<!--         <h2>联系我们</h2> -->
<!--         <p>contact us</p> -->
<!--       </div> -->
      <div class="about_content" style="margin-top: 0px;">
             <div class="row">
					<div class="col-sm-6" style="border: none; ">
						<div style="margin-left: 15px;"><p style="white-space:normal;">
							<strong>无锡总部</strong> 
						</p>
						<p style="white-space:normal;">
							电话：0510-88264900&nbsp;
						</p>
						<p style="white-space:normal;">
							<span style="line-height:1.5;">传真： 0510-88264901</span> 
						</p>
						<p style="white-space:normal;">
							<span style="line-height:1.5;">联系电话：18801510032<br />
</span> 
						</p>
						<p style="white-space:normal;">
							电子邮件：sales@wxfriedrich.com
						</p>
						<p style="white-space:normal;">
							地址：江苏省无锡市锡山开发区蓉通路75号　
						</p>
						</div>
					</div>	
					<div class="col-sm-6" style="border: none">
					<img src="${contextPath}/styles/img/map.jpg" class="img-responsive" style="border:2px solid #ccc; margin-bottom: -2px "/>
					</div>
				</div>
				<div class="col-sm-6">
						<p style="white-space:normal;">
							<strong>1.长春办事处</strong> 
						</p>
						<p style="white-space:normal;">
							<span style="line-height:1;">电话：13086848373 &nbsp;</span><span style="line-height:1;">传真：0510-88264901</span> 
						</p>
						<p style="white-space:normal;">
							<span style="line-height:1;">地址：长春市高新开发区飞跃路2688号</span> 
						</p>

					</div>
				<div class="col-sm-6">
						<p style="white-space:normal;">
							<strong>2.天津办事处</strong> 
						</p>
						<p style="white-space:normal;">
							电话：022-87286929 &nbsp;传真：022-87286929
						</p>
						<p style="white-space:normal;">
							地址：天津市南开区金轩商业中心B座1710
						</p>
</div>
				<div class="col-sm-6">
						<p style="white-space:normal;">
							<strong>3.西安办事处</strong><strong></strong> 
						</p>
						<p style="white-space:normal;">
							电话：18800582561
						</p>
						<p style="white-space:normal;">
							地址：西安市龙湖星悦荟1-711室
						</p>

					</div>
					<div class="col-sm-6">
						<p style="white-space:normal;">
							<strong>4.武汉办事处</strong> 
						</p>
						<p style="white-space:normal;">
							电话： 027-88189601 &nbsp;传真：027-88189601
						</p>
						<p style="white-space:normal;">
							地址：武汉市汉阳区王家湾凯悦大厦B座1004室
						</p>

				</div>
				<div class="col-sm-6">
						<p style="white-space:normal;">
							<strong>5.重庆办事处</strong><strong></strong> 
						</p>
						<p style="white-space:normal;">
							电话：023-68857626 &nbsp;传真：023-68855337&nbsp;
						</p>
						<p style="white-space:normal;">
							地址：重庆市九龙坡区兴隆湾120号五环大厦B1座2-28
						</p>
</div>
					<div class="col-sm-6">
						<p style="white-space:normal;">
							<strong>6.</strong><strong>宁波办事处</strong> 
						</p>
						<p style="white-space:normal;">
							电话：18800582562 传真：0574-87830609&nbsp;
						</p>
						<p style="white-space:normal;">
							地址：宁波市高新区江南路1498号科技广场1号楼1402室
						</p>

				</div>
				<div class="col-sm-6" style=" border-bottom: none">
						<p style="white-space:normal;">
							<strong>7.广州办事处</strong> 
						</p>
						<p style="white-space:normal;">
							电话： 020-28639616 &nbsp;传真： 020-28639616
						</p>
						<p style="white-space:normal;">
							地址： 广州增城区新塘镇群贤路253号电信商务大厦
						</p>
</div>
			

      </div>
    
    </div>
  
  </div>
  
 <!-- 平台结束-->

 <!-- 注册开始-->

 <!-- 注册结束-->
 <!-- 底部开始-->
      <div class="foot" >
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
	   <li><a href="${contextPath}/index/showProduct" ><i class="fa fa-cog"></i>产品中心</a></li>
	   <li><a href="${contextPath}/index/contact" class="first"><i class="fa fa-phone"></i>联系我们</a></li>
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