var searchDeviceItems = ["name", "sn"];
var searchDeviceSelectItems = ["typename", "linename"];
var deviceColumns = [
	{
		"radio": true
	},{
		"field": "name",
		"title": "设备名称"
	},{
		"field": "mesDrivertype.typename",
		"title": "设备类型"
	},{
		"field": "sn",
		"title": "设备编号"
	},{
		"field": "mesProductline.linename",
		"title": "产线"
	},{
		"field": "Date",
		"title": "投产日期"
	}
];
var mesPointsColumns = [
                 	{
                 		"radio": true
                 	},{
                 		"field": "name",
                 		"title": "测点名称"
                 	},{
                 		"field": "codekey",
                 		"title": "测点ID"
                 	},{
                 		"field": "mesPointType.name",
                 		"title": "测点类型"
                 	},{
                 		"field": "datatype",
                 		"title": "数据类型"
                 	},{
                 		"field": "unit",
                 		"title": "单位"
                 	}
                 ];
var MONITOR_PROPS = {
	"PassFailureRate": {
		"id": 1,
		"name": "PassFailureRate",
		"description": "合格/不合格"
	},
	"ElectricConsumption": {
		"id": 3,
		"name": "ElectricConsumption",
		"description": "耗电量"
	},
	"WaterConsumption": {
		"id": 4,
		"name": "WaterConsumption",
		"description": "耗水量"
	},
	"GasConsumption": {
		"id": 4,
		"name": "GasConsumption",
		"description": "耗气量"
	},
	"ElectricWaterConsumption": {
		"id": 3,
		"name": "ElectricWaterConsumption",
		"description": "耗电量/耗水量/耗气量"
	},
	"Capacity": {
		"id": 5,
		"name": "Capacity",
		"description": "产量"
	},
	"ProductionProcess": {
		"id": 6,
		"name": "ProductionProcess",
		"description": "生产过程"
	},
	"DriverStatusAnalysis": {
		"id": 7,
		"name": "DriverStatusAnalysis",
		"description": "设备状态对比图"
	},
	"DriverProperty": {
		"id": 8,
		"name": "DriverProperty",
		"description": "设备属性监控"
	}
}
var CHART_MAP = {
	"bar": ["Capacity","ElectricConsumption","WaterConsumption","GasConsumption","ElectricWaterConsumption","PassFailureRate","DriverStatusAnalysis"],
	"line": ["Capacity","ElectricConsumption","WaterConsumption","GasConsumption","ProductionProcess"],
	"pie": ["Capacity","ElectricConsumption","WaterConsumption","GasConsumption","DriverStatusAnalysis"],
	"gauge": ["Capacity","DriverProperty"],
	"scatter": ["PassFailureRate", "Capacity"]
};
var SCOPE = [
	/*{
		"name": "company",
		"description": "公司"
	},*/{
		"name": "factory",
		"description": "工厂"
	},/*{
		"name": "childFactory",
		"description": "子工厂"
	},*/{
		"name": "productLine",
		"description": "产线"
	},{
		"name": "driver",
		"description": "设备"
	}
];

var SCOPE_DRIVER = [
	{
		"name": "driver",
		"description": "设备"
	}
];
var TIMESEQUENCE = [
                    {"name":"5","description":"5分钟"},
                    {"name":"10","description":"10分钟"},
                    {"name":"15","description":"15分钟"},
                    {"name":"20","description":"20分钟"},
                    {"name":"25","description":"25分钟"},
                    {"name":"30","description":"30分钟"}
                   ];

