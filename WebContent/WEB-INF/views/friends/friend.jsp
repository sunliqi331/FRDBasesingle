<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>好友聊天</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
<link href="${contextPath}/styles/css/friend.css" rel="stylesheet" />
<script type="text/javascript">
	sessionStorage.clear();
</script>
</head>
<body>
<input type="hidden" id="chatType" />
<input type="hidden" id="friendid" />
<input type="hidden" id="groupid" />
<input type="hidden" id="fromUserName" value="${user.realname }" />
<input type="hidden" id="fromUser" value="${user.id }" />
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
      <div class="main-content">
       <ol class="breadcrumb" style="background: #f1f2f7; height:41px; margin:0;line-height:40px;    border-radius: 0; display:block;">
        <li><i class="fa fa-home"></i><a
          href="${contextPath}/management/index"> 首页</a></li>
        <li>好友聊天</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
        <div class="searchBar friend_title" >
          <div class="search_header">
             <i class="fa fa-comments-o"></i> 好友聊天
           </div>
          </div>
          <div class="chart_content"  style="border:1px solid #6ebbfd; overflow: hidden; border-top:none">
          <div class="row">
           <div class="col-sm-3 friend_list">
            
             <ul class="sidebar-menu friend-menu" id="accordion" >
                 <li class="friend_submenu" >    
		            <div class="link">
                     <span class="total"></span><img src="${contextPath}/styles/img/group.png" /> 我的好友<span id="friendMessageNum" ></span>
                     <span class="arrow"></span>
                  </div>
		              <ul class="friend_sub" id="myFriend">
		               </ul> 
		         </li>
                 <li class="friend_submenu" >
                  <div class="link">
                     <span class="total"></span> <img src="${contextPath}/styles/img/group.png" />我的群组
                     <span class="arrow"></span>
                  </div>
                  <ul class="friend_sub" id="myGroup">                   
                  </ul>
                </li>
   
            </ul>
              
           </div>
           <div class="col-sm-9 friend_content" style="padding-top: 20px">
	           <div class="box box-success direct-chat direct-chat-success">
	            <div class="box-header with-border">
	              <h3 class="box-title" style="color: #6ebbfd;font-weight: 500;">聊天</h3>
	            </div>
	            <!-- /.box-header -->
	            <div class="box-body">
	              <!-- Conversations are loaded here -->
	              <div class="direct-chat-messages">
	              </div>
	            </div>
	            <!-- /.box-body -->
	            <div class="box-footer">
	                <div class="input-group" >
	                  <input type="text" id="messages" name="message" placeholder="在此处输入信息" class="form-control">
	                      <span class="input-group-btn">
	                        <button class="btn btn-info btn-flat">发送</button>
	                      </span>
	                </div>
	            </div>
	            <!-- /.box-footer-->
	          </div>
          </div>
         </div>
          <!--/.direct-chat -->
        </div>
    </div>
    </div>
        </div>
      </div>
<script type="text/x-jquery-tmpl" id="messageContentFrom">
	<div class="direct-chat-msg">
	                  <div class="direct-chat-info clearfix">
                      	<span class="direct-chat-timestamp pull-left" data-content="timestamp"></span>
	                  </div>
	                  <span class="direct-chat-name pull-left" data-content="fromUser"></span>
	                  <div class="direct-chat-text pull-left" data-content="content">
	                  </div>
	</div>

</script>
<script type="text/x-jquery-tmpl" id="messageContentTo">
	<div class="direct-chat-msg right">
	                  <div class="direct-chat-info clearfix">
	                    <span class="direct-chat-timestamp pull-right" data-content="timestamp"></span>
	                  </div>
                      <span class="direct-chat-name pull-right" data-content="fromUser"></span>
	                  <div class="direct-chat-text pull-right" data-content="content">
	                  </div>
	</div>

