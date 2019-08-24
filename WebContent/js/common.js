
$(document).ready(function($){
	$.ajaxSetup({ cache:false });
	jQuery.prototype.serializeObject=function(){  
	    var a,o,h,i,e;  
	    a=this.serializeArray();  
	    o={};  
	    h=o.hasOwnProperty;  
	    for(i=0;i<a.length;i++){  
	        e=a[i];  
	        if(!h.call(o,e.name)){  
	            o[e.name]=e.value;  
	        }  
	    }  
	    return o;  
	};
//  sidebar dropdown menu
	if(document.body.clientWidth<768){
		$(".sidebar-menu").css("height","auto");}
  $('#container').addClass('sidebar-closed');
  $('#sidebar .sub-menu > a').click(function () {
    var last = $('.sub-menu.open', $('#sidebar'));
    last.removeClass("open");
    $('.arrow', last).removeClass("open");
    $('.sub', last).slideUp(200);
    var sub = $(this).next();
    if (sub.is(":visible")) {
      $('.arrow', $(this)).removeClass("open");
      $(this).parent().removeClass("open");
      sub.slideUp(200);
    } else {
      $('.arrow', $(this)).addClass("open");
      $(this).parent().addClass("open");
      sub.slideDown(200);
    }
  });
  

if(document.body.clientWidth<768){
	//$(".sidebar-menu").css("height","auto");
	  $('.tooltips').click(function () {
		  if ($('#sidebar > ul').is(":visible") === true) {
			  $('.tooltips').removeClass("tooltips_color");
		      $("#container").addClass("sidebar-closed");
		    } else {
		      $('.tooltips').addClass("tooltips_color");
		      $("#container").removeClass("sidebar-closed");
		    }
		  if ($('#companyIdUrl').is(":visible") === true) {
			  $('#companyIdUrl').hide();
		  }		
	  })
	  $('.company_list').click(function () {
		  if ($('#companyIdUrl').is(":visible") === false) {
			  $(".company_list").addClass("company_color");
			  $('#companyIdUrl').show(); 
		  }else{
			  $(".company_list").removeClass("company_color");
			  $('#companyIdUrl').hide();   			  
		  }
		  if ($('#sidebar > ul').is(":visible") === true) {
			  $('.tooltips').removeClass("tooltips_color");
			  $("#container").addClass("sidebar-closed");
		  }		
	  })
	  }
//  sidebar toggle
//  $('.tooltips').click(function () {
//    if ($('#sidebar > ul').is(":visible") === true) {
//      $("#container").addClass("sidebar-closed");
//    } else {
//      $("#container").removeClass("sidebar-closed");
//    }
//  });
//  $('.company_list').click(function () {
//	    if ($('.menu_padding > ul').is(":visible") === true) {
//	      $(".menu_padding > ul").hide();
//	    } else {
//	    	$(".menu_padding > ul").show();
//	    }
//	  });
  function responsiveView() {
    var wSize = $(window).width();
    if (wSize <= 768) {
      $('#container').addClass('sidebar-close');
      $('#sidebar > ul').hide();
    }

    if (wSize > 768) {
      $('#container').removeClass('sidebar-close');
      $('#sidebar > ul').show();
    }
  }
  
  $("a[target=releaseTodo]").each(function(){
      var $this = $(this);
      var selectedIds = $this.attr("rel") || "ids";
      var postType = $this.attr("postType") || "map";
      $this.click(function(){
         var targetType = $this.attr("targetType") || $this.attr("data-target");
         var ids = _getIds(selectedIds, targetType);
         if (!ids) {
              swal("错误", "请选择正确的信息。", "error");
              return false;
         }
          var _callback = $this.attr("callback") || (targetType == "dialog" ? dialogAjaxDone : dialogReloadCallback);
          if (! $.isFunction(_callback)) _callback = eval('(' + _callback + ')');
        
          function _doPost(){
             swal("发布成功");
          }
          var title = $this.attr("title");
          if (title) {
              swal({
                  title: title,
                  type: "warning",
                  showCancelButton: true,
                  confirmButtonColor: '#DD6B55',
                  confirmButtonText: '确认',
                  cancelButtonText: "取消",
                  closeOnConfirm: false
              },
              function(isConfirm){
                  if (isConfirm) _doPost();
               });
        } else {
        _doPost();
         }
         return false;
      });
  });
 
  $('.collapse-trigger').each(function(i, el) {
    var $trigger = $(this);
    var $p = $trigger.parents('.fold-wrap:first');
    $trigger.on('click', function(event){
      if ($p.hasClass('folded')){
        event.stopPropagation();
        $p.removeClass('folded');
      } else {
        $p.addClass('folded');
      }
      return false;
    });
  });
  //  custom scrollbar
//  $("#sidebar").niceScroll({
//    cursorcolor:"#6db6f2",
//    cursorwidth: '0',
//    cursorborderradius: '10px',
//    background: '#404040',
//    cursorborder: ''
//  });
//  $(".main-body").niceScroll({
//    cursorcolor:"#6db6f2",
//    cursorwidth: '4',
//    cursorborderradius: '2px',
//    background: '#404040',
//    cursorborder: '',
//    zindex: '1000',
   // touchbehavior:'true'
    
//  });
//  $(".tree-list").niceScroll({
//    cursorcolor:"#6db6f2",
//    cursorwidth: '2',
//    cursorborderradius: '1px',
//    background: '#404040',
//    cursorborder: '',
//    zindex: '1000'
//  });

  if ($.table) $.table.init('table',{toolbar:'#toolBar'});
  initUI(); 
  
  $("#btnSearchClear").click(function(){
	  clearSearchOptions();
	  return true;
  });
});
function initUI(_box){
  var $p = $(_box || document);
  if ($.fn.lookup) $("a[lookupGroup]", $p).lookup();
  if ($.fn.multLookup) $("[multLookup]:button", $p).multLookup();
  if ($.fn.ajaxTodo) $("a[target=ajaxTodo]", $p).ajaxTodo();
  if ($.fn.uploadify) {
    $(":file[uploaderOption]", $p).each(function(){
      var $this = $(this);
      var options = {
        fileObjName: $this.attr("name") || "file",
        auto: true,
        multi: true,
        onUploadError: uploadifyError
      };
      
      var uploaderOption = DWZ.jsonEval($this.attr("uploaderOption"));
      $.extend(options, uploaderOption);
      DWZ.debug("uploaderOption: "+DWZ.obj2str(uploaderOption));
      
      $this.uploadify(options);
    });
  }
  $("a[target=download]", $p).each(function(){
    var $this = $(this);
    $this.click(function(event){
      var url = $this.attr("url");
      if($this.attr("data-target")) {
        $.table.setCurrent($this.attr("data-target"));
        var selectId = $.table.getSelectedId();
        if (selectId.length == 1) {
          selectId = selectId[0];
          url += $('tr[data-uniqueid=' + selectId + ']').attr('url');
        } else {
          swal("错误", "请选择正确的信息。", "error");
          return false;
        }
      }
      if (!url.isFinishedTm()) {
        swal("Oops...", "Something went wrong!", "error");
        return false;
      }
      $this.attr('href', url);
    });
  });
  $("a[target=dialog]", $p).each(function(){
    $(this).click(function(event){
      var $this = $(this);
      var checkRs = $this.attr("checkRs");
      if(checkRs!=null && checkRs!=undefined &&checkRs !=""){
    	  if("0" == checkRs){
    		  return false;
    	  }
      }
      if($this.attr("data-target")) {
        $.table.setCurrent($this.attr("data-target"));
      }
      var selectId = $.table.getSelectedId();
      var op = {};
      op.title = $this.attr("title") || $this.text();
      op.url = $this.attr("href");
      op.destroyOnClose = $this.attr("refresh") || true;
      op.close = eval($this.attr("close") || "");
      var rel = $this.attr("rel") || "_blank";
      if (op.url.match(/{[A-Za-z_]+[A-Za-z0-9_]*}/)) {
        if (selectId.length == 1) {
          selectId = selectId[0];
          op.url = op.url.replace(/{[A-Za-z_]+[A-Za-z0-9_]*}/,selectId);
          rel += selectId;
        } else {
          swal("错误", "请选择正确的信息。", "error");
          return false;
        }
      }
      
      if (!op.url.isFinishedTm()) {
        swal("Oops...", "Something went wrong!", "error");
        return false;
      } else{
        $.pdialog.open(rel, op);
      }
      return false;
    });
  });
  $("a[target=page]", $p).each(function(){
    $(this).click(function(event){
    	
      var $this = $(this);
      if($this.attr("data-target")) {
        $.table.setCurrent($this.attr("data-target"));
      }
      var op = {};
      op.title = $this.attr("title") || $this.text();
      op.url = $this.attr("href");
      if (op.url.match(/{[A-Za-z_]+[A-Za-z0-9_]*}/)) {
          var selectId = $.table.getSelectedId();
          if (selectId.length == 1) {
            selectId = selectId[0];
            op.url = op.url.replace(/{[A-Za-z_]+[A-Za-z0-9_]*}/,selectId);
          } else {
            swal("错误", "请选择正确的信息。", "error");
            return false;
          }
        }
  
      if (!op.url.isFinishedTm()) {
        swal("Oops...", "Something went wrong!", "error");
        return false;
      } else{
        // to do with page or navtab things
        var $newPage = $this.parents('.main-body').after('<div class="main-body" name="main-body" style="display:none"></div>').next();
        var $topTitle = $('.main-content > .breadcrumb');
        $newPage.loadUrl(op.url, {}, function() {
          $newPage.pageswitch('show');
          $newPage.one('hide.pageswitch',function () {
            $topTitle.children('li:last-child').remove();
          });
          $topTitle.append('<li>' + op.title + '</li>');
        
          
        });
      }
      return false;
    });
  });
  $("a[target=selectedTodo]", $p).each(function(){
    var $this = $(this);
    var selectedIds = $this.attr("rel") || "ids";
    var postType = $this.attr("postType") || "map";

    $this.click(function(){
      var targetType = $this.attr("targetType") || $this.attr("data-target");
      var ids = _getIds(selectedIds, targetType);
      if (!ids) {
        swal("错误", "请选择正确的信息。", "error");
        return false;
      }

      var _callback = $this.attr("callback") || (targetType == "dialog" ? dialogAjaxDone : dialogReloadCallback);
      if (! $.isFunction(_callback)) _callback = eval('(' + _callback + ')');
      
      function _doPost(){
        $.ajax({
          type:'POST', url:$this.attr('href'), dataType:'json', cache: false,
          data: function(){
            if (postType == 'map'){
              return $.map(ids.split(','), function(val, i) {
                return {name: selectedIds, value: val};
              })
            } else {
              var _data = {};
              _data[selectedIds] = ids;
              return _data;
            }
          }(),
          success: _callback,
          error: DWZ.ajaxError
        });
      }
      var title = $this.attr("title");
      if (title) {
        swal({
          title: title,
          type: "warning",
          showCancelButton: true,
          confirmButtonColor: '#DD6B55',
          confirmButtonText: '确认',
          cancelButtonText: "取消",
          closeOnConfirm: false
        },
        function(isConfirm){
          if (isConfirm) _doPost();
        });
      } else {
        _doPost();
      }
      return false;
    });
  });
  $("a[target=ajax]").each(function(){
    $(this).click(function(event){
      var $this = $(this);
      var rel = $this.attr("rel");
      if (rel) {
        var $rel = $("#"+rel);
        $rel.loadUrl($this.attr("href"), {}, function(){
          $rel.find("[layoutH]").layoutH();
        });
      }
      event.preventDefault();
    });
  });
}
function _getIds(selectedIds, targetType){
  var ids = "";
  if (targetType == "dialog") {
    var $box = $.pdialog.getCurrent();
    $box.find("input:checked").filter("[name='"+selectedIds+"']").each(function(i){
      var val = $(this).val();
      ids += i==0 ? val : ","+val;
    });
  } else {
    $.table.setCurrent(targetType);
    ids = $.table.getSelectedId().join(',');
  }
  return ids;
}
function subPageCallback(json){
    DWZ.ajaxDone(json);
    if (json.statusCode == DWZ.statusCode.ok){
      $.table && $.table.refreshCurrent();
      if ("closeCurrent" == json.callbackType) {
          $.pageswitch.currentEle.pageswitch('destroy');
      }
  }
}
function reloadSubPage(json) {
	if (json.statusCode == DWZ.statusCode.error) {
		if (json.message) {
			swal({
				title : json.message,
				type : 'error',
				animation : "slide-from-top",
			});
		}
	} else {
		swal({
			title : json.message,
			type : 'success',
			animation : "slide-from-top"
		}, function(isConfirm) {
			if (isConfirm && json.statusCode == DWZ.statusCode.ok) {
				if ("closeCurrent" == json.callbackType) {
					window.location.reload();
				}

			}
		});
	}
}
function dialogReloadCallback(json){
    DWZ.ajaxDone(json);
    if (json.statusCode == DWZ.statusCode.ok){
      $.table && $.table.refreshCurrent();
      if ("closeCurrent" == json.callbackType) {
          $.pdialog.destroyCurrent();
      }
  }
}
function checkCompanyCallback(json){
	DWZ.ajaxDone(json);
		$.table && $.table.refreshCurrent();
		if ("closeCurrent" == json.callbackType) {
			$.pdialog.destroyCurrent();
		}
}
function dialogReloadNotDestroyCallback(json){
	DWZ.ajaxDone(json);
	if (json.statusCode == DWZ.statusCode.ok){
		$.table && $.table.refreshCurrent();
		if ("closeCurrent" == json.callbackType) {
	          $.pdialog.destroy();
	      }
	}
}
function deleteCallback(json){
    DWZ.ajaxDone(json);
    if (json.statusCode == DWZ.statusCode.ok){
      setTimeout(function(){
        $.table && $.table.refreshCurrent(null,null,function(data){
          var pageNum = data.page.pageNum;
          var curData = $.table.getCurrentPageData();
          if ((!curData || !curData.length) && pageNum && pageNum > 0) {
            $.table.refreshCurrent(null,{pageNum:pageNum});
          }
        });
      },100);
      if ("closeCurrent" == json.callbackType) {
          $.pdialog.destroyCurrent();
      }
  }
}
function reloadRel(selector){
    var $curEle = $(selector);
    var rel = $curEle.attr("rel");
    if (rel) {
        var $wrap = $("#"+rel);
        $wrap.loadUrl($curEle.attr("href"));
    }
}

