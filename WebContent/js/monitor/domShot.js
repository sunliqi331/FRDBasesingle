function domShot() {
    //0.5.0-beta4方法
    // 画面截图
    // allowTaint: true, //允许污染
    // taintTest: true, //在渲染前测试图片
    // useCORS: true, //使用跨域(当allowTaint为true时这段代码没什么用,下面解释)

    html2canvas($("#canvas"),{
        allowTaint:true, // 允许污染
        height: $("#canvas").outerHeight() + 20
    }).then(function(canvas) {
        var timestamp = Date.parse(new Date());
        $('#down_button').attr('href', canvas.toDataURL());
        $('#down_button').attr('download', timestamp + '.png');
        var fileObj=document.getElementById("down_load_hidden");
        fileObj.click();
    });
}

/* $("#down").click(function(){
alert("123");
html2canvas($("#canvas"), {
    onrendered: function(canvas){
        //document.body.appendChild(canvas);
        var img = canvas.toDataURL();
        console.log(img); //在console中会输出图片的路径，然后复制在浏览器一粘贴，就可以看到。
    },
    width:300,
    height:200
})
}) */
//$("#down_button").hide();

function initExportContextMenu(){ //导入导出右键绑定事件
	$.contextMenu({
		selector: '.monitor_content',
		callback: function(key, options) {
			if(key=="importPainter"){  //监控画面导入
				$("#painterFile").click();
			}else if(key=="exportPainter"){  //监控画面导出
				exportMonitorPainter();
			}
		},
		items: { 
			exportPainter: {
				name: '导出监控画面',
				icon: 'fa-download',
			},
			importPainter: {
				name: '导入监控画面',
				icon: 'fa-upload',
			}
		}
	});
 }

//监控画面导入
$("#painterFile").bind("change",function(){
	var pathArr = $("#painterFile").val().split("\\");
	var fileName = pathArr[pathArr.length-1]; //文件名
	var fileSuffix = fileName.split(".")[1];  //文件后缀
	//验证文件后缀是否正确
	if(fileSuffix!="mptu" || fileSuffix=="" || fileSuffix==null){
		swal("错误","请选择正确的文件","error");
		return;
	}
	
	swal({    
		title: "监控画面导入",    
		text: "请输入监控名称：",   
		type: "input",    
		showCancelButton: true,    
		closeOnConfirm: false,    	
		animation: "slide-from-top",    
		inputPlaceholder: "监控名称"  
		}, 
		function(inputValue){    
			if (inputValue === false) {
				return false;       						
			}
			if (inputValue === "") {      
			swal.showInputError("监控名称不能为空！");     
			return false;    
			}  
			
		    $("#painterNewName").val(inputValue);
			var formData = new FormData($("#painterFileForm")[0]);
        	  $.ajax({
	     		url: contextPath + "/procedureMonitor/painterFileUpload",
	     		dataType:"text",
	     		type:"POST",
	     		data:formData,
	     		cache: false,
	          	contentType: false,
	          	processData: false,
	     		success:function(data){
	     			if(data=="success"){
	     				uplocadSuccess();
	     			}else{
	     				uplocadError();
	     			}
	     		}
	     	  });
			
	}); //swal语句结束
	
});


function uplocadSuccess(){
	$("#monitorPainterList").empty();
      ajaxTodo(contextPath + "/procedureMonitor/getMonitorPainterList",paintPainter);
      function paintPainter(data){
    	if(data.length==1){
    		$("#monitorPainterList").append("<option value=''>请选择</option>");
    	}  
        $.each(data,function(idx,item){
          $("#monitorPainterList").append("<option value='"+ item.id +"'>"+ item.name +"</option>");
        });
       
    };
	swal("成功","导入画面成功！","success");
}

function uplocadError(){
	window.setTimeout(function(){
		swal("错误","文件损坏或者已经过期！","error");
	},1000);
}

function exportMonitorPainter(){
	swal({
          title: "确定要导出画面？",
          type: "warning",
          showCancelButton: true,
          confirmButtonColor: '#DD6B55',
          confirmButtonText: '确认',
          cancelButtonText: "取消",
          closeOnConfirm: true
        },
        function(isConfirm){
          if (isConfirm){
        	    var id = $("#monitorPainterList").val();
	  			$.ajax({
	  	     		url: contextPath + "/procedureMonitor/painterFileDownload",
	  	     		dataType:"text",
	  	     		type:"POST",
	  	     		data:"id="+id,
	  	     		success:function(data){
	  	     			if(data=="error"){
	  	     				window.setTimeout(function(){
	  	     					swal("错误","未知错误，请重试！","error");
	  	     				},1000);
	  	     			}else{
	  	     				var url= contextPath + "/statistics/downloadExport/"+data;
	  		     		    document.getElementById("ifile").src=url;
	  	     			}
	  	     		}
	  	     	});
	          }
    });//swal语句结尾
	
}