</script>
<script type="text/javascript" src="${contextPath }/js/monitor.websocket.js"></script>
<script src="${contextPath }/js/sockjs.js"></script>
<script src="${contextPath }/js/stomp.js"></script>
<script src="${contextPath }/js/jquery.loadTemplate-1.4.4.js"></script>
   <script type="text/javascript" >
   $(document).ready(function($){
	   document.onkeypress = function(e){ 
		    var ev = document.all ? window.event : e;
		    if(ev.keyCode==13) {
		    	$(".btn-flat").trigger('click');
		     }
		}
	   $('.direct-chat-messages').scrollTop( $('.direct-chat-messages')[0].scrollHeight );
	   
	   var Accordion = function(el, multiple) {
			this.el = el || {};
			this.multiple = multiple || false;

			// Variables privadas
			var links = this.el.find('.link');
			// Evento
			links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown)
		}

		Accordion.prototype.dropdown = function(e) {
			var $el = e.data.el;
				$this = $(this),
				$next = $this.next();

			$next.slideToggle();
			$this.parent().toggleClass('open');

			if (!e.data.multiple) {
				$el.find('.friend_sub').not($next).slideUp().parent().removeClass('open');
			};
		}	

		var accordion = new Accordion($('#accordion'), false);
   });
   </script>
