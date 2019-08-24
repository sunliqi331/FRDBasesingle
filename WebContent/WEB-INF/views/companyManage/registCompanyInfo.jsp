<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<title>注册公司</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>

<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath }/styles/css/jquery-ui-tooltip.css" rel="stylesheet" type="text/css" />
</head>
<script>
jQuery(document).ready(function(){
//   $("select").chosen({search_contains:true});
  jQuery("#formID").validationEngine();
  $("#file_upload").uploadify({
      'swf'      : '${contextPath}/styles/uploadify/scripts/uploadify.swf',    //指定上传控件的主体文件
      'uploader' : '${contextPath}/styles/uploadify/scripts/uploadify.php',    //指定服务器端上传处理文件
      'auto' : false,
      'multi' : true,
      'height' : 25,
      'width' : 50,
      'simUploadLimit' : 3,
      'wmode' : 'transparent',
      onError : function(event, queueID, fileObj) {
        swal("文件:" + fileObj.name + " 上传失败");
      }
    });
  });
</script>
<body>
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a
          href="${contextPath}/management/index"> 首页</a></li>
        <li>注册公司</li>
      </ol>
      <div class="main-wrap">
        <div class="main-body">
        <div class="searchBar page_see_title" >
          <div class="search_header">
             <i class="fa fa-pencil"></i> 填写公司信息
           </div>
          </div>
          <div class="page_see_content" >
            <form method="post" id="registerForm" action="${contextPath}/company/create" class="form form-horizontal" enctype="multipart/form-data" onsubmit="return iframeCallback(this, reloadSubPage);">
              <div class="pageFormContent" layoutH="58">
                <div class="listleft">
<!--                 <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4">公司形式</label>
                    <div class="col-sm-6">
                      <select id="companytype" name="companytype" class="form-control validate[required] required"
                       onchange="if(ChkSave()==false)return false;">
                        <option>请选择</option>
                        <option value="factory">工厂</option>
                        <option value="company">公司</option>
                      </select>
                    </div>
                  </div> -->
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>公司名称</label>
                    <div id="divOfCompanyname" class="col-sm-6">
                      <input type="text" id="companyname" name="companyname"
                        class="form-control input-medium validate[required,custom[onlyLetterNumber],maxSize[30]] required"
                        maxlength="30" value="" />
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>公司邮箱</label>
                    <div id="divOfCompanyemail" class="col-sm-6">
                      <input type="text" id="companyemail" name="companyemail"
                        class="form-control input-medium validate[required,custom[email],maxSize[100]] required"
                        maxlength="100" value="" />
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>公司地址</label>
                    <div id="divOfAddress" class="col-sm-6">
                      <input id="address" type="text" name="address"
                        class="form-control input-medium validate[required,maxSize[40]] required"
                        maxlength="40" value="" />
                    </div>
                  </div>
