    // 网关MAC监控
    function MacStatusMonitor(){
        var TextBind_canvas = $("canvas[data-mac]");
        var macS = [];
        var driverid;
        var dataType;
        if(TextBind_canvas.hasOwnProperty("context")){
            $.each(TextBind_canvas,function(idx,can_){
                var arcId = $(can_).attr("id");
                driverid = $(can_).attr("data-driverid");
                //console.error("arcid:" + arcId);
                dataType = $("#" + arcId).prev().attr("data-type");
                //console.error("dataType:" + dataType);
                if("Arc" != dataType && "ArcReal" != dataType) {
                    return true;
                }
                var mac = $(can_).attr("data-mac");
                // console.error("mac:" + mac);
                var Pojo = {};
                Pojo.driverid = driverid;
                Pojo.dataType = dataType;
                Pojo.mac = mac;
                macS.push(Pojo);
            });
            if(macS.length == 0){
                return true;
            }
            $.ajax({
                type : "POST",
                url : contextPath + "/MesAlarmShow/getGateWayConnectionMonitor",
                data : "list="+JSON.stringify(macS),
                // data: {"macString":JSON.stringify(macS)},
                dataType : "json",
                success : function(data) {
                     if(null!=data){
                         if(data.length==0){
                             return ;
                         }
                         // console.log("回调成功。")
                         $.each(data,function(index,obj){
                            // console.log("obj.mac:" + obj.mac);
                            // console.log("obj.macStatsu:" + obj.macStatsu);

                            $.each(TextBind_canvas,function(idx,can_){
                                var arcId = $(can_).attr("id");
                                // console.log("arcid:" + arcId);
                                var dataType = $("#" + arcId).prev().attr("data-type");
                                // console.log("dataType:" + dataType);
                                var driverid = $(can_).attr("data-driverid");

                                if("Arc" != dataType && "ArcReal" != dataType) {
                                    return true;
                                }
                                if("ArcReal"==dataType){
                                    var mac = $(can_).attr("data-mac");
                                    if(obj.mac == mac) {
                                        var c = document.getElementById(arcId);
                                        var ctx=c.getContext("2d");
                                        if("0" == obj.macStatsu) {
                                            ctx.fillStyle= "#0aee4e";
                                        } else {
                                            ctx.fillStyle= "#f00";
                                        }

                                        ctx.fill();
                                    }
                                }else{
                                    if(obj.driverid==driverid){
                                        var c = document.getElementById(arcId);
                                        var ctx=c.getContext("2d");
                                        if("0" == obj.macStatsu) {
                                            ctx.fillStyle= "#0aee4e";
                                        }else if("1" == obj.macStatsu){
                                            ctx.fillStyle= "#0aee4e";
                                        }else {
                                            ctx.fillStyle= "#f00";
                                        }
                                        ctx.fill();
                                    }
                                }

                            });

                         });
                     }
                },
                error:function(jqXHR, textStatus, errorThrown){console.log(textStatus);}
         });
        }

    }