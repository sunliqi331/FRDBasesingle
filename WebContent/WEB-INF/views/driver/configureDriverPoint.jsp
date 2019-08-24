<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
  trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<style>
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button{
-webkit-appearance: none !important;
 }
</style>
<link href="${contextPath}/styles/css/driver.css" rel="stylesheet" />
<link href="${contextPath }/styles/colorpicker/css/bootstrap-colorpicker.css" rel="stylesheet">
<div class="pageContent">
  <form method="post" id="checkDataform" action="${contextPath}/driver/saveMesPointCheckData" enctype="multipart/form-data" class="form form-horizontal" onsubmit="return iframeCallback(this, dialogReloadNotDestroyCallback);">
    <input id="mesDriverPoint" type="hidden" name="mesDriverPoints.id" value="${mesDriverPoint.id}">
      <div class="pageFormContent" layoutH="58">
       <div class="row">
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>状态名称</label>
          <div id="divOfName" class="col-sm-6">
            <input id="name" type="text" name="name" class="form-control input-medium validate[required,maxSize[36]] required" maxlength="36" />
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>状态值</label>
          <div id="divOfCheckvalue" class="col-sm-6">
            <input id="checkvalue" type="number" name="checkvalue" class="form-control input-medium validate[custom[number],required,maxSize[22]]" maxlength="22" 
            onKeyPress="if (event.keyCode!=46 && event.keyCode!=45 && 
                (event.keyCode<48 || event.keyCode>57)) event.returnValue=false"/>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>状态颜色</label>
          <div class="col-sm-6">
             <div class="option-grid">
                  <div id="colorPicker" class="input-group colorpicker-component">
                    <input type="text" name="colorcode" value="#00AABB" class="form-control" />
                    <span class="input-group-addon" ><i></i></span>
                  </div>
                </div>
                <div class="option-grid">
                  <div id="colorPicker" class="input-group colorpicker-component">
                  </div>
                </div>
          </div>
        </div>
        <div class="form-group">
          <label for="inputText" class="control-label col-sm-4">状态动图</label>
          <div id="divOfCheckvalue" class="col-sm-6">
            <input id="statusimg" type="file" name="statusimg" class="form-control"/>
          </div>
        </div>
      </div>
      </div>
      <div class="modal-footer" style="margin-bottom:-10px">
      <a class="btn btn-default1" target="selectedTodo" id="bye" data-target="mesPointCheckDatasTable" rel="ids" href="${contextPath }/driver/deleteMesPointCheckData" title="确认要删除?"  style="float:left; margin-left:-10px"> 
    <i class="fa fa-remove"></i> 
    <span>删除状态测点属性</span>
  </a>
        <button id="confirm" type="button" class="btn btn-primary">确定</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      </div>
  </form>
   
  <table class="table  table-striped" id="mesPointCheckDatasTable" data-field="mesPointCheckDatas" data-url="${contextPath}/driver/MesPointCheckDataData/${mesDriverPoint.id}">
    <thead>
     <tr>
      <th data-checkbox="true" width="22">
      <input class="cbr checkboxCtrl" type="checkbox" group="ids">
      </th>
      <th data-field="name" width="100">状态名称</th>
      <th data-field="checkvalue" width="100">状态值</th>
      <th data-field="color" width="100">状态颜色</th>
      <th data-field="companyfilePath" width="100">状态动图</th>
     </tr>
    </thead>
  </table> 
  <br/>
</div>
  <!-- Modal -->
  <script type="text/template" id="dialogTemp">
  <div class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
          <i class="icon-edit">
            <span class="modal-title">Modal title</span>
          </i>
        </div>
        <div class="modal-body unitBox">
        </div>
      </div>
    </div>
  </div>
</script>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/colorpicker/js/bootstrap-colorpicker.js"></script>
<script type="text/javascript">
  $(document).ready(function($) {
	  $("#bye").click(function(){
		  $.table.refreshCurrent("${contextPath}/driver/MesPointCheckDataData/${mesDriverPoint.id}");
	  })
    function close2upload() {
      $.table && $.table.refreshCurrent();
    }
    $.table.init('mesPointCheckDatasTable', {
      //toolbar : '#toolBar1'
    }, function(data) {
      var $p = $('#mesPointCheckDatasTable').find('tbody');
      $('tr[data-uniqueid]', $p).each(function(i) {
        var $this = $(this);
        var item = data.mesPointCheckDatas[i];
        $this.attr('url', item.storeType + '/' + item.uuid);
      });
    });
  });

