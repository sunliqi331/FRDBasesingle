<%@ page pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@page import="com.its.frd.util.ResourceUtil"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<style type="text/css">
	#showChangeCompanyName > a{
		  /* background-color: #2a70d8!important; 
		     font-size: medium;
		       display: none */
	}
</style>
<!--header start-->
<header class="header white-bg">
	
<!--   <div class="sidebar-toggle-box"> -->

<!--       <div data-original-title="Toggle Navigation" data-placement="right" class="tooltips"><i class="fa fa-reorder"></i>菜单</div> -->
<!--   </div> -->
  <div class="nav notify-row" id="top_menu">
    <!--  notification start -->
   
      <ul class="nav top-menu">
      <li class="sidebar-toggle-box"><div data-original-title="Toggle Navigation" data-placement="right" class="tooltips"><i class="fa fa-bars"></i>菜单</div></li>
      <!-- notification dropdown start-->
	  <%--  <li class="dropdown menulist" >
	    <a href="<%=ResourceUtil.getValueForDefaultProperties("shop.url") %>" class="menu_padding"  target="_blank"><i class="fa fa-shopping-cart"></i>商城入口</a>
	  </li> --%>
	  <!-- 显示具体选择的公司名称start -->
	  <li class="dropdown menulist"  id="showChangeCompanyName">
	        <a data-toggle="dropdown" class="dropdown-toggle menu_padding" href="#"><span style="color:#04eaff;"><%-- <i class="fa fa-building-o"></i> --%> ${sessionScope.companyinfo.companyname}</span></a>
	  </li>
	  <!-- 显示具体选择的公司名称stop -->
	  
      <li class="dropdown menulist1" >
        <a data-toggle="dropdown" class="dropdown-toggle menu_padding" href="#"> 
         <span class="username mes_list"><i class="fa fa-cogs"></i>PDM SYSTEM</span>       
          <span class="username company_list"><i class="fa fa-sitemap"></i>公司列表</span>
          <b class="caret"></b>
        </a>
        <script type="text/javascript">
        	//function showOne(){
		        $.ajax({
		            type : "GET",
		            url : "${contextPath}/management/index/userCompanys",
		            data : "",
		            dataType : "json", 
		            success : function(data) {
		               var cpIdUrl = $("#companyIdUrl");
		               cpIdUrl.append("<div class='log-arrow-up'></div>");
		               for(var i = 0; i<data.length; i++){
							var cp = data[i];
							var url = "<li><a href='${contextPath}/management/index?companyId="+cp.id+"'>"+cp.companyname+"</a></li>";
							cpIdUrl.append(url);
						}
		               var urlShopping = 
		               "<li>"+
		       	                "<a href=\"<%=ResourceUtil.getValueForDefaultProperties("shop.url") %>\"   target=\"_blank\">商城入口</a>"+
		       	       "</li>";
		               //cpIdUrl.append(urlShopping);
		            }
		        });
        	//}
        	
        $(document).on("show.bs.modal", ".modal", function(){
            $(this).draggable({
                handle: ".modal-header"   // 只能点击头部拖动
            });

        });
        	
        </script>
        <ul id="companyIdUrl"  class="dropdown-menu extended mess">
			
        </ul>
      
      </li>
      <li class="personal_phone"><a href="${contextPath}/register/personalinfo"><i class="fa fa-user"></i>个人中心</a></li>
      <li class="dropdown system" style="margin: 3px 8px 0 8px;">
        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
          <img alt="" src="${contextPath}/styles/img/avatar-mini.jpg" width="23" style="border-radius: 50%;border: 2px solid #fff;">
          <span class="username">${login_user.realname }  </span>
          <b class="caret"></b>
        </a>
        <ul class="dropdown-menu extended logout">
          <div class="log-arrow-up"></div>
          <li><a href="${contextPath}/register/personalinfo"><i class="fa fa-user"></i>个人中心</a></li>
          <li><a href="${contextPath}/management/index"><i class="fa fa-cog"></i> 返回平台</a></li>
          <li><a href="${contextPath}/index"><i class="fa fa-home"></i> 返回首页</a></li>
          <li><a href="${contextPath}/logout" class="text-center"><i class="fa fa-power-off1" style="display: inline-block;"></i>注销</a></li>
        </ul>
      </li>
      <!-- notification dropdown end -->
    </ul>
    <!--  notification end -->
  </div>
  <script>
	var test = '${sessionScope.companyinfo.companyname}';
	if(test && test != ''){
	console.log("当前登录的公司名称："+test);
		//document.getElementById("showChangeCompanyName").style.display = "inline";
		$("#showChangeCompanyName > a").show();
	}
</script>
</header>

<!--header end-->