<script type="text/javascript">
	var chatClient = $.monitorSocket.connect({connectUrl:"${contextPath}/chat",connectCallback:function(frame){
		chatClient.ws.onclose =function(event){
	    	console.log("lose connection");
	    	//判断是不是Firefox浏览器
			var isFirefox=navigator.userAgent.toUpperCase().indexOf("FIREFOX")?true:false;
			if(idFirefox == false){
	    	swal("聊天室连接中断,请刷新页面重试")
			}
	    	sessionStorage.removeItem("headerIds");
	    };
	    chatClient.subscribe("/doChat/chat/groups", function(data){
	    	
	    	console.log(JSON.parse(data.body).length);
	    	$.each(JSON.parse(data.body),function(idx,item){
	    		$("#myGroup").append('<li><a><img src="${contextPath}/styles/img/people.png" /><span>'+ item.name +'</span><input type="hidden" value="'+ item.id +'"></a></li>')
	    	});
	    	$("#myGroup").on('click','li',function(){
	    		$("#chatType").val("群聊");
	    		$("#myGroup").children(".active").removeClass('active');
				$(this).addClass("active");	  
				$("#groupid").val($(this).find("input[type='hidden']").val());
				$(".box-title").html("正在"+$(this).find("span").html()+"聊天");
				 $(".direct-chat-messages").empty();
				 var contentLog = JSON.parse(sessionStorage.getItem("group-"+$("#groupid").val()));
				 if(contentLog == null){
					 contentLog = new Array();
				 }
				 $.each(contentLog,function(idx,item){
	    			 	var $messageContent;
	    				 if(item.userid != null){
	    					 $messageContent = $("#messageContentFrom")
	    				 }else{
	    					 $messageContent = $("#messageContentTo")
	    				 }
		    			 $(".direct-chat-messages").loadTemplate($messageContent,
		    					    {
		    					        fromUser: item.fromUser,
		    					        timestamp: '<fmt:formatDate value="<%=new Date() %>" type="both"/>',
		    					        content: item.content
		    					    },{ append: true});
		    		 });
				 var ids = JSON.parse(sessionStorage.getItem("headerIds"));
	    			if(null == ids){
	    				ids = new Array();
	    			}
	    			if(ids.length == 0 || $.inArray("group-" + $("#groupid").val(),ids)  == -1){
	    				chatClient.subscribe("/topic/"+ $("#groupid").val() +"/chat",function(data){
	   					 console.log(JSON.parse(data.body));
	   					 var content = JSON.parse(data.body)
	   					 if(content.userid != $("#fromUser").val()){
	   						 var tmp = JSON.parse(sessionStorage.getItem("group-"+$("#groupid").val()));
	   						 if(tmp == null){
	   							 tmp = new Array();
	   						 }
	   						 tmp.push(JSON.parse(data.body));
	   						 sessionStorage.setItem("group-"+$("#groupid").val(),JSON.stringify(tmp));
	   						 $(".direct-chat-messages").loadTemplate($("#messageContentFrom"),
	   		    					    {
	   		    					        fromUser: JSON.parse(data.body).fromUser,
	   		    					        timestamp: '<fmt:formatDate value="<%=new Date() %>" type="both"/>',
	   		    					        content: JSON.parse(data.body).content
	   		    					    },{ append: true});
	   					 }
	   					
	   				 },{id:"group-" + $("#groupid").val()});
	    				ids.push("group-" + $("#groupid").val());
	    			     sessionStorage.setItem("headerIds",JSON.stringify(ids));
	    			}
				 
	    	});
	    });
		 chatClient.subscribe("/doChat/chat/participants", function(data){
		    	console.log(JSON.parse(data.body).length);
		    	var totalNum = 0;
		    	$.each(JSON.parse(data.body),function(idx,item){
		    		if(item.remainingNum==0){
		    			$("#myFriend").append('<li><a><img src="${contextPath}/styles/img/people.png" /><span>'+ item.friendname +'</span><input type="hidden" value="'+ item.friendid +'"></a></li>')	
		    		}else{
		    			$("#myFriend").append('<li><a><img src="${contextPath}/styles/img/people.png" /><span>'+ item.friendname +'</span><span class="msgNum">'+item.remainingNum+'</span><input type="hidden" value="'+ item.friendid +'"></a></li>')	
		    		}
		    		
		    		totalNum += item.remainingNum;
		    	});
		    	if(totalNum != 0){
		    		$("#friendMessageNum").html(totalNum).addClass("friendMessageNum");
		    	}
		    	$("#myFriend").on('click','li',function(){
		    		$("#friendMessageNum").empty().removeClass("friendMessageNum");
		    		$(this).find(".msgNum").empty();
		    		 $.ajax({
		    			url:"${contextPath}/friends/updateFriendsRemainingNum",
		    		    data:{friendid:$(this).find("input[type='hidden']").val(),remainingNum:0},
		    		    success:function(data){
		    		    	
		    		    }
		    		    
		    		});
		    		$("#chatType").val("私聊");
		    		$("#myFriend").children(".active").removeClass('active');
					$(this).addClass("active");	  
		    		$("#friendid").val($(this).find("input[type='hidden']").val());
		    		$(this).find(".remain").html("0");
		    		$(".box-title").html("正在和"+$(this).find("span").html()+"聊天");
		    		
		    		 $(".direct-chat-messages").empty();
		    			 var contentLog = JSON.parse(sessionStorage.getItem($("#friendid").val()+ "-" + $("#fromUser").val()));
		    			 if(null == contentLog){
		    				 contentLog = new Array();
		    			 }
		    			 sessionStorage.setItem($("#friendid").val()+ "-" + $("#fromUser").val(),JSON.stringify(contentLog));
		    			 $.each(contentLog,function(idx,item){
		    			 	var $messageContent;
		    				 if(item.userid != null){
		    					 $messageContent = $("#messageContentFrom")
		    				 }else{
		    					 $messageContent = $("#messageContentTo")
		    				 }
			    			 $(".direct-chat-messages").loadTemplate($messageContent,
			    					    {
			    					        fromUser: item.fromUser,
			    					        timestamp: '<fmt:formatDate value="<%=new Date() %>" type="both"/>',
			    					        content: item.content
			    					    },{ append: true});
			    		 });
		    			 $('.direct-chat-messages').scrollTop( $('.direct-chat-messages')[0].scrollHeight);
		    			var ids = JSON.parse(sessionStorage.getItem("headerIds"));
		    			if(null == ids){
		    				ids = new Array();
		    			}
		    			if(ids.length == 0 || $.inArray($("#friendid").val()+ "-" + $("#fromUser").val(),ids)  == -1){
		    				 var client = chatClient.subscribe("/user/"+ $("#friendid").val()+ "-" + $("#fromUser").val() +"/chat",function(data){
		    					 var contentLog_ = JSON.parse(sessionStorage.getItem($("#friendid").val()+ "-" + $("#fromUser").val()));
				    			 if(null == contentLog_){
				    				 contentLog_ = new Array();
				    			 }
				    			 contentLog_.push(JSON.parse(data.body));
					    		 sessionStorage.setItem($("#friendid").val()+ "-" + $("#fromUser").val(),JSON.stringify(contentLog_));
					    		 $.ajax({
						    			url:"${contextPath}/friends/updateFriendsRemainingNum",
						    		    data:{friendid:JSON.parse(data.body).userid,remainingNum:0},
						    		    success:function(data){
						    		    	
						    		    }
						    		});
					    			 $(".direct-chat-messages").loadTemplate($("#messageContentFrom"),
					    					    {
					    					        fromUser: JSON.parse(data.body).fromUser,
					    					        timestamp: '<fmt:formatDate value="<%=new Date() %>" type="both"/>',
					    					        content: JSON.parse(data.body).content
					    					    },{ append: true});
					    			 $('.direct-chat-messages').scrollTop( $('.direct-chat-messages')[0].scrollHeight);
				    			 
				    		 },{id:$("#friendid").val()+ "-" + $("#fromUser").val()});
		    				 ids.push($("#friendid").val()+ "-" + $("#fromUser").val());
		    			     sessionStorage.setItem("headerIds",JSON.stringify(ids));
		    			}
		    	})
		 });
		
	}});
	
	 $(".btn-flat").click(function(){
		  if($("#chatType").val() == ''){
			 swal("未选择好友或群组");
			 return;
		 } 
		  if($("#chatType").val() == '私聊' && $("#friendid").val() == ''){
			  swal("请选择好友");
			    return;
		  }else if($("#chatType").val() == '群聊' && $("#groupid").val() == ''){
			  swal("请选择群组");
			    return;
		  }
		   if($("#messages").val() == ""){
			   swal("请输入内容");
			    return;
		   }
		   var sendContent = {
			        fromUser: $("#fromUserName").val(),
			        timestamp: '<fmt:formatDate value="<%=new Date() %>" type="both"/>',
			        content: $("#messages").val()
			    };
		   var contentLog;
		   if($("#chatType").val() == '私聊'){
			  contentLog = JSON.parse(sessionStorage.getItem($("#friendid").val()+ "-" + $("#fromUser").val()));
			  if(null == contentLog){
					 contentLog = new Array();
				 }
			 contentLog.push(sendContent);
			 sessionStorage.setItem($("#friendid").val()+ "-" + $("#fromUser").val(),JSON.stringify(contentLog))
		   }else{
			   contentLog = JSON.parse(sessionStorage.getItem("group-" + $("#groupid").val()));
			   if(null == contentLog){
					 contentLog = new Array();
				 }
			 contentLog.push(sendContent);
			 sessionStorage.setItem("group-" + $("#groupid").val(),JSON.stringify(contentLog))
		   }
			
		   $(".direct-chat-messages").loadTemplate($("#messageContentTo"),
				   sendContent,{ append: true});
			 if($("#chatType").val() == '私聊'){
		    stompClient.send("/doChat/chatting", {}, JSON.stringify({friendid:$("#friendid").val(),userid:$("#fromUser").val(),content:$("#messages").val(),chatType:$("#chatType").val()}));
			 }else{
		    stompClient.send("/doChat/chatting", {}, JSON.stringify({friendid:$("#groupid").val(),userid:$("#fromUser").val(),content:$("#messages").val(),chatType:$("#chatType").val()}));
				 
			 }
		   $("#messages").val("");
		   //alert( $('.direct-chat-messages')[0].scrollHeight + 100);
		   $('.direct-chat-messages').scrollTop( $('.direct-chat-messages')[0].scrollHeight);
	 });
