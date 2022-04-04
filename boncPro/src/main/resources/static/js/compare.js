/**
 * 接口历史列表操作
 */
var compareModel = {
	//初始页面内容显示
	init: function(idx){
		debugger;
		var obj = {}
		obj['dataSrcAbbr'] = idx;
		obj['batchNo'] = localStorage.getItem("batchNo");
		initCompareTable(obj);
		compareModel.setIndexParam(idx);
		//按条件查询用
	},
    //查看版本
    version: function(dataSrcAbbr,dataInterfaceNo,dataInterfaceName){
    	$('#versionAlert').modal({'show': 'center', "backdrop": "static"});
		var obj ={};
		obj['dataSrcAbbr']=dataSrcAbbr;
		obj['dataInterfaceNo']=dataInterfaceNo;
		obj['dataInterfaceName']=dataInterfaceName;
		initVersionTable(obj);
    },
    //查看详情
    detail: function(dataSrcAbbr,dataInterfaceNo,dataInterfaceName){
    	$('#detailAlert').modal({'show': 'center', "backdrop": "static"});
    	detailModel.init(dataSrcAbbr,dataInterfaceNo,dataInterfaceName);
    },
    importInfo:function (){
		var param = {};
		param.dataSrcAbbr=localStorage.getItem("idx");
		param.batchNo = localStorage.getItem("batchNo");
		param.needVrsnNbr=localStorage.getItem("batchNo");
		var json = JSON.stringify(param);
		$('#loadAlert').modal({'show': 'center', "backdrop": "static"});
		$.ajax({
			url:"/interface/saveAll",
			type:"post",
			data:json,
			contentType:"application/json;charset=UTF-8",
			success:function(data){
				$('#loadAlert').modal('hide');
				console.log(data);
				zUI.dialog.alert('<pre>'+data.msgData+'</pre>');
				dataModelModel.init(localStorage.getItem("idx"));
			}
		})
	},
	getData:function (row,data){
		if(data==null)
			return '-';
		console.log(row.red);
		console.log(data);
		var red = row.red;
		if(red==null||red==''){
			 return data;
		}
		if(red.indexOf("'"+data+"'")!=-1){
			return '<font color="red">'+data+'</font>';
		}
		return data;
	},
	setIndexParam:function(idx){
		var param = {};
		param.dataSrcAbbr = idx;
		param.batchNo = localStorage.getItem("batchNo");
		var json = JSON.stringify(param);
		console.log(json);
		$.ajax({
			url:"/interface/queryCurrentNum",
			type:"POST",
			data:json,
			contentType:"application/json;charset=UTF-8",
			success: function(result){
				console.log(result);
				if(result.state=='success'){
					$("#tmp").text(result.tmp);
					$("#compare_intUpdateNum").text(result.intUpdateNum);
					$("#compare_intInsertNum").text(result.intInsertNum);
					$("#compare_colUpdateNum").text(result.colUpdateNum);
					$("#compare_colInsertNum").text(result.colInsertNum);
				}else{
					$("#compare_intUpdateNum").text('0');
					$("#compare_intInsertNum").text('0');
					$("#compare_colUpdateNum").text('0');
					$("#compare_colInsertNum").text('0');
				}
			}
		});
	},
}


/**
 * 初始化 接口历史列表
 */