var renderManager = (function(){
	var SEARCH_BAR_HTML = [
		'<div class="searchBar search_driver">',
		'<form class="form-inline" method="get" action="getDeviceName_ajax" data-target="table" onsubmit="return navTabSearch(this);">',
		'<button type="submit" class="btn btn-info  btn-search1">搜索</button>',
		'</form></div>'
	].join('');
	var SEARCH_POINTS_BAR_HTML = [
	                       '<div class="searchBar search_driver">',
	                       '<form class="form-inline" method="get" action="getDeviceName_ajax" data-target="table" onsubmit="return navTabSearch(this);">',
	                       '<button type="submit" class="btn btn-info  btn-search1">搜索</button>',
	                       '</form></div>'
	                       ].join('');
	var SEARCH_GROUP_HTML = [	
		'<div class="form-group">',
		'<label for="searchInput#ID#" class="searchtitle">#TITLE#</label>',
		'<input type="text" class="form-control searchtext" id="searchInput#ID#" name="#SEARCHNAME#">',
		'</div>'
	].join('');
	var SEARCH_GROUP_SELECT_HTML = [
	                 		'<div class="form-group">',
	                 		'<label for="searchInput#ID#" class="searchtitle">#TITLE#</label>',
	                 		'<select class="form-control" id="searchInput#ID#" name="#SEARCHNAME#"></select>',
	                 		'</div>'
	                 	].join('');
	var SELECT_HTML = [
		'<div class="form-group">',
		'<div class="col-sm-6">',
		'<div class="select select-white">',
		'<span class="form-control placeholder">#DESCRIPTION#</span>',
		'<ul></ul>',
		'<input type="hidden" name="#NAME#" value="#VALUE#" data-attr="#ATTR#">',
		'</div></div></div>'
	].join('');
	var CHECKBOX_HTML = [
        //'<div class="form-group">',
        '<label class="check_label">',
        '<input class="cbr" type="checkbox" name="#NAME#" value="#VALUE#" ><span>#TEXT#</span>',
        '</label>'
       /// '</div>'
        ].join('');
	var CHECKBOX_HTML_PROCESS = [
        //'<div class="form-group">',
        '<label class="check_label">',
        '<input class="cbr" id="#ID#" type="checkbox" name="#NAME#" value="#VALUE#" ><span>#TEXT#</span>',
        '</label>'
       /// '</div>'
        ].join('');
	var SEARCH_DATE_HTML = [
	                        '<div class="form-group">',
	                        '<label class="control-label col-sm-4"></label>',
	                        '<div class="col-sm-6">',
	                        '<input type="text" class="form-control" id="searchStartDate#ID#" name="#SEARCH_START_DATE#">',
	                        '</div></div>'
	                       ].join('');
	var Label_CHK_HTML = [
	           		'<div class="form-group">',
	           		'<div class="col-sm-6">',
	           		'<div class="add_attribute" style="background: none; border: 1px solid #e8e6e6;padding:15px">',
	           		'<div class="filter">',
	           		'<h4 class="media-heading" style="font-size:13px;font-weight:bold">#DESCRIPTION#</h4>',
	           		'<hr class="hr-normal">',
	           		'<div class="form-group">',
	           		'<div class="col-sm-12">',
	           		'<div class="checkbox toNewParameter">',
	           		'</div></div></div>',
	           		'<h4 class="media-heading" style="font-size:13px;font-weight:bold">动态新增</h4>',
	           		'<hr class="hr-normal">',
	           		'<div class="#NAME#Add">',
	           		'<div class="form-group select company">',
	           		'</div>',
	           		'<div class="form-group">',
	           		'<div class="control-label col-sm-8 addBtn">',
	           		'</div></div></div></div>',
	           		'</div></div></div>'
	           	].join('');
	var BindDataDialog = function($widget, $dialog) {
		
		this.$widget = $widget;
		this.$dialog = $dialog;
		
	};
	
	function generateFactory(tmp,callback){
		
		if($(".factory").length == 0){
			tmp.find("select:last").parent().parent().after("<div class='form-group factory'><label class='control-label col-sm-4' style='font-weight: normal;'>工厂</label><div class='col-sm-6'><select class='form-control'></select></div></div>");
		}
		var id = $(".company").find("select").val();
		if(null == id || id == ""){
			return ;
		}
		//ajaxTodo(contextPath+"/procedureMonitor/getFactorynameByCurrentCompany/"+$(".company").find("select").val(),function(data){
		ajaxTodo(contextPath+"/procedureMonitor/getChildFactorynameByFactoryName/"+$(".company").find("select").val(),function(data){	
		$(".factory").find("select").find("option").remove();
			$(".factory").find("select").append("<option value=''>请选择</option>");
			$.each(data,function(idx,item){
				$(".factory").find("select").append("<option value='"+ item.id +"'>"+ item.companyname +"</option>");
			});
			
			$(".factory").find("select").change(function(){
				if($(".produtLine")){
					$(".produtLine").find("select").empty();
					generateLine($(this));
				}
			});
			/*$(".factory").find("select").change(function(){
				if($(".childFactory")){
					$(".childFactory").find("select").empty();
					generateChildFactory($(this));
				}
			});*/
			if(callback)
				callback();
			//select.find("select").append("<option value='aa'>fff</option>");
		},false);
		
	};
	
	//updatedBy:xsq 7.14 增加对应子工厂
	// 生成/绑定子工厂
	function generateChildFactory(tmp,callback){
		
		if($(".childFactory").length == 0){
			tmp.find("select:last").parent().parent().after("<div class='form-group childFactory'><label class='control-label col-sm-4' style='font-weight: normal;'>子工厂</label><div class='col-sm-6'><select class='form-control'></select></div></div>");
		}
		var id = $(".factory").find("select").val();
		if(null == id || id == ""){
			return ;
		}
		ajaxTodo(contextPath+"/procedureMonitor/getChildFactorynameByFactoryName/"+$(".factory").find("select").val(),function(data){
			$(".childFactory").find("select").find("option").remove();
			$(".childFactory").find("select").append("<option value=''>请选择</option>");
			$.each(data,function(idx,item){
				$(".childFactory").find("select").append("<option value='"+ item.id +"'>"+ item.companyname +"</option>");
			});
			$(".childFactory").find("select").change(function(){
				if($(".produtLine")){
					$(".produtLine").find("select").empty();
					generateLine($(this));
				}
			});
			/*$(".childFactory").find("select").change(function(){
				if($(".chiChildFactory")){
					$(".chiChildFactory").find("select").empty();
					generateChiChildFactory($(this));
				}
			});*/
			if(callback)
				callback();
			//select.find("select").append("<option value='aa'>fff</option>");
		},false);
		
	};
	
	// 生成/绑定产线数据和画面元素
	function generateLine(tmp,callback){
		
		if($(".produtLine").length == 0){
			tmp.find("select:last").parent().parent().after("<div class='form-group produtLine'><label class='control-label col-sm-4' style='font-weight: normal;'>产线</label><div class='col-sm-6'><select class='form-control'></select></div></div>");
		}
		var id = $(".childFactory").find("select").val();
		if(id == "" || id == null){
			id = $(".factory").find("select").val();
		}
		if(null == id || id == ""){
			return ;
		}
		ajaxTodo(contextPath+"/productline/getProductlineByCompanyid/"+id,function(data){
			$(".produtLine").find("select").find("option").remove();
			$(".produtLine").find("select").append("<option value=''>请选择</option>");
			if(data == null || data == undefined || data == ""){
				$(".produtLine").find("select").find("option").remove();
				$(".produtLine").find("select").append("<option value=''>您选择的工厂暂时没有产线</option>");
				$(".driver").find("select").find("option").remove();
			}
			$.each(data,function(idx,item){
				$(".produtLine").find("select").append("<option value='"+ item.id +"'>"+ item.linename +"</option>");
			});
			$(".produtLine").find("select").change(function(){
				if($(".driver")){
					$(".driver").find("select").empty();
					generateDriver($(this));
				}
			});
			if(callback)
				callback();
			//select.find("select").append("<option value='aa'>fff</option>");
		},false);
		
	};
	// 生成/绑定设备
	function generateDriver(tmp,callback){
		
		$(".driver").find("select").find("option").remove();
		if($(".driver").length == 0 ){
			tmp.find("select:last").parent().parent().after("<div class='form-group driver'><label class='control-label col-sm-4' style='font-weight: normal;'>设备</label><div class='col-sm-6'><select class='form-control'></select></div></div>");
		}
		var id = $(".produtLine").find("select").val();
		if(null == id || id == ""){
			return ;
		}
		ajaxTodo(contextPath+"/procedureMonitor/getDrivernameByCurrentLine/"+id,function(data){
			$(".driver").find("select").find("option").remove();
			$(".driver").find("select").append("<option value=''>请选择</option>");
			if(data == null || data == undefined || data == ""){
				$(".driver").find("select").find("option").remove();
				$(".driver").find("select").append("<option value=''>无关联设备</option>");
			}
			$.each(data,function(idx,item){
			$(".driver").find("select").append("<option value='"+ item.id +"'>"+ item.name +"</option>");
			});
			var chartsTypeVal = $("input[name='chartsType']").val();
			console.log("设备选择控件类型：" + chartsTypeVal);
			if("gauge" == chartsTypeVal) {
				$(".driver").find("select").change(function(){
					if($(".driverProperty")){
						$(".driverProperty").find("select").empty();
						generateDriverProperty($(this));
					}
				});
			}
			if(callback)
				callback();
			/*select.find("select").append("<option value='aa'>fff</option>");*/
		},false);
		
	};
	// 生成/绑定设备
	function generateDriverProperty(tmp,callback){
		
		$(".driverProperty").find("select").find("option").remove();
		if($(".driverProperty").length == 0 ){
			tmp.find("select:last").parent().parent().after("<div class='form-group driverProperty'><label class='control-label col-sm-4' style='font-weight: normal;'>设备属性</label><div class='col-sm-6'><select class='form-control'></select></div></div>");
		}
		var id = $(".driver").find("select").val();
		if(null == id || id == ""){
			return ;
		}
		ajaxTodo(contextPath+"/procedureMonitor/getpropertyByDriverId/"+id,function(data){
			$(".driverProperty").find("select").find("option").remove();
			$(".driverProperty").find("select").append("<option value=''>请选择</option>");
			if(data == null || data == undefined || data == ""){
				$(".driverProperty").find("select").find("option").remove();
				$(".driverProperty").find("select").append("<option value=''>无设备属性</option>");
			}
			$.each(data,function(idx,item){
			$(".driverProperty").find("select").append("<option value='"+ item.propertykeyid +"'>"+ item.propertyname +"</option>");
			});
			if(callback)
				callback();
			/*select.find("select").append("<option value='aa'>fff</option>");*/
		},false);
		
	};
	// 生产/绑定产线相关数据
	function generateProductionProcess_fordriverAlter(form,$chart,that){
		
		var $form = form;
		$form.find(".form-group:gt(0)").remove();
		$form.find(".modal-footer").remove();
		var factory,childFactory,producLine,driver,product,productProcedure,procedureProperty;
		factory = genFactory(0);
		factory && factory.appendTo($form);
		//updatedBy:xsq 7.14 增加子工厂
		//childFactory = genChildFactory($("input[name=factoryId][type='hidden']").val());
		/*childFactory = genChildFactory($("input[name=factoryId][type='hidden']").val());
		childFactory && childFactory.appendTo($form);*/
		producLine = genProductLine($("input[name='childFactoryId'][type='hidden']").val());
		producLine && producLine.appendTo($form);
		driver = genDrivers($("input[name='productLineId'][type='hidden']").val());
		driver && driver.appendTo($form);
		product = genProduct(0);
		product && product.appendTo($form);
		productProcedure = genProductProcedure($("input[name='productId'][type='hidden']").val());
		productProcedure && productProcedure.appendTo($form);
		procedureProperty = genProcedureProperty($("input[name='productProcedureId'][type='hidden']").val());
		procedureProperty && procedureProperty.appendTo($form);
		var pointsNumOpt = [{name:"15",description:"15"},{name:"30",description:"30"},{name:"60",description:"60"},{name:"120",description:"120"}];
		var pointsNum = renderSelectBox(pointsNumOpt,'pointsNum','请选择展示点数：');
		//pointsNum.appendTo($form);
		//renderDate("startTime","请选择时间：").appendTo($form);
		//var addBtn = $('<div class="form-group"><label class="control-label col-sm-4">参数选择：</label><div class="col-sm-6"><button type="button" class="btn btn-primary" style="width:100px;">添加</button></div></div>');
		//addBtn.appendTo($form);
		//var selectedProp = $('<div class="form-group selectedProp"></div>').appendTo($form);
		//selectedProp.prepend('<label class="control-label col-sm-4">已选参数：</label>');
		
		
		var modalFooter = $('<div class="modal-footer" style="margin-top:15px;"><button class="btn btn-primary">确定</button></div>');
		modalFooter.appendTo($form);
		/*
		addBtn.on('click',function(){
			var flag = true;
			$.each($(".selectedProp").find("input[type='checkbox']"),function(idx,item){
				if($(this).val() == $("input[name='procedurePropertyId'][type='hidden']").val()){
					flag = false;
				}
			});
			if(flag){
				var _procedureProperty = $("input[name='procedurePropertyId'][type='hidden']").parent().parent().parent();
				if($("input[name='procedurePropertyId'][type='hidden']").attr('data-attr') == ''){
					
					swal("该参数未绑定过测点");
				}else{
					$(CHECKBOX_HTML_PROCESS.replace('#ID#', $("input[name='procedurePropertyId'][type='hidden']").val()).replace('#NAME#', 'procedurePropertyIds').replace('#VALUE#',$("input[name='procedurePropertyId'][type='hidden']").val()).replace('#TEXT#',_procedureProperty.find(".select span").text())).appendTo(selectedProp).find("input[type='checkbox']").attr("checked","true");
					$(CHECKBOX_HTML_PROCESS.replace('#NAME#', '_procedurePropertyIds').replace('#VALUE#',$("input[name='procedurePropertyId'][type='hidden']").attr('data-attr')).replace('#TEXT#',"")).appendTo(selectedProp).find("input[type='checkbox']").attr("checked","true").css("display","none");
				}
			}else{
				swal("请勿重复选择！");
			}
		});
		*/
		modalFooter.on('click', function() {
			/*
			if($(".selectedProp").find("input[type='checkbox']").length == 0){
				swal("未添加筛选条件");
				return;
			}
			*/
			$chart.siblings('.image').hide();
			// $chart.attr('data-url', 'getHistoryTrend');
			// $chart.attr('data-pointNum',pointsNum.find("input[type='hidden']").val());
			if (that.$widget.height() == 'auto' || that.$widget.height() == 0){
				that.$widget.height(350);
			}
			var data = $form.serializeArray();
			var productProcedureId;
			$.each(data,function(idx,item){
				 if("productId" == item.name) {
						if("" != item.value && null != item.value) {
							$chart.attr('data-productId', item.value);
							$chart.attr('data-product-name', $("input[name='productId']").prev().prev().html());
						}
				} else if("productProcedureId" == item.name) {
					if("" != item.value && null != item.value) {
						$chart.attr('data-productProcedureId', item.value);
						$chart.attr('data-productProcedure-name', $("input[name='productProcedureId']").prev().prev().html());

					}
				} else if("procedurePropertyId" == item.name) {
					if("" != item.value && null != item.value) {
						$chart.attr('data-procedurePropertyId', item.value);
						var idsId = item.value;
						$chart.attr('data-procedureProperty-name',  $("input[name='procedurePropertyId']").prev().prev().html());
					}
				}
			});
			
			// renderManager.drawCharts($chart, data);
//			var obj = {};
//			obj.name= "category";
//			obj.value= "ProductionProcess";
//			data.push(obj);
			storageManager.saveChartsData(that.$widget.attr('id'), data);

			that.$widget.trigger('change');
			that.$dialog.modal('hide');
		});
		//mark;
		
		
	}

	// 生产/绑定产线相关数据
	function generateProductionProcess(form,$chart,that){
		
		var $form = form;
		$form.find(".form-group:gt(0)").remove();
		$form.find(".modal-footer").remove();

		var factory,childFactory,producLine,driver,product,productProcedure,procedureProperty;
		factory = genFactory(0);
		factory && factory.appendTo($form);
		//updatedBy:xsq 7.14 增加子工厂
		//childFactory = genChildFactory($("input[name=factoryId][type='hidden']").val());
		/*childFactory = genChildFactory($("input[name=factoryId][type='hidden']").val());
		childFactory && childFactory.appendTo($form);*/
		producLine = genProductLine($("input[name='childFactoryId'][type='hidden']").val());
		producLine && producLine.appendTo($form);
		driver = genDrivers($("input[name='productLineId'][type='hidden']").val());
		driver && driver.appendTo($form);
		product = genProduct(0);
		product && product.appendTo($form);
		productProcedure = genProductProcedure($("input[name='productId'][type='hidden']").val());
		productProcedure && productProcedure.appendTo($form);
		procedureProperty = genProcedureProperty($("input[name='productProcedureId'][type='hidden']").val());
		procedureProperty && procedureProperty.appendTo($form);
		var pointsNumOpt = [{name:"15",description:"15"},{name:"30",description:"30"},{name:"60",description:"60"},{name:"120",description:"120"}];
		var pointsNum = renderSelectBox(pointsNumOpt,'pointsNum','请选择展示点数：');
		pointsNum.appendTo($form);
		//renderDate("startTime","请选择时间：").appendTo($form);
		var addBtn = $('<div class="form-group"><label class="control-label col-sm-4">参数选择：</label><div class="col-sm-6"><button type="button" class="btn btn-primary" style="width:100px;">添加</button></div></div>');
		addBtn.appendTo($form);
		var selectedProp = $('<div class="form-group selectedProp"></div>').appendTo($form);
		selectedProp.prepend('<label class="control-label col-sm-4">已选参数：</label>');
		
		var modalFooter = $('<div class="modal-footer" style="margin-top:15px;"><button class="btn btn-primary">确定</button></div>');
		modalFooter.appendTo($form);
		addBtn.on('click',function(){
			var flag = true;
			$.each($(".selectedProp").find("input[type='checkbox']"),function(idx,item){
				if($(this).val() == $("input[name='procedurePropertyId'][type='hidden']").val()){
					flag = false;
				}
			});
			if(flag){
				var _procedureProperty = $("input[name='procedurePropertyId'][type='hidden']").parent().parent().parent();
				if($("input[name='procedurePropertyId'][type='hidden']").attr('data-attr') == ''){
					
					swal("该参数未绑定过测点");
				}else{
					$(CHECKBOX_HTML.replace('#NAME#', 'procedurePropertyIds').replace('#VALUE#',$("input[name='procedurePropertyId'][type='hidden']").val()).replace('#TEXT#',_procedureProperty.find(".select span").text())).appendTo(selectedProp).find("input[type='checkbox']").attr("checked","true");
					$(CHECKBOX_HTML.replace('#NAME#', '_procedurePropertyIds').replace('#VALUE#',$("input[name='procedurePropertyId'][type='hidden']").attr('data-attr')).replace('#TEXT#',"")).appendTo(selectedProp).find("input[type='checkbox']").attr("checked","true").css("display","none");
				}
			}else{
				swal("请勿重复选择！");
			}
		});
		modalFooter.on('click', function() {
			if($(".selectedProp").find("input[type='checkbox']").length == 0){
				swal("未添加筛选条件");
				return;
			}
			$chart.siblings('.image').hide();
			$chart.attr('data-url', 'getHistoryTrend');
			$chart.attr('data-pointNum',pointsNum.find("input[type='hidden']").val());
			if (that.$widget.height() == 'auto' || that.$widget.height() == 0){
				that.$widget.height(350);
			}
			var data = $form.serializeArray();
			
			renderManager.drawCharts($chart, data);
			storageManager.saveChartsData(that.$widget.attr('id'), data);

			that.$widget.trigger('change');
			that.$dialog.modal('hide');
		});
		//mark;
		
		
	}
	// 生成/绑定工序属性
	function genProcedureProperty(id){
		
		var ProcedureProperty = [];
		ajaxTodo(contextPath+"/procedureMonitor/getProcedurePropByProce/"+id,function(data){
			$.each(data,function(idx,item){
				var obj = {};
				obj.name= item.id;
				obj.attr= item.mesPoints != null ? item.mesPoints.codekey : 0;
				obj.description=item.propertyname;
				ProcedureProperty.push(obj);
			});
		},false);
		var $procedureProperty = renderSelectBox(ProcedureProperty,'procedurePropertyId','工序参数：');
		$procedureProperty && $procedureProperty.on('change',function(){
			//$(".selectedProp").find('.check_label').remove();
			//
			
			var codeKey = $procedureProperty.find("li[data-value='"+ $("input[name='procedurePropertyId'][type='hidden']").val() +"']").attr('data-attr');
			$("input[name='procedurePropertyId'][type='hidden']").attr('data-attr',codeKey);
		});
		
		return $procedureProperty;
	}
	// 根据产品ID获取工序
	function genProductProcedure(id){
		
		var ProductProcedure = [];
		ajaxTodo(contextPath+"/procedureMonitor/getProcedureByProduct/"+id,function(data){
			$.each(data,function(idx,item){
				var obj = {};
				obj.name= item.id;
				obj.description=item.procedurename;
				ProductProcedure.push(obj);
			});
		},false);
		// 生成工序数据
		var $productProcedure = renderSelectBox(ProductProcedure,'productProcedureId','工序：');
		$productProcedure && $productProcedure.on('change',function(){
			$(".selectedProp").find('.check_label').remove();
			var procedureProperty = $("input[name='procedurePropertyId'][type='hidden']").parent().parent().parent();
			if(procedureProperty){
				procedureProperty.remove();
			}
			procedureProperty = genProcedureProperty($("input[name='productProcedureId'][type='hidden']").val());
			//productProcedure && productProcedure.appendTo($form);
			procedureProperty && $productProcedure.after(procedureProperty);
		});
		
		return $productProcedure;
	}
	// 根据公司ID获取产品
	function genProduct(id){
		
		var Product = [];
		ajaxTodo(contextPath+"/procedureMonitor/getProductionByCompanyId/"+id,function(data){
			$.each(data,function(idx,item){
				var obj = {};
				obj.name= item.id;
				obj.description=item.name;
				Product.push(obj);
			});
		},false);
		// 生成产品选项
		var $product = renderSelectBox(Product,'productId','产品：');
		$product && $product.on('change',function(){
			$(".selectedProp").find('.check_label').remove();
			var $form = $product.parent().find(".selectedProp");
			var productProcedure = $("input[name='productProcedureId'][type='hidden']").parent().parent().parent();
			if(productProcedure){
				productProcedure.remove();
			}
			productProcedure = genProductProcedure($("input[name='productId'][type='hidden']").val());
			//productProcedure && productProcedure.appendTo($form);
			productProcedure && $product.after(productProcedure);
			//productProcedure = $("input[name='productProcedureId'][type='hidden']").parent().parent().parent();
			var procedureProperty = $("input[name='procedurePropertyId'][type='hidden']").parent().parent().parent();
			if(procedureProperty){
				procedureProperty.remove();
			}
			if(productProcedure){
				procedureProperty = genProcedureProperty($("input[name='productProcedureId'][type='hidden']").val());
				//procedureProperty && procedureProperty.appendTo($form);
				procedureProperty && productProcedure.after(procedureProperty);
			}
		});
		
		return $product;
	}
	// 根据公司获取下属工厂
	function genFactory(id){
		
		var Factory = [];
		if(id){
			ajaxTodo(contextPath+"/procedureMonitor/getFactorynameByCurrentCompany/"+id,function(data){
			$.each(data,function(idx,item){
				var obj = {};
				obj.name= item.id;
				obj.description=item.companyname;
				Factory.push(obj);
			});
		},false);}
		var $factory = renderSelectBox(Factory,'factoryId','工厂：');
		$factory && $factory.on('change',function(){
			$(".selectedProp").find('.check_label').remove();
			var $form = $factory.parent();
			//updatedBy:xsq
			var childFactory = $("input[name='childFactoryId'][type='hidden']").parent().parent().parent();
			if(childFactory){
				childFactory.remove();
			}
			childFactory = genChildFactory($("input[name='factoryId'][type='hidden']")).val();
			$factory.after(childFactroy);
			
			var producLine = $("input[name='productLineId'][type='hidden']").parent().parent().parent();
			if(producLine){
				producLine.remove();
			}
			//producLine = genProductLine($("input[name='factoryId'][type='hidden']").val());
			//producLine && $factory.after(producLine);
			producLine = genProductLine($("input[name='childFactoryId'][type='hidden']").val());
			producLine && childFactory.after(producLine);
			//producLine = $("input[name='productLineId'][type='hidden']").parent().parent().parent();
			var driver = $("input[name='mesDriverId'][type='hidden']").parent().parent().parent();
			if(driver){
				driver.remove();
			}
			if(producLine){
				driver = genDrivers($("input[name='productLineId'][type='hidden']").val());
				driver && producLine.after(driver);
			}
		});
		
		return $factory;
	}
	
	//updatedBy:xsq 增加对应子工厂
	
	/*function genChildFactory(id){
		var childFactory = [];
		if(id){
			ajaxTodo(contextPath+"/procedureMonitor/getChildFactorynameByFactoryName/"+id,function(data){
			$.each(data,function(idx,item){
				var obj = {};
				obj.name= item.id;
				obj.description=item.factory;
				childFactory.push(obj);
			});
		},false);}
		var $childFactory = renderSelectBox(childFactory,'childFactoryId','子工厂：');
		$childFactory && $childFactory.on('change',function(){
			$(".selectedProp").find('.check_label').remove();
			var producLine = $("input[name='productLineId'][type='hidden']").parent().parent().parent();
			var $form = $childFactory.parent();
			if(producLine){
				producLine.remove();
			}
			producLine = genProductLine($("input[name='childFactoryId'][type='hidden']").val());
			producLine && $childFactory.after(producLine);
			//producLine = $("input[name='productLineId'][type='hidden']").parent().parent().parent();
			var driver = $("input[name='mesDriverId'][type='hidden']").parent().parent().parent();
			if(driver){
				driver.remove();
			}
			if(producLine){
				driver = genDrivers($("input[name='productLineId'][type='hidden']").val());
				driver && producLine.after(driver);
			}
		});
		return $childFactory;
	}*/
	
	
	function genProductLine(id){
		
		var ProductLine = [];
		if(id){
			ajaxTodo(contextPath+"/procedureMonitor/getProductLineByCompanyId/"+id,function(data){
				$.each(data,function(idx,item){
					var obj = {};
					obj.name= item.id;
					obj.description=item.linename;
					ProductLine.push(obj);
				});
			},false);
		}
		
		var $productLine = renderSelectBox(ProductLine,'productLineId','产线：');
		$productLine && $productLine.on('change',function(){
			$(".selectedProp").find('.check_label').remove();
			var driver = $("input[name='mesDriverId'][type='hidden']").parent().parent().parent();
			var $form = $productLine.parent();
			if(driver){
				driver.remove();
				driver = genDrivers($("input[name='productLineId'][type='hidden']").val());
				driver && ($("input[name='productLineId'][type='hidden']").parent().parent().parent()).after(driver);
			}
		});
		
		return $productLine;
	}
	function genDrivers(id){
		
		var Driver = [];
		if(id){
			ajaxTodo(contextPath+"/procedureMonitor/getDrivernameByCurrentLine/"+id,function(data){
				$.each(data,function(idx,item){
					var obj = {};
					obj.name= item.id;
					obj.description=item.name;
					Driver.push(obj);
				});
			},false);
		}
		
		var $driver = renderSelectBox(Driver,'mesDriverId','设备：');
		
		return $driver;
	}

	function _generateSpcAnalysisProcess(form,$chart,that,flag_){
		var $form = form;
		// var data = $form.serializeArray();
		var url = 'getSpcAnalysisJson_ajax';
		$chart.attr('data-url', url);
		//绑定数据提交
		// $('<div class="modal-footer" style="margin-top:15px;"><button class="btn btn-primary">确定</button></div>')
		//     .appendTo($form)
		$form.find("#formID").find("#submit")
			.on('click', function() {
				var data = $form.find("#formID").serializeArray();
				var $dialog = $.pdialog.getUpper();
				// var params = $dialog.data("params");
				// if(params) {
				// 	params = DWZ.jsonEval(params);
				// }

				$chart.siblings('.image').hide();
				// 在后端生成图表的各项元素
				if (that.$widget.height() == 'auto' || that.$widget.height() == 0){
					that.$widget.height(350);
					that.$widget.width(350);
				}
				renderManager.drawSpcCharts($chart, data);
				storageManager.saveSpcAnalysisData(that.$widget.attr('id'), data);
				that.$widget.trigger('change');
				that.$dialog.modal('hide');
			});
	}
	/* start slq 2019-05-09 */
	function _generateSpcProcess(form,$chart,that,flag_){
		var $form = form;
		// var data = $form.serializeArray();
		var url = 'getSpcJson_ajax';
		$chart.attr('data-url', url);
		//绑定数据提交
		// $('<div class="modal-footer" style="margin-top:15px;"><button class="btn btn-primary">确定</button></div>')
		//     .appendTo($form)
		$form.find("#formID").find("#submit")
			.on('click', function() {
				var data = $form.find("#formID").serializeArray();
				var $dialog = $.pdialog.getUpper();
				// var params = $dialog.data("params");
				// if(params) {
				// 	params = DWZ.jsonEval(params);
				// }

				$chart.siblings('.image').hide();
				// 在后端生成图表的各项元素
				if (that.$widget.height() == 'auto' || that.$widget.height() == 0){
					that.$widget.height(350);
					that.$widget.width(350);
				}
				renderManager.drawSpcCharts($chart, data);
				storageManager.saveSpcData(that.$widget.attr('id'), data);
				that.$widget.trigger('change');
				that.$dialog.modal('hide');
			});
	}
	/* end */
	function _generateProductionProcess(form,$chart,that,flag_){
		var chartsTypeVal = $("input[name='chartsType']").val();
		console.log("控件类型：" + chartsTypeVal);

		var $form = form;
		$form.find(".form-group:gt(0)").remove();
		$form.find(".modal-footer").remove();
//		var searchScope = SCOPE;
//		if("gauge" == chartsTypeVal){
//			searchScope = SCOPE_DRIVER;
//		}
		var tmp ;
		renderSelectBox(SCOPE, 'scope', '统计范围：')
		.appendTo($form)
		.on('change', function(){
			$(".toNewParameter").empty();
			var $this = $(this);
			switch ($this.find('input[type=hidden]').val()) {
				case 'company':
					$(".factory").remove();
					$(".childFactory").remove();
					$(".produtLine").remove();
					$(".driver").remove();
				break;
				case 'factory':
					$(".childFactory").remove();
					$(".produtLine").remove();
					$(".driver").remove();
						generateFactory(tmp);
						$(".factory").find("select").change(function(){
							if($(".produtLine")){
								generateLine($(this));
							}
						});
					
				break;
				/*case 'childFactory':
					$(".produtLine").remove();
					$(".driver").remove();
					generateChildFactory(tmp);
						$(".childFactory").find("select").change(function(){
							if($(".produtLine")){
								generateLine($(this));
							}
						});
					
				break;*/
				/*case 'productLine': 
					$(".driver").remove();
					if($(".factory").length == 0){
						generateFactory(tmp,function(){
							generateLine(tmp);
						});
					}
					else{
						generateLine(tmp);
					}
					$(".produtLine").find("select").change(function(){
						if($(".driver")){
							generateDriver($(this));
						}
					})
					
				break;*/
				
				
				//updatedBy:xsq 
				case 'productLine': 
					$(".driver").remove();
					$(".childFactory").remove();
					generateLine(tmp);
					/*if($(".factory").length == 0){
						generateFactory(tmp,function(){
							generateLine(tem);
						})
					}else{
						generateChildFactory(tmp);
					};
					if($(".childFactory").length == 0){
						generateChildFactory(tmp,function(){
							generateLine(tmp);
						});
					}else{
						generateLine(tmp);
					};*/
					$(".produtLine").find("select").change(function(){
						if($(".driver")){
							generateDriver($(this));
						}
					})
					
					
				break;
				case 'driver':
					//$(".factory").remove();
					$(".childFactory").remove();
					//$(".produtLine").remove();
					//$(".driver").remove();
					generateFactory(tmp);
					//generateChildFactory(tmp);
					generateLine(tmp);
					generateDriver(tmp);
					if("gauge" == chartsTypeVal) {
						generateDriverProperty(tmp);
					}

			}
		});
		if(flag_){
			renderDate("begin","请选择时间：").appendTo($form);
		}
		tmp = renderLabelBox(SCOPE,'筛选：').appendTo($form);
		$('<button type="button" class="btn btn-primary" style="width:100px;">添加</button>').appendTo(tmp.find(".addBtn"))
		.on('click',function(){
			var flag = true;
			$.each($(".toNewParameter").find("input[type='checkbox']"),function(idx,item){
				if($(this).val() == tmp.find("select:last").find("option:selected").html()){
					flag = false;
				}
			});
			if(flag){
				
				if(tmp.find("select:last").find("option:selected").html() == '' || null == tmp.find("select:last").find("option:selected").html()){
					swal("您添加的数据不存在");
				}else if(tmp.find("select:last").find("option:selected").html()=="请选择"){
					swal("请选择具体数据");
				}else
				    $(".toNewParameter").append("<label class='check_label'><input class='cbr' type='checkbox' name='map[\""+ tmp.find("select:last").val() +"\"]' checked='true' value='"+tmp.find("select:last").find("option:selected").html()+"'>"+tmp.find("select:last").find("option:selected").html()+"");
			}
			else{
				swal("请勿重复选择");
			}
			
		});
		//renderSelectBox(TIMESEQUENCE,'timeSequence','刷新频率').appendTo($form);
		
		var url = 'getJson_ajax';
		//$form.append($('<a class="btn btn-info" data-dismiss="modal">确定</a>'));
		$('<div class="modal-footer" style="margin-top:15px;"><button class="btn btn-primary">确定</button></div>')
		.appendTo($form)
		.on('click', function() {
			var flag = false;
			$.each($(".toNewParameter").find("input[type='checkbox']"),function(idx,item){
				if($(item).prop("checked")){
					flag = true;
				}
			});
			if($(".toNewParameter").find("input[type='checkbox']").length == 0){
				swal("未添加筛选条件");
				return;
			}
			if(!flag){
				swal("未选择筛选条件");
				return;
			}
			$chart.siblings('.image').hide();
			// 在后端生成图表的各项元素
			$chart.attr('data-url', url);
			if (that.$widget.height() == 'auto' || that.$widget.height() == 0){
				that.$widget.height(350);
			}
			var data = $form.serializeArray();
			if("gauge" == chartsTypeVal) {
			    // openId
				data.push({name: "driverId", value: $(".driver").find("option:selected").val()});
			}
			if($chart.attr('data-pietype'))
			data.push({name : "pieType", value : $chart.attr('data-pietype')});
			renderManager.drawCharts($chart, data);
			storageManager.saveChartsData(that.$widget.attr('id'), data);

			that.$widget.trigger('change');
			that.$dialog.modal('hide');
		});

	}
	BindDataDialog.prototype = {
		renderSpcAnalysis: function(){
			var that = this;
			var $modalBody = $(".modal-body > .pageContent", this.$dialog);
			this.$dialog.modal().on('hidden.bs.modal', function (e) {
				$(this).remove();
			});
			var $chart = this.$widget.find('.view > .chart-container');
			var type = $chart.attr('data-type');
			var options = [];
			$.each(CHART_MAP[type], function(i, v) {
				options.push(MONITOR_PROPS[v]);
			});
			var $form = $('<form style="height:550px;width" action="" class="form form-horizontal" onsubmit="return false;"><input type="hidden" name="chartsType" value="'+ $chart.attr("data-type") +'"/><input type="hidden" name="name" value="' + this.$widget.attr("id") + '"></form>')

			$.ajax({
				url: contextPath+"/statistics/monitor_spc_analysis",
				type: 'get',
				success: function (data, status) {
					$form.html("");
					$form.html(data);
					$modalBody.append($form);
					$form.find("#formID .chosen-container-single").css("width","135px");
					_generateSpcAnalysisProcess($form,$chart,that);
					// console.log(data)
				},
				fail: function (err, status) {
					console.log(err)


				}
			})
		},
		/* slq 20190506 */
		renderSpc: function(){
			var that = this;
			var $modalBody = $(".modal-body > .pageContent", this.$dialog);
			this.$dialog.modal().on('hidden.bs.modal', function (e) {
				$(this).remove();
			});
			var $chart = this.$widget.find('.view > .chart-container');
			var type = $chart.attr('data-type');
			var options = [];
			$.each(CHART_MAP[type], function(i, v) {
				options.push(MONITOR_PROPS[v]);
			});
			var $form = $('<form style="height:550px;width" action="" class="form form-horizontal" onsubmit="return false;"><input type="hidden" name="chartsType" value="'+ $chart.attr("data-type") +'"/><input type="hidden" name="name" value="' + this.$widget.attr("id") + '"></form>')
			// renderSelectBox(options, 'category', '统计数据类型：').appendTo($form).change(function(evt){
			//     if($("input[name='category'][type='hidden']").val() == 'ProductionProcess'){
			//         generateProductionProcess($form,$chart,that);
			//     }else if($("input[name='category'][type='hidden']").val() == 'Capacity'){
			//         _generateProductionProcess($form,$chart,that,true);
			//     }else{
			//         _generateProductionProcess($form,$chart,that);
			//     }
			// });

			// _generateProductionProcess($form,$chart,that,true);

			// $form.load("spc.jsp");

			//从缓存中取出已绑定数据,时间
			// var BChartData = $.parseJSON(localStorage.getItem("chartsData"));
			// var BChartId = $form.find("input[name='name']").val();
			// if(BChartData && BChartId && (BChartData.length > 0)){
			//     $.each(BChartData, function(idx,obj){
			//         if(obj.chartId == BChartId){
			//             var Bdata = $.parseJSON(obj.data);
			//         }
			//     });
			// }

			$.ajax({
				url: contextPath+"/statistics/monitor_spc",
				type: 'get',
				// dataType: 'json',
				// async:false,
				// timeout: 2000,
				success: function (data, status) {
					// $form.html(data)
					$(data).appendTo($form);
					$modalBody.append($form);
					$form.find("#formID .chosen-container-single").css("width","135px");
					_generateSpcProcess($form,$chart,that);
					//
					//
					// var BChartData = $.parseJSON(localStorage.getItem("spcData"));
					// var BChartId = $form.find("input[name='name']").val();
					// if(BChartData && BChartId && (BChartData.length > 0)){
					// 	$.each(BChartData, function(idx,obj){
					// 		if(obj.chartId == BChartId){
					// 			var Bdata = $.parseJSON(obj.data);
					// 			$.each(Bdata, function(_idx, _obj){
					// 				//子组数量
					// 				if ((_obj.name == "subNum") && _obj.value){
					//
					// 				}
					//
					// 				if((_obj.name.indexOf("map"))!=-1){
					// 					($form.find(".toNewParameter")).append("<label class='check_label'><input class='cbr' type='checkbox' name='"
					// 						+_obj.name+"' checked='true' value='"+_obj.value+"'>"+_obj.value+"");
					// 				}
					// 				if((_obj.name=='begin') && _obj.value){
					// 					$form.find("input[name='begin']").val(_obj.value);
					// 				}
					// 				if((_obj.name=='category') && _obj.value){
					// 					$form.find("input[name='category'][type='hidden']").val(_obj.value);
					// 				}
					// 				if((_obj.name=='scope') && _obj.value){
					// 					$form.find("input[name='scope'][type='hidden']").val(_obj.value);
					// 				}
					// 			});
					// 		}
					// 	});
					// }
				},
				fail: function (err, status) {
					console.log(err)


				}
			})

			// ajaxTodo(contextPath+"/statistics/monitor_spc",function(data){
			// 	$form.html(data);
			// 	$modalBody.append($form);
			// 	// $.each(data,function(idx,item){
			// 		console.log(1111);
			// 	// });
			// });

		},
		/* end */
		renderChart: function(){

			var that = this;
			var $modalBody = $(".modal-body > .pageContent", this.$dialog);
			this.$dialog.modal().on('hidden.bs.modal', function (e) {
				$(this).remove();
			});

			var $chart = this.$widget.find('.view > .chart-container');
			var type = $chart.attr('data-type');
			var options = [];
			$.each(CHART_MAP[type], function(i, v) {
				options.push(MONITOR_PROPS[v]);
			});
			var $form = $('<form action="" class="form form-horizontal" onsubmit="return false;"><input type="hidden" name="chartsType" value="'+ $chart.attr("data-type") +'"/><input type="hidden" name="name" value="' + this.$widget.attr("id") + '"></form>')
			renderSelectBox(options, 'category', '统计数据类型：').appendTo($form).change(function(evt){
				if($("input[name='category'][type='hidden']").val() == 'ProductionProcess'){
					generateProductionProcess($form,$chart,that);
				}else if($("input[name='category'][type='hidden']").val() == 'Capacity'){
					_generateProductionProcess($form,$chart,that,true);
				}else{
					_generateProductionProcess($form,$chart,that);
				}
			});
			_generateProductionProcess($form,$chart,that,true);

			//从缓存中取出已绑定数据,时间
			var BChartData = $.parseJSON(localStorage.getItem("chartsData"));
			var BChartId = $form.find("input[name='name']").val();
			if(BChartData && BChartId && (BChartData.length > 0)){
				$.each(BChartData, function(idx,obj){
					if(obj.chartId == BChartId){
						var Bdata = $.parseJSON(obj.data);
						$.each(Bdata, function(_idx, _obj){
							if((_obj.name.indexOf("map"))!=-1){
								($form.find(".toNewParameter")).append("<label class='check_label'><input class='cbr' type='checkbox' name='"
										+_obj.name+"' checked='true' value='"+_obj.value+"'>"+_obj.value+"");
							}
							if((_obj.name=='begin') && _obj.value){
								$form.find("input[name='begin']").val(_obj.value);
							}
							if((_obj.name=='category') && _obj.value){
								$form.find("input[name='category'][type='hidden']").val(_obj.value);
							}
							if((_obj.name=='scope') && _obj.value){
								$form.find("input[name='scope'][type='hidden']").val(_obj.value);
							}
						});
					}
				});
			}

			$modalBody.append($form);
			//$form.appendTo($modalBody);
		//	$("div").removeClass("modal-lg");

		},
		renderTextBox: function(){

			var that = this;
			var data = that.$widget.find(".widget-word").parent();
			//
			var op = {};
		    op.title = "测点数据源";
		    op.destroyOnClose = true;
		    op.url = "mesPointsMonitorPage?bindType=textBox";
		    //
		    op.data = {"data-mac":data.attr("data-mac"),"data-codekey":data.attr("data-codekey"),"data-countTime":data.attr("data-countTime")};
		    op.close = function(params){
		    	that.$widget.data("params",JSON.stringify(params));
		    	that.$widget.editableWidget('bindData');
		    };
		    $.pdialog.open("driverStatus",op);

		},
		renderCanvas: function(){

			var that = this;
			var data = that.$widget.find("canvas");
			var op = {};
		    op.title = "测点数据源";
		    op.destroyOnClose = true;
		    op.url = "mesPointsMonitorPage?bindType=circular";
		    op.data = {"data-mac":data.attr("data-mac"),"data-codekey":data.attr("data-codekey")};
		    op.close = function(params){
		    	that.$widget.data("params",JSON.stringify(params));
		    	that.$widget.editableWidget('bindData');
		    };
		    $.pdialog.open("driverStatus",op);

		},
		renderCanvasGateWay: function(){

			var that = this;
			var data = that.$widget.find("canvas");
			var op = {};
		    op.title = "网关";
		    op.destroyOnClose = true;
		    op.url = "mesGateWayMonitorPage?bindType=circular";
		    op.data = {"data-mac":data.attr("data-mac"),"data-codekey":data.attr("data-codekey")};
		    op.close = function(params){
		    	that.$widget.data("params",JSON.stringify(params));
		    	that.$widget.editableWidget('bindData');
		    };
		    $.pdialog.open("driverStatus",op);

		},
		renderCanvasProceseAlter: function(){

			var that = this;
			var $modalBody = $(".modal-body > .pageContent", this.$dialog);
			this.$dialog.modal().on('hidden.bs.modal', function (e) {
				$(this).remove();
			});

			// var $chart = this.$widget.find('.view > .chart-container');
			var $chart = that.$widget.find("canvas");
			var type = $chart.attr('data-type');
			/*
			var options = [];
			$.each(CHART_MAP[type], function(i, v) {
				options.push(MONITOR_PROPS[v]);
			});
			*/
			$chart.attr("category", "ProductionProcess");
			var $form = $('<form action="" class="form form-horizontal" onsubmit="return false;"><input type="hidden" name="chartsType" value="'+ $chart.attr("data-type") +'"/><input type="hidden" name="name" value="' + this.$widget.attr("id") + '"></form>')
			generateProductionProcess_fordriverAlter($form,$chart,that);
			/*
			renderSelectBox(options, 'category', '统计数据类型：').appendTo($form).change(function(evt){

				if($("input[name='category'][type='hidden']").val() == 'ProductionProcess'){
					generateProductionProcess($form,$chart,that);
				}else if($("input[name='category'][type='hidden']").val() == 'Capacity'){
					_generateProductionProcess($form,$chart,that,true);
				}else{
					_generateProductionProcess($form,$chart,that);
				}

			});
			*/
			// _generateProductionProcess($form,$chart,that,true);

			//从缓存中取出已绑定数据,时间
			/*
			var BChartData = $.parseJSON(localStorage.getItem("chartsData"));
			var BChartId = $form.find("input[name='name']").val();
			if(BChartData && BChartId && (BChartData.length > 0)){
				$.each(BChartData, function(idx,obj){
					if(obj.chartId == BChartId){
						var Bdata = $.parseJSON(obj.data);
						$.each(Bdata, function(_idx, _obj){
							if((_obj.name.indexOf("map"))!=-1){
								($form.find(".toNewParameter")).append("<label class='check_label'><input class='cbr' type='checkbox' name='"
										+_obj.name+"' checked='true' value='"+_obj.value+"'>"+_obj.value+"");
							}
							if((_obj.name=='begin') && _obj.value){
								$form.find("input[name='begin']").val(_obj.value);
							}
							if((_obj.name=='category') && _obj.value){
								$form.find("input[name='category'][type='hidden']").val(_obj.value);
							}
							if((_obj.name=='scope') && _obj.value){
								$form.find("input[name='scope'][type='hidden']").val(_obj.value);
							}
						});
					}
				});
			}
			*/
			$modalBody.append($form);

		},

        renderCanvasQualified: function(){

            var that = this;
            var $modalBody = $(".modal-body > .pageContent", this.$dialog);
            this.$dialog.modal().on('hidden.bs.modal', function (e) {
                $(this).remove();
            });

            var $chart = that.$widget.find("canvas");
            var type = $chart.attr('data-type');

            $chart.attr("category", "Product");
            var $form = $('<form action="" class="form form-horizontal" onsubmit="return false;"><input type="hidden" name="chartsType" value="'+ $chart.attr("data-type") +'"/><input type="hidden" name="name" value="' + this.$widget.attr("id") + '"></form>')

			$form.find(".form-group:gt(0)").remove();
            $form.find(".modal-footer").remove();

            var Product = [];
            ajaxTodo(contextPath+"/procedureMonitor/getProductionByCompanyId/0",function(data){
                $.each(data,function(idx,item){
                    var obj = {};
                    obj.name= item.id;
                    obj.description=item.name;
                    Product.push(obj);
                });
            },false);
            // 生成产品选项
            var $product = renderSelectBox(Product,'productId','产品：');
            $product && $product.appendTo($form);

            var modalFooter = $('<div class="modal-footer" style="margin-top:15px;"><button class="btn btn-primary">确定</button></div>');
            modalFooter.appendTo($form);

            modalFooter.on('click', function() {
                $chart.siblings('.image').hide();
                if (that.$widget.height() == 'auto' || that.$widget.height() == 0){
                    that.$widget.height(350);
                }
                var data = $form.serializeArray();
                var productProcedureId;
                $.each(data,function(idx,item){
                    if("productId" == item.name) {
                        if("" != item.value && null != item.value) {
                            $chart.attr('data-productId', item.value);
                            $chart.attr('data-product-name', $("input[name='productId']").prev().prev().html());
                        }
                    }
                });
                storageManager.saveChartsData(that.$widget.attr('id'), data);

                that.$widget.trigger('change');
                that.$dialog.modal('hide');
            });
            $modalBody.append($form);
        },


		renderDevice: function(){
			
			var that = this;
			
			var driverId = that.$widget.attr("data-url") && that.$widget.attr("data-url").split("?")[1];
			var $modalBody = $(".modal-body > .pageContent", this.$dialog);
			this.$dialog.modal().on('hidden.bs.modal', function (e) {
				$(this).remove();
			}).on('shown.bs.modal', function () {
				renderTable($table, {
					url: 'getDeviceName_ajax?search_EQ_'+driverId,
					dataField: 'mesDrivers',
					pagination: true,
					pageSize: 10,
					singleSelect: true,
					columns: deviceColumns
				}, function(data) {
					//
					if(data && data.mesDrivers.length == 1){
						$table.bootstrapTable('checkAll');
	    	    	}
				});
	        	});
			var $form = $modalBody.append(SEARCH_BAR_HTML).find('form');
			$form.find('button').on('click',function(){
				$table.bootstrapTable('destroy');
				renderTable($table, {
					url: 'getDeviceName_ajax',
					dataField: 'mesDrivers',
					pagination: true,
					pageSize: 10,
					singleSelect: true,
					columns: deviceColumns
				}, function() {
					//
				});
				$form.submit();
			});
			var groupHtmls = ''
			$.each(deviceColumns, function(i,v) {
				if (searchDeviceItems.indexOf(v.field) != -1) {
					groupHtmls += SEARCH_GROUP_HTML
					.replace(/#ID#/g, v.field)
					.replace(/#SEARCHNAME#/g, 'search_LIKE_' + v.field)
					.replace(/#TITLE#/g, v.title);
				}
			})
			$form.prepend(groupHtmls);
			
			var deviceTypeSe = $(SEARCH_GROUP_SELECT_HTML.replace(/#ID#/g, "deviceType")
					.replace(/#SEARCHNAME#/g, 'search_EQ_mesDrivertype.id')
					.replace(/#TITLE#/g, "设备类型"));
			$form.prepend(deviceTypeSe);
			$.ajax({
				url:contextPath+"/procedureMonitor/getMesDriverTypeList",
				type:"POST",
				dataType:"JSON",
				async:false,
				success:function(data){
					$.each(data,function(idx,item){
						deviceTypeSe.find('select').append("<option value='"+ item.id +"'>"+ item.typename +"</option>");
					});
				}
			});
			var productionLineSe = $(SEARCH_GROUP_SELECT_HTML.replace(/#ID#/g, "productionLine")
					.replace(/#SEARCHNAME#/g, 'search_EQ_mesProductline.id' )
					.replace(/#TITLE#/g, "产线"));
			$form.prepend(productionLineSe);
			$.ajax({
				url:contextPath+"/procedureMonitor/getProductionLinesByCurrentUser",
				type:"POST",
				dataType:"JSON",
				async:false,
				success:function(data){
					$.each(data,function(idx,item){
						productionLineSe.find('select').append("<option value='"+ item.id +"'>"+ item.linename +"</option>");
					});
				}
			});
			var monitorTypeSe = $(SEARCH_GROUP_SELECT_HTML.replace(/#ID#/g, "differencetype")
					.replace(/#SEARCHNAME#/g, 'search_EQ_differencetype' )
					.replace(/#TITLE#/g, "监控设备"));
			$form.prepend(monitorTypeSe);
			monitorTypeSe.find("select").append('<option value="1" selected>设备</option><option value="0">网关</option>');
			monitorTypeSe.change(function(){
				
				var $val = monitorTypeSe.find("option:selected").attr("value");
				if($val == '0'){
					deviceTypeSe.hide();
					deviceTypeSe.find("select").attr("disabled","disabled");
					productionLineSe.hide();
					productionLineSe.find("select").attr("disabled","disabled");
				}else{
					deviceTypeSe.show();
					deviceTypeSe.find("select").removeAttr("disabled");
					productionLineSe.show();
					productionLineSe.find("select").removeAttr("disabled");
				}
				$.table.setCurrent("table");
				$.table.refreshCurrent('getDeviceName_ajax?numPerPage=10&pageNum=1&sortOrder=asc&search_EQ_differencetype='+ monitorTypeSe.find("select").val()  +'&search_LIKE_name=&search_LIKE_sn=');
			});
			var $table = $('<table id="table" class="table-striped"></table>').appendTo($modalBody);
			
			$('<div class="modal-footer" style="margin-top:15px;"><button class="btn btn-primary">确定</button></div>')
			.appendTo($modalBody)
			.on('click', function() {
				var selections = $table.bootstrapTable('getSelections');
				var url = 'getDeviceProperties_ajax?id='+selections[0].id;
				that.$widget.editableWidget('bindData', url).on('bindTextCreate', storageManager.initBindText);
				that.$dialog.modal('hide');
			});
			/*var that = this;
			var op = {};
		    op.title = "设备数据源";
		    op.destroyOnClose = true;
		    op.url = "mesDriverMonitorPage";
		    op.close = function(params){
		    	that.$widget.data("params",JSON.stringify(params));
		    	that.$widget.editableWidget('bindData');
		    };
		    $.pdialog.open("driverStatus",op);*/
			
		},
		
		
		renderAlarmTable: function(){
			
			var that = this;
			var $modalBody = $(".modal-body > .pageContent", this.$dialog);
			this.$dialog.modal().on('hidden.bs.modal', function (e) {
				$(this).remove();
			});
			var $AlarmTable = this.$widget.find('.view > .AlarmTable-container > table' );
			var columns = $AlarmTable.find("thead > tr > th");
			
			var type = $AlarmTable.attr('data-type');
			var $form = $('<form action="" class="form form-horizontal" onsubmit="return false;"></form>');
			var companyOption = new Array();
			ajaxTodo(contextPath+"/procedureMonitor/getFactorynameByCurrentCompany/0",function(data){
				$.each(data,function(idx,item){
					var obj = {};
					obj.name = item.id;
					obj.description = item.companyname;
					companyOption.push(obj);
				});
				var factoryForm, lineForm,driverForm,countLineForm;
				
				factoryForm = renderSelectBox(companyOption,'factory','请选择工厂').appendTo($form).change(function(){
					getLineByFactoryId(lineForm);
				});
				 lineForm = renderSelectBox([{name:'',description:''}],'line','请选择产线').appendTo($form).change(function(){
					 getDriverByLineId(driverForm);
				 });
				 driverForm = renderSelectBox([{name:'',description:''}],'driver','请选择设备').appendTo($form);
				 var countLineOption = new Array();
				 for(var p=5;p<=20;p+=5){
					 var obj = {};
					 obj.name = p;
					 obj.description = p;
					 countLineOption.push(obj);
				 }
				 //2018-12-14 zq AlarmTable 绑定操作去除行数countLine
				 // countLineForm = renderSelectBox(countLineOption,'countLine','请选择行数').appendTo($form);
				 
				 //确认按钮点击事件
				 $('<div class="modal-footer" style="margin-top:15px;"><button class="btn btn-primary">确定</button></div>')
					.appendTo($form)
					.on('click', function() {
						var className = "table ALARM_SHOW ";
						var scopeAndId = "";
						var line = "";
						var width="100%";
						if((driverForm.find("input[type='hidden']").val() == '')&&(lineForm.find("input[type='hidden']").val() == '')){
							scopeAndId = "company_"+factoryForm.find("input[type='hidden']").val();
						}else if(driverForm.find("input[type='hidden']").val() == ''){
							scopeAndId = "productline_"+lineForm.find("input[type='hidden']").val();
						}else{
							scopeAndId = "driver_"+driverForm.find("input[type='hidden']").val();
						}

						//2018-12-14 zq AlarmTable 监控设计工具栏行数取值 strat
						// countLine = countLineForm.find("input[type='hidden']").val();
                        countLine = document.getElementById(that.$widget[0].id+'_tableLineCount').value;
                        //2018-12-14 zq AlarmTable 监控设计工具栏行数取值 end

						className = className + scopeAndId + " line_" + countLine;
						that.$widget.find("table").attr("class",className);
						//taht.$widget.find("table").attr("style",width);
						
						if (that.$widget.height() == 'auto' || that.$widget.height() == 0){
							that.$widget.height(350);
						}
						var headData = ["行号","工厂","产线","设备","日期","类型","报警ID","信息","状态"];
                        // var headData = ["行号","设备","日期","类型","报警ID","信息","状态"];
                        renderManager.drawAlarmTable($AlarmTable, headData, countLine);
						storageManager.saveAlarmTableData(that.$widget.attr('id'), data);
						localStorage.setItem(that.$widget.attr('id')+"_lineCount", countLine);
						that.$dialog.modal('hide');
					});
			});
			$form.appendTo($modalBody);
			$("div").removeClass("modal-lg");
			
		},

		// 量具绑定zq
        renderAlarmTableMT: function(){

            var that = this;
            var $modalBody = $(".modal-body > .pageContent", this.$dialog);
            this.$dialog.modal().on('hidden.bs.modal', function (e) {
                $(this).remove();
            });

            var $AlarmTableMT = this.$widget.find('.view > .AlarmTableMT-container > table' );
            var columns = $AlarmTableMT.find("thead > tr > th");
            var type = $AlarmTableMT.attr('data-type');

            var $form = $('<form action="" class="form form-horizontal" onsubmit="return false;"></form>');
            var companyOption = new Array();
            ajaxTodo(contextPath+"/procedureMonitor/getFactorynameByCurrentCompany/0",function(data){
                $.each(data,function(idx,item){
                    var obj = {};
                    obj.name = item.id;
                    obj.description = item.companyname;
                    companyOption.push(obj);
                });
                var factoryForm, lineForm,productForm,procedureFrom,propertyForm;
                productForm = renderSelectBox([{name:'',description:''}],'product','请选择产品').appendTo($form).change(function(event){
                    getProcedureByProduct_MT(procedureFrom);
                });
                procedureFrom = renderSelectBox([{name:'',description:''}],'procedure','请选择工序').appendTo($form);

                getProductByCompanyId_MT(productForm);

                $('<div class="modal-footer" style="margin-top:15px;"><button class="btn btn-primary">确定</button></div>')
                    .appendTo($form)
                    .on('click', function() {
                        var className = "table ALARM_MT_SHOW ";
                        var scopeAndId = "";
                        var line = "";
                        var width="100%";
                        scopeAndId = "procedure_"+procedureFrom.find("input[type='hidden']").val();

                        //2018-12-14 zq AlarmTable 监控设计工具栏行数取值 strat
                        // countLine = countLineForm.find("input[type='hidden']").val();
                        countLine = document.getElementById(that.$widget[0].id+'_tableLineCount').value;
                        //2018-12-14 zq AlarmTable 监控设计工具栏行数取值 end

                        className = className + scopeAndId + " line_" + countLine;
                        that.$widget.find("table").attr("class",className);
                        //taht.$widget.find("table").attr("style",width);

                        if (that.$widget.height() == 'auto' || that.$widget.height() == 0){
                            that.$widget.height(350);
                        }
                        var headData = ["序号","量具编号","量具名称","量具类型","量具状态","所属产线","所属站点","预警时间","报警时间","超出报警时长","报警信息","显示"];
                        renderManager.drawAlarmTable($AlarmTableMT, headData, countLine);
                        storageManager.saveAlarmTableMTData(that.$widget.attr('id'), data);
                        localStorage.setItem(that.$widget.attr('id')+"_lineCount", countLine);
                        that.$dialog.modal('hide');
                    });
            })	;
            $form.appendTo($modalBody);
            $("div").removeClass("modal-lg");

        },





			renderMonitorTable: function(){
				
				var that = this;
				var $modalBody = $(".modal-body > .pageContent", this.$dialog);
				this.$dialog.modal().on('hidden.bs.modal', function (e) {
					$(this).remove();
				});
				var $monitorTable = this.$widget.find('.view > .monitorTable-container > table' );
				var columns = $monitorTable.find("thead > tr > th");
				
				var type = $monitorTable.attr('data-type');
				var $form = $('<form action="" class="form form-horizontal" onsubmit="return false;"></form>');
				var companyOption = new Array();
				ajaxTodo(contextPath+"/procedureMonitor/getFactorynameByCurrentCompany/0",function(data){
					$.each(data,function(idx,item){
						var obj = {};
						obj.name = item.id;
						obj.description = item.companyname;
						companyOption.push(obj);
					});
					var factoryForm, lineForm,productForm,procedureFrom,propertyForm;
					/*factoryForm = renderSelectBox(companyOption,'factory','请选择工厂').appendTo($form).change(function(){
						//productForm.empty();
						//getLineByFactoryId(lineForm);
						getProductByCompanyId(productForm);
					});*/
					// lineForm = renderSelectBox([{name:'',description:''}],'line','请选择产线').appendTo($form);
					 productForm = renderSelectBox([{name:'',description:''}],'product','请选择产品').appendTo($form).change(function(event){
						 //$("input[name='product_model'][type='hidden']").val($($(event.target).context.innerHTML).find("li").attr("model-data"));
						// 
						// procedureFrom.empty();
						 getProcedureByProduct(procedureFrom);
					 });
					// $("<input type='hidden' name='product_model'>").appendTo($form);
					 procedureFrom = renderSelectBox([{name:'',description:''}],'procedure','请选择工序').appendTo($form).change(function(){
						// propertyForm.empty();
						 getPropertyByProcedure(propertyForm,columns);
						 propertyForm.find(".check_label").remove();
					 });
					 propertyForm = renderCheckBox([],'property','选择工序属性').appendTo($form);
					// getLineByFactoryId(lineForm);
					 getProductByCompanyId(productForm);
					 //getProcedureByProduct(procedureFrom);
					// getPropertyByProcedure(propertyForm);
					 $('<div class="modal-footer" style="margin-top:15px;"><button class="btn btn-primary">确定</button></div>')
						.appendTo($form)
						.on('click', function() {
							/*if(factoryForm.find("input[type='hidden']").val() == ''){
								swal("请选择工厂");
								return;
							}*/
							if(productForm.find("input[type='hidden']").val() == ''){
								swal("请选择产品");
								return;
							}
							if(procedureFrom.find("input[type='hidden']").val() == ''){
								swal("请选择工序");
								return;
							}
							//$monitorTable.attr('data-url', url);
							if (that.$widget.height() == 'auto' || that.$widget.height() == 0){
								that.$widget.height(350);
							}
							var data = $form.serializeArray();
							$.each(data,function(idx,item){
								item.content = $("input[name='"+ item.name +"'][type='hidden']").parent().find("span").text();
								if(item.name == 'product'){
									item.model = $("input[name='product_model'][type='hidden']").val();
								}
							});
							if(JSON.stringify(data).indexOf("mesProcedureProperties") == -1){
								var flag = false;
								$.each($("input[type='checkbox'][name='mesProcedureProperties']"),function(idx,item){
									
									if($(item).attr("checked") != undefined){
										flag = true;
										data.push({name:"mesProcedureProperties",value:$(item).val()+"-"+$(item).next().text()});
									}
								});
								if(!flag){
									swal("您未选择工序参数");
									return;
								}
							}
							
							renderManager.drawMonitorTable($monitorTable, data);
							//storageManager.saveChartsData(that.$widget.attr('id'), data);
							data.push({name:"lineCount",value:that.$widget.find("tbody > tr").length});
							storageManager.saveMonitorTableData(that.$widget.attr('id'), data);
							//that.$widget.trigger('change');
							that.$dialog.modal('hide');
						});
				})	;	
				//$modalBody.append($form);
			
		
			//$modalBody.append($form);
			$form.appendTo($modalBody);
			$("div").removeClass("modal-lg");
			
			}
	};
	
	function getLineByFactoryId($container){
		
		var lineOption = new Array();
		ajaxTodo(contextPath+"/procedureMonitor/getProductionLineByCompanyId?companyId="+$("input[name='factory']").val(),function(_data){
			lineOption.push({name:"",description:"全部"});
			$.each(_data,function(idx,_item){
				var _obj = {};
				_obj.name = _item.id;
				_obj.description = _item.linename;
				lineOption.push(_obj);
			});
			$container.find('span').empty();
			$container.find('ul').empty();
			if(lineOption.length != 0){
				$container.find('span').html(lineOption[0].description);
				$container.find('input[type="hidden"]').val(lineOption[0].name);
			}
			$.each(lineOption, function(i, v){
				$container.find('ul').append($('<li></li>')
		          .attr('data-value', v.name)
		          .html(v.description));
			});
		});
		
	};
	
	//updatedBy:xsq 7.14 增加对应子工厂
	
	
function getLineByChildFactoryId($container){
	
		var lineOption = new Array();
		ajaxTodo(contextPath+"/procedureMonitor/getProductionLineByCompanyId?companyId="+$("input[name='childFactory']").val(),function(_data){
			$.each(_data,function(idx,_item){
				var _obj = {};
				_obj.name = _item.id;
				_obj.description = _item.linename;
				lineOption.push(_obj);
			});
			$container.find('span').empty();
			$container.find('ul').empty();
			if(lineOption.length != 0){
				$container.find('span').html(lineOption[0].description);
				$container.find('input[type="hidden"]').val(lineOption[0].name);
			}
			$.each(lineOption, function(i, v){
				$container.find('ul').append($('<li></li>')
		          .attr('data-value', v.name)
		          .html(v.description));
			});
		});
		
	};
function getDriverByLineId($container){
	
		var driverOption = new Array();
		ajaxTodo(contextPath+"/procedureMonitor/getDrivernameByCurrentLine/"+$("input[name='line']").val(),function(_data){
			driverOption.push({name:"",description:"全部"});
			$.each(_data,function(idx,_item){
				var _obj = {};
				_obj.name = _item.id;
				_obj.description = _item.name;
				driverOption.push(_obj);
			});
			$container.find('span').empty();
			$container.find('ul').empty();
			if(driverOption.length != 0){
				$container.find('span').html(driverOption[0].description);
				$container.find('input[type="hidden"]').val(driverOption[0].name);
			}
			$.each(driverOption, function(i, v){
				$container.find('ul').append($('<li></li>')
		          .attr('data-value', v.name)
		          .html(v.description));
			});
		});
		
	};

	//量具绑定 产品项 zq
    function getProductByCompanyId_MT($container,callback){

        var productOption = new Array();
        ajaxTodo(contextPath+"/procedureMonitor/getProductionByCompanyId/0",function(_data){
            $.each(_data,function(idx,_item){
                var _obj = {};
                _obj.name = _item.id;
                _obj.description = _item.name;
                _obj.modelnum = _item.modelnum;
                productOption.push(_obj);
                //	select.find("select").append("<option value='"+ item.id +"'>"+ item.companyname +"</option>");
            });
            $container.find('span').empty();
            $container.find('ul').empty();
            $container.find('input[type="hidden"]').val("");

			$container.find('span').html('全部');
			$container.find('input[type="hidden"]').val(0);
			$container.trigger('change');

            $container.find('ul').append($('<li model-data='+ 0 +'></li>')
                .attr('data-value', 0)
                .html('全部'));
            // $.each(productOption, function(i, v){
            //     $container.find('ul').append($('<li model-data='+ v.modelnum +'></li>')
            //         .attr('data-value', v.name)
            //         .html(v.description));
            // });
            if(callback)
                callback();
        },false);

    };

    //量具绑定 工序项 zq
    function getProcedureByProduct_MT($container,callback){

        var procedureOption = new Array();
        if($("input[name='product']").val() != ''){
            ajaxTodo(contextPath+"/procedureMonitor/getProcedureByProduct/"+$("input[name='product']").val(),function(data){
                //	var options = new Array();
                $.each(data,function(idx,item){
                    var obj = {};
                    obj.name = item.id;
                    obj.description = item.procedurename;
                    procedureOption.push(obj);
                });
                $container.find('span').empty();
                $container.find('ul').empty();
                $container.find('input[type="hidden"]').val("");

                $container.find('span').html('全部');
                $container.find('input[type="hidden"]').val(0);
                $container.trigger('change');

                $container.find('ul').append($('<li model-data='+ 0 +'></li>')
                    .attr('data-value', 0)
                    .html('全部'));

                // $.each(procedureOption, function(i, v){
                //     $container.find('ul').append($('<li></li>')
                //         .attr('data-value', v.name)
                //         .html(v.description));
                // });

                if(callback && $container.find('input[type="hidden"]').val() != '')
                    callback();
            },false)
        }

    };


	function getProductByCompanyId($container,callback){
		
		var productOption = new Array();
		ajaxTodo(contextPath+"/procedureMonitor/getProductionByCompanyId/0",function(_data){
			$.each(_data,function(idx,_item){
				var _obj = {};
				_obj.name = _item.id;
				_obj.description = _item.name;
				_obj.modelnum = _item.modelnum;
				productOption.push(_obj);
			//	select.find("select").append("<option value='"+ item.id +"'>"+ item.companyname +"</option>");
			});
			$container.find('span').empty();
			$container.find('ul').empty();
			$container.find('input[type="hidden"]').val("");
			if(productOption.length != 0){
				$container.find('span').html(productOption[0].description);
				$container.find('input[type="hidden"]').val(productOption[0].name);
				//$('input[name="product_model"][type="hidden"]').val(productOption[0].modelnum);
				$container.trigger('change');
			}
			$.each(productOption, function(i, v){
				$container.find('ul').append($('<li model-data='+ v.modelnum +'></li>')
		          .attr('data-value', v.name)
		          .html(v.description));
			});
			if(callback)
				callback();
			//getProcedureByProduct(procedureFrom);
		},false);
		
	};
	function getProcedureByProduct($container,callback){
		
		var procedureOption = new Array();
		if($("input[name='product']").val() != ''){
			ajaxTodo(contextPath+"/procedureMonitor/getProcedureByProduct/"+$("input[name='product']").val(),function(data){
				//	var options = new Array();
					$.each(data,function(idx,item){
						var obj = {};
						obj.name = item.id;
						obj.description = item.procedurenum;
						procedureOption.push(obj);
					});
					$container.find('span').empty();
					$container.find('ul').empty();
					$container.find('input[type="hidden"]').val("");
					if(procedureOption.length != 0){
						$container.find('span').html(procedureOption[0].description);
						$container.find('input[type="hidden"]').val(procedureOption[0].name);
						$container.trigger('change');
					}
					$.each(procedureOption, function(i, v){
						$container.find('ul').append($('<li></li>')
				          .attr('data-value', v.name)
				          .html(v.description));
					});
					/*if($container.find('input[type="hidden"]').val() != ''){
						getPropertyByProcedure($form);
					}*/
					if(callback && $container.find('input[type="hidden"]').val() != '')
						callback();
				},false)
		}
		
	};
	function getPropertyByProcedure(propertyForm,columns){
		
		var procedurePropOption = new Array();
		var array = [];
		if(columns.length > 4){
			$.each(columns,function(idx,item){
				var $this = $(item);
				if(idx < 4){
					return true;
				}
				
				array.push($this.attr("data-value").split("-")[1]);
			});
		}
		if($("input[name='procedure']").val() != ''){
			ajaxTodo(contextPath+"/procedureMonitor/getProcedurePropByProce/"+$("input[name='procedure']").val(),function(data){
				//	var options = new Array();
					$.each(data,function(idx,item){
						if(!item.mesPoints){
							return false;
						}
						var obj = {};
						obj.name = item.mesPoints.codekey;
						obj.description = item.propertyname;
						procedurePropOption.push(obj);
					});
					$.each(procedurePropOption, function(i, v){
						var val = v.name+"-"+v.description;
					
						var options = $(CHECKBOX_HTML.replace('#NAME#', 'mesProcedureProperties').replace('#VALUE#',v.name+"-"+v.description).replace('#TEXT#',v.description)).appendTo(propertyForm);
						if($.inArray(v.name,array) != -1){
							options.find("input[type='checkbox']").attr("checked","checked");
						}
					});
					//renderCheckBox(options,'procedureProperty','产品工序属性').appendTo($form);
				})
		}
		
	}
	var ChartsInfo = function() {
		
	};
	function renderSelectBox(options, name, legend) {
		
		if(options && options.length > 0){
			var $container = $(SELECT_HTML
					.replace('#DESCRIPTION#', options[0].description)
					.replace('#VALUE#', options[0].name)
					.replace('#ATTR#',options[0].attr || "")
					.replace('#NAME#', name));
				if (legend) {
					$container.prepend('<label class="control-label col-sm-4">' + legend + '</label>');
				}
				$.each(options, function(i, v){
					$container.find('ul').append($('<li></li>')
			          .attr('data-value', v.name).attr('data-attr',v.attr || "")
			          .html(v.description));
				});
				
				return $container;
		}
		
	}
	function renderCheckBox(options, name ,legend) {
		
		var $container = $('<div class="form-group"></div>');
		if (legend) {
			$container.prepend('<label class="control-label col-sm-4">' + legend + '</label>');
		}
		$.each(options, function(i, v){
			$(CHECKBOX_HTML.replace('#NAME#', name).replace('#VALUE#',v.name).replace('#TEXT#',v.description)).appendTo($container);
		});
		
		return $container;
	}
	function renderLabelBox(options,legend){
		
		var $container = $(Label_CHK_HTML.replaceAll('#DESCRIPTION#', options[0].description)
				.replaceAll('#NAME#', options[0].name));
			if (legend) {
				$container.prepend('<label class="control-label col-sm-4">' + legend + '</label>');
			}
			
			var select = $container.find(".select").append("<label class='control-label col-sm-4' style='font-weight: normal;'>公司</label><div class='col-sm-6'><select class='form-control' disabled='disabled'></select></div>");
			ajaxTodo(contextPath+"/procedureMonitor/getCompanynameByCurrentChose/",function(data){
				$.each(data,function(idx,item){
					select.find("select").append("<option value='"+ item.id +"'>"+ item.companyname +"</option>");
				});
				//updatedBy:xsq 
				$(".company").find("select").change(function(){
					if($(".factory")){
						$(".factory").find("select").empty();
						generateFactory($(this));
					}
				});
				generateFactory($container);
				//select.find("select").append("<option value='aa'>fff</option>");
			});
			
			return $container;
	}
	function renderDate(id, legend) {
		
		var $date = $(SEARCH_DATE_HTML
			.replace('#ID#', id)
			.replace('#SEARCH_START_DATE#', 'begin'));
		if (legend) {
			$date.prepend('<label class="control-label col-sm-4">' + legend + '</label>');
		}
		$date.find('input').datetimepicker({
			 language : 'zh-CN',
		        format : 'yyyy-mm-dd hh:ii',
		        weekStart : 1,
		        todayBtn : 'linked',
		        autoclose : 1,
		        todayHighlight : 1,
		        startView : 2,
		        forceParse : 0,
		        showMeridian : 1,
		});
		
		return $date;
	}
	function renderTable($table, options, callback) {
		
		$table.bootstrapTable($.extend({
			uniqueId: 'id',
			locale: 'zh-CN',
			sidePagination: 'server',
			clickToSelect: true,
			responseHandler: function(res) {
				
				res.total = res.page.totalCount;
				// this: options of current bootstrap table 
				this.oriData = res;
				return res;
			},
			queryParams: function (params) {
				
				return {
				  numPerPage: params.limit,   //(this.options.pageSize)
				  pageNum: this.pageNumber,  // current page
				  sort: params.sort,
				  sortOrder: params.order
				}
			}
		}, options));
		callback && $table.off('post-body.bs.table').on('post-body.bs.table', function () {
			
	      callback($(this).bootstrapTable('getOptions').oriData);
	    });
	}
	/*function generateMonitorTable($table, options, lineCount) {
		//$table.empty();
		var h_tr = $("<tr></tr>");
		var b_td;
		var b_tr = "";
		$.each(options,function(idx,item){
			$("<th data-value='"+ item.name +"-"+ item.value +"'>"+ item.title +"</th>").appendTo(h_tr);
			if(item.name == 'mesProcedureProperties' || item.content == undefined){
				b_td += "<td>--</td>";
			}else
				b_td += "<td>" + item.content + "</td>";
		});
		$table.find("thead").empty();
		h_tr.appendTo($table.find("thead"));
		
		for(var i=0;i<7;i++){
			b_tr += "<tr>"+ b_td +"</tr>";
		}
		$table.find("tbody").empty();
		$(b_tr).appendTo($table.find("tbody"));
		//$("<tr>"+ b_td +"</tr>").appendTo($table.find("tbody"));
		callback && $table.off('post-body.bs.table').on('post-body.bs.table', function () {
			callback($(this).bootstrapTable('getOptions').oriData);
		});
	}*/

	function getChartsData(type, url, data, callback) {
		
		$.ajax({
			type: 'POST',
			url: url,
			data: data,
			dataType:"json",
			success: function(res){
				
				callback(res);
			},
			complete: function(res){
				
			}
		});
		
	}
	
	function getChartsDataForDriver(type, url, data, callback) {
		
		$.ajax({
			type: 'POST',
			url: url,
			data: data,
			dataType:"json",
			success: function(res){
				
				callback(res);
			},
			complete: function(res){
				
			}
		});
		
	}

	

	return {
		generateMonitorTable : function($table, options, lineCount) {
			
			//$table.empty();
			var h_tr = $("<tr></tr>");
			var b_td;
			var b_tr = "";
			$.each(options,function(idx,item){
				$("<th data-value='"+ item.name +"-"+ item.value +"'>"+ item.title +"</th>").appendTo(h_tr);
				if(item.name == 'mesProcedureProperties' || item.content == undefined){
					b_td += "<td>--</td>";
				}else
					//b_td += "<td>" + item.content + "</td>";
				b_td += "<td>--</td>";
			});
			$table.find("thead").empty();
			h_tr.appendTo($table.find("thead"));
			if(!lineCount){
				lineCount = 7;
			}
			for(var i=0;i<lineCount;i++){
				b_tr += "<tr>"+ b_td +"</tr>";
			}
			$table.find("tbody").empty();
			$(b_tr).appendTo($table.find("tbody"));
			//$("<tr>"+ b_td +"</tr>").appendTo($table.find("tbody"));
			/*callback && $table.off('post-body.bs.table').on('post-body.bs.table', function () {
				callback($(this).bootstrapTable('getOptions').oriData);
			});*/
			
		},
		generateAlarmTable : function($table, options, lineCount) {
			
			var h_tr = $("<tr></tr>");
			var b_td;
			var b_tr = "";
			$.each(options,function(idx,item){
				$("<th>"+ item.title +"</th>").appendTo(h_tr);
				b_td += "<td>--</td>";
			});
			$table.find("thead").empty();
			h_tr.appendTo($table.find("thead"));
			for(var i=0;i<lineCount;i++){
				b_tr += "<tr>"+ b_td +"</tr>";
			}
			$table.find("tbody").empty();
			$(b_tr).appendTo($table.find("tbody"));
			
		},
		
		generateChartsData : function(res) {
			
			if (!res) return false;
			var option = $.extend({}, CHART_DEFAULT_OPTIONS[type]);

			switch (type) {
				case 'bar':
					option.xAxis[0].data = res.xAxis;
					var series = $.extend({},option.series[0]);
					option.title.text = '柱状图';
					option.series = [];

					$.each(res.data, function(k, v){
						var s = $.extend(true, {}, series);
						s.name = k;
						s.data = v;
						option.legend.data.push(k);
						option.series.push(s);
					});
				break;
				case 'line':
				break;
				case 'pie':
					var series = $.extend({},option.series[0]);
					option.title.text = '饼图';
					option.series = [];
					option.legend.data = res.xAxis;
					var s = $.extend(true, {}, series);
					//{"data":{"NTTDATA":0,"文思海辉":2},"chartsType":"pie","chartsName":"charts7",dataType:"Capacity"}
					$.each(res.data, function(k, v){
						s.data.push({value:v,name:k});
					});
					option.series.push(s);
				break;
				case 'gauge':
					var series = $.extend({},option.series[0]);
					option.title.text = '仪表盘';
					option.series = [];
					option.legend.data = res.xAxis;
					var s = $.extend(true, {}, series);
					$.each(res.data, function(k, v){
						s.data.push({value:v,name:k});
					});
					option.series.push(s);
				break;
				case 'scatter':
				break;
			}
			
			return option;
		}
		,
	
		renderDialog: function($widget, options){
			
			var $body = $('body');
			$body.append($('#dialogTemp').html());
			var $dialog = $body.children('.modal:last-child');
			$dialog.find(".modal-title").html(options.title);

			var dialog = new BindDataDialog($widget, $dialog);
			dialog['render' + options.type]();
			
		},
		/*start slq 2019-05-09*/
		drawSpcCharts: function($ele, data){
			var url = $ele.attr('data-url');
			if (!url) return;
			var chartObj = echarts.getInstanceByDom($ele[0]);
			if(!chartObj){
				//chartObj = echarts.init($ele[0]);
				// echarts.registerTheme('dark','dark');
				chartObj = echarts.init($ele[0], 'chalk');
			}
			var type = $ele.attr('data-type') || 'bar';
			if("gauge" != type &&( "undefined" == typeof(driverRuntimeType)||null == driverRuntimeType || "" == driverRuntimeType )) {
				getChartsData(type, url, data, function(option){
					option.title = undefined;
					option && chartObj.setOption(option);
				});
			} else {

			}

			return chartObj;
		},
		/* end */
		drawCharts: function($ele, data){
			
			var url = $ele.attr('data-url');
			if (!url) return;
			var chartObj = echarts.getInstanceByDom($ele[0]);
			if(!chartObj){
				 //chartObj = echarts.init($ele[0]);
				// echarts.registerTheme('dark','dark');
				 chartObj = echarts.init($ele[0], 'chalk');
			}
			var type = $ele.attr('data-type') || 'bar';

			var driverRuntimeType = $ele.attr('driver-runtime');
			if("gauge" != type &&( "undefined" == typeof(driverRuntimeType)||null == driverRuntimeType || "" == driverRuntimeType )) {
				getChartsData(type, url, data, function(option){
					option.title = undefined;
					option && chartObj.setOption(option);
				});
			} else {
				// delete option8['dataZoom'];
				var getDataUrl = "getJson_ajaxFroDriver";
				if("gauge" == type) {
					getDataUrl = "getJson_ajaxFroDriverProperty";
				}
				getChartsDataForDriver(type, getDataUrl, data, function(option){
					option.title = undefined;
					if("gauge" == type) {
						optionDriverGauge.series[0].data[0].value = option.propertyData;
						chartObj.setOption(optionDriverGauge);
					} else {
				        option8.series[3].data = option.driverRuntimeList;
				        option8.xAxis.min = option.driverRuntimeList[0].value[1];
						chartObj.setOption(option8);
					}

				});


			}

			return chartObj;
		},
		drawAlarmTable: function($ele, headData, countLine){
			
			$ele.parent().siblings('.image').hide();
			$ele.data("data",JSON.stringify(data));
			var propertyColumns = [];
			$.each(headData,function(idx,item){
			    	var column = {};
			    	column.title = item;
			    	propertyColumns.push(column);
				});
		    renderManager.generateAlarmTable($ele,propertyColumns,countLine);
		    
	},
		drawMonitorTable: function($ele, data){
			
			var lineCount;
			
			$ele.parent().siblings('.image').hide();
			$ele.data("data",JSON.stringify(data));
			var propertyColumns = [
				                    /*{
						         		"title": "工厂"
						         	},{
				                 		"title": "产品名称"
				                 	},{
				                 		"title": "产品型号"
				                 	},{
				                 		"title": "工序名称"
				                 	}*/
				                 ];
		//	$.each($("input[type='checkbox'][name='mesProcedureProperties']:checked"),function(idx,item){
			$.each(data,function(idx,item){
				//	
			    	var column = {};
			    	if(item.name == 'factory'){
			    		column.title = '工厂';
			    		column.name = 'factory';
			    		column.content = item.content;
			    		column.value = item.value;
			    		//column.name=item.value;
			    	}else if(item.name == 'product'){
			    		column.title = '产品名称';
			    		column.name = 'product';
			    		column.content = item.content;
			    		column.value = item.value;
			    		$ele.attr("id",item.value+"_"+idx) ;
			    		$ele.addClass(item.value);
			    	}else if(item.name == 'procedure'){
			    		column.title = '工序名称';
			    		column.name = 'procedure';
			    		column.content = item.content;
			    		column.value = item.value;
			    	}
			    	else if(item.name == 'mesProcedureProperties'){
						column.title = item.value.split("-")[1];
						column.name = 'mesProcedureProperties';
						column.value = item.value.split("-")[0];
					}else if(item.name == 'lineCount'){
						lineCount = item.value;
						
						return true;
					}
			    	propertyColumns.push(column);
				});
		    propertyColumns.splice(2, 0, {"title":"产品型号","name":"","value":""});
		    propertyColumns.splice(3, 0, {"title":"序列号","name":"","value":""});
		    renderManager.generateMonitorTable($ele,propertyColumns,lineCount);
		    
		}
	};
})();

// Selectbox event listener
$(document).on('click', '.select>.placeholder', function(){
	
	var $p = $(this).closest('.select');
	if (!$p.hasClass('is-open')){
		$p.addClass('is-open');
		$('.select.is-open').not($p).removeClass('is-open');
	}else{
		$p.removeClass('is-open');
	}
	
}).on('click','.select>ul>li',function(){
	
	var $p = $(this).closest('.select');
	$p.removeClass('is-open').find('.placeholder').text( $(this).text() );
	$p.find('input[type=hidden]').attr('value', $(this).attr('data-value') );
	$p.trigger('change');
	
}).on('click.nice_select', function(event) { // Close when clicking outside
	
	if ($(event.target).closest('.select').length === 0) {
		$('.select').removeClass('is-open')
	}
	
});