(function($){
	$.monitorSocket = {
			_op : {
				subscribeUrl:"",
				connectUrl:"",
				sendUrl:"",
				data:{},
				subscribeCallback:function(data){
					
				},
				connectCallback:function(frame){
					
				}
			},
			stompClient : null,
	        connect:function(opt){
	        	 var $op = $.extend({},this._op,opt);
				 var socket = new SockJS($op.connectUrl);
				 // destory Storm链接，并重新生成一个。
		         stompClient = Stomp.over(socket);
		         stompClient.connect({aa:"bb"}, $op.connectCallback);
		         return stompClient;
			},
			connectRegular : function(opt){
				var $op = $.extend({},this._op,opt);
				stompClient = Stomp.client($op.connectUrl);
				stompClient.connect({}, $op.connectCallback);
		         return stompClient;
			},
	        disconnect : function(){
	        	if(stompClient) 
	        	      stompClient.disconnect();
	        },
	        getCurrentClient : function(){
	        	return stompClient;
	        },
	        send : function(opt){
	        	var $op = $.extend({},this._op,opt);
	        	stompClient.send($op.sendUrl, {}, JSON.stringify($op.data));
	        },
	        subscribe : function(opt){
	        	var $op = $.extend({},this._op,opt);
	        	stompClient.subscribe($op.subscribeUrl, $op.subscribeCallback);
	        }
	}
	
})(jQuery);
