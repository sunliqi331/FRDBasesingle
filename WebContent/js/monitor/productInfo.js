    //产品，工序，工序属性信息显示
    function productInfo(){
        var TextBind_canvas1 = $("canvas[category]");//产品，工序，工序属性
        if(TextBind_canvas1.hasOwnProperty("context")){
            var productInfo = [];
            var productprocedureid;
            var procedurepropertyid;
            $.each(TextBind_canvas1,function(idx,can_){
                var arcId = $(can_).attr("id");
                var dataType = $("#" + arcId).prev().attr("data-type");
                // var category = $(can_).attr("category");
                var productid = $(can_).attr("data-productid");
                if("ArcVirtual" != dataType && "Qualified" != dataType) {
                    return true;
                }

                if("Qualified" != dataType){
                    productprocedureid = $(can_).attr("data-productprocedureid");
                    procedurepropertyid = $(can_).attr("data-procedurepropertyid");
                }

                var Pojo = {};
                Pojo.dataType = dataType;
                Pojo.productid = productid;
                Pojo.productprocedureid = productprocedureid;
                Pojo.procedurepropertyid = procedurepropertyid;
                productInfo.push(Pojo);
            });

            if(productInfo.length == 0){
                return true;
            }

            $.ajax({
                type : "POST",
                url : contextPath + "/MesAlarmShow/getProductInfo",
                data : "list="+JSON.stringify(productInfo),
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

                            $.each(TextBind_canvas1,function(idx,can_){
                                var arcId = $(can_).attr("id");
                                // console.log("arcid:" + arcId);
                                var dataType = $("#" + arcId).prev().attr("data-type");
                                // console.log("dataType:" + dataType);
                                var productid = $(can_).attr("data-productid");


                                if("ArcVirtual" != dataType && "Qualified" != dataType) {
                                    return true;
                                }
                                if("Qualified"==dataType){
                                    if(obj.productid == productid) {
                                        var c = document.getElementById(arcId);
                                        var ctx=c.getContext("2d");
                                        if(obj.qualified!=null && "1" == obj.qualified) {
                                            ctx.fillStyle= "#0aee4e";
                                        }else if(obj.qualified!=null && "0" == obj.qualified) {
                                            ctx.fillStyle= "#f00";
                                        }
                                        ctx.fill();
                                    }
                                }else{
                                    // if(obj.driverid==driverid){
                                    //     var c = document.getElementById(arcId);
                                    //     var ctx=c.getContext("2d");
                                    //     if("0" == obj.macStatsu) {
                                    //         ctx.fillStyle= "#0aee4e";
                                    //     }else if("1" == obj.macStatsu){
                                    //         ctx.fillStyle= "#0aee4e";
                                    //     }else {
                                    //         ctx.fillStyle= "#f00";
                                    //     }
                                    //     ctx.fill();
                                    // }
                                }

                            });

                        });
                    }
                },
                error:function(jqXHR, textStatus, errorThrown){console.log(textStatus);}
            });

        }

    }