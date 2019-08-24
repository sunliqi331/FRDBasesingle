(function($){
//  var g_query;
  $.table = {
    _op:{
      pagination: 'true',
      uniqueId: 'id',
      locale: 'zh-CN',
      sidePagination: 'server',
      pageSize: 10,
      cache:false,
      clickToSelect: true,
      columns:[{  
          field: 'Number',//可不加  
          formatter: function (value, row, index) {
        	  var table_ = $.table.getCurrent() || $("#table");
        	  var pageNumber = table_.bootstrapTable('getOptions').pageNumber;
    	  	  var pageSize = table_.bootstrapTable('getOptions').pageSize;
    	  	  return (pageNumber-1) * pageSize+index+1;
              //return index+1;  
          },
          width:'22px'
      }
      ],
      responseHandler: function(res) {
        res.total = res.page.totalCount;
        $.table.setOriData(res);
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
    },
    _current:null,
    setOriData: function(data) {
      data && this._current && this._current.data('table.data',data);
    },
    getCurrent:function(){
      return this._current;
    },
    getSelectedId: function() {
      var ids = [];
      if (this._current) {
        var selections = this._current.bootstrapTable('getSelections');
        if (selections.length) {
          $.each(selections, function(){
            ids.push(this.id);
          })
        }
      }
      return ids;
    },
    setCurrent:function(tid){
      if (tid) {
        var $table = $('#' + tid);
        this._current = $table.length ? $table : null;
      }
    },
    
    init: function(tid,op,callback) {
      g_query = '';
      var $table = $('#' + tid);
      if ($table.length) {
        op.url = $table.attr('data-url');
        op.dataField = $table.attr('data-field');
        $table.bootstrapTable($.extend({},this._op, op));
        callback && $table.off('post-body.bs.table').on('post-body.bs.table', function () {
          callback($(this).data('table.data'));
        });
        this._current = $table;
      }
    },
    refreshCurrent: function(url,query,callback) {
      if (this._current) {
        var op = {silent: true};
        if (url) {
          op.url = url;
          op.silent = false;
        }
        query && (op.query = query);
        g_query = query;
        callback && this._current.off('post-body.bs.table').on('post-body.bs.table', function () {
          callback($(this).data('table.data'));
        });
        this._current.bootstrapTable('refresh', op);
      }
    },
    getCurrentPageData: function(){
      if (this._current)
        return this._current.bootstrapTable('getData', true);
    },
    getOption: function(){
    	return this._current.bootstrapTable('getOptions');
    }
  };
})(jQuery);