<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
          <div class="searchBar">
          <div class="search_header">
          <i class="fa fa-minus search_small" onclick="searchHide()"></i>
            <i class="fa fa-plus search_small" onclick="searchShow()"></i>
             <i class="fa fa-search"></i> 工序属性查询条件
           </div>
           <div class="ishidden" >
            <form class="form-inline" method="post" action="${contextPath}/procedure/procedurePropertyData/${ProcedureId}" data-target="mesProcedurePropertyTable" onsubmit="return navTabSearch(this)">
              <div class="form-group">
                <label for="inputText" class="searchtitle" style="width: 100px;">工序属性名称</label>
                <input type="text" class="form-control searchtext" id="inputText" name="search_LIKE_propertyname" value="${param.search_LIKE_propertyname}" />
              </div>
              <button type="submit" class="btn btn-info btn-search1">搜索</button>
            </form>
            </div>
          </div>
          <div class="driver_info"> 
       
              
                <b>产品名称:</b> ${procedure.mesProduct.name}&nbsp;&nbsp;&nbsp;&nbsp;
              
           
                <b>工序名称:</b> ${procedure.procedurename}&nbsp;&nbsp;&nbsp;&nbsp;
             
              
                <b>工序号:</b> ${procedure.procedurenum}
             
           
            </div>
          <div id="toolBar2" class="clearfix" style=" margin-bottom: -5px">
            <div class="btn-group pull-left">
            <shiro:hasPermission name="Product:saveProperty">
              <a class="btn btn-default1" target="dialog" rel="create_procedure_property" refresh="true" href="${contextPath}/procedure/addProcedureProperty/${ProcedureId}?pagename=addProcedureProperty">
                <i class="fa fa-plus"></i> 
                <span>添加工序属性</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="Product:editProperty">
              <a class="btn btn-default1" target="dialog" data-target="mesProcedurePropertyTable" refresh="true" rel="edit_procedure_property" href="${contextPath}/procedure/findPropertyById/{slt_uid}?pagename=editProcedureProperty">
                <i class="fa fa-pencil"></i> 
                <span>修改工序属性</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="Product:deleteProperty">
              <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="mesProcedurePropertyTable" rel="ids" href="${contextPath}/procedure/deletePropertyById" title="确认要删除?">
                <i class="fa fa-remove"></i> 
                <span>删除工序属性</span>
              </a> 
              </shiro:hasPermission>
              <shiro:hasPermission name="Product:savePoint">
              <a class="btn btn-default1 btn-tool" target="dialog" data-target="mesProcedurePropertyTable" refresh="true" rel="savePoint" href="${contextPath}/procedure/addPoint/{slt_uid}">
                <i class="fa fa-cogs"></i> 
                <span>配置测点</span>
              </a> 
              </shiro:hasPermission>
                <form action="#" id="relationFileForm" method="post" hidden="true" enctype="multipart/form-data">
                    <input type="file" id="importFile" name="alarmRelationFile">
                    <input type="text" name="ProcedureId" value="${ProcedureId}">
                </form>
                <a class="btn btn-default1" href="javascript:void(0);" id="importRelation">
                    <i class="fa fa-upload"></i>
                    <span>导入</span>
                </a>
            </div>
          </div>
          <table class="table table-striped" id="mesProcedurePropertyTable" data-field="mesProcedureProperty" data-url="${contextPath}/procedure/procedurePropertyData/${ProcedureId}">
            <thead>
              <tr>
                   <th data-field="Number" width="2%" data-align="center">序号</th>
                <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids"></th>
                <!-- <th data-field="keyid" width="100">工序属性id</th> -->
                <th data-field="propertyname" width="100">工序属性名称</th>
                <th data-field="mesPoints.name" width="100">测点名称 </th>
                <th data-field="mesPoints.codekey" width="100">测点ID</th>
                <th data-field="mesProductProcedure.procedurenum" width="100">所属工序</th>
                <th data-field="controlWay" width="100">测量方法</th>
                <th data-field="uppervalues" width="100">上公差</th>
                <th data-field="lowervalues" width="100">下公差</th>
                <th data-field="standardvalues" width="100">标准值</th>
                <th data-field="mesPoints.units" width="100">单位 </th>
              </tr>
            </thead>
          </table>
<!-- Modal -->
 <!--<script type="text/template" id="dialogTemp">
  <div class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <i class="fa fa-edit">
            <span class="modal-title">Modal title</span>
          </i>
        </div>
        <div class="modal-body unitBox">
        </div>
      </div>
    </div>
  </div>
</script> -->
<script type="text/javascript">
    //导入报警信息模板
    $("#importRelation").bind("click",function(){
        $("#importFile").click();
    });
    $("#importFile").bind("change",function(){
        var pathArr = $("#importFile").val().split("\\");
        var fileName = pathArr[pathArr.length-1]; //文件名
        var fileSuffix = fileName.split(".")[1];  //文件后缀
        //验证文件后缀是否正确
        if(fileSuffix!="xlsx" || fileSuffix=="" || fileSuffix==null){
            swal("错误","请选择正确的文件","error");
            return;
        }
        var formData = new FormData($("#relationFileForm")[0]);
        $.ajax({
            url:"${contextPath}/procedure/read_gongxu_excel",
            dataType:"text",
            type:"POST",
            data:formData,
            cache: false,
            contentType: false,
            processData: false,
            success:function(data){
                console.log(data);
                var data =  eval("("+data+")");
                if("success"== data.result){
                    uplocadSuccess();
                }else{
                    uplocadError();
                }
            }
        });
        $("#importFile").val("");
    });
    function uplocadSuccess(){
        $.table.refreshCurrent();
        swal("成功","导入成功！","success");
    }
    function uplocadError(){
        $.table.refreshCurrent();
        swal("警告","文件内部格式有误，部分内容导入失败！","warning");
    }
  $(document).ready(function($) {
    function close2upload() {
      $.table && $.table.refreshCurrent();
    }
    $.table.init('mesProcedurePropertyTable', {
      toolbar : '#toolBar1'
    }, function(data) {
      var $p = $('#mesProcedurePropertyTable').find('tbody');
      $('tr[data-uniqueid]', $p).each(function(i) {
        var $this = $(this);
        var item = data.mesProcedureProperty[i];
        $this.attr('url', item.storeType + '/' + item.uuid);
      });
    });
  });
</script>
<script src="${contextPath}/js/uikit.pageswitch.js"></script>
<div class="modal-footer">
  <button type="button" id="propertyBtn" class="btn btn-default" data-dismiss="pageswitch">返回</button>
</div>
<script type="text/javascript">
$("#test").click(function(){
	$.table.setCurrent($("#mesProductProcedureTable"));
})
/* $("#propertyBtn").click(function(){
	$.table.setCurrent($("#mesProductProcedureTable"));
}) */
</script>