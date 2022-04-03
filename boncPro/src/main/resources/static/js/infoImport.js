/**
 * 接口列表操作
 */
var infoModel = {

    // 初始页面内容显示
    init: function(idx){
        var obj = {};
        obj['dataSrcAbbr'] = idx;
        initInterfaceTable(obj);
        //按条件查询用
        $("#inter_dataSrcAbbr").val(idx);
    },
    initColumn: function(dataSrcAbbr,dataInterfaceNo,columnNo){
        debugger;
        var obj = {};
        obj['dataSrcAbbr'] = dataSrcAbbr;
        obj['dataInterfaceNo'] = dataInterfaceNo;
        obj['columnNo'] = columnNo;
        initColumnTable(obj);

        $("#column_dataSrcAbbr").val(dataSrcAbbr);
        $("#column_dataInterfaceNo").val(dataInterfaceNo);
    },
    reset: function(idx){
        var obj = {};
        obj['dataSrcAbbr'] = idx;
        initColumnTable(obj);
        //按条件查询用
        $("#column_dataSrcAbbr").val(idx);
    },
    initProc: function(idx){
        var obj = {};
        obj['dataSrcAbbr'] = idx;
        initProcTable(obj);
        //按条件查询用
        $("#proc_dataSrcAbbr").val(idx);
    },

    //导入正式表
    common:function(url){
        var chkBoxes = $('#interfaceTable').find('input:checked');
        if (chkBoxes.length == 0) {
            zUI.dialog.alert('<pre>请至少选择一条记录</pre>');
            return false;
        }
        var dataSrcAbbr = $("#model_dataSrcAbbr").val();
        // var dataInterfaceNo = $("#model_dataInterfaceNo").val();
        var dataInterfaceName = $("#model_dataInterfaceName").val();

        var arr = [];
        $(chkBoxes).each(function() {
            arr.push($(this).attr('id'));
        });
        var paramInface = {};
        paramInface.tables = arr;
        paramInface.dataSrcAbbr = dataSrcAbbr;
        // param.dataInterfaceNo = dataInterfaceNo;
        paramInface.dataInterfaceName = dataInterfaceName;
        var json = JSON.stringify(paramInface);
        console.log("json:::"+json);
        $('#loadAlert').modal({'show': 'center', "backdrop": "static"});
        $.ajax({
            url:url,
            type:"post",
            data:json,
            contentType:"application/json;charset=UTF-8",
            success:function(data){
                $('#loadAlert').modal('hide');
                console.log(data);
                if(data.msgCode=='1111'){
                    zUI.dialog.alert('<pre>'+data.msgData+'</pre>');
                }else{
                    zUI.dialog.alert_max('<pre>'+data.msgData+'</pre>');
                    debugger;
                    interfaceModel.init(data.idx);
                }
            }
        })
    },

    //按条件查询
    search: function(){
        var inputs = $('#interfaceContent .input-group .form-control');
        var obj = {};
        for (var i = 0; i < inputs.length; i++) {
            if ($.trim($(inputs[i]).val()) == '') {
                continue;
            }
            obj[$(inputs[i]).attr('data-col')] = $.trim($(inputs[i]).val());
        }
        initInterfaceTable(obj);
    },

    //导入表结构
    importTable: function(){
        // window.open("/interface/importTable",'newwindow','width='+(window.screen.availWidth)+',height='+(window.screen.availHeight-20)+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
        window.open("/info/infoAlert",'newwindow','width='+(window.screen.availWidth)+',height='+(window.screen.availHeight-20)+',top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');

    },
    // 关闭弹框
    hidden: function () {
        $('#interfaceAlert').modal('toggle', 'center');
    },
};


/**
 * 初始化
 */