</script>
<script type="text/javascript">
$(document).ready(function(){
//  if($("span:contains('每页显示 ')").is(":hidden")) 
//    { 
//       $(this).parent().css("margin-top","10px");
         
//    }
  var PaintStyle = function() {
      this.strokeStyle = '#000';
      this.lineWidth = 4;
  }
  var gSelectedGraphic = {
        graphicType: 'arrow',
        connectorType: 'Flowchart',
        paintStyle: new PaintStyle()
    };
  var colors = {
	    	'red': '#FF0000',
	        'black': '#000000',
	        'white': '#ffffff',
	        'default': '#777777',
	        'primary': '#337ab7',
	        'success': '#5cb85c',
	        'info': '#5bc0de',
	        'warning': '#f0ad4e',
	        'danger': '#d9534f', "aliceblue": "#f0f8ff",
	         "antiquewhite": "#faebd7",
	         "aqua": "#00ffff",
	         "aquamarine": "#7fffd4",
	         "azure": "#f0ffff",
	         "beige": "#f5f5dc",
	         "bisque": "#ffe4c4",
	         "black": "#000000",
	         "blanchedalmond": "#ffebcd",
	         "blue": "#0000ff",
	         "blueviolet": "#8a2be2",
	         "brown": "#a52a2a",
	         "burlywood": "#deb887",
	         "cadetblue": "#5f9ea0",
	         "chartreuse": "#7fff00",
	         "chocolate": "#d2691e",
	         "coral": "#ff7f50",
	         "cornflowerblue": "#6495ed",
	         "cornsilk": "#fff8dc",
	         "crimson": "#dc143c",
	         "cyan": "#00ffff",
	         "darkblue": "#00008b",
	         "darkcyan": "#008b8b",
	         "darkgoldenrod": "#b8860b",
	         "darkgray": "#a9a9a9",
	         "darkgreen": "#006400",
	         "darkkhaki": "#bdb76b",
	         "darkmagenta": "#8b008b",
	         "darkolivegreen": "#556b2f",
	         "darkorange": "#ff8c00",
	         "darkorchid": "#9932cc",
	         "darkred": "#8b0000",
	         "darksalmon": "#e9967a",
	         "darkseagreen": "#8fbc8f",
	         "darkslateblue": "#483d8b",
	         "darkslategray": "#2f4f4f",
	         "darkturquoise": "#00ced1",
	         "darkviolet": "#9400d3",
	         "deeppink": "#ff1493",
	         "deepskyblue": "#00bfff",
	};
 $('#colorPicker').colorpicker({
        //color: '#000'
	 colorSelectors: colors
        
    }).on('changeColor',function(e){
        var paintStyle = new PaintStyle();
        paintStyle.strokeStyle =  e.color.toHex();
        gSelectedGraphic.paintStyle = paintStyle;
    });
});
var submitStatus1 = new Array();
var submitStatus2 = new Array();
function checkValue(){
    if($("#name").val()!=""&&$.trim($("#name").val()) != ''){
        ajaxTodo("${contextPath}/driver/checkName/" + $("#mesDriverPoint").val()  + "/" + $("#name").val() ,function(data){
            checkData(data,$("#name"),"状态名不可重复",$("#divOfName"),$("#checkDataform"),submitStatus1,"name");
        });
      }
}
function checkValue1(){
    if($("#name").val()!=""&&$.trim($("#name").val()) != ''){
        ajaxTodo("${contextPath}/driver/checkName/" + $("#mesDriverPoint").val()  + "/" + $("#name").val() ,function(data){
            checkData1(data,$("#name"),"状态名不可重复",$("#divOfName"),$("#checkDataform"));
        });
      }
}
function checkValue2(){
    if($("#checkvalue").val()!=""&&$.trim($("#checkvalue").val()) != ''){
        ajaxTodo("${contextPath}/driver/checkCheckvalue/" + $("#mesDriverPoint").val()  + "/" + $("#checkvalue").val() ,function(data){
            checkData(data,$("#checkvalue"),"状态值不可重复",$("#divOfCheckvalue"),$("#checkDataform"),submitStatus2,"checkvalue");
        });
      }
}
function checkValue3(){
    if($("#checkvalue").val()!=""&&$.trim($("#checkvalue").val()) != ''){
        ajaxTodo("${contextPath}/driver/checkCheckvalue/" + $("#mesDriverPoint").val()  + "/" + $("#checkvalue").val() ,function(data){
            checkData1(data,$("#checkvalue"),"状态值不可重复",$("#divOfCheckvalue"),$("#checkDataform"));
        });
      }
}
$("#name").keyup(checkValue);
$("#checkvalue").keyup(checkValue2);
$("#confirm").click(function(){
    if(submitStatus1.length>0||submitStatus2.length>0){
        checkValue1();
        checkValue3();
    }else{
        $("#checkDataform").submit();
        if($("#name").val()!=""&&$("#checkvalue").val()!=""){
            $("#name").val("");
            $("#checkvalue").val("");
            $("#checkDataform").find("div.parentFormformID").remove();
            $("#checkDataform").find("div.snformError").remove();
        }
    }
});
</script>