<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@include file="/WEB-INF/views/include/head.jsp"%>

<html lang="en">
<head lang="en">
	<c:choose>
		<c:when test="${pageType eq 'recheck'}">
			<title>QA复检</title>
		</c:when>
		<c:otherwise>
			<title>QA反馈报告（不通过）</title>
		</c:otherwise>
	</c:choose>

	<link rel="stylesheet" href="${static.resource.url}/js/plugs/layui-v2.5.4/css/layui.css">
	<link rel="stylesheet" href="${static.resource.url}/css/activity.css" />
	<link rel="stylesheet" href="${static.resource.url}/css/task.css?v=20190912" />
	<link rel="stylesheet" href="${static.resource.url}/css/qa.reject.css" />
	<script src="${static.resource.url}/js/plugs/layui-v2.5.4/layui.js"></script>
</head>
<body class="childBody">
<div class="tab-con sec-case-form btn-active">
	<div class="sec">
		<article class="sec-title t2">券信息</article>
		<div class="sec-form">
			<div class="sec-tbl layui-module-row">
				<table class="layui-table" id="moduleRowTab" lay-filter="moduleRowTab"></table>
			</div>
		</div>
	</div>


	<c:if test="${pageType eq 'recheck'}">
		<div class="sec">
			<div class="sec-top">
				<article class="sec-title t2">
					<span>QA上次检查结果</span>
				</article>
			</div>
			<div class="sec-form">
				<div class="sec-form-file">
					<div class="layui-form-item">
						<label class="layui-form-label layui-form-title"><b class="label">*</b>检查表：</label>
						<div class="layui-input-block">
							<c:choose>
								<c:when test="${not empty reportAttach}">
									<a href="javascript:void(0)" data-orgName="${reportAttach.originalFileName}" data-fileName="${reportAttach.fileName}" onclick="downloadAttach(this)">${reportAttach.originalFileName}</a>
								</c:when>
								<c:otherwise>

								</c:otherwise>
							</c:choose>

						</div>
					</div>
				</div>
				<div class="layui-collapse layui-prev-collapse">
					<c:forEach items="${moduleRowResult}" var="row">
						<c:if test="${row.qaResult > 0}">
							<div class="layui-colla-item" data-rowId="${row.id}">
								<h2 class="layui-colla-title">
										${row.configCertifiCenter}
								</h2>
								<div class="layui-colla-content">
									<table class="layui-table layui-table-prob" lay-data="{id:'${row.id}'}" lay-filter="${row.id}">
										<thead>
										<tr>
											<th lay-data="{type:'numbers', width:80, unresize:true}">序号</th>
											<th lay-data="{field:'remark', width:300, unresize:true}">问题描述</th>
											<th lay-data="{field:'imgSrc', unresize:true}">图片附件</th>
											<th lay-data="{field:'type', width:120, align:'center', unresize:true}">类型</th>
											<th lay-data="{field:'tempId', width:200, align:'center', unresize:true}">反馈情况</th>
										</tr>
										</thead>

										<tbody>
										<c:forEach items="${qaCheckList}" var="qt" varStatus="qtIndex">
											<c:if test="${qt.couponId eq row.id}">
												<tr>
													<td></td>
													<td>${qt.description}</td>
													<td>
														<div class="layui-item-value layui-label-color">
															<div class="layui-upload-list">
																<div class="layer-photos attach-view ${qt.id}-${qtIndex.index}" data-rowId="${qt.id}-${qtIndex.index}">
																	<c:forEach items="${fns:findListByClassAndType(qaCheckClassName, qt.id, '')}" var="attach">
																		<div class="layui-upload-item">
																			<img alt="${attach.originalFileName}" src="${attach.filePath}" class="layui-upload-img">
																		</div>
																	</c:forEach>
																</div>
															</div>
														</div>
													</td>
													<td>${fns:getDictLabel(qt.type, 'qa_check_type', '')}</td>
													<td>
														<c:choose>
															<c:when test="${qt.ignore eq '0'}">
																<div class="layui-edit-btn">
																	<button type="button" class="layui-btn layui-btn-sm layui-btn-disabled">已忽略</button>
																</div>
															</c:when>
															<c:otherwise>
																<div class="layui-edit-btn" title="${qt.feedback}">
																	${qt.feedback}
																</div>
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
											</c:if>
										</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</c:if>
					</c:forEach>
				</div>
			</div>
		</div>
	</c:if>

	<div class="sec">
		<div class="sec-top">
			<article class="sec-title t2">
				<c:choose>
					<c:when test="${pageType eq 'recheck'}">
						<span>QA本次检查结果</span>
					</c:when>
					<c:otherwise>
						<span>QA检查</span>
					</c:otherwise>
				</c:choose>
				<c:if test="${activity.receiveUser eq agent && (activity.status eq actStatusTesting || activity.status eq actStatusTestFeedBack)}">
					<span style="float: right;"><button id="addCouponData" type="button" class="layui-btn layui-btn-sm layui-coupon-btn" data-type="getCheckData" title="添加至问题券">+ 添加至问题券</button></span>
				</c:if>
			</article>
		</div>
		<div class="sec-form">
			<div class="sec-form-submit layui-form">
				<div class="layui-form-item">
					<label class="layui-form-label layui-form-title"><b class="label">*</b>检查结果：</label>
					<div class="layui-input-block" id="checkResult">
						<input id="rejectResult" type="radio" name="result" value="1" lay-skin="primary" title="不通过" checked="checked" lay-filter="result">
						<input id="approvalRsult" type="radio" name="result" value="0" lay-skin="primary" title="通过" lay-filter="result">
					</div>
				</div>
			</div>
			<div id="checkReportDiv" class="sec-form-file">
				<div class="layui-form-item">
					<label class="layui-form-label layui-form-title"><b class="label">*</b>检查表：</label>
					<div class="layui-input-block">
						<a id="checkReport" href="javascript:void(0)">检查报告</a>
						<span id="reportFilePrew"></span>
					</div>
				</div>
			</div>
			<div class="layui-collapse layui-curr-collapse" lay-filter="ucpCollapse">

			</div>
		</div>
	</div>
	<div class="sec">
		<div class="sec-btn">
			<c:if test="${activity.receiveUser eq agent && (activity.status eq actStatusTesting || activity.status eq actStatusTestFeedBack)}">
				<div class="sec-btn-submit">
					<button id="submitBtn" type="button" class="layui-btn layui-btn-sm layui-coupon-btn" data-type="saveData" title="保存">保存</button>
					<button id="approval" type="button" class="layui-btn layui-btn-sm layui-coupon-btn layui-hide" data-type="approval" title="通过">通过</button>
				</div>
			</c:if>
			<div class="sec-btn-close"><button type="button" class="layui-btn layui-btn-sm layui-btn-close" data-type="closeWindow" title="关闭">关闭</button></div>
		</div>
	</div>
