(function($){
  $.fn.extend({
     // custom Radio and Check box
    checkboxRadioReplace: function() {
      var $wrapper = '<div class="cbr-replaced"><div class="cbr-input"></div><div class="cbr-state"></div></div>';
      return this.each(function(){
        var $input = $(this),
          is_radio = $input.is(':radio'),
          is_checkbox = $input.is(':checkbox'),
          is_disabled = $input.is(':disabled'),
          styles = ['primary', 'secondary', 'success', 'danger', 'warning', 'info', 'purple', 'blue', 'red', 'gray', 'pink', 'yellow', 'orange', 'turquoise'];

        if( ! is_radio && ! is_checkbox)
            return;
        $input.after( $wrapper );
        $input.addClass('cbr-done');
        var $wrp = $input.next();
        $wrp.find('.cbr-input').append( $input );
        if(is_radio)
          $wrp.addClass('cbr-radio');
        if(is_checkbox)
          $wrp.addClass('cbr-checkbox');
        if(is_disabled)
          $wrp.addClass('cbr-disabled');
        if($input.is(':checked'))
          $wrp.addClass('cbr-checked');
        // Style apply
        $.each(styles, function(key, val)
        {
          var cbr_class = 'cbr-' + val;
          
          if( $input.hasClass(cbr_class))
          {
            $wrp.addClass(cbr_class);
            $input.removeClass(cbr_class);
          }
        });
        // Events
        $wrp.on('click', function(ev) {
          if(is_radio && $input.prop('checked') || $wrp.parent().is('label'))
            return;
          
          if($(ev.target).is($input) == false) {
            $input.prop('checked', ! $input.is(':checked'));
            $input.trigger('change');
          }
        });
        $input.on('change', function(ev) {   
          $wrp.removeClass('cbr-checked');
          
          if($input.is(':checked'))
            $wrp.addClass('cbr-checked');
            
          $("input.cbr-done").reCheck();
        });
      });
    },
    reCheck: function() {
      return this.each(function(){
        var $input = $(this),
          is_radio = $input.is(':radio'),
          is_checkbox = $input.is(':checkbox'),
          is_disabled = $input.is(':disabled'),
          $wrp = $input.closest('.cbr-replaced');
        
        if(is_disabled)
          $wrp.addClass('cbr-disabled');
        
        if(is_radio && ! $input.prop('checked') && $wrp.hasClass('cbr-checked')) {
          $wrp.removeClass('cbr-checked');
        }
      });
    },
    checkboxCtrl: function(parent){
      return this.each(function(){
        var $trigger = $(this),
          is_button = $trigger.is(':button');
        var $wrp = is_button ? $trigger : $trigger.closest('.cbr-replaced');
        $wrp.on('click',function(ev){
          var group = $trigger.attr("group");
          if (!is_button) {
            var type = $trigger.is(":checked") ? "all" : "none";
            if (group) $.checkbox.select(group, type, parent);
          } else {
            if (group) $.checkbox.select(group, $trigger.attr("selectType") || "all", parent);
          }
        });
      });
    }
  });
  
  $.checkbox = {
    selectAll: function(_name, _parent){
      this.select(_name, "all", _parent);
    },
    unSelectAll: function(_name, _parent){
      this.select(_name, "none", _parent);
    },
    selectInvert: function(_name, _parent){
      this.select(_name, "invert", _parent);
    },
    select: function(_name, _type, _parent){
      $parent = $(_parent || document);
      $checkboxLi = $parent.find(":checkbox[name='"+_name+"']");
      switch(_type){
        case "invert":
          $checkboxLi.each(function(){
            $checkbox = $(this);
            $checkbox.prop('checked', !$checkbox.is(":checked"));
            $checkbox.trigger('change');
          });
          break;
        case "none":
          $checkboxLi.prop('checked', false);
          $checkboxLi.trigger('change');
          break;
        default:
          $checkboxLi.prop('checked', true);
          $checkboxLi.trigger('change');
          break;
      }
    },
    initCheckbox: function(_parent){
      var $parent = $(_parent || document);
      $('input[type="checkbox"].cbr, input[type="radio"].cbr', $parent).filter(':not(.cbr-done)').checkboxRadioReplace();
      $(".checkboxCtrl", $parent).checkboxCtrl();
    }
  };

  $(function () {
    $.checkbox.initCheckbox();
    });
})(jQuery);
