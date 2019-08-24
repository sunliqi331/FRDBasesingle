jQuery(document).ready(function($){

//    login

  $('.btn-submit').click(function(e){
            $('.input-username').addClass('animated fadeOutLeft')
            $('.input-password').addClass('animated fadeOutRight')
            $('.captcha').addClass('animated fadeOutLeft')
            $('.rememberList').addClass('animated fadeOutRight')
            $('.helpList').addClass('animated fadeOutLeft')
            $('.btn-submit').addClass('animated fadeOutUp')
            setTimeout(function () {
                      $('.avatar').addClass('avatar-top');
                      //$('.submit').html('<i class="fa fa-spinner fa-spin text-white"></i>');
                     // $('.btn-login').html('<div class="progress"><div class="progress-bar progress-bar-success" aria-valuetransitiongoal="100"></div></div>');
                      //$('.progress .progress-bar').progressbar(); 
              },
          800);

           /* setTimeout(function () {
                  window.location.href = 'index.html#redirect'; 

              },*/
          /*1500);*/

          });  

});