/**
 * Created by Administrator on 2016/6/15.
 */
var Common = {
    storage: window.localStorage,
    bindEvent: function() {
    //    $("#tab a").on("click",function(){
    //        $(this).addClass("current").siblings().removeClass("current");
    //        $(".tab-con .tab-item").eq($(this).index()).show().siblings().hide();
    //    })
    //    $("#tab a i").on("click",function(){
    //        $(this).parent().remove();
    //        Common.closeTab($(this).parent().attr("rel"));
    //    })
    //    $("#show-aside2").on("click",function(){
    //        $("#show-aside2").toggleClass("current");
    //        if($("#right-conatiner").parent().width()>1000){
    //            $("#right-conatiner").parent().css({"width":"48%"});
    //            $("#right-aside").width("48%");
    //        }else{
    //            $("#right-conatiner").parent().css({"width":"96%"});
    //            $("#right-aside").css("width","0");
    //        }
    //    })
    //    //拖拽，拉伸
    //    var clickX, leftOffset, inx, nextW2, nextW;
    //    var dragging  = false;
    //    var doc 	  = document;
    //    var labBtn 	  = $("#dragg-btn");
    //    var wrapWidth = $(".right-section").width();
    //    labBtn.bind('mousedown',function(){
    //            dragging   = true;
    //            leftOffset = $(".right-section").offset().left
    //    }
    //    );
    //    doc.onmousemove = function(e){
    //        if (dragging) {
    //            //labBtn.eq(inx).prev().text( labBtn.eq(inx).prev().width() );
    //            //labBtn.eq(inx).next().text( labBtn.eq(inx).next().width() );
    //            //--------------------------------------------
    //            clickX = e.pageX;
    //            //$("#test").text( '鼠标位置：' + clickX );
    //
    //            //第一个拖动按钮左边不出界
    //            if(clickX > leftOffset) {
    //                labBtn.css('left', clickX - 7 - leftOffset + 'px');//按钮移动
    //
    //                labBtn.prev().prev().width( clickX-leftOffset + 'px');
    //                nextW2 = clickX-leftOffset;
    //                $("#right-aside").width( wrapWidth - nextW2-30 + 'px');
    //            } else {
    //                labBtn.css('left', '0px');
    //            }
    //
    //            if(clickX > ($("#right-aside").offset().left-25)) {
    //                //第一个按钮右边不出界
    //                labBtn.css('left', parseFloat(labBtn.css('left')) + 'px');
    //                //第一个按钮，左右容器不出界
    //                labBtn.prev().prev().width( labBtn.offset().left + 6 - leftOffset + 11 + 'px' );
    //                $("#right-aside").width( '0px' );
    //            }
    //        }
    //    };
    //
    //    $(doc).mouseup(function(e) {
    //        dragging = false;
    //        e.cancelBubble = true;
    //    })

    },
    init: function() {
        Common.bindEvent();
        //$("nav li").click(function(){
        //    $(this).addClass("current").siblings().removeClass("current");
        //    var txt=$(this).children("a").text();
        //    var rel=$(this).children("a").attr("rel");
        //    var flag=0;
        //    $("#tab a").each(function(){
        //        if($(this).children("b").text()==txt){
        //            flag=1;
        //            $(this).addClass("current").siblings().removeClass("current");
        //            $(".tab-con .tab-item").eq($(this).index()).show().siblings().hide();
        //        }
        //    })
        //    if(flag==0){
        //        $("#tab").append('<a href="javascript:;" rel="'+rel+'"><b>'+txt+'</b><i>&times;</i></a>');
        //        Common.bindEvent();
        //        Common.creatTab(rel);
        //    }
        //})
        //
        //$("#right-aside").load("right-aside.html");

    },
    creatTab:function(rel){
        //新建当前页
        $(".tab-con").append('<div class="tab-item" data-rel="'+rel+'"></div>');
        $(".tab-con .tab-item").eq(-1).html("").load(rel).siblings().hide();
        $("#tab a").eq(-1).addClass("current").siblings().removeClass("current");
    },
    closeTab:function(rel){
        //关闭当前页
        $(".tab-con .tab-item").each(function(){
            if($(this).data("rel")==rel){
                $(this).prev(".tab-item").show();
                $("#tab a").eq($(this).index()-1).addClass("current").siblings().removeClass("current");
                $(this).remove();
            }
        })
    },
    getQueryString: function(paramName) {
        var reg = new RegExp("(^|&)" + paramName + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return unescape(r[2]);
            unescape()
        } else {
            return "";
        }
    },
    generateUUID:function(){
        var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
        s[8] = s[13] = s[18] = s[23] = "-";

        var uuid = s.join("");
        return uuid;
    },
    request: function(strParame) {
        var args = new Object();
        var query = location.href;
        var newQuery = query.substring(query.indexOf('?') + 1);
        var pairs = newQuery.split("&"); // Break at ampersand
        for (var i = 0; i < pairs.length; i++) {
            var pos = pairs[i].indexOf('=');
            if (pos == -1)
                continue;
            var argname = pairs[i].substring(0, pos);
            var value = pairs[i].substring(pos + 1);
            value = decodeURIComponent(value);
            args[argname] = value;
        }
        return args[strParame];
    },
    formateDate:function (str){
        if(typeof (str)!="undefined"&&typeof (str)!=null&&str!=null&&str!=""){
            var d = eval('new ' + str.substr(1, str.length - 2));
            var ar_date = [d.getFullYear(), d.getMonth() + 1, d.getDate()];
            for (var i = 0; i < ar_date.length; i++) ar_date[i] = Common.dFormat(ar_date[i]);
            return ar_date.join('-');
        }else{
            return "";
        }
    },
    formateBool:function(i){
        if(i=="true"||i==1){
            return "是";
        }else{
            return "否";
        }
    },
    myAlert:function(message,type) {
        var html;
        if(type==3){
            var top=($(window.parent).height()/2)+$(window.parent).scrollTop();
            html = '<div id="appendtip" style="position:absolute; top:'+top+'px; left:50%;transform: translate(-50%,0px); z-index: 99999999; text-align: center;padding: 10px 20px;background: #000;color: #fff;border-radius: 5px;opacity: 0.8">' + message + '</div>';
        }else if(type==2){
            var top=($(window).height()/2)+$(window).scrollTop()+200;
             html = '<div id="appendtip" style="position: fixed; top:'+top+'px; left:50%;transform: translate(-55%,0); z-index: 99999999; text-align: center;padding: 10px 20px;background: #000;color: #fff;border-radius: 5px;opacity: 0.8">' + message + '</div>';
        }else if(type==4){
            var top=($(window).height()/2)-400;
             html = '<div id="appendtip" style="position: fixed; top:'+top+'px; left:50%;transform: translate(-55%,0); z-index: 99999999; text-align: center;padding: 10px 20px;background: #000;color: #fff;border-radius: 5px;opacity: 0.8">' + message + '</div>';
        }
        else{
            html = '<div id="appendtip" style="position: fixed; z-index: 99999999; text-align: center; left:50%; top:50%;transform: translate(-50%,-50%);padding: 10px 20px;background: #000;color: #fff;border-radius: 5px;opacity: 0.8">' + message + '</div>';

        }
        if ($("#appendtip").size() > 0) {
            $("#appendtip").text(message)
            $("#appendtip").fadeIn().delay(2000).fadeOut();
        } else {
            $("body").append(html);
            $("#appendtip").delay(2000).fadeOut()
        }
    }
};

