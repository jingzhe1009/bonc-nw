var colorList;
var count;

$(function(){
  count= 6;
  loadFlow(count);
  checkColor(colorList);
    
})

/*function startFlow(i){
	debugger;
  var count=6;
  index = i;
  loadFlow(count);
  checkColor(colorList);
}*/
function initFlow(){
	for(var index=2;index<=6;index++){
		$(".for" + String.fromCharCode(index + 64)).removeClass("for-cur");
	}
    loadFlowDiv(1);
    checkColor("default");
    checkBtn(1,6);
    $("#filename").val('');
	$("#resultText").text('');
	$("#batchNo").val('');
}

//页面跳转
function methodBtn(index, method, end) {
	debugger;
    var fFor;
    if (end != true) {
        if (method == "back") {
            if (index == 1) {
                fFor = ".for" + String.fromCharCode(index + 65);
            } else {
                fFor = ".for" + String.fromCharCode(index + 64);
            }
            $(fFor).removeClass("for-cur");
            loadFlowDiv(index-1);
            checkColor("default");
        } else if (method == "forward") {
            fFor = ".for" + String.fromCharCode(index + 65);//65 A
            $(fFor).addClass("for-cur");
            loadFlowDiv(index+1);
            checkColor(colorList);
        }
    } else if (end == true) {
       
    }

}
//确定流程颜色状态
function checkColor(color) {
	
    if (color != "default") {
        $(".flowList.for-cur").css({ "border": "2px solid #1ABB9C"});
        $(".flowList.for-cur,.flowListBox .for-cur em").css({ "background-color": "#1ABB9C"});
        $(".flowListBox .for-cur em").css({ "border": "0px none #000" }); 
    } else {
        $this = $('.flowList:not(.for-cur)');
        $this.css({ "border": "2px solid #ccc", "background-color": "#ccc" });
        $this.children("em").css({ "background-color": "#ccc" }); 
    }
    /*让当前选中步骤变为深蓝色*/
	 var obj=$('.for-cur:last');
	 obj.css({ "border": "2px solid #34495e", "background-color": "#34495e" });
	 obj.children("em").css({ "background-color": "#34495e" }); 
}
//确定步骤的宽度占比
function fixWidth(count) {
    var part = parseInt(100 / count) + "%";
    $(".flowListBox .flowList").css("width", part);
}
//加载步骤
function loadFlow(count){
  var flowFor;
  var flowVar="";
  for(var i=1;i<=count;i++){
    flowFor="for"+String.fromCharCode(i+64);
    console.log('count='+count);
    if(i==1){
      flowVar += "<div class='flowList for-cur "+flowFor +"' style='position:relative'>\n";
      flowVar += "	<em style='position:absolute;left:35%'>"+i+"</em><br/><strong style='position:absolute;left:40%'>配置说明</strong>\n";
      flowVar += "</div>\n";
    }else if(i==2){
      flowVar += "<div class='flowList "+flowFor +"' style='position:relative'>\n";
      flowVar += "	<em style='position:absolute;left:35%'>"+i+"</em><br/><strong style='position:absolute;left:40%'>接口导入</strong>\n";
      flowVar += "</div>\n";
    }else if(i==3){
      flowVar += "<div class='flowList "+flowFor +"' style='position:relative'>\n";
      flowVar += "	<em style='position:absolute;left:35%'>"+i+"</em><br/><strong style='position:absolute;left:40%'>信息核对</strong>\n";
      flowVar += "</div>\n";
    }else if(i==4){
      flowVar += "<div class='flowList "+flowFor +"' style='position:relative'>\n";
      flowVar += "	<em style='position:absolute;left:35%'>"+i+"</em><br/><strong style='position:absolute;left:40%'>信息确认</strong>\n";
      flowVar += "</div>\n";
    }else if(i==5){
      flowVar += "<div class='flowList "+flowFor +"' style='position:relative'>\n";
      flowVar += "	<em style='position:absolute;left:35%'>"+i+"</em><br/><strong style='position:absolute;left:40%'>物化建模</strong>\n";
      flowVar += "</div>\n";
    }else if(i==6){
      flowVar += "<div class='flowList "+flowFor +"' style='position:relative'>\n";
      flowVar += "	<em style='position:absolute;left:35%'>"+i+"</em><br/><strong style='position:absolute;left:40%'>信息下载</strong>\n";
      flowVar += "</div>\n";
    }

  }
  $(".flowListBox").html(flowVar);
  fixWidth(count);
  loadFlowDiv(1);
  checkBtn(1,count);
}
//加载内容详情
function loadFlowDiv(index){ 
	console.log(index);
 /* var strVar = "";
 if(index==1){strVar="aaaaa<br>aaaaaaaaa<br>aaaaaaaaa<br>aaaaaaaaa<br>aaaaaaaaa<br>aaaaaaaaa<br>aaaaaaaaa<br>aaaaaaaaa<br>aaaaaaaaa"}
 if(index==2){strVar="bb<br>bbbbbb<br>22222222<br>22222222222<br>222222222<br>222222<br>2222222<br>222222<br>222222"}
 if(index==3){strVar="cc"}
 if(index==4){strVar="dd"}
 $("#iList").html(strVar);*/
 if(index==1){$("#contX").removeClass("contentList");$("#contX").siblings().addClass("contentList")}
 if(index==2){$("#contA").removeClass("contentList");$("#contA").siblings().addClass("contentList")}
 if(index==3){$("#contB").removeClass("contentList");$("#contB").siblings().addClass("contentList")}
 if(index==4){$("#contC").removeClass("contentList");$("#contC").siblings().addClass("contentList")}
 if(index==5){$("#contD").removeClass("contentList");$("#contD").siblings().addClass("contentList")}
 if(index==6){$("#contE").removeClass("contentList");$("#contE").siblings().addClass("contentList")}


}
//上一步下一步按钮点击事件
var maxstep=1;
function checkBtn(index, count) {
	 /*默认进来隐藏上一步按钮*/
	$("#btnBack").hide();
	$("#btnok").hide();
	$("#btnNext").unbind('click');
	$("#btnBack").unbind('click');
	/*下一步点击事件*/ 
    $("#btnNext").click(function () {
    	debugger;
    	var idx = localStorage.getItem("idx");
		if(index==2){
			debugger;
			if($("#batchNo").val()!=''&&$("#filename").val()!=''&&$("#resultText").text()!=''){
			}else{
				zUI.dialog.alert('<pre>请上传文件</pre>');
				return;
			}
		}
		//信息确认-下一步
		if(index==4){
			var dataTable=$('#compareTable').DataTable();
	    	var info=dataTable.page.info();
	    	var dataRows =info.recordsTotal;
	    	if(dataRows==0){
	    		zUI.dialog.alert('<pre>接口没有变化,不会生成新版本</pre>');
	    		//dataModelModel.init(localStorage.getItem("idx"));
	    		return;
	    	}
			compareModel.importInfo();
		}
		
        methodBtn(index++, 'forward', false);

		if(index>maxstep){
			maxstep=index;
		}
        if (index != 1) {
			/*非第一步的时候，显示上一步*/

			//接口导入-下一步
			if(index==3){
				infoModel.tmpInfoCheck();
			}
			//信息确认
			if(index==4){
				compareModel.init(idx);
			}
			
			
			if(index==6){
				console.log('建模');
				dataModelModel.insertDb();
			}
			/*else{
				$("#btnBack").addClass("disabled");
				$("#btnBack").removeClass("disabled");
			}*/

			$("#btnBack").show();
        }
        if (index >= count) {  
			/*到最后一步时 去掉下一步 显示上一步和完成*/
            $("#btnNext").hide();
			$("#btnok").show();
        }
        refreshBack(index);
    });
	/*上一步点击事件*/
    $("#btnBack").click(function () {
    	debugger;
		$("#btnok").hide();
        if (refreshBack(index) > 1) {
            methodBtn(index--, 'back', false);
			 $("#btnNext").show();
            if (index == 1) {
				/*如果当前为第一步 则给上一步添加disabled属性*/
                //$("#btnBack").addClass("disabled");
                $("#btnBack").hide();
        		$("#filename").val('');
        		$("#resultText").text('');
        		$("#batchNo").val('');
            } 
            if(index==5){
				//$("#btnBack").addClass("disabled");
			}
			/* for(var num=1;num<=maxstep;num++){
				 if(num==maxstep+1){
					  $(".flowList.for-cur").css({ "border": "2px solid #1ABB9C"});
        			  $(".flowList.for-cur,.flowListBox .for-cur em").css({ "background-color": "#1ABB9C"});
					 }
				 }*/
        }
    });
}
/*上一步*/
function refreshBack(index) {
    return index;

}