<%--                   <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4">父公司 </label>
                    <div class="col-sm-6">
                      <select id="parentname" name="parentname"
                        class="form-control validate[required] required">
                        <option>请选择</option>
                        <c:forEach var="companyinfo" items="${companyinfo}">
                          <option value="${companyinfo.companyname }">${companyinfo.companyname}</option>
                        </c:forEach>
                      </select>
                    </div>
                  </div> --%>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4">成立日期
                    </label>
                    <div class="col-sm-6">
                      <div class="controls input-append date form_datetime" data-date="" data-date-format="yyyy-mm-dd HH:mm:ss" data-link-field="dtp_input1">
                        <input class="form-control datetime" type="text" style="background: none;" value="" readonly> 
                        <span class="add-on" style="position: absolute; right: 44px; bottom:0; padding: 7px;">
                          <i class="fa fa-remove"></i>
                          </span> <span class="add-on" style="position: absolute; right: 15px; bottom:0; padding: 7px;">
                          <i class="fa fa-th"></i></span>
                      </div>
                      <input type="hidden" id="dtp_input1" value="" name="startdate" />
                    </div>
                  </div>
                  
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>法人</label>
                    <div id="divOfLegalperson" class="col-sm-6">
                      <input id="legalperson" type="text" name="legalperson"
                        class="form-control input-medium validate[required,maxSize[20]] required"
                        maxlength="20" value="" />
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>类型</label>
                    <div id="divOfType" class="col-sm-6 select_nobg">
                      <select id="infotype" name="infotype"
                        class="form-control validate[required] required">
                        <option value="">请选择类型</option>
                      <c:forEach var="Dictionary2" items="${Dictionary2}">
                        <option value="${Dictionary2.name }">${Dictionary2.name}</option>
                        </c:forEach>
                      </select>
                    </div>
                  </div>
                   <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>所属行业</label>
                    <div id="divOfBusinesstype" class="col-sm-6 select_nobg">
                      
                      <select id="businesstype" name="businesstype"
                        class="form-control validate[required] required">
                      <option value="">请选择行业</option>
                      <c:forEach var="Dictionary1" items="${Dictionary1}">
                        <option value="${Dictionary1.name }">${Dictionary1.name}</option>
                        </c:forEach>
                      </select>
                    </div>
                  </div>
                </div>
                <div class="listright">
                
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>注册号
                    </label>
                    <div id="divOfRegisternum" class="col-sm-6">
                      <input id="registernum" type="text" name="registernum"
                        class="form-control input-medium validate[required,maxSize[20]] required"
                        maxlength="20" value="" />
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>登记机关</label>
                    <div id="divOfRegisterorgan" class="col-sm-6">
                      <input id="registerorgan" type="text" name="registerorgan"
                        class="form-control input-medium validate[required,maxSize[20]] required"
                        maxlength="20" value="" />
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>登记状态</label>
                    <div id="divOfRegisterstatus" class="col-sm-6">
                      <input id="registerstatus" type="text" name="registerstatus"
                        class="form-control input-medium validate[required,maxSize[10]] required"
                        maxlength="10" value="" />
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4">公司电话</label>
                    <div id="divOfCompanyphone" class="col-sm-6">
                      <input id="companyphone" type="text" name="companyphone"
                        class="form-control input-medium validate[maxSize[15]]"
                        maxlength="15" value="" />
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>注册资本</label>
                    <div id="divOfRegistercapital" class="col-sm-6">
                    <div class="input-group">
                      <input id="registercapital" type="text" name="registercapital"
                        class="form-control input-medium validate[custom[number],min[10],required,maxSize[20]] required"
                        maxlength="6" value=""  style=" border-right:none" />
                        <span class="input-group-addon input-unit">万元</span>
                      </div>
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>营业执照
                    </label>
                    <div id="divOfyyzz" class="col-sm-6">
                     <div class="a-upload">
                        <input readOnly="true" type='text' name='textfield' id="textfield1" class='txt' style="margin-top:0 " />
                        <span  class='btn_up' onclick="javascript:openBrowse1();" ><i class="fa fa-folder-open"></i> 选择</span>                      
                        <input hideFocus class="form-control input-medium validate[required] required file" style="display: none" type="file" name="files" id="file1" onchange="document.getElementById('textfield1').value=this.value">
                        
                      </div>
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>公章样本
                    </label>
                    <div id="divOfgzyb" class="col-sm-6">
                      <div class="a-upload">
                        <input readOnly="true" type='text' name='textfield' id="textfield2" class='txt' style="margin-top:0 " />
                        <span  class='btn_up' onclick="javascript:openBrowse2();" ><i class="fa fa-folder-open"></i> 选择</span>                      
                        <input hideFocus class="form-control input-medium validate[required] required file" style="display: none" type="file" name="files" id="file2" onchange="document.getElementById('textfield2').value=this.value">
                        
                      </div>
                    </div>
                  </div>
                   <div class="form-group">
                  <label for="inputText" class="control-label col-sm-4">
                  </label>
                  <span >(注:上传图片不得大于1M,图片格式请选择jpg/png/jpeg)</span>
                  </div>
                </div>
              </div>
              <div class="modal-footer" style="border-top: 0; text-align: center;  ">
                <button type="button" id="register" class="btn btn-primary" style="width: 200px;margin-top: 15px;">注册</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
