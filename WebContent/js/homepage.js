$(document).ready(function($){
	$('#sangarBanner').hide();
	$('#sangarBanner').sangarSlider({
		timer : true, // true or false to have the timer
		width : 1436, // slideshow width
		height : 411 // slideshow height
	});
	$(".sangar-timer").hide();
//	$('#sangarBanner1').sangarSlider({
//		timer : false, // true or false to have the timer
//		width : 1436, // slideshow width
//		height : 250, // slideshow height
//		directionalNav :'autohide',
//		directionalNavShowOpacity :0, // from 0 to 1
//	    directionalNavHideOpacity : 0, // from 0 to 1
//	});
	//alert($(document.body).width());
	if($(document.body).width()<1436){
		$("#sangarBanner1 img").width($(document.body).width()*0.95);
		}
		else{
		$("#sangarBanner1 img").width(1436);	
		}
	$('#sangarBanner').show();
}(jQuery));
$(window).resize(function(){
	if($(document.body).width()<1436){
		$("#sangarBanner1 img").width($(document.body).width()*0.95);
		}
		else{
		$("#sangarBanner1 img").width(1436);	
		}
	
});
