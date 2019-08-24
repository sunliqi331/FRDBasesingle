<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/WEB-INF/views/include.inc.jsp"%>
<link href="${contextPath }/styles/chosen/chosen.css" rel="stylesheet">
<link href="${contextPath}/styles/css/count.css" rel="stylesheet" />
<script src="${contextPath}/styles/echarts/echarts.min.js"></script>
<!DOCTYPE html>
<html>
<head>
<title>生产记录</title>
<link href="${contextPath }/styles/css/analyse/analyse.css" rel="stylesheet">
<style>
        html{
            height: 100%;
            overflow: hidden;//overflow去掉滚动条
        }

        body{
            height: 100%;
        }
</style>
<script type="text/javascript">
// var lineOptionData = ${lineOptionData};
//var lineOptionData1 = JSON.parse(${lineOptionData});
//console.log(lineOptionData1.grid);
var contextPathJS = "${contextPath}";
var seriesData = ${seriesData};
var seriesPercentListData = ${seriesPercentListData};
</script>
</head>
<body>
<form method="post" id="_formID" class="form-inline">
</form>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<div class="maindiv" id="maindiv">
    <div id="maindata1" class="maindata1">1231232323232312321</div>
    <div id="maindata2" class="maindata2">1231232323232312321</div>
    <div id="maindata3" class="maindata3">1231232323232312321</div>
    <div id="maindata4" class="maindata4">1231232323232312321</div>
    <div id="maindata5" class="maindata5">1231232323232312321</div>
    <div id="maindata6" class="maindata6">1231232323232312321</div>
    <div class="modal-footer">
<!--           <button id="confirm" type="button" class="btn btn-primary">确定</button> -->
      <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
    </div>
</div>
</body>
</html>
<script src="${contextPath}/js/jquery.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${contextPath }/styles/echarts/echarts.js"></script>
<script type="text/javascript" src="${contextPath }/styles/echarts/chalk.js"></script>
<script type="text/javascript" src="${contextPath }/js/analyse/analyse.js"></script>
<script type="text/javascript" src="${contextPath }/js/analyse/maindoc.js"></script>

<script type="text/javascript" src="${contextPath }/js/jquery-ui.js"></script>
<script type="text/javascript" src="${contextPath }/styles/chosen/chosen.jquery.js"></script>