<!-- Modal -->
  <script type="text/template" id="dialogTemp">
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
</script>
<script type="text/javascript">
// function ChkSave()
// {
//     if( document.getElementById("companytype").value=="company")
//     {
//       document.getElementById("parentname").value="";
//       document.getElementById("parentname").disabled=true;       
//       document.getElementById("legalperson").disabled=false;      
//       document.getElementById("type").disabled=false;
//       document.getElementById("businesstype").disabled=false;
//       document.getElementById("registercapital").disabled=false;
//       document.getElementById("registerstatus").disabled=false;
//       document.getElementById("registerorgan").disabled=false;
//       if( $("#parentname").prev().hasClass("formError")){
//             $("#parentname").prev().css("display","none");  
//         }
//       if( $("#legalperson").prev().hasClass("formError")){
//             $("#legalperson").prev().css("display","block");  
//         } 
//       if( $("#type").prev().hasClass("formError")){
//             $("#type").prev().css("display","block");  
//         }
//       if( $("#businesstype").prev().hasClass("formError")){
//             $("#businesstype").prev().css("display","block");  
//         }
//         if( $("#registercapital").prev().hasClass("formError")){
//               $("#registercapital").prev().css("display","block");  
//         }
//         if( $("#registerstatus").prev().hasClass("formError")){
//             $("#registerstatus").prev().css("display","block");  
//         }
//         if( $("#registerorgan").prev().hasClass("formError")){
//               $("#registerorgan").prev().css("display","block");  
//         }
//         return false;
//     }else{
//       document.getElementById("parentname").disabled=false;
//       document.getElementById("legalperson").disabled=true;         
//       document.getElementById("legalperson").value="";
//       document.getElementById("type").disabled=true;      
//       document.getElementById("type").value="";
//       document.getElementById("businesstype").disabled=true;     
//       document.getElementById("businesstype").value="";
//       document.getElementById("registercapital").disabled=true;     
//       document.getElementById("registercapital").value="";
//       document.getElementById("registerstatus").disabled=true;     
//       document.getElementById("registerstatus").value="";
//       document.getElementById("registerorgan").disabled=true;      
//       document.getElementById("registerorgan").value="";
//       if( $("#legalperson").prev().hasClass("formError")){
//           $("#legalperson").prev().css("display","none");  
//         }
//       if( $("#type").prev().hasClass("formError")){
//             $("#type").prev().css("display","none");  
//         }
//       if( $("#businesstype").prev().hasClass("formError")){
//           $("#businesstype").prev().css("display","none");  
//         }
//       if( $("#registercapital").prev().hasClass("formError")){
//             $("#registercapital").prev().css("display","none");  
//         }
//       if( $("#registerstatus").prev().hasClass("formError")){
//           $("#registerstatus").prev().css("display","none");  
//         }
//       if( $("#registerorgan").prev().hasClass("formError")){
//             $("#registerorgan").prev().css("display","none");  
//         }
//         return false;
//     }
// }
</script>
<c:set var="ParentTitle" value="Company" />
<c:set var="ModuleTitle" value="CompanyRegister" />

<%@ include file="/WEB-INF/views/com_foot.jsp"%>
<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>
<script>
function openBrowse1(){ 
    var ie=navigator.appName=="Microsoft Internet Explorer" ? true : false; 
    if(ie){ 
    document.getElementById("file1").click(); 
    //document.getElementById("filename").value=document.getElementById("file").value;
    }else{
    var a=document.createEvent("MouseEvents");//FF的处理 
    a.initEvent("click", true, true);  
    document.getElementById("file1").dispatchEvent(a); 
    } 
    } 
function openBrowse2(){ 
    var ie=navigator.appName=="Microsoft Internet Explorer" ? true : false; 
    if(ie){ 
    document.getElementById("file2").click(); 
    //document.getElementById("filename").value=document.getElementById("file").value;
    }else{
    var a=document.createEvent("MouseEvents");//FF的处理 
    a.initEvent("click", true, true);  
    document.getElementById("file2").dispatchEvent(a); 
    } 
    } 
$('.form_datetime').datetimepicker({
    language:  'zh-CN',
    format: 'yyyy-mm-dd',
    weekStart: 1,
    todayBtn:'linked',
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    forceParse: 0,
    showMeridian: 1,
    minView: 2
    
 });