/**
 * 根据id自动局部刷新，用于module页面
 * @param json
 */
function dialogReloadRel2Module(json){
    if (json.statusCode == DWZ.statusCode.ok) {
        reloadRel('#refreshJbsxBox2moduleTree');
    }
    dialogReloadCallback(json);
}
function deleteReloadRel2Module(json){
    if (json.statusCode == DWZ.statusCode.ok) {
        reloadRel('#refreshJbsxBox2moduleTree');
    }
    deleteCallback(json);
}

/**
 * 根据id自动局部刷新，用于department页面
 * @param json
 */
function dialogReloadRel2Department(json){
	if (json.statusCode == DWZ.statusCode.ok) {
		reloadRel('#refreshJbsxBox2departmentTree');
	}
	dialogReloadCallback(json);
}
function deleteReloadRel2Department(json){
	if (json.statusCode == DWZ.statusCode.ok) {
		reloadRel('#refreshJbsxBox2departmentTree');
	}
	deleteCallback(json);
}

/**
 * 根据id自动局部刷新，用于factory页面
 * @param json
 */
function dialogReloadRel2Factory(json){
	if (json.statusCode == DWZ.statusCode.ok) {
		reloadRel('#refreshJbsxBox2factoryTree');
	}
	dialogReloadCallback(json);
}
function deleteReloadRel2Factory(json){
	if (json.statusCode == DWZ.statusCode.ok) {
		reloadRel('#refreshJbsxBox2factoryTree');
	}
	deleteCallback(json);
}
function close2upload() {
  $.table && $.table.refreshCurrent();
}
/**
 * 检查重名
 * @param json
 */
