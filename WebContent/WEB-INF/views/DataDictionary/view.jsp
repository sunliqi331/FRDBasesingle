<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<script type="text/javascript">
<!--
$(document).ready(function(){
  $.table.init('viewCtl',{toolbar:'#toolBar1'});
});
//-->
</script>
<div class="searchBar">
<div class="search_header">
             <i class="fa fa-search"></i> 词典查询条件
           </div>
  <form class="form-inline" method="post" action="${contextPath }/DataDictionary/data/${pDictionary.id}" data-target="viewCtl" onsubmit="return navTabSearch(this)">
    <div class="form-group">
      <label for="inputText" class="searchtitle">名称</label> <input type="search" class="form-control searchtext" id="inputText" name="search_LIKE_name" value="${param.search_LIKE_name }" />
    </div>
    <button type="submit" class="btn btn-info btn-search">搜索</button>
  </form>
</div>
<form method="post" action="${contextPath }/DataDictionary/view" class="form form-horizontal" onsubmit="validateCallback(this, subPageCallback);">
      <div id="toolBar1">
          <div class="btn-group pull-left">
              <a class="btn btn-default1 btn-tool" target="dialog" refresh="true" rel="create${pDictionary.id}" mask="true" href="${contextPath }/DataDictionary/create?pid=${pDictionary.id}">
                <i class="fa fa-plus"></i>
                <span>添加词典</span>
              </a>
              <a class="btn btn-default1 btn-tool" target="dialog" data-target="viewCtl" rel="update" refresh="true" mask="true" href="${contextPath }/DataDictionary/update/{slt_uid}">
                <i class="fa fa-pencil"></i>
                <span>编辑词典</span>
              </a>
               <a class="btn btn-default1 btn-tool" target="selectedTodo" data-target="viewCtl" refresh="true" rel="ids" href="${contextPath }/DataDictionary/delete" title="确认要删除该角色?">
                   <i class="fa fa-remove"></i> 
                   <span>删除词典</span>
               </a>
          </div>
      </div>
           <table class="table table-striped" id="viewCtl" width="100%" data-field="DataDictionary" data-url="${contextPath }/DataDictionary/data/${pDictionary.id}">
           <thead>
            <tr target="slt_uid" rel="${item.id}">
              <th data-checkbox="true" width="22">
                <input class="cbr checkboxCtrl" type="checkbox" group="ids" >
              </th>
<!--               <th data-field="id" width="20%">序号</th> -->
              <th data-field="name" width="20%">名称</th>
              <th data-field="value" width="40" >内容</th>
            </tr>
           </thead>
           </table>
</form>
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
<div class="modal-footer">
  <button type="button" class="btn btn-default" data-dismiss="pageswitch">返回</button>
  </div>