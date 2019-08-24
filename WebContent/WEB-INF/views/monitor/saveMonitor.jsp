<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
        <form class="form form-horizontal" id="authority">
            <div class="pageFormContent" layoutH="58">
              <div class="row">
                <div class="form-group">
		            <label for="monitorPainterName" class="control-label col-sm-3">监控名称 :</label>
		            <div class="col-sm-7">
		              <input type="text" id="monitorPainterName" value="${monitorPainter.name }" class="form-control required" />
		            </div>
	            </div>
             	<div class="form-group">
		            <label for="authId" class="control-label col-sm-3">监控权限 :</label>
		            <div class="col-sm-7">
		              <select data-placeholder="Choose a user..." multiple  id="authId" class="form-control"  >
		            	  <option value="0">全部</option>
		            	  <c:forEach items="${users }" var="user">
		            	  		<option value="${user.id }">
		            	  		<%-- <c:choose>
		            	  			<c:when test="${user.realname == null || user.realname == ''}">${user.username }</c:when>
		            	  			<c:otherwise>${user.realname }</c:otherwise>
		            	  		</c:choose>  --%>
		            	  		${user.username }-${user.realname }
		            	  		</option>
		            	  </c:forEach>
		              </select>
		            </div>
		        </div>
                </div>
                </div>
	        <div class="modal-footer">
	          <a id="submit" class="btn btn-primary">确定</a>
	          <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	          <!-- 当前登录用户id -->
	          <input type="hidden" value="${currentUserId }" id="currentUserId">
	          <!-- 画面创建者id -->
	          <input type="hidden" value="${painterFounderId }" id="painterFounderId">
	        </div>
        </form>
        <!-- <h2 id="denied" hidden="true" align="center">您没有权限修改这个页面！</h2> -->