function initCompareTable(obj) {
	
    $('#compareTable').width('100%').dataTable({
    	//默认搜索组件
        "searching": true,
        //排序功能
        "ordering": false,
        "destroy": true,
        // 自动列宽
        "autoWidth": false,
        //滚动条
        "scrollX":true,
        //控制每页显示条数
        "lengthChange": false,
        "pagingType": "full_numbers",
        //翻页功能
        "paging": true,
        //控制总数显示
        "info": true,
        //列表的过滤,搜索和排序信息会传递到Server端进行处理
        "serverSide": false,
        "pageLength": 100,
        "columns": [
        	/*{"title":"操作" ,"data": null,"render": function(data, type, row) {
            	var html = '<div>';
            		html += '<span onclick="compareModel.detail(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\',\''+row.dataInterfaceName+'\');" class="btn-sm cm-tblA">字段</span>';
            		html += '</div>';
				return html;
			}},*/
			{"title": "版本", "data": "flag","render":function(data,type,row){
				if(data=='0'){
					return '上一版本';
				}else{
					return '本次导入版本';
				}
            }},
			{"title": "状态", "data": "flag","render":function(data,type,row){
				if(data=='2'){
					return '<font color="red">修改</font>';
				}else if(data=='3'){
					return '<font color="blue">新增</font>';
				}else if(data=='1'){
					return '无变化';
				}else if(data=='4'){
					return '<font color="red">删除</font>';
				}else{
					return '-';
				}
            }},
            {"title": "数据源缩写", "data": "dataSrcAbbr","render":function(data,type,row){
            	debugger;
				if(data==null){
					return '-';
				}else{
					return data;
				}
            }},
            {"title": "数据接口编号", "data": "dataInterfaceNo","render":function(data,type,row){
				if(data==null||data==''){
					return '-';
				}
				if(row.flag=='0'){
					return data;
				}
				return compareModel.getData(row,'<a href="#" onclick=compareModel.detail("'+row.dataInterfaceName+'")>'+data+'</a>');
            }},
            {"title": "数据接口名", "data": "dataInterfaceName","render":function(data,type,row){
				if(data==null){
					return '-';
				}else{
					return data;
				}
            }},
            {"title": "数据接口描述", "data": "dataInterfaceDesc","render":function(data,type,row){
				if(data==null||data==''){
					return '-';
				}
				if(row.flag=='0'){
					return data;
				}
				return compareModel.getData(row,data);
            }},
            /*{"title": "存储过程", "data": "procName","render":function(data,type,row){
				return compareModel.getData(row,data);
            }},*/
            {"title": "数据加载频率", "data": "dataLoadFreq","render":function(data,type,row){
				return compareModel.getData(row,data);
            }},
            {"title": "数据加载方式", "data": "dataLoadMthd","render":function(data,type,row){
				return compareModel.getData(row,data);
            }},
            {"title": "字段分割符", "data": "filedDelim","render":function(data,type,row){
				return compareModel.getData(row,data);
            }},
            {"title": "行分隔符", "data": "lineDelim","render":function(data,type,row){
				return compareModel.getData(row,data);
            }},
            {"title": "外表数据库", "data": "extrnlDatabaseName","render":function(data,type,row){
				return compareModel.getData(row,data);
            }},
            {"title": "内表数据库", "data": "intrnlDatabaseName","render":function(data,type,row){
				return compareModel.getData(row,data);
            }},
            {"title": "外表表名", "data": "extrnlTableName","render":function(data,type,row){
				return compareModel.getData(row,data);
            }},
            {"title": "内表表名", "data": "intrnlTableName","render":function(data,type,row){
				return compareModel.getData(row,data);
            }},
            {"title": "表类型", "data": "tableType","render":function(data,type,row){
				return compareModel.getData(row,data);
            }},
            {"title": "分桶数", "data": "bucketNumber","render":function(data,type,row){
            	return compareModel.getData(row,data);
            }},
            /*{"title": "起效日期", "data": "sDate","render": function(data, type, row) {
            	var oDate = new Date(data);
            	var oYear = oDate.getFullYear();
            	var oMonth = oDate.getMonth()+1;
            	var oDay = oDate.getDate();
            	return oYear+"-"+oMonth+"-"+oDay;
            }},*/
            {"title": "标红", "data": "red","visible":false}
            ],
        ajax: {
            url: '/interface/queryInterfaceCompare',
            "type": 'GET',
            "data": function (d) { // 查询参数
            	debugger;
                return $.extend({}, d, obj);
            }
        },
        "fnDrawCallback": function (oSettings, json) {
            
        }
    });
}