</script>
<script type="text/javascript">
function formReset(){
    document.getElementById("registerForm").reset()
    }
    $("#register").click(function() {
        var companyname = $("#companyname").val();
        var address = $("#address").val();
        var legalperson = $("#legalperson").val();
        var infotype = $("#infotype").val();
        var businesstype = $("#businesstype").val();
        var registernum = $("#registernum").val();
        var registerorgan = $("#registerorgan").val();
        var registerstatus = $("#registerstatus").val();
        var registercapital = $("#registercapital").val();
        $("#divOfCompanyname").find("div.parentFormformID").remove();
        $("#divOfAddress").find("div.parentFormformID").remove();
        $("#divOfLegalperson").find("div.parentFormformID").remove();
        $("#divOfType").find("div.parentFormformID").remove();
        $("#divOfBusinesstype").find("div.parentFormformID").remove();
        $("#divOfRegisternum").find("div.parentFormformID").remove();
        $("#divOfRegisterorgan").find("div.parentFormformID").remove();
        $("#divOfRegisterstatus").find("div.parentFormformID").remove();
        $("#divOfRegistercapital").find("div.parentFormformID").remove();
        $("#divOfyyzz").find("div.parentFormformID").remove();
        $("#divOfgzyb").find("div.parentFormformID").remove();
                var file1 = $("#file1").val();
                var file2 = $("#file2").val();
                if (file1 != "" && file2 != "") {
                    var FileExt1 = file1.replace(/.+\./, "");
                    var FileExt2 = file2.replace(/.+\./, "");
                    var fileSize1 = $("#file1")[0].files[0].size;
                    var fileSize2 = $("#file2")[0].files[0].size;
                    fileSize1 = fileSize1 / 1024;
                    fileSize2 = fileSize2 / 1024;
                    if (FileExt1 === "jpg" || FileExt1 === "png"
                            || FileExt1 === "jpeg" || FileExt1 === "JPG"
                            || FileExt1 === "PNG" || FileExt1 === "JPEG") {
                        if (fileSize1 > "1024") {
                              $("#file1").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 营业执照图片过大</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
//                          swal("错误", "营业执照图片过大,请重新上传！", "error");
                        } else {
                            if (FileExt2 === "jpg" || FileExt2 === "png"
                                    || FileExt2 === "jpeg"
                                    || FileExt2 === "JPG" || FileExt2 === "PNG"
                                    || FileExt2 === "JPEG") {
                                if (fileSize2 > "1024") {
                                      $("#file2").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 公章样本图片过大</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
//                                  swal("错误", "公章样本图片过大,请重新上传！", "error");
                                } else {
                                    $("#registerForm").submit();
                                }
                            } else {
                                  $("#file2").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 不支持公章样本图片格式</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
//                              swal("错误", "不支持公章样本图片格式！", "error");
                            }
                        }
                    } else {
                          $("#file1").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 不支持营业执照图片格式</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
//                      swal("错误", "不支持营业执照图片格式！", "error");
                    }
                } else {
                    if(companyname == ""){
                          $("#companyname").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 此处不可空白</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
                    }
                    if(address == ""){
                          $("#address").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 此处不可空白</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
                    }
                    if(legalperson == ""){
                          $("#legalperson").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 此处不可空白</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
                    }
                    if(infotype == ""){
                          $("#infotype").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 此处不可空白</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
                    }
                    if(businesstype == ""){
                          $("#businesstype").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 此处不可空白</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
                    }
                    if(registernum == ""){
                          $("#registernum").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 此处不可空白</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
                    }
                    if(registerorgan == ""){
                          $("#registerorgan").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 此处不可空白</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
                    }
                    if(registerstatus == ""){
                          $("#registerstatus").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 此处不可空白</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
                    }
                    if(registercapital == ""){
                          $("#registercapital").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 此处不可空白</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
                    }
                    if(file1 == ""){
//                      swal("错误", "未上传营业执照图片！", "error");
                          $("#file1").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 营业执照不能为空</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
                    }
                    if(file2 == ""){
//                      swal("错误", "未上传公章样本图片！", "error");
                          $("#file2").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;line-height: 16px;''><div class='formErrorContent'>* 公章样本不能为空</div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
                    }
//                  $("#registerForm").submit();
                }
            });
</script>
<script type="text/javascript">
$("#companyname").blur(function(){
  $("#divOfCompanyname").find("div.parentFormformID").remove();
  if($("#companyname").val()!=""){
        Ajax();
    }
  });
function Ajax(){
    ajaxTodo("${contextPath}/company/checkName/" + $("#companyname").val(), function(data) {
      $.each(data, function(idx, item) {
        if (item != 1) {
          $("#divOfCompanyname").find("div.parentFormformID").remove();
          $("#companyname").after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -35px;''><div class='formErrorContent'>* 该公司名已存在<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
          $("#register").attr("disabled", "true");
          } else {
            $("#divOfCompanyname").find("div.parentFormformID").remove();
            $("#register").removeAttr("disabled");
            }
        });
      });
    }
</script>
</body>
</html>