var pconfig={
  startx:0,
  starty:0,
  beginx:0,
  beginy:0,
  endx:0,
  endy:0,
  divId:""
  };
function jswh(){
 if(pconfig.divId!=""){
  var w=Math.abs(parseInt(pconfig.endx,10)-parseInt(pconfig.beginx,10));
  var h=Math.abs(parseInt(pconfig.endy,10)-parseInt(pconfig.beginy,10));
  $("#"+pconfig.divId).css({"width":w+"px","height":h+"px","top":pconfig.starty,"left":pconfig.startx}); 
  $("#showxy").val(w);
 }
}
 $(function(){ 
 $(document).mousedown(function(event){ 
  pconfig.startx=event.pageX;
  pconfig.starty=event.pageY;
  pconfig.beginx=event.pageX;
  pconfig.beginy=event.pageY;
  pconfig.divId="newlinediv";
  if($("#"+pconfig.divId).size()<=0){
   $("<div class='line' id='newlinediv'></div>").appendTo(document.body);
  }else{
   $("#"+pconfig.divId).hide();
  }
  $(document).bind("mousemove",function(event){
        $("#"+pconfig.divId).show();
   pconfig.endx=event.pageX;
   pconfig.endy=event.pageY;
   if(pconfig.endx<pconfig.beginx){
    pconfig.startx=pconfig.endx;
   }
   if(pconfig.endy<pconfig.beginy){
    pconfig.starty=pconfig.endy;
   } 
   jswh();
  });
  $(document).bind("mouseup",function(){
   $(document).unbind("mousemove");
  })
 });
 })