<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script type="text/javascript">
$("#search").click(function() {
	if($("#username").val()==""&&$("#realname").val()==""&&$("#phone").val()==""&&$("#email").val()==""){
		return;
	}
    var M = $("#email").val();
    $.table.setCurrent("friendsTable");
    var param = $("form").serializeObject();
    $.table.refreshCurrent("${contextPath }/friends/data2",param,function(){
//      $("#userTable").bootstrapTable("checkBy",{field:'username',values:[M]});
    });
  });
</script>

<div class="pageFormContent" layoutH="58" style="height:500px">
  <div class="searchBar personnel_search" style="margin-bottom: -5px; margin-top: 10px;border:none;box-shadow: none;  width:auto; max-width:inherit ; ">
  <form class="form-inline" method="post" style="padding:0px">
    <input type="hidden" id="fromUser" value="<shiro:principal />"/>
       <div class="form-group">
		      <!--   <label for="inputText" class="searchtitle" >邮箱</label>
		          <input id="email" name="email"
		            class="form-control searchtext validate[custom[email],required,maxSize[45]] required"
		            maxlength="45"  /> -->
	        <label for="inputText" class="searchtitle" >用户名</label>
	          <input id="username" name="username"
	            class="form-control searchtext validate[required,maxSize[45]] required"
	            maxlength="45"  />
	            </div>
	     <div class="form-group">
	        <label for="inputText" class="searchtitle" >姓名</label>
	          <input id="realname" name="realname"
	            class="form-control searchtext validate[required,maxSize[45]] required"
	            maxlength="45"  />
	            </div>
	            <div class="form-group">
	        <label for="inputText" class="searchtitle" >手机号</label>
	          <input id="phone" name="phone"
	            class="form-control searchtext validate[required,maxSize[45]] required"
	            maxlength="45"  />
	       </div>
	        <div class="form-group">
	        <label for="inputText" class="searchtitle" >邮箱</label>
	          <input id="email" name="email"
	            class="form-control searchtext validate[required,maxSize[45]] required"
	            maxlength="45"  />    
	            </div> <br>
	         <button type="button" class="btn btn-info btn-search1 search_person_mail" id="search" style="float:left ">搜索</button>
	 </form>
	</div><br><br>
	<table class="table table-striped"  id="friendsTable" data-field="user" data-url="#" data-checkbox-header="false" data-single-select="true">
          <thead>
            <tr>
              <th data-checkbox="true" width="22">
                <!-- <input class="cbr checkboxCtrl" type="checkbox" group="ids"> -->
              </th>
              <th data-field="username" width="100">用户名</th>
              <th data-field="realname" width="100">好友姓名</th>
              <th data-field="phone" width="100">手机号</th>
              <th data-field="email" width="200">邮箱</th>
              
             <!--  <th data-field="phone" width="120">电话</th> -->
<!--               <th data-field="roles" width="150">角色</th> -->
            </tr>
          </thead>
        </table>
        </div>
<!--          <button  class="btn btn-info  btn-success">确定</button> -->
       <div class="modal-footer " style="margin-top:15px">
        <button id="sendInvitation" type="submit" class="btn btn-primary" >确定</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>

<script type="text/javascript">
  $(document).ready(function($) {
	  //debugger;
	//$("span:contains('每页显示')").detach();  
    $.table.init('friendsTable', {
      pageSize: 10,
      toolbar : '#toolBar1'
    }, function(data) {
     /*  var $p = $('#friendsTable').find('tbody');
      $('tr[data-uniqueid]', $p).each(function(i) {
        var $this = $(this);
        var item = data.users[i];
        console.log(data.users[i]);
        $this.attr('url', item.storeType + '/' + item.uuid);
      }); */
    });
    $("#sendInvitation").click(function(){
    	if(!$.table.getSelectedId()[0] || '' == $.table.getSelectedId()[0]){
    		swal("您未选择任何成员");
    		return;
    	}
    	$.ajax({
    		type:'POST',
    		url:"${contextPath }/chat.do/friendInvitation",
    		dataType:"json",
    		cache: false,
    		data:{friendid:$.table.getSelectedId()[0],content:"i wanna tobe your friend ^_^! From:"+$("#fromUser").val()},
    		success: function(data){
    			if(data.success == 'success')
    				swal("邀请消息已发送");
    			else
    				swal("请勿重发邀请或你们已成为好友");
    			this.$dialog.modal('hide');
    		},
    		error: DWZ.ajaxError
    	});
    		//chatClient.send("/doChat/friendInvitation",{},{fromUser:$("#fromUser").val(),friendid:$.table.getSelectedId(),content:"i wanna tobe your friend ^_^! From:"+$("#fromUser").val()});
    });
   
    function connect() {
        
    }
    
  });
</script>