</script>

 <c:set var="ParentTitle" value="FriendManage" />
 <c:set var="ModuleTitle" value="FriendChat" />
<%@ include file="/WEB-INF/views/com_foot.jsp"%>
          <script type="text/javascript">

  $("#childType").change(function(e){
	  //alert($("#analysisMethod").val());
	 if($("#childType").val()=="datasource"){
		window.open('dataSource.html');
		//$(".count_cg").css("display","none"); 
		//$(".count_grr").css("display","block");  
		 } 

	  });
	   $('.form_datetime1').datetimepicker({
    language:  'zh-CN',
	   format: 'yyyy-mm-dd hh:ii:ss',
     weekStart: 1,
     todayBtn:'linked',
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		forceParse: 0,
     showMeridian: 1,
		//minView: 2
		
 });
 
   $('.form_datetime2').datetimepicker({
    language:  'zh-CN',
	   format: 'yyyy-mm-dd hh:ii:ss',
     weekStart: 1,
     todayBtn:'linked',
		autoclose: 1,
		todayHighlight: 1,
		startView: 2,
		forceParse: 0,
     showMeridian: 1,
		//minView: 2
		
 });


</script>
  <script> 
//   $(document).ready(function($){
//     $('#sidebar > ul > li[title="${ParentTitle}"] > a').click();
//     $('#sidebar .sub > li[title="${ModuleTitle}"]').addClass('active');
//   });
  </script>
</body>
</html>