</div>

<script type="text/javascript">

    // a.html 设置刷新 检测缓存是否有标志 要是有就说明数据有变化  a.html跳转到b.html页面
    window.addEventListener("pageshow", function(){
        if(sessionStorage.getItem("need-refresh")){
            layui.use(['form'], function(){
                var form = layui.form;
                var inputVal = $("#checkResult").html();
                $("#checkResult").empty().append(inputVal);
                form.render();
			});
            sessionStorage.removeItem("need-refresh");
        }
    });

	var checkReportFile = new Array();

    var typeData = '${checkType}';
    typeData = typeData.replace(/\\/g, "\\\\");
    typeData = typeData.replace(/\n/g,"").replace(/\r/g,"");
    if(typeData && typeData.length > 0){
        try {
            typeData = JSON.parse(typeData);
        } catch (e) {
            typeData = eval('('+typeData+')');
        }
    } else {
        typeData = new Array();
    }

    //准备视图对象
    window.viewObj = {
        tbData: [],
        typeData: typeData,
        renderSelectOptions: function(data, settings) {
            settings = settings || {};
            var valueField = settings.valueField || 'value',
                textField = settings.textField || 'text',
                selectedValue = settings.selectedValue || "";
            var html = [];
            for(var i = 0, item; i < data.length; i++) {
                item = data[i];
                html.push('<option value="');
                html.push(item[valueField]);
                html.push('"');
                if(selectedValue && item[valueField] == selectedValue) {
                    html.push(' selected="selected"');
                }
                html.push('>');
                html.push(item[textField]);
                html.push('</option>');
            }
            return html.join('');
        }
    };

    layui.use(["laytpl", 'table','element', 'upload', 'form', 'layer'], function(){
        var  laytpl = layui.laytpl,
            table = layui.table,
            elem = layui.element,
            upload = layui.upload,
            form = layui.form,
            layer = layui.layer;

        $(".layui-table-prob").empty();
        form.on('radio(result)', function(data){
            var radioValue = data.value;
            if(radioValue == 0){
                $("#addCouponData").addClass("layui-hide");
                $("#submitBtn").addClass("layui-hide");
                $("#checkReportDiv").addClass("layui-hide");

                $("#approval").removeClass("layui-hide");
            } else {
                $("#addCouponData").removeClass("layui-hide");
                $("#submitBtn").removeClass("layui-hide");
                $("#checkReportDiv").removeClass("layui-hide");

                $("#approval").addClass("layui-hide");
            }
        });

        var tabRowData = '${rowArray}';
        tabRowData = tabRowData.replace(/\\/g, "\\\\");
        tabRowData = tabRowData.replace(/\n/g,"").replace(/\r/g,"");
        if(tabRowData && tabRowData.length > 0){
            try {
                tabRowData = JSON.parse(tabRowData);
            } catch (e) {
                tabRowData = eval('('+tabRowData+')');
            }
        } else {
            tabRowData = new Array();
        }

        var couponTab = table.render({
            elem : '#moduleRowTab',
            id : 'moduleRowTab',
            page : false,
            limit: 10000,
            cols : [ [
                {
                    field: 'check',
                    title:'选择',
                    width:80,
                    templet: function(d) {
                        if(d.rowCheck){
                            return '<input class="checkbox-disabled" type="checkbox" name="layTableCheckbox" lay-skin="primary" disabled>';
                        }
                        return '<input type="checkbox" name="layTableCheckbox" lay-skin="primary">';
                    }
                }, {
                    title : '序号',
                    type : 'numbers',
                	unresize:true,
                    width:80
                }, {
                    field: 'configCertifiCenter',
                    title: '具体项-配券中心',
                    unresize:true
                }, {
                    field: 'goodsname',
                    title: '商品名称（商城系统）',
                    unresize:true
                }, {
                    field: 'activity',
                    title: '券名（券系统）',
                    unresize:true
                }, {
                    field: 'onsalegoodsname',
                    title: '售卖时产品名称（Prime系统）',
                    unresize:true
                }] ],
            data:tabRowData,
            done : function(res, curr, count){
                $('.layui-module-row').find('.checkbox-disabled').next().attr("title","已添加至问题券");
            }
        });

        var active = {
            getCheckData: function(){ //获取选中数据
                var	checkStatus = table.checkStatus('moduleRowTab'),
                    data = checkStatus.data;
                if(data.length == 0){
                    layer.alert("请选择券信息");
                    return false;
                }
                var checkData = "";
                for(var i = 0; i < data.length; i++){
                    var row = $(".layui-curr-collapse div[data-rowId='"+ data[i].rowId +"']");
                    if(row.length == 0){
                        checkData += data[i].rowId + ",";
                        active.addCouponRow(data[i]);
                    } else {
                        layer.msg(data[i].configCertifiCenter + "券已添加至问题券中");
                    }
                }

                var newTabRowData = [];
                for(var i = 0; i < tabRowData.length; i++){
                    if(checkData.indexOf(tabRowData[i].rowId) > -1){
                        tabRowData[i].rowCheck = true;
                        tabRowData[i].LAY_CHECKED = false;
                    }
                }

                couponTab.reload({
                    data: tabRowData
                });
            },
            saveData : function(){
                var feedBackData = new Array();
                var checkResult = true;
                $(".layui-curr-collapse .layui-colla-item").each(function(rowIndex){
                    var tableBody = $(this).find(".layui-table-body");
                    var tableId = $(this).attr("data-rowId");
                    var data = table.cache[tableId];

                    for(var i = 0; i < data.length; i++){
                        var item = data[i],
                            trIndex = item.LAY_TABLE_INDEX,
                            tr = tableBody.find('tr[data-index="'+ trIndex +'"]');

                        if(item.remark.length == 0){
                            tr.addClass("layui-border");
                            checkResult = false;
                        }

                        if(item.type.length == 0){
                            tr.addClass("layui-border");
                            checkResult = false;
                        }

                        item.imgSrc = new Array();
                        tr.find(".layui-upload-item input").each(function(index){
                            item.imgSrc[index] = JSON.parse($(this).val());
                        });

                        feedBackData.push(item);
                    }
                });

                if(!checkResult){
                    layer.msg("请检查是否输入问题描述或者选择类型！");
                } else {
                    if(feedBackData.length > 0){
						var mask = layer.msg('努力提交中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
						$.ajax({
							url: "${ctx}/activity/qa/saveQaCheck",
							type: "POST",
							data: {
								"feedBackData" : JSON.stringify(feedBackData),
								"actId" : "${actId}",
								"checkReportFile" : JSON.stringify(checkReportFile)
							},
							async : false,
							success: function (data) {
								layer.close(mask);
								if(data.success){
									layer.open({
										title: '信息',
										skin: 'layui-layer-lan',
										content: "保存成功！",
										btn:["确定"],
										closeBtn : false,
										success : function(){

										},
										btn1 : function(){
											window.close();
										}
									});
								} else {
									layer.msg("保存失败");
								}
							},
							error:function(data){
								layer.close(mask);
								layer.msg("保存失败");
							}
						});
					} else {
                        layer.msg("请添加有问题的券信息");
					}
				}
            },
            addCouponRow : function(rowData){
                var content = "";
                laytpl(dataTableTemp.innerHTML).render(rowData, function(html){
                    content = html;
                });
                var layTableId = rowData.rowId;
                viewObj.tbData = [{
                    tempId: new Date().valueOf(),
                    cpRowId : layTableId,
                    remark : "",
                    imgSrc : [],
                    type : ""
                }];

                $(".layui-curr-collapse").append(content);
                elem.init();
                var tbWidth = $(".layui-curr-collapse .layui-colla-content").width();

                var currTab = table.render({
                    elem: '#' + layTableId,
                    id: layTableId,
                    data: viewObj.tbData,
                    width: tbWidth,
                    page: false,
                    loading: true,
                    even: true, //不开启隔行背景
                    cols: [
                        [{
                            title: '序号',
                            type: 'numbers',
                            width:80
                        },
                            {
                                field: 'remark',
                                title: '问题描述',
                                edit: 'textarea',
                                width:300,
                                templet: function(d) {
                                    return "<div class='layui-cell-text'>" + (d.remark.replace(RegExp('\n', "g"),'<br/>') || '') + "</div>";
                                }
                            },
                            {
                                field: 'img',
                                title: '图片附件',
                                edit: false,
                                templet: function(d) {
                                    var content = "";
                                    laytpl(uploadImgTemp.innerHTML).render(d, function(html){
                                        content = html;
                                    });
                                    return content;
                                }
                            },
                            {
                                field: 'type',
                                title: '类型',
                                width:120,
                                templet: function(d) {
                                    var options = viewObj.renderSelectOptions(viewObj.typeData, {
                                        valueField: "value",
                                        textField: "label",
                                        selectedValue: d.type
                                    });
                                    return '<a lay-event="type"></a><select name="type" lay-filter="type"><option value="">请选择</option>' + options + '</select>';
                                }
                            },
                            {
                                field: 'tempId',
                                title: '操作',
                                align : "center",
                                width:80,
                                templet: function(d) {
                                    return '<a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="del" lay-id="' + d.tempId + '">删除</a>';
                                }
                            }
                        ]
                    ],
                    done: function(res, curr, count) {
                        $(".layui-curr-collapse .layui-colla-item .layui-form .attach-view").each(function(){
                            var rowId = "." + $(this).attr("prev-class");
                            layer.photos({photos: rowId, anim: 5});
                        });

                        $(".layui-curr-collapse div[data-rowId='"+ layTableId +"']").find(".layui-upload-btn").each(function(){
                            var _t = $(this);
                            var mask = "";
                            var dataRowId = _t.attr("data-rowId");
                            var up = upload.render({
                                elem: _t.parent(),
                                url: '${ctx}/common/uploadFtpAttach',
                                multiple: true,
                                accept:"images",
                                exts:"jpg|png|gif|bmp|jpeg",
                                acceptMime:'image/*',
                                auto : true,
                                before:function(obj){
                                    mask = layer.msg('努力上传中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
                                },
                                done : function(res){
                                    layer.close(mask);
                                    var obj = res.obj;
                                    if(obj.errcode === '0'){
                                        var item = _t.parent().parent();

                                        item.addClass(dataRowId).attr("prev-class",dataRowId);

                                        _t.parent().before('<div class="layui-upload-item"><a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a><img alt="'+ obj.originalFileName +'" src="'+ obj.filePath +'" class="layui-upload-img"></div>');
                                        previewImage(item, layer);
                                        var lastUpload = item.find(".layui-upload-item:last");
                                        var input = "<input alt='" + obj.originalFileName + "' type='hidden' value='" + JSON.stringify(obj) + "'>";
                                        lastUpload.append(input);

                                        var oldData = table.cache[layTableId];
                                        for(var i = 0, row; i < oldData.length; i++) {
                                            row = oldData[i];
                                            if(row.tempId.toString() === dataRowId) {
                                                row.imgSrc[row.imgSrc.length] = obj;
                                                $.extend(oldData[i], row);
                                                return;
                                            }
                                        }

                                        previewImage(item);
                                        layer.msg('上传成功');
                                    } else {
                                        layer.msg('上传失败');
                                    }
                                    up.config.elem.next()[0].value = '';
                                },
                                error: function(index, upload){
                                    layer.close(mask);
                                    layer.msg('上传失败');
                                    up.config.elem.next()[0].value = '';
                                }
                            });
                        });
                    }
                });

                //定义事件集合
                var colActive = {
                    addRow: function(e) { //添加一行

                        e = window.event || e;
                        if (e.stopPropagation) {
                            e.stopPropagation();      //阻止事件 冒泡传播
                        } else {
                            e.cancelBubble = true;   //ie兼容
                        }
                        var oldData = table.cache[layTableId];
                        var newRow = {
                            tempId: new Date().valueOf(),
                            cpRowId : layTableId,
                            remark : "",
                            imgSrc : [],
                            type : ""
                        };

                        oldData.push(newRow);

                        currTab.reload({
                            data: oldData
                        });
                    },
                    updateRow: function(obj) {
                        var oldData = table.cache[layTableId];
                        for(var i = 0, row; i < oldData.length; i++) {
                            row = oldData[i];
                            if(row.tempId === obj.tempId) {
                                $.extend(oldData[i], obj);
                                return;
                            }
                        }
                        currTab.reload({
                            data: oldData
                        });
                    },
                    removeEmptyTableCache: function() {
                        var oldData = table.cache[layTableId];
                        for(var i = 0, row; i < oldData.length; i++) {
                            row = oldData[i];
                            if(!row || !row.tempId) {
                                oldData.splice(i, 1); //删除一项
                            }
                            continue;
                        }
                        currTab.reload({
                            data: oldData
                        });
                    },
                    delCouponRow : function(th,e){
                        e = window.event || e;
                        if (e.stopPropagation) {
                            e.stopPropagation();      //阻止事件 冒泡传播
                        } else {
                            e.cancelBubble = true;   //ie兼容
                        }

                        layer.confirm('确定要删除券信息吗？', function(index) {
                            var tableId = $(th).attr("data-tableId");
                            $(".layui-curr-collapse div[data-rowId='"+ tableId +"']").remove();

                            for(var i = 0; i < tabRowData.length; i++){
                                if(tableId === tabRowData[i].rowId){
                                    tabRowData[i].rowCheck = false;
                                    tabRowData[i].LAY_CHECKED = false;
                                    break;
                                }
                            }
                            couponTab.reload({
                                data: tabRowData
                            });
                            colActiveByType('removeEmptyTableCache');
                            layer.close(index);
                        });
                    }
                };

                //激活事件
                var colActiveByType = function(type, arg) {
                    if(arguments.length === 2) {
                        colActive[type] ? colActive[type].call(this, arg) : '';
                    } else {
                        colActive[type] ? colActive[type].call(this) : '';
                    }
                }

                //注册按钮事件
                $(".layui-curr-collapse div[data-rowId='"+ layTableId +"']").find('.layui-add-btn[data-type]').on('click', function() {
                    var type = $(this).data('type');
                    colActiveByType(type, this);
                });

                //监听select下拉选中事件
                form.on('select(type)', function(data) {
                    var elem = data.elem; //得到select原始DOM对象
                    $(elem).prev("a[lay-event='type']").trigger("click");
                });

                //监听工具条
                table.on('tool('+ layTableId +')', function(obj) {
                    var data = obj.data,
                        event = obj.event,
                        tr = obj.tr; //获得当前行 tr 的DOM对象;

                    switch(event) {
                        case "type":
                            var select = tr.find("select[name='type']");
                            if(select) {
                                var selectedVal = select.val();
                                if(!selectedVal) {
                                    layer.tips("请选择一个分类", select.next('.layui-form-select'), {
                                        tips: [3, '#FF5722']
                                    }); //吸附提示
                                }
                                $.extend(obj.data, {
                                    'type': selectedVal
                                });
                                colActiveByType('updateRow', obj.data); //更新行记录对象
                            }
                            break;
                        case "del":
                            layer.confirm('确定要删除该行吗？', function(index) {
                                var item = tr.closest('.layui-colla-item'),
                                    tbody = tr.parent();

                                if(tbody.find('tr').length == 1){
                                    var tableId = item.find('.layui-del-btn').attr("data-tableId");
                                    $(".layui-curr-collapse div[data-rowId='"+ tableId +"']").remove();

                                    for(var i = 0; i < tabRowData.length; i++){
                                        if(tableId === tabRowData[i].rowId){
                                            tabRowData[i].rowCheck = false;
                                            tabRowData[i].LAY_CHECKED = false;
                                            break;
                                        }
                                    }
                                    couponTab.reload({
                                        data: tabRowData
                                    });
                                    colActiveByType('removeEmptyTableCache');
                                    layer.close(index);
                                } else {
                                    obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                                    colActiveByType('removeEmptyTableCache');
                                }
                                layer.close(index);
                            });
                            break;
                    }
                });
            },
            closeWindow : function(){
                window.close();
            },
            approval : function(){
                window.location.href="${ctx}/activity/qa/approval?actId=${actId}";
			}
        };

        $('.btn-active .layui-btn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });

        elem.on('collapse(ucpCollapse)', function(data){
            if(data.show){
                $(this).find(".layui-add-btn").removeClass("layui-hide");
            } else {
                $(this).find(".layui-add-btn").addClass("layui-hide");
            }
        });

        $(".layui-prev-collapse .layui-colla-item .layui-form .attach-view").each(function(){
            var rowId = "." + $(this).attr("data-rowId");
            layer.photos({photos: rowId, anim: 5});
        });

        var reportMask = null;
        var report = upload.render({
            elem: "#checkReport",
            url: '${ctx}/common/uploadFtpAttach',
            accept:"file",
            auto : true,
            field : "file",
            before:function(obj){
                reportMask = layer.msg('努力上传中', {icon: 16, time: 0, shade: [0.8, '#393D49']});
            },
            done : function(res){
                layer.close(reportMask);
                var obj = res.obj;
                console.info(obj);
                if(obj.errcode === '0'){
                    if(checkReportFile.length > 0){
                        uploadImgDelete(checkReportFile[0].filePath);
                    }

					$("#reportFilePrew").html(obj.originalFileName);
                    checkReportFile[0] = obj;
                    layer.msg('上传成功');
                } else {
                    layer.msg('上传失败');
                }
                report.config.elem.next()[0].value = '';
            },
            error: function(index, upload){
                layer.close(reportMask);
                layer.msg('上传失败');
                report.config.elem.next()[0].value = '';
            }
        });
    });

    function previewImage(item, layph){
        var cssName = "." + item.attr("prev-class");
        layph.photos({
            photos: cssName,
            anim: 5
        });
    }

    function uploadImgDelete(obj){
        var filePath = $(obj).parent().find("img").attr("src");
        $.ajax({
            url: "${ctx}/common/delFtpAttach",
            type: "POST",
            data: {
                "filePath" : filePath
            },
            success: function (data) {
            }
        });
        $(obj).parent().remove();
    }
</script>

<script type="text/html" id="dataTableTemp">
	<div class="layui-colla-item" data-rowId="{{d.rowId}}">
		<h2 class="layui-colla-title">
			{{d.configCertifiCenter}}
			<span style="float: right;">
						<button type="button" class="layui-btn layui-btn-sm layui-add-btn" data-type="addRow" title="添加行"><span class="layui-plus">+</span> 添加行</button>
						<button type="button" class="layui-btn layui-btn-sm layui-add-btn layui-del-btn" data-tableId="{{d.rowId}}" data-type="delCouponRow" title="删除券"><span class="layui-plus">-</span> 删除</button>
					</span>
		</h2>
		<div class="layui-colla-content layui-show">
			<table id="{{d.rowId}}" lay-filter="{{d.rowId}}" class="layui-hide"></table>
		</div>
	</div>
</script>

<script type="text/html" id="uploadImgTemp">
	<div class="layui-item-value layui-label-color">
		<div class="layui-upload-list">
			<div class="layer-photos attach-view {{d.tempId}}" prev-class="{{d.tempId}}">
				{{#  layui.each(d.imgSrc, function(index, item){ }}
				<div class="layui-upload-item">
					<a class="layui-upload-img-del" onclick="uploadImgDelete(this);"></a>
					<img alt="{{item.originalFileName}}" src="{{item.filePath}}" class="layui-upload-img">
					<input type="hidden" value='{"fileName":"{{item.fileName}}","filePath":"{{item.filePath}}","originalFileName":"{{item.originalFileName}}","fileSize":{{item.fileSize}},"ext":"{{item.ext}}"}'/>
				</div>
				{{#  }); }}
				<div class="upload_btn">
					<button type="button" data-rowId={{d.tempId}} class="layui-upload-btn"></button>
				</div>
			</div>
		</div>
	</div>
</script>
</body>
</html>