$(function() {
    Common.init();
    $.extend({
        //弹出框
        /*!参数
         * url：要载入的页面地址；
         * dialogHtml：dialog内容页（不和url共存）；
         * title：模态框标题；
         * width：模态框宽度：默认600
         * height：模态框高度：默认550
         * BtnOk：确认按钮文字
         * BtnCancel：取消按钮文字
         * okCallBack：确认按钮回调函数
         * cancelCallBack：取消按钮回调函数
         */
        /*!调用方法
         $.Dialog.load({
         url: "system/user/user_form.html",
         dialogHtml: "111",
         title: "远程加载页面",
         width: 1000,
         height: 250,
         btnok: "确定",
         btncl: "取消",
         cancelCallBack:function(){
         alert(11);
         },
         okCallBack:function(){
         alert(22);
         }
         })
         */
        Dialog: {
            _tplLoadHtml: '<div class="modal created-modal" id="[Id]">' +
            '<div class="modal-dialog" style="width:[Width]px ">' +
            '<div class="modal-content">' +
            '<div class="modal-header">' +
            '<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span></button>' +
            '<h5 class="modal-title">[Title]</h5>' +
            '</div>' +
            '<div class="modal-body small">' +
                //'<iframe src="[Url]" frameborder="0" height="[Height]px" width="100%" style="padding:0px; margin:0px;"></iframe>' +
            '<div style="padding:0px; margin:0px; height:[Height]px;width:100%"id="[Id]_inner">[DialogHtml]</div>' +
            '</div>' +
            '<div class="modal-footer" >' +
            '<button type="button" class="btn btn-primary ok" id="sure">[BtnOk]</button>' +
            '<button type="button" class="btn btn-default cancel" data-dismiss="modal">[BtnCancel]</button>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>',
            //随机生成id
            getId:function () {
                var date = new Date();
                return 'mdl' + date.valueOf();
            },
            //载入模态框
            load:function(options){
                //设置默认值
                var ops = {
                    url: '',
                    dialogHtml: '',
                    title: '',
                    btnok: "确认",
                    btncl: "取消",
                    okCallBack:function(){
                        return
                    },
                    cancelcallBack:function(){
                        return
                    }
                };
                $.extend(ops, options);
                var reg=new RegExp("\\[([^\\[\\]]*?)\\]", 'igm');
                var modalId = this.getId();
                var html = this._tplLoadHtml.replace(reg, function (node, key) {
                    return {
                        Id: modalId,
                        Title: ops.title,
                        Url: ops.url,
                        DialogHtml: ops.dialogHtml,
                        Height: ops.height,
                        Width: ops.width,
                        BtnOk: ops.btnok,
                        BtnCancel: ops.btncl,
                        okCallBack:ops.okCallBack,
                        cancelCallBack:ops.cancelcallBack
                    }[key];
                });
                $('body').append(html);
                if(ops.url!=""){
                    $('#' + modalId+"_inner").load(ops.url);
                }
                var modal = $('#' + modalId).modal({
                    width: ops.width
                });
                //模态框关闭事件
                $('#' + modalId).on('hide.bs.modal', function (e) {
                    $('#' + modalId).parent('.modal-scrollable').next().remove();
                    $('#' + modalId).parent('.modal-scrollable').remove();
                    $('#' + modalId).remove();
                });
                //模态框提交事件
                $("#sure").on("click",function(){
                    ops.okCallBack();

                })
            }
        },



        //loading加载中图标展示隐藏
        /*!调用方法
         $.Loading.showLoading();
         $.Loading.hideLoading();
         */
        Loading:{
            _scrollTop:$(window).scrollTop(),
            _windowHeight:$(window).height(),
            _trueTop:"",
            _tplLoadHtml: '<div id="out-loading"><div id="loading"><div id="floatingBarsG">'+
            '	<div class="blockG" id="rotateG_01"></div>'+
            '	<div class="blockG" id="rotateG_02"></div>'+
            '	<div class="blockG" id="rotateG_03"></div>'+
            '	<div class="blockG" id="rotateG_04"></div>'+
            '	<div class="blockG" id="rotateG_05"></div>'+
            '	<div class="blockG" id="rotateG_06"></div>'+
            '	<div class="blockG" id="rotateG_07"></div>'+
            '	<div class="blockG" id="rotateG_08"></div>'+
            '</div></div></div>',
            showLoading: function(){
                $("body").append(this._tplLoadHtml);
                this._trueTop=parseInt(this._scrollTop)+((parseInt(this._windowHeight)-55)/2);
                $("#loading").css("top",this._trueTop+"px");
            },
            hideLoading:function(){
                $("#out-loading").remove();
            }
        },

        //jquery datatable 二次封装
        DataTable:{
            isEmpty:function(str){
                return (str == null || $.trim(str).length == 0);
            },
            options:"",
            issearch:0,
            load:function(options){
                var data_table_object;
                this.options=options;
                // options参数说明 function 有complete 查询完成后会调用的事件
                // load 替换现有在加载函数 调用自己的加载函数来加载datatable数据 调用load函数会传递 这个4个参数url, queryParam,callback, oSettings
                // success 在表格数据成功加载后需要调用的function 会传递json数据 即后台返回的数据包

                // dataColum 设置每个对象即一列 里面的参数包括 Class title visible width  sort out
                // render 四个属性 Class自定义样式 title标题 visible是否显示列 width 自定义每列宽度 默认平均分配宽度
                // out 自定义此列输出内容 返回字符串 会传递2个参数 当前值 跟当前行的所有列对象数据
                // render 此函数覆盖原来的输出列函数 自定义列输出 三个参数 当前行数据 当前列数据 oSettings对象


                // hidden_title 是否隐藏表头 默认显示
                // selected 是否显示选择列
                // selectType 单选或多选 默认多选
                // showOrderNo 显示序号 默认不显示
                // language 语言版本（默认中文可选ch，en）
                // selectCall 选择框change时的触发事件调用函数 传递当前选中的内容json数组格式
                // 可以调selectedAll()函数获取当前选中的内容json数组
                var issearch=this.issearch;
                var url = options.url ? options.url : "自定义默认url";
                var table = options.table;// 显示列表数据 table
                var query = options.query ? options.query : "#queryParam";// 查询条件包裹元素id
                var table_class = $(table).attr("class"); // 是否自定义样式
                var hidden_title = options.hidden_title ? options.hidden_title : null;// 是否隐藏表头
                var sLanguage = options.language ? options.language : "ch";// 国际化
                var sUrl="../js/jquery.dataTable.cn.txt";
                var ajaxType=options.ajaxType?options.ajaxType:"GET";
                var ajaxData=options.ajaxData?options.ajaxData:[];
                var iDisplayLength=options.iDisplayLength?options.iDisplayLength:10;
                var lengthMenu=options.iDisplayLength?[[options.iDisplayLength],[options.iDisplayLength]]:[[10, 25, 50], [10, 25, 50]];
                if(sLanguage=="en"){
                    sUrl="";
                }
                var complete = function() {// 加载完成调用事件
                    if (options.complete)
                        options.complete();
                }

                var columns = [];
                var columnDefs = [];
                if ($.DataTable.isEmpty(table_class)) {
                    $(table).removeClass("table table-striped table-hover table-bordered");
                    $(table).addClass("table table-striped table-hover table-bordered");
                }
                var data_options = options.dataColum;// 表头的自定义列属性
                var bSort = options.sort ? options.sort : false;// 自定义表格否排序 true false
                //    data_options = JSON.parse(data_options);
                var selectedType = options.selectType ? options.selectType : "checkbox";// 选择类型单选或多选
                // checkbox
                // radio
                var dis = (selectedType == "radio") ? "disabled" : "";
                if (options.selected) {// 是否显示复选框默认不显示
                    columns.push({
                        "mDataProp" : "check",
                        "sTitle" : "<input title='全选/反选' type='" + selectedType + "' "
                        + dis + " name='bootstarp_data_table_checkbox'>",
                        "sClass" : "left selected",
                        "bVisible" : true,
                        "sWidth" : "2%",
                        "bSortable" : false,
                        "render" : function() {
                            return "<input title='选择' type='" + selectedType
                                + "' name='bootstarp_data_table_checkbox'>";
                        }
                    });
                }
                //显示序号
                if (options.showOrderNo) {// 是否显示复选框默认不显示
                    columns.push({
                        "sTitle" : "序号",
                        "sClass" : "center selected",
                        "bVisible" : true,
                        "sWidth" : "5%",
                        "bSortable" : false,
                        "mData": null,

                    });
                }
                var displayLen = data_options.length;// 获取显示列数量
                $.each(data_options, function() {
                    var visible = this["visible"];
                    if (visible==false)
                        displayLen--;
                });
                $.each(data_options, function(index, td) {// 初始化列数据
                    var sClass = td["Class"] ? td["Class"] : "center";// 居中
                    var sTitle = td["title"] ? td["title"] : "";// 标题
                    var bVisible = td["visible"] ? false : true;// 是否隐藏
                    var sWidth = td["width"] ? td["width"] : (100 / displayLen) + "%";// 不设置宽度默认
                    var bSortable = td["sort"] ? td["sort"] : bSort;// 自定义列是否排序 true false
                    var isIS8N = td["isIS8N"] ? td["isIS8N"] :false;//是否国际化
                    if(isIS8N=="true"){
                        sTitle=td["etitle"];
                    }

                    var norender=function(data,dis,full){
                        if(typeof(data)=="undefined" ){
                            return "";
                        }else{
                            return data;
                        }
                    }
                    var renderDef=function(data,dis,full){
                        if(typeof(data)=="undefined" ){
                            return "";
                        }else{
                            return td.render(data,dis,full);
                        }
                    }
                    var render=td.render ? renderDef :norender;
                    var current_index=index;
                    if(options.selected||options.showOrderNo){
                        current_index=index+1;
                    }
                    if(options.selected && options.showOrderNo){
                        current_index=index+2;
                    }
                    // 平均分配
                    var column = {
                        "mDataProp" : td.name,
                        "sTitle" : sTitle,
                        "sClass" : sClass,
                        "bVisible" : bVisible,
                        "sWidth" : sWidth,
                        "bSortable" : bSortable
                    };
                    var columnDef={
                        "targets":[current_index],
                        "render":render
                    };
                    //插入操作列
                    if (td.out) {
                        column["render"] = function(data, type, full) {// 编辑列需要执行的自定义函数输出内容
                            // 此函数会接收两个参数 （第一个是此列的值
                            // 第二个是当前行所有内容）
                            var doAction='<div class="hidden-sm hidden-xs action-buttons">';
                            $.each(td.out,function(index,res){
                                //debugger
                                if(typeof(res.isshow)=="undefined"||res.isshow(data, type, full)!=false){
                                    doAction+='<a class="blue" href="javascript:;" onclick="'+res.aaction+'" title="'+res.aname+'" alt="'+res.aname+'"> <i class="ace-icon '+res.aicon+' bigger-130"></i> </a> ';
                                }
                            })
                            doAction+="</div>";
                            doAction+='<div class="hidden-md hidden-lg">'+
                                '<div class="inline pos-rel">' +
                                '<button class="btn btn-minier btn-yellow dropdown-toggle" data-position="auto" data-toggle="dropdown" aria-expanded="false">' +
                                ' <i class="ace-icon fa fa-caret-down icon-only bigger-120"></i>' +
                                '</button>' +
                                ' <ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close" style="">';
                            $.each(td.out,function(index,res){
                                if(typeof(res.isshow)=="undefined"||res.isshow(data, type, full)!=false) {
                                    doAction += ' <li>' +
                                        '<a class="tooltip-info" title="" data-rel="tooltip" href="#" data-original-title="View">' +
                                        '<span class="blue" onclick="' + res.aaction + '" title="' + res.aname + '" alt="' + res.aname + '">' +
                                        '<i class="ace-icon ' + res.aicon + ' bigger-120"></i>' +
                                        '</span>' +
                                        '</a>' +
                                        ' </li>';
                                }
                            })
                            doAction+='</ul>' +
                                '</div>' +
                                '</div>  ';
                            return doAction;
                        }
                    }
                    columns.push(column);
                    columnDefs.push(columnDef);


                });


                //数据返回成功后回调函数
                function success(json) {
                    // to code
                }
                var successFunc = options.success ? options.success : success;
                // 3个参数的名字可以随便命名,但必须是3个参数,少一个都不行
                //sSource, aoData, fnCallback, oSettings
                function datatable_callback(url, queryParam, callback, oSettings) {
                    //queryParam.push($(query).formJSON());// 查询条件
                    if($.isArray(ajaxData)){
                        for(i=0;i<ajaxData.length;i++){
                            if($.inArray(ajaxData[i],queryParam)==-1){
                                queryParam.push(ajaxData[i]);
                            }
                        }
                    }
                    var paramObj={ "name": $(query).attr("name"), "value":encodeURI($(query).val())};
                    if($.inArray(paramObj,queryParam)==-1){
                        queryParam.push(paramObj);
                    }
                    if(issearch==1){
                        queryParam[3].value=0;
                    }
                    console.log(queryParam);
                    $.request({
                        url : url,// 这个就是请求地址对应sAjaxSource
                        data : queryParam, // 这个是把datatable的一些基本数据传给后台,比如起始位置,每页显示的行数
                        success : function(json) {
                            json = json || [];
                            json["sEcho"] = oSettings._sEcho | oSettings.iDraw;
                            if (options.success)
                                options.success(json);
                            typeof (json.obj.data)=="undefined"? json.obj.data=[] :json.obj.data;
                            callback(json.obj);// 把返回的数据传给这个方法就可以了,datatable会自动绑定数据的
                        }
                    });
                }
                var data_table_load = options.load ? options.load : datatable_callback;
                data_table_object = $(table).dataTable({
                    "bFilter": false,//去掉搜索框
                    //自适应宽度
                    bAutoWidth: false,
                    bServerSide: true,
                    "bStateSave" : true,
                    "sAjaxSource" : url+"?timeStamp="+new Date().getTime()+"",
                    "bDeferRender":true,
                    "aaSorting": [],
                    "bPaginate": true, //翻页功能
                    "sPaginationType" : "full_numbers",
                    "iDisplayLength":iDisplayLength,//每页显示几条
                    "bLengthChange": true,
                    "lengthMenu":lengthMenu,
                    "sAjaxSource" : url,
                    "sAjaxDataProp" : "data",
                    "fnServerData" : data_table_load, // 获取数据的处理函数
                    //"bSort" : bSort,// 是否使用排序
                    "aoColumns" : columns,
                    "aoColumnDefs" : columnDefs,
                    "oLanguage" : {
                        "sUrl":sUrl,
                    },
                    "fnDrawCallback": function(){
                        //表格颜色重置
                        $(".dataTable > thead > tr > th[class*=sorting_]").css("color","#333");
                        //显示序号
                        if (options.showOrderNo) {
                            var api = this.api();
                            var startIndex= api.context[0]._iDisplayStart;//获取到本页开始的条数

                            if(options.selected){
                                api.column(1).nodes().each(function(cell, i) {
                                    cell.innerHTML = startIndex + i + 1;
                                });
                            }else{
                                api.column(0).nodes().each(function(cell, i) {
                                    cell.innerHTML = startIndex + i + 1;
                                });
                            }
                        }
                        //全选
                        $('thead', $(table)).find(
                            ":input[name='bootstarp_data_table_checkbox']").on(
                            "click",
                            function() {
                                //alert(11);
                                $('tbody', $(table)).find(
                                    ":input[name='bootstarp_data_table_checkbox']")
                                    .prop("checked", $(this).is(":checked"));
                                if($(this).is(":checked")){
                                    $('tbody', $(table)).find("tr").addClass("selected");
                                }else{
                                    $('tbody', $(table)).find("tr").removeClass("selected");
                                }

                            });
                        if(issearch==1){
                            $('#datatable_first a').click();
                            issearch=0;
                            this.issearch=0;
                        }
                        //在tabel外增加div，然后自动滚动
                        $.each($('.dataTable'),function(){
                            if($(this).parent().attr('class')!='divDataTable'){
                                $(this).after('<div style="overflow-x:auto" class="divDataTable"><div class="postionDatatable"></div></div>');
                                $(this).insertAfter($(this).parent().find('.postionDatatable'));
                            }
                        })
                        //$('.dataTable').after('<div style="overflow-x:auto" id="divDataTable"><div id="postionDatatable"></div></div>');
                        //$('.dataTable').insertAfter($('#postionDatatable'));
                    }
                });

                if (options.selected) {
                    // 选择框事件
                    $('tbody', $(table))
                        .on(
                        'click',
                        'td',
                        function() {
                            if ($(this)
                                    .find(
                                    ":input[name='bootstarp_data_table_checkbox']").length < 1) {
                                var box = $(this)
                                    .parent()
                                    .find(
                                    ":input[name='bootstarp_data_table_checkbox']");
                                box.prop("checked", !box.is(":checked"));
                                $(this).parents("tr").toggleClass("selected");
                            }
                            if (options.selectCall) {// 选择框点击时触发selectCall函数
                                var selecteds = data_table_object.selectedAll();
                                options.selectCall(selecteds);
                            }
                        });


                    $('tbody', $(table))
                        .on(
                        'click',
                        'input[name="bootstarp_data_table_checkbox"]',
                        function() {
                            if ($(this).is(":checked")){
                                $(this).parents("tr").addClass("selected");
                            }else{
                                $(this).parents("tr").removeClass("selected");
                            }
                            if (options.selectCall) {// 选择框点击时触发selectCall函数
                                var selecteds = data_table_object.selectedAll();
                                options.selectCall(selecteds);
                            }
                        });


                    // 获取datatable选中行的所有数据
                    data_table_object.selectedAll = function() {
                        var selected = [];
                        $.each(this.fnGetNodes(), function() {
                            var rows = this;
                            var checked = $(this).find(
                                ":input[name='bootstarp_data_table_checkbox']").is(
                                ":checked");
                            if (checked)
                                selected.push(data_table_object.fnGetData(rows));
                        });
                        return selected;
                    };
                }
                //影藏表头
                if (hidden_title) {
                    $(table).find("tr:eq(0)").hide();
                }
                return data_table_object;
            },
            search:function(oTable,json){
                oTable.fnDestroy();
                this.options.iDisplayStart = 0;
                this.options.ajaxData=json;
                this.issearch=1;
                this.load(this.options);
            },
            getSelectedData:function(tblId) {
                var nTrs = tblId.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr对象
                var arr=[];
                for (var i = 0; i < nTrs.length; i++) {
                    if ($(nTrs[i]).hasClass('selected')) {
                        arr.push(tblId.fnGetData(nTrs[i]));
                    }
                }
                return arr;
            },
            getCurrentRowData:function(tblId,index){
                var nTrs = tblId.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr对象
                for (var i = 0; i < nTrs.length; i++) {
                    if (i==index) {
                        return tblId.fnGetData(nTrs[i]);
                    }
                }
            },
            getCurrentRowDataByObj:function(tblId,obj){
                var nTrs = tblId.fnGetNodes();//fnGetNodes获取表格所有行，nTrs[i]表示第i行tr对象
                for (var i = 0; i < nTrs.length; i++) {
                    if (i==$(obj).parents("tr").index()) {
                        return tblId.fnGetData(nTrs[i]);
                    }
                }
            }
        }
    })
});