function removeByValue(arr, val) {
	  for(var i=0; i<arr.length; i++) {
	    if(arr[i] == val) {
	      arr.splice(i, 1);
	      break;
	    }
	  }
	}
function checkData(data,checkRegion,msg,msgRegion,submitButton,submitStatus,id){
	  $.each(data, function(idx, item) {
	      if (item != 1) {
	    	  msgRegion.find("div.parentFormformID").remove();
	    	  msgRegion.find("div.snformError").remove();
	    	  checkRegion.after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -12px;''><div class='formErrorContent'>* "+ msg +"<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
	    	  checkRegion.focus();
	    	  submitStatus.push(id);
	        } else {
	          msgRegion.find("div.parentFormformID").remove();
	          submitStatus.length=0;
	          }
	      });
	}
function checkData1(data,checkRegion,msg,msgRegion,submitButton){
	$.each(data, function(idx, item) {
		if (item != 1) {
			msgRegion.find("div.parentFormformID").remove();
			msgRegion.find("div.snformError").remove();
			checkRegion.after("<div class='addressformError parentFormformID formError' style='opacity: 0.87; position: absolute; top: 0px; left: 80%; margin-top: -12px;''><div class='formErrorContent'>* "+ msg +"<br></div><div class='formErrorArrow'><div class='line10'><!-- --></div><div class='line9'><!-- --></div><div class='line8'><!-- --></div><div class='line7'><!-- --></div><div class='line6'><!-- --></div><div class='line5'><!-- --></div><div class='line4'><!-- --></div><div class='line3'><!-- --></div><div class='line2'><!-- --></div><div class='line1'><!-- --></div></div></div>");
			checkRegion.focus();
		} else {
			msgRegion.find("div.parentFormformID").remove();
		}
	});
}
function add_submit(){
	$("body").addClass("Submit_on").prepend("<div class='submit_body'><img src='${contextPath}/js/submit.gif'/></div>"); 
 }
 function remove_submit(){
	 $("body").removeClass("Submit_on"); 
 }
 function add_nicescroll(){
	  //  custom scrollbar
 $("#sidebar").niceScroll({
	    cursorcolor:"#6db6f2",
	    cursorwidth: '0',
	    cursorborderradius: '10px',
	    background: '#404040',
	    cursorborder: ''
  });
  $(".main-body").niceScroll({
	    cursorcolor:"#6db6f2",
	    cursorwidth: '4',
	    cursorborderradius: '2px',
	    background: '#404040',
	    cursorborder: '',
	    zindex: '1000',
 touchbehavior:'true'
	    
 });
  $(".tree-list").niceScroll({
	    cursorcolor:"#6db6f2",
	    cursorwidth: '2',
	    cursorborderradius: '1px',
	    background: '#404040',
	    cursorborder: '',
	    zindex: '1000'
  }); 
 }
 function searchShow(){ 
	    $(".ishidden").show(); 
	     $(".fa-plus").hide(); 
	     $(".fa-minus").show(); 
	   } 
	  function searchHide(){ 
		  $(".ishidden").hide(); 
	       $(".fa-minus").hide(); 
	       $(".fa-plus").show(); 
	     }
/**
 * 清除检索条件
 * @returns void
 */
function clearSearchOptions(){
	var selectArray = $("select");
	for(var i =0; i<selectArray.length; i++){
		selectArray[i].options[0].selected=true;
	}

	var testArray = $("input[type=text]");
	for(var i =0; i<testArray.length; i++){
		testArray[i].value="";
	}
}