<script type="text/javascript">
$(document).ready(function(){
	//判断是否是画面创建者在修改监控页面,如果不是，就隐藏画面
	//画面第一次创建
	/* if($("#painterFounderId").val()==""){
		$("#authority").show();
	}else if(($("#painterFounderId").val()!="")&&($("#painterFounderId").val()!=$("#currentUserId").val())){
		$("#authority").hide();
		$("#denied").show();
	}	 */
	
	$("div").removeClass("modal-lg");
	$("#authId").chosen({search_contains:true}).change(function(){
		if($(this).val() == 0){
			var ids = new Array();
			$.each($("#authId").find("option"),function(idx,item){
				var val = $(item).attr("value");	
				if(val != 0){
					ids.push(val);
				}
			});
			$("#authId").val(ids);
			$("#authId option:first").remove();
			$("#authId").trigger("chosen:updated");
		}
	});
 	$("#authId").change(function(){
		if($("#authId option:first").val()=='0'){
			$("#authId option:first").remove();
			$("#authId").trigger("chosen:updated");
		} 
	}); 
	
	
	$("#authId").next().css("width","350px");
	console.log("im in");
	if($("#monitorPainterId").val() != 0){
		var selectedIds = JSON.parse('${selectedIds}');
		console.log(selectedIds);
		$("#authId").val(selectedIds);
		$("#authId").trigger("chosen:updated");
	}
	$("#submit").click(function(e) {
		console.log($("#monitorPainterName").val());
		if($("#monitorPainterName").val() == ''){
			swal("请输入监控名称");
			return;
		}

        // 替换浏览器缓存的
        var ComponentInfo = function(id, eleDiv){
            console.info("ComponentInfo");
            this.chartId = id;
            this.elementDiv = eleDiv;
            // this.sourceData = '';
        }
        var components = JSON.parse(localStorage.getItem("components"));
        if (components) {
                for (var i = 0; i < components.length; i++) {
                    var htmlChange = null;
                    htmlChange = components[i].elementDiv;
                    var outHtml = jQuery.parseHTML(htmlChange)
                    if(outHtml){
                       // storageManager.saveComponentsData(outHtml[0]);
                       console.info("saveComponentsData start");
                        // var _id = outHtml[0]
                        var _id = components[i].chartId;
                        // var _eleDiv = outHtml[0].replace(resizeReg, '');
                        var _eleDiv = htmlChange.replace(resizeReg, '');
                        var chartsInfo = new ComponentInfo(_id, _eleDiv);
                        syncToLocal('components', chartsInfo,'chartId');
                        console.info("saveComponentsData end");
                    }
                }
        }
        function syncToLocal(field, obj, key) {
            console.info("syncToLocal start");
            var localObj = JSON.parse(localStorage.getItem(field));
            var isExist = false;
            key = key || 'id';
            if (localObj != null && localObj.length != 0) {
                for (var i = 0; i < localObj.length; i++) {
                    if (typeof obj == 'string') {
                        if(localObj[i][key] == obj) {
                            localObj.splice(i,1);
                            break;
                        }
                    } else {
                        if(localObj[i][key] == obj[key]){
                            localObj[i] = obj;
                            isExist = true;
                            break;
                        }
                    }
                }
            }
            if (!isExist && (typeof obj != 'string')) {
                if(localObj == null){
                    localObj = [];
                }
                localObj.push(obj);
            }
            localStorage.setItem(field,JSON.stringify(localObj));
            console.info("syncToLocal end");
        }
		var customerArray = {
				"bindingData":localStorage.getItem("bindingData"),
				"connections":localStorage.getItem("connections"),
				"components":localStorage.getItem("components"),
				"elementsInfo":localStorage.getItem("elementsInfo"),
				"winId":localStorage.getItem("winId") == ""||localStorage.getItem("winId")==null ? 0 : localStorage.getItem("winId"),
				"chartsData":localStorage.getItem("chartsData"),
				"monitorTableData":localStorage.getItem("monitorTableData"),
				"AlarmTableData":localStorage.getItem("AlarmTableData"),
				//"domContent":$("#canvas").prop("outerHTML").trim(),
				"domContent":$("#canvas").html().trim(),
				"name":$("#monitorPainterName").val(),
				"id":$("#monitorPainterId").val(),
				"authIds":JSON.stringify($("#authId").val()),
				"background":localStorage.getItem("background"),
				"container":localStorage.getItem("container"),
				/* 2019-05-15 slq */
				"spcData": localStorage.getItem("spcData") == 'null'?"":localStorage.getItem("spcData"),
				"spcAnalysisData": localStorage.getItem("spcAnalysisData")== 'null'?"":localStorage.getItem("spcAnalysisData")
				/* end */
				
		};
		var driverSns = {};
		 $.each($(".prop-textbox > .widget-word"),function(idx,item){
			    var points = new Array();
				var driverId = $(item).find("li:first").attr("class").split("_")[1];
				$.each($(item).find("li"),function(i,v){
					points.push($(v).attr("class").split("_")[2]);
				});
				driverSns[driverId] = points;
	    });
		 customerArray.driverSns = JSON.stringify(driverSns);
		 console.log($("#authId").val());
		 //判断是否是画面创建者在修改监控页面
		 //console.log("----"+$("#painterFounderId").val());
		 //if($("#currentUserId").val()==$("#painterFounderId").val() || $("#painterFounderId").val()==""){
			 /* if($("#authId").val() == null){
				 swal("请选择监控人员");
				 return;
			 } */
		 //}
			$.ajax({
				url:contextPath+"/procedureMonitor/insert",
				type:"POST",
				dataType:"JSON",
				data: customerArray, 
				success:function(data){
					if(data.result == 'success'){
						$.pdialog.destroyCurrent();
						$("#myModal").modal('hide');
						$("#isSaved").val("true");
						swal("保存成功");
					}else{
						swal("存在相同的名字,请检查");
					}
					
					
				}
				
			});
	});
	
});
</script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>