function initInfoTable(obj) {

    $('#procTable').width('100%').dataTable({
        //默认搜索组件
        "searching": false,
        //排序功能
        "ordering": false,
        "destroy": true,
        // 自动列宽
        "autoWidth": true,
        //滚动条
        "scrollX":false,
        //控制每页显示条数
        "lengthChange": false,
        "pagingType": "full_numbers",
        //翻页功能
        "paging": true,
        //控制总数显示
        "info": true,
        //列表的过滤,搜索和排序信息会传递到Server端进行处理
        "serverSide": false,
        "pageLength": 10,
        "columns": [
            /*{"title": "全选<input type=\"checkbox\" id=\"checkedAll\" onclick=\"checkedAll()\">", "data": null, "width": "5%","render": function (data, type, row) {
               return '<input type="checkbox" id="'+row.dataInterfaceNo+'" name="items">';
            }},*/

            {"title": "序号", "data": "dataSrcAbbr"},
            {"title": "版本", "data": "dataInterfaceNo"},
            {"title": "导入文件名称", "data": "dataInterfaceNo"},
            {"title": "接口总数", "data": "dataInterfaceNo"},
            {"title": "新增", "data": "procDatabaseName"},
            {"title": "修改", "data": "procName"},
            {"title": "修改时间", "data": "procName"},
            {"title":"操作" ,"data": null,"render": function(data, type, row) {
                    var html = '<div>';
                    html += '<span onclick="procModel.version(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\')" class="cm-tblA"><img src="imgs/index/history.png" ></span>';
                    html += '<span onclick="procModel.add(2,this)" title="生成上线脚本"><img src="imgs/index/down.png" ></span>';
                    html += '</div>';
                    $("#row_"+row.functionId).data("rowData",row);
                    return html;
                }},
            /*{"title": "起效日期", "data": "sDate"},
            {"title": "失效日期", "data": "eDate"}*/

        ],
        ajax: {
            url: '/model/queryProc',
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

function initInterfaceTable(obj) {

    $('#interfaceTable').width('100%').dataTable({
        //默认搜索组件
        "searching": false,
        //排序功能
        "ordering": false,
        "destroy": true,
        // 自动列宽
        "autoWidth": false,
        //滚动条
        "scrollX": true,
        //控制每页显示条数
        "lengthChange": false,
        "pagingType": "full_numbers",
        //翻页功能
        "paging": true,
        //控制总数显示
        "info": true,
        //列表的过滤,搜索和排序信息会传递到Server端进行处理
        "serverSide": false,
        "pageLength": 10,
        "columns": [
            {"title": "<input type=\"checkbox\" id=\"checkedAll\" onclick=\"checkedAll()\">全选", "data": null, "width": "5%","render": function (data, type, row) {
                    return '<input type="checkbox" id="'+row.dataSrcAbbr+'-'+row.dataInterfaceNo+'-'+row.dataInterfaceName+'" name="items">';
                }},

            // {"title":"操作" ,"data": null,"render": function(data, type, row) {
            // 	var html = '<div>';
            // 		html += '<span onclick="interfaceModel.detail(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\',\''+row.dataInterfaceName+'\');" class="btn-sm cm-tblA">字段</span>';
            // 		/*html += '<span onclick="interfaceModel.version(1,\''+row.dataSrcAbbr+'|'+row.dataInterfaceNo+'\');" class="btn-sm cm-tblA">版本</span>';*/
            // 		html += '<span onclick="interfaceModel.version(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\',\''+row.dataInterfaceName+'\')" class="btn-sm cm-tblA">版本</span>';
            // 		html += '<span onclick="interfaceModel.add(2,this)" class="btn-sm cm-tblA">修改</span>';
            // 		html += '<span onclick="interfaceModel.del(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\',\''+row.dataInterfaceName+'\');" class="btn-sm cm-tblA">删除</span>';
            // 		html += '</div>';
            // 	//$("#row_"+row.functionId).data("rowData",row);
            // 	return html;
            // }},

            {"title": "数据源缩写", "data": "dataSrcAbbr"},
            {"title": "数据接口编号", "data": "dataInterfaceNo"},
            {"title": "数据接口名", "data": "dataInterfaceName"},
            {"title": "数据接口描述", "data": "dataInterfaceDesc","render":function(data,type,row){
                    return '<p style="word-wrap:break-word;">' + data + '</p>';
                }},
            {"title": "数据加载频率", "data": "dataLoadFreq"},
            {"title": "数据加载方式", "data": "dataLoadMthd"},
            {"title": "字段分割符", "data": "filedDelim"},
            {"title": "行分隔符", "data": "lineDelim"},
            {"title": "外表数据库", "data": "extrnlDatabaseName"},
            {"title": "内表数据库", "data": "intrnlDatabaseName"},
            {"title": "外表表名", "data": "extrnlTableName"},
            {"title": "内表表名", "data": "intrnlTableName"},
            {"title": "表类型", "data": "tableType"},
            {"title": "分桶数", "data": "bucketNumber"},
            {"title": "起效日期", "data": "sDate","render": function(data, type, row) {
                    var oDate = new Date(data);
                    var oYear = oDate.getFullYear();
                    var oMonth = oDate.getMonth()+1;
                    var oDay = oDate.getDate();
                    return oYear+"-"+oMonth+"-"+oDay;
                }}
            /*{"title": "失效日期", "data": "eDate","hidden":"true"},*/

        ],
        ajax: {
            url: '/interface/queryInterface',
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

function initColumnTable(obj) {
    $('#columnTable').width('100%').dataTable({
        //默认搜索组件
        "searching": false,
        //排序功能
        "ordering": false,
        "destroy": true,
        // 自动列宽
        "autoWidth": false,
        //滚动条
        "scrollX": true,
        //控制每页显示条数
        "lengthChange": false,
        "pagingType": "full_numbers",
        //翻页功能
        "paging": true,
        //控制总数显示
        "info": true,
        //列表的过滤,搜索和排序信息会传递到Server端进行处理
        "serverSide": false,
        "pageLength": 10,
        "columns": [
            {
                "title": "<input type=\"checkbox\" id=\"checkedAll\" onclick=\"checkedAll()\">全选",
                "data": null,
                "width": "5%",
                "render": function (data, type, row) {
                    return '<input type="checkbox" id="' + row.dataInterfaceName + '-' + row.columnName + '" name="itemsColumn">';
                }
            },
            {
                "title": "操作", "data": null, "render": function (data, type, row) {
                    var html = '<div>';
                    html += '<span onclick="columnModel.version(\'' + row.dataSrcAbbr + '\',\'' + row.dataInterfaceNo + '\',\'' + row.columnNo + '\',\'' + row.dataInterfaceName + '\');" class="btn-sm cm-tblA">版本</span>';
                    html += '<span onclick="columnModel.add(2,this)" class="btn-sm cm-tblA">修改</span>';
                    html += '<span onclick="columnModel.del(\'' + row.dataSrcAbbr + '\',\'' + row.dataInterfaceNo + '\',\'' + row.columnNo + '\',\'' + row.dataInterfaceName + '\');" class="btn-sm cm-tblA">删除</span>';
                    html += '</div>';
                    $("#row_" + row.functionId).data("rowData", row);
                    return html;
                }
            },
            {"title": "数据源缩写", "data": "dataSrcAbbr"},
            {"title": "接口编号", "data": "dataInterfaceNo"},
            {"title": "接口名", "data": "dataInterfaceName"},
            {"title": "字段编号", "data": "columnNo"},
            {"title": "字段名", "data": "columnName"},
            {"title": "数据类型", "data": "dataType"},
            {"title": "格式", "data": "dataFormat"},
            {"title": "是否非空", "data": "nullable"},
            {"title": "空值替代值", "data": "replacenull"},
            {"title": "分隔符", "data": "comma"},
            {"title": "字段描述", "data": "columnComment"},
            {"title": "分桶字段", "data": "isbucket"},
            {"title": "是否为主键", "data": "iskey"},
            {"title": "是否有效字段", "data": "isvalid"},
            {"title": "增量字段", "data": "incrementfield"},
            {
                "title": "起效日期", "data": "sDate", "render": function (data, type, row) {
                    var oDate = new Date(data);
                    var oYear = oDate.getFullYear();
                    var oMonth = oDate.getMonth() + 1;
                    var oDay = oDate.getDate();
                    return oYear + "-" + oMonth + "-" + oDay;
                }
            }
            /*{"title": "失效日期", "data": "eDate" },*/
        ],
        ajax: {
            url: '/col/queryColumn',
            "type": 'GET',
            "data": function (d) { // 查询参数
                return $.extend({}, d, obj);
            }
        },
        "fnDrawCallback": function (oSettings, json) {

        }
    });
}

function initProcTable(obj) {

    $('#procTable').width('100%').dataTable({
        //默认搜索组件
        "searching": false,
        //排序功能
        "ordering": false,
        "destroy": true,
        // 自动列宽
        "autoWidth": true,
        //滚动条
        "scrollX":false,
        //控制每页显示条数
        "lengthChange": false,
        "pagingType": "full_numbers",
        //翻页功能
        "paging": true,
        //控制总数显示
        "info": true,
        //列表的过滤,搜索和排序信息会传递到Server端进行处理
        "serverSide": false,
        "pageLength": 10,
        "columns": [
            {"title": "全选<input type=\"checkbox\" id=\"checkedAll\" onclick=\"checkedAll()\">", "data": null, "width": "5%","render": function (data, type, row) {
                    return '<input type="checkbox" id="'+row.dataInterfaceNo+'" name="itemsProc">';
                }},
            {"title":"操作" ,"data": null,"render": function(data, type, row) {
                    var html = '<div>';
                    html += '<span onclick="procModel.version(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\')" class="btn-sm cm-tblA">版本</span>';
                    html += '<span onclick="procModel.add(2,this)" class="btn-sm cm-tblA">修改</span>';
                    html += '<span onclick="procModel.del(\''+row.dataSrcAbbr+'\',\''+row.dataInterfaceNo+'\');" class="btn-sm cm-tblA">删除</span>';
                    html += '</div>';
                    $("#row_"+row.functionId).data("rowData",row);
                    return html;
                }},
            {"title": "数据源缩写", "data": "dataSrcAbbr"},
            {"title": "数据接口编号", "data": "dataInterfaceNo"},
            /*{"title": "数据接口名", "data": "dataInterfaceName"},*/
            /*{"title": "数据接口描述", "data": "dataInterfaceDesc"},*/
            {"title": "存储过程数据库名", "data": "procDatabaseName"},
            {"title": "存储过程", "data": "procName"}
            /*{"title": "起效日期", "data": "sDate"},
            {"title": "失效日期", "data": "eDate"}*/

        ],
        ajax: {
            url: '/model/queryProc',
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