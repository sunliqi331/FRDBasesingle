  var startDate;
  var endDate;
  var id;
  var dateType;
  var typeScope;
  var EnergyType;
  var modelnum;

$(document).ready(function(){
   $('.form_datetime3').datetimepicker({
        minView: "month", //选择日期后，不会再跳转去选择时分秒 
        language:  'zh-CN',
        format: 'yyyy-mm-dd',
        todayBtn:  1,
        autoclose: 1,
     });
    // --------------------图表数据----------------------------
    /*
    var maindata1 = echarts.init(document.getElementById('mainData1'));
    maindata1.setOption(option);

    var maindata2 = echarts.init(document.getElementById('mainData2'));
    maindata2.setOption(option2);

    var maindata3 = echarts.init(document.getElementById('mainData3'));
    //option2.series[0].type = "line";
    maindata3.setOption(option21);

    var maindata4 = echarts.init(document.getElementById('mainData4'));
    maindata4.setOption(option3);

    var maindata5 = echarts.init(document.getElementById('mainData5'));
    maindata5.setOption(option4);

    $("select").chosen({search_contains:true});
    $("#tab2 .chosen-container-single").css("width","135px");
    $("#tab3 .chosen-container-single").css("width","155px");

    $("#modelnumSelected").text($(".chosen-single").children().html());
    $("#productNo").text($("#modelnum").val());

    // 产品选择时间范围
    $("#startTimeForS").text(getNowFormatDate("2") + " - " + getNowFormatDate("1"));
    console.log(minusTime(getNowFormatDate("2"), getNowFormatDate("1")));
    */

    $("#tab2").css("min-height", "100px");
    $("#tab2Ddiv").hide();

    $("#productCountAreaUp").hide();
    $("#productCountArea1").hide();
    $("#productCountArea2").hide();
    $("#productCountArea3").hide();

    // TODO:设备UI初始测试用
    // searchForDriver();
    
    // TODO:调试用初始话
    $("#driverEnergyTable").bootstrapTable('destroy');
//    $("#driverEnergyTable").attr("data-url","${contextPath}/stats/listAlldriverenergy?energyType="+$("#EnergyType").val()
//            +"&typeScope="+typeScope+"&id="+id+"&startDate="+startDate+"&endDate="+endDate);
    $.table.init("driverEnergyTable",{toolbar : '#toolBar1'});

    /*
    $("#modelnum").change(function() {
        // alert($(this).children('option:selected').text());
        $("#modelnumSelected").text($(this).children('option:selected').text())
        $("#productNo").text($(this).children('option:selected').val());
    });
    */

    // 默认产品的批次，在首页加载时，进行加载
    generateProcedureSelect($("#modelnum").val());
    // 根据所选产品，获取对应的批次
    $("#modelnum").change(function(){
        generateProcedureSelect($(this).val());
      });

});
$("#chooseFactory").change(function(eventt){
    $("#chooseProductLine").empty();
    $("#chooseProductLine").append("<option value=''>全部</option>");
    $("#chooseProductLine").trigger("chosen:updated");
    if($("#chooseFactory").val()!=""){
        ajaxTodo(contextPathGet + "/productline/getProductlineByCompanyid/"+$("#chooseFactory").val(),paintProductLine);
    }
});

function paintProductLine(data){
   $.each(data,function(idx,item){
     $("#chooseProductLine").append("<option value='"+ item.id +"'>"+ item.linename +"</option>");
   });
   $("#chooseProductLine").trigger("chosen:updated");
};

//工厂和设备选择框的级联
$("#chooseProductLine").change(function(event){
      $("#ChooseDriver").empty();
      $("#ChooseDriver").append("<option value=''>全部</option>");
      $("#ChooseDriver").trigger("chosen:updated");
    if($("#chooseProductLine").val()!=""){
        ajaxTodo(contextPathGet + "/productline/DriverData2/"+$("#chooseProductLine").val(),paintDrivers);
    }
});
function paintDrivers(data){
  $.each(data,function(idx,item){
    $("#ChooseDriver").append("<option value='"+ item.id +"'>"+ item.name +"</option>");
  });
    $("#ChooseDriver").trigger("chosen:updated");
};



