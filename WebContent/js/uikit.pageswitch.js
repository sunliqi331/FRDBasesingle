(function($){
  var PageSwitch = function (element, options) {
    this.options             = options
    this.$element            = $(element)
    this.isShown             = null
  }

  PageSwitch.DEFAULTS = {
    show: true
  }

  PageSwitch.prototype.toggle = function () {
    return this.isShown ? this.hide() : this.show()
  }

  PageSwitch.prototype.show = function () {
    var that = this
    var e    = $.Event('show.pageswitch')

    this.$element.trigger(e)

    if (this.isShown || e.isDefaultPrevented()) return

    this.isShown = true

    this.$element.on('click.dismiss.pageswitch', '[data-dismiss="pageswitch"]', $.proxy(this.destroy, this))

    this.$element.show().prev().hide()

    $.pageswitch.currentEle = this.$element

  }

  PageSwitch.prototype.hide = function (e) {
    if (e) e.preventDefault()

    e = $.Event('hide.pageswitch')

    this.$element.trigger(e)

    if (!this.isShown || e.isDefaultPrevented()) return

    this.isShown = false

    $(document).off('focusin.pageswitch')

    this.$element
      .removeClass('in')
      .off('click.dismiss.pageswitch')
      .off('mouseup.dismiss.pageswitch')

    this.hidePageSwitch()

    $.pageswitch.currentEle = null
  }

  PageSwitch.prototype.destroy = function () {
    var that = this
    this.hide()
    this.$element.removeData('pageswitch')
    this.$element.remove()
    this.$element = null
    this.options = null
  }

  PageSwitch.prototype.hidePageSwitch = function () {
    var that = this
    this.$element.hide().prev().show()
  }

  // PAGESWITCH PLUGIN DEFINITION
  // =======================

  function Plugin(option) {
    return this.each(function () {
      var $this   = $(this)
      var data    = $this.data('pageswitch')
      var options = $.extend({}, PageSwitch.DEFAULTS, $this.data(), typeof option == 'object' && option)

      if (!data) $this.data('pageswitch', (data = new PageSwitch(this, options)))
      if (typeof option == 'string') data[option]()
      else if (options.show) data.show()
    })
  }

  var old = $.fn.pageswitch

  $.fn.pageswitch             = Plugin
  $.fn.pageswitch.Constructor = PageSwitch


  // PAGESWITCH NO CONFLICT
  // =================

  $.fn.pageswitch.noConflict = function () {
    $.fn.pageswitch = old
    return this
  }

  $.pageswitch = {
    currentEle: null
  }
})(jQuery);


