/* ========================================================================
 * Editable Widget
 * ======================================================================== */
+function ($) {
  'use strict';

    var EditableWidget = function(element, options) {
    	console.info("EditableWidget start");
        this.options = options;
        this.$el = $(element);
        this.id = this.$el.attr('id');

        this.init();
        console.info("EditableWidget end");
    }

    var privateFunction =  function() {

    };

    EditableWidget.DEFAULTS = {
        elementType: 'Container',
        pElement: '.widget',
        wordSelector: '.widget-word'
    };

    EditableWidget.prototype = {
        init: function() {
        	console.info("EditableWidget init start");
            if (this.options.elementType != 'Container') {
                this.$el.addClass('widget-element').layers({
                    selector: '.widget-element'
                });
            }
            this.show();
            console.info("EditableWidget init end");
        },
        initContainer: function() {
        	console.info("EditableWidget initContainer start");
            this.$container = $([
                ' <div class="widget-contanier" id="' + this.id + '_Container"></div>'
            ].join(''));
            this.$pElement = $(this.options.pElement).append(this.$container);
            console.info("EditableWidget initContainer end");
        },
        initItems: function() {
        	console.info("EditableWidget initItems start");
            var that = this;
            var htmls = '';
            var colorSelectors = {
            	'red': '#FF0000',
            	'white': '#ffffff',
                'black': '#000000',
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
            this.settings = METADATA[this.options.elementType];
            $.each(this.settings, function(idx, setting) {
                htmls += that.renderItems(METADATA[setting]);
            });

            this.$container.append($(htmls));
            
            $('.widget-group', this.$container).each(function(idx, dom) {
                var $ele = $(dom);
                that.itemType = $ele.attr('item-type');
                switch (that.itemType) {
                    case 'position':

                      that.$pos = $ele;
                        $('input[type= text]', that.$pos).on('blur', function(e) {
                        that.setPosition();

                        });
                        //that.$el.trigger('change');
                    break;
                    case 'backposition':
                    	that.$backpos =$ele;
                    	$('input[type=text]',that.$backpos).on('blur',function(e){
                    		that.setBackPosition();
                    	});
                    	break;
                    case 'size':
                        that.$size = $ele;
                        $('input[type=text]', that.$size).on('blur', function(e) {
                            that.setSize();
                          //  that.$el.trigger('change');
                        }).on('keydown',function(e){
                        	if(e.keyCode == 13){
                        		that.setSize();
                        	}
                        });;
                      /*  $('input[type=text]', that.$size)*/
                        
                    break;
                    case 'coverType': 
                    
                    	 that.$coverType=$('select', $ele).on('click', function(e){ 
                    		 /*if( $(this).val()=='cover'){            			
                				 $(".widget-backposition").hide();
                			 }else{
                				 $(".widget-backposition").show(); 
                			 }*/
                    		 if( $(this).val()=='repeat'){  
                    			 that.$el.css('background-position-x',0);
                    			 that.$el.css('background-position-y',0);
                				 $(".widget-backposition").hide();
                    		 }
                    		 if($(this).val() != 'cover'){                   			
                    			 var x =$('#canvas_backposition1').val()+'px';
                    			 var y =$('#canvas_backposition2').val()+'px';
                    			 that.$el.css('background-position-x',x);
                    			 that.$el.css('background-position-y',y);
                    			 that.$el.css('background-size','');
                    			 that.$el.css('background-repeat', $(this).val());
                    			 $(".widget-backposition").show(); 
                    			
                    		}else{
                    			 that.$el.css('background-size', $(this).val());
                    			 that.$el.css('background-position-x',0);
                    			 that.$el.css('background-position-y',0);
                    			 $(".widget-backposition").hide();
                    		 }
                    		 that.$el.trigger('change');
                         });
                    		
                    break;
                    case 'background':
                        console.log('background');
                        that.$bgColor = $('.colorpicker-component', $ele);
                        var $color = that.$bgColor;
                        var attrName = 'background-color';
                    case 'bordercolor':
                        console.log('bordercolor');
                        //updatedBy:xsq 7.5
                        if (!attrName && !$color) {
                            that.$bdColor = $('.colorpicker-component', $ele);
                            var $color = that.$bdColor;
                            var attrName = 'border-color';
                        }
                    case 'color':
                        if (!attrName && !$color) {
                            that.$color = $('.colorpicker-component', $ele);
                            var $color = that.$color;
                            var attrName = 'color';
                        }
                        var orgCol = that.$el.css(attrName);
//                        console.log('color');
                        console.log('attrName: ' + attrName);
                        console.log('orgCol: ' + orgCol);
                        $color.find("input").colorpicker({
                            colorSelectors: colorSelectors})
                        .on('changeColor',function(event){
                        	var color = event.color;
                        	var $this = $(this);
                        	console.log($this.val());
                        	var colorVal = color;
                        	//updatedBY:xsq 7.6
                        	var text = that.$el.find("li");
                        	if(text){
                        		text.css(attrName, $this.val());
                        	}
                        		that.$el.css(attrName, $this.val());
                            var trigger_ = function(){
                            	that.$el.trigger('change');
                            }
                            setTimeout(trigger_,600);
                        });
                    break;
                    case 'fontfamily':
                        $('select', $ele).on('change', function(e){
                            that.$el.css('font-family', $(this).val());
                            that.$el.trigger('change');
                        });
                    break;
                    case 'timer':
                        $('select', $ele).on('change', function(e){
                        	var formater = $(this).val();
                        	console.log(that.$el.data("timer"));
                        	if(that.$el.data("timer")){clearInterval(that.$el.data("timer"));}
                        	that.$el.find(".widget-word").text(storageManager.nowDate(formater));
        					var timer = setInterval(function(){
        						that.$el.find(".widget-word").text(storageManager.nowDate(formater));
        					},1000);
        					that.$el.data("timer",timer);
        					that.$el.attr("data-formatter",formater);
                            /*that.$el.css('font-family', $(this).val());*/
                            that.$el.trigger('change');
                        });
                    break;
                    case 'canvasLineWidth':
                    	that.$fontSize = $('input[type=text]', $ele).spinner({
                            min: 1,
                            max: 12,
                            stop: function( event, ui ) {
                            	var e = that.$el.hasClass('textbox') ? that.$el : that.$el.find('canvas');
                                var canvas = document.getElementById(e.attr("id"));
                                var ctx=canvas.getContext("2d");
                                ctx.clearRect(0, 0, e.width(), e.height());
                                canvas.width = e.width();
                                var type = that.$el.find(".canvas").attr("data-type");
                                e.attr("data-lineWidth",$(this).val());
                                storageManager["render"+type](canvas,{lineWidth:$(this).val(),strokeStyle:e.attr("data-strokeStyle"),fillStyle:e.attr("data-fillStyle")});
                                that.$el.trigger('change');
                            }
                        });
                    break;
                    
                    case 'canvasStrokeStyle':
                    	var e = that.$el.hasClass('textbox') ? that.$el : that.$el.find('canvas');
                        var canvas = document.getElementById(e.attr("id"));
                        var ctx=canvas.getContext("2d");
                    	var orgCol = ctx.strokeStyle;
                    	that.$color = $('.colorpicker-component', $ele);
                    	var $color = that.$color;
                        $color.find("input").colorpicker({    colorSelectors: colorSelectors})
                        .on('changeColor',function(event){
                        	var $this = $(this);
                        	var colorVal = event.color;
                        	ctx.clearRect(0, 0, e.width(), e.height());
                            canvas.width = e.width();
                            var type = that.$el.find(".canvas").attr("data-type");
                            e.attr("data-strokeStyle",$(this).val());
                            storageManager["render"+type](canvas,{lineWidth:e.attr("data-lineWidth"),strokeStyle:$(this).val(),fillStyle:e.attr("data-fillStyle")});
                            that.$el.trigger('change');
                        });
                    	
                    break;
                    case 'canvasFillStyle':
                    	
                    	var e = that.$el.hasClass('textbox') ? that.$el : that.$el.find('canvas');
                        var canvas = document.getElementById(e.attr("id"));
                        var ctx=canvas.getContext("2d");
                    	var orgCol = ctx.strokeStyle;
                    	that.$color = $('.colorpicker-component', $ele);
                    	var $color = that.$color;
                        $color.find("input").colorpicker({colorSelectors: colorSelectors})
                        .on('changeColor',function(event, color){
                        	var $this = $(this);
                        	//$(this).val("#ffffff");
                        	var colorVal = event.color;
                        	ctx.clearRect(0, 0, e.width(), e.height());
                            canvas.width = e.width();
                            var type = that.$el.find(".canvas").attr("data-type");
                            //updatedBy:xsq 6.30
                          
                    	   e.attr("data-fillStyle",$(this).val());
                    	   console.log("*************fillStyle"+$(this).val());
                    	   storageManager["render"+type](canvas,{lineWidth:e.attr("data-lineWidth"),strokeStyle:e.attr("data-strokeStyle"),fillStyle:$(this).val()});
                            that.$el.trigger('change');
                        });
                    	
                    break;
                    case 'fontsize':
                        that.$fontSize = $('input[type=text]', $ele).spinner({
                            min: 12,
                            max: 32,
                            stop: function( event, ui ) {
                                (that.$el.hasClass('textbox') ? that.$el : that.$el.find('.textbox')).css('font-size', $(this).val() + 'px');
                                that.$el.trigger('change');
                            }
                        });
                    break;
                    case 'word':
                        that.$textEle = that.$el.find(that.options.wordSelector);
                        that.$word = $('input[type=text]', $ele).on('blur', function(e){
                            that.$textEle.text($(this).val());
                            that.$el.trigger('change');
                        });
                    break;
                    
                    
                    
                    
                    
                    
                    
                    case 'disablePosition':
                   	 $('select', $ele).on('change', function(e){
                   		 if($(this).val() == 'static'){
                   			that.$el.draggable('disable');
                   		 }
                   		 if($(this).val() == 'absolute'){
                   			that.$el.draggable('enable');
                   		 }
                        });
                    
                    case 'wordLeval':
                    	that.$wordLeval = $('select', $ele).on('change', function(e){
                            that.$el.css('text-align', $(this).val());
                            that.$el.trigger('change');
                        });
                    break;
                    
	               case 'wordValign':
	            	   that.$wordValign = $('select', $ele).on('change', function(e){
	            		   var elHeight = that.$el.height();
	            		   var pHeight = that.$el.find("p").height();
	            		   var heightProty = $(this).val();
	            		   that.$el.find(".view").css("width",that.$el.width());
                  		   if(heightProty == 'top'){
	                   	    that.$el.find(".view").css({"top":"0px","position":"absolute","transform":"translateY(0%)"});
	                     }
	                    if(heightProty == 'center'){
	                   	     that.$el.find(".view").css({"top":"50%","position":"absolute","transform":"translateY(-50%)"})
	                    }
                       if(heightProty == 'bottom'){
                    	   that.$el.find(".view").css({"top":"100%","position":"absolute","transform":"translateY(-100%)"})
                       }
                   		/*that.$el.css('display','block');
                        var $child = that.$el.find("p");
                        $child.css({'display':'block','overflow':'hidden'});
                   	    var childHeight = $child.height();
                   	    var fatherHeight = that.$el.height();
                   	    that.$el.css({"position":"relative"});
                   	    $child.css({"position":"absolute"});
                   	    var top = $child.top;
                           var heightProty = $(this).val();
	                    if(heightProty == 'top'){
	                   	    var top = "0px";
	                   	    $child.css({"top":top});
	                     }
	                    if(heightProty == 'center'){
	                   	    var top = 0.5*(fatherHeight - childHeight);
	                   	    $child.css({"top":top});
	                    }
                        if(heightProty == 'bottom'){
	                       	var top = fatherHeight - childHeight ;
	                       	$child.css({"top":top});
                        }*/
	            		   
                        that.$el.trigger('change');
                   });
	           // break;
                case 'paramHeight':
                    that.$paramHeight = $('input[type=text]', $ele).spinner({
                        min: 12,
                        max: 40,
                        stop: function( event, ui ) {
                            (that.$el.hasClass('textbox') ? that.$el : that.$el.find('.textbox')).css('line-height', $(this).val() + 'px');
                            that.$el.trigger('change');
                        }
                    });
                break;
                    
                case 'fontStyle':
                	$('select', $ele).on('change', function(e){
                        that.$el.css('font-style', $(this).val());
                        that.$el.trigger('change');
                    });
                break;    
                
                case 'fontWeight':
                	$('select',$ele).on('change',function(e){
                		that.$el.css('font-weight',$(this).val());
                		that.$el.trigger('change');
                	});
                	 break;


                	 //2018-12-19 zq start 圆角触发方法
                    case 'Radius':
                        that.$Radius = $('input[type=text]', $ele).spinner({
                            min: 10,
                            max: 30,
                            stop: function( event, ui ) {
                                that.$el.css('border-radius',$(this).val() + 'px');
                                that.$el.trigger('change');
                            }
                        });
                        break;
                     //end


                    case 'canvasLineHeight':
                        that.$paramHeight = $('input[type=text]', $ele).spinner({
                            min: 1,
                            max: 12,
                            stop: function( event, ui ) {
                                var e = that.$el.hasClass('textbox') ? that.$el : that.$el.find('canvas');
                                var canvas = document.getElementById(e.attr("id"));
                                var ctx=canvas.getContext("2d");
                                ctx.clearRect(0, 0, e.width(), e.height());
                                canvas.width = e.width();
                                var type = that.$el.find(".canvas").attr("data-type");
                                e.attr("data-lineWidth",$(this).val());
                                storageManager["render"+type](canvas,{lineWidth:$(this).val(),strokeStyle:e.attr("data-strokeStyle"),fillStyle:e.attr("data-fillStyle")});
                                that.$el.trigger('change');
                            }
                        });
                        break;

                    case 'border':
                        that.$border = $('input[type=text]', $ele).spinner({
                            min: 0,
                            max: 5,
                            stop: function() {
                                that.$el.css('border-width', $(this).val());
                                that.$el.trigger('change');
                            }
                        });
                        break;


                    case 'fileupload':
                    	 that.$fileupload = $('.widget-fileupload', $ele);
                    	 
                    break;

                    case 'tableLineCount':
                    	that.$tableLine = $('input[type=text]', $ele).spinner({
                    		min: 2,
                    		max: 12,
                    		stop: function( event, ui ) {
                    			//that.$el.css('border-width', $(this).val());
                    			var tr =  that.$el.find("tbody tr:last").prop("outerHTML");
                    			var b_tr = '';
                    			var lineCount = parseInt($(this).val());

                    			//2018-12-14 zq 中移项目监控设计AlarmTable行数BUG start
                    			if(isNaN(lineCount)){
                                    lineCount = "1";
                                }
                                var classValue = that.$el.find("table").attr("class").split(" line_")[0] + ' line_' + lineCount;
                                that.$el.find("table").attr("class",classValue);
                                //2018-12-14 zq 中移项目监控设计AlarmTable行数BUG end
                                for(var i=0;i<lineCount;i++){
                    				b_tr += tr;
                    			}
                    			 that.$el.find("tbody").empty();
                    			$(b_tr).appendTo( that.$el.find("tbody"));
                    			var data = JSON.parse(localStorage.getItem("monitorTableData"));
                    			//var data =JSON.parse(localStorage.getItem("AlarmTableData"));
                    			console.log(data);
                    			if(data){
                    				$.each(data,function(idx,item){
                    					if(item.chartId == that.$el.attr('id')){
                    						var tableData = JSON.parse(item.data);
                    						$.each(tableData,function(idx_,item_){
                    							if(item_.name == 'lineCount'){
                    								item_.value = lineCount;
                    							}
                    							tableData.splice(idx_,1,item_);
                    						});
                    						/*data.splice(idx,1,tableData);*/
                    						item.data = JSON.stringify(tableData);
                    					}
                    				})
                    			}
                    			console.log(JSON.stringify(data));
                    			localStorage.setItem("monitorTableData",JSON.stringify(data));
                    			//localStorage.setItem("AlarmTableData",JSON.stringify(data));

                    			//that.$el.trigger('change');
                    		}
                    	});
                    	break;
                    default:
                    break;
                }
            });

            that.updateValue();

            $('input[type=text]', this.$container).on('keydown', function(e) {
                if(e.keyCode == 13) { // Enter Key
                    $(this).trigger('blur');
                }
            });
            console.info("EditableWidget initItems end");
        },
        initEvents: function() {
        	console.info("initEvents start");
            var that = this;
            this.$el.on('mousedown touchdown', function(e) {
            	console.info("mousedown touchdown start");
                if (e.type == 'mousedown' && e.button == 2) return false;
                    e.preventDefault();
                    e.stopPropagation();
                    that.show.apply(that);
                    console.info("initEvents end");
                }
            );
            console.info("initEvents end");
        },
        show: function() {
        	console.info("show start");
            var that = this;
            var e    = $.Event('show.widget');

            this.$el.trigger(e);

            if (e.isDefaultPrevented()) return;

            if (!this.isInited) {
                this.initContainer();
                this.initItems();
                this.initEvents();
                this.isInited = true;
            }

            this.$container.siblings('.widget-contanier').hide()
            .end().show();
            console.info("show end");
        },
        // generate elements htmls of diffent kind of items
        renderItems: function(settings) {
        	console.info("renderItems start");
            var temp = ['<div class="widget-group widget-' + settings.identifier + '" item-type="' + settings.identifier + '">'];
            switch(settings.type) {
                case 'text' :
                    temp.push('<label class="widget-label" for="' + this.id + '_' + settings.identifier + '">' + settings.title + '</label>',
                            '<input type="text" class="widget-control" id="' + this.id + '_' + settings.identifier + '"/>');
                break;
                case 'doubletext':
                    temp.push('<label class="widget-label">' + settings.title + '</label>',
                        '<div class="widget-unit">',
                        '<label for="' + this.id + '_' + settings.identifier + '1">' + settings.name1 + '</label>',
                        '<input type="text" class="widget-control" id="' + this.id + '_' + settings.identifier + '1" placeholder="' + settings.name1 + '">',
                        '</div>',
                        '<div class="widget-unit">',
                        '<label for="' + this.id + '_' + settings.identifier + '2">' + settings.name2 + '</label>',
                        '<input type="text" class="widget-control" id="' + this.id + '_' + settings.identifier + '2" placeholder="' + settings.name2 + '">',
                        '</div>');
                break;
                case 'colorpicker':
                    temp.push('<label class="widget-label" for="' + this.id + '_' + settings.identifier + '">' + settings.title + '</label>',
                            '<div class="input-group colorpicker-component">',
                            '<input type="text" class="widget-control" value="" id="' + this.id + '_' + settings.identifier + '" />',
                            '<span class="input-group-addon"><i></i></span></div>');
		        break;
                case 'select':
                    temp.push('<label class="widget-label" for="' + this.id + '_' + settings.identifier + '">' + settings.title + '</label>',
                            '<select class="widget-control" id="' + this.id + '_' + settings.identifier + '">');
                    for (var i = 0; i < settings.options.length; i++) {
                        temp.push('<option value="' + settings.options[i].value +'">' + settings.options[i].text + '</option>');
                    }
                    temp.push('</select>');
                break;
                case 'textarea':
                    temp.push('<label class="widget-label" for="' + this.id + '_' + settings.identifier + '">' + settings.title + '</label>',
                            '<textarea class="widget-control" id="' + this.id + '_' + settings.identifier + '" />');
                break;
                case 'checkboxes':
                    if (settings.identifier == 'properties' && this.properties) {
                        temp.push('<label class="widget-label">' + settings.title + '</label>');
                        $.each(this.properties, function(i, v) {
                            temp.push('<br><input type="checkbox" class="" id="' + this.id + '_' + v.id + '" value="' + v.name + '" checked/>',
                                '<label for="' + this.id + '_' + v.id + '">' + v.name + '</label>');
                        })
                    }/*else if(settings.identifier == 'backgroundType'){
                    	temp.push('<label class="widget-label">' + settings.title + '</label>');
                            temp.push('<input type="checkbox" class="" id="backgroundType_1" value="" checked/>',
                                '<label for="backgroundType_1">显示状态图片</label>');
                            temp.push('<input type="checkbox" class="" id="backgroundType_2" value="" checked/>',
                            '<label for="backgroundType_2">显示状态颜色</label>');
                    }*/
                break;
                case 'file' :
                	var motherContent = this;
                	if (settings.identifier == 'fileupload') {
                        temp.push('<label class="widget-label" for="' + this.id + '_' + settings.identifier + '">' + settings.title + '</label>',
                             //   '<form method="post" action="'+contextPath+'/procedureMonitor/saveBackgroundPic" enctype="multipart/form-data" onsubmit="return iframeCallback(this, function(data){var message = data.message;$(\'#\'+message.split(\'-\')[0]).css({\'background\':url('+contextPath+'/company/showPic/\'+message.split(\'-\')[1]),\'background-size\':\'cover\'});localStorage.setItem(\'background\',\'url('+contextPath+'/company/showPic/\'+message.split(\'-\')[1]+\')\')})"><input type="hidden" name="widgetId" value="'+ this.id +'" /><input type="file" name="files" class="widget-control" id="' + this.id + '_' + settings.identifier + '"/><button type="submit">提交</button></form>');
                        		// test 注销
                        		//'<form method="post" action="'+contextPath+'/procedureMonitor/saveBackgroundPic" enctype="multipart/form-data" onsubmit="return iframeCallback(this, function(data){var message = data.message;$(\'#\'+message.split(\'-\')[0]).css({\'background\':\'url('+contextPath+'/company/showPic/\'+message.split(\'-\')[1]+\')\'});storageManager.saveContainerData($(\'.demo\')); localStorage.setItem(\'background\',\'url('+contextPath+'/company/showPic/\'+message.split(\'-\')[1]+\')\');    })"><input type="hidden" name="widgetId" value="'+ this.id +'" /><input type="file" name="files" class="widget-control" id="' + this.id + '_' + settings.identifier + '"/><button type="submit">提交</button></form></br><button onclick="$(\'#canvas\').css({background:\'\'});localStorage.removeItem(\'background\')">删除背景</button>');
                        '<form method="post" action="'+contextPath+'/procedureMonitor/saveBackgroundPic" enctype="multipart/form-data" onsubmit="return iframeCallback(this, function(data){var message = data.message;$(\'#\'+message.split(\'-\')[0]).css({\'background\':\'url('+contextPath+'/company/showPic/\'+message.split(\'-\')[1]+\')\'});storageManager.saveContainerData($(\'.demo\')); localStorage.setItem(\'background\',\'url('+contextPath+'/company/showPic/\'+message.split(\'-\')[1]+\')\');    })"><input type="hidden" name="widgetId" value="'+ this.id +'" /><button onclick = "clp('+this.id + '_' + settings.identifier+');">选择图片</button><input readonly="readOnly" type="text" id="fileUrl" style="width: 86px;"><input style="display:none;" type="file" name="files" class="widget-control" id="' + this.id + '_' + settings.identifier + '"/><button type="submit">提交</button></form></br><button onclick="$(\'#canvas\').css({background:\'\'});localStorage.removeItem(\'background\')">删除背景</button>');
                	}else{
                    		//alert("img");
                			console.log(motherContent.$el.attr("id"));
                    		 temp.push('<label class="widget-label" for="' + this.id + '_' + settings.identifier + '">' + settings.title + '</label>',
                    				 '<form method="post" action="'+contextPath+'/procedureMonitor/saveBackgroundPic" enctype="multipart/form-data" onsubmit="return iframeCallback(this, function(data){var message = data.message;$(\'#\'+message.split(\'-\')[0]).find(\'.device-image\').attr(\'src\',\''+contextPath+'/company/showPic/\'+message.split(\'-\')[1]);storageManager.saveComponentsData($(\'#'+motherContent.$el.attr("id")+'\'));localStorage.setItem(\'src\',\''+contextPath+'/company/showPic/\'+message.split(\'-\')[1])})"><input type="hidden" name="widgetId" value="'+ this.id +'" /><input type="file" name="files" class="widget-control" id="' + this.id + '_' + settings.identifier + '"/><button type="submit">提交</button></form>');
                    	}
                break;
                default:
                    temp.push('<label class="widget-label">type: ' + settings.type + '</label>');
                break;
            }
            temp.push('</div>');
            console.info("renderItems end");
            return temp.join('');
        },
        bindData: function(url) {
        	console.info("bindData start");
            var that = this;
            if (this.options.elementType == 'TextBox'){
            	var params = JSON.parse(that.$el.data("params"));
            	var $e = that.$el.find('.view > .textbox');
            	$.each(params,function(k,v){
            		$e.attr("data-"+k,v);
            	});
            	that.$el.trigger('change');
            	/*$e.attr("data-driverId",params.driverId);
            	$e.attr("data-codekey",params.codekey);
            	$e.attr("data-pointTypeName",params.pointTypeName);
            	$e.attr("data-propertyId",params.propertyId);
            	$e.attr("data-procedureId",params.procedureId);
            	$e.attr("data-productId",params.productId);*/
            }
            if(this.options.elementType == 'Canvas'){
            	var params = JSON.parse(that.$el.data("params"));
            	var $e = that.$el.find('.view > canvas');
            	$.each(params,function(k,v){
            		$e.attr("data-"+k,v);
            	});
            	that.$el.trigger('change');
            }
            if(this.options.elementType == 'CanvasGateWay'){
            	var params = JSON.parse(that.$el.data("params"));
            	var $e = that.$el.find('.view > canvas');
            	$.each(params,function(k,v){
            		$e.attr("data-"+k,v);
            	});
            	that.$el.trigger('change');
            }
            if(this.options.elementType == 'CanvasProceseAlter'){
            	var params = JSON.parse(that.$el.data("params"));
            	var $e = that.$el.find('.view > canvas');
            	$.each(params,function(k,v){
            		$e.attr("data-"+k,v);
            	});
            	that.$el.trigger('change');
            }
            if(this.options.elementType == 'CanvasQualified'){
                var params = JSON.parse(that.$el.data("params"));
                var $e = that.$el.find('.view > canvas');
                $.each(params,function(k,v){
                    $e.attr("data-"+k,v);
                });
                that.$el.trigger('change');
            }
            if (this.options.elementType == 'Device' && (url || this.options.url)) {
                console.log(this.id+ '  bindData')
                console.log(url);
                // 判断有无绑定数据，如果有，需要清空现有属性， 重新绑定
                if (that.$prop) that.$prop.remove();
                url && (that.options.url = url);
                that.bindId = this.id; // need to change to actual device id
                that.$el.attr('data-url', that.options.url);
                $.ajax({
                    url: that.options.url,
                    type: 'GET',
                    cache: false,
                    dataType: 'json',
                    success: function(response) {
                        that.properties = response;
                        that.$prop = $(that.renderItems(METADATA['Properties'])).appendTo(that.$container)
                        .on('change', 'input[type="checkbox"]', function(){
                            that.syncSelectedProperties();
                        });
                        if (!that.$bindingText) {
                            var deviceInfoList = ['<ul>',
                            '<li>设备名称：' + response[0].mesDriver + '</li>',
                            '<li>设备编号：' + response[0].mesDriverSn + '</li>',
                            '<li></li>',
                            '</ul>'].join('');
                            var htmls = ['<div class="prop-textbox textbox" widget-type="BindTextBox" bind-target="' + that.id + '">',
                            //'<div class="prop-title">' + deviceInfoList + '</div>',
                            '<ul class="widget-word"></ul></div>'].join('');
                            that.$bindingText = $(htmls).appendTo(that.$el.parent());
                            var position = that.getPosition();
                            var backposition =that.getBackPosition();
                            var size = that.getSize();
                            //updatedBy:xsq 7.6
                            that.$bindingText.css({
                                left: (position[0] + size[0]) + 'px',
                                top: position[1] + 'px'
                            })
                            that.$el.trigger('bindTextCreate');
                            var $view = that.$el.find('.view');
                            if ($view.children('.device-info').length) {
                                $view.children('.device-info').html(deviceInfoList);
                            } else {
                                $view.append('<div class="device-info">' + deviceInfoList + '</div>');
                            }
                            
                        }
                        that.syncSelectedProperties();
                        that.$el.trigger('change');
                    },
                    error: function (res) {
                        
                    }
                });
            }
            console.info("bindData end");
        },
        backgroundImg:function(data){
        	var str = '';
        },
        syncSelectedProperties: function() {
        	console.info("syncSelectedProperties start");
            if (!this.$bindingText) return;
            var props = this.getSelectedProperties();
            var values = this.getSelectedValues(props);
            var temp = '';
            for (var i = 0; i <= props.length - 1; i++) {
               //  temp += '<li id=\"prop_'+ values[i].id +'\">' + values[i].name + ':' + values[i].Value + '</li>';
                 temp += '<li class=\"prop_'+values[i].mesDriverId + '_' + values[i].propertyKey +'\">' + values[i].name + ':<span></span></li>';
            };
            this.$bindingText.children('.widget-word').empty().append(temp);
            this.$bindingText.trigger('change');
            console.info("syncSelectedProperties end");
        },
        updateSelectedProperties: function(props) {
        	console.info("updateSelectedProperties start");
            if (this.$prop) {
                this.$prop.find('input[type="checkbox"]').prop('checked', false);
                for (var i = 0; i <= props.length - 1; i++) {
                    this.$prop.find('input[type="checkbox"][value="' + props[i] + '"]').prop('checked', true);
                }
            }
            console.info("updateSelectedProperties end");
        },
        getSelectedProperties: function() {
        	console.info("getSelectedProperties start");
            if (this.$prop) {
                var props = [];
                this.$prop.find('input[type="checkbox"]').each(function(i, dom) {
                    var $ele = $(dom);
                    if ($ele.prop('checked') == true) {
                        props.push($ele.val());
                    }
                });
                return props;
            }
            console.info("getSelectedProperties end");
            return null;
        },
        getSelectedValues: function(props) {
        	console.info("getSelectedValues start");
            if (props && props.length) {
                var values = [];
                $.each(this.properties, function(i, v){
                    if ($.inArray(v.name, props) != -1) {
                        values.push(v);
                    }
                });
                return values;
            }
            console.info("getSelectedValues end");
            return null;
        },
        getFamily: function() {
        	console.info("getFamily");
        	var that = this;
            var fontFamliay = that.$el.css('font-family');
            return fontFamliay;
        }, 
        getBindId: function() {
        	console.info("getBindId");
            return this.bindId || '';
        }, 
        getBindTextBox: function() {
        	console.info("getBindTextBox");
            return this.$bindingText;
        },
        getLineCount: function() {
        	console.info("getLineCount");
            return this.$tableLine;
        },
        getPosition: function() {
        	console.info("getPosition");
            return [
                parseFloat(parseFloat(this.$el.css('left')).toFixed(2)),
                parseFloat(parseFloat(this.$el.css('top')).toFixed(2))
            ];
        },
        getBackPosition:function(){
        	console.info("getBackPosition start");
        	var array = new Array();
        	var $this = this;
        	//var trigger_ = function(){
            	array.push(parseFloat(parseFloat($this.$el.css('background-position-x')).toFixed(2)));
            	array.push(parseFloat(parseFloat($this.$el.css('background-position-y')).toFixed(2)));
        	//}
            console.info("getBackPosition end");
        	return array;
            //setTimeout(trigger_,600);
        },
        getCoverType:function(){
        	console.info("getCoverType");
        	return this.$coverType;
        },
        getSize: function() {
        	console.info("getSize start");
            return [
                parseFloat(parseFloat(this.$el.css('width')).toFixed(2)),
                parseFloat(parseFloat(this.$el.css('height')).toFixed(2))
            ];
        },
        // update properties of element in widget
        updateValue: function() {
        	console.info("updateValue start");
            this.updatePosition();
            this.updateBackPosition();
            this.updateSize();
            this.updateWord();
            this.updateFontSize();
            this.updateFontFamily();
            this.updateParamHeight();
            this.updateBorder();
            this.updateRadius();
            this.updateColor();
            this.updateTableLineCount();
            this.updateWordposition();
            this.updateCoverType();
            console.info("updateValue end");
        },
        updateWord: function() {
        	console.info("updateWord start");
            if (this.$textEle && this.$word) {
                this.$word.val(this.$textEle.text());
            }
            console.info("updateWord end");
        },
        updateFontSize: function() {
        	console.info("updateFontSize start");
            if (this.$fontSize) {
            	//$(this.$el.find("div")[0]).find("div").css('font-size')
            	//$(this.$el.find(".view")).children().css('font-size')
                //this.$fontSize.val(parseInt(this.$el.css('font-size')));
                this.$fontSize.val(parseInt($(this.$el.find(".view")).children().css('font-size')));
            }
            console.info("updateWord end");
        },
        updateFontFamily: function() {
            console.info("updateFontFamily start");
            var $this = this;
            var fontFamilyGet = $this.getFamily();

            if ($this.$fontfamily) {
                $this.$fontfamily.css('font-family', fontFamilyGet);
            }
            console.info("updateFontFamily end");
        },
        updateParamHeight: function() {
        	console.info("updateParamHeight start");
        	if (this.$paramHeight) {
        		this.$paramHeight.val(parseInt($(this.$el.find(".view")).children().css('line-height')));
        	}
        	console.info("updateParamHeight end");
        },
        updateCoverType:function(){
        	console.info("updateCoverType start");
        	var $this = this;
        	var covertype = $this.getCoverType();
        	if($this.$coverType){
	        	if($this.$el.css("background-size") == 'cover'){
	        		$this.$coverType.val($this.$el.css('background-size'));
	        	}else {
	        		$this.$coverType.val($this.$el.css('background-repeat'));
	        	}
        	}
        	/*var $this = this;
        	var covertype = $this.getCoverType();
        	if($this.$coverType != ""){
        		if($this.$el.css("background-size") == 'cover'){
            		$this.$coverType.val($this.$el.css('background-size'));
            	}else {
            		$this.$coverType.val($this.$el.css('background-repeat'));
            	}
        	}*/
        	console.info("updateCoverType end");
        },
        updateBorder: function() {
        	console.info("updateBorder start");
            if (this.$border) {
                this.$border.val(parseInt(this.$el.css('border-width')));
            }
            console.info("updateBorder end");
        },


        //2018-12-19 zq start 新增圆角编辑工具修改
        updateRadius: function() {
            console.info("updateRadius start");
            if (this.$Radius) {
                this.$Radius.val(parseInt(this.$el.css('border-radius')));
            }
            console.info("updateRadius end");
        },
        //end


        updateTableLineCount: function(){
        	console.info("updateTableLineCount start");
        	if (this.$tableLine) {
        		//console.log(this.$el.find(".table > tbody > tr").length());

                //2018-12-18 zq strat 设计页面重新加载时会读取行数缓存中的行数，显示在拖拽插件的右边工具栏
                if(localStorage.getItem(this.id +"_lineCount")){
                    var count = localStorage.getItem(this.id +"_lineCount");
                    this.$tableLine.val(count);
                }else{
                    this.$tableLine.val("7");
                }
                //2018-12-18 zq end
            }
        	console.info("updateTableLineCount end");
        },
        updatePosition: function(x,y) {
        	console.info("updatePosition start");
            var position = this.getPosition();
            var $this = this;
        	 if ($this.$pos && position.length) {
                 $('input[type=text]', $this.$pos).each(function(idx){
                     var $input = $(this);
                     $input.val(position[idx]);
                 });
             }     
        	 console.info("updatePosition end");
        },
        updateBackPosition: function(x,y){
        	console.info("updateBackPosition start");
        	var $this = this;
        	var trigger_ = function(){
        		var backposition = $this.getBackPosition();
            	 if ($this.$backpos && backposition.length != 0) {
                     $('input[type=text]', $this.$backpos).each(function(idx){
                         var $input = $(this);
                         $input.val(backposition[idx]);
                     });
                 }     
        	}
        	setTimeout(trigger_,600);
        	
        	console.info("updateBackPosition end");
        },
        updateWordposition:function(){
        	console.info("updateWordposition start");
        	var wordValign = this.$wordValign;
        	var $this = this;
        	if(wordValign){
        		var transform = $this.$el.find(".view")["0"].style.transform;
        	
        		console.log($this.$el.find(".view")["0"].style.transform);
        		if(transform == "translateY(-50%)"){
        			wordValign.val("center");
        		}else if(transform == "translateY(-100%)"){
        			wordValign.val("bottom");
        		}else{
        			wordValign.val("top");
        		}
        	}
        	var wordLeval = this.$wordLeval;
        	if(wordLeval){
        		var align = $this.$el["0"].style.textAlign;
        		wordLeval.val(align);
        	}
        	console.info("updateWordposition end");
        },
        updateSize: function() {
        	console.info("updateSize start");
            var size = this.getSize();
            if (this.$size && size.length) {
                $('input[type=text]', this.$size).each(function(idx){
                    $(this).val(size[idx]);
                }); 
            }
            console.info("updateSize end");
        },
        updateLineCount: function() {
        	console.info("updateLineCount start");
            var lineCount = this.getLineCount();
            if (this.$tableLine && lineCount.length) {
                /*$('input[type=text]', this.$tableLine).each(function(idx){
                    $(this).val(lineCount.find(""));
                	console.log(lineCount);
                }); */
                this.$tableLine.val(this.$el.find(".table > tbody > tr").length);
            }
            console.info("updateLineCount end");
        },
        updateColor: function() {
        	console.info("updateColor start");
        	var that = this;
            if (this.$color) {
            	var trigger_ = function(){
            		if(that.$el.find('canvas').length != 0){
            			that.$color.find("input").colorpicker('setValue', that.$el.find('canvas').attr("data-fillStyle") || "white");
            		}else
            			that.$color.find("input").colorpicker('setValue', that.$el.css('color'));
            	}
            	setTimeout(trigger_,600);
                
            }
            if (this.$bgColor) {
            	var trigger_ = function(){
            		that.$bgColor.find("input").colorpicker('setValue', that.$el.css('background-color'));
            	}
                setTimeout(trigger_,600);
            }
            if (this.$bdColor) {
            	this.$bdColor.find("input").colorpicker('setValue', this.$el.css('border-color'));
            }
            console.info("updateColor end");
        },
     
        
        setPosition: function(){
        	console.info("setPosition start");
            if (!this.$pos) return;
            var position = [];
            $('input[type=text]', this.$pos).each(function(idx){
                position[idx] = $(this).val();
            });
            if (position.length == 2){
                this.$el.css({
                    left: (position[0] || 0) + 'px',
                    top: (position[1] || 0) + 'px'
                });
                this.$el.trigger('change');
            }
            console.info("setPosition end");
        },
        setBackPosition: function(){
        	console.info("setBackPosition start");
        	var that = this;
            if (!this.$backpos) return;
            var backposition = [];
            $('input[type=text]', this.$backpos).each(function(idx){
                backposition[idx] = $(this).val();
            });
            if (backposition.length == 2){
            	 var x =$('#canvas_backposition1').val()+'px';
    			 var y =$('#canvas_backposition2').val()+'px';
    			 that.$el.css('background-position-x',x);
    			 that.$el.css('background-position-y',y);
    			 that.$el.css('background-size','');
    			 //that.$el.css('background-repeat', $(this).val());
               /* this.$el.css({
                    left: (backposition[0] || 0) + 'px',
                    top: (backposition[1] || 0) + 'px'
                });*/
    			 that.$el.trigger('change');
            }
            console.info("setBackPosition end");
        },
        setSize: function(){
        	console.info("setSize start");
            if (!this.$size) return;
            var size = [];var that = this;
            $('input[type=text]', this.$size).each(function(idx){
                size[idx] = $(this).val();
            });
            if (size.length == 2) {
                this.$el.css({
                    width: (size[0] || 0) + 'px',
                    height: (size[1] || 0) + 'px'
                });
                var _trigger = function(){
                	that.$el.trigger('change');
                	if(that.$el.attr("widget-type") == 'Canvas'
                		|| that.$el.attr("widget-type") == 'CanvasGateWay'
                		|| that.$el.attr("widget-type") == 'CanvasProceseAlter'
                        || that.$el.attr("widget-type") == 'CanvasQualified'
                			){
        				var id = that.$el.find("canvas").attr("id");
        				var e = $("#"+id);
        				var extendsImg = that.$el.find(".canvas").attr('data-type');
        				var ele = document.getElementById(id);
        				var w_h = [that.$el.width(),that.$el.height()];//$(this).editableWidget('getSize');
        				ele.width=w_h[0];
        				ele.height=w_h[1];
        				var ctx=ele.getContext("2d");
        				ctx.clearRect(0, 0, ele.width, ele.height);
                        canvas.width = ele.width;
        				storageManager["render"+extendsImg](ele,{lineWidth:size[0]+"px",strokeStyle:size[1]+"px",fillStyle:e.attr("data-fillStyle")});
        			}
                }
                setTimeout(_trigger,600);
              //  that.$el.trigger('change');
            }
            var e = $.Event('resize', {isSetSize: true});

            this.$el.trigger(e);
            console.info("setSize end");
        },
        setBackgroundColor: function() {
            
        },
        // setRadius: function(){
        // 	console.info("setRadius start");
        //     if (!this.$pos) return;
        //     var position = [];
        //     $('input[type=text]', this.$pos).each(function(idx){
        //         position[idx] = $(this).val();
        //     });
        //     if (position.length == 2){
        //         this.$el.css({
        //             left: (position[0] || 0) + 'px',
        //             top: (position[1] || 0) + 'px'
        //         });
        //         this.$el.trigger('change');
        //     }
        //     console.info("setRadius end");
        // },
        getElementType: function() {
        	console.info("getElementType");
            return this.options.elementType;
        },
        copyitem : function() {
            console.info("copyitem");
            return this.options.elementType;
        },
        destroy:function() {
        	console.info("destroy start");
            this.$container.remove();
            if (this.$bindingText) {
                this.$bindingText.editableWidget('destroy');
                this.$bindingText.remove();
            }
            console.info("destroy end");
        }
    };

    // PLUGIN DEFINITION
    // ==========================
        // PLUGIN DEFINITION
    // ==========================
    var allowedMethods = [
        'updatePosition','updateSize','updateValue','updateBackPosition',
        'bindData',
        'copyitem',
        'getElementType',
        'getSelectedProperties', 'getBindId',
        'updateSelectedProperties',
        'getBindTextBox',
        'destroy','getPosition','getSize','updateLineCount'
    ];

    function Plugin(option) {
    	console.info("Plugin start");
        var value,
            args = Array.prototype.slice.call(arguments, 1);
        this.each(function () {
            var $this = $(this), 
                data = $this.data('editableWidget'),
                elementType = $this.attr('widget-type');
            var options = $.extend({}, EditableWidget.DEFAULTS, {elementType: elementType}, $this.data(), typeof option == 'object' && option)
            if (!data) {
                $this.data('editableWidget', (data = new EditableWidget(this, options)));
            }

            if (typeof option === 'string') { //call method
                if ($.inArray(option, allowedMethods) < 0) {
                    throw new Error("Unknown method: " + option);
                }

                //call method
                value = data[option].apply(data, args);
            } else if (options.show) {
                data.show();
            }
        });
        console.info("Plugin end");
        return typeof value === 'undefined' ? this : value;
    }

    var old = $.fn.editableWidget;

    $.fn.editableWidget             = Plugin;
    $.fn.editableWidget.Constructor = EditableWidget;
    $.fn.editableWidget.defaults = EditableWidget.DEFAULTS;

    // NO CONFLICT
    // ====================
    $.fn.editableWidget.noConflict = function () {
    	console.info("fn.editableWidget.noConflict");
        $.fn.editableWidget = old
        return this
    }

    
}(jQuery);

/* ========================================================================
 * Layers Manager
 * ======================================================================== */
+function ($) {
    var Layers = function(element, options) {
    	console.info("Layers start");
        this.$layer = $(element);
        this.options = options;
        var z = this.$layer.css('z-index');
        this.order = z == 'auto' ? this.$layer.index(options.selector) : parseInt(z);
        // this.$layer.attr('order', this.order); // for debug
        this.$layer.css('z-index', this.order);
        console.info("Layers end");
    };

    var getZIndex =  function($ele) {
    	console.info("getZIndex");
        var tempIndex = $ele.css('z-index');
        return parseInt(tempIndex == 'auto' ? 0 : tempIndex);
    };

    Layers.DEFAULTS = {
        selector: 'layers'
    };

    Layers.prototype = {
        getOrder: function() {
            return this.order;
        },
        setOrder: function(order) {
        	console.info("Layers.prototype setOrder start");
            this.order = order;
            // this.$layer.attr('order', order); // for debug
            this.$layer.css('z-index', order);
            this.$layer.trigger('change');
            console.info("Layers.prototype setOrder end");
        },
        getMaxOrder: function() {
            return $(this.options.selector).length - 1;
        },
        isFront: function() {
            return this.order == this.getMaxOrder() ? true : false;
        },
        isBottom: function() {
            return this.order == 0 ? true : false;
        },
        changeLayer: function(key) {
        	console.info("Layers.prototype changeLayer start");
            var that = this;
            // var order = this.$layerlayers('getOrder');
            var maxZIndex = this.getMaxOrder();
            switch (key) {
                case 'bringForward':
                    if (that.order < maxZIndex) {
                        that.$layer.siblings(that.options.selector).each(function(){
                            var $ele = $(this);
                            var eo = $ele.layers('getOrder');
                            if ((that.order + 1) == eo) {
                                $ele.layers('setOrder', that.order);
                                that.setOrder(eo);
                            }
                        });
                    }
                break;
                case 'sendBackward':
                    if (that.order > 0) {
                        that.$layer.siblings(that.options.selector).each(function(){
                            var $ele = $(this);
                            var eo = $ele.layers('getOrder');
                            if ((that.order - 1) == eo) {
                                $ele.layers('setOrder', that.order)
                                that.setOrder(eo);
                            }
                        });
                    }
                break;
                case 'bringFront':
                    if (that.order != maxZIndex) {
                        that.$layer.siblings(that.options.selector).each(function(){
                            var $ele = $(this);
                            var eo = $ele.layers('getOrder');
                            if (that.order < eo) {
                                $ele.layers('setOrder', eo - 1)
                            }
                        });
                        that.setOrder(maxZIndex);
                    }
                break;
                case 'sendBack':
                    if (that.order != 0) {
                        that.$layer.siblings(that.options.selector).each(function(){
                            var $ele = $(this);
                            var eo = $ele.layers('getOrder');
                            if (that.order > eo) {
                                $ele.layers('setOrder', eo + 1)
                            }
                        });
                        that.setOrder(0);
                    }
                break;
                case 'copyitem':
                    if (that.order != 0) {
                        that.$layer.siblings(that.options.selector).each(function(){
                            var $ele = $(this);
                            var eo = $ele.layers('getOrder');
                            if (that.order > eo) {
                                $ele.layers('setOrder', eo + 1)
                            }
                        });
                        that.setOrder(0);
                    }
                break;
                default:
                break;
            }
            console.info("Layers.prototype changeLayer end");
        }
    }

    // PLUGIN DEFINITION
    // ==========================
    var allowedMethods = [
        'getOrder','setOrder',
        'getMaxOrder',
        'isFront','isBottom',
        'changeLayer'
    ];

    $.fn.layers = function(option) {
    	console.info("fn.layers start");
        var value,
            args = Array.prototype.slice.call(arguments, 1);

        this.each(function () {
            var $this = $(this), 
                data = $this.data('layers'),
                options = $.extend({}, Layers.DEFAULTS, $this.data(), typeof option == 'object' && option);

            if (!data) {
                $this.data('layers', (data = new Layers(this, options)));
            }

            if (typeof option === 'string') {
                if ($.inArray(option, allowedMethods) < 0) {
                    throw new Error("Unknown method: " + option);
                }

                //call method 
                value = data[option].apply(data, args);

            } else if (options.show) {
                data.show();
            }
        });
        console.info("fn.layers end");
        return typeof value === 'undefined' ? this : value;
    }

    $.fn.layers.Constructor = Layers;

}(jQuery);

var METADATA = {
    "ElementType": ["Container", "Device", "Chart", "Connection", "TextBox","MonitorTable","DriverRuntimeTable","AlarmTable","Image",'ImageUpload',"FileBox",'Canvas','CanvasGateWay','CanvasProceseAlter','CanvasQualified','Timer',"Position","BackPosition"],
    "Container": ["Background","Size","CoverType","BackPosition","Fileupload"],
    "Device": ["Size", "Position", "DisablePosition", "Background", "Color", "FontFamily", "FontSize","Properties","BackgroundType"],
    "Chart": ["Size", "Position","DisablePosition", "FontFamily", "FontSize","DataSource"],
    "Spc": ["Size", "Position","DisablePosition", "FontFamily", "FontSize","DataSource"],
    "SpcAnalysis": ["Size", "Position","DisablePosition", "FontFamily", "FontSize","DataSource"],
    "MonitorTable": ["Size", "Position","DisablePosition","FontFamily", "FontSize", "TableLineCount"],
    "DriverRuntimeTable": ["Size", "Position","DisablePosition","FontFamily", "FontSize", "TableLineCount"],
    "AlarmTable":["Size", "Position","DisablePosition","FontFamily", "FontSize", "TableLineCount"],
    "AlarmTableMT":["Size", "Position","DisablePosition","FontFamily", "FontSize", "TableLineCount"],
    "Connection": ["Color", "LineWidth"],
    "TextBox": ["Background", "Color", "Position", "DisablePosition","Size", "BorderColor","Border", "FontFamily", "FontSize", "Word", "WordLeval","WordValign", "ParamHeight","FontStyle","FontWeight","Radius"],
    "Image": ["Position","DisablePosition", "Size", "Border", "BorderColor"],
    "FileBox": ["Fileupload","BackPosition"],
    "ImageUpload": ["Position","DisablePosition", "Size","Border","ImgSrc"],
    "Timer":["Formatter","Color","Border","FontSize","DisablePosition"],
    "Canvas":["Position","Size","DisablePosition", "CanvasLineWidth","CanvasStrokeStyle","CanvasFillStyle","CanvasLineHeight"],
    "CanvasGateWay":["Position","Size","DisablePosition", "CanvasLineWidth","CanvasStrokeStyle","CanvasFillStyle","CanvasLineHeight", "Radius"],
    "CanvasProceseAlter":["Position","Size","DisablePosition", "CanvasLineWidth","CanvasStrokeStyle","CanvasFillStyle","CanvasLineHeight", "Radius"],
    "CanvasQualified":["Position","Size","DisablePosition", "CanvasLineWidth","CanvasStrokeStyle","CanvasFillStyle","CanvasLineHeight"],
    "BindTextBox": ["Background", "Color", "Position","DisablePosition", "Size", "FontFamily", "FontSize", "Border", "BorderColor"],
    "CanvasLineWidth":{
    	"title": "图形边框：",
        "identifier":"canvasLineWidth",
        "type":"text"
    },
    "CanvasStrokeStyle":{
    	"title": "线条颜色：",
        "identifier":"canvasStrokeStyle",
        "type":"colorpicker"
    },
    "CanvasFillStyle":{
    	"title": "填充颜色：",
        "identifier":"canvasFillStyle",
        "type":"colorpicker"
    },
    "Formatter":{
    	"title":"日期格式：",
    	"identifier":"timer",
    	"type":"select",
    	"options":[
    			{"text":"YYYY-MM-DD","value":"yyyy-MM-dd"},
    			{"text":"YYYY-MM","value":"yyyy-MM"},
    			{"text":"YYYY-MM-DD HH","value":"yyyy-MM-dd HH"},
    			{"text":"YYYY-MM-DD HH:MM:SS","value":"yyyy-MM-dd HH:mm:ss"},
    			{"text":"YYYYMMDDHHMMSS","value":"yyMMddHHmmss"}
    			]
    },
    "CoverType":{
    	"title":"背景平铺",
    	"identifier":"coverType",
        "type":"select",
        "options":[
                {"text":"平铺","value":"repeat"},
                {"text":"水平平铺","value":"repeat-x"},
                {"text":"垂直平铺","value":"repeat-y"},
                {"text":"填充","value":"cover"},
                {"text":"无","value":"no-repeat"},
                
          ]
    },
    "Background": {
        "title": "背景颜色：",
        "identifier":"background",
        "type":"colorpicker",
        
    },
    "Color": {
        "title": "字体颜色：",
        "identifier":"color",
        "type":"colorpicker"
    },
    "FontFamily": {
        "title": "字　　体：",
        "identifier":"fontfamily",
        "type":"select",
        "options":[
                   {"text":"宋体","value":"SimSun"},
                   {"text":"仿宋","value":"FangSong"},
                   {"text":"黑体","value":"SimHei"},
                   {"text":"微软雅黑","value":"Microsoft YaHei"},
                   {"text":"微软正黑体","value":"Microsoft JhengHei"},
                   {"text":"新宋体","value":"NSimSun"},
                   {"text":"新细明体","value":"PMingLiU"},
                   {"text":"细明体","value":"MingLiU"},
                   {"text":"标楷体","value":"DFKai-SB"},
                   {"text":"楷体","value":"KaiTi"},
                   {"text":"隶书","value":"LiSu"},
                   {"text":"幼圆","value":"YouYuan"},
                   {"text":"华文细黑","value":"STXihei"},
                   {"text":"华文楷体","value":"STKaiti"},
                   {"text":"华文宋体","value":"STSong"},
                   {"text":"华文中宋","value":"STZhongsong"},
                   {"text":"华文仿宋","value":"STFangsong"},
                   {"text":"方正舒体","value":"FZShuTi"},
                   {"text":"方正姚体","value":"FZYaoti"},
                   {"text":"华文彩云","value":"STCaiyun"},
                   {"text":"华文琥珀","value":"STHupo"},
                   {"text":"华文隶书","value":"STLiti"},
                   {"text":"华文行楷","value":"STXingkai"},
                   {"text":"华文新魏","value":"STXinwei"},
        {
            "text":"Impact\,Charcoal\,sans-serif",
            "value":"Impact\,Charcoal\,sans-serif"
        },{
            "text":"Arial\,Helvetica\,sans-serif",
            "value":"Arial\,Helvetica\,sans-serif"
        },{
            "text":"Arial Black\,Gadget\,sans-serif",
            "value":"Arial Black\,Gadget\,sans-serif"
        },
        /*新增字体样式2017-3-24*/
        {"text":"Arial\,Helmet\,Freesans\,sans-serif","value":"Arial\,Helmet\,Freesans\,sans-serif"},
        {"text":"'Arial Narrow'\,'Nimbus Sans L'\,sans-serif","value":"'Arial Narrow'\,'Nimbus Sans L'\,sans-serif"},
        {"text":"'Bookman Old Style'\,Bookman\,'URW Bookman L'\,serif","value":"'Bookman Old Style'\,Bookman\,'URW Bookman L'\,serif"},
        {"text":"'Century Gothic'\,Futura\,'URW Gothic L'\,sans-serif","value":"'Century Gothic'\,Futura\,'URW Gothic L'\,sans-serif"},
        {"text":"'Comic Sans MS'\,cursive","value":"'Comic Sans MS'\,cursive"},
        {"text":"'Courier New'\,Courier\,Freemono\,'Nimbus Mono L'\,monospace","value":"'Courier New'\,Courier\,Freemono\,'Nimbus Mono L'\,monospace"},
        {"text":"Constantina\,Georgia\,'Nimbus Roman No9 L'\,serif","value":"Constantina\,Georgia\,'Nimbus Roman No9 L'\,serif"},
        {"text":"Consolas\,'Lucida Console'\,'Bitstream VeraSans Mono'\,'DejaVu Sans Mono'\,monospace","value":"Consolas\,'Lucida Console'\,'Bitstream VeraSans Mono'\,'DejaVu Sans Mono'\,monospace"},
        {"text":"'Lucida Sans Unicode'\,'Lucida Grande'\,'Lucida Sans'\,'DejaVu Sans Condensed'\,sans-serif","value":"'Lucida Sans Unicode'\,'Lucida Grande'\,'Lucida Sans'\,'DejaVu Sans Condensed'\,sans-serif"},
        {"text":"Cambria\,'Palatino Linotype'\,'Book Antiqua'\,'URW Palladio L'\,serif","value":"Cambria\,'Palatino Linotype'\,'Book Antiqua'\,'URW Palladio L'\,serif"},
        {"text":"symbol\,'Standard Symbols L'","value":"symbol\,'Standard Symbols L'"},
        {"text":"Cambria\,'Times New Roman'\,'Nimbus Roman No9 L'\,'Freeserif'\,Times\,serif","value":"Cambria\,'Times New Roman'\,'Nimbus Roman No9 L'\,'Freeserif'\,Times\,serif"},
        {"text":"Verdana\,Geneva\,'Bitstream Vera Sans'\,'DejaVu Sans'\,sans-serif","value":"Verdana\,Geneva\,'Bitstream Vera Sans'\,'DejaVu Sans'\,sans-serif"}, 
        {"text":"'Helvetica Neue'\,Arial\,'Liberation Sans'\,FreeSans\,sans-serif","value":"'Helvetica Neue'\,Arial\,'Liberation Sans'\,FreeSans\,sans-serif"},
        {"text":"'Bookman Old Style'\, serif","value":"'Bookman Old Style'\, serif"},
        {"text":"'Comic Sans MS'\, cursive","value":"'Comic Sans MS'\, cursive"},
        {"text":"'Courier New'\, Courier\, monospace","value":"'Courier New'\, Courier\, monospace"},
        {"text":"Garamond\, serif","value":"Garamond\, serif"},
        {"text":"Georgia\, serif","value":"Georgia\, serif"},
        {"text":"'Lucida Console'\, Monaco\, monospace","value":"'Lucida Console'\, Monaco\, monospace"},
        {"text":"'Lucida Sans Unicode'\, 'Lucida Grande'\, sans-serif","value":"'Lucida Sans Unicode'\, 'Lucida Grande'\, sans-serif"},
        {"text":"'MS Sans Serif'\, Geneva\, sans-serif","value":"'MS Sans Serif'\, Geneva\, sans-serif"}, /* bitmap */
        {"text":"'MS Serif'\, 'New York'\, serif","value":"'MS Serif'\, 'New York'\, serif"}, /* bitmap */
        {"text":"'Palatino Linotype'\, 'Book Antiqua'\, Palatino\, serif","value":"'Palatino Linotype'\, 'Book Antiqua'\, Palatino\, serif"},
        {"text":"Symbol\, sans-serif","value":"Symbol\, sans-serif"},
        {"text":"Tahoma\, Geneva\, sans-serif","value":"Tahoma\, Geneva\, sans-serif"},
        {"text":"'Times New Roman'\, Times\, serif","value":"'Times New Roman'\, Times\, serif"},
        {"text":"'Trebuchet MS'\, Helvetica\, sans-serif","value":"'Trebuchet MS'\, Helvetica\, sans-serif"},
        {"text":"Verdana\, Geneva\, sans-serif","value":"Verdana\, Geneva\, sans-serif"},
        {"text":"Webdings\, sans-serif","value":"Webdings\, sans-serif"},
        {"text":"Wingdings\, 'Zapf Dingbats'\, sans-serif","value":"Wingdings\, 'Zapf Dingbats'\, sans-serif"},
        ]
    },
    "FontSize": {
        "title": "字体大小：",
        "identifier":"fontsize",
        "type":"text"
    },
    /*"Name": {
        "title": "名　　称：",
        "identifier":"name",
        "type":"text"
    },*/
   /* "Description": {
        "title": "描　　述：",
        "identifier":"description",
        "type":"textarea"
    },*/
    "DataSource": {
        "title": "名　　称：",
        "identifier":"datasource",
        "type":"datasource"
    },
    "Size": {
        "title": "尺　　寸：",
        "identifier":"size",
        "type":"doubletext",
        "name1":"宽",
        "name2":"高"
    },
    "Position": {
        "title": "位　　置：",
        "identifier":"position",
        "type":"doubletext",
        "name1":"X",
        "name2":"Y"
    },
    "BackPosition":{
    	"title":"背景位置： ",
    	"identifier":"backposition",
    	"type":"doubletext",
    	"name1":"X",
    	"name2":"Y"
    },
    "Word": {
        "title": "文　　字：",
        "identifier":"word",
        "type":"text"
    },
    "DisablePosition":{
   	 "title": "位置锁定：",
        "identifier":"disablePosition",
        "type":"select",
    	 "options":[
    	         {"text":"解锁","value":"absolute"}, 
      	         {"text":"锁定","value":"static"}, 
    ]},
    "WordLeval": {
    	"title": "文字水平对齐:",
    	"identifier":"wordLeval",
    	"type":"select",
    	"options":[
    	    {"text":"左对齐","value":"left"},
    	    {"text":"居中","value":"center"},
    	    {"text":"右对齐","value":"right"},
    ]},
 "WordValign": {
    	"title": "文字垂直对齐：",
    	"identifier" :"wordValign",
    	"type":"select",
    	"options":[
      	         {"text":"顶端对齐","value":"top"}, 
    	         {"text":"居中","value":"center"}, 
    	         {"text":"底部对齐","value":"bottom"},
    ]},
    "ParamHeight": {
    	"title": "段落行高：",
    	"identifier" :"paramHeight",
    	"type":"text",
    },
    "FontStyle": {
    	"title": "字体样式：",
    	"identifier" :"fontStyle",
    	"type":"select",
    	"options":[
      	         {"text":"正常","value":"normal"}, 
      	         {"text":"斜体","value":"italic"},
    ]},
    "FontWeight": {
    	"title": "字体加粗：",
    	"identifier" :"fontWeight",
    	"type":"select",
    	"options":[
      	         {"text":"正常","value":"normal"}, 
      	         {"text":"加粗","value":"bold"}, 
    ]},
    
    "CanvasLineHeight":{
    	"title": "段落边框：",
        "identifier":"canvasLineHeight",
        "type":"text"
    },
    "Border": {
        "title": "边　　框：",
        "identifier":"border",
        "type":"text"
    },
    "BorderColor": {
        "title": "边框颜色：",
        "identifier":"bordercolor",
        "type":"colorpicker"
    },
    "Properties": {
        "title": "可选属性：",
        "identifier":"properties",
        "type":"checkboxes"
    },
    "TableLineCount": {
        "title": "行数：",
        "identifier":"tableLineCount",
        "type":"text"
    },
    "ImgSrc": {
        "title": "图片上传：",
        "identifier":"src",
        "type":"file"
    },
    "Fileupload": {
        "title": "背景上传：",
        "identifier":"fileupload",
        "type":"file"
    },
    "BackgroundType": {
    	"title": "状态背景选择：",
    	"identifier":"backgroundType",
    	"type":"checkboxes"
    },
    "Radius": {
        "title": "Radius：",
        "identifier":"Radius",
        "type":"text"
    },
};

function clp(id) {
	$('#canvas').css({background:''});localStorage.removeItem('background');
	var fileBtnNm = "#" + id.id;
	$(fileBtnNm).click();
	$("#fileUrl").val($(fileBtnNm).val());
	return true;
}