function chooseCompanyRange(){
    if($("#chooseFactory").val()==""){
        id=$("#chooseCompany").val();
        typeScope="company";
    }else if($("#chooseProductLine").val()==""){
        id=$("#chooseFactory").val();
        typeScope="factory";
    }else{
        id=$("#chooseProductLine").val();
        typeScope="productline";
    }
}
  
  $(document).ready(function($) {
     $(".rangetime").hide();  
     $("#search_time").change(function(e){   
     if($("#search_time").val()=="define_time"){
        $(".rangetime").show();   
         }else{
            $(".rangetime").hide();
            judgeDate();
            /*console.log("startDate:" + startDate);
            console.log("endDate:" + endDate);

            $("#startTimeForS").text(startDate + " - " + getNowFormatDate("1"));
            console.log(minusTime(startDate, getNowFormatDate("1")));
            */
         }
    
      });

     // 统计类型选择内容动态绑定
     $("#EnergyType").change(function(e){   
         if("runtime" == $("#EnergyType").val()){
              $("#timeSelectTab3").show();
              $("#searchTimeTabArea3").hide();
              //$(".form_datetime3").datepicker('clearDates');
              $("#driverAnalysisRuntime").css("min-height", "823px");
         } else {
             $("#timeSelectTab3").hide();
             $("#searchTimeTabArea3").show();
             $("#driverAnalysisRuntime").css("min-height", "568px");
         }
     });

    $('.form_datetime1').datetimepicker({
    language:  'zh-CN',
       format: 'yyyy-mm-dd hh:ii:ss',
     weekStart: 1,
     todayBtn:'linked',
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0,
     showMeridian: 1,
        
 });
 
   $('.form_datetime2').datetimepicker({
    language:  'zh-CN',
       format: 'yyyy-mm-dd hh:ii:ss',
     weekStart: 1,
     todayBtn:'linked',
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        forceParse: 0,
     showMeridian: 1,
        
 });
   
    
    $(".tab_content").hide();
    $("ul.tabs li:first").addClass("active").show();
    $(".tab_content:first").show();
    $("ul.tabs li").click(function() {  
        $("ul.tabs li").removeClass("active");
        $(this).addClass("active");
        $(".tab_content").hide();
        var activeTab = $(this).find("a").attr("href");
        $(activeTab).show();
        return false;  
    });  
    // 产品图标展示
    $("#searchForEcharts").click(function(){
        var blockText = "数据正在分析，请稍等......";
        $.blockUI({
        baseZ:99999,
        message:"<h1>"+blockText+"</h1>",
            onBlock:function(){
                judgeDate();
                $.ajax({
                    type : "GET",
                    url : contextPathGet + "/stats/productAnalyseNew",
                    data : {
                        "startDate" :startDate,
                        "endDate" :endDate,
                        "energyType":$("#EnergyType").val(),
                        "typeScope":typeScope,
                        "dateType" :dateType,
                        "driverid":$("#ChooseDriver").val(),
                        "id" :$("#modelnum").val(),
                        "modelnum" :$("#modelnum").val(),
                        "productBatchid" :$("#productBatchid").val(),
                        "productNm" : $(".chosen-single").children().html()
                    },
                    dataType : "json",
                    success : function(outdata) {
                        console.info(outdata.dataflg);
                        var rs = searchForProduct(outdata);
                        if(rs){
                            $("#modelnumSelected").text(outdata.productNm);
                            $("#productNo").text($("#modelnum").val());

                            $("#tab2Ddiv").show();
                            $("#productCountAreaUp").show();
                            $("#productCountArea1").show();
                            $("#productCountArea2").show();
                            // $("#productCountArea3").show();
                            if("0" != $("#productBatchid").val()){
                                $("#tab2").css("min-height", "1800px");
                            } else {
                                $("#tab2").css("min-height", "2100px");
                            }

                        } else {
                            $("#tab2Ddiv").hide();
                            $("#productCountAreaUp").hide();
                            $("#productCountArea1").hide();
                            $("#productCountArea2").hide();
                            // $("#productCountArea3").hide();
                            $("#tab2").css("min-height", "100px");
                            var msg = getTimeNameByCode($("#search_time").val());
                            swal("该产品在" + msg + "没有进行生产，请选择其他产品。");
                        }
                        $.unblockUI();
                    }
                });
            }
        })


        /*
        judgeDate();
        var op = {};
        op.title = "统计分析";
        op.url = "${contextPath}/stats/productAnalyse";
        var dataType = 
        op.data = {
            "startDate" :startDate,
            "endDate" :endDate,
            "dateType" :dateType,
            "modelnum" :$("#modelnum").val(),
            "passType" :"COUNT",
            "id" :$("#chooseCompany").val(),
            "searchKind" : "1"
        };
        op.resizable = true;
        op.destroyOnClose=false;
        $.pdialog.open("productAnalyseData",op);
        */
    });
    // 合格图标展示
    $("#searchBarForEcharts").click(function(){
        judgeDate();
        var op = {};
        op.title = "统计分析";
        op.url = "${contextPath}/stats/productAnalyse";
        var dataType = 
        op.data = {
            "startDate" :startDate,
            "endDate" :endDate,
            "dateType" :dateType,
            "modelnum" :$("#modelnum").val(),
            "passType" :"COUNT",
            "id" :$("#chooseCompany").val(),
            "searchKind" :"2"
        };
        op.destroyOnClose=true;
        $.pdialog.open("productAnalyseData",op);
    });

    // 设备图标展示
    $("#searchFosearchForEchartsForDriverrEcharts").click(function(){
        var factoryId = $("#chooseFactory").val();
        var lineId = $("#chooseProductLine").val();
        var driverId = $("#ChooseDriver").val();
        if(!nullCheck(factoryId)){
           layer.msg('请选择工厂');
           return false;
        } else if(!nullCheck(lineId)){
            layer.msg('请选择产线');
            return false;
        } else if(!nullCheck(driverId)){
           layer.msg('请选择设备');
           return false;
        }

        var blockText = "数据正在分析，请稍等......";
        $.blockUI({
        baseZ:99999,
        message:"<h1>"+blockText+"</h1>",
            onBlock:function(){
                chooseCompanyRange();
                judgeDate("1");
                var tempType = $("#EnergyType").val();
                var tempDriver = $("#ChooseDriver").val();
                var searchKind = "0";
                //判断能耗类型为耗电，耗水，或者耗气
                if(tempType!="runtime"){ 
                    if(tempDriver!=""){
                      //如果选择的是单个设备
                      searchKind = "0";
                    }else{
                      //所有设备
                      searchKind = "1"; 
                    }
                }

                var anlysisDateVal = "";
                if("" != $("#beginTab3").val()) {
                    anlysisDateVal = $("#beginTab3").val();
                } else {
                    anlysisDateVal = getNowFormatDate("1");
                }

                $.ajax({
                    type : "GET",
                    url : contextPathGet + "/stats/productAnalyseForDriver",
                    data : {
                        "startDate" :startDate,
                        "endDate" :endDate,
                        "energyType":$("#EnergyType").val(),
                        "typeScope":typeScope,
                        "searchKind":searchKind,
                        "dateType" :dateType,
                        "driverid":$("#ChooseDriver").val(),
                        "id" :id,
                        "chooseFactoryId":$("#chooseFactory").val(),
                        "chooseProductLineId":$("#chooseProductLine").val(),
                        "anlysisDate":anlysisDateVal
                    },
                    dataType : "json",
                    success : function(outdata) {
                        console.info(outdata.dataflg);
                        if("runtime" == $("#EnergyType").val()){
                            $("#driverAnalysis").hide()
                            var rs = searchForDriverRuntime(outdata);
                            if(rs){
                            	$("#driverAnalysisRuntime").show();
                                $("#modelnumSelectedTab4").text(outdata.driverNm);
                                $("#productNoTab4").text(outdata.driverSn);
//                                $("#startTimeForSTab4").text(startDate + " - " + getNowFormatDate("1"));
//                                console.log(minusTime(startDate, getNowFormatDate("1"), "2"));
                            } else {
                                // var msg = getTimeNameByCode($("#search_timeTab3").val());
                                $("#driverAnalysisRuntime").hide();
                                swal("该设备在选择的日期，没有进行生产，请选择其他设备。或者运行日期");
                            }
                        } else {
                            $("#driverAnalysisRuntime").hide();
                            var rs = searchForDriver(outdata);
                            if(rs){

                            } else {
                                var msg = getTimeNameByCode($("#search_timeTab3").val());
                                swal("该设备在"+msg+"没有进行生产，请选择其他设备。");
                            }
                        }

                        $.unblockUI();
                    }
                });
            }
        })
    });

  });
 
  function getDate(){
//今天时间：
  var year=new Date().getFullYear();
  var month=new Date().getMonth()+1;
  var day=new Date().getDate();
  }
