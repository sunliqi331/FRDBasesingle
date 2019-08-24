<%@ page language="java" contentType="text/html; charset=UTF-8"
  trimDirectiveWhitespaces="true" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<style>
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button{
-webkit-appearance: none !important;
 }
</style>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
      + path + "/";
%>
<script>
jQuery(document).ready(function(){
  jQuery("#formID").validationEngine();
  $("#file_upload").uploadify({
      'swf'      : '<%=basePath%>styles/uploadify/scripts/uploadify.swf',    //指定上传控件的主体文件
      'uploader' : '<%=basePath%>styles/uploadify/scripts/uploadify.php',    //指定服务器端上传处理文件
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
 <div class="searchBar page_see_title" >
          <div class="search_header">
             <i class="fa fa-eye"></i> 编辑公司
           </div>
          </div>
<div  class="pageContent page_see_content">
            <form method="post" id="registerForm" action="<%=basePath%>company/create" class="form form-horizontal" enctype="multipart/form-data" onsubmit="return iframeCallback(this, subPageCallback);">
              <div class="pageFormContent clearfix " layoutH="58">
              <input type="hidden" name="id" value="${companyinfo.id}">
              <input type="hidden" name="userid" value="${companyinfo.userid}">
              <input type="hidden" name="createtime" value="${companyinfo.createtime}">
              
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
                        maxlength="30" value="${companyinfo.companyname}" />
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>公司邮箱</label>
                    <div id="divOfCompanyemail" class="col-sm-6">
                      <input type="text" id="companyemail" name="companyemail"
                        class="form-control input-medium validate[required,custom[email],maxSize[100]] required"
                        maxlength="100" value="${companyinfo.companyemail}" />
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>公司地址</label>
                    <div class="col-sm-6">
                      <input id="address" type="text" name="address"
                        class="form-control input-medium validate[required,maxSize[40]] required"
                        maxlength="40" value="${companyinfo.address}" />
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
                        <input class="form-control datetime" type="text" style="background: none;" value="${companyinfo.startdate}" readonly> 
                        <span class="add-on" style="position: absolute; right: 44px; bottom:0; padding: 5px 7px;">
                          <i class="fa fa-remove"></i>
                          </span> <span class="add-on" style="position: absolute; right: 15px;bottom:0; padding:5px 7px;">
                          <i class="fa fa-th"></i></span>
                      </div>
                      <input type="hidden" id="dtp_input1" value="${companyinfo.startdate}" name="startdate" />
                    </div>
                  </div>
                  
                  
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>类型</label>
                    <div class="col-sm-6">
                      <select id="type" name="infotype"
                        class="form-control validate[required] required">
                        <option value="${companyinfo.infotype}">${companyinfo.infotype}</option>
                      <c:forEach var="Dictionary2" items="${Dictionary2}">
                        <option value="${Dictionary2.name }">${Dictionary2.name}</option>
                        </c:forEach>
                      </select>
                    </div>
                  </div>
			   <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>法人</label>
                    <div class="col-sm-6">
                      <input id="legalperson" type="text" name="legalperson"
                        class="form-control input-medium validate[required,maxSize[20]] required"
                        maxlength="20" value="${companyinfo.legalperson}" />
                    </div>
                  </div>
                   <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>所属行业</label>
                    <div class="col-sm-6">
                      <select id="businesstype" name="businesstype"
                        class="form-control validate[required] required">
                      <option value="${companyinfo.businesstype}">${companyinfo.businesstype}</option>
                      <c:forEach var="Dictionary1" items="${Dictionary1}">
                        <option value="${Dictionary1.name }">${Dictionary1.name}</option>
                        </c:forEach>
                      </select>
                    </div>
                  </div>
                   <c:if test="${companyfiles!= null && fn:length(companyfiles) != 0}">
                  <div class="form-group">
					<label for="inputText" class="control-label col-sm-4">营业执照
					</label>
					<div class="col-sm-6">
						<div class="official">
							<img alt="营业执照" style="height:160px" src="${contextPath }/company/showPic/${companyfiles[0].id}">
							<input id="yyzz" name="pictureids" type="hidden" value="${companyfiles[0].id}">
						</div>
					</div>
			</div></c:if>
                </div>
                <div class="listright">
                
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>注册号
                    </label>
                    <div class="col-sm-6">
                      <input id="registernum" type="text" name="registernum"
                        class="form-control input-medium validate[required,maxSize[20]] required"
                        maxlength="20" value="${companyinfo.registernum}" />
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>登记机关</label>
                    <div class="col-sm-6">
                      <input id="registerorgan" type="text" name="registerorgan"
                        class="form-control input-medium validate[required,maxSize[20]] required"
                        maxlength="20" value="${companyinfo.registerorgan}" />
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>登记状态</label>
                    <div class="col-sm-6">
                      <input id="registerstatus" type="text" name="registerstatus"
                        class="form-control input-medium validate[required,maxSize[10]] required"
                        maxlength="10" value="${companyinfo.registerstatus}" />
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>公司电话</label>
                    <div class="col-sm-6">
                      <input id="companyphone" type="text" name="companyphone"
                        class="form-control input-medium validate[maxSize[15]] "
                        maxlength="15" value="${companyinfo.companyphone}" />
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>注册资本</label>
                    <div class="col-sm-6">
                    <div class="input-group">
                      <input id="registercapital" type="number" name="registercapital"
                        class="form-control input-medium validate[custom[number],min[10],required,maxSize[20]] required"
                        maxlength="6" value="${companyinfo.registercapital}" style=" border-right:none"/>
                        <span class="input-group-addon input-unit">万元</span>
                        </div>
                    </div>
                  </div>
                    <script type="text/javascript">
                    function clearYyzz(){
                    	$("#yyzz").val("");
                    }
                    function clearGzyb(){
                    	$("#gzyb").val("");
                    }
                    </script>
                  <!-- <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>营业执照
                    </label>
                    <div class="col-sm-6">
                      <input
                        class="form-control input-medium "
                        type="file" name="files" id="" multiple="true" onclick="clearYyzz()">
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>公章样本
                    </label>
                    <div class="col-sm-6">
                      <input class="form-control input-medium "
                        type="file" name="files"  onclick="clearGzyb()">
                    </div>
                  </div> -->
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>营业执照
                    </label>
                    <div class="col-sm-6">
                     <div class="a-upload">
					    <input readOnly="true"  type='text' name='textfield' id="textfield1" class='txt' />
                        <span  class='btn_up'  onclick="javascript:openBrowse1();"><i class="fa fa-folder-open"></i> 选择</span>						
                        <input hideFocus class="form-control input-medium validate[] file" type="file" name="files" style="display:none" id="file1" multiple="true" onclick="clearYyzz()" onchange="document.getElementById('textfield1').value=this.value">
					    
					  </div>
                    </div>
                  </div>
                  <div class="form-group">
                    <label for="inputText" class="control-label col-sm-4"><span class="require">*&nbsp;</span>公章样本
                    </label>
                    <div class="col-sm-6">
                      <div class="a-upload">
					    <input readOnly="true"  type='text' name='textfield' id="textfield2" class='txt' />
                        <span  class='btn_up' onclick="javascript:openBrowse2();" ><i class="fa fa-folder-open"></i> 选择</span>						
                        <input hideFocus class="form-control input-medium validate[] file" type="file" name="files" id="file2" style="display:none" onclick="clearGzyb()" onchange="document.getElementById('textfield2').value=this.value">
					    
					  </div>
                    </div>
                  </div>
                  
                  
                  <c:if test="${companyfiles!= null && fn:length(companyfiles) >1}">
                  <div class="form-group">
					<label for="inputText" class="control-label col-sm-4">公章样本
					</label>
					<div class="col-sm-6">
						<div class="official">
							
								<img alt="公章样本" style="height:160px" src="${contextPath }/company/showPic/${companyfiles[1].id}">
								<input id="gzyb" name="pictureids" type="hidden" value="${companyfiles[1].id}">
						</div>
					</div>
					
			</div></c:if>
                  <div class="form-group">
                  <label for="inputText" class="control-label col-sm-4">
                  </label>
                  <span>(注:上传图片不得大于1M,图片格式请选择jpg/png/jpeg)</span>
                  </div>
                </div>
              </div>
<div class="modal-footer" style="margin:0 15px">
		<button type="button" id="editBtn" class="btn btn-primary">确定</button>
		<button type="button" class="btn btn-default"
			data-dismiss="pageswitch">关闭</button>
	</div>
            </form>
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
	</script>
<script>
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
	$("#editBtn").click(function() {
						var file1 = $("#file1").val();
						var file2 = $("#file2").val();
						if (file1 != "" || file2 != "") {
							if (file1 != "") {
								var FileExt1 = file1.replace(/.+\./, "");
								var fileSize1 = $("#file1")[0].files[0].size;
								fileSize1 = fileSize1 / 1024;

								if (FileExt1 === "jpg" || FileExt1 === "png"
										|| FileExt1 === "jpeg"
										|| FileExt1 === "JPG"
										|| FileExt1 === "PNG"
										|| FileExt1 === "JPEG") {
									if (fileSize1 > "1024") {
										swal("错误", "营业执照图片过大,请重新上传！", "error");
									} else {
										if (file2 == "") {
											$("#registerForm").submit();
										} else {
											var FileExt2 = file2.replace(
													/.+\./, "");
											var fileSize2 = $("#file2")[0].files[0].size;
											fileSize2 = fileSize2 / 1024;
											if (FileExt2 === "jpg"
													|| FileExt2 === "png"
													|| FileExt2 === "jpeg"
													|| FileExt2 === "JPG"
													|| FileExt2 === "PNG"
													|| FileExt2 === "JPEG") {
												if (fileSize2 > "1024") {
													swal("错误",
															"公章样本图片过大,请重新上传！",
															"error");
												} else {
													$("#registerForm").submit();
												}
											} else {
												swal("错误", "不支持公章样本图片格式！",
														"error");
											}
										}
									}
								} else {
									swal("错误", "不支持营业执照图片格式！", "error");
								}
							} else {
								var FileExt2 = file2.replace(/.+\./, "");
								var fileSize2 = $("#file2")[0].files[0].size;
								fileSize2 = fileSize2 / 1024;
								if (FileExt2 === "jpg" || FileExt2 === "png"
										|| FileExt2 === "jpeg"
										|| FileExt2 === "JPG"
										|| FileExt2 === "PNG"
										|| FileExt2 === "JPEG") {
									if (fileSize2 > "1024") {
										swal("错误", "公章样本图片过大,请重新上传！", "error");
									} else {
										$("#registerForm").submit();
									}
								} else {
									swal("错误", "不支持公章样本图片格式！", "error");
								}
							}
						} else {
							$("#registerForm").submit();
						}
					});
</script>
<script type="text/javascript">
var oldname = $("#companyname").val();
 $("#companyname").blur(function(){
  $("#divOfCompanyname").find("div.parentFormformID").remove();
  if($("#companyname").val()!=""){
	  if(oldname != $("#companyname").val()){
        Ajax();
	  }
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