function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " " + date.getHours() + seperator2 + date.getMinutes()
        + seperator2 + date.getSeconds();
    return currentdate;
}

function showLoad(type){
    $(window.parent.document).find("#png").height($(window.parent.document).height());
    $(window.parent.document).find("#png").show();
    if(type){
        $(window.parent.document).find("#floatingCirclesG").css("top",($(window).scrollTop()+($(window).height())+70)+"px")
    }else{
        $(window.parent.document).find("#floatingCirclesG").css("top",($(window).scrollTop()+($(window).height())/2-50)+"px")
    }
    $(window.parent.document).find("#floatingCirclesG").show();
};
function closeLoad(){
    $(window.parent.document).find("#floatingCirclesG").hide();
    $(window.parent.document).find("#png").hide();
};

function updateSuccess(){
    Common.myAlert("更新成功");
    setTimeout(" window.location.reload();",2000);

};

Date.prototype.format = function(format) {  
    var o = {  
        "M+" :this.getMonth() + 1, // month  
        "d+" :this.getDate(), // day  
        "h+" :this.getHours(), // hour  
        "m+" :this.getMinutes(), // minute  
        "s+" :this.getSeconds(), // second  
        "q+" :Math.floor((this.getMonth() + 3) / 3), // quarter  
        "S" :this.getMilliseconds()  
    }  
  
    if (/(y+)/.test(format)) {  
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));  
    }  
  
    for ( var k in o) {  
        if (new RegExp("(" + k + ")").test(format)) {  
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
        }  
    }  
    return format;  
}  


