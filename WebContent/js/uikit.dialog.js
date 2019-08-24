(function($){
  $.pdialog = {
    _current:[],
    //打开一个层
    open:function(dlgid, op) {
      var $body = $("body");
      var dialog = $body.data(dlgid);
      var $existDialog = $body.children('.modal.in');
      
      //重复打开一个层
      if(dialog && dialog.is(":hidden") && !op.destroyOnClose) {
        dialog.modal('show');
      } else { //打开一个全新的层
        $body.append($('#dialogTemp').html());
        dialog = $body.children('.modal:last-child');
        dialog.data("id",dlgid);
        dialog.data("url",op.url);
        dialog.data("params",JSON.stringify(op.params) || "{}");
        dialog.data("destroyOnClose",op.destroyOnClose);
        dialog.find(".modal-title").html(op.title);

        $body.data(dlgid, dialog);

        //load data
        var $modalBody = $(".modal-body", dialog);
        var that = this;
        $modalBody.loadUrl(op.url, typeof(op.data) == "object" ? op.data : {} , function(){
          dialog.modal();
          dialog.on('hidden.bs.modal', function (e) {
              // only handle the case of two dialog: remove class of parent dialog
              $body.children('.submodal-open').removeClass('submodal-open');
              var _dialog = e.target;
              var $this = $(this);
              if(op.close && $.isFunction(op.close)) {
                var param = dialog.data("params");
                if(param && param != ""){
                  param = DWZ.jsonEval(param);
                  op.close(param);
                } else {
                  op.close();
                }
              }
              
              that._current.pop();
              if ($this.data('destroyOnClose')) {
            	  that.destroy($this);
              }
            });
          dialog.on('shown.bs.modal', function () {
        	  if($('.chosen-select', this).length > 0){
        		  $('.chosen-select', this).chosen('destroy').chosen({search_contains:true});
        	  }
        	});
        });
       
         
      }
      if ($existDialog.length) { // 存在一个已经打开的dialog的情况
        $existDialog.addClass("submodal-open");
      } else {
        this._current = [];
      }
      this._current.push(dialog);
    },
    close:function(dialog) {
      if (dialog) {
        return dialog.modal('hide');
      }
    },
    closeCurrent:function() {
      var cur = this.getUpper();
      return this.close(cur);
    },
    destroy: function(dialog) {
      if (dialog && dialog.data('id')) {
        $('body').removeData(dialog.data('id'));
        dialog.remove();
      }
    },
    destroyCurrent: function() {
      var cur = this.closeCurrent();
      cur && cur.data('destroyOnClose', true);
    },
    getUpper:function() {
      var length = this._current.length;
      if (length > 0) {
        return this._current[length - 1];
      }
    },
    checkTimeout:function(){
      var $conetnt = $(".modal-body", $.pdialog._current);
      var json = DWZ.jsonEval($conetnt.html());
      if (json && json.statusCode == DWZ.statusCode.timeout) this.destroyCurrent();
    },
    forbidden:function(){
      var $conetnt = $(".modal-body", $.pdialog._current);
      var json = DWZ.jsonEval($conetnt.html());
      if (json && json.statusCode == DWZ.statusCode.forbidden) this.destroyCurrent();
    }
  };
})(jQuery);