//判断开始时间和结束时间
function judgeDate(kind){
  var myDate = new Date();
  //获取当前年
  var year=myDate.getFullYear();
  //获取当前月
  var month=myDate.getMonth()+1;
  //获取当前日
  var date=myDate.getDate(); 
  var h=myDate.getHours();       //获取当前小时数(0-23)
  var m=myDate.getMinutes();     //获取当前分钟数(0-59)
  var s=myDate.getSeconds();  
  var now=year+'-'+p(month)+"-"+p(date)+" "+p(h)+':'+p(m)+":"+p(s);

  var searchTime;
  // 设备分析专用
  if("1" == kind){
      searchTime = $("#search_timeTab3").val();
  } else {
      searchTime = $("#search_time").val();
  }
  switch (searchTime){
  case "1":
    startDate = year+'-'+p(month)+"-"+p(date)+" "+"00:00:00";
    endDate = year+'-'+p(month)+"-"+p(date)+" "+"23:59:59";
    dateType="day";
    break;
  case "2":
    var M = new Date(dateRangeUtil.getCurrentWeek()[0]);
    var S = new Date(dateRangeUtil.getCurrentWeek()[1]);
    startDate = M.getFullYear() + '-' + (M.getMonth() + 1) + '-' + M.getDate() + ' 00:00:00';
    endDate = S.getFullYear() + '-' + (S.getMonth() + 1) + '-' + S.getDate() + ' 23:59:59';
    dateType="week";
    break;
  case "3":
    startDate = year+'-'+p(month)+"-"+01+" "+"00:00:00";
    if(p(month)=="1"||p(month)=="3"||p(month)=="5"||p(month)=="7"||p(month)=="8"||p(month)=="10"||p(month)=="12"){
        endDate = year+'-'+p(month)+"-"+"31"+" "+"23:59:59";
    }else if(p(month)=="4"||p(month)=="6"||p(month)=="9"||p(month)=="11"){
        endDate = year+'-'+p(month)+"-"+"30"+" "+"23:59:59";
    }else{
        if((year%4==0 && year%100!=0)||(year%100==0 && year%400==0)){
            endDate = year+'-'+p(month)+"-"+"28"+" "+"23:59:59";
        }else{
            endDate = year+'-'+p(month)+"-"+"29"+" "+"23:59:59";
            }
    }
    dateType="month";
    break;
  case "4":
        // startDate = year+'-'+"01"+"-"+p(date)+" "+"00:00:00";
        startDate = year+'-'+"01"+"-"+"01"+" "+"00:00:00";
        endDate = year+'-'+"12"+"-"+p(date)+" "+"23:59:59";
        //当选择时间范围为本年时，设置时间范围为自定义(后台没有关于年的封装，所以归类到自定义时间)
        dateType="defineDate";
        break;
  case "define_time":
    startDate = $("#begin").val();
    endDate = $("#end").val();
    dateType="defineDate";
    break;
  }
};
function p(s) {
  return s < 10 ? '0' + s: s;
};
// 根据产品，提取对应批次
function generateProcedureSelect(productId,callback){
    var option = "<option value='0'>全部</option>";
    $.ajax({
        url: contextPathGet + "/statistics/generateProBatchids2/"+productId,
        dataType:"JSON",
        type:"POST",
        async:false,
        success:function(data){
          $("#productBatchid").empty();
          $.each(data,function(idx,item){
            option += "<option value='"+ item.productBatchid +"'>"+ item.productBatchid +"</option>";
          });
          $("#productBatchid").append(option);
          $("#productBatchid").trigger("chosen:updated");
          if(callback)
            callback($("#productBatchid").val());
        }
      });
  }
