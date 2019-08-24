<%@ page language="java" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<!DOCTYPE html>
<head>
<title>数据字典</title>
<%@ include file="/WEB-INF/views/com_head.jsp"%>
</head>
<body>
<script type="text/javascript">
<!--
function jumpDictionary() {
	var tabid = navTab.getCurrentNavTab().attr("tabid");
	navTab.reloadFlag(tabid);
}

function back() {
	var url = "${contextPath }/management/system/dictionary/list";
	navTab.getCurrentNavTab().attr("url", url);
	navTab.reload(url);
}
//-->
</script>
  <div id="container">
    <%@ include file="/WEB-INF/views/include.header.jsp"%>
    <%@ include file="/WEB-INF/views/include.sidebar.jsp"%>
    <div class="main-content">
      <ol class="breadcrumb">
        <li><i class="fa fa-home"></i><a
          href="${contextPath}/management/index"> 首页</a></li>
        <li>数据字典</li>
      </ol>
     <div class="main-wrap">
        <div class="main-body">
          <div class="searchBar">
			<dwz:paginationForm action="${contextPath }/management/system/dictionary/list?id=${pDictionary.id }" page="${page }">
				<input type="hidden" name="search_LIKE_name" value="${param['search_LIKE_name']}"/>
			</dwz:paginationForm>
			
			<form class="form-inline" method="post" action="${contextPath }/management/system/dictionary/list?id=${pDictionary.id}" onsubmit="return navTabSearch(this)">				
						 <div class="form-group">
							<c:choose>
								<c:when test="${dictionaryType == 'THEME' }">
									<label for="inputText" class="searchtitle">主题名称：</label>
								</c:when>
								<c:otherwise>
									<label for="inputText" class="searchtitle">词条名称：</label>
								</c:otherwise>
							</c:choose>			
							<input type="text" name="search_LIKE_name" value="${param['search_LIKE_name']}"/>	
						</div>
						<button type="submit" class="btn btn-info btn-search">搜索</button>
								<c:if test="${dictionaryType == 'ITEM' }">
							<button type="button" onclick="back();" class="btn btn-default btn-search">返回</button>
								</c:if>
				
			</form>

  </div>

  <div id="toolBar">
            <div class="btn-group pull-left">
			<c:choose>
				<c:when test="${dictionaryType == 'THEME' }">
			<shiro:hasPermission name="Dictionary:save">
				<a class="btn btn-default1" target="dialog" data-target="table" rel="Dictionary_save" href="${contextPath }/management/system/dictionary/create">
                <i class="fa fa-plus"></i>
                <span>添加字典主题</span></a>
			</shiro:hasPermission>
			<shiro:hasPermission name="Dictionary:edit">
				<a class="btn btn-default1" target="dialog" data-target="table"  rel="Dictionary_edit" href="${contextPath }/management/system/dictionary/update/{slt_uid}">
				<i class="fa fa-pencil"></i> 
				<span>编辑字典主题</span></a>
			</shiro:hasPermission>
			<shiro:hasPermission name="Dictionary:delete">
				<a class="btn btn-default1 btn-tool" target="ajaxTodo" data-target="table"  rel="ids" href="${contextPath }/management/system/dictionary/delete" title="确认要删除选定数据字典主题?">
				<i class="fa fa-remove"></i> 
				<span>删除数据字典主题</span></a>
			</shiro:hasPermission>
				</c:when>
				<c:otherwise>
			<shiro:hasPermission name="Dictionary:save">
				<a class="btn btn-default1" target="dialog" data-target="table" rel="Dictionary_save" href="${contextPath }/management/system/dictionary/create?pid=${pDictionary.id}">
				<i class="fa fa-plus"></i>
				<span>添加字典词条</span></a>
			</shiro:hasPermission>
			<shiro:hasPermission name="Dictionary:edit">
				<a class="btn btn-default1" target="dialog" data-target="table" rel="Dictionary_edit" href="${contextPath }/management/system/dictionary/update/{slt_uid}">
				<i class="fa fa-pencil"></i> 
				<span>编辑字典词条</span></a>
			</shiro:hasPermission>
			<shiro:hasPermission name="Dictionary:delete">
				<a class="btn btn-default1" target="dialog" data-target="table" rel="ids" href="${contextPath }/management/system/dictionary/delete" title="确认要删除选定数据字典词条?">
				<i class="fa fa-remove"></i>
				<span>删除数据字典词条</span></a>
			</shiro:hasPermission>
				</c:otherwise>
			</c:choose>		
	 </div>
    </div>
	
	<table class="table table-striped" id="table" layoutH="137" width="100%">
		<thead>
			<tr>
				<th width="22"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
				<th width="100">名称</th>
				<th width="50" orderField="priority" class="${page.orderField eq 'priority' ? page.orderDirection : ''}">序号</th>
				<c:choose>
					<c:when test="${dictionaryType == 'THEME' }">
				<th>描述</th>
				<th width="30">操作</th>
					</c:when>
					<c:otherwise>
				<th>内容</th>
				<th width="100">字典主题</th>
					</c:otherwise>
				</c:choose>	
			</tr>
		</thead>
		<tbody>
			<c:forEach var="item" items="${dictionarys}">
			<tr target="slt_uid" rel="${item.id}">
				<td><input name="ids" value="${item.id}" type="checkbox"></td>
				<td>${item.name}</td>
				<td>${item.priority}</td>
				<td>${item.value}</td>
				<c:choose>
					<c:when test="${dictionaryType == 'THEME' }">
				<td><a iconClass="book_open" href="${contextPath}/management/system/dictionary/list?id=${item.id}" target="navTab" onclick="jumpDictionary();" title="进入词条">进入词条</a></td>
					</c:when>
					<c:otherwise>
				<td>${pDictionary.name }</td>
					</c:otherwise>
				</c:choose>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	   </div>
      </div>
    </div>
  </div>
	<!-- 分页 -->
	<dwz:pagination page="${page }"/>
  <%@ include file="/WEB-INF/views/com_foot.jsp"%>
  <script src="${contextPath}/js/uikit.pageswitch.js"></script>
</